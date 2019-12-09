package com.smartling.api.files.v2.pto;

public enum LocaleMode
{
    /**
     * Locale code is added to the end of the file path. e.g. {@code /strings/es-ES/nav.properties}.
     */
    LOCALE_IN_PATH,

    /**
     * Locale code is added to the end of the file name e.g. {@code /strings/nav_es-ES.properties}.
     */
    LOCALE_IN_NAME,

    /**
     * Locale code is added to both the path and the filename. e.g. {@code /strings/es-ES/nav_es-ES.properties}.
     */
    LOCALE_IN_NAME_AND_PATH
}
