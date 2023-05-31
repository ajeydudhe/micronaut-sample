package org.expedientframework.user;

import io.micronaut.aot.core.AOTCodeGenerator;
import io.micronaut.aot.core.AOTContext;
import io.micronaut.aot.core.AOTModule;
import io.micronaut.aot.core.Option;
import io.micronaut.aot.core.codegen.AbstractCodeGenerator;
import io.micronaut.context.BeanDefinitionRegistry;
import io.micronaut.context.BeanRegistration;
import io.micronaut.core.beans.BeanIntrospectionReference;
import io.micronaut.core.beans.BeanIntrospector;
import io.micronaut.core.io.scan.BeanIntrospectionScanner;
import io.micronaut.core.io.service.SoftServiceLoader;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.inject.BeanDefinition;
import io.micronaut.inject.BeanDefinitionReference;
import io.micronaut.inject.qualifiers.Qualifiers;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collection;
import java.util.ServiceLoader;
import java.util.function.Predicate;

@AOTModule(
    id = MyResourceGenerator.ID,
    options = {
        @Option(key = "greeter.message", sampleValue = "Hello, world!", description = "The message to write")
    }
)
public class MyResourceGenerator extends AbstractCodeGenerator {
    private static final Logger log = LoggerFactory.getLogger(MyResourceGenerator.class);
    
    public static final String ID = "my.resource.generator";

    @Override
    public void generate(final AOTContext context) {
        log.error("### Ajey ==> MyResourceGenerator.generate() called...{}", new BeanIntrospectionScanner().scan("io.micronaut.http.client.annotation.Client", ""));
        BeanIntrospector.SHARED.findIntrospectedTypes(beanIntrospectionReference -> {
            log.error("### Ajey ==> beanIntrospectionReference = {}", beanIntrospectionReference);
            return false;
        }).forEach(c -> log.error("### Ajey ==> class = {}", c));
            
        context.getAnalyzer().getApplicationContext().findBeanDefinition(Object.class, Qualifiers.any());
        SoftServiceLoader<BeanDefinitionReference> loader = SoftServiceLoader.load(io.micronaut.inject.BeanDefinitionReference.class);
        loader.forEach(c -> log.error("### Ajey ==> BeanDefinitionReference = {} : {}", c.getName(), c.isPresent(), c.load()));
        
        System.out.println("### Ajey ==> MyResourceGenerator.generate() called...");
        context.registerGeneratedResource("hello.txt", file -> {
            try (PrintWriter writer = new PrintWriter(file)) {
                final String message = context.getConfiguration().mandatoryValue("greeter.message");
                writer.println(message);
            } catch (Exception e) {
                log.error("An error occurred.", e);
            }
        });
    }
}
