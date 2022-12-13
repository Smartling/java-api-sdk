package com.smartling.resteasy.ext.integration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.resteasy.ext.ExtendedMultipartFormWriter;
import com.smartling.resteasy.ext.data.DummyMultipartApi;
import com.smartling.resteasy.ext.data.FormWithDynamicParams;
import com.smartling.resteasy.ext.data.FormWithListParams;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aMultipart;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.smartling.resteasy.ext.data.DummyMultipartApi.DUMMY_MULTIPART_API;
import static com.smartling.resteasy.ext.data.FormWithListParams.LIST_PARAM_NAME;
import static java.lang.Boolean.TRUE;

public class ExtendedMultipartFormWriterIntegrationTest
{
    @Rule
    public WireMockRule dummyApiEndpoint = new WireMockRule();

    @Before
    public void setup()
    {
        dummyApiEndpoint.stubFor(post(anyUrl()).willReturn(ok()));
    }

    private DummyMultipartApi dummyApi() throws Exception
    {
        return ((ResteasyClientBuilder) ClientBuilder.newBuilder()).build()
            .target(dummyApiEndpoint.baseUrl())
            .register(new ExtendedMultipartFormWriter())
            .proxy(DummyMultipartApi.class);
    }

    @Test
    public void shouldExtractDynamicFormParams() throws Exception
    {
        Map<String, Object> dynamicFields = new HashMap<>();
        dynamicFields.put("dynamic.field.string", "string value");
        dynamicFields.put("dynamic.field.integer", 42);
        dynamicFields.put("dynamic.field.boolean", TRUE);
        FormWithDynamicParams form = new FormWithDynamicParams();
        form.setDynamicFields(dynamicFields);

        dummyApi().postDynamicParams(form);

        dummyApiEndpoint.verify(postRequestedFor(urlEqualTo(DUMMY_MULTIPART_API + "/dynamic-fields"))
            .withRequestBodyPart(aMultipart()
                .withName("dynamic.field.string")
                .withBody(equalTo("string value"))
                .build())
            .withRequestBodyPart(aMultipart()
                .withName("dynamic.field.integer")
                .withBody(equalTo("42"))
                .build())
            .withRequestBodyPart(aMultipart()
                .withName("dynamic.field.boolean")
                .withBody(equalTo("true"))
                .build())
        );
    }

    @Test
    public void shouldExtractListFormParams() throws Exception
    {
        List<Object> listFields = new ArrayList<>();
        listFields.add("string value");
        listFields.add(42);
        listFields.add(new ArrayList<>());
        listFields.add(new ByteArrayInputStream("".getBytes()));
        FormWithListParams form = new FormWithListParams();
        form.setListField(listFields);

        dummyApi().postListParams(form);

        dummyApiEndpoint.verify(postRequestedFor(urlEqualTo(DUMMY_MULTIPART_API + "/list-fields"))
            .withRequestBodyPart(aMultipart()
                .withName(LIST_PARAM_NAME)
                .withBody(equalTo("42"))
                .build())
            .withRequestBodyPart(aMultipart()
                .withName(LIST_PARAM_NAME)
                .withBody(equalTo("string value"))
                .build())
        );
    }
}
