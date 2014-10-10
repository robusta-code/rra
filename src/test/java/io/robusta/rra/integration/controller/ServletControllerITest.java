package io.robusta.rra.integration.controller;

import static org.junit.Assert.assertEquals;
import io.robusta.rra.integration.controller.servletController.implementation.MyClientPropertyServlet;
import io.robusta.rra.integration.controller.servletController.implementation.ServletControllerImpl;

import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.core.MediaType;
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
public class ServletControllerITest {
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
		return ShrinkWrap.create(WebArchive.class).addClasses(ServletControllerImpl.class,
				MyClientPropertyServlet.class);
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
		return WebClient.create(webappUrl.toURI()).path("test").accept(MediaType.APPLICATION_JSON)
				.type("application/json");

	}

	@Test
	public void doPost() throws URISyntaxException {
		String json = "{\"email\":\"email\",\"name\":\"name\"}";
		final Response response = initWebClient().post(json);
		assertEquals(200, response.getStatus());
	}

	@Test
	public void doGet() throws URISyntaxException {
		final Response response = initWebClient().get();
		assertEquals(200, response.getStatus());
	}

}
