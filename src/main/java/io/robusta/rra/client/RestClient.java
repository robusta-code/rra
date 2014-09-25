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
import io.robusta.rra.representation.Representation;
import io.robusta.rra.utils.CoupleList;

import java.util.Map;

public interface RestClient<Client> {

    /**
     * if not modified, value is "application/xml;charset=utf-8"
     */
    public static String xmlContentType  = "application/xml; charset=utf-8";
    /**
     * if not modified, value is "text/html;charset=utf-8"
     */
    public static String htmlContentType = "text/html; charset=utf-8";
    /**
     * if not modified, value is
     * "application/x-www-form-urlencoded; charset=utf-8"
     */
    public static String formContentType = "application/x-www-form-urlencoded; charset=utf-8";

    public static String jsonContentType = "application/json; charset=utf-8";

    public void setApplicationUri( String uri );

    /**
     * Set authorization value for next or all requests, depending on the
     * implemntation
     * 
     * @param authorizationValue
     */
    public void setAuthorizationValue( String authorizationValue );

    /**
     * Set the Content-Type for all next requests. Defautl content-type is
     * "application/xml; charset=utf-8"
     * 
     * @param contentType
     */
    public void setContentType( String contentType );

    /**
     * Set the entity of the next request.
     * 
     * @param entity
     */
    public void setNextEntity( String entity );

    /**
     * Returns last request's Http Status Code. If no request have been made, or
     * if an exception is thrown (ex : the request hast not even been launched),
     * the function will send an IllegalStateException
     * 
     * @return last Status Code
     * @throws IllegalStateException
     *             if no request has been made, or last request did not complete
     */
    public int getHttpCode() throws IllegalStateException;

    /**
     * Returns last response sent by the server
     * 
     * @return last response
     */
    public String getResponse();

    public Map<String, String> getHeaderResponse();

    /**
     * Returns the wrapped client
     * 
     * @return the wrapped client
     */
    public Client getUnderlyingClient();

    /**
     * <p>
     * Execute a POST request toward a specified Resource, with evenutal
     * parameters.
     * </p>
     * <p>
     * If ApplicationUri is set to
     * <strong>http://localhost:8080/mywebapp/</strong> and
     * <strong>relativeFileWithNoParam</strong> is set to
     * <strong>user/register.jsp</strong>, then the request is sent to
     * <strong>http://localhost:8080/mywebapp/user/register.jsp</strong>
     * </p>
     * <p>
     * Params are set by a CoupleList ; null is accepted for no param
     * </p>
     * <p>
     * It's also possible to set the "pure entity" of the request by using
     * setEntityToNextRequest() before using this function. See the turorial for
     * detailed exemple
     * </p>
     *
     * @param relativeFileWithNoParam
     * @param representation
     *            Classic parameters added to the request.
     * @return The content of the Response
     * @throws io.robusta.rra.exception.HttpException
     *             : When we can't send the request (malformed Uri) or if there
     *             is no server response (no connection, no server, etc.)
     * @throws io.robusta.rra.exception.RestException
     *             : When the server sends back an error described by a status
     *             code (401, 500, etc.)
     * @todo3 : rename executePOSTmethod by executePOST to make it smaller, or
     *        even get/post/put/delete for simplicity
     */
    // TODO representation
    // public String POST(String relativeFileWithNoParam, CoupleList<String,
    // Object> parameters) throws HttpException;
    public String POST( String relativeFileWithNoParam, Representation representation ) throws HttpException;

    /**
     * <p>
     * Execute a POST request toward a specified Resource, with evenutal
     * parameters.
     * </p>
     * <p>
     * If ApplicationUri is set to
     * <strong>http://localhost:8080/mywebapp/</strong> and
     * <strong>relativeFileWithNoParam</strong> is set to
     * <strong>user/register.jsp</strong>, then the request is sent to
     * <strong>http://localhost:8080/mywebapp/user/register.jsp</strong>
     * </p>
     * <p>
     * Params are set by a CoupleList ; null is accepted for no param. Here is
     * an easy way to set params :
     * </p>
     * <p>
     * URL params <strong>?firstname=James&city=London</strong> is set by
     * CoupleList.builder("firstname", "James","city","London")
     * </p>
     * <p>
     * It's also possible to set the "pure entity", but it"s not recommanded for
     * a GET request.
     * </p>
     *
     * @param relativeFileWithNoParam
     * @param parameters
     *            Classic parameters added to the request.
     * @return The content of the Response
     * @throws io.robusta.rra.exception.HttpException
     *             : When we can't send the request (malformed Uri) or if there
     *             is no server response (no connection, no server, etc.)
     * @throws io.robusta.rra.exception.RestException
     *             : When the server sends back an error described by a status
     *             code (401, 500, etc.)
     */
    public String GET( String relativeFileWithNoParam, CoupleList<String, Object> parameters ) throws HttpException;

    /**
     * Execute a PUT request. The _method param is <strong>NOT</strong> used
     * neither needed.
     *
     * @param relativeFileWithNoParam
     * @param representation
     * @return
     * @throws io.robusta.rra.exception.HttpException
     *             : When we can't send the request (malformed Uri) or if there
     *             is no server response (no connection, no server, etc.)
     * @throws io.robusta.rra.exception.RestException
     *             : When the server sends back an error described by a status
     *             code (401, 500, etc.)
     */
    // TODO representation
    // public String PUT(String relativeFileWithNoParam, CoupleList<String,
    // Object> parameters) throws HttpException;
    public String PUT( String relativeFileWithNoParam, Representation representation ) throws HttpException;

    /**
     * Execute a DELETE request. The _method param is <strong>NOT</strong> used
     * neither needed.
     *
     * @param relativeFileWithNoParam
     * @param parameters
     * @return
     * @throws io.robusta.rra.exception.HttpException
     *             : When we can't send the request (malformed Uri) or if there
     *             is no server response (no connection, no server, etc.)
     * @throws io.robusta.rra.exception.RestException
     *             : When the server sends back an error described by a status
     *             code (401, 500, etc.)
     */
    public String DELETE( String relativeFileWithNoParam, CoupleList<String, Object> parameters ) throws HttpException;

    /**
     * Execute a DELETE request. The _method param is <strong>NOT</strong> used
     * neither needed.
     *
     * @param relativeFileWithNoParam
     * @param parameters
     * @return
     * @throws io.robusta.rra.exception.HttpException
     *             : When we can't send the request (malformed Uri) or if there
     *             is no server response (no connection, no server, etc.)
     * @throws io.robusta.rra.exception.RestException
     *             : When the server sends back an error described by a status
     *             code (401, 500, etc.)
     */
    public String OTHER( String method, String relativeFileWithNoParam, CoupleList<String, Object> parameters )
            throws HttpException;

    /**
     * <p>
     * Execute a GET Http request. The relative apth compares to the
     * AbsolutePath
     * </p>
     * <p>
     * For exemple, if ApplicationUri has been set to
     * http://exemple.com/mycontext/, a relative url of students/johndoe will
     * set a GET request to the http://exemple.com/mycontext/johndoe resource.
     * </p>
     * <p>
     * The request handles parameters that will be added to the url request. You
     * might use CoupleList.build(param1, value1, param2, value2) to set these
     * parameters.
     * </p>
     * <p>
     * AsyncCallback<String> refers to GWT
     * </p>
     * 
     * @param relativePath
     *            compare to ApplicationUri path
     * @param parameters
     * @param callback
     * @throws HttpException
     * @throws io.robusta.rra.exception.RestException
     *
     */
    public void get( String relativePath, CoupleList<String, Object> parameters, Callback callback )
            throws HttpException;

    public void post( String relativePath, Representation representation, Callback callback ) throws HttpException;

    public void put( String relativePath, Representation representation, Callback callback ) throws HttpException;

    public void delete( String relativePath, CoupleList<String, Object> parameters, Callback callback )
            throws HttpException;

    /**
     * Execute any othe method, including WEBDAV methods, or the PATCH method
     * 
     * @param method
     * @param relativePath
     * @param parameters
     * @param callback
     * @throws HttpException
     */
    public void other( String method, String relativePath, CoupleList<String, Object> parameters, Callback callback )
            throws HttpException;

    public void join();

}
