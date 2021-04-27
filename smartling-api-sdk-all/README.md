# Smartling Java API Bom

The Smartling API BOM helps set a compatible set of common dependency versions.

## Using this Bom

The Smartling API BOM is distributed via Maven Central and is compatible with JDK 1.7
(Java 7) and up.

### Maven

Managing dependencies with bom:

    <dependencyManagement>
        <dependencies>
          <dependency>
            <groupId>com.smartling.api</groupId>
            <artifactId>smartling-api-bom</artifactId>
            <version>${version}</version>
            <type>pom</type>
            <scope>import</scope>
          </dependency>
        </dependencies>
    </dependencyManagement

Then in dependencies
     
    <dependencies>
        ...
        <dependency>
          <groupId>com.smartling.api</groupId>
          <artifactId>smartling-api-sdk</artifactId>
        </dependency>
        <dependency>
          <groupId>com.smartling.api</groupId>
          <artifactId>smartling-job-batches-api</artifactId>
        </dependency>
        ...
    </dependencies>

[Smartling API]: https://api-reference.smartling.com/
