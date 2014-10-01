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

import javax.servlet.http.HttpServletRequest;

/**
 * @author Nicolas Zozol
 *
 */
public class ClientPropertyServlet implements ClientProperty<HttpServletRequest> {

    /**
     * @param request
     * @return
     */
    private String getAgent( HttpServletRequest request ) {
        return request.getHeader( "user-agent" );
    }

    /**
     * @param request
     * @return
     */
    public boolean isChrome( HttpServletRequest request ) {
        return ( getAgent( request ).toUpperCase().contains( "CHROME" ) );
    }

    /**
     * @param request
     * @return
     */
    public boolean isFirefox( HttpServletRequest request ) {
        return ( getAgent( request ).toUpperCase().contains( "FIREFOX" ) );
    }

    /**
     * @param request
     * @return
     */
    public boolean isTablet( HttpServletRequest request ) {
        return ( getAgent( request ).toUpperCase().contains( "TABLET" ) || getAgent( request ).toUpperCase().contains(
                "IPAD" ) );
    }

    /**
     * @param request
     * @return
     */
    public boolean isMobile( HttpServletRequest request ) {
        return ( getAgent( request ).toUpperCase().contains( "MOBILE" ) );
    }

}
