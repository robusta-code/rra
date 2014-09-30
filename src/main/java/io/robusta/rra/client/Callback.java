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

import io.robusta.rra.exception.RestException;

import java.io.InputStream;

/**
 * Callback called after a request is done.
 * 
 * @author Nicolas Zozol
 */
public interface Callback {

    /**
     * Returns the client that made the request
     * 
     * @return
     */
    RestClient<?> getClient();

    /**
     * Datas are sent, validated by the server, and safely received, with a 200
     * - 300
     *
     * @param response
     */
    public void onSuccess( String response );

    /**
     * @param inputStream
     */
    public void onSuccess( InputStream inputStream );

    /**
     * A failure occurs after datas are sent into the web. There might be no
     * return, unvalidated by the server, or there is a server error. 400-500
     * failure response. Status code is available through the client ; This
     * method may be implemented only once or a few time for the whole
     * application
     * 
     * @param failure
     *            contains the status code and a message
     */
    public void onFailure( RestException failure );

    // TODO : httpException not compatible with Gwt ?
    /**
     * @param exception
     */
    public void onException( Exception exception );

    /**
     * 
     */
    public void onComplete();

}
