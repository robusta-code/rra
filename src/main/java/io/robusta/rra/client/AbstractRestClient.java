/*
 * Copyright (c) 2014. by Robusta Code and individual contributors
 *  as indicated by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.robusta.rra.client;

import io.robusta.rra.exception.HttpException;
import io.robusta.rra.exception.RestException;
import io.robusta.rra.representation.Representation;
import io.robusta.rra.utils.Couple;
import io.robusta.rra.utils.CoupleList;
import io.robusta.rra.utils.StringUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used both for Synchronous and Asynchronous client
 * <p/>
 * Created by Nicolas Zozol for Robusta Code
 *
 * @author Nicolas Zozol
 */
public abstract class AbstractRestClient<Client> implements RestClient<Client> {

    /**
     * can be easily configured outside
     */
    protected static String       applicationUri;
    protected static String       defaultContentType = RestClient.xmlContentType;
    /**
     *
     */
    protected static String       authorizationValue = "";
    protected Map<String, String> responseHeaders    = new HashMap<String, String>();
    /**
     *
     */
    protected String              entity             = "";
    /**
     *
     */
    protected String              credentials        = "";
    /**
     *
     */
    protected int                 httpCode;
    /**
     *
     */
    protected String              response;
    /**
     *
     */
    protected String              contentType        = RestClient.jsonContentType;

    public static void setDefaultContentType( String contentType ) {
        defaultContentType = contentType;
        if ( contentType.equals( "xml" ) ) {
            defaultContentType = RestClient.xmlContentType;
        }
        if ( contentType.equals( "html" ) ) {
            defaultContentType = RestClient.htmlContentType;
        }
        if ( contentType.equals( "form" ) ) {
            defaultContentType = RestClient.formContentType;
        }
        if ( contentType.equals( "json" ) ) {
            defaultContentType = RestClient.jsonContentType;
        }
    }

    /**
     * For non-gwt application, it will check that uri starts with http or https
     *
     * @param applicationUri
     * @throws IllegalArgumentException
     *             if uri does not start with http:// or https://
     */
    protected void checkConstructorUri( String applicationUri ) throws IllegalArgumentException {
        if ( !applicationUri.startsWith( "http://" ) && !applicationUri.startsWith( "https://" ) ) {
            throw new IllegalArgumentException( "applicationUri must start with http:// or https://" );
        }
    }

    /**
     * Returns the full uri and the request Body in a String[2] array If content
     * is Form, and Method is POST or PUT, parameters will go in the entity For
     * all other cases, it will go to the url, such as ?param1=Jo&param2=Bob
     *
     * @param method
     *            Http method (or Other)
     * @param relativePath
     * @param parameters
     * @return result[0] is the full URI, result[1] is the entity
     * @throws io.robusta.rra.exception.HttpException
     */
    public String[] prepareMethod( HttpMethod method, String relativePath, CoupleList<String, Object> parameters,
            Representation representation ) throws HttpException {

        this.responseHeaders = new HashMap<String, String>();
        String body = entity;
        // Making a clear uri
        String url = StringUtils.addPath( applicationUri, relativePath );

        // Dealing with parameters
        // Special case of forms && POST/PUT methods
        if ( representation != null ) {
            // params will go in the entity
            // adding parameters to the entity
            assert entity == null || entity.length() == 0;
            // body = encodeFormEntity(parameters);
            body = representation.toString();
        } else if ( ( parameters != null && parameters.size() > 0 ) ) {
            url = encodeUrl( applicationUri, relativePath, parameters );
        }

        return new String[] { url, body };
    }

    /**
     * Set autorizationValue as a static method
     *
     * @param authorizationValue
     */
    public void setAuthorizationValue( String authorizationValue ) {
        AbstractRestClient.authorizationValue = authorizationValue;
    }

    protected boolean contentTypeIsForm() {
        return this.contentType.contains( "x-www-form-urlencoded" );
    }

    /**
     * Once the contentType is set, it will be used as long as the object lives.
     * default is "application/x-www-form-urlencoded;charset=utf-8". If you
     * contentType="" or "form", it will be set to default. If you just put
     * contentType="xml", it will be "application/xml;charset=utf-8"
     *
     * @param contentType
     */
    public void setContentType( String contentType ) {
        this.contentType = contentType;
        if ( contentType.equals( "xml" ) ) {
            this.contentType = RestClient.xmlContentType;
        }
        if ( contentType.equals( "html" ) ) {
            this.contentType = RestClient.htmlContentType;
        }
        if ( contentType.equals( "form" ) ) {
            this.contentType = RestClient.formContentType;
        }
        if ( contentType.equals( "json" ) ) {
            this.contentType = RestClient.jsonContentType;
        }
    }

    /**
     * May be done only once, as the application path is static
     *
     * @param applicationUri
     */
    public void setApplicationUri( String applicationUri ) {
        AbstractRestClient.applicationUri = applicationUri;
    }

    public void setNextEntity( String postBody ) {
        this.entity = postBody;
    }

    public int getHttpCode() {
        return httpCode;
    }

    // TODO
    public String getResponse() {
        return response;
    }

    public void clean() {
        setNextEntity( "" );
        this.contentType = defaultContentType;
    }

    /**
     * Encode an URL parameter with UTF-8, as asked by wwc Exemple :
     * encodeParameter("A B C $%" returns "A+B+C %24%25"
     *
     * @param nameOrValue
     * @return
     */
    public abstract String encodeParameter( String nameOrValue );

    protected String encodeUrl( String applicationUri, String relativePath, CoupleList<String, Object> parameters ) {
        String url = StringUtils.addPath( applicationUri, relativePath );
        StringBuilder result = new StringBuilder( url );
        // throw new
        // HttpException("Can't do a POST method with both parameters and postbody");
        if ( !parameters.isEmpty() ) {
            result.append( "?" );
        }
        for ( int i = 0; i < parameters.size(); i++ ) {

            Couple<String, Object> c = parameters.get( i );
            String paramName = encodeParameter( c.getLeft() );
            String paramValue = encodeParameter( c.getRight().toString() );

            result.append( paramName );
            result.append( "=" );
            result.append( paramValue );
            // StringUtilities.replaceAll(value, "+","%20");
            if ( i != parameters.size() - 1 ) {
                result.append( "&" );
            }
        }
        return result.toString();
    }

    protected String encodeFormEntity( CoupleList<String, Object> parameters ) {
        StringBuilder result = new StringBuilder();

        boolean firstLine = true;
        for ( int i = 0; i < parameters.size(); i++ ) {

            if ( firstLine ) {
                firstLine = false;
            } else {
                result.append( "\n" );
            }
            Couple<String, Object> c = parameters.get( i );
            String paramName = encodeParameter( c.getLeft() );
            String paramValue = encodeParameter( c.getRight().toString() );

            result.append( paramName );
            result.append( "=" );
            result.append( paramValue );
        }
        return result.toString();
    }

    @Override
    public Map<String, String> getHeaderResponse() {
        return this.responseHeaders;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String POST( String relativeFileWithNoParam, Representation representation ) throws HttpException {
        String[] obj = prepareMethod( HttpMethod.POST, relativeFileWithNoParam, null, representation );
        assert obj.length == 2;
        return executeMethod( HttpMethod.POST, obj[0], obj[1] );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String GET( String relativeFileWithNoParam, CoupleList<String, Object> parameters ) throws HttpException {
        String[] obj = prepareMethod( HttpMethod.GET, relativeFileWithNoParam, parameters, null );
        assert obj.length == 2;
        return executeMethod( HttpMethod.GET, obj[0], obj[1] );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String PUT( String relativeFileWithNoParam, Representation representation ) throws HttpException {
        String[] obj = prepareMethod( HttpMethod.PUT, relativeFileWithNoParam, null, representation );
        assert obj.length == 2;
        return executeMethod( HttpMethod.PUT, obj[0], obj[1] );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String DELETE( String relativeFileWithNoParam, CoupleList<String, Object> parameters ) throws HttpException {
        String[] obj = prepareMethod( HttpMethod.DELETE, relativeFileWithNoParam, parameters, null );
        assert obj.length == 2;
        return executeMethod( HttpMethod.DELETE, obj[0], obj[1] );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String OTHER( String method, String relativeFileWithNoParam, CoupleList<String, Object> parameters )
            throws HttpException {
        String[] obj = prepareMethod( HttpMethod.OTHER.setMethod( method ), relativeFileWithNoParam, parameters, null );
        assert obj.length == 2;
        return executeMethod( HttpMethod.OTHER.setMethod( method ), obj[0], obj[1] );
    }

    /**
     * This method has the responsability do make the correct call with a
     * specific implementation
     *
     * @param method
     * @param url
     * @param entity
     * @return
     * @throws HttpException
     */
    protected abstract String executeMethod( final HttpMethod method, final String url, final String entity )
            throws HttpException;

    @Override
    public void get( String relativePath, CoupleList<String, Object> parameters, Callback callback )
            throws HttpException {
        String[] obj = prepareMethod( HttpMethod.GET, relativePath, parameters, null );
        assert obj.length == 2;
        String url = obj[0], body = obj[1];
        executeMethod( HttpMethod.GET, url, body, callback );
    }

    @Override
    public void post( String relativePath, Representation representation, Callback callback ) throws HttpException {
        String[] obj = prepareMethod( HttpMethod.POST, relativePath, null, representation );
        assert obj.length == 2;
        String url = obj[0], body = obj[1];

        setNextEntity( body );
        executeMethod( HttpMethod.POST, url, body, callback );
    }

    @Override
    public void put( String relativePath, Representation representation, Callback callback ) throws HttpException {
        String[] obj = prepareMethod( HttpMethod.PUT, relativePath, null, representation );
        assert obj.length == 2;
        String url = obj[0], body = obj[1];

        setNextEntity( body );
        executeMethod( HttpMethod.PUT, url, body, callback );
    }

    @Override
    public void delete( String relativePath, CoupleList<String, Object> parameters, Callback callback )
            throws HttpException {
        String[] obj = prepareMethod( HttpMethod.DELETE, relativePath, parameters, null );
        assert obj.length == 2;
        String url = obj[0], body = obj[1];

        setNextEntity( body );
        executeMethod( HttpMethod.DELETE, url, body, callback );
    }

    @Override
    public void other( String method, String relativePath,
            CoupleList<String, Object> parameters, Callback callback )
            throws HttpException {
        String[] obj = prepareMethod( HttpMethod.OTHER.setMethod( method ), relativePath, parameters, null );
        assert obj.length == 2;
        String url = obj[0], body = obj[1];

        setNextEntity( body );
        executeMethod( HttpMethod.OTHER.setMethod( method ), url, body, callback );

    }

    // TODO : add the method getResponse
    @Override
    public abstract void join();

    protected abstract void executeMethod( final HttpMethod method, final String url, final String entity,
            final Callback callback ) throws HttpException;

    /**
     * calls the callback succes or failure, using the httpcode
     *
     * @param httpCode
     * @param inputStream
     * @param callback
     */
    // protected void callCallback(Callback callback, int httpCode, String
    // response) {
    protected void callCallback( Callback callback, int httpCode, InputStream inputStream ) {

        if ( httpCode >= 200 && httpCode < 300 ) {
            // callback.onSuccess(response);

            callback.onSuccess( inputStream );
        } else if ( httpCode >= 300 && httpCode < 400 ) {
            // no success
        } else if ( httpCode >= 400 && httpCode < 600 ) {
        } else if ( httpCode < 600 ) {
            // callback.onFailure(new RestException(httpCode, response));
            callback.onFailure( new RestException( httpCode, inputStream ) );
        }
    }
}
