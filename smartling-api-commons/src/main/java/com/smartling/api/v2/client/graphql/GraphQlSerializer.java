package com.smartling.api.v2.client.graphql;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class GraphQlSerializer extends JsonSerializer<AbstractGraphQlModel>
{
    private static final String OPERATION_NAME = "operationName";
    private static final String QUERY = "query";
    private static final String VARIABLES = "variables";

    @Override
    public void serialize(AbstractGraphQlModel value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException
    {
        gen.writeStartObject();

        gen.writeStringField(OPERATION_NAME, value.operationName());
        gen.writeStringField(QUERY, value.query());
        serializers.defaultSerializeField(VARIABLES, value.variables(), gen);

        gen.writeEndObject();
    }
}
