/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.robusta.rra.client.implementation;

import static org.junit.Assert.assertTrue;
import io.robusta.rra.client.Callback;
import io.robusta.rra.client.JdkRestClient;
import io.robusta.rra.client.SimpleCallback;
import io.robusta.rra.exception.RestException;
import io.robusta.rra.representation.implementation.GsonRepresentation;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author robusta web
 */
public class AbstractAsynchronousRestClientJDKTest {

    JdkRestClient client;

    public AbstractAsynchronousRestClientJDKTest() {
    }

    // @BeforeClass
    public static void setUpClass() throws Exception {
    }

    // @AfterClass
    public static void tearDownClass() throws Exception {
    }

//    @Before
    public void setUp() {
        client = new JdkRestClient( "http://localhost:8080/classify" );
    }

    // @After
    public void tearDown() {
    }

    @Test
    public void testSetApplicationUri() {
    }

//     @Test
    public void testSetAuthorizationValue() throws Exception {
        client.setAuthorizationValue( "James Bond" );

        Callback cb = new SimpleCallback( client ) {

            @Override
            public void onSuccess( InputStream inputStream ) {

                System.out.println( "success :" + new GsonRepresentation( inputStream ).toString() );
            }

            @Override
            public void onFailure( RestException ex ) {
                ex.printStackTrace();
                System.out.println( "fail : " + client.getHttpCode() );
            }

            @Override
            public void onComplete() {
                System.out.println( "complete" );
            }
        };

		client.post("api/ad/create", new GsonRepresentation("{\"email\":\"email\",\"name\":\"name\"}"), cb);
		client.join();
		System.out.println("client.getHttpCode()=" + client.getHttpCode());
		assertTrue(client.getHttpCode() < 300);
		System.out.println("finished");

    }

    @Test
    public void testSetContentType() {
    }

    @Test
    public void testSetRequestBody() {
    }

    @Test
    public void testExecuteGet() throws Exception {
    }

    @Test
    public void testExecuteMethod() {
    }

   // @Test
    public void testExecutePost() throws Exception {

        Callback cb = new SimpleCallback( client ) {

            @Override
            public void onSuccess( InputStream inputStream ) {

                System.out.println( "success :" + new GsonRepresentation( inputStream ).toString() );
            }

            @Override
            public void onFailure( RestException ex ) {
                ex.printStackTrace();
                System.out.println( "fail : " + client.getHttpCode() );
            }

            @Override
            public void onComplete() {
                System.out.println( "complete" );
            }
        };

        client.post( "api/ad/test", new GsonRepresentation( "{\"email\":\"email\",\"name\":\"name\"}" ), cb );
        client.join();
        System.out.println( "client.getHttpCode()=" + client.getHttpCode() );
        assertTrue( client.getHttpCode() < 300 );
        System.out.println( "finished" );
    }

    @Test
    public void testExecutePut() throws Exception {
    }

    @Test
    public void testExecuteDelete() throws Exception {
    }

    @Test
    public void testGetLastStatusCode() {
    }

    @Test
    public void testGetLastResponse() {
    }
}
