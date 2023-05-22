package org.expedientframework.hello;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client(id = "helloService", path = "/greet")
public interface HelloServiceClient {
    @Get("/{userId}")
    String greet(long userId);
}
