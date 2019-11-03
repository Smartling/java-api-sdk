package com.smartling.api.v2.client.unmarshal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.smartling.api.v2.response.DependencyErrorField.DependencyErrorFieldType;
import com.smartling.api.v2.response.DependencyErrorDetails;
import com.smartling.api.v2.response.DependencyErrorField;
import com.smartling.api.v2.response.Details;
import com.smartling.api.v2.response.ErrorIdDetails;
import com.smartling.api.v2.response.FieldErrorDetails;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DetailsDeserializerTest
{
    private static final String TEXT = "text";

    private DetailsDeserializer detailsDeserializer;

    @Mock
    private JsonParser parser;

    @Mock
    private ObjectCodec objectCodec;

    @Mock
    private JsonNode node;

    @Mock
    private JsonNode childNode;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        detailsDeserializer = new DetailsDeserializer();

        when(parser.getCodec()).thenReturn(objectCodec);
        when(objectCodec.readTree(parser)).thenReturn(node);
        when(node.size()).thenReturn(1);
    }

    @Test
    public void testDeserializeNullBody() throws Exception
    {
        when(node.getNodeType()).thenReturn(JsonNodeType.NULL);
        final Details details = detailsDeserializer.deserialize(parser, null);
        assertNull(details);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeMiss() throws Exception
    {
        detailsDeserializer.deserialize(parser, null);
    }

    @Test
    public void testDeserialize() throws Exception
    {
        when(node.get(DetailsDeserializer.ERROR_ID_NODE)).thenReturn(childNode);
        when(childNode.asText()).thenReturn(TEXT);

        final Details details = detailsDeserializer.deserialize(parser, null);
        assertTrue(details instanceof ErrorIdDetails);
        assertEquals(TEXT, ((ErrorIdDetails)details).getErrorId());
    }

    @Test
    public void testDeserializeEmptyNode() throws Exception
    {
        when(node.size()).thenReturn(0);
        when(node.get(DetailsDeserializer.ERROR_ID_NODE)).thenReturn(childNode);

        assertNull(detailsDeserializer.deserialize(parser, null));
    }

    @Test
    public void testGetDetailsMiss() throws Exception
    {
        final Details details = detailsDeserializer.getDetails(node);
        assertNull(details);
    }

    @Test
    public void testGetDetailsErrorIdDetails() throws Exception
    {
        when(node.get(DetailsDeserializer.ERROR_ID_NODE)).thenReturn(childNode);
        when(childNode.asText()).thenReturn(TEXT);

        final Details details = detailsDeserializer.getDetails(node);
        assertTrue(details instanceof ErrorIdDetails);
        assertEquals(TEXT, ((ErrorIdDetails)details).getErrorId());
    }

    @Test
    public void testGetDetailsFieldErrorDetails() throws Exception
    {
        when(node.get(DetailsDeserializer.FIELD_NODE)).thenReturn(childNode);
        when(childNode.asText()).thenReturn(TEXT);

        final Details details = detailsDeserializer.getDetails(node);
        assertTrue(details instanceof FieldErrorDetails);
        assertEquals(TEXT, ((FieldErrorDetails)details).getField());
    }

    @Test
    public void testGetDetailsDependencyErrorDetails() throws Exception
    {
        List<DependencyErrorField> fields = new ArrayList<>();
        fields.add(new DependencyErrorField("field1", DependencyErrorField.DependencyErrorFieldType.NAME));
        fields.add(new DependencyErrorField("field2", DependencyErrorFieldType.ID));
        DependencyErrorDetails detailsPojo = new DependencyErrorDetails(fields);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(detailsPojo);
        JsonNode dependecyChildNode = mapper.readTree(json);
        when(node.get(DetailsDeserializer.DEPENDENCY_NODE)).thenReturn(dependecyChildNode);

        final Details details = detailsDeserializer.getDetails(node);
        assertTrue(details instanceof DependencyErrorDetails);
    }

}
