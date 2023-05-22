package org.expedientframework.common;

import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.env.yaml.YamlPropertySourceLoader;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.core.io.file.FileSystemResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class ServiceClientConfigurer {
    private static final Logger log = LoggerFactory.getLogger(ServiceClientConfigurer.class);
    
    public static void configure(final ApplicationContextBuilder builder, final Class<?> serviceClientType) {
        log.info("Loading configuration for service client {}", serviceClientType.getName());
        
        final String configFileName = getConfigFileName(serviceClientType);
        final String configFilePath = "classpath:" + configFileName + ".yml";
        
        final Map<String, Object> properties = readPropertiesYamlFile(configFileName, configFilePath);
        builder.properties(properties);
    }
    
    private static String getConfigFileName(final Class<?> serviceClientType) {
        // Convert simple class name from camel-case to kebab case e.g. UserServiceClient to user-service-client
        return serviceClientType.getSimpleName().replaceAll("([a-z0-9])([A-Z])", "$1-$2").toLowerCase();
    }
    
    private static Map<String, Object> readPropertiesYamlFile(final String fileName, final String filePath) {
        try {
            log.info("Reading config file {}", filePath);
            final ResourceLoader resourceLoader = new ResourceResolver().getSupportingLoader(filePath)
                                                                        .orElse(FileSystemResourceLoader.defaultLoader());
    
            final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
            final Map<String, Object> properties = loader.read(fileName, resourceLoader.getResourceAsStream(filePath).get());
            log.info("Properties: {}", properties); //TODO: Change log level!!!
            return properties;
        } catch (Exception e) {
            log.error("An error occurred while reading config file {}", fileName, e);
            throw new RuntimeException(e);
        }
    }
}
