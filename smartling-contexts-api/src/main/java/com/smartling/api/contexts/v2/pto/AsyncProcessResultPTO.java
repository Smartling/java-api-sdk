package com.smartling.api.contexts.v2.pto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = AsyncProcessResultPTODeserializer.class)
public interface AsyncProcessResultPTO
{
}
