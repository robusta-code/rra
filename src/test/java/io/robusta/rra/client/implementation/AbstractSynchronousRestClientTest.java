package io.robusta.rra.client.implementation;

import static org.junit.Assert.assertEquals;
import io.robusta.rra.client.AbstractRestClient;
import io.robusta.rra.client.JdkRestClient;
import io.robusta.rra.representation.implementation.GsonRepresentation;
import io.robusta.rra.utils.CoupleList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author n.zozol
 */
public class AbstractSynchronousRestClientTest {

    String             applicationUri = "http://localhost:8080/classify";
    /*
     * AbstractSynchronousRestClient[] clients = new
     * AbstractSynchronousRestClient[]{ new SunRestClient(applicationUri), new
     * ApacheRestClient(applicationUri)};
     */

    AbstractRestClient clients[];
    String             expected, actual;

    public AbstractSynchronousRestClientTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        this.clients = new AbstractRestClient[] {
                new JdkRestClient( applicationUri ) };
    }

    @After
    public void tearDown() {
    }

    // @Test
    public void testPOST() {
        expected = "{\"email\":\"email\",\"name\":\"name\"}\n";
        for ( AbstractRestClient client : clients ) {
            actual = client.POST( "api/ad/test", new GsonRepresentation( "{\"email\":\"email\", \"name\":\"name\"}" ) );
            // System.out.println("client.getHttpCode()="+client.getHttpCode());
            assertEquals( expected, actual );
        }
    }

    // @Test
    public void testGET() {
        expected = "ok\n";
        for ( AbstractRestClient client : clients ) {
            actual = client.GET( "client", null );
            // System.out.println("client.getHttpCode()="+client.getHttpCode());
            assertEquals( expected, actual );
        }
    }

    @Test
    public void testPUT() {
    }

    @Test
    public void testDELETE() {
    }

    // @Test
    public void testEncodeParameter() {
        expected = "12\n";
        for ( AbstractRestClient client : clients ) {
            actual = client.GET( "client", CoupleList.build( "p1", 12 ) );
            assertEquals( expected, actual );
        }
    }

    @Test
    public void testExecuteMethod() {
    }
}
