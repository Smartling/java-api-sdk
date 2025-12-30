package com.smartling.api.v2.tests.wiremock;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.UrlPattern;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * WireMock utility class for Smartling API testing.
 * Provides convenience methods for creating standardized mock responses
 * that match the Smartling API response format.
 */
public class SmartlingWireMock extends WireMock
{
    // language=JSON
    private static final String RESPONSE_TEMPLATE = "{\n" +
        "  \"response\": {\n" +
        "    \"code\": \"\"" +
        "  }\n" +
        "}";

    private static final ObjectMapper mapper = new ObjectMapper();

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

    /**
     * Creates a POST request matcher with JSON content type header.
     *
     * @param urlPattern the URL pattern to match against incoming requests
     * @return a mapping builder configured for JSON POST requests
     */
    public static MappingBuilder postJson(UrlPattern urlPattern)
    {
        return post(urlPattern)
            .withHeader("Content-Type", equalTo("application/json"));
    }

    /**
     * Creates a Smartling API response with the specified response code, content type, and optional data/errors.
     *
     * @param <T> the enum type for the response code
     * @param responseCode the response code enum value
     * @param contentType the Content-Type header value for the response
     * @param data the response data as JSON string (can be null or empty)
     * @param errors the error messages as JSON string (can be null or empty)
     * @return a response definition builder with Smartling API format
     */
    public static <T extends Enum<T>> ResponseDefinitionBuilder smartlingResponse(
        Enum<T> responseCode, String contentType, String data, String errors)
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
            .withHeader("Content-Type", contentType)
            .withJsonBody(response);
    }

    /**
     * Creates a Smartling API response with application/json content type.
     * This is a convenience method that calls the 4-arg version with "application/json" as content type.
     *
     * @param <T> the enum type for the response code
     * @param responseCode the response code enum value
     * @param data the response data as JSON string (can be null or empty)
     * @param errors the error messages as JSON string (can be null or empty)
     * @return a response definition builder with Smartling API format
     */
    public static <T extends Enum<T>> ResponseDefinitionBuilder smartlingResponse(Enum<T> responseCode, String data, String errors)
    {
        return smartlingResponse(responseCode, "application/json", data, errors);
    }

    /**
     * Creates a successful Smartling API response with the provided data.
     *
     * @param data the response data as JSON string
     * @return a response definition builder for a successful response
     */
    public static ResponseDefinitionBuilder success(String data)
    {
        return smartlingResponse(ResponseCode.SUCCESS, data, null);
    }

    /**
     * Creates an error response with the specified error code and messages.
     *
     * @param <T> the enum type for the response code
     * @param responseCode the error response code enum value
     * @param errors the error messages as JSON string
     * @return a response definition builder for an error response
     */
    public static  <T extends Enum<T>> ResponseDefinitionBuilder error(Enum<T> responseCode, String errors)
    {
        return smartlingResponse(responseCode,null, errors);
    }

    /**
     * Standard Smartling API response codes for testing.
     */
    public enum ResponseCode
    {
        /**
         * Indicates a successful API operation.
         */
        SUCCESS
    }
}
