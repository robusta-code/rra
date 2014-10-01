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
public class JaxRsController {

    public static Representation defaultRepresentation = Rra.defaultRepresentation;

    /**
     * 
     */
    protected ClientProperty     clientProperty;

    /**
     * 
     */
    @Context
    HttpHeaders                  httpHeader;
    /**
     * 
     */
    @Context
    UriInfo                      uriInfo;
    /**
     * 
     */
    @Context
    Response                     response;
    /**
     * 
     */
    @Context
    Request                      request;

    public JaxRsController() {
        super();
        clientProperty = new ClientPropertyJaxRs();
    }

    /**
     * @return
     */
    public HttpHeaders getHttpHeader() {
        return httpHeader;
    }

    /**
     * @return
     */
    public UriInfo getUriInfo() {
        return uriInfo;
    }

    /**
     * @return
     */
    public MultivaluedMap<String, String> getHeaders() {
        return getHttpHeader().getRequestHeaders();
    }

    /**
     * @return
     */
    public boolean isJsonApplication() {
        List<String> type = getHeaders().get( "content-type" );
        return ( type.get( 0 ) != null && type.get( 0 ).equals( "application/json" ) );
    }

    /**
     * @param uriInfo
     * @return
     */
    public boolean isSecure( UriInfo uriInfo ) {
        return uriInfo.getAbsolutePath().toString().contains( "https" );
    }

    /**
     * @return
     */
    public String[] getBasicAuthentification() {
        String[] values = new String[2];
        List<String> authorization = getHeaders().get( "authorization" );
        if ( authorization.get( 0 ) != null && authorization.get( 0 ).startsWith( "Basic" ) ) {
            String base64Credentials = authorization.get( 0 ).substring( "Basic".length() ).trim();
            CodecImpl codecimpl = new CodecImpl();
            try {
                values[0] = codecimpl.getUsername( base64Credentials );
                values[1] = codecimpl.getPassword( base64Credentials );
            } catch ( CodecException e ) {
                e.printStackTrace();
            }
        }
        return values;
    }

    /**
     * @return
     */
    public Response getBasicAuthentificationResponse() {
        if ( !isSecure( getUriInfo() ) ) {
            return response
                    .status( 401 )
                    .entity(
                            "<a href='http://docs.oracle.com/javaee/5/tutorial/doc/bnbxw.html'>Establishing a Secure Connection Using SSL</a>" )
                    .build();
        } else
            return response.status( 200 ).entity( "The authentification is secure !" ).build();
    }

    /**
     * @param representation
     * @throws ControllerException
     */
    protected void throwIfNull( Representation representation ) throws ControllerException {
        if ( representation == null ) {
            throw new ControllerException( "Representation is null" );
        }
    }

    /**
     * @param requestEntity
     * @return
     */
    public Representation getRepresentation( String requestEntity ) {
        return defaultRepresentation.createNewRepresentation( getRequestEntity( requestEntity ) );
    }

    /**
     * @param requestEntity
     * @param keys
     * @return
     */
    public boolean validate( String requestEntity, String... keys ) {
        Representation representation = getRepresentation( requestEntity );
        throwIfNull( representation );
        return representation.has( keys );
    }

    /**
     * @param requestEntity
     * @param keys
     * @return
     */
    public Response validateResponse( String requestEntity, String... keys ) {
        if ( !validate( requestEntity, keys ) ) {
            return response.status( 406 ).entity( "Json representation is not valid !" ).build();
        } else
            return response.status( 200 ).entity( requestEntity ).build();
    }

    /**
     * @param requestEntity
     * @return
     */
    public String getRequestEntity( String requestEntity ) {
        return requestEntity;
    }

    /**
     * @return
     */
    public List<String> getUserAgent() {
        return getHeaders().get( "user-agent" );
    }

    public ClientProperty getClientProperty() {
        return clientProperty;
    }

    public void setClientProperty( ClientProperty clientProperty ) {
        this.clientProperty = clientProperty;
    }
}
