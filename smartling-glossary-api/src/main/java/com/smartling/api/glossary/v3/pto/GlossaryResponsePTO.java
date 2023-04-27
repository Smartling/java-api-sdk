package com.smartling.api.glossary.v3.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Glossary response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GlossaryResponsePTO implements ResponseData {
    /**
     * Glossary uid.
     */
    private String glossaryUid;
    /**
     * Account uid.
     */
    private String accountUid;
    /**
     * Glossary name.
     */
    private String glossaryName;
    /**
     * Glossary description.
     */
    private String description;
    /**
     * Glossary verificationMode.
     */
    private boolean verificationMode;
    /**
     * Glossary archived.
     * In case if {@code true} set, then glossary is archived.
     */
    private boolean archived;
    /**
     * Glossary createdByUserUid.
     */
    private String createdByUserUid;
    /**
     * Glossary modifiedByUserUid.
     */
    private String modifiedByUserUid;
    /**
     * Glossary createdDate.
     */
    private String createdDate;
    /**
     * Glossary modifiedDate.
     */
    private String modifiedDate;
    /**
     * Glossary localeIds.
     */
    private List<String> localeIds;
    /**
     * Fallback locales configuration.
     */
    private List<FallbackLocalePTO> fallbackLocales;
}
