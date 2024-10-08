package org.irods.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for sending HTTP requests and processing responses.
 * Provides methods to send POST and GET requests and handle the responses.
 */
public class HttpRequestUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static HttpResponse<String> response;

    /**
     * Sends a POST HTTP request with form data and parses the response.
     *
     * @param formData A map containing the parameters for the HTTP request, where keys and values are converted
     *                 to a query string format (e.g., "key1=value1&key2=value2").
     * @param baseUrl The base URL to which the POST request is sent.
     * @param token The authorization token to be included in the request header.
     * @param client The {@link HttpClient} instance used to send the request.
     * @return The {@link HttpResponse} object containing the server's response as a String.
     */
    public static HttpResponse<String> sendAndParsePOST(Map<Object, Object> formData, String baseUrl, String token,
                                                        HttpClient client) {
        /// creating the request body
        String form = formData.entrySet()
                .stream()
                .map(Map.Entry::toString) // method reference to Map.Entry.toString(): key=value
                .collect(Collectors.joining("&"));

        // creating the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        // sending request
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a GET HTTP request with form data appended to the URL and parses the response.
     *
     * @param formData A map containing the parameters for the HTTP request, where keys and values are converted
     *                 to a query string format (e.g., "key1=value1&key2=value2").
     * @param baseUrl The base URL to which the GET request is sent. Form data is appended as query parameters.
     * @param token The authorization token to be included in the request header.
     * @param client The {@link HttpClient} instance used to send the request.
     * @return The {@link HttpResponse} object containing the server's response as a String.
     */
    public static HttpResponse<String>  sendAndParseGET(Map<Object, Object> formData, String baseUrl, String token,
                                                        HttpClient client)  {
        // creating the request body
        String form = formData.entrySet()
                .stream()
                .map(Map.Entry::toString) // method reference to Map.Entry.toString(): key=value
                .collect(Collectors.joining("&"));

        // creating the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?" + form))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        // sending request
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}