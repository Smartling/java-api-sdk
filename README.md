# Smartling Java API SDK

Java SDK for integrating with the [Smartling API].

## Using this SDK

The Smartling API SDK is distributed via Maven Central and is compatible with JDK 1.7
(Java 7) and up.

### Maven

Add the SDK to your dependencies:

    <dependencies>
        <dependency>
            <groupId>com.smartling.api</groupId>
            <artifactId>smartling-api-sdk</artifactId>
            <version>${version}</version>
        </dependency>
    </dependency>

### Gradle

Add the SDK to your dependencies:

    dependencies {
        implementation "com.smartling.api:smartling-api-sdk:${version}"
    }

### Initialize the SDK

The Smartling SDK manages OAuth 2 authentication automatically when you provide 
a API v2.0 identifier and a user secret to the API factory.

    SmartlingApi createSmartlingApi(String userIdentifier, String userSecret)
    {
        return new SmartlingApiFactory()
            .build(userIdentifier, userSecret);
    }

## Contributing

Open an issue on this repository to discuss any planned changes other than bug fixes
with Smartling's maintainers.

Fork this repository and create a pull request with your proposed changes. Your pull
request must pass all automated tests before it will be considered for inclusion.

Smartling developers should refer to the wiki for instructions on contributing to this
SDK.

[Smartling API]: https://api-reference.smartling.com/
