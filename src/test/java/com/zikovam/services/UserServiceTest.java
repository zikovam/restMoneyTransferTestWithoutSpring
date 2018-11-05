package com.zikovam.services;

import com.zikovam.StartApplication;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class UserServiceTest {

    private static final String SERVICE_URL = "http://localhost:8080/api/users";

    private HttpServer httpServer;

    @Before
    public void setUp () {
        httpServer = StartApplication.startServer();

        StartApplication.fillingDatabase();
    }

    @After
    public void tearDown () {
        httpServer.shutdown();
    }

    @Test
    public void givenGetAllEmployees_whenCorrectRequest_thenResponseCodeSuccess () throws IOException {
        final HttpUriRequest request = new HttpGet(SERVICE_URL);
        final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void givenGetEmployee_whenEmployeeExists_thenResponseodeSuccessAndUserIsCorrect () throws IOException {
        final HttpUriRequest request = new HttpGet(SERVICE_URL + "/1");
        final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

        String user;
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        user = reader.readLine();

        assertEquals("User{" +
                "id:1," +
                "name:'Elon Musk'," +
                "accounts:[" +
                "Account{id:1,balance:'2000'}, " +
                "Account{id:2,balance:'5000'}" +
                "]}", user);
    }

    @Test
    public void givenGetEmployee_whenEmployeeDoesNotExist_thenResponseCodeNotFound () throws IOException {
        final HttpUriRequest request = new HttpGet(SERVICE_URL + "/1000");
        final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_NOT_FOUND, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void givenAddEmployee_whenEmployeeDoesNotExist_thenResponseCodeCreated () throws IOException {

        String user = "test%20user";

        final HttpUriRequest request = new HttpPost(SERVICE_URL + "/" + user);
        final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_CREATED, httpResponse.getStatusLine().getStatusCode());
    }
}