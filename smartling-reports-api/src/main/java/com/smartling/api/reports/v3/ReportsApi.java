package com.smartling.api.reports.v3;

import com.smartling.api.reports.v3.pto.WordCountReportCommandPTO;
import com.smartling.api.reports.v3.pto.WordCountResponsePTO;
import com.smartling.api.v2.response.ListResponse;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

@Path("/reports-api/v3")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ReportsApi extends AutoCloseable
{
    /**
     * Obtains word count report data
     * @param wordCountReportCommandPTO command with filters for the report
     *    date fields using pattern: yyyy-MM-dd
     *    fields: List of fields in the responce. IF empty - return list of columns by default.
     *    includeTranslationResource: deprecated. Use fields instead.
     *    includeJob: deprecated. Use fields instead.
     *    includeJobReferenceNumber: deprecated. Use fields instead.
     *    includeFuzzyMatchProfile: deprecated. Use fields instead.
     *    includeWorkflowStep: deprecated. Use fields instead.
     *
     * @return word count report data in {@link WordCountResponsePTO} object
     */
    @GET
    @Path("word-count")
    ListResponse<WordCountResponsePTO> wordCountReport(@BeanParam WordCountReportCommandPTO wordCountReportCommandPTO);

    /**
     * Obtains word count report in csv
     * @param wordCountReportCommandPTO command with filters for the report
     *    date fields using pattern: yyyy-MM-dd
     *    fields: List of fields in the responce. IF empty - return list of columns by default.
     *    includeTranslationResource: deprecated. Use fields instead.
     *    includeJob: deprecated. Use fields instead.
     *    includeJobReferenceNumber: deprecated. Use fields instead.
     *    includeFuzzyMatchProfile: deprecated. Use fields instead.
     *    includeWorkflowStep: deprecated. Use fields instead.
     *
     * @return csv file
     */
    @GET
    @Path("word-count/csv")
    InputStream downloadWordCountReport(@BeanParam WordCountReportCommandPTO wordCountReportCommandPTO);
}
