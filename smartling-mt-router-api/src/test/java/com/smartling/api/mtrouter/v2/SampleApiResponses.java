package com.smartling.api.mtrouter.v2;

interface SampleApiResponses
{
    String SUCCESS_RESPONSE_ENVELOPE = "{\"response\":{\"code\":\"SUCCESS\",\"data\":%s}})";

    String ERRONEOUS_GENERATE_ACCOUNT_TRANSLATION_RESPONSE_BODY = "{\n"
        + "            \"totalCount\": 1,\n"
        + "            \"items\": [\n"
        + "                {\n"
        + "                    \"key\": \"test-key\",\n"
        + "                    \"mtUid\": null,\n"
        + "                    \"translationText\": null,\n"
        + "                    \"error\": {\n"
        + "                        \"code\": \"INVALID_SOURCE_TEXT\",\n"
        + "                        \"key\": \"validation.error\",\n"
        + "                        \"message\": \"Source text was invalid\"\n"
        + "                    },\n"
        + "                    \"provider\": null\n"
        + "                }\n"
        + "            ]\n"
        + "        }";

    String SUCCESS_GENERATE_ACCOUNT_TRANSLATION_RESPONSE_BODY = " {\n"
        + "            \"totalCount\": 1,\n"
        + "            \"items\": [\n"
        + "                {\n"
        + "                    \"key\": \"test-key\",\n"
        + "                    \"mtUid\": \"a36z4bunraj1\",\n"
        + "                    \"translationText\": \"Zu übersetzenden Text testen\",\n"
        + "                    \"error\": null,\n"
        + "                    \"provider\": \"AUTO_SELECT_PROVIDER\"\n"
        + "                }\n"
        + "            ]\n"
        + "        }";
}
