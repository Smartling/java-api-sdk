package com.smartling.api.files.v2.pto;

public enum RetrievalType
{
    /**
     * Smartling returns any translations (including non-published translations)
     */
    PUBLISHED("published"),

    /**
     * Smartling returns only published/pre-published translations.
     */
    PENDING("pending"),

    /**
     * Smartling returns a modified version of the original text with certain characters
     * transformed and the text expanded.<br>
     *
     * For example, the uploaded string "This is a sample string", will return as
     * "T~hís ~ís á s~ámpl~é str~íñg". Pseudo translations enable you to test
     * how a longer string integrates into your application.
     */
    PSEUDO("pseudo"),

    /**
     * Smartling returns a modified version of the original file with strings wrapped
     * in a specific set of Unicode symbols that can later be recognized and matched
     * by the Chrome Context Capture Extension.
     */
    CONTEXT_MATCHING_INSTRUMENTED("contextMatchingInstrumented");

    private final String value;

    RetrievalType(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return value;
    }
}
