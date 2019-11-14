package com.smartling.api.jobs.v3


interface SampleApiResponses
{
    def GET_TRANSLATION_JOB_RESPONSE_BODY = '''
        {
            "translationJobUid": "translationJobUid",
            "jobName": "jobName",
            "jobNumber": "PP-11111",
            "targetLocaleIds": ["fr-FR"],
            "description": "jobDescription",
            "dueDate": "2015-11-21T0B1:51:17Z",
            "referenceNumber": "referenceNumber",
            "callbackUrl": "https://www.callback.com/smartling/job",
            "callbackMethod": "GET",
            "createdDate": "2015-11-21T0B1:51:17Z",
            "modifiedDate": "2015-11-21T0B1:51:17Z",
            "createdByUserUid": "createdUserUid",
            "modifiedByUserUid": "modifiedUserUid",
            "firstCompletedDate": "2015-11-21T0B1:51:17Z",
            "lastCompletedDate": "2015-11-21T0B1:51:17Z",
            "jobStatus" : "IN_PROGRESS",
            "sourceFiles" : [{
                "uri" : "/file/app.properties",
                "name" : "/file/app.properties"
             }],
             "customFields": [{"fieldUid": "field1", "fieldName":"field name 1", "fieldValue": "value1"}, {"fieldUid": "field2", "fieldName":"field name 2", "fieldValue": "value2"}]
        }
'''
    def GET_TRANSLATION_JOBS_LIST_RESPONSE_BODY = '''
        {
           "totalCount":2,
           "items":[
              {
                 "translationJobUid":"translationJobUid1",
                 "jobName":"jobName1",
                 "jobNumber": "PP-11111",
                 "dueDate":"2015-11-21T11:51:17Z",
                 "targetLocaleIds":[
                    "fr-FR"
                 ],
                 "jobStatus":"IN_PROGRESS",
                 "createdDate":"2015-11-22T11:51:17Z"
              },
              {
                 "translationJobUid":"translationJobUid2",
                 "jobName":"jobName2",
                 "jobNumber": "PP-11111",
                 "dueDate":"2015-11-23T11:51:17Z",
                 "targetLocaleIds":[
                    "de-DE"
                 ],
                 "jobStatus":"AWAITING_AUTHORIZATION",
                 "createdDate":"2015-11-24T11:51:17Z"
              }
           ]
        }
'''
    def GET_TRANSLATION_JOB_PROGRESS_RESPONSE_BODY = '''
        {
            "contentProgressReport": [
                {
                    "targetLocaleId": "es-LA",
                    "targetLocaleDescription": "Spanish (Latin America)",
                    "workflowProgressReportList": [
                        {
                            "workflowUid": "84daac787eb5",
                            "workflowName": "Project Default",
                            "workflowStepSummaryReportItemList": [
                                {
                                    "workflowStepUid": "5963a9155d2c",
                                    "workflowStepName": "Translation",
                                    "stringCount": 62,
                                    "wordCount": 2003,
                                    "workflowStepType": "TRANSLATION"
                                },
                                {
                                    "workflowStepUid": "596348abe74a",
                                    "workflowStepName": "Published",
                                    "stringCount": 6,
                                    "wordCount": 39,
                                    "workflowStepType": "PUBLISH"
                                }
                            ]
                        }
                    ],
                    "summaryReport": [
                    {
                      "workflowStepType": "AWAITING_AUTHORIZATION",
                      "stringCount": 7,
                      "wordCount": 105
                    },
                    {
                      "workflowStepType": "TRANSLATION",
                      "stringCount": 62,
                      "wordCount": 2003
                    },
                    {
                      "workflowStepType": "PUBLISH",
                      "stringCount": 6,
                      "wordCount": 39
                    }
                  ],
                    "unauthorizedProgressReport": {
                        "stringCount": 7,
                        "wordCount": 105
                    },
                    "progress": {
                        "totalWordCount": 2147,
                        "percentComplete": 1
                    }
                },
                {
                    "targetLocaleId": "es-ES",
                    "targetLocaleDescription": "Spanish (Spain)",
                    "workflowProgressReportList": [
                        {
                            "workflowUid": "847090bd14ab",
                            "workflowName": "Approval",
                            "workflowStepSummaryReportItemList": [
                                {
                                    "workflowStepUid": "59648440638e",
                                    "workflowStepName": "Translation",
                                    "stringCount": 84,
                                    "wordCount": 2176,
                                    "workflowStepType": "TRANSLATION"
                                },
                                {
                                    "workflowStepUid": "59642e37b37e",
                                    "workflowStepName": "Approval",
                                    "stringCount": 0,
                                    "wordCount": 0,
                                    "workflowStepType": "ADMIN_APPROVAL"
                                },
                                {
                                    "workflowStepUid": "d5214950217a",
                                    "workflowStepName": "Review step",
                                    "stringCount": 0,
                                    "wordCount": 0,
                                    "workflowStepType": "POST_TRANSLATION__REVIEW"
                                },
                                {
                                    "workflowStepUid": "5964597358db",
                                    "workflowStepName": "Published",
                                    "stringCount": 7,
                                    "wordCount": 35,
                                    "workflowStepType": "PUBLISH"
                                }
                            ]
                        }
                    ],
                    "summaryReport": [
                    {
                      "workflowStepType": "TRANSLATION",
                      "stringCount": 84,
                      "wordCount": 2176
                    },
                    {
                      "workflowStepType": "PUBLISH",
                      "stringCount": 7,
                      "wordCount": 34
                    }
                  ],                    
                    "unauthorizedProgressReport": {
                        "stringCount": 0,
                        "wordCount": 0
                    },
                    "progress": {
                        "totalWordCount": 2211,
                        "percentComplete": 1
                    }
                }
            ],
            "progress": {
                "totalWordCount": 4358,
                "percentComplete": 1
            }
        }
'''
    def COUNT_MODIFIED_STRINGS = '''
        {
            "successCount": 1,
            "failCount": 1
        }
'''
    def ASYNC_RESPONSE = '''
        {
            "message": "Message text",
            "url": "http://url.com",
            "processUid": "uid"
        }
'''
    def EMPTY_DATA = 'null'

    def JOB_PROGRESS = '''
        {
            "contentProgressReport" : [{
                "targetLocaleId" : "es",
                "targetLocaleDescription" : "Spanish",
                "unauthorizedProgressReport" : {
                    "stringCount" : 0,
                    "wordCount" : 0
                },
                "workflowProgressReportList" : [{
                    "workflowUid" : "2344rf456",
                    "workflowName" : "Workflow Name",
                    "workflowStepSummaryReportItemList": [{
                        "workflowStepUid": "609f53657cf8",
                        "workflowStepName": "Translation",
                        "workflowStepType": "TRANSLATION",
                        "stringCount": 0,
                        "wordCount": 0
                    },
                    {
                        "workflowStepUid": "a9e4ad8ee94d",
                        "workflowStepName": "Edit Step",
                        "workflowStepType": "POST_TRANSLATION__EDIT",
                        "stringCount": 0,
                        "wordCount":0
                    }]
                }]
            }]
        }
        
'''


    def GET_JOB_FILES_LIST = '''
        {
          "totalCount": 2,
          "items": [
                {
                 "uri":"/app/file1.properties"
              },
                {
                 "uri":"/app/file2.properties"
              }
          ]
        }
'''


}
