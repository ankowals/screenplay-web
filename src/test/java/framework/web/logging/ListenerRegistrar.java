package framework.web.logging;

import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v107.log.Log;
import org.openqa.selenium.devtools.v107.log.model.LogEntry;
import org.openqa.selenium.devtools.v107.network.Network;
import org.openqa.selenium.devtools.v107.network.model.Request;
import org.openqa.selenium.devtools.v107.network.model.ResourceType;
import org.openqa.selenium.devtools.v107.network.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/*
Support may vary between different drivers
 */
public class ListenerRegistrar {

    private static final Logger log = LoggerFactory.getLogger(ListenerRegistrar.class);

    private final DevTools devTools;

    ListenerRegistrar(DevTools devTools) {
        this.devTools = devTools;
    }

    public ListenerRegistrar addNetworkRequestListener() {
        devTools.addListener(Network.requestWillBeSent(), entry -> {
            Request request = entry.getRequest();
            if (entry.getType().equals(Optional.of(ResourceType.FETCH))) {
                if (request.getPostData().isPresent()) {
                    log.info("[{}] Request with URL : {} : With body : {}",
                            request.getMethod(),
                            request.getUrl(),
                            request.getPostData().get());
                } else {
                    log.info("[{}] Request with URL : {}",
                            request.getMethod(),
                            request.getUrl());
                }
            }
        });

        return this;
    }

    public ListenerRegistrar addNetworkResponseListener() {
        devTools.addListener(Network.responseReceived(), entry -> {
            Response response = entry.getResponse();
            if (entry.getType().equals(ResourceType.FETCH) || entry.getType().equals(ResourceType.XHR)) {
                if (response.getStatus() >= 400) {
                    log.error("Response with URL : {} : With status code : {}",
                            response.getUrl(),
                            response.getStatus());
                } else {
                    log.info("Response with URL : {} : With status code : {}",
                            response.getUrl(),
                            response.getStatus());
                }
            }
        });

        return this;
    }

    public ListenerRegistrar addLogListener() {
        devTools.addListener(Log.entryAdded(), entry -> {
            if (entry.getLevel().equals(LogEntry.Level.ERROR)) {
                log.error("[LOG.ERROR] Entry added with text: {}", entry.getText());
                if (entry.getStackTrace().isPresent()) {
                    log.error("[LOG.ERROR]\tWith stack trace : {}", entry.getStackTrace().get());
                }
            }
        });

        return this;
    }

    public ListenerRegistrar addJavascriptExceptionListener() {
        devTools.getDomains()
                .events()
                .addJavascriptExceptionListener(e -> {
                    log.error("Java script exception occurred : {}", e.getMessage());
                    e.printStackTrace();
                });

        return this;
    }
}
