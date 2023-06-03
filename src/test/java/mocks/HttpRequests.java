package mocks;

import org.openqa.selenium.remote.http.HttpMethod;
import org.openqa.selenium.remote.http.HttpRequest;

import java.util.function.Predicate;

public class HttpRequests {
    public static Predicate<HttpRequest> post(String uri) {
        return request -> HttpMethod.POST == request.getMethod() && request.getUri().contains(uri);
    }

    public static Predicate<HttpRequest> get(String uri) {
        return request -> HttpMethod.GET == request.getMethod() && request.getUri().contains(uri);
    }
}
