package com.dockerapp.pluginapi.http;

import com.dockerapp.clientApi.entities.ClientResponse;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Test class for HttpUtil class.
 * <p/>
 * Created by Guilherme Ribeiro
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpUtil.class)
public class HttpUtilTest {

    @Mock
    private ClientResponse clientResponse;

    /**
     * Setup mocks.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(HttpUtil.class);
        mockStatic(HttpUtil.class, Mockito.CALLS_REAL_METHODS);
    }

    /**
     * Tests that createHttpRequest() returns an appropriate ClientResponse
     * and follows the HTTP route.
     */
    @Test
    public void testSendRequestHttp() throws Exception {
        ClientResponse expectedResponse = new ClientResponse(200, null, "Http");
        doReturn(expectedResponse).when(HttpUtil.class, "createHttpRequest", anyString(), anyMap(), anyString(), anyMap(), any());
        ClientResponse receivedResponse = HttpUtil.sendRequest("http:\\www.someURL.co", "someMethod", new HashMap<String, String>(), "authHeader", new HashMap<String, String>());
        assertEquals("sending Http request through HttpUtil has failed.", "Http", receivedResponse.getBody());
    }

    /**
     * Tests that createHttpRequest() returns an appropriate ClientResponse
     * and follows the HTTPS route.
     */
    @Test
    public void testSendRequestHttps() throws Exception {
        ClientResponse expectedResponse = new ClientResponse(200, null, "Https");
        doReturn(expectedResponse).when(HttpUtil.class, "createHttpsRequest", anyString(), anyMap(), anyString(), anyMap(), any());
        ClientResponse receivedResponse = HttpUtil.sendRequest("https:\\www.someURL.co", "someMethod", new HashMap<String, String>(), "authHeader", new HashMap<String, String>());
        assertEquals("Sending Https request through HttpUtil has failed.", "Https", receivedResponse.getBody());
    }

    /**
     * Tests sendRequest *https* calls private method createHttpRequest and
     * returns the correct response
     */
    @Test
    public void testcreateHttpsRequest() throws Exception {

        URL mockurl = mock(URL.class);
        HttpsURLConnection mockhuc = mock(HttpsURLConnection.class);
        InputStream stubInputStream = IOUtils.toInputStream("Https Requested!");

        PowerMockito.when(mockurl.openConnection()).thenReturn(mockhuc);
        PowerMockito.when(mockhuc.getInputStream()).thenReturn(stubInputStream);
        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(mockurl);
        PowerMockito.whenNew(DataOutputStream.class).withAnyArguments().thenReturn(mock(DataOutputStream.class));

        // maps
        Map<String, String> parameters = new HashMap<>();
        parameters.put("parameterA", "A");
        parameters.put("parameterB", "B");
        Map<String, String> headers = new HashMap<>();
        headers.put("A", "HeaderA");
        headers.put("B", "HeaderB");

        // test
        ClientResponse clientResponse = HttpUtil.sendRequest("https://www.someweb.site", "Method", parameters, "Authorization", headers);
        assertEquals("HttpUtil not creating an Https request correctly", "Https Requested!", clientResponse.getBody());
    }

    /**
     * Tests sendRequest *http* calls private method createHttpRequest and
     * returns the correct response
     */
    @Test
    public void testcreateHttpRequest() throws Exception {

        // mocking
        URL mockurl = mock(URL.class);
        HttpURLConnection mockhuc = mock(HttpURLConnection.class);
        InputStream stubInputStream = IOUtils.toInputStream("Http Requested!");
        PowerMockito.when(mockurl.getProtocol()).thenReturn("http");
        PowerMockito.when(mockurl.openConnection()).thenReturn(mockhuc);
        PowerMockito.when(mockhuc.getInputStream()).thenReturn(stubInputStream);
        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(mockurl);
        PowerMockito.whenNew(DataOutputStream.class).withAnyArguments().thenReturn(mock(DataOutputStream.class));

        // maps
        Map<String, String> parameters = new HashMap<>();
        parameters.put("parameterA", "A");
        parameters.put("parameterB", "B");
        Map<String, String> headers = new HashMap<>();
        headers.put("A", "HeaderA");
        headers.put("B", "HeaderB");

        // test
        ClientResponse clientResponse = HttpUtil.sendRequest("http://www.someweb.site", "Method", parameters, "Authorization", headers);
        assertEquals("HttpUtil not creating an Http request correctly", "Http Requested!", clientResponse.getBody());
    }

    /**
     * Tests private method mapToGenerateParameterList
     */
    @Test
    public void testGenerateParameterList() throws Exception {
        Map<String, String> mapToGenerateParameterList = new HashMap<>();
        mapToGenerateParameterList.put("a", "1");
        mapToGenerateParameterList.put("b", "2");
        mapToGenerateParameterList.put("c", "3");

        String resultCallGenerateParameterList = Whitebox.invokeMethod(HttpUtil.class, mapToGenerateParameterList);
        //TODO review problem with this test
        assertTrue("Parameter list not being generated correctly from provided map.", resultCallGenerateParameterList.contains("c=3"));
    }
}
