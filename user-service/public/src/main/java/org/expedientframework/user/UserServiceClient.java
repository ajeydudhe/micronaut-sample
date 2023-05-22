package org.expedientframework.user;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Singleton;

@Singleton
@Client(id = "userService", path = "/users")
public interface UserServiceClient {
    @Get("/{userId}")
    String getUser(long userId);
}
