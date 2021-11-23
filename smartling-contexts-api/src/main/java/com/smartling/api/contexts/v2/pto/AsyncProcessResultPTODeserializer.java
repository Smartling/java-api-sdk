package com.smartling.api.contexts.v2.pto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class AsyncProcessResultPTODeserializer extends JsonDeserializer<AsyncProcessResultPTO>
{
    @Override
    public AsyncProcessResultPTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException
    {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        return mapper.readValue(jsonParser, MatchAsyncProcessResultPTO.class);
    }
}
