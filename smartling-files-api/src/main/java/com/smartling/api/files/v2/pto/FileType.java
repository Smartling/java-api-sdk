package com.smartling.api.files.v2.pto;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum FileType
{
    JAVA_PROPERTIES("text/plain", true),            // Java resources
    IOS("text/plain", true),                        // iOS resources
    ANDROID("application/xml", true),               // Android resources
    GETTEXT("text/plain", true),                    // GetText .PO/.POT file
    XLIFF("application/xml", true),                 // XLIFF file
    YAML("text/plain", true),                       // Ruby/YAML file
    JSON("application/json", true),                 // generic JSON file
    XML("application/xml", true),                   // generic XML file
    HTML("text/html", true),                        // HTML file
    FREEMARKER("application/octet-stream", false),  // FreeMarker template
    DOCX("application/octet-stream", false),        // DOCX
    DOC("application/octet-stream", false),         // DOC file (Microsoft Word)
    PPTX("application/octet-stream", false),        // PPTX
    XLSX("application/octet-stream", false),        // XLSX
    XLS("application/octet-stream", false),         // XLS
    IDML("application/octet-stream", false),        // IDML
    RESX("application/xml", true),                  // .NET resource (.resx, .resw)
    QT("application/xml", true),                    // Qt Linguist (.TS file)
    CSV("text/csv", true),                          // CSV (Comma-separated values)
    PLAIN_TEXT("text/plain", true),                 // plain text (.txt files)
    PPT("application/octet-stream", false),         // PPT binary file format
    PRES("text/plain", true),                       // Pres resources
    STRINGSDICT("application/xml", true),           // iOS/OSX resources in dictionary format
    MADCAP("application/octet-stream", false),      // MADCAP file
    SRT("text/plain", false),                       // SubRip Text Format
    MARKDOWN("text/markdown", true),                // Markdown Text Format
    PHP_RESOURCE("text/plain", false),              // PHP resources
    TMX("application/xml", false),                  // TMX translation memory file format
    XLIFF_CAT("application/xml", false),            // XLIFF for offline CAT integration
    DITA("application/xml", false),                 // DITA file format
    PDF("application/octet-stream", false),         // PDF file format
    FLUENT("text/plain", true),                     // Mozilla Fluent parser
    DOCM("application/octet-stream", false),        // Microsoft Word with macros
    SVG("image/svg+xml", false),                    // SVG image
    VTT("text/vtt", false),                         // Video Text Tracks Format (subtitles)
    DITA_ZIP("application/octet-stream", false),    // DITA zip file format
    RTF("application/rtf", false),                  // Microsoft Rich Text Format
    FLARE("application/octet-stream", false),       // MadCap Flare parser
    XLIFF2("application/xml", false),               // XLIFF 2.0 parser
    ARB("application/json", false),                 // ARB parser
    XLSX_TEMPLATE("application/octet-stream", false);        // Smartling XLSX - https://help.smartling.com/hc/en-us/articles/1260804224670-Translating-Spreadsheets#h_01F3TKVZBVPFJSF2XZEBHDH1EQ
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
