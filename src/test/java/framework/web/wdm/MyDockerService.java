package framework.web.wdm;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.*;
import io.github.bonigarcia.wdm.cache.ResolutionCache;
import io.github.bonigarcia.wdm.config.Config;
import io.github.bonigarcia.wdm.docker.DockerContainer;
import io.github.bonigarcia.wdm.docker.DockerService;
import io.github.bonigarcia.wdm.online.HttpClient;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/*
Customization done below allows us to run containers in BB pipelines and use features provided by DevTools driver in CI
Following docker flags are restricted from usage in bitbucket pipelines
   --privileged
   --cap-add
   --mount
For details see https://support.atlassian.com/bitbucket-cloud/docs/run-docker-commands-in-bitbucket-pipelines/

When Selenium wants to access a web app running bitbucket pipelines use host.docker.internal:port
instead of localhost:port when calling webdriver.get(url)
For details see https://community.atlassian.com/t5/Bitbucket-articles/Changes-to-make-your-containers-more-secure-on-Bitbucket/ba-p/998464
For the browser to access files either mount bitbucket clone dir to the container and access them from there or use official selenium images and set FileDetector in driver
More details under //https://github.com/aerokube/selenoid/issues/1079
*/
public class MyDockerService extends DockerService {

    private static final Map<String, DockerContainer> PROXY_CONTAINER_CACHE = new ConcurrentHashMap<>();

    private final Config config;

    public MyDockerService(Config config, HttpClient httpClient, ResolutionCache resolutionCache) {
        super(config, httpClient, resolutionCache);
        this.config = config;
    }

    @Override
    public synchronized String startContainer(DockerContainer dockerContainer) throws DockerException {
        return super.startContainer(this.customize(dockerContainer));
    }

    @Override
    public synchronized void stopAndRemoveContainer(DockerContainer dockerContainer) {
        String containerId = dockerContainer.getContainerId();

        Optional.ofNullable(PROXY_CONTAINER_CACHE.get(containerId)).ifPresent(
                proxyContainer -> {
                    PROXY_CONTAINER_CACHE.remove(containerId);
                    super.stopAndRemoveContainer(proxyContainer);
        });

        super.stopAndRemoveContainer(dockerContainer);
    }

    @Override
    public DockerContainer startBrowserContainer(String dockerImage, String cacheKey, String browserVersion, boolean androidEnabled) {
        DockerContainer browserContainer = super.startBrowserContainer(dockerImage,
                cacheKey,
                browserVersion,
                androidEnabled);

        if (dockerImage.contains("selenoid")) {
            try {
                DockerContainer proxyContainer = this.startProxyContainer(browserContainer);

                String port = this.getBindPort(proxyContainer.getContainerId(),
                        String.format("%s/tcp", this.config.getDockerBrowserPort()));

                String url = browserContainer.getContainerUrl()
                        .replaceAll(":\\d+/", String.format(":%s/", port));

                browserContainer.setContainerUrl(url);
            } catch(URISyntaxException e){
                throw new RuntimeException(e);
            }
        }

        return browserContainer;
    }

    //we can expose 7070 for selenoid devTools only here and configure caddy to route the traffic
    //but still augmenter needs to extract this port from container
    //and configure the client to trigger it
    private DockerContainer startProxyContainer(DockerContainer browserContainer) throws URISyntaxException {
        String dockerImage = "caddy:2.8.4-alpine";
        String cacheKey = dockerImage.split(":")[0];
        String imageVersion = dockerImage.split(":")[1];

        this.pullImageIfNecessary(cacheKey, dockerImage, imageVersion);

        DockerContainer proxyContainer = DockerContainer.dockerBuilder(dockerImage)
                .envs(List.of(
                        "BROWSER_CONTAINER_IP=" + browserContainer.getAddress(),
                        "BROWSER_CONTAINER_PORT=" + this.config.getDockerBrowserPort()))
                .network(browserContainer.getNetwork().orElse(null))
                .binds(List.of(
                        String.format(
                                "%s:/etc/caddy/Caddyfile",
                                this.getProxyConfig())))
                .extraHosts(Arrays.stream(browserContainer.getExtraHosts()).toList())
                .exposedPorts(List.of(String.valueOf(this.config.getDockerBrowserPort())))
                .build();

        String containerId = this.startContainer(proxyContainer);
        proxyContainer.setContainerId(containerId);

        PROXY_CONTAINER_CACHE.putIfAbsent(browserContainer.getContainerId(), proxyContainer);

        return proxyContainer;
    }

    private DockerContainer customize(DockerContainer dockerContainer) {
        return DockerContainer.dockerBuilder(dockerContainer.getImageId())
                .binds(this.getBinds(dockerContainer))
                .cmd(dockerContainer.getCmd().orElse(null))
                .envs(dockerContainer.getEnvs().orElse(null))
                .exposedPorts(this.addPort(dockerContainer.getExposedPorts(), 7070))
                .entryPoint(dockerContainer.getEntryPoint().orElse(null))
                .network(dockerContainer.getNetwork().orElse(null))
                .extraHosts(Arrays.stream(dockerContainer.getExtraHosts()).toList())
                .shmSize(dockerContainer.getShmSize().orElse(0L))
                .mounts(null)
                .build();
    }

    private List<String> getBinds(DockerContainer dockerContainer) {
        return dockerContainer.getBinds()
                .map(binds -> binds.stream().map(Bind::toString).toList())
                .orElse(null);
    }

    private List<String> addPort(List<String> ports, int port) {
        List<String> copy = new ArrayList<>(ports);
        copy.add(String.valueOf(port));

        return copy;
    }

    private List<String> addBitbucketCloneDir(List<String> originalBinds) {
        List<String> binds = new ArrayList<>(originalBinds);

        String bitBucketCloneDir = System.getenv("BITBUCKET_CLONE_DIR");

        if (bitBucketCloneDir != null) {
            binds.add(String.format("%s:/home/selenium/clone", bitBucketCloneDir));
        }

        return binds;
    }

    private List<String> addBitbucketHost(List<String> originalHosts) {
        List<String> hosts = new ArrayList<>(originalHosts);

        String bitBucketHost = System.getenv("BITBUCKET_DOCKER_HOST_INTERNAL");

        if (bitBucketHost != null) {
            hosts.add("host.docker.internal:" + bitBucketHost);
        }

        return hosts;
    }
    //does not work in jar
    private Path getProxyConfig() throws URISyntaxException {
        URL url = this.getClass().getClassLoader().getResource("caddyfile");
        return new File(Objects.requireNonNull(url).toURI()).toPath();
    }
}