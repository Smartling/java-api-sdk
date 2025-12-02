# Smartling Java API SDK

[![Maven Central](https://img.shields.io/maven-central/v/com.smartling.api/smartling-api-sdk.svg)](https://central.sonatype.com/search?q=g%3Acom.smartling.api&smo=true&namespace=com.smartling.api)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

The official Java SDK for the [Smartling Translation Management Platform](https://www.smartling.com). This library provides a type-safe, idiomatic Java interface to Smartling's REST APIs, enabling seamless integration of translation workflows into your applications.

## Features

- **Automatic OAuth 2.0 Management** - Built-in token acquisition, refresh, and secure credential handling
- **Type-Safe API Clients** - Strongly-typed interfaces with comprehensive error handling
- **Multi-Module Architecture** - Include only the API modules you need to minimize dependencies
- **Production Ready** - Battle-tested by hundreds of enterprise integrations with comprehensive test coverage
- **Modern Java Support** - Built with RESTEasy, Jackson, and Apache HttpClient

## Requirements

- **Java 17 or higher** (JDK 17+)
- Maven 3.8+ or Gradle 7.0+

### Core Dependencies

- RESTEasy 4.7.10 (JAX-RS client implementation)
- Jackson 2.15.4 (JSON serialization)
- Apache HttpClient 4.5.14 (HTTP transport)
- SLF4J 2.0.16 (Logging facade)

## Installation

### Maven

Add the SDK to your `pom.xml`:

```xml
<dependency>
    <groupId>com.smartling.api</groupId>
    <artifactId>smartling-api-sdk</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Gradle

Add the SDK to your `build.gradle`:

```gradle
dependencies {
    implementation 'com.smartling.api:smartling-api-sdk:2.0.0'
}
```

## Quick Start

The SDK automatically manages OAuth 2.0 authentication when you provide your API credentials:

```java
import com.smartling.api.files.v2.FilesApi;
import com.smartling.api.files.v2.FilesApiFactory;
import com.smartling.api.v2.authentication.AuthenticationApi;
import com.smartling.api.v2.authentication.AuthenticationApiFactory;
import com.smartling.api.v2.client.ClientFactory;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.Authenticator;
import com.smartling.api.v2.client.auth.BearerAuthSecretFilter;

public class SmartlingExample {
    public static void main(String[] args) {
        String userIdentifier = "YOUR_USER_IDENTIFIER";
        String userSecret = "YOUR_USER_SECRET";

        // Create a factory for building API clients
        DefaultClientConfiguration clientConfiguration = DefaultClientConfiguration.builder().build();
        ClientFactory clientFactory = new ClientFactory();

        // Create the authenticator to be used by subsequent API calls
        AuthenticationApi authenticationApi = new AuthenticationApiFactory(clientFactory).buildApi(clientConfiguration);
        Authenticator authenticator = new Authenticator(userIdentifier, userSecret, authenticationApi);

        // Create files API client
        FilesApi filesApi = new FilesApiFactory(clientFactory)
            .buildApi(new BearerAuthSecretFilter(authenticator), clientConfiguration);
        
        // Use the API client
        // ...
    }
}
```

For detailed examples and tutorials, visit our [samples repository](https://github.com/Smartling/smartling-samples/).

## Version Support

### Current Version (2.x)

- **Minimum Java Version**: Java 17 (JDK 17)
- **Status**: Stable and production-ready

### Previous Version (1.x)

- **Minimum Java Version**: Java 8 (JDK 1.8)
- **End of Support**: December 1, 2026
- **Status**: Maintenance mode - critical fixes only

For projects still using Java 8-16, continue using version 1.x. We recommend planning your migration to Java 17+ to access new features and ensure continued support beyond 2026.

## Documentation

- **[API Reference](https://api-reference.smartling.com/)** - Complete REST API documentation
- **[Java SDK Guide](https://help.smartling.com/hc/en-us/articles/4403912351131-Java-SDK)** - SDK-specific documentation and best practices
- **[Help Center](https://help.smartling.com/hc/en-us/sections/1260801854870-Smartling-API)** - API guides and tutorials
- **[Code Samples](https://github.com/Smartling/smartling-samples/)** - Working examples and integration patterns

## Support

- **Bug Reports**: [GitHub Issues](https://github.com/Smartling/java-api-sdk/issues)
- **Feature Requests**: [GitHub Issues](https://github.com/Smartling/java-api-sdk/issues)
- **Security Issues**: Please report privately to security@smartling.com

## Contributing

We welcome contributions from the community! Before submitting significant changes:

1. Open an issue to discuss your proposed changes with the maintainers
2. Fork the repository and create a feature branch
3. Ensure all tests pass and add new tests for your changes
4. Submit a pull request with a clear description of your changes

For Smartling employees, please refer to the internal wiki for contribution guidelines.

## License

This project is licensed under the [Apache License 2.0](LICENSE).

---

**Smartling** | [Website](https://www.smartling.com) | [API Documentation](https://api-reference.smartling.com/)
