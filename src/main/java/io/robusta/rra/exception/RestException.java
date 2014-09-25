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

package io.robusta.rra.exception;

import java.io.InputStream;

/**
 * Exception when a request sends an Http code in a <strong>RESTful</strong>
 * context ; this code will be between 400 & 600.
 * 
 * @author Nicolas Zozol
 * @todo1 : create a RepresentationManager for Exception ?
 */
public class RestException extends Exception {

    int         httpCode;
    String      rawResponse;
    InputStream inputStream;

    /**
     * Create a REST exception with a 400-599 Http Code, a message explaining
     * the failure, and the raw server response This is usually made by the
     * RestCleint when it receives a 400-599 Http Code.
     * 
     * @param httpCode
     *            Http Code sent by the server
     * @param rawResponse
     *            the response sent by the server
     */
    public RestException( int httpCode, String rawResponse ) {

        this.httpCode = httpCode;
        this.rawResponse = rawResponse;
    }

    /**
     * Create a REST exception with a 400-599 Http Code, a message explaining
     * the failure, and the raw server response This is usually made by the
     * RestCleint when it receives a 400-599 Http Code.
     * 
     * @param httpCode
     *            Http Code sent by the server
     * @param inputStream
     *            the response sent by the server
     */
    public RestException( int httpCode, InputStream inputStream ) {

        this.httpCode = httpCode;
        this.inputStream = inputStream;
    }

    /**
     * Returns the Http Code
     * 
     * @return the Http Code
     */
    public int getHttpCode() {
        return httpCode;
    }

    /**
     * Returns the response sent by the server
     * 
     * @return the server's response
     */
    public String getRawResponse() {
        return rawResponse;
    }
}
