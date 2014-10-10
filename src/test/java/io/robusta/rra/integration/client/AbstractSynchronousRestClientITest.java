package io.robusta.rra.integration.client;

import static org.junit.Assert.assertEquals;
import io.robusta.rra.client.AbstractRestClient;
import io.robusta.rra.client.JdkRestClient;
import io.robusta.rra.representation.implementation.GsonRepresentation;

import java.net.URISyntaxException;
import java.net.URL;

import org.apache.cxf.jaxrs.client.WebClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

//@RunWith(Arquillian.class)
public class AbstractSynchronousRestClientITest {

	
	/**
	 * ShrinkWrap is used to create a war file on the fly.
	 *
	 * The API is quite expressive and can build any possible flavor of war
	 * file. It can quite easily return a rebuilt war file as well.
	 *
	 * More than one @Deployment method is allowed.
	 * 
	 * @return WebArchive
	 */
//	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class).addClasses(AbstractRestClient.class, JdkRestClient.class);
	}

	/**
	 * This URL will contain the following URL data
	 *
	 * - http://<host>:<port>/<webapp>/
	 *
	 * This allows the test itself to be agnostic of server information or even
	 * the name of the webapp
	 *
	 */
	@ArquillianResource
	private URL webappUrl;
	private String applicationUri;
	private AbstractRestClient clients[];
	private String expected, actual;

	private WebClient initWebClient() throws URISyntaxException {
		return WebClient.create(webappUrl.toURI());

	}
	
//	@Test
	public void testPOST() throws URISyntaxException {
		applicationUri = initWebClient().getCurrentURI().toString();
		clients = new AbstractRestClient[] { new JdkRestClient(applicationUri) };
		expected = "{\"email\":\"email\",\"name\":\"name\"}\n";
		for (AbstractRestClient client : clients) {
			actual = client.POST("api/ad/create", new GsonRepresentation("{\"email\":\"email\", \"name\":\"name\"}"));
			// System.out.println("client.getHttpCode()="+client.getHttpCode());
			assertEquals(expected, actual);
		}
	}
}
