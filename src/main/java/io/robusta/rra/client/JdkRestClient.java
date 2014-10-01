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
import io.robusta.rra.utils.CoupleList;
import io.robusta.rra.utils.FileUtils;
import io.robusta.rra.utils.ListUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple REST Http client wrapping the JDK Client. Compare to Apache, many JVM
 * have this client. Created by Nicolas Zozol for Robusta Code
 * 
 * @author Nicolas Zozol
 */
public class JdkRestClient extends AbstractRestClient<HttpURLConnection> {

    static Proxy      proxy;
    HttpURLConnection http;
    Thread            requestThread;

    /**
     * Constructor
     * 
     * @param applicationPath
     *            default path of the request
     */
    public JdkRestClient( String applicationUri ) {
        checkConstructorUri( applicationUri );
        JdkRestClient.applicationUri = applicationUri;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.robusta.rra.client.AbstractRestClient#executeMethod(io.robusta.rra
     * .client.HttpMethod, java.lang.String, java.lang.String)
     */
    @Override
    protected String executeMethod( final HttpMethod method, final String url, final String entity )
            throws HttpException {

        assert url.startsWith( "http" );

        URL u;
        try {
            u = new URL( url );
        } catch ( MalformedURLException ex ) {
            throw new HttpException( "malformedURI", ex );
        }

        try {

            if ( JdkRestClient.proxy != null ) {
                http = (HttpURLConnection) u.openConnection( JdkRestClient.proxy );
            } else {
                http = (HttpURLConnection) u.openConnection();
            }

            http.addRequestProperty( "Content-type", this.contentType );
            if ( authorizationValue != null ) {
                http.addRequestProperty( "Authorization", JdkRestClient.authorizationValue );
            }

            http.setRequestMethod( method.toString() );
            http.setDoInput( true );
            switch ( method ) {
            case PUT:
            case POST:
                http.setDoOutput( true );
                /* if there is something to put in the entity */
                if ( this.entity != null && this.entity.length() >= 0 ) {
                    DataOutputStream wr = new DataOutputStream( http.getOutputStream() );
                    wr.writeBytes( entity );
                    wr.flush();
                    wr.close();
                }
                break;
            }
            this.response = FileUtils.readInputStream( http.getInputStream() );
            this.responseHeaders = readHeaders( http );
            for ( String key : this.responseHeaders.keySet() ) {
                System.out.println( "key " + key + " -> " + this.responseHeaders.get( key ) );
            }
            this.httpCode = http.getResponseCode();

            return this.response;
        } catch ( IOException ex ) {
            ex.printStackTrace();
            throw new HttpException( ex.getMessage(), ex );
        } finally {
            clean();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.robusta.rra.client.AbstractRestClient#executeMethod(io.robusta.rra
     * .client.HttpMethod, java.lang.String, java.lang.String,
     * io.robusta.rra.client.Callback)
     */
    @Override
    protected void executeMethod( final HttpMethod method, final String url, final String entity,
            final Callback callback ) throws HttpException {

        requestThread = new Thread() {

            @Override
            public void run() {

                assert url.startsWith( "http" );

                URL u;
                try {
                    u = new URL( url );
                } catch ( MalformedURLException ex ) {
                    throw new HttpException( "malformedURI", ex );
                }

                try {
                    if ( JdkRestClient.proxy != null ) {
                        http = (HttpURLConnection) u.openConnection( JdkRestClient.proxy );
                    } else {
                        http = (HttpURLConnection) u.openConnection();
                    }
                    http.addRequestProperty( "Content-type", contentType );
                    if ( authorizationValue != null ) {
                        http.addRequestProperty( "Authorization", JdkRestClient.authorizationValue );
                    }
                    http.setRequestMethod( method.toString() );
                    http.setDoInput( true );
                    switch ( method ) {
                    case PUT:
                    case POST:
                        http.setDoOutput( true );
                        /* if there is something to put in the entity */
                        if ( entity != null && entity.length() >= 0 ) {
                            DataOutputStream wr = new DataOutputStream( http.getOutputStream() );
                            wr.writeBytes( entity );
                            wr.flush();
                            wr.close();
                        }
                        break;
                    }
                    httpCode = http.getResponseCode();
                    callCallback( callback, httpCode, http.getInputStream() );

                } catch ( IOException ex ) {
                    ex.printStackTrace();
                    throw new HttpException( ex.getMessage(), ex );
                } finally {
                    clean();
                }
            }
        };
        requestThread.start();

    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.client.AbstractRestClient#join()
     */
    @Override
    public void join() {
        try {
            requestThread.join();
        } catch ( InterruptedException ex ) {
            throw new HttpException( "Can't join the client's thread : " + ex.getMessage(), ex );
        }
    }

    /**
     * @param http
     * @return
     */
    private Map<String, String> readHeaders( HttpURLConnection http ) {
        Map<String, List<String>> maps = http.getHeaderFields();
        Map<String, String> map = new HashMap<String, String>();
        for ( String key : maps.keySet() ) {
            map.put( key, ListUtils.join( ";", maps.get( key ) ) );
        }
        return map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.robusta.rra.client.AbstractRestClient#encodeParameter(java.lang.String
     * )
     */
    @Override
    public String encodeParameter( String nameOrValue ) {
        try {
            return URLEncoder.encode( nameOrValue, "UTF-8" );
        } catch ( UnsupportedEncodingException ex ) {
            throw new IllegalStateException( "Can't encode " + nameOrValue );
        }
    }

    /**
     * Set a proxy for the class
     * 
     * @param proxy
     */
    public static void setProxy( Proxy proxy ) {
        JdkRestClient.proxy = proxy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.client.RestClient#getUnderlyingClient()
     */
    @Override
    public HttpURLConnection getUnderlyingClient() {
        return http;
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.client.AbstractRestClient#OTHER(java.lang.String,
     * java.lang.String, io.robusta.rra.utils.CoupleList)
     */
    @Override
    public String OTHER( String method, String relativeFileWithNoParam, CoupleList<String, Object> parameters )
            throws HttpException {
        return null;
    }

    /**
     * This will change the defaultContentType of SunRestClient
     * 
     * @param contentType
     */
    public static void setDefaultContentType( String contentType ) {
        JdkRestClient.setDefaultContentType( defaultContentType );
    }
}
