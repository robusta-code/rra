package io.robusta.rra.integration.client;

import static org.junit.Assert.assertTrue;
import io.robusta.rra.client.AbstractRestClient;
import io.robusta.rra.client.Callback;
import io.robusta.rra.client.JdkRestClient;
import io.robusta.rra.client.SimpleCallback;
import io.robusta.rra.exception.RestException;
import io.robusta.rra.representation.implementation.GsonRepresentation;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.cxf.jaxrs.client.WebClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

//@RunWith(Arquillian.class)
public class AbstractAsynchronousRestClientJDKITest {

	JdkRestClient client;

	/**
	 * ShrinkWrap is used to create a war file on the fly.
	 *
	 * The API is quite expressive and can build any possible flavor of war
	 * file. It can quite easily return a rebuilt war file as well.
	 *
	 * More than one @Deployment method is allowed.
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

	private WebClient initWebClient() throws URISyntaxException {
		return WebClient.create(webappUrl.toURI());

	}

//	@Test
	public void testSetAuthorizationValue() throws Exception {
//		 final Response response = initWebClient().path("api/jaxrs/validate").post(null);
		String applicationUri = initWebClient().getCurrentURI().toString();

		client = new JdkRestClient(applicationUri);
		client.setAuthorizationValue("James Bond");

		Callback cb = new SimpleCallback(client) {

			@Override
			public void onSuccess(InputStream inputStream) {

				System.out.println("success :" + new GsonRepresentation(inputStream).toString());
			}

			@Override
			public void onFailure(RestException ex) {
				ex.printStackTrace();
				System.out.println("fail : " + client.getHttpCode());
			}

			@Override
			public void onComplete() {
				System.out.println("complete");
			}
		};

		System.out.println("applicationUri 1 :::: " + applicationUri);
		System.out.println("clientUri 1 :::: " + client.getUnderlyingClient());
		client.post("api/jaxrs/validate", new GsonRepresentation("{\"email\":\"email\",\"name\":\"name\"}"), cb);
		client.join();
		System.out.println("applicationUri 2 :::: " + applicationUri);	
		System.out.println("client getContentType :::: " + client.getUnderlyingClient().getContentType());
		System.out.println("clientUri getRequestMethod :::: " + client.getUnderlyingClient().getRequestMethod());
		System.out.println("clientUrl :::: " + client.getUnderlyingClient().getURL());
		System.out.println("clientUri 2 :::: " + client.getUnderlyingClient());
		
		System.out.println("client.getHttpCode()=" + client.getHttpCode());
		assertTrue(client.getHttpCode() < 300);
		System.out.println("finished");

	}

	// ************************************************

	// public AbstractAsynchronousRestClientJDKITest() {
	// }

	// @Before
	// public void setUp() {
	// client = new JdkRestClient("http://localhost:8080/classify");
	// }

	// @Test
	public void testExecutePost() throws Exception {

		Callback cb = new SimpleCallback(client) {

			@Override
			public void onSuccess(InputStream inputStream) {

				System.out.println("success :" + new GsonRepresentation(inputStream).toString());
			}

			@Override
			public void onFailure(RestException ex) {
				ex.printStackTrace();
				System.out.println("fail : " + client.getHttpCode());
			}

			@Override
			public void onComplete() {
				System.out.println("complete");
			}
		};

		client.post("api/ad/test", new GsonRepresentation("{\"email\":\"email\",\"name\":\"name\"}"), cb);
		client.join();
		System.out.println("client.getHttpCode()=" + client.getHttpCode());
		assertTrue(client.getHttpCode() < 300);
		System.out.println("finished");
	}
}
