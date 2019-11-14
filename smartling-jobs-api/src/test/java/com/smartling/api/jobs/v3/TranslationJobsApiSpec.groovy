package com.smartling.api.jobs.v3

import com.smartling.api.jobs.v3.pto.AddLocaleCommandPTO
import com.smartling.api.jobs.v3.pto.AsyncResponsePTO
import com.smartling.api.jobs.v3.pto.ContentProgressReportPTO
import com.smartling.api.jobs.v3.pto.HashcodesAndLocalesPTO
import com.smartling.api.jobs.v3.pto.LocaleAndHashcodeListCommandPTO
import com.smartling.api.jobs.v3.pto.PagingCommandPTO
import com.smartling.api.jobs.v3.pto.SortCommandPTO
import com.smartling.api.jobs.v3.pto.StringModifiedCountResponsePTO
import com.smartling.api.jobs.v3.pto.TranslationJobAddFileCommandPTO
import com.smartling.api.jobs.v3.pto.TranslationJobAuthorizeCommandPTO
import com.smartling.api.jobs.v3.pto.TranslationJobCancelCommandPTO
import com.smartling.api.jobs.v3.pto.TranslationJobCreateCommandPTO
import com.smartling.api.jobs.v3.pto.TranslationJobCreateResponsePTO
import com.smartling.api.jobs.v3.pto.TranslationJobGetResponsePTO
import com.smartling.api.jobs.v3.pto.TranslationJobListCommandPTO
import com.smartling.api.jobs.v3.pto.TranslationJobRemoveFileCommandPTO
import com.smartling.api.jobs.v3.pto.TranslationJobSearchCommandPTO
import com.smartling.api.jobs.v3.pto.TranslationJobUpdateCommandPTO
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter
import com.smartling.api.v2.response.EmptyData
import groovy.json.JsonSlurper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.apache.http.HttpStatus
import spock.lang.Specification

import javax.ws.rs.HttpMethod
import javax.ws.rs.core.HttpHeaders

class TranslationJobsApiSpec extends Specification
{
    def SUCCESS_RESPONSE_ENVELOPE = '{"response":{"code":"SUCCESS","data":%s}})'

    def PROJECT_ID = '4bca2a7b8'
    def TRANSLATION_JOB_UID = '0123456789ab'
    def TARGET_LOCALE_ID = 'fr-FR'

    TranslationJobsApi translationJobsApi

    def slurper = new JsonSlurper()

    MockWebServer mockWebServer = new MockWebServer()

    def setup()
    {
        mockWebServer.start()

        def authFilter = new BearerAuthStaticTokenFilter('foo')
        translationJobsApi = new TranslationJobsApiFactory().buildApi(authFilter, mockWebServer.url('/').toString())
    }

    def assignResponse(httpStatusCode, body)
    {
        def response = new MockResponse()
            .setResponseCode(httpStatusCode)
            .setHeader(HttpHeaders.CONTENT_LENGTH, body.length())
            .setHeader(HttpHeaders.CONTENT_TYPE, 'application/json; charset=utf-8')
            .setBody(body)

        mockWebServer.enqueue(response)
    }

    def 'get translation job'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.GET_TRANSLATION_JOB_RESPONSE_BODY));

        when:
            TranslationJobGetResponsePTO responsePTO = translationJobsApi.getTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID);

        then:
            responsePTO.getTranslationJobUid() == "translationJobUid"
            responsePTO.getJobName() == "jobName"
            responsePTO.getJobNumber() == "PP-11111"
            responsePTO.getTargetLocaleIds().size() == 1
            responsePTO.getTargetLocaleIds().get(0) == "fr-FR"
            responsePTO.getDescription() == "jobDescription"
            responsePTO.getDueDate() == "2015-11-21T0B1:51:17Z"
            responsePTO.getReferenceNumber() == "referenceNumber"
            responsePTO.getCallbackUrl() == "https://www.callback.com/smartling/job"
            responsePTO.getCallbackMethod() == "GET"
            responsePTO.getCreatedDate() == "2015-11-21T0B1:51:17Z"
            responsePTO.getModifiedDate() == "2015-11-21T0B1:51:17Z"
            responsePTO.getCreatedByUserUid() == "createdUserUid"
            responsePTO.getModifiedByUserUid() == "modifiedUserUid"
            responsePTO.getFirstCompletedDate() == "2015-11-21T0B1:51:17Z"
            responsePTO.getLastCompletedDate() == "2015-11-21T0B1:51:17Z"
            responsePTO.getJobStatus() == "IN_PROGRESS"
            responsePTO.getSourceFiles().size() == 1
            responsePTO.getSourceFiles().get(0).getUri() == "/file/app.properties"
            responsePTO.getSourceFiles().get(0).getName() == "/file/app.properties"
            responsePTO.customFields.fieldUid == ["field1", "field2"]
            responsePTO.customFields.fieldValue == ["value1", "value2"]
            responsePTO.customFields.fieldName == ["field name 1", "field name 2"]


            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.GET
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_SINGLE_JOB_ENDPOINT).replace('{projectId}', PROJECT_ID).replace('{translationJobUid}', TRANSLATION_JOB_UID))
    }

    def 'create translation job'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.GET_TRANSLATION_JOB_RESPONSE_BODY))
            def requestBodyStr = '''
                    {
                        "jobName": "jobName",
                        "targetLocaleIds": ["fr-FR"],
                        "description": "jobDescription",
                        "dueDate": "2015-11-21T0B1:51:17Z",
                        "referenceNumber": "referenceNumber",
                        "callbackUrl": "https://www.callback.com/smartling/job",
                        "callbackMethod": "GET",
                        "customFields": [{"fieldUid": "field1", "fieldValue": "value1"}, {"fieldUid": "field1", "fieldValue": "value2"}]
                    }
            '''.replaceAll('\\s+', '')

            TranslationJobCreateCommandPTO requestBody = slurper.parseText requestBodyStr

        when:
            TranslationJobCreateResponsePTO responsePTO = translationJobsApi.createTranslationJob(PROJECT_ID, requestBody)

        then:
            responsePTO.getTranslationJobUid() == "translationJobUid"
            responsePTO.getJobName() == "jobName"
            responsePTO.getJobNumber() == "PP-11111"
            responsePTO.getTargetLocaleIds().size() == 1
            responsePTO.getTargetLocaleIds().get(0) == "fr-FR"
            responsePTO.getDescription() == "jobDescription"
            responsePTO.getDueDate() == "2015-11-21T0B1:51:17Z"
            responsePTO.getReferenceNumber() == "referenceNumber"
            responsePTO.getCallbackUrl() == "https://www.callback.com/smartling/job"
            responsePTO.getCallbackMethod() == "GET"
            responsePTO.getCreatedDate() == "2015-11-21T0B1:51:17Z"
            responsePTO.getModifiedDate() == "2015-11-21T0B1:51:17Z"
            responsePTO.getCreatedByUserUid() == "createdUserUid"
            responsePTO.getModifiedByUserUid() == "modifiedUserUid"
            responsePTO.getFirstCompletedDate() == "2015-11-21T0B1:51:17Z"
            responsePTO.getLastCompletedDate() == "2015-11-21T0B1:51:17Z"
            responsePTO.getJobStatus() == "IN_PROGRESS"
            responsePTO.customFields.fieldUid == ["field1", "field2"]
            responsePTO.customFields.fieldValue == ["value1", "value2"]
            responsePTO.customFields.fieldName == ["field name 1", "field name 2"]

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.POST
            request.body.readUtf8() == requestBodyStr
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOBS_ENDPOINT).replace('{projectId}', PROJECT_ID))
    }

    def 'list translation jobs'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.GET_TRANSLATION_JOBS_LIST_RESPONSE_BODY))
            def filterBody = new TranslationJobListCommandPTO('job2', 'TT-123', ['jobUid'], ['DRAFT', 'IN_PROGRESS'])
            def pagingBody = new PagingCommandPTO(offset: 10, limit: 100)
            def sortBody = new SortCommandPTO(sortDirection: "asc", sortBy: "jobName")

        when:
            def listResponse = translationJobsApi.listTranslationJobs(PROJECT_ID, filterBody, pagingBody, sortBody)

        then:
            listResponse.totalCount == 2

            listResponse.items[0].translationJobUid == 'translationJobUid1'
            listResponse.items[0].jobName == 'jobName1'
            listResponse.items[0].jobNumber == "PP-11111"
            listResponse.items[0].targetLocaleIds == ['fr-FR']
            listResponse.items[0].jobStatus == 'IN_PROGRESS'
            listResponse.items[0].createdDate == '2015-11-22T11:51:17Z'
            listResponse.items[0].dueDate == '2015-11-21T11:51:17Z'

            listResponse.items[1].translationJobUid == 'translationJobUid2'
            listResponse.items[1].jobName == 'jobName2'
            listResponse.items[0].jobNumber == "PP-11111"
            listResponse.items[1].targetLocaleIds == ['de-DE']
            listResponse.items[1].jobStatus == 'AWAITING_AUTHORIZATION'
            listResponse.items[1].createdDate == '2015-11-24T11:51:17Z'
            listResponse.items[1].dueDate == '2015-11-23T11:51:17Z'

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.GET
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOBS_ENDPOINT).replace('{projectId}', PROJECT_ID))
            request.path.contains("translationJobStatus=DRAFT")
            request.path.contains("translationJobStatus=IN_PROGRESS")
            request.path.contains("jobName=job2")
            request.path.contains("jobNumber=TT-123")
            request.path.contains("translationJobUids=jobUid")
            request.path.contains("offset=10")
            request.path.contains("limit=100")
            request.path.contains("sortBy=jobName")
            request.path.contains("sortDirection=asc")
    }

    def 'get translation job progress'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.GET_TRANSLATION_JOB_PROGRESS_RESPONSE_BODY))

        when:
            def response = translationJobsApi.getTranslationJobProgress(PROJECT_ID, TRANSLATION_JOB_UID, TARGET_LOCALE_ID)

        then:
            response.progress.totalWordCount == 4358
            response.progress.percentComplete == 1

            response.contentProgressReport.targetLocaleId == ['es-LA', 'es-ES']
            response.contentProgressReport.targetLocaleDescription == ['Spanish (Latin America)', 'Spanish (Spain)']

            def report1 = response.contentProgressReport[0]

            def workflowReport1 = report1.workflowProgressReportList
            workflowReport1.workflowUid == ['84daac787eb5']
            def workflow1Steps = workflowReport1[0].workflowStepSummaryReportItemList;
            workflow1Steps.workflowStepUid == ['5963a9155d2c', '596348abe74a']
            workflow1Steps.workflowStepName == ['Translation', 'Published']
            workflow1Steps.workflowStepType == ['TRANSLATION', 'PUBLISH']
            workflow1Steps.wordCount == [2003, 39]
            workflow1Steps.stringCount == [62, 6]


            report1.summaryReport.stringCount == [7, 62, 6]
            report1.summaryReport.wordCount == [105, 2003, 39]
            report1.summaryReport.workflowStepType == ['AWAITING_AUTHORIZATION', 'TRANSLATION', 'PUBLISH']

            report1.unauthorizedProgressReport.stringCount == 7
            report1.unauthorizedProgressReport.wordCount == 105

            report1.progress.totalWordCount == 2147
            report1.progress.percentComplete == 1

            def report2 = response.contentProgressReport[1]

            report2.summaryReport.stringCount == [84, 7]
            report2.summaryReport.wordCount == [2176, 34]
            report2.summaryReport.workflowStepType == ['TRANSLATION', 'PUBLISH']

            report2.unauthorizedProgressReport.stringCount == 0
            report2.unauthorizedProgressReport.wordCount == 0

            report2.progress.totalWordCount == 2211
            report2.progress.percentComplete == 1

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.GET
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_PROGRESS_ENDPOINT).replace('{projectId}', PROJECT_ID).replace('{translationJobUid}', TRANSLATION_JOB_UID))
            request.path.contains("targetLocaleId=$TARGET_LOCALE_ID")
    }

    def 'update translation job'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.GET_TRANSLATION_JOB_RESPONSE_BODY))
            def requestBodyStr = '''
                        {
                            "jobName": "jobName",
                            "description": "jobDescription",
                            "dueDate": "2015-11-21T0B1:51:17Z",
                            "referenceNumber": "referenceNumber",
                            "callbackUrl": "https://www.callback.com/smartling/job",
                            "callbackMethod": "GET",
                            "customFields": [{"fieldUid": "field1", "fieldValue": "value1"}, {"fieldUid": "field1", "fieldValue": "value2"}]
                        }
                '''.replaceAll('\\s+', '')

            TranslationJobUpdateCommandPTO requestBody = slurper.parseText requestBodyStr

        when:
            TranslationJobGetResponsePTO responsePTO = translationJobsApi.updateTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, requestBody)

        then:
            responsePTO.getTranslationJobUid() == "translationJobUid"
            responsePTO.getJobName() == "jobName"
            responsePTO.getTargetLocaleIds().size() == 1
            responsePTO.getTargetLocaleIds().get(0) == "fr-FR"
            responsePTO.getDescription() == "jobDescription"
            responsePTO.getDueDate() == "2015-11-21T0B1:51:17Z"
            responsePTO.getReferenceNumber() == "referenceNumber"
            responsePTO.getCallbackUrl() == "https://www.callback.com/smartling/job"
            responsePTO.getCallbackMethod() == "GET"
            responsePTO.getCreatedDate() == "2015-11-21T0B1:51:17Z"
            responsePTO.getModifiedDate() == "2015-11-21T0B1:51:17Z"
            responsePTO.getCreatedByUserUid() == "createdUserUid"
            responsePTO.getModifiedByUserUid() == "modifiedUserUid"
            responsePTO.getFirstCompletedDate() == "2015-11-21T0B1:51:17Z"
            responsePTO.getLastCompletedDate() == "2015-11-21T0B1:51:17Z"
            responsePTO.getJobStatus() == "IN_PROGRESS"
            responsePTO.customFields.fieldUid == ["field1", "field2"]
            responsePTO.customFields.fieldValue == ["value1", "value2"]
            responsePTO.customFields.fieldName == ["field name 1", "field name 2"]

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.PUT
            request.body.readUtf8() == requestBodyStr
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_SINGLE_JOB_ENDPOINT).replace('{projectId}', PROJECT_ID).replace('{translationJobUid}', TRANSLATION_JOB_UID))
    }

    def 'add file to job: sync'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.COUNT_MODIFIED_STRINGS))
            def requestBodyStr = '''
                    {
                        "moveEnabled" : true,
                        "fileUri" : "/file/app.properties",
                        "targetLocaleIds" : ["fr-FR"]
                    }
            '''.replaceAll('\\s+', '')
            TranslationJobAddFileCommandPTO command = slurper.parseText requestBodyStr

        when:
            StringModifiedCountResponsePTO responsePTO = translationJobsApi.addFileToTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command)

        then:
            !responsePTO.async
            responsePTO.successCount == 1
            responsePTO.failCount == 1

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.POST
            request.body.readUtf8() == requestBodyStr
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_ADD_FILE_ENDPOINT).replace('{projectId}', PROJECT_ID).replace('{translationJobUid}', TRANSLATION_JOB_UID))
    }

    def 'add file to job: async'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.ASYNC_RESPONSE))
            def requestBodyStr = '''
                    {
                        "moveEnabled" : true,
                        "fileUri" : "/file/app.properties",
                        "targetLocaleIds" : ["fr-FR"]
                    }
            '''.replaceAll('\\s+', '')
            TranslationJobAddFileCommandPTO command = slurper.parseText requestBodyStr

        when:
            StringModifiedCountResponsePTO responsePTO = translationJobsApi.addFileToTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command)

        then:
            responsePTO.async
            responsePTO.url == "http://url.com"
            responsePTO.message == "Message text"

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.POST
            request.body.readUtf8() == requestBodyStr
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_ADD_FILE_ENDPOINT).replace('{projectId}', PROJECT_ID).replace('{translationJobUid}', TRANSLATION_JOB_UID))
    }

    def 'remove file from job: sync'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.COUNT_MODIFIED_STRINGS))
            TranslationJobRemoveFileCommandPTO command = new TranslationJobRemoveFileCommandPTO("/file/app.properties")

        when:
            StringModifiedCountResponsePTO responsePTO = translationJobsApi.removeFileFromTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command)

        then:
            responsePTO.successCount == 1
            responsePTO.failCount == 1
            !responsePTO.url
            !responsePTO.message


            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.POST
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_REMOVE_FILE_ENDPOINT).replace('{projectId}', PROJECT_ID).replace('{translationJobUid}', TRANSLATION_JOB_UID))
    }

    def 'remove file from job: async'()
    {
        given:
            assignResponse(HttpStatus.SC_ACCEPTED, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.ASYNC_RESPONSE))
            TranslationJobRemoveFileCommandPTO command = new TranslationJobRemoveFileCommandPTO("/file/app.properties")

        when:
            StringModifiedCountResponsePTO responsePTO = translationJobsApi.removeFileFromTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command)

        then:
            responsePTO.url == "http://url.com"
            responsePTO.message == "Message text"
            !responsePTO.successCount
            !responsePTO.failCount

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.POST
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_REMOVE_FILE_ENDPOINT).replace('{projectId}', PROJECT_ID).replace('{translationJobUid}', TRANSLATION_JOB_UID))
    }

    def 'add / move strings to job'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.COUNT_MODIFIED_STRINGS))
            def requestBodyStr = '''
                    {
                        "moveEnabled" : true,
                        "hashcodes" : ["hashcode1","hashcode2"],
                        "targetLocaleIds" : ["fr-FR"]
                    }
            '''.replaceAll('\\s+', '')
            LocaleAndHashcodeListCommandPTO command = slurper.parseText requestBodyStr

        when:
            StringModifiedCountResponsePTO responsePTO = translationJobsApi.addOrMoveStringsToTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command)

        then:
            responsePTO.successCount == 1
            responsePTO.failCount == 1

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.POST
            request.body.readUtf8() == requestBodyStr
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_ADD_STRINGS_ENDPOINT).replace('{projectId}', PROJECT_ID).replace('{translationJobUid}', TRANSLATION_JOB_UID))
    }

    def 'remove strings from job'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.ASYNC_RESPONSE))
            def requestBodyStr = '''
                    {
                        "hashcodes" : ["hashcode1","hashcode2"],
                        "localeIds" : ["fr-FR"]
                    }
            '''.replaceAll('\\s+', '')
            HashcodesAndLocalesPTO command = slurper.parseText requestBodyStr

        when:
            StringModifiedCountResponsePTO responsePTO = translationJobsApi.removeStringsFromTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command)

        then:
            responsePTO.url == "http://url.com"
            responsePTO.message == "Message text"

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.POST
            request.body.readUtf8() == requestBodyStr
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_REMOVE_STRINGS_ENDPOINT).replace('{projectId}', PROJECT_ID).replace('{translationJobUid}', TRANSLATION_JOB_UID))
    }

    def 'add locale to job'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.COUNT_MODIFIED_STRINGS))
            def requestBodyStr = '''
                    {
                        "syncContent" : true
                    }
            '''.replaceAll('\\s+', '')
            AddLocaleCommandPTO command = slurper.parseText requestBodyStr

        when:
            AsyncResponsePTO responsePTO = translationJobsApi.addLocaleToTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, "fr-FR", command)

        then:
            !responsePTO.url
            !responsePTO.message

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.POST
            request.body.readUtf8() == requestBodyStr
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_ADD_LOCALE_ENDPOINT).replace('{projectId}', PROJECT_ID)
                .replace('{translationJobUid}', TRANSLATION_JOB_UID).replace('{localeId}', 'fr-FR'))
    }

    def 'remove locale from job'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.ASYNC_RESPONSE))

        when:
            AsyncResponsePTO responsePTO = translationJobsApi.removeLocaleFromTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, "fr-FR")

        then:
            responsePTO.url == "http://url.com"
            responsePTO.message == "Message text"

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.DELETE
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_REMOVE_LOCALE_ENDPOINT).replace('{projectId}', PROJECT_ID)
                .replace('{translationJobUid}', TRANSLATION_JOB_UID).replace('{localeId}', 'fr-FR'))
    }

    def 'cancel job'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.EMPTY_DATA))
            def requestBodyStr = '''
                    {
                        "reason" : "Reason for cancel"
                    }
            '''.replaceAll('\\s+', '')
            TranslationJobCancelCommandPTO command = slurper.parseText requestBodyStr

        when:
            EmptyData responsePTO = translationJobsApi.cancelTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command)

        then:
            responsePTO instanceof EmptyData

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.POST
            request.body.readUtf8() == requestBodyStr
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_CANCEL_ENDPOINT).replace('{projectId}', PROJECT_ID)
                .replace('{translationJobUid}', TRANSLATION_JOB_UID))
    }

    def 'get job file progress'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.JOB_PROGRESS))
        when:
            ContentProgressReportPTO responsePTO = translationJobsApi.getTranslationJobFileProgress(PROJECT_ID, TRANSLATION_JOB_UID, "fileUri", TARGET_LOCALE_ID)

        then:
            responsePTO.contentProgressReport.size() == 1

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.GET
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_FILE_PROGRESS_ENDPOINT).replace('{projectId}', PROJECT_ID).replace('{translationJobUid}', TRANSLATION_JOB_UID))
            request.path.contains("fileUri=fileUri")
            request.path.contains("targetLocaleId=$TARGET_LOCALE_ID")
    }

    def 'job search'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.GET_TRANSLATION_JOBS_LIST_RESPONSE_BODY))
            def requestBodyStr = '''
                    {
                        "hashcodes" : ["hashcode1","hashcode2"],
                        "fileUris" : ["/file/file1.properties"],
                        "translationJobUids" : ["jobUid1", "jobUid2"]
                    }
            '''.replaceAll('\\s+', '')

            TranslationJobSearchCommandPTO command = slurper.parseText requestBodyStr
        when:
            def listResponse = translationJobsApi.translationJobsSearch(PROJECT_ID, command)

        then:
            listResponse.totalCount == 2

            listResponse.items[0].translationJobUid == 'translationJobUid1'
            listResponse.items[0].jobName == 'jobName1'
            listResponse.items[0].targetLocaleIds == ['fr-FR']
            listResponse.items[0].jobStatus == 'IN_PROGRESS'
            listResponse.items[0].createdDate == '2015-11-22T11:51:17Z'
            listResponse.items[0].dueDate == '2015-11-21T11:51:17Z'

            listResponse.items[1].translationJobUid == 'translationJobUid2'
            listResponse.items[1].jobName == 'jobName2'
            listResponse.items[1].targetLocaleIds == ['de-DE']
            listResponse.items[1].jobStatus == 'AWAITING_AUTHORIZATION'
            listResponse.items[1].createdDate == '2015-11-24T11:51:17Z'
            listResponse.items[1].dueDate == '2015-11-23T11:51:17Z'

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.POST
            request.body.readUtf8() == requestBodyStr
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOBS_SEARCH_ENDPOINT).replace('{projectId}', PROJECT_ID))
    }

    def 'authorize job'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.EMPTY_DATA))
            def requestBodyStr = '''
                    {
                        "localeWorkflows" : [{
                            "targetLocaleId" : "fr-FR",
                            "workflowUid" : "w123"
                        }]
                    }
            '''.replaceAll('\\s+', '')
            TranslationJobAuthorizeCommandPTO command = slurper.parseText requestBodyStr
        when:
            EmptyData responsePTO = translationJobsApi.authorizeTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command)

        then:
            responsePTO instanceof EmptyData

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.POST
            def jsonRoot = new JsonSlurper().parse(request.body.readUtf8().chars)
            jsonRoot.localeWorkflows[0].targetLocaleId == 'fr-FR'
            jsonRoot.localeWorkflows[0].workflowUid == 'w123'
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_AUTHORIZE_ENDPOINT).replace('{projectId}', PROJECT_ID).replace('{translationJobUid}', TRANSLATION_JOB_UID))
    }


    def 'delete job'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.EMPTY_DATA))
        when:
            def responsePTO = translationJobsApi.deleteTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID)
        then:
            responsePTO instanceof EmptyData

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.DELETE
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_DELETE_ENDPOINT).replace('{projectId}', PROJECT_ID).replace('{translationJobUid}', TRANSLATION_JOB_UID))
    }

    def 'close job'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.EMPTY_DATA))
        when:
            def responsePTO = translationJobsApi.closeTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID)
        then:
            responsePTO instanceof EmptyData

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.POST
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_CLOSE_ENDPOINT).replace('{projectId}', PROJECT_ID).replace('{translationJobUid}', TRANSLATION_JOB_UID))
    }

    def 'get job files'()
    {
        given:
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.GET_JOB_FILES_LIST))
            def pagingBody = new PagingCommandPTO(offset: 10, limit: 100)
        when:
            def response = translationJobsApi.getTranslationJobFiles(PROJECT_ID, TRANSLATION_JOB_UID, pagingBody)

        then:
            response.totalCount == 2
            response.items.size() == 2
            response.items[0].uri == '/app/file1.properties'
            response.items[1].uri == '/app/file2.properties'

            def request = mockWebServer.takeRequest()
            request.method == HttpMethod.GET
            request.path.startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_FILES_LIST_ENDPOINT).replace('{projectId}', PROJECT_ID).replace('{translationJobUid}', TRANSLATION_JOB_UID))
            request.path.contains("offset=10&limit=100")
    }

}
