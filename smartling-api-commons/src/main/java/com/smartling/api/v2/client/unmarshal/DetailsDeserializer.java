package com.smartling.api.v2.client.unmarshal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.api.v2.response.DependencyErrorDetails;
import com.smartling.api.v2.response.Details;
import com.smartling.api.v2.response.ErrorIdDetails;
import com.smartling.api.v2.response.FieldErrorDetails;
import java.io.IOException;

/**
 * JSON deserializer extension to unmarshal error details if present on the response.
 */
public class DetailsDeserializer extends JsonDeserializer<Details>
{
    static final String ERROR_ID_NODE   = "errorId";
    static final String FIELD_NODE      = "field";
    static final String DEPENDENCY_NODE = "dependencies";

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * {@inheritDoc}
     */
    @Override
    public Details deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException
    {
        final JsonNode detailsNode = p.getCodec().readTree(p);
        if (detailsNode.isNull() || detailsNode.size() == 0)
            return null;

        final Details details = getDetails(detailsNode);
        if (null == details)
            throw new JsonProcessingException("Unknown response in JSON for Details: " + detailsNode.toString()) {};

        return details;
    }

    /**
     * Returns the response details from the given JSON node.
     *
     * @param detailsNode the {@code JsonNode} containing the response details.
     *
     * @return the unmarshalled <code>Details</code>
     * @throws JsonProcessingException if a JSON processing error occurs
     */
    protected Details getDetails(final JsonNode detailsNode) throws JsonProcessingException
    {
        if (null != detailsNode.get(ERROR_ID_NODE))
            return new ErrorIdDetails(detailsNode.get(ERROR_ID_NODE).asText());

        if (null != detailsNode.get(FIELD_NODE))
            return new FieldErrorDetails(detailsNode.get(FIELD_NODE).asText());

        if (null != detailsNode.get(DEPENDENCY_NODE))
            return objectMapper.treeToValue(detailsNode.get(DEPENDENCY_NODE), DependencyErrorDetails.class);

        return null;
    }
}
