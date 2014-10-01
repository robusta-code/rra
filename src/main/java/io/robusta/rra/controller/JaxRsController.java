package io.robusta.rra.controller;

import io.robusta.rra.representation.Representation;
import io.robusta.rra.representation.Rra;
import io.robusta.rra.security.implementation.CodecException;
import io.robusta.rra.security.implementation.CodecImpl;

import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author Nicolas Zozol
 */
/**
 * @author dev
 *
 */
public class JaxRsController {

	public static Representation defaultRepresentation = Rra.defaultRepresentation;

	/**
     * 
     */
	@Context
	HttpHeaders httpHeader;
	/**
     * 
     */
	@Context
	UriInfo uriInfo;
	/**
     * 
     */
	@Context
	Response response;
	/**
     * 
     */
	@Context
	Request request;

	/**
	 * retrieve the header of the request
	 * @return
	 */
	public HttpHeaders getHttpHeader() {
		return httpHeader;
	}

	/**
	 * retrieve the uri info of the request
	 * @return
	 */
	public UriInfo getUriInfo() {
		return uriInfo;
	}

	/**
	 * retrieve the header fields of the http request
	 * @return
	 */
	public MultivaluedMap<String, String> getHeaders() {
		return getHttpHeader().getRequestHeaders();
	}

	/**
	 * check if the content-type of the header is a "application/json"
	 * @return
	 */
	public boolean isJsonApplication() {
		List<String> type = getHeaders().get("content-type");
		return (type.get(0) != null && type.get(0).equals("application/json"));
	}

	/**
	 *check if the uri is secure 
	 * @param uriInfo
	 * @return
	 */
	public boolean isSecure(UriInfo uriInfo) {
		return uriInfo.getAbsolutePath().toString().contains("https");
	}

	/**
	 * return the username and the password of the header's authorization
	 * @return
	 */
	public String[] getBasicAuthentification() {
		String[] values = new String[2];
		List<String> authorization = getHeaders().get("authorization");
		if (authorization.get(0) != null && authorization.get(0).startsWith("Basic")) {
			String base64Credentials = authorization.get(0).substring("Basic".length()).trim();
			CodecImpl codecimpl = new CodecImpl();
			try {
				values[0] = codecimpl.getUsername(base64Credentials);
				values[1] = codecimpl.getPassword(base64Credentials);
			} catch (CodecException e) {
				e.printStackTrace();
			}
		}
		return values;
	}

	/**
	 * check if the uri is secure and update the response
	 * @return
	 */
	public Response getBasicAuthentificationResponse() {
		if (!isSecure(getUriInfo())) {
			return ok(426, "<a href='http://docs.oracle.com/javaee/5/tutorial/doc/bnbxw.html'>Establishing a Secure Connection Using SSL</a>");
		} else
			return ok(200, "The authentification is secure !");
	}

	/**
	 * throw an exception if the representation is null
	 * @param representation
	 * @throws ControllerException
	 */
	protected void throwIfNull(Representation representation) throws ControllerException {
		if (representation == null) {
			throw new ControllerException("Representation is null");
		}
	}

	/**
	 * create a representation from the content of request's entity 
	 * @param requestEntity
	 * @return
	 */
	public Representation getRepresentation(String requestEntity) {
		return defaultRepresentation.createNewRepresentation(getRequestEntity(requestEntity));
	}

	/**
	 * check if the content of request's entity contains specific keys
	 * @param requestEntity
	 * @param keys
	 * @return
	 */
	public boolean validate(String requestEntity, String... keys) {
		Representation representation = getRepresentation(requestEntity);
		throwIfNull(representation);
		return representation.has(keys);
	}

	/**
	 * update the response if the content of request's entity contains specific keys
	 * @param requestEntity
	 * @param keys
	 * @return
	 */
	public Response validateResponse(String requestEntity, String... keys) {
		if (!validate(requestEntity, keys)) {
			return ok(406, "Json representation is not valid !");
		} else
			return ok(200, requestEntity);
	}

	/**
	 * retrieve the entity of the http request
	 * @param requestEntity
	 * @return
	 */
	public String getRequestEntity(String requestEntity) {
		return requestEntity;
	}

	/**
	 * retrieve the user-agent of the request's header
	 * @return
	 */
	public String getUserAgent() {
		
		List<String> userAgent = getHeaders().get("user-agent");
		if (userAgent != null) {
			return userAgent.get(0);
		}else {
			throw new NullPointerException();
		}
		//return getHeaders().get("user-agent");
	}

	/**
	 * 
	 * @return
	 */
	public boolean isChrome() {
		return getUserAgent().toUpperCase().contains("CHROME");
	}

	/**
	 * @return
	 */
	public boolean isFirefox() {
		return getUserAgent().toUpperCase().contains("FIREFOX");
	}

	/**
	 * @return
	 */
	public boolean isTablet() {
		return getUserAgent().toUpperCase().contains("TABLET")
				|| getUserAgent().toUpperCase().contains("IPAD");
	}

	/**
	 * @return
	 */
	public boolean isMobile() {
		return getUserAgent().toUpperCase().contains("MOBILE");
	}
	
	
	/**
	 * update the response with a status and an entity 
	 * @param status
	 * @param entity
	 * @return
	 */
	public Response ok(int status, Object entity){
		return Response.status(status).entity(entity).build();
	}
}
