package com.smartling.api.external.client.graphql;

import java.util.Map;

public interface AbstractGraphQlModel
{
    String operationName();
    String query();
    Map<String, Object> variables();
}
