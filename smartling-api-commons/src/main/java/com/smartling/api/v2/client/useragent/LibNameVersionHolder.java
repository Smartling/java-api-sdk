package com.smartling.api.v2.client.useragent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Data
@AllArgsConstructor
public class LibNameVersionHolder
{
    private final static String PROJECT_PROPERTIES_FILE = "sdk-project.properties";
    private final static String PROJECT_VERSION = "version";
    private final static String PROJECT_ARTIFACT_ID = "artifactId";

    private String clientLibName;

    private String clientLibVersion;

    public LibNameVersionHolder(Class klazz) throws LibNameVerionPropertiesReadError
    {
        final Properties properties = new Properties();
        try
        {
            InputStream propertiesStream = klazz.getResourceAsStream(PROJECT_PROPERTIES_FILE);

            if(propertiesStream == null)
            {
                throw new LibNameVerionPropertiesReadError(String.format("Properties file not found filename=%s", PROJECT_PROPERTIES_FILE));
            }

            properties.load(propertiesStream);
            this.clientLibName = properties.getProperty(PROJECT_ARTIFACT_ID) + "-java";
            this.clientLibVersion = properties.getProperty(PROJECT_VERSION);
        }
        catch (IOException e)
        {
            throw new LibNameVerionPropertiesReadError(String.format("Could not read properties file=%s", PROJECT_PROPERTIES_FILE), e);
        }
    }
}
