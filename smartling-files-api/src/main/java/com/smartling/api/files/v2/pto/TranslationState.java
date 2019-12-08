package com.smartling.api.files.v2.pto;

public enum TranslationState
{
    /**
     * The translated content is published.
     */
    PUBLISHED,
    /**
     * The translated content is imported into the first step after
     * translation - if there is none, it will be published.
     */
    POST_TRANSLATION
}
