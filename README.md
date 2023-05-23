# Introduction
The provided CLI tool generates a single Maven module into which we can easily add a REST services and a client as well as write tests. However, typically we have, multi module Maven project. Hence, created sample modules to validate following:
* Separate module for REST service and the REST client.
* Calling another service using the REST client.

# Multi module project

## Layout
This was easy to achieve with following modules:
* **service**: This module contains the REST service.
* **public**: This module contains the REST client and anything that needs to be shared.

### service module
This module contains the REST controller and uses the **[Micronaut Maven Plugin](https://micronaut-projects.github.io/micronaut-maven-plugin/latest/index.html)** to build the fat jar.
Created following two services:
* **[user-service](user-service/service/src/main/java/org/expedientframework/user/UserServiceController.java)**: Returns the user name given a user id.
* **[hello-service](hello-service/service/src/main/java/org/expedientframework/hello/HelloServiceController.java)**: Calls the user-service to get the user name and then returns the greeting as "Hello userName".

### public module
This module contains the [declarative http client](https://micronaut-projects.github.io/micronaut-guides-poc/latest/micronaut-http-client-maven-java.html#declarative-client) using the **[@Client](https://docs.micronaut.io/2.1.4/api/io/micronaut/http/client/annotation/Client.html)** annotation.
```java
@Client(id = "userService", path = "/users")
public interface UserServiceClient {
    @Get("/{userId}")
    String getUser(long userId);
}
```
#### Defining http client configuration
As seen above, apart from the http Get method, the **[UserServiceClient](user-service/public/src/main/java/org/expedientframework/user/UserServiceClient.java)** has following two attributes for the **_@Client_** annotation:
* **_path_**: This is the relative path.
* **_id_**: This is the service ID to be used for looking up the configuration. The [configuration](user-service/public/src/main/resources/user-service-client.yml#L5) at minimum contains the http url for the service as shown below:
```yaml
micronaut:
  http:
    services:
      user-service:
        url: 'http://localhost:9090'
```
Note that the service ID changes from camel-case in **_@Client_** annotation to kebab-case in yaml file.

#### Making the http client available to the consumers
Consumers can use the http client by adding dependency on the **_public_** module. However, the configuration is not directly available to the consumer. Tried following options:
1. Copied the above configuration yaml properties into consumers **_application.yml_** file which is not user-friendly.
2. Used the **_[additional configuration files](https://docs.micronaut.io/1.3.0.M1/guide/index.html#_included_propertysource_loaders)_** option passing the consumer configuration file classpath as **_micronaut.config.files_** parameter value.
3. Read the additional configuration file from another module using classpath and manually passed the properties as property source during application start up as mentioned [here](https://docs.micronaut.io/1.3.0.M1/guide/index.html#propertySource).

With all of the above options, the consumer has an overhead of making sure the configuration is defined properly. Ideally, consumer should just add dependency on the **_public_** module and import the client to consume it. This was achieved by defining a [class implementing](user-service/public/src/main/java/org/expedientframework/user/UserServiceClientConfigurer.java) the **_[ApplicationContextConfigurer](https://docs.micronaut.io/3.2.1/api/io/micronaut/context/ApplicationContextConfigurer.html)_** interface and having the **_[@ContextConfigurer](https://docs.micronaut.io/3.5.3/api/io/micronaut/context/annotation/ContextConfigurer.html)_** annotation as below:
```java
@ContextConfigurer
public class UserServiceClientConfigurer implements ApplicationContextConfigurer {
    @Override
    public void configure(final ApplicationContextBuilder builder) {
        final Map<String, Object> properties = readPropertiesYamlFileAsMap();
        builder.properties(properties);
    }
}
```
That's it. When consumer service starts then above class is auto-detected and its **_configure()_** method is called.
# Observations
## Maven dependencies
For the top most module, the parent is defined as **_micronaut-parent_** which has all the required dependencies declared with required versions. 
```xml
<parent>
    <groupId>io.micronaut</groupId>
    <artifactId>micronaut-parent</artifactId>
    <version>3.9.2</version>
</parent>
```
It was observed that using latest version for any of the dependencies does not work. For example, if latest version of logback is used then logging stops working.
