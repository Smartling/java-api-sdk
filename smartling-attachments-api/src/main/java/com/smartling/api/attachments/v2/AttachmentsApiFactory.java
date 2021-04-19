package com.smartling.api.attachments.v2;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientFactory;

public class AttachmentsApiFactory extends AbstractApiFactory<AttachmentsApi>
{
    public  AttachmentsApiFactory()
    {
        super();
    }

    public AttachmentsApiFactory(ClientFactory clientFactory)
    {
        super(clientFactory);
    }

    @Override
    protected Class<AttachmentsApi> getApiClass()
    {
        return AttachmentsApi.class;
    }
}
