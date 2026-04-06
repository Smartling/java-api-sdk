# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is the official Smartling Java API SDK - a multi-module Maven project providing type-safe Java clients for Smartling's Translation Management Platform REST APIs. The SDK handles OAuth 2.0 authentication, provides strongly-typed request/response objects (PTOs), and includes comprehensive error handling.

**Key Technologies:**
- Java 17+ (JDK 17 required)
- Maven 3.8+ (use `./mvnw` wrapper)
- RESTEasy 4.7.10 (JAX-RS implementation)
- Jackson 2.17.3 (JSON serialization)
- Apache HttpClient 4.5.14 (HTTP transport)
- JUnit 4 (testing) + WireMock (API mocking)

## Architecture

### Multi-Module Structure

This is a Maven reactor build with 17+ modules organized as follows:

**Core Modules:**
- `smartling-api-commons` - Shared infrastructure (ClientFactory, authentication, exceptions, response wrappers)
- `samrtling-api-test-utils` - Test utilities and WireMock helpers (note: typo in directory name is intentional)
- `smartling-authorization-api` - Deprecated, maintained for backward compatibility

**API Modules (one per Smartling API):**
Each API module follows the same structure:
- `smartling-{name}-api/` - Files API, Glossary API, Jobs API, etc.
- Module contains: `{Name}Api.java` interface, `{Name}ApiFactory.java`, and PTOs (Plain Transfer Objects)

**Aggregator Module:**
- `smartling-api-sdk-all` - Convenience module that includes all API modules (keep as last module in build order)

### Key Architectural Patterns

**1. Factory Pattern for API Clients**
Each API module provides a factory that builds configured API clients:
```java
// Pattern: {ApiName}ApiFactory extends AbstractApiFactory
FilesApiFactory factory = new FilesApiFactory(clientFactory);
FilesApi api = factory.buildApi(authFilter, clientConfiguration);
```

**2. Authentication Flow**
- `AuthenticationApi` obtains OAuth 2.0 tokens from Smartling
- `Authenticator` manages token lifecycle (acquisition, refresh, expiry)
- `BearerAuthSecretFilter` or `BearerAuthStaticTokenFilter` inject auth headers into requests
- All managed automatically via `ClientFactory` and filters

**3. Response Wrapping**
All API responses follow the pattern:
- `Response<T>` - Generic wrapper containing response code, data, and errors
- `ListResponse<T>` - For paginated list endpoints
- PTOs (Plain Transfer Objects) - Strongly-typed request/response data classes

**4. Exception Hierarchy**
- `RestApiRuntimeException` - Base exception
  - `ClientApiException` - 4xx errors (AuthenticationError, ValidationError, NotFoundError, etc.)
  - `ServerApiException` - 5xx errors (ServiceBusyError, MaintenanceModeError)
- `DefaultRestApiExceptionMapper` maps HTTP status codes to specific exceptions

**5. Dependency Version Management**
- All dependency versions are properties in root `pom.xml` (e.g., `${jackson.version}`)
- `<dependencyManagement>` section in root POM ensures version consistency
- Child modules inherit versions automatically - never hardcode versions in module POMs

## Build Commands

### Building the Project

```bash
# Clean build with tests (requires Java 17+)
./mvnw clean install

# Build without tests
./mvnw clean install -DskipTests

# Build without tests and skip GPG signing (for local development)
./mvnw clean install -DskipTests -Dgpg.skip=true

# Build specific module
./mvnw clean install -pl smartling-files-api -DskipTests

# Build module and its dependencies
./mvnw clean install -pl smartling-files-api -am -DskipTests
```

**Important:**
- Gradle builds are slow due to compilation and dependency resolution - use 10 min timeout minimum
- GPG signing is enabled by default and will fail locally without keys - add `-Dgpg.skip=true`
- Never use `rm -rf .gradle` or `rm -rf build` (permission denied) - use `./mvnw clean` instead

### Running Tests

```bash
# Run all tests
./mvnw test

# Run tests for specific module (use -am to also build its dependencies)
./mvnw test -pl smartling-files-api -am

# Run single test class
./mvnw test -pl smartling-files-api -am -Dtest=FilesApiTest

# Run single test method
./mvnw test -pl smartling-files-api -am -Dtest=FilesApiTest#testUploadFile

### Documentation

```bash
# Generate Javadoc for all modules
./mvnw javadoc:javadoc

# Generate Javadoc for specific module
./mvnw javadoc:javadoc -pl smartling-files-api
```

### Verification Commands

```bash
# Check effective POM (resolved properties and dependencies)
./mvnw help:effective-pom -pl smartling-files-api

# View dependency tree
./mvnw dependency:tree

# Check for dependency updates
./mvnw versions:display-dependency-updates
```

## Development Workflow

### Adding a New API Module

1. Create module directory: `smartling-{name}-api/`
2. Add module to root `pom.xml` `<modules>` section (alphabetical order, except keep `smartling-api-sdk-all` last)
3. Create module POM with parent reference:
   ```xml
   <parent>
       <groupId>com.smartling.api</groupId>
       <artifactId>smartling-sdk-parent</artifactId>
       <version>2.0.2-SNAPSHOT</version>
   </parent>
   ```
4. Create `{Name}Api.java` interface with JAX-RS annotations
5. Create `{Name}ApiFactory.java` extending `AbstractApiFactory`
6. Add PTOs (Plain Transfer Objects) in `pto/` package
7. Write tests using WireMock for API mocking

### Adding/Updating Dependencies

1. Add version property to root `pom.xml` `<properties>` section
2. Add dependency to root `pom.xml` `<dependencyManagement>` section with `${property.version}`
3. Reference dependency in module POMs using same `${property.version}`
4. **Never hardcode versions** in module POMs - always use properties

### Commit Message Format

Follow conventional commits (enforced by maven-release-plugin):

```
<type>: <subject>

Types: feat, fix, docs, refactor, test, chore
Example: feat: add Glossary API
Example: fix: handle token refresh race condition
```

## Testing

### Test Structure
- Unit tests: Use JUnit 4 with Mockito
- Integration tests: Use WireMock to mock Smartling APIs
- Test utilities: `SmartlingWireMock` helper in `samrtling-api-test-utils`

### Test Naming
- Test classes: `{ClassName}Test.java` for unit tests
- Integration tests: `{ClassName}IntTest.java`

### Common Test Patterns
```java
// Use WireMock for API testing
@Rule
public WireMockRule wireMockRule = new WireMockRule(options().dynamicPort());

// Use BearerAuthStaticTokenFilter for test authentication
BearerAuthStaticTokenFilter authFilter = new BearerAuthStaticTokenFilter("test-token");

// Build test API client
FilesApi api = new FilesApiFactory(clientFactory)
    .buildApi(authFilter, clientConfiguration);
```

## Release Process

CI/CD via CircleCI (`.circleci/config.yml`):
- **Test job**: Runs on all branches - builds, tests, generates Javadoc
- **Deploy job**: Runs on `master` and `2.x` branches only - performs Maven release with GPG signing

Version management:
- Use `maven-release-plugin` with conventional commits plugin
- Versions follow semantic versioning
- Release creates git tags: `v{version}` (e.g., `v2.0.0`)

## Important Notes

### Java Version Compatibility
- **Current version (2.x)**: Requires Java 17+
- **Previous version (1.x)**: Java 8+ (maintenance mode, EOL Dec 2026)
- CI/CD uses `cimg/openjdk:17.0` Docker image

### Module Order Matters
- `smartling-api-commons` must build first (other modules depend on it)
- `samrtling-api-test-utils` should build second (test utilities)
- `smartling-api-sdk-all` must build last (aggregates all API modules)
- Keep modules in root POM alphabetically ordered except for these constraints

### Authentication Architecture
- Never store credentials in code or version control
- Use environment variables or secure configuration management
- `Authenticator` class handles token refresh automatically before expiry
- Tokens are bearer tokens passed in `Authorization: Bearer {token}` header

## Common Patterns to Follow

1. **API Interface Design**: Use JAX-RS annotations (@Path, @GET, @POST, etc.) on interface methods
2. **Request Objects**: Suffix with `PTO` (e.g., `UploadFilePTO`) - Plain Transfer Objects
3. **Response Objects**: Wrap in `Response<T>` or `ListResponse<T>` from `smartling-api-commons`
4. **Exception Handling**: Let `DefaultRestApiExceptionMapper` map HTTP codes to specific exceptions
5. **Factory Pattern**: Always provide `{Name}ApiFactory` extending `AbstractApiFactory`

## Related Documentation

- [Smartling API Reference](https://api-reference.smartling.com/)
- [Java SDK Help Center](https://help.smartling.com/hc/en-us/articles/4403912351131-Java-SDK)
- [Code Samples Repository](https://github.com/Smartling/smartling-samples/)
