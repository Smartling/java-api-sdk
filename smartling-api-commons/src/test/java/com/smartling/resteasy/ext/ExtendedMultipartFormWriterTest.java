package com.smartling.resteasy.ext;

import com.smartling.resteasy.ext.data.FormWithDynamicParams;
import com.smartling.resteasy.ext.data.FormWithListParams;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.TRUE;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ExtendedMultipartFormWriterTest
{
    private ExtendedMultipartFormWriter testedInstance;

    @Before
    public void setup()
    {
        testedInstance = new ExtendedMultipartFormWriter();
    }

    @Test
    public void shouldExtractDynamicFormParams() throws Exception
    {
        MultipartFormDataOutput output = mock(MultipartFormDataOutput.class);

        Map<String, Object> dynamicFields = new HashMap<>();
        dynamicFields.put("dynamic.field.string", "string value");
        dynamicFields.put("dynamic.field.integer", 42);
        dynamicFields.put("dynamic.field.boolean", TRUE);
        dynamicFields.put("should.be.skipped.list", new ArrayList<>());
        dynamicFields.put("should.be.skipped.object", new ByteArrayInputStream("".getBytes()));
        FormWithDynamicParams form = new FormWithDynamicParams();
        form.setDynamicFields(dynamicFields);

        testedInstance.getFields(FormWithDynamicParams.class, output, form);

        for (Map.Entry<String, Object> entry : dynamicFields.entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.startsWith("dynamic.field"))
            {
                verify(output).addFormData(eq(key), eq(value.toString()), eq(String.class), eq(String.class), eq(TEXT_PLAIN_TYPE), isNull(String.class));
            }
            else
            {
                verify(output, never()).addFormData(eq(key), any(), eq(String.class), eq(String.class), eq(TEXT_PLAIN_TYPE), isNull(String.class));
            }
        }
        verifyNoMoreInteractions(output);
    }

    @Test
    public void shouldExtractListFormParams() throws Exception
    {
        MultipartFormDataOutput output = mock(MultipartFormDataOutput.class);

        List<Object> listFields = new ArrayList<>();
        listFields.add("string value");
        listFields.add(42);
        listFields.add(new ArrayList<>());
        listFields.add(new ByteArrayInputStream("".getBytes()));
        FormWithListParams form = new FormWithListParams();
        form.setListField(listFields);

        testedInstance.getFields(FormWithListParams.class, output, form);

        verify(output).addFormData(eq("listParamName[]"), eq("string value"), eq(String.class), eq(String.class), eq(TEXT_PLAIN_TYPE), isNull(String.class));
        verify(output).addFormData(eq("listParamName[]"), eq("42"), eq(String.class), eq(String.class), eq(TEXT_PLAIN_TYPE), isNull(String.class));
        verifyNoMoreInteractions(output);
    }

}
