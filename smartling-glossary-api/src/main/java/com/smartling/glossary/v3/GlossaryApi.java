package com.smartling.glossary.v3;

import com.smartling.glossary.v3.components.GlossaryManagementApi;
import com.smartling.glossary.v3.components.entry.EntryManagementApi;
import com.smartling.glossary.v3.components.entry.auth.EntryAuthorizeForTranslationApi;
import com.smartling.glossary.v3.components.ie.ImportExportApi;
import com.smartling.glossary.v3.components.label.LabelManagementApi;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Glossary Api, as composite api.
 */
@Path("/glossary-api/v3")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GlossaryApi extends GlossaryManagementApi, EntryManagementApi, EntryAuthorizeForTranslationApi, ImportExportApi, LabelManagementApi {

}
