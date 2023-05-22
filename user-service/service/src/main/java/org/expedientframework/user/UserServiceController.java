package org.expedientframework.user;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/users")
public class UserServiceController {
    @Get("/{userId}")
    public String getUser(final long userId) {
        return "User " + userId;
    }
}
