package com.smartling.api.files.v2.pto;

public enum FileNameMode
{
    /**
     * Full original file path is used
     */
    UNCHANGED,
    /**
     * Remove all except the last path segment. e.g. {@code /en/strings/nav.properties} becomes {@code nav.properties}
     */
    TRIM_LEADING,
    /**
     * Adds a locale folder to the file path directly before the filename. e.g. {@code /strings/nav.properties} becomes {@code /strings/en/nav.properties}
     */
    LOCALE_LAST
}
