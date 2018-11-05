package com.zikovam.services;

import com.zikovam.StartApplication;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TransferServiceTest {
    private static final String SERVICE_URL = "http://localhost:8080/api/transfer";

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
    public void givenPostTransfer_whenCorrectRequest_thenResponseCodeSuccess () throws IOException {

        String userFrom = "Elon%20Musk";
        String accountIdFrom = "1";
        String userTo = "Bear%20Grills";
        String accountIdTo = "3";
        String sum = "1000";

        final HttpUriRequest request = new HttpPost(SERVICE_URL +
                "/" + userFrom + "/" + accountIdFrom +
                "/" + userTo + "/" +accountIdTo +
                "/" +sum);

        final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
    }
}
