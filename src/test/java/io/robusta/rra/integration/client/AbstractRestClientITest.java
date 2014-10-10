package io.robusta.rra.integration.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.robusta.rra.client.AbstractRestClient;
import io.robusta.rra.client.HttpMethod;
import io.robusta.rra.client.JdkRestClient;
import io.robusta.rra.utils.CoupleList;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.cxf.jaxrs.client.WebClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AbstractRestClientITest {

	String applicationUri = null;
	AbstractRestClient[] clients = null;
	
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
	@Deployment
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

	private WebClient initWebClient() throws URISyntaxException {
		return WebClient.create(webappUrl.toURI());

	}

	@Test
	public void postAgent() throws URISyntaxException, UnsupportedEncodingException {

		initWebClient().path("test");

		applicationUri = initWebClient().getCurrentURI().toString();

		clients = new AbstractRestClient[] { new JdkRestClient(applicationUri) };

		for (AbstractRestClient client : clients) {

			String expected = "A+B+C+%24%25";
			String actual = client.encodeParameter("A B C $%");
			assertEquals(expected, actual);
		}

		// Testing decodeParameters for different kinds : real UTF-8 encoding,
		// and Browser decoding
		// GWT : A%20B%20C%20$%25
		// Official : A+B+C+%24%25
		String expected = "A B C $%";
		String actual = URLDecoder.decode("A+B+C+%24%25", "UTF-8");
		System.out.println("UTF8 ok");
		assertEquals(expected, actual);
		actual = URLDecoder.decode("A%20B%20C%20$%25", "UTF-8");
		assertEquals(expected, actual);
		System.out.println("FF UTF8 ok");
	}

	@Test
	public void testPrepareMethod() throws URISyntaxException {

		String applicationUri = initWebClient().getCurrentURI().toString();

		String relativePath = "test/user";

		clients = new AbstractRestClient[] { new JdkRestClient(applicationUri) };
		CoupleList<String, Object> cl = CoupleList.<String, Object> build("id", 12L, "username", "john doe");
		for (AbstractRestClient client : clients) {
			String[] expected = new String[] { "http://localhost:8080/classify/test/user?id=12&username=john+doe", "" };
			String[] actual = client.prepareMethod(HttpMethod.GET, relativePath, cl, null);

			assertTrue(actual[0].startsWith("http://localhost:"));
			assertTrue(actual[0].endsWith("user?id=12&username=john+doe"));
		}
	}

}
