package com.smartling.api.projects.v2;

import com.smartling.api.projects.v2.pto.ProjectDetailsPTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/projects-api/v2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ProjectsApi
{
    /**
     * Obtains details about a project.
     *
     * @param projectId              unique string identifier of a project
     * @param includeDisabledLocales toggle whether disabled locale info should be returned
     * @return details about a project in the form of a {@link ProjectDetailsPTO} object
     */
    @GET
    @Path("/projects/{projectId}")
    ProjectDetailsPTO getDetails(@PathParam("projectId") String projectId, @QueryParam("includeDisabledLocales") boolean includeDisabledLocales);
}
