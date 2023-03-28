package mocks;

import org.openqa.selenium.remote.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Routes {

    private static final Logger log = LoggerFactory.getLogger(Routes.class);

    public static Route returnIamTeapot() {
        return Route.matching(request -> HttpMethod.POST == request.getMethod()
                        && request.getUri().contains("example.form.io/example/submission"))
                .to(() -> request -> {

                    log.info(createLogMessage(request));

                    return new HttpResponse().setStatus(418)
                            .addHeader( "Access-Control-Allow-Origin", "*")
                            .setContent(request.getContent());
                });
    }

    private static String createLogMessage(HttpRequest request) {
        return String.format("Request => method: %s, url: %s %s%s %s%s %s",
                request.getMethod(),
                request.getUri(),
                System.lineSeparator(),
                getHeaders(request),
                System.lineSeparator(),
                System.lineSeparator(),
                Contents.utf8String(request.getContent()));
    }

    private static String getHeaders(HttpRequest request) {
        return StreamSupport.stream(request.getHeaderNames().spliterator(), false)
                .map(name -> name + ":" + request.getHeader(name))
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
