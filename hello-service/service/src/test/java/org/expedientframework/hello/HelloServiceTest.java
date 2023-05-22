package org.expedientframework.hello;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.expedientframework.user.UserServiceClient;
import org.junit.jupiter.api.Test;

@MicronautTest
class HelloServiceTest {

    @Inject
    EmbeddedApplication<?> application;
    @Inject
    HelloServiceClient helloServiceClient;
    @Inject
    UserServiceClient userServiceClient;
    
    @Test
    void testItWorks() {
        assertThat(application.isRunning()).isTrue();
        assertThat(userServiceClient).as("userClient").isNotNull();
        assertThat(userServiceClient.getUser(1234567890)).as("user").isEqualTo("User 1234567890");
        assertThat(helloServiceClient.greet(789)).as("greeting").isEqualTo("Hello User 789");
    }
    
    @MockBean(UserServiceClient.class)
    public UserServiceClient mockUserClient() {
        return userId -> "User " + userId;
    }
}
