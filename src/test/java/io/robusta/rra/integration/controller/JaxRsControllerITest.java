package io.robusta.rra.integration.controller;

import static org.junit.Assert.assertEquals;
import io.robusta.rra.integration.controller.jaxRsController.implementation.JaxRsControllerImpl;
import io.robusta.rra.integration.controller.jaxRsController.implementation.MyClientPropertyJaxRs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class JaxRsControllerITest {
	/**
	 * ShrinkWrap is used to create a war file on the fly.
	 *
	 * The API is quite expressive and can build any possible flavor of war
	 * file. It can quite easily return a rebuilt war file as well.
	 *
	 * More than one @Deployment method is allowed.
	 */
	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class).addClasses(JaxRsControllerImpl.class, MyClientPropertyJaxRs.class);
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
	public void postAgent() throws URISyntaxException {
		final Response response = initWebClient().path("jaxrs/agent").post(null);
		assertEquals(200, response.getStatus());
	}

	@Test
	public void postAuth() throws URISyntaxException {
		final Response response = initWebClient().path("jaxrs/auth").post(null);
		assertEquals(426, response.getStatus());
	}

	@Test
	public void postValidate() throws URISyntaxException {
		String json = "{\"email\":\"email\",\"name\":\"name\"}";
		final Response response = initWebClient().path("jaxrs/validate").post(json);
		assertEquals(200, response.getStatus());

	}

	@Test
	public void getHeader() throws URISyntaxException, IOException {
		final Response response = initWebClient().path("jaxrs/header").get();
		assertEquals(200, response.getStatus());
	}

}
