package framework.web.wdm.docker;

import io.github.bonigarcia.wdm.cache.ResolutionCache;
import io.github.bonigarcia.wdm.config.Config;
import io.github.bonigarcia.wdm.docker.DockerContainer;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/*
Customization below allows to use Selenoid images with devTools/BiDi and LocalFileDetector
Caddy is used as a proxy container which can modify paths when required
*/

@Deprecated(forRemoval = true)
public class SelenoidDockerService extends BitbucketDockerService {

  private static final Map<String, DockerContainer> PROXY_CONTAINER_CACHE =
      new ConcurrentHashMap<>();

  private final Config config;

  public SelenoidDockerService(Config config, ResolutionCache resolutionCache) {
    super(config, resolutionCache);
    this.config = config;
  }

  @Override
  public synchronized void stopAndRemoveContainer(DockerContainer dockerContainer) {
    String containerId = dockerContainer.getContainerId();

    Optional.ofNullable(PROXY_CONTAINER_CACHE.get(containerId))
        .ifPresent(
            proxyContainer -> {
              PROXY_CONTAINER_CACHE.remove(containerId);
              super.stopAndRemoveContainer(proxyContainer);
            });

    super.stopAndRemoveContainer(dockerContainer);
  }

  @Override
  public DockerContainer startBrowserContainer(
      String dockerImage, String cacheKey, String browserVersion) {
    DockerContainer browserContainer =
        super.startBrowserContainer(dockerImage, cacheKey, browserVersion);

    try {
      DockerContainer proxyContainer = this.startProxyContainer(browserContainer);

      String port =
          this.getBindPort(
              proxyContainer.getContainerId(),
              String.format("%s/tcp", this.config.getDockerBrowserPort()));

      String url =
          browserContainer.getContainerUrl().replaceAll(":\\d+/", String.format(":%s/", port));

      browserContainer.setContainerUrl(url);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }

    return browserContainer;
  }

  @Override
  protected DockerContainer customize(DockerContainer dockerContainer) {
    return super.toBuilder(dockerContainer)
        .exposedPorts(this.addPort(dockerContainer.getExposedPorts(), 7070))
        .build();
  }

  // we can expose 7070 for selenoid devTools only here and configure caddy to route the traffic
  // but still augmenter needs to extract this port from container
  // and configure the client to trigger it
  private DockerContainer startProxyContainer(DockerContainer browserContainer)
      throws URISyntaxException {
    String dockerImage = "caddy:2.8.4-alpine";
    String cacheKey = dockerImage.split(":")[0];
    String imageVersion = dockerImage.split(":")[1];

    super.pullImageIfNecessary(cacheKey, dockerImage, imageVersion);

    DockerContainer proxyContainer =
        DockerContainer.dockerBuilder(dockerImage)
            .envs(
                List.of(
                    "BROWSER_CONTAINER_IP=" + browserContainer.getAddress(),
                    "BROWSER_CONTAINER_PORT=" + this.config.getDockerBrowserPort()))
            .network(browserContainer.getNetwork().orElse(null))
            .binds(List.of(String.format("%s:/etc/caddy/Caddyfile", this.getProxyConfig())))
            .extraHosts(Arrays.stream(browserContainer.getExtraHosts()).toList())
            .exposedPorts(List.of(String.valueOf(this.config.getDockerBrowserPort())))
            .build();

    String containerId = super.startContainer(proxyContainer);
    proxyContainer.setContainerId(containerId);

    PROXY_CONTAINER_CACHE.putIfAbsent(browserContainer.getContainerId(), proxyContainer);

    return proxyContainer;
  }

  private List<String> addPort(List<String> ports, int port) {
    List<String> copy = new ArrayList<>(ports);
    copy.add(String.valueOf(port));

    return copy;
  }

  // does not work in jar
  private Path getProxyConfig() throws URISyntaxException {
    URL url = this.getClass().getClassLoader().getResource("caddyfile");
    return new File(Objects.requireNonNull(url).toURI()).toPath();
  }
}
