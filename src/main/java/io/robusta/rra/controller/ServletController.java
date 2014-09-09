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


import io.robusta.rra.Controller;
import io.robusta.rra.Representation;
import io.robusta.rra.representation.implementation.GsonRepresentation;
import io.robusta.rra.security.implementation.CodecException;
import io.robusta.rra.security.implementation.CodecImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by dev on 08/09/14.
 */
public class ServletController extends HttpServlet implements Controller {

    HttpServletRequest request;
    HttpServletResponse response;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.request=req;
        this.response=resp;

        super.service(req, resp);
    }

    @Override
    public void validate(Representation r, String... keys) {

    }

    @Override
    public String[] getBasicAuthentication() {
        String authorization = request.getHeader("Authorization");
        String[] values=new String[2];
        if (authorization != null && authorization.startsWith("Basic")) {
            String base64Credentials = authorization.substring("Basic".length()).trim();
            CodecImpl codecimpl=new CodecImpl();
            try {
                values[0]=codecimpl.getUsername(base64Credentials);
                values[1]=codecimpl.getPassword(base64Credentials);
            } catch (CodecException e) {
                e.printStackTrace();
            }
        }
        return  values;
    }
}
