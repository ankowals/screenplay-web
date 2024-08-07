package framework.web.wdm;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.*;
import io.github.bonigarcia.wdm.cache.ResolutionCache;
import io.github.bonigarcia.wdm.config.Config;
import io.github.bonigarcia.wdm.docker.DockerContainer;
import io.github.bonigarcia.wdm.docker.DockerService;
import io.github.bonigarcia.wdm.online.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/*
To allow to run containers in for example Bitbucket we need to remove restricted options
from container start command.
To allow to use selenoid images and file uploads we need to re-write paths using reverse proxy.
Client should trigger proxy and not browser container directly.
We do not use host names so no need to setup a dedicated docker network.
*/
public class MyDockerService extends DockerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyDockerService.class);
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
    public DockerContainer startBrowserContainer(String dockerImage, String cacheKey, String browserVersion, boolean androidEnabled) {
        DockerContainer browserContainer;

        if (dockerImage.contains("selenoid")) {
            browserContainer = super.startBrowserContainer(dockerImage,
                    cacheKey,
                    browserVersion,
                    androidEnabled);

            try {
                Path proxyConfig = this.getProxyConfig("caddyfile");
                DockerContainer proxyContainer = this.startProxyContainer(browserContainer, proxyConfig);

                String port = this.getBindPort(proxyContainer.getContainerId(), String.format("%s/tcp", this.config.getDockerBrowserPort()));
                String url = browserContainer.getContainerUrl().replaceAll(":[0-9]+/", String.format(":%s/", port));

                browserContainer.setContainerUrl(url);
            } catch(URISyntaxException e){
                throw new RuntimeException(e);
            }

        } else {
            browserContainer = super.startBrowserContainer(dockerImage,
                    cacheKey,
                    browserVersion,
                    androidEnabled);
        }

        LOGGER.info("Proxy container started");
        return browserContainer;
    }

    public Optional<DockerContainer> getProxyContainer(String browserContainerId) {
        Optional<DockerContainer> maybeProxyContainer = Optional.ofNullable(
                MyDockerService.PROXY_CONTAINER_CACHE.get(browserContainerId));

        if (maybeProxyContainer.isPresent()) {
            MyDockerService.PROXY_CONTAINER_CACHE.remove(browserContainerId);
        }

        return maybeProxyContainer;
    }

    //we can expose 7070 for selenoid devTools only here and configure caddy to route the traffic
    //but still augmenter needs to extract this port from container
    //and configure the client to trigger it
    private DockerContainer startProxyContainer(DockerContainer browserContainer, Path proxyConfig) {
        String dockerImage = "caddy:2.8.4-alpine";
        String cacheKey = dockerImage.split(":")[0];
        String imageVersion = dockerImage.split(":")[1];

        this.pullImageIfNecessary(cacheKey, dockerImage, imageVersion);

        DockerContainer proxyContainer = DockerContainer.dockerBuilder(dockerImage)
                .envs(List.of("BROWSER_CONTAINER_IP=" + browserContainer.getAddress(),
                        "BROWSER_CONTAINER_PORT=" + this.config.getDockerBrowserPort()))
                .network(browserContainer.getNetwork().orElse(null))
                .binds(List.of(String.format("%s:/etc/caddy/Caddyfile", proxyConfig)))
                .extraHosts(Arrays.stream(browserContainer.getExtraHosts()).toList())
                .exposedPorts(List.of(String.valueOf(this.config.getDockerBrowserPort())))
                .build();

        String containerId = this.startContainer(proxyContainer);
        proxyContainer.setContainerId(containerId);

        PROXY_CONTAINER_CACHE.putIfAbsent(browserContainer.getContainerId(), proxyContainer);

        return proxyContainer;
    }

    //does not work in jar
    private Path getProxyConfig(String resourcePath) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(resourcePath);
        return new File(resource.toURI()).toPath();
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
}
