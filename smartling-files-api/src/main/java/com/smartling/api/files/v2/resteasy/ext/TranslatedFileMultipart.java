package com.smartling.api.files.v2.resteasy.ext;

import lombok.Getter;
import org.jboss.resteasy.plugins.providers.multipart.MultipartInput;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.InputStream;

public class TranslatedFileMultipart
{
    @Getter
    InputStream fileBody;
    @Getter
    MultivaluedMap<String, String> fileHeaders;
    @Getter
    MediaType fileMediaType;
    @Getter
    TranslatedFileMetadata translatedFileMetadata;
    MultipartInput multipartInput;

    void close()
    {
        if (null != multipartInput)
            multipartInput.close();
    }
}
