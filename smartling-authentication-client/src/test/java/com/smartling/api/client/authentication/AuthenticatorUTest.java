package com.smartling.api.client.authentication;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest({Authenticator.class, Authentication.class, System.class})
public class AuthenticatorUTest
{
    private final static String USERNAME = "someuser";
    private final static String PASSWORD = "pass";
    private static final String ACCESS_TOKEN = "dsfahsdlfhsdf";
    private static final String REFRESH_TOKEN = "aaaaaaa";
    private static final int EXPIRES_IN = 480;
    private static final int REFRESH_EXPIRES_IN = 3600;
    private static final String TOKEN_TYPE = "Bearer";

    @Mock
    private HttpClient httpClient;

    private Authenticator authenticator;
    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(httpClient);
        authenticator = spy(new Authenticator(USERNAME, PASSWORD));
        PowerMockito.mockStatic(System.class);

    }

    @Test
    public void testGetAccessToken() throws IOException
    {
        ResponseEntity<JsonObject> response = new ResponseEntity<JsonObject>(200, "OK", createOkResponse());
        when(httpClient.post(eq("https://api.smartling.com/auth-api/v2/authenticate"), eq(expectedAuthRequest()), anyString())).thenReturn(response);

        assertEquals(ACCESS_TOKEN, authenticator.getAccessToken());
    }

    @Test
    public void testGetAccessTokenRefreshToken() throws IOException
    {
        long currentTime = System.currentTimeMillis();
        PowerMockito.when(System.currentTimeMillis()).thenReturn(currentTime);
        ResponseEntity<JsonObject> response = new ResponseEntity<JsonObject>(200, "OK", createOkResponse());
        ResponseEntity<JsonObject> refreshResponse = new ResponseEntity<JsonObject>(200, "OK", createOkResponse(ACCESS_TOKEN + 2));
        when(httpClient.post(eq("https://api.smartling.com/auth-api/v2/authenticate"), eq(expectedAuthRequest()), anyString())).thenReturn(response);
        when(httpClient.post(eq("https://api.smartling.com/auth-api/v2/authenticate/refresh"), eq(expectedRefreshRequest()), anyString())).thenReturn(refreshResponse);

        assertEquals(ACCESS_TOKEN, authenticator.getAccessToken());

        PowerMockito.when(System.currentTimeMillis()).thenReturn(currentTime + EXPIRES_IN * 1000 + 1);
        assertEquals(ACCESS_TOKEN + 2, authenticator.getAccessToken());
        InOrder inOrder = Mockito.inOrder(httpClient);

        inOrder.verify(httpClient).post(eq("https://api.smartling.com/auth-api/v2/authenticate"), any(JsonValue.class), anyString());
        inOrder.verify(httpClient).post(eq("https://api.smartling.com/auth-api/v2/authenticate/refresh"), any(JsonValue.class), anyString());

    }

    @Test
    public void testAllThreadsAreLockedOnRefresh() throws IOException, InterruptedException, ExecutionException
    {
        final String expectedAccessToken = "Token returned on first refresh";
        long currentTime = System.currentTimeMillis();
        PowerMockito.when(System.currentTimeMillis()).thenReturn(currentTime);
        when(httpClient.post(eq("https://api.smartling.com/auth-api/v2/authenticate"), eq(expectedAuthRequest()), anyString()))
                .thenReturn(createRequestOrRefreshResponse("Initial token response"));
        authenticator.getAccessToken();

        //this to simulate that refresh is required
        PowerMockito.when(System.currentTimeMillis()).thenReturn(currentTime + EXPIRES_IN * 1000 + 1);
        reset(httpClient);
        when(httpClient.post(eq("https://api.smartling.com/auth-api/v2/authenticate/refresh"), eq(expectedRefreshRequest()), anyString()))
                .thenAnswer(createRefreshAnswer(expectedAccessToken))
                .thenAnswer(createRefreshAnswer("refresh2"))
                .thenAnswer(createRefreshAnswer("refresh3"))
                .thenAnswer(createRefreshAnswer("refresh4"))
                .thenAnswer(createRefreshAnswer("refresh5"));
        doReturn(createRequestOrRefreshResponse("some response")).when(httpClient).post(eq("https://api.smartling.com/auth-api/v2/authenticate"), eq(expectedAuthRequest()), anyString());


        int amountOfConcurrentThreads = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(amountOfConcurrentThreads);
        List<Callable<String>> tokenFetchers = new ArrayList<Callable<String>>();
        for (int threadCounter = 0; threadCounter < amountOfConcurrentThreads; threadCounter++)
            tokenFetchers.add(new AccessTokenFetcher(authenticator));
        List<Future<String>> futures = executorService.invokeAll(tokenFetchers);

        //make sure only single refresh was made and everyone else, stuck waiting for it got it
        for (Future<String> future : futures)
            assertEquals(expectedAccessToken, future.get());
        verify(httpClient).post(eq("https://api.smartling.com/auth-api/v2/authenticate/refresh"), any(JsonValue.class), anyString());
        verifyNoMoreInteractions(httpClient);
    }

    private Answer<ResponseEntity<JsonObject>> createRefreshAnswer(final String accessToken)
    {
        return new Answer<ResponseEntity<JsonObject>>()
        {
            @Override
            public ResponseEntity<JsonObject> answer(InvocationOnMock invocation) throws Throwable
            {
                //simulate long auth request execution
                Thread.sleep(5000);
                return createRequestOrRefreshResponse(accessToken);
            }
        };
    }

    private ResponseEntity<JsonObject> createRequestOrRefreshResponse(String accessToken) throws IOException
    {
        return new ResponseEntity<JsonObject>(200, "OK", createOkResponse(accessToken));
    }


    private JsonObject createOkResponse() throws IOException
    {
        return createOkResponse(ACCESS_TOKEN);
    }

    private JsonObject createOkResponse(String accessToken) throws IOException
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"response\": {\"code\": \"SUCCESS\",\"data\": {\"accessToken\": \"")
                .append(accessToken)
                .append("\",\"refreshToken\": \"")
                .append(REFRESH_TOKEN)
                .append("\",\"expiresIn\": ")
                .append(EXPIRES_IN)
                .append(",\"refreshExpiresIn\": ")
                .append(REFRESH_EXPIRES_IN)
                .append(",\"tokenType\": \"")
                .append(TOKEN_TYPE)
                .append("\"}}}");

        return Json.parse(new StringReader(builder.toString())).asObject();
    }

    private JsonValue expectedAuthRequest()
    {
        JsonObject request = new JsonObject();
        request.add("userIdentifier", USERNAME);
        request.add("userSecret", PASSWORD);

        return request;

    }


    private JsonValue expectedRefreshRequest()
    {
        JsonObject request = new JsonObject();
        request.add("refreshToken", REFRESH_TOKEN);

        return request;

    }
}
