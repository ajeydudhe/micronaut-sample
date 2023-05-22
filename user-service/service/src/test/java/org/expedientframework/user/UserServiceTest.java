package org.expedientframework.user;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest
class UserServiceTest {

    @Inject
    EmbeddedApplication<?> application;
    @Inject
    UserServiceClient userServiceClient;
    
    @Test
    void testItWorks() {
        assertThat(application.isRunning()).isTrue();
        assertThat(userServiceClient.getUser(1234567890)).as("getUser").isEqualTo("User 1234567890");
    }
}
