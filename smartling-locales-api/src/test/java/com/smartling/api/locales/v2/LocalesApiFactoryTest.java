package com.smartling.api.locales.v2;

import com.smartling.api.locales.v2.pto.LanguagePTO;
import com.smartling.api.locales.v2.pto.LocalePTO;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.response.ListResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class LocalesApiFactoryTest
{
    private static final String SUCCESS_RESPONSE_ENVELOPE = "{ \"response\": { \"code\": \"SUCCESS\", \"data\": %s } })";

    private static final String GET_LOCALES_DATA_JSON =
            "{"
                    + "   \"totalCount\":5,"
                    + "            \"items\": [{"
                    + "                \"localeId\": \"aa\","
                    + "                \"description\": \"Afar\","
                    + "                \"language\": {"
                    + "                    \"languageId\": \"aa\","
                    + "                    \"description\": \"Afar\","
                    + "                    \"direction\": \"LTR\","
                    + "                    \"wordDelimiter\": \"CHARACTER\""
                    + "                },"
                    + "                \"country\": null"
                    + "            }, {"
                    + "                \"localeId\": \"aa-ET\","
                    + "                \"description\": \"Afar (Ethiopia)\","
                    + "                \"language\": {"
                    + "                    \"languageId\": \"aa\","
                    + "                    \"description\": \"Afar\","
                    + "                    \"direction\": \"RTL\","
                    + "                    \"wordDelimiter\": \"SPACE\""
                    + "                },"
                    + "                \"country\": {"
                    + "                    \"countryId\": \"ET\","
                    + "                    \"description\": \"Ethiopia\""
                    + "                }"
                    + "            }, {"
                    + "                \"localeId\": \"ab\","
                    + "                \"description\": \"Abkhazian\","
                    + "                \"language\": {"
                    + "                    \"languageId\": \"ab\","
                    + "                    \"description\": \"Abkhazian\","
                    + "                    \"direction\": \"LTR\","
                    + "                    \"wordDelimiter\": \"SPACE\""
                    + "                },"
                    + "                \"country\": null"
                    + "            }, {"
                    + "                \"localeId\": \"zh-TW\","
                    + "                \"description\": \"Chinese (Traditional)\","
                    + "                \"language\": {"
                    + "                    \"languageId\": \"zh\","
                    + "                    \"description\": \"Chinese\","
                    + "                    \"direction\": \"LTR\","
                    + "                    \"wordDelimiter\": \"SPACE\""
                    + "                },"
                    + "                \"country\": {"
                    + "                    \"countryId\": \"TW\","
                    + "                    \"description\": \"Taiwan\""
                    + "                }"
                    + "            }, {"
                    + "                \"localeId\": \"zu-ZA\","
                    + "                \"description\": \"Zulu (South Africa)\","
                    + "                \"language\": {"
                    + "                    \"languageId\": \"zu\","
                    + "                    \"description\": \"Zulu\","
                    + "                    \"direction\": \"RTL\","
                    + "                    \"wordDelimiter\": \"CHARACTER\""
                    + "                },"
                    + "                \"country\": {"
                    + "                    \"countryId\": \"ZA\","
                    + "                    \"description\": \"South Africa\""
                    + "                }"
                    + "            }]"
                    + "        }";

    private static final String GET_FILTERED_LOCALES_DATA_JSON =
            "{"
                    + "   \"totalCount\":2,"
                    + "            \"items\": [{"
                    + "                \"localeId\": \"aa\","
                    + "                \"description\": \"Afar\","
                    + "                \"language\": {"
                    + "                    \"languageId\": \"aa\","
                    + "                    \"description\": \"Afar\","
                    + "                    \"direction\": \"LTR\","
                    + "                    \"wordDelimiter\": \"CHARACTER\""
                    + "                },"
                    + "                \"country\": null"
                    + "            }, {"
                    + "                \"localeId\": \"zh-TW\","
                    + "                \"description\": \"Chinese (Traditional)\","
                    + "                \"language\": {"
                    + "                    \"languageId\": \"zh\","
                    + "                    \"description\": \"Chinese\","
                    + "                    \"direction\": \"RTL\","
                    + "                    \"wordDelimiter\": \"SPACE\""
                    + "                },"
                    + "                \"country\": {"
                    + "                    \"countryId\": \"TW\","
                    + "                    \"description\": \"Taiwan\""
                    + "                }"
                    + "            }]"
                    + "        }";

    private static final String GET_FILTERED_EXTENDED_LOCALES_DATA_JSON =
            "{"
                    + "   \"totalCount\":2,"
                    + "            \"items\": [{"
                    + "                \"localeId\": \"aa\","
                    + "                \"description\": \"Afar\","
                    + "                \"language\": {"
                    + "                    \"languageId\": \"aa\","
                    + "                    \"description\": \"Afar\","
                    + "                    \"direction\": \"LTR\","
                    + "                    \"wordDelimiter\": \"CHARACTER\""
                    + "                },"
                    + "                \"country\": null,"
                    + "                \"pluralTags\":[\"ONE\",\"OTHER\"]"
                    + "            }, {"
                    + "                \"localeId\": \"zh-TW\","
                    + "                \"description\": \"Chinese (Traditional)\","
                    + "                \"language\": {"
                    + "                    \"languageId\": \"zh\","
                    + "                    \"description\": \"Chinese\","
                    + "                    \"direction\": \"RTL\","
                    + "                    \"wordDelimiter\": \"SPACE\""
                    + "                },"
                    + "                \"country\": {"
                    + "                    \"countryId\": \"TW\","
                    + "                    \"description\": \"Taiwan\""
                    + "                },"
                    + "                \"pluralTags\":[\"ONE\",\"MANY\"]"
                    + "            }]"
                    + "        }";

    private static final String LANGUAGES_DATA_JSON =
            "{\"totalCount\":2,\"items\":[{\"languageId\":\"fr\",\"description\":\"French\",\"direction\":\"LTR\",\"wordDelimiter\":\"CHARACTER\"},{\"languageId\":\"en\",\"description\":\"English\",\"direction\":\"RTL\",\"wordDelimiter\":\"SPACE\"}]}";

    private MockWebServer mockWebServer;
    private LocalesApi    localesApi;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final LocalesApiFactory factory = new LocalesApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");
        final ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();

        localesApi = factory.buildApi(tokenFilter, config);
    }

    @After
    public void tearDown() throws Exception
    {
        mockWebServer.shutdown();
    }

    private void assignResponse(final int httpStatusCode, final String body)
    {
        final MockResponse response = new MockResponse()
                .setResponseCode(httpStatusCode)
                .setHeader(HttpHeaders.CONTENT_LENGTH, body.length())
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .setBody(body);

        mockWebServer.enqueue(response);
    }

    @Test
    public void testGetAllLocales() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, GET_LOCALES_DATA_JSON));

        final ListResponse<LocalePTO> locales = localesApi.getLocalesAsDictionary(null);

        final RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertTrue(request.getPath().contains("/locales-api/v2/dictionary/locales"));
        assertFalse(request.getPath().contains("?")); //no params are passed

        assertNotNull(locales);
        assertEquals(5, locales.getTotalCount());
        assertEquals(5, locales.getItems().size());
        validateLocale(locales.getItems().get(0), "aa", "Afar", "aa", "Afar", "LTR", "CHARACTER", false, null, null);
        validateLocale(locales.getItems().get(1), "aa-ET", "Afar (Ethiopia)", "aa", "Afar",  "RTL", "SPACE", true, "ET", "Ethiopia");
        validateLocale(locales.getItems().get(2), "ab", "Abkhazian", "ab", "Abkhazian", "LTR", "SPACE", false, null, null);
        validateLocale(locales.getItems().get(3), "zh-TW", "Chinese (Traditional)", "zh", "Chinese", "LTR", "SPACE", true, "TW", "Taiwan");
        validateLocale(locales.getItems().get(4), "zu-ZA", "Zulu (South Africa)", "zu", "Zulu", "RTL", "CHARACTER", true, "ZA", "South Africa");
    }


    @Test
    public void testGetFilteredLocales() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, GET_FILTERED_LOCALES_DATA_JSON));

        final List<String> localeIds = new LinkedList<>();
        localeIds.add("aa");
        localeIds.add("zh-TW");

        final ListResponse<LocalePTO> filteredLocales = localesApi.getLocalesAsDictionary(localeIds);

        final RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertTrue(request.getPath().contains("/locales-api/v2/dictionary/locales"));
        assertTrue(request.getPath().contains(String.format("localeIds=%s", localeIds.get(0))));
        assertTrue(request.getPath().contains(String.format("localeIds=%s", localeIds.get(1))));

        assertNotNull(filteredLocales);
        assertEquals(2, filteredLocales.getTotalCount());
        assertEquals(2, filteredLocales.getItems().size());
        validateLocale(filteredLocales.getItems().get(0), "aa", "Afar", "aa", "Afar", "LTR", "CHARACTER", false, null, null);
        validateLocale(filteredLocales.getItems().get(1), "zh-TW", "Chinese (Traditional)", "zh", "Chinese", "RTL", "SPACE", true, "TW", "Taiwan");

    }

    private void validateLanguage(LanguagePTO languagePTO, String languageId, String languageDescription, String direction, String wordDelimiter)
    {
        assertEquals(languageId, languagePTO.getLanguageId());
        assertEquals(languageDescription, languagePTO.getDescription());
        assertEquals(direction, languagePTO.getDirection());
        assertEquals(wordDelimiter, languagePTO.getWordDelimiter());
    }

    private void validateLocale(LocalePTO localePTO, String localeId, String localeDescription, String languageId, String languageDescription, String languageDirection, String languageWordDelimiter, boolean countryExpected, String countryId, Object countryDescription)
    {
        validateLocale(localePTO, localeId, localeDescription);
        validateLanguage(localePTO.getLanguage(), languageId, languageDescription, languageDirection, languageWordDelimiter);
        if (countryExpected)
        {
            assertNotNull(localePTO.getCountry());
            validateCountry(localePTO, countryId, countryDescription);
        }
        else
        {
            assertNull(localePTO.getCountry());
        }
    }

    private void validateCountry(LocalePTO localePTO, String countryId, Object countryDescription)
    {
        assertEquals(countryId, localePTO.getCountry().getCountryId());
        assertEquals(countryDescription, localePTO.getCountry().getDescription());
    }

    private void validateLocale(LocalePTO localePTO, String localeId, String localeDescription)
    {
        assertEquals(localeId, localePTO.getLocaleId());
        assertEquals(localeDescription, localePTO.getDescription());
    }
}
