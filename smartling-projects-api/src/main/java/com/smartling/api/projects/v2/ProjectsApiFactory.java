package com.smartling.api.projects.v2;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientFactory;

public class ProjectsApiFactory extends AbstractApiFactory<ProjectsApi>
{
    public ProjectsApiFactory()
    {
        super();
    }

    public ProjectsApiFactory(ClientFactory clientFactory)
    {
        super(clientFactory);
    }

    @Override
    protected Class<ProjectsApi> getApiClass()
    {
        return ProjectsApi.class;
    }
}
