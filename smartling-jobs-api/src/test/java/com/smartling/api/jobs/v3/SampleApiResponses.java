package com.smartling.api.jobs.v3;

interface SampleApiResponses
{
    String SUCCESS_RESPONSE_ENVELOPE = "{\"response\":{\"code\":\"SUCCESS\",\"data\":%s}})";

    String GET_TRANSLATION_JOB_RESPONSE_BODY = "{\n"
        + "        \"translationJobUid\":\"translationJobUid\",\n"
        + "            \"jobName\":\"jobName\",\n"
        + "            \"jobNumber\":\"PP-11111\",\n"
        + "            \"targetLocaleIds\": [\"fr-FR\"],\n"
        + "        \"description\":\"jobDescription\",\n"
        + "            \"dueDate\":\"2015-11-21T0B1:51:17Z\",\n"
        + "            \"referenceNumber\":\"referenceNumber\",\n"
        + "            \"callbackUrl\":\"https://www.callback.com/smartling/job\",\n"
        + "            \"callbackMethod\":\"GET\",\n"
        + "            \"createdDate\":\"2015-11-21T0B1:51:17Z\",\n"
        + "            \"modifiedDate\":\"2015-11-21T0B1:51:17Z\",\n"
        + "            \"createdByUserUid\":\"createdUserUid\",\n"
        + "            \"modifiedByUserUid\":\"modifiedUserUid\",\n"
        + "            \"firstCompletedDate\":\"2015-11-21T0B1:51:17Z\",\n"
        + "            \"lastCompletedDate\":\"2015-11-21T0B1:51:17Z\",\n"
        + "            \"jobStatus\" :\"IN_PROGRESS\",\n"
        + "            \"sourceFiles\" : [{\n"
        + "        \"uri\" :\"/file/app.properties\",\n"
        + "                \"name\" :\"/file/app.properties\"\n"
        + "    }],\n"
        + "        \"customFields\": [{\n"
        + "        \"fieldUid\":\"field1\", \"fieldName\":\"field name 1\", \"fieldValue\":\"value1\"\n"
        + "    },{\n"
        + "        \"fieldUid\":\"field2\", \"fieldName\":\"field name 2\", \"fieldValue\":\"value2\"\n"
        + "    }]\n"
        + "    }";

    String GET_TRANSLATION_JOBS_LIST_RESPONSE_BODY = "{\n"
        + "        \"totalCount\":2,\n"
        + "            \"items\":[\n"
        + "        {\n"
        + "            \"translationJobUid\":\"translationJobUid1\",\n"
        + "                \"jobName\":\"jobName1\",\n"
        + "                \"jobNumber\": \"PP-11111\",\n"
        + "                \"dueDate\":\"2015-11-21T11:51:17Z\",\n"
        + "                \"targetLocaleIds\":[\n"
        + "            \"fr-FR\"\n"
        + "                 ],\n"
        + "            \"jobStatus\":\"IN_PROGRESS\",\n"
        + "                \"createdDate\":\"2015-11-22T11:51:17Z\"\n"
        + "        },\n"
        + "        {\n"
        + "            \"translationJobUid\":\"translationJobUid2\",\n"
        + "                \"jobName\":\"jobName2\",\n"
        + "                \"jobNumber\": \"PP-11111\",\n"
        + "                \"dueDate\":\"2015-11-23T11:51:17Z\",\n"
        + "                \"targetLocaleIds\":[\n"
        + "            \"de-DE\"\n"
        + "                 ],\n"
        + "            \"jobStatus\":\"AWAITING_AUTHORIZATION\",\n"
        + "                \"createdDate\":\"2015-11-24T11:51:17Z\"\n"
        + "        }\n"
        + "           ]\n"
        + "    }";

    String GET_TRANSLATION_JOB_PROGRESS_RESPONSE_BODY = "{\n"
        + "        \"contentProgressReport\": [\n"
        + "        {\n"
        + "            \"targetLocaleId\": \"es-LA\",\n"
        + "                \"targetLocaleDescription\": \"Spanish (Latin America)\",\n"
        + "                \"workflowProgressReportList\": [\n"
        + "            {\n"
        + "                \"workflowUid\": \"84daac787eb5\",\n"
        + "                    \"workflowName\": \"Project Default\",\n"
        + "                    \"workflowStepSummaryReportItemList\": [\n"
        + "                {\n"
        + "                    \"workflowStepUid\": \"5963a9155d2c\",\n"
        + "                        \"workflowStepName\": \"Translation\",\n"
        + "                        \"stringCount\": 62,\n"
        + "                        \"wordCount\": 2003,\n"
        + "                        \"workflowStepType\": \"TRANSLATION\"\n"
        + "                },\n"
        + "                {\n"
        + "                    \"workflowStepUid\": \"596348abe74a\",\n"
        + "                        \"workflowStepName\": \"Published\",\n"
        + "                        \"stringCount\": 6,\n"
        + "                        \"wordCount\": 39,\n"
        + "                        \"workflowStepType\": \"PUBLISH\"\n"
        + "                }\n"
        + "                            ]\n"
        + "            }\n"
        + "                    ],\n"
        + "            \"summaryReport\": [\n"
        + "            {\n"
        + "                \"workflowStepType\": \"AWAITING_AUTHORIZATION\",\n"
        + "                    \"stringCount\": 7,\n"
        + "                    \"wordCount\": 105\n"
        + "            },\n"
        + "            {\n"
        + "                \"workflowStepType\": \"TRANSLATION\",\n"
        + "                    \"stringCount\": 62,\n"
        + "                    \"wordCount\": 2003\n"
        + "            },\n"
        + "            {\n"
        + "                \"workflowStepType\": \"PUBLISH\",\n"
        + "                    \"stringCount\": 6,\n"
        + "                    \"wordCount\": 39\n"
        + "            }\n"
        + "                  ],\n"
        + "            \"unauthorizedProgressReport\": {\n"
        + "            \"stringCount\": 7,\n"
        + "                    \"wordCount\": 105\n"
        + "        },\n"
        + "            \"progress\": {\n"
        + "            \"totalWordCount\": 2147,\n"
        + "                    \"percentComplete\": 1\n"
        + "        }\n"
        + "        },\n"
        + "        {\n"
        + "            \"targetLocaleId\": \"es-ES\",\n"
        + "                \"targetLocaleDescription\": \"Spanish (Spain)\",\n"
        + "                \"workflowProgressReportList\": [\n"
        + "            {\n"
        + "                \"workflowUid\": \"847090bd14ab\",\n"
        + "                    \"workflowName\": \"Approval\",\n"
        + "                    \"workflowStepSummaryReportItemList\": [\n"
        + "                {\n"
        + "                    \"workflowStepUid\": \"59648440638e\",\n"
        + "                        \"workflowStepName\": \"Translation\",\n"
        + "                        \"stringCount\": 84,\n"
        + "                        \"wordCount\": 2176,\n"
        + "                        \"workflowStepType\": \"TRANSLATION\"\n"
        + "                },\n"
        + "                {\n"
        + "                    \"workflowStepUid\": \"59642e37b37e\",\n"
        + "                        \"workflowStepName\": \"Approval\",\n"
        + "                        \"stringCount\": 0,\n"
        + "                        \"wordCount\": 0,\n"
        + "                        \"workflowStepType\": \"ADMIN_APPROVAL\"\n"
        + "                },\n"
        + "                {\n"
        + "                    \"workflowStepUid\": \"d5214950217a\",\n"
        + "                        \"workflowStepName\": \"Review step\",\n"
        + "                        \"stringCount\": 0,\n"
        + "                        \"wordCount\": 0,\n"
        + "                        \"workflowStepType\": \"POST_TRANSLATION__REVIEW\"\n"
        + "                },\n"
        + "                {\n"
        + "                    \"workflowStepUid\": \"5964597358db\",\n"
        + "                        \"workflowStepName\": \"Published\",\n"
        + "                        \"stringCount\": 7,\n"
        + "                        \"wordCount\": 35,\n"
        + "                        \"workflowStepType\": \"PUBLISH\"\n"
        + "                }\n"
        + "                            ]\n"
        + "            }\n"
        + "                    ],\n"
        + "            \"summaryReport\": [\n"
        + "            {\n"
        + "                \"workflowStepType\": \"TRANSLATION\",\n"
        + "                    \"stringCount\": 84,\n"
        + "                    \"wordCount\": 2176\n"
        + "            },\n"
        + "            {\n"
        + "                \"workflowStepType\": \"PUBLISH\",\n"
        + "                    \"stringCount\": 7,\n"
        + "                    \"wordCount\": 34\n"
        + "            }\n"
        + "                  ],\n"
        + "            \"unauthorizedProgressReport\": {\n"
        + "            \"stringCount\": 0,\n"
        + "                    \"wordCount\": 0\n"
        + "        },\n"
        + "            \"progress\": {\n"
        + "            \"totalWordCount\": 2211,\n"
        + "                    \"percentComplete\": 1\n"
        + "        }\n"
        + "        }\n"
        + "            ],\n"
        + "        \"progress\": {\n"
        + "        \"totalWordCount\": 4358,\n"
        + "                \"percentComplete\": 1\n"
        + "    }\n"
        + "    }";

    String COUNT_MODIFIED_STRINGS = "{\n"
        + "        \"successCount\":1,\n"
        + "            \"failCount\":1\n"
        + "    }";

    String ASYNC_RESPONSE = "{\n"
        + "        \"message\":\"Message text\",\n"
        + "            \"url\":\"http://url.com\",\n"
        + "            \"processUid\":\"uid\"\n"
        + "    }";

    String EMPTY_DATA = "null";

    String JOB_PROGRESS = "{\n"
        + "        \"contentProgressReport\" : [{\n"
        + "        \"targetLocaleId\" :\"es\",\n"
        + "                \"targetLocaleDescription\" :\"Spanish\",\n"
        + "                \"unauthorizedProgressReport\" :{\n"
        + "            \"stringCount\" :0,\n"
        + "                    \"wordCount\" :0\n"
        + "        },\n"
        + "        \"workflowProgressReportList\" : [{\n"
        + "            \"workflowUid\" :\"2344rf456\",\n"
        + "                    \"workflowName\" :\"Workflow Name\",\n"
        + "                    \"workflowStepSummaryReportItemList\": [{\n"
        + "                \"workflowStepUid\":\"609f53657cf8\",\n"
        + "                        \"workflowStepName\":\"Translation\",\n"
        + "                        \"workflowStepType\":\"TRANSLATION\",\n"
        + "                        \"stringCount\":0,\n"
        + "                        \"wordCount\":0\n"
        + "            },\n"
        + "            {\n"
        + "                \"workflowStepUid\":\"a9e4ad8ee94d\",\n"
        + "                    \"workflowStepName\":\"Edit Step\",\n"
        + "                    \"workflowStepType\":\"POST_TRANSLATION__EDIT\",\n"
        + "                    \"stringCount\":0,\n"
        + "                    \"wordCount\":0\n"
        + "            }]\n"
        + "        }]\n"
        + "    }]\n"
        + "    }";

    String GET_JOB_FILES_LIST = "{\n"
        + "        \"totalCount\":2,\n"
        + "            \"items\": [\n"
        + "        {\n"
        + "            \"uri\":\"/app/file1.properties\"\n"
        + "        },\n"
        + "        {\n"
        + "            \"uri\":\"/app/file2.properties\"\n"
        + "        }\n"
        + "          ]\n"
        + "    }";

    String FIND_JOB_BY_LOCALES_AND_HASHCODES_RESPONSE_BODY =
        "{" +
            "    \"totalCount\": 3," +
            "    \"items\": [" +
            "          {" +
            "           \"translationJobUid\":\"translationJobUid1\"," +
            "           \"jobName\":\"jobName1\"," +
            "           \"dueDate\":\"2015-11-21T11:51:17Z\"," +
            "           \"hashcodesByLocale\": [" +
            "             {" +
            "                 \"localeId\": \"fr-FR\"," +
            "                 \"hashcodes\": [\"hashcode1\", \"hashcode3\"]" +
            "             }, {" +
            "                 \"localeId\": \"de-DE\"," +
            "                 \"hashcodes\": [\"hashcode4\", \"hashcode3\"]" +
            "             }" +
            "           ]" +
            "        }," +
            "        {" +
            "           \"translationJobUid\":\"translationJobUid2\"," +
            "           \"jobName\":\"jobName2\"," +
            "           \"dueDate\":\"2015-11-23T11:51:17Z\"," +
            "           \"hashcodesByLocale\": [" +
            "             {" +
            "                 \"localeId\": \"es-ES\"," +
            "                 \"hashcodes\": [\"hashcode5\", \"hashcode6\"]" +
            "             }, {" +
            "                 \"localeId\": \"it-IT\"," +
            "                 \"hashcodes\": [\"hashcode7\", \"hashcode8\"]" +
            "             }" +
            "           ]" +
            "        }" +
            "    ]" +
            "}";

    String FIND_STRINGS_FOR_JOB_RESPONSE_BODY =
        "{" +
            "    \"totalCount\": 2," +
            "    \"items\": [" +
            "          {" +
            "           \"targetLocaleId\":\"aa-AA\"," +
            "           \"hashcode\":\"hashcode1\"" +
            "        }," +
            "          {" +
            "           \"targetLocaleId\":\"aa-AA\"," +
            "           \"hashcode\":\"hashcode2\"" +
            "        }" +
            "    ]" +
            "}";
}



