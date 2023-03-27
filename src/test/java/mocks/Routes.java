package mocks;

import org.openqa.selenium.remote.http.Contents;
import org.openqa.selenium.remote.http.HttpMethod;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.Route;
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

                    String headers = StreamSupport.stream(request.getHeaderNames().spliterator(), false)
                            .map(name -> name + ":" + request.getHeader(name))
                            .collect(Collectors.joining(System.lineSeparator()));

                    log.info("Request => method: {}" + System.lineSeparator() + "url: {}" + System.lineSeparator() + "headers: {}" + System.lineSeparator() + "body: {}",
                            request.getMethod(),
                            request.getUri(),
                            headers,
                            Contents.utf8String(request.getContent()));

                    HttpResponse response = new HttpResponse();

                    return response.setStatus(418)
                            .addHeader( "Access-Control-Allow-Origin", "*")
                            .setContent(request.getContent());
                });
    }
}
