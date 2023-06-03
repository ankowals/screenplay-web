package mocks;

import org.openqa.selenium.remote.http.Contents;
import org.openqa.selenium.remote.http.HttpHandler;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Mock {

    public static final int I_AM_A_TEA_POT = 418;

    public HttpHandler response(int statusCode) {
        return req -> new HttpResponse().setStatus(statusCode)
            .addHeader( "Access-Control-Allow-Origin", "*")
            .setContent(req.getContent());
    }

    private String createLogMessage(HttpRequest request) {
        return String.format("Request => method: %s, url: %s %s%s %s%s %s",
                request.getMethod(),
                request.getUri(),
                System.lineSeparator(),
                this.getHeaders(request),
                System.lineSeparator(),
                System.lineSeparator(),
                Contents.utf8String(request.getContent()));
    }

    private String getHeaders(HttpRequest request) {
        return StreamSupport.stream(request.getHeaderNames().spliterator(), false)
                .map(name -> name + ":" + request.getHeader(name))
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
