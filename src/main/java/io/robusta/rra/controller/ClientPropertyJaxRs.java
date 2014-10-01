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

package io.robusta.rra.controller;

import io.robusta.rra.exception.HttpException;

import javax.ws.rs.core.HttpHeaders;

/**
 * @author Nicolas Zozol
 *
 */
public class ClientPropertyJaxRs implements ClientProperty<HttpHeaders> {

    /**
     * @param request
     * @return
     */
    protected String getAgent( HttpHeaders httpHeaders ) {
        if ( httpHeaders.getRequestHeader( "user-agent" ) != null ) {
            return httpHeaders.getRequestHeader( "user-agent" ).get( 0 );

        } else {
            throw new HttpException( "user-agent is null !" );
        }
    }

    /**
     * @param request
     * @return
     */
    public boolean isChrome( HttpHeaders httpHeaders ) {
        return ( getAgent( httpHeaders ).toUpperCase().contains( "CHROME" ) );
    }

    /**
     * @param request
     * @return
     */
    public boolean isFirefox( HttpHeaders httpHeaders ) {
        return ( getAgent( httpHeaders ).toUpperCase().contains( "FIREFOX" ) );
    }

    /**
     * @param request
     * @return
     */
    public boolean isTablet( HttpHeaders httpHeaders ) {
        return ( getAgent( httpHeaders ).toUpperCase().contains( "TABLET" ) || getAgent( httpHeaders ).toUpperCase()
                .contains(
                        "IPAD" ) );
    }

    /**
     * @param request
     * @return
     */
    public boolean isMobile( HttpHeaders httpHeaders ) {
        return ( getAgent( httpHeaders ).toUpperCase().contains( "MOBILE" ) );
    }

}
