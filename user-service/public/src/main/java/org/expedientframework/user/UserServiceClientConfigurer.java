package org.expedientframework.user;

import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.ApplicationContextConfigurer;
import io.micronaut.context.annotation.ContextConfigurer;
import org.expedientframework.common.ServiceClientConfigurer;

// We cannot have a base class implementing ApplicationContextConfigurer and then extend that.
// With above the class does not get loaded for configuration. Hence, using a utility method.
@ContextConfigurer
public class UserServiceClientConfigurer implements ApplicationContextConfigurer {
    @Override
    public void configure(final ApplicationContextBuilder builder) {
        ServiceClientConfigurer.configure(builder, UserServiceClient.class);
    }
}
