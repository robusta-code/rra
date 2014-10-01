/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package io.robusta.rra.client.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.robusta.rra.client.AbstractRestClient;
import io.robusta.rra.client.HttpMethod;
import io.robusta.rra.client.JdkRestClient;
import io.robusta.rra.utils.CoupleList;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Used both for synchronous and asynchronous
 * 
 * @author n.zozol
 */
public class AbstractRestClientTest {

    String               applicationUri = "http://localhost:8080/classify/test/";

    /*
     * AbstractRestClient[] clients = new AbstractRestClient[]{ new
     * JDKRestClient(applicationUri), new ApacheRestClient(applicationUri)};
     */

    AbstractRestClient[] clients        = new AbstractRestClient[] {
                                        new JdkRestClient( applicationUri ) };

    public AbstractRestClientTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testCheckConstructorUri() {
    }

    @Test
    public void testEncodeParameter() throws UnsupportedEncodingException {
        for ( AbstractRestClient client : clients ) {

            String expected = "A+B+C+%24%25";
            String actual = client.encodeParameter( "A B C $%" );
            assertEquals( expected, actual );
        }

        // Testing decodeParameters for different kinds : real UTF-8 encoding,
        // and Browser decoding
        // GWT : A%20B%20C%20$%25
        // Official : A+B+C+%24%25
        String expected = "A B C $%";
        String actual = URLDecoder.decode( "A+B+C+%24%25", "UTF-8" );
        System.out.println( "UTF8 ok" );
        assertEquals( expected, actual );
        actual = URLDecoder.decode( "A%20B%20C%20$%25", "UTF-8" );
        assertEquals( expected, actual );
        System.out.println( "FF UTF8 ok" );
    }

    @Test
    public void testPrepareMethod() {

        String relativePath = "user";
        CoupleList<String, Object> cl = CoupleList.<String, Object> build( "id", 12L, "username", "john doe" );
        for ( AbstractRestClient client : clients ) {
            String[] expected = new String[] { "http://localhost:8080/classify/test/user?id=12&username=john+doe", "" };
            String[] actual = client.prepareMethod( HttpMethod.GET, relativePath, cl, null );
            System.out.println("actual " + actual);
            assertEquals( expected[0], actual[0] );
            assertEquals( expected[1], actual[1] );
            assertTrue( Arrays.asList( actual ).toString(), Arrays.equals( actual, expected ) );

            /*
             * client.setContentType("form"); expected = new
             * String[]{"http://localhost:8080/classify/test/user",
             * "id=12\nusername=john+doe"}; actual =
             * client.prepareMethod(HttpMethod.POST, relativePath, cl);
             * assertEquals(expected[0], actual[0]);
             * System.out.println("excepted :"+expected[1]);
             * System.out.println("actual :"+actual[1]);
             * assertEquals(expected[1], actual[1]);
             * assertTrue(Arrays.asList(actual).toString(),
             * Arrays.equals(actual, expected));
             */

        }
    }

    @Test
    public void testSetAuthorizationValue() {
    }

    @Test
    public void testContentTypeIsForm() {
    }

    @Test
    public void testSetDefaultContentType() {
    }

    @Test
    public void testSetContentType() {
    }

    @Test
    public void testSetApplicationUri() {
    }

    @Test
    public void testSetNextRequestBody() {
    }

    @Test
    public void testGetHttpCode() {
    }

    @Test
    public void testGetResponse() {
    }

    @Test
    public void testClean() {
    }

}