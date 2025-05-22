package com.smartling.api.filetranslations.v2.pto;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum FileType
{
    DOCX("application/octet-stream", false),        // DOCX
    DOCM("application/octet-stream", false),        // Microsoft Word with macros
    RTF("application/octet-stream", false),        // RTF
    PPTX("application/octet-stream", false),        // PPTX
    XLSX("application/octet-stream", false),        // XLSX
    IDML("application/octet-stream", false),        // IDML
    RESX("application/xml", true),                  // .NET resource (.resx, .resw)
    PLAIN_TEXT("text/plain", true),                 // plain text (.txt files)
    XML("application/xml", true),                   // generic XML file
    HTML("text/html", true),                        // HTML file
    PRES("text/plain", true),                       // Pres resources
    SRT("text/plain", false),                       // SubRip Text Format
    MARKDOWN("text/markdown", true),                // Markdown Text Format
    DITA("application/xml", false),                 // DITA file format
    VTT("text/vtt", true), // WebVTT file format
    FLARE("application/octet-stream", false), // MadCap Flare parser
    SVG("image/svg+xml", false), // SVG parser
    XLIFF2("application/xml", true), // XLIFF 2.0 parser
    CSV("text/csv", true),                          // CSV (Comma-separated values)
    XLSX_TEMPLATE("application/octet-stream", false),        // Smartling XLSX - https://help.smartling.com/hc/en-us/articles/1260804224670-Translating-Spreadsheets#h_01F3TKVZBVPFJSF2XZEBHDH1EQ
    ANDROID("application/xml", true), // ANDROID parser
    JSON("application/json", true),
    ;
    private final String identifier;
    private final String mimeType;
    private final boolean isTextFormat;

    // Lookup map
    public final static Map<String, FileType> BY_NAME_LOOKUP = new HashMap<>();

    static
    {
        for (final FileType value : FileType.values())
        {
            BY_NAME_LOOKUP.put(value.identifier.toLowerCase(), value);
            BY_NAME_LOOKUP.put(value.name().toLowerCase(), value);
        }
    }

    private static String toLowerCamel(String s)
    {
        StringBuilder buf = new StringBuilder();
        String[] parts = s.split("_");

        for (int i = 0; i < parts.length; i++)
            buf.append((i == 0) ? parts[i].toLowerCase() : StringUtils.capitalize(parts[i].toLowerCase()));

        return buf.toString();
    }

    FileType(final String mimeType, final boolean isTextFormat)
    {
        this.identifier = toLowerCamel(name());
        this.mimeType = mimeType;
        this.isTextFormat = isTextFormat;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public boolean isTextFormat()
    {
        return isTextFormat;
    }

    public static FileType lookup(final String fileTypeString)
    {
        return BY_NAME_LOOKUP.get(StringUtils.lowerCase(fileTypeString));
    }
}
