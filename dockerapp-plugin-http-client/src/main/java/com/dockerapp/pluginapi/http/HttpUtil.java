package com.dockerapp.pluginapi.http;

import com.dockerapp.clientApi.entities.ClientResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Helper class for HTTP request / response.
 *
 * @author Zsolt Rabi
 */
public final class HttpUtil {

    public static final String HTTP_METHOD_POST = "POST";
    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_OPTIONS = "OPTIONS";
    public static final String HTTP_METHOD_PUT = "PUT";
    public static final String REQUEST_BODY_PARAMETER = "requestBody";

    private static final String AUTH_TOKEN_KEY = "Basic ";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    private HttpUtil() {

    }

    /**
     * Sends a HTTP request
     *
     * @param url        is the address to send to
     * @param method     is the HTTP method
     * @param parameters is the map of parameters
     * @param headers
     * @return the result of the request.
     */
    public static ClientResponse sendRequest(String url, String method, Map<String, String> parameters, String authHeader, Map<String, String> headers) throws IOException {
        ClientResponse response = null;

        System.out.println(">>>>>>>>>>>>>>>" + url);

        URL urlObject = new URL(url);

        response = "http".equals(urlObject.getProtocol()) ? createHttpRequest(method, parameters, authHeader, headers, urlObject) : createHttpsRequest(method, parameters, authHeader, headers, urlObject);

        return response;
    }

    private static ClientResponse createHttpsRequest(String method, Map<String, String> parameters, String authHeader, Map<String, String> headers, URL url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        ClientResponse response = fulfillConnection(method, parameters, authHeader, headers, connection);

        return response;
    }

    private static ClientResponse createHttpRequest(String method, Map<String, String> parameters, String authHeader, Map<String, String> headers, URL urlObject) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        ClientResponse response = fulfillConnection(method, parameters, authHeader, headers, connection);

        return response;
    }

    private static ClientResponse fulfillConnection(String method, Map<String, String> parameters, String authHeader, Map<String, String> headers, HttpURLConnection connection) throws IOException {
        connection.setRequestMethod(method);
        IOException exception = null;
        StringBuffer responseFromA = new StringBuffer();
        connection.setDoOutput(true);
        connection.setDoInput(true);

        if (headers != null) {
            for (Entry<String, String> header : headers.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
        } else {
            connection.setRequestProperty("User-Agent", USER_AGENT);
        }

        if (authHeader != null) {
            connection.setRequestProperty("Authorization", AUTH_TOKEN_KEY + authHeader);
        }

        if (parameters != null && !parameters.isEmpty()) {

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(generateParameterList(parameters));
            wr.flush();
            wr.close();
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            exception = e;
        }

        String inputLine = "";
        while ((inputLine = in.readLine()) != null) {
            responseFromA.append(inputLine);
        }

        in.close();

        if (exception != null) {
            // Let's see the errors
            LOGGER.error("Error output: ", responseFromA.toString());
            throw new IOException(exception);
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>" + responseFromA.toString());
        return new ClientResponse(connection.getResponseCode(), connection.getHeaderFields(), responseFromA.toString());
    }

    /**
     * Generates a parameter list from the given map.
     *
     * @param parameters is the map of the paramteres
     * @return the parameters as String
     */
    private static String generateParameterList(Map<String, String> parameters) {
        StringBuffer urlParameters = new StringBuffer();

        if (parameters.size() == 1 && parameters.keySet().contains(REQUEST_BODY_PARAMETER)) {
            urlParameters.append(parameters.get(REQUEST_BODY_PARAMETER));
        } else {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                if (urlParameters.length() > 0) {
                    urlParameters.append("&");
                }
                urlParameters.append(entry.getKey() + "=" + entry.getValue());
            }
        }

        return urlParameters.toString();
    }

    public static <K, V> ClientResponse sendRequestWPayload(String url, String method, String authHeader, Map<K, V> payload) throws IOException {
        ClientResponse response = null;

        URL urlObject = new URL(url);

        response = "http".equals(urlObject.getProtocol()) ? createHttpRequestWithPayload(method, authHeader, urlObject, payload) : createHttpsRequestWithPayload(method, authHeader, urlObject, payload);

        return response;
    }

    private static <K, V> ClientResponse createHttpsRequestWithPayload(String method, String authHeader, URL urlObject, Map<K, V> payload) throws IOException {
        System.setProperty("jsse.enableSNIExtension", "false");

        HttpsURLConnection connection = (HttpsURLConnection) urlObject.openConnection();

        ClientResponse response = fulfillConnectionWithPayload(method, authHeader, connection, payload);

        return response;
    }

    private static <K, V> ClientResponse createHttpRequestWithPayload(String method, String authHeader, URL urlObject, Map<K, V> payload) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        ClientResponse response = fulfillConnectionWithPayload(method, authHeader, connection, payload);

        return response;
    }

    private static <K, V> ClientResponse fulfillConnectionWithPayload(String method, String authHeader, HttpURLConnection connection, Map<K, V> payload) throws IOException {
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");

        StringBuffer responseFromA = new StringBuffer();
        StringBuffer error = new StringBuffer();

        connection.setRequestProperty("User-Agent", USER_AGENT);

        if (authHeader != null) {
            connection.setRequestProperty("Authorization", AUTH_TOKEN_KEY + authHeader);
        }

        connection.setDoOutput(true);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        ObjectMapper om = new ObjectMapper();
        out.write(om.writer().writeValueAsString(payload));
        out.close();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
                responseFromA.append(inputLine);
            }
            in.close();

        } catch (IOException e) {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

            String errorLine = "";
            while ((errorLine = errorReader.readLine()) != null) {
                error.append(errorLine);
            }
            errorReader.close();

            // Let's see the errors
            LOGGER.error("Request failed: ", e);
            LOGGER.error("Error output: ", error.toString());
            throw new IOException(e);
        }

        return new ClientResponse(connection.getResponseCode(), connection.getHeaderFields(), responseFromA.toString());
    }
}
