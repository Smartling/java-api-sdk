package com.smartling.api.v2.tests.wiremock;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.smartling.api.v2.response.ResponseCode;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class SmartlingWireMock extends WireMock
{
    // language=JSON
    private static final String RESPONSE_TEMPLATE = "{\n" +
        "  \"response\": {\n" +
        "    \"code\": \"\"" +
        "  }\n" +
        "}";

    private static ObjectMapper mapper = new ObjectMapper();

    private static JsonNode readTree(String object)
    {
        try
        {
            return mapper.readTree(object);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Can't create json response", e);
        }
    }

    private static ObjectNode responseTemplate()
    {
        return (ObjectNode) readTree(RESPONSE_TEMPLATE);
    }

    public static MappingBuilder postJson(UrlPattern urlPattern)
    {
        return post(urlPattern)
            .withHeader("Content-Type", equalTo("application/json"));
    }

    public static ResponseDefinitionBuilder smartlingResponse(ResponseCode responseCode, String data, String errors)
    {
        ObjectNode response = responseTemplate();
        ((ObjectNode) response.get("response"))
            .put("code", responseCode.name());

        if (isNotEmpty(data))
        {
            ((ObjectNode) response.get("response"))
                .set("data", readTree(data));
        }

        if (isNotEmpty(errors))
        {
            ((ObjectNode) response.get("response"))
                .set("errors", readTree(errors));
        }

        return aResponse()
            .withHeader("Content-Type", "application/json")
            .withJsonBody(response);
    }

    public static ResponseDefinitionBuilder success(String data)
    {
        return smartlingResponse(ResponseCode.SUCCESS, data, null);
    }

    public static ResponseDefinitionBuilder error(ResponseCode responseCode, String errors)
    {
        return smartlingResponse(responseCode,null, errors);
    }
}
