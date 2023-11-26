package wdm;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.*;
import io.github.bonigarcia.wdm.cache.ResolutionCache;
import io.github.bonigarcia.wdm.config.Config;
import io.github.bonigarcia.wdm.docker.DockerContainer;
import io.github.bonigarcia.wdm.docker.DockerService;
import io.github.bonigarcia.wdm.online.HttpClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyDockerService extends DockerService {

    public MyDockerService(Config config, HttpClient httpClient, ResolutionCache resolutionCache) {
        super(config, httpClient, resolutionCache);
    }

    @Override
    public synchronized String startContainer(DockerContainer dockerContainer) throws DockerException {
        return super.startContainer(this.customize(dockerContainer));
    }

    private DockerContainer customize(DockerContainer dockerContainer) {
        return DockerContainer.dockerBuilder(dockerContainer.getImageId())
                .binds(this.getBinds(dockerContainer))
                .cmd(dockerContainer.getCmd().orElse(null))
                .envs(dockerContainer.getEnvs().orElse(null))
                .exposedPorts(this.addPort(dockerContainer.getExposedPorts(), 7070))
                .entryPoint(dockerContainer.getEntryPoint().orElse(null))
                .network(dockerContainer.getNetwork().orElse(null))
                .extraHosts(Arrays.stream(dockerContainer.getExtraHosts()).collect(Collectors.toList()))
                .shmSize(dockerContainer.getShmSize().orElse(0L))
                .mounts(null)
                .build();
    }

    private List<String> getBinds(DockerContainer dockerContainer) {
        return dockerContainer.getBinds()
                .map(binds -> binds.stream().map(Bind::toString).collect(Collectors.toList()))
                .orElse(null);
    }

    private List<String> addPort(List<String> ports, int port) {
        List<String> copy = new ArrayList<>(ports);
        copy.add(String.valueOf(port));

        return copy;
    }
}
