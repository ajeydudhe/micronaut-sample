package org.expedientframework.common.aot;

import com.squareup.javapoet.JavaFile;
import io.micronaut.aot.core.AOTModule;
import io.micronaut.aot.core.Option;
import io.micronaut.aot.core.codegen.AbstractSingleClassFileGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AOTModule(
    id = ServiceClientConfigurerSourceGenerator.ID,
    options = {
        @Option(key = "greeter.message", sampleValue = "Hello, world!", description = "The message to write")
    }
)
public class ServiceClientConfigurerSourceGenerator extends AbstractSingleClassFileGenerator {
    public static final String ID = "service.client.configurer.generator";
    
    private static final Logger log = LoggerFactory.getLogger(ServiceClientConfigurerSourceGenerator.class);
    
    @Override
    protected JavaFile generate() {
        log.error("### Ajey ==> ServiceClientConfigurerSourceGenerator.generate() called...");
        return null;
    }
}
