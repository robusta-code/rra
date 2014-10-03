package io.robusta.rra.controller;

import io.robusta.rra.exception.HttpException;
import io.robusta.rra.representation.Representation;
import io.robusta.rra.representation.Rra;
import io.robusta.rra.security.implementation.CodecException;
import io.robusta.rra.security.implementation.CodecImpl;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
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

    public static Representation  defaultRepresentation = Rra.defaultRepresentation;

    /**
     * 
     */
    protected ClientProperty      clientProperty;

    /**
     * 
     */
    @Context
    protected HttpHeaders         httpHeader;
    /**
     * 
     */
    @Context
    protected UriInfo             uriInfo;
    /**
     * 
     */
    @Context
    protected HttpServletResponse httpServletResponse;

    /**
     * 
     */
    @Context
    protected Request             request;

    public JaxRsController() {
        super();
        clientProperty = new ClientPropertyJaxRs();
    }

    /**
     * retrieve the header fields of the http request
     * 
     * @return
     */
    public MultivaluedMap<String, String> getHeaders() {
        return httpHeader.getRequestHeaders();
    }

    /**
     * check if the content-type of the header is a "application/json"
     * 
     * @return
     */
    public boolean isJsonApplication() {
        List<String> type = getHeaders().get( "content-type" );
        return ( type.get( 0 ) != null && type.get( 0 ).equals( "application/json" ) );
    }

    /**
     * check if the uri is secure
     * 
     * @param uriInfo
     * @return
     */
    public boolean isSecure( UriInfo uriInfo ) {
        return uriInfo.getAbsolutePath().toString().contains( "https" );
    }

    /**
     * return the username and the password of the header's authorization
     * 
     * @return
     * @throws IOException
     */
    public String[] getBasicAuthentification() throws HttpException {
        String[] values = new String[2];
        if ( !isSecure( uriInfo ) ) {
            try {
                httpServletResponse
                        .sendError( 426,
                                "<a href='http://docs.oracle.com/javaee/5/tutorial/doc/bnbxw.html'>Establishing a Secure Connection Using SSL</a>" );
                throw new HttpException( "connection is not secure !" );
            } catch ( IOException e ) {
            }
        } else
        {

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
        }
        return values;
    }

    /**
     * throw an exception if the representation is null
     * 
     * @param representation
     * @throws ControllerException
     */
    protected void throwIfNull( Representation representation ) throws ControllerException {
        if ( representation == null ) {
            throw new ControllerException( "Representation is null" );
        }
    }

    /**
     * create a representation from the content of request's entity
     * 
     * @param requestEntity
     * @return
     */
    public Representation getRepresentation( String requestEntity ) {
        return defaultRepresentation.createNewRepresentation( requestEntity );
    }

    /**
     * check if the content of request's entity contains specific keys
     * 
     * @param requestEntity
     * @param keys
     * @return
     */
    public boolean validate( String requestEntity, String... keys ) {
        Representation representation = getRepresentation( requestEntity );
        throwIfNull( representation );
        boolean valid = representation.has( keys );
        if ( !valid ) {
            try {
                httpServletResponse
                        .sendError( 406,
                                "Json representation is not valid !" );
            } catch ( IOException e ) {
            }
        }
        return valid;
    }

    /**
     * update the response with a status and an entity
     * 
     * @param status
     * @param entity
     * @return
     */
    public Response ok( int status, Object entity ) {
        return Response.status( status ).entity( entity ).build();
    }

    /**
     * retrieve the client property
     * 
     * @return
     */
    public ClientProperty getClientProperty() {
        return clientProperty;
    }

    /**
     * update the client property
     * 
     * @param clientProperty
     */
    public void setClientProperty( ClientProperty clientProperty ) {
        this.clientProperty = clientProperty;
    }

}
