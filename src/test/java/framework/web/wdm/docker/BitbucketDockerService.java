package framework.web.wdm.docker;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.*;
import io.github.bonigarcia.wdm.cache.ResolutionCache;
import io.github.bonigarcia.wdm.config.Config;
import io.github.bonigarcia.wdm.docker.DockerContainer;
import io.github.bonigarcia.wdm.docker.DockerService;
import java.util.*;

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
*/
public class BitbucketDockerService extends DockerService {

  public BitbucketDockerService(Config config, ResolutionCache resolutionCache) {
    super(config, resolutionCache);
  }

  @Override
  public synchronized String startContainer(DockerContainer dockerContainer)
      throws DockerException {
    return super.startContainer(this.customize(dockerContainer));
  }

  protected DockerContainer customize(DockerContainer dockerContainer) {
    return this.toBuilder(dockerContainer).build();
  }

  protected DockerContainer.DockerBuilder toBuilder(DockerContainer dockerContainer) {
    return DockerContainer.dockerBuilder(dockerContainer.getImageId())
        .containerName(dockerContainer.getContainerName().orElse(null))
        .binds(this.addBitbucketCloneDir(this.getBinds(dockerContainer)))
        .cmd(dockerContainer.getCmd().orElse(null))
        .envs(dockerContainer.getEnvs().orElse(null))
        .exposedPorts(dockerContainer.getExposedPorts())
        .entryPoint(dockerContainer.getEntryPoint().orElse(null))
        .network(dockerContainer.getNetwork().orElse(null))
        .extraHosts(this.addBitbucketHost(Arrays.stream(dockerContainer.getExtraHosts()).toList()))
        .shmSize(dockerContainer.getShmSize().orElse(0L))
        .mounts(null);
  }

  private List<String> getBinds(DockerContainer dockerContainer) {
    return dockerContainer
        .getBinds()
        .map(binds -> binds.stream().map(Bind::toString).toList())
        .orElse(null);
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
}
