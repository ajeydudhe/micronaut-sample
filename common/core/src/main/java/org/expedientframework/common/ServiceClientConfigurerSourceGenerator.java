package org.expedientframework.common;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import io.micronaut.aot.core.AOTContext;
import io.micronaut.aot.core.AOTModule;
import io.micronaut.aot.core.codegen.AbstractCodeGenerator;
import io.micronaut.aot.core.codegen.AbstractSingleClassFileGenerator;
import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.ApplicationContextConfigurer;
import io.micronaut.context.annotation.ContextConfigurer;
import io.micronaut.core.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import javax.lang.model.element.Modifier;

@AOTModule(
    id = ServiceClientConfigurerSourceGenerator.ID,
    description = "Code generator for http service client configuration initialization" 
)
public class ServiceClientConfigurerSourceGenerator extends AbstractCodeGenerator {
    private static final Logger log = LoggerFactory.getLogger(ServiceClientConfigurerSourceGenerator.class);
    
    public static final String ID = "service.client.configurer.source.generator";
    
    @Override
    public void generate(@NonNull AOTContext context) {
        final String serviceClientClasses = context.getConfiguration().mandatoryValue("service.client.classes");
        for (String serviceClientClass : serviceClientClasses.split(",")) {
            final String serviceClientClassFQN = serviceClientClass.trim();
            log.info("Generating service client configurer class for {}", serviceClientClassFQN);
            
            final String targetClassSimpleName = getTargetClassSimpleName(serviceClientClassFQN);
            
            final TypeSpec typeSpec = TypeSpec.classBuilder(targetClassSimpleName)
                                              .addModifiers(Modifier.PUBLIC)
                                              .addAnnotation(ContextConfigurer.class)
                                              .addSuperinterface(ApplicationContextConfigurer.class)
                                              .addMethod(methodSpec(serviceClientClassFQN))
                                              .build();
            
            final JavaFile javaFile = context.javaFile(typeSpec);
            
            // Register the service. This creates / updates META-INF/services/io.micronaut.context.ApplicationContextConfigurer with the generated class name.
            context.registerServiceImplementation(ApplicationContextConfigurer.class, targetClassSimpleName);
            context.registerGeneratedSourceFile(javaFile);
            
            log.info("Generated service client {} for {}", targetClassSimpleName, serviceClientClassFQN);
        }
    }

    private static MethodSpec methodSpec(final String serviceClientClassFQN) {
        final String code = String.format("org.expedientframework.common.ServiceClientConfigurer.configure(builder, %s.class);",
                                          serviceClientClassFQN);
        
        return MethodSpec.methodBuilder("configure")
                         .addModifiers(Modifier.PUBLIC)
                         .addAnnotation(Override.class)
                         .addParameter(ApplicationContextBuilder.class, "builder", Modifier.FINAL)
                         .addCode(code)
                         .build();
    }

    private static String getTargetClassSimpleName(final String serviceClientClassFQN) {
        //TODO: Should validate that serviceClientClassFQN is class FQN
        final int lastIndex = serviceClientClassFQN.lastIndexOf(".");
        return serviceClientClassFQN.substring(lastIndex + 1) + "ConfigurerGeneratedUsingAot";
    }
}
