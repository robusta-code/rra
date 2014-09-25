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
import io.robusta.rra.representation.Representation;

import java.io.InputStream;

public class SimpleCallback implements Callback {

    public static boolean debug = true;

    RestClient            client;

    public SimpleCallback( RestClient client ) {
        this.client = client;
    }

    /**
     * This implementation doesn't do anything. Overwrite it to extend
     * functionnalities.
     * 
     * @param response
     */
    @Override
    public void onSuccess( String response ) {
        if ( debug ) {
            System.out.println( "Debug SimpleCallback : Successful request" );
        }
    }

    @Override
    public void onSuccess( InputStream inputStream ) {
        if ( debug ) {
            System.out.println( "Debug SimpleCallback : Successful request" );
        }
    }

    public void onSuccess( Representation response ) {
        if ( debug ) {
            System.out.println( "Debug SimpleCallback : Successful request" );
        }
    }

    public <T> void onSuccess( Class<T> clazz, T response ) {

        if ( debug ) {
            System.out.println( "Debug SimpleCallback : Successful request" );
        }
    }

    /**
     * This implementation doesn't do anything. Overwrite it to extend
     * functionnalities.
     * 
     * @param failure
     */
    @Override
    public void onFailure( RestException failure ) {
        if ( debug ) {
            System.out.println( "Debug SimpleCallback : Request failed :" + failure );
        }
    }

    /**
     * This implementation doesn't do anything. Overwrite it to extend
     * functionnalities.
     */
    @Override
    public void onComplete() {
        if ( debug ) {
            System.out.println( "Debug SimpleCallback : Request complete" );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public RestClient getClient() {
        return this.client;
    }

    /**
     * This implementation throws a new RuntimeException. Overwrite it to extend
     * functionnalities.
     */
    @Override
    public void onException( Exception exception ) {
        throw new RuntimeException( exception );
    }

}
