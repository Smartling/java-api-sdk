package com.smartling.api.files.v2.pto;

public enum RetrievalType
{
    /**
     * Smartling returns any translations (including non-published translations)
     */
    PUBLISHED,
    /**
     * Smartling returns only published/pre-published translations.
     */
    PENDING,
    /**
     * Smartling returns a modified version of the original text with certain characters
     * transformed and the text expanded.<br>
     *
     * For example, the uploaded string "This is a sample string", will return as
     * "T~hís ~ís á s~ámpl~é str~íñg". Pseudo translations enable you to test
     * how a longer string integrates into your application.
     */
    PSEUDO,
    /**
     * Smartling returns a modified version of the original file with strings wrapped
     * in a specific set of Unicode symbols that can later be recognized and matched
     * by the Chrome Context Capture Extension.
     */
    CONTEXTMATCHINGINSTRUMENTED
}
