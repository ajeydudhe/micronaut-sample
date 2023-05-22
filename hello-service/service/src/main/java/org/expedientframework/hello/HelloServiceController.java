package org.expedientframework.hello;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import org.expedientframework.user.UserServiceClient;

@Controller("/greet")
public class HelloServiceController {
    @Inject
    private UserServiceClient userServiceClient;
    
    @Get("/{userId}")
    public String greet(final long userId) {
        return "Hello " + userServiceClient.getUser(userId);
    }
}
