package io.robusta.rra.integration.controller.jaxRsController.implementation;

import io.robusta.rra.controller.JaxRsController;
import io.robusta.rra.exception.HttpException;
import io.robusta.rra.representation.Representation;
import io.robusta.rra.representation.implementation.GsonRepresentation;
import io.robusta.rra.representation.implementation.JacksonRepresentation;

import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@Path( "jaxrs" )
public class JaxRsControllerImpl extends JaxRsController {

	public JaxRsControllerImpl() {
		super();
		setClientProperty(new MyClientPropertyJaxRs());
	}

	@POST
	@Path("auth")
	@Consumes("application/json")
	public String getAuth() {
		String username = null;
		String password = null;

		try {
			username = getBasicAuthentification()[0];
			password = getBasicAuthentification()[1];
		} catch (HttpException e) {
			// e.printStackTrace();
		}

		System.out.println("username basic authentification :::: " + username);
		System.out.println("password basic authentification :::: " + password);

		return "";

	}

	@POST
	@Path("/validate")
//	@Consumes("application/json")
	public String validateRepresentation(String requestEntity) {
		// decomment to override the current representation
		// JaxRsController.defaultRepresentation = new JacksonRepresentation();
		Representation representation = getRepresentation(requestEntity);
		System.out.println("representation " + representation.toString());
		System.out.println("Gson representation "
				+ (JaxRsController.defaultRepresentation instanceof GsonRepresentation));
		System.out.println("Jackson representation "
				+ (JaxRsController.defaultRepresentation instanceof JacksonRepresentation));
		System.out.println("validate " + validate(requestEntity, "email", "name"));

		return representation.toString();
	}

	@POST
	@Path("/agent")
	public Response userAgentMethod(String requestEntity, @HeaderParam("user-agent") String userAgentRequest) {
		System.out.println("isChrome " + getClientProperty().isChrome(httpHeader));
		System.out.println("isFirefox " + getClientProperty().isFirefox(httpHeader));
		System.out.println("isTablet " + getClientProperty().isTablet(httpHeader));
		System.out.println("isMobile " + getClientProperty().isMobile(httpHeader));
		System.out.println("isWebKit " + getClientProperty().isWebKit(httpHeader));

		return Response.status(200).entity(userAgentRequest).build();
	}

	@GET
	@Path("header")
	public Response getHeader(@HeaderParam("user-agent") String userAgentRequest,
			@HeaderParam("connection") String connection) {
		MultivaluedMap<String, String> requestHeaders = httpHeader.getRequestHeaders();
		Set<String> requestHeaderSet = requestHeaders.keySet();
		for (String currentHeader : requestHeaderSet) {
			System.out.println(currentHeader);
			System.out.println(requestHeaders.get(currentHeader));
		}

		// List<String> connection = requestHeaders.get( "connection" );
		List<String> authorization = requestHeaders.get("authorization");
		List<String> userAgent = requestHeaders.get("user-agent");

		System.out.println("connection " + connection);
		System.out.println("authorization " + authorization);
		System.out.println("user-agent " + userAgent);
		System.out.println("user-agent request " + userAgentRequest);

		return Response.status(200).entity("connection :: " + connection + " -- user-agent :: " + userAgent).build();
	}

	@Override
	public MyClientPropertyJaxRs getClientProperty() {
		return (MyClientPropertyJaxRs) super.getClientProperty();
	}
}
