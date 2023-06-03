package wdm;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.*;
import io.github.bonigarcia.wdm.cache.ResolutionCache;
import io.github.bonigarcia.wdm.config.Config;
import io.github.bonigarcia.wdm.docker.DockerContainer;
import io.github.bonigarcia.wdm.docker.DockerService;
import io.github.bonigarcia.wdm.online.HttpClient;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

//poor man's WA to expose selenoid dev tools port in container
public class MyDockerService extends DockerService {

    public MyDockerService(Config config, HttpClient httpClient, ResolutionCache resolutionCache) {
        super(config, httpClient, resolutionCache);
    }

    @Override
    public synchronized String startContainer(DockerContainer dockerContainer) throws DockerException {

        final Logger log = getLogger(lookup().lookupClass());

        String imageId = dockerContainer.getImageId();
        log.info("Starting Docker container {}", imageId);
        HostConfig hostConfigBuilder = new HostConfig();
        String containerId;

        try (CreateContainerCmd containerConfigBuilder = getDockerClient().createContainerCmd(imageId)) {

            boolean privileged = dockerContainer.isPrivileged();
            if (privileged) {
                log.trace("Using privileged mode");
                hostConfigBuilder.withPrivileged(true);
            }
            if (dockerContainer.isSysadmin()) {
                log.trace("Adding sysadmin capability");
                hostConfigBuilder.withCapAdd(Capability.SYS_ADMIN);
            }

            Optional<Long> shmSize = dockerContainer.getShmSize();
            if (shmSize.isPresent()) {
                log.trace("Using shm size: {}", shmSize.get());
                hostConfigBuilder.withShmSize(shmSize.get());
            }

            Optional<String> network = dockerContainer.getNetwork();
            if (network.isPresent()) {
                log.trace("Using network: {}", network.get());
                hostConfigBuilder.withNetworkMode(network.get());
            }
            List<String> exposedPorts = dockerContainer.getExposedPorts();

            //expose selenoid dev tools port in container
            exposedPorts.add("7070");

            if (!exposedPorts.isEmpty()) {
                log.trace("Using exposed ports: {}", exposedPorts);
                containerConfigBuilder.withExposedPorts(exposedPorts.stream()
                        .map(ExposedPort::parse).collect(Collectors.toList()));
                hostConfigBuilder.withPortBindings(exposedPorts.stream()
                        .map(PortBinding::parse).collect(Collectors.toList()));
                hostConfigBuilder.withPublishAllPorts(true);
            }
            Optional<List<Bind>> binds = dockerContainer.getBinds();
            if (binds.isPresent()) {
                log.trace("Using binds: {}", binds.get());
                hostConfigBuilder.withBinds(binds.get());
            }

            Optional<List<Mount>> mounts = dockerContainer.getMounts();
            if (mounts.isPresent()) {
                log.trace("Using mounts: {}", mounts.get());
                hostConfigBuilder.withMounts(mounts.get());
            }

            Optional<List<String>> envs = dockerContainer.getEnvs();
            if (envs.isPresent()) {
                log.trace("Using envs: {}", envs.get());
                containerConfigBuilder
                        .withEnv(envs.get().toArray(new String[] {}));
            }
            Optional<List<String>> cmd = dockerContainer.getCmd();
            if (cmd.isPresent()) {
                log.trace("Using cmd: {}", cmd.get());
                containerConfigBuilder
                        .withCmd(cmd.get().toArray(new String[] {}));
            }
            Optional<List<String>> entryPoint = dockerContainer.getEntryPoint();
            if (entryPoint.isPresent()) {
                log.trace("Using entryPoint: {}", entryPoint.get());
                containerConfigBuilder.withEntrypoint(
                        entryPoint.get().toArray(new String[] {}));
            }

            hostConfigBuilder.withExtraHosts(dockerContainer.getExtraHosts());

            containerId = containerConfigBuilder
                    .withHostConfig(hostConfigBuilder).exec().getId();

            getDockerClient().startContainerCmd(containerId).exec();
        }

        return containerId;

    }
}
