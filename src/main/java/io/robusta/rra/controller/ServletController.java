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

import io.robusta.rra.representation.Representation;
import io.robusta.rra.representation.Rra;
import io.robusta.rra.security.implementation.CodecException;
import io.robusta.rra.security.implementation.CodecImpl;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Nicolas Zozol
 *
 */
public class ServletController extends HttpServlet implements Controller {

	/**
     * 
     */
	protected ClientProperty clientProperty;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		clientProperty = new ClientPropertyServlet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String contentType = req.getContentType();

		if (contentType != null && contentType.contains("application/json")) {
			InputStream in = req.getInputStream();
			StringBuffer stringBuffer = new StringBuffer();
			int d;
			while ((d = in.read()) != -1) {
				stringBuffer.append((char) d);
			}
			req.setAttribute("representation", stringBuffer.toString());
		}

		super.service(req, resp);
	}

	/**
	 * create a representation from the content of request's entity
	 * 
	 * @param req
	 * @return
	 */
	public Representation getRepresentation(HttpServletRequest req) {
		return Rra.defaultRepresentation.createNewRepresentation(req.getAttribute("representation").toString());
	}

	/**
	 * throw an exception if the representation is null
	 * 
	 * @param representation
	 * @throws ControllerException
	 */
	protected void throwIfNull(Representation representation) throws ControllerException {
		if (representation == null) {
			throw new ControllerException("Representation is null");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.controller.Controller#validate(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.String[])
	 */
	@Override
	public boolean validate(HttpServletRequest request, HttpServletResponse response, String... keys) {
		Representation representation = getRepresentation(request);
		throwIfNull(representation);
		boolean valid = representation.has(keys);
		if (!valid) {
			try {
				response.sendError(406, "Json not valid !");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return valid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.robusta.rra.controller.Controller#getBasicAuthentication(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String[] getBasicAuthentication(HttpServletRequest req, HttpServletResponse resp) {
		String authorization = req.getHeader("Authorization");
		String[] values = new String[2];
		if (authorization != null && authorization.startsWith("Basic")) {
			if (req.isSecure()) {
				String base64Credentials = authorization.substring("Basic".length()).trim();
				CodecImpl codecimpl = new CodecImpl();
				try {
					values[0] = codecimpl.getUsername(base64Credentials);
					values[1] = codecimpl.getPassword(base64Credentials);
				} catch (CodecException e) {
					e.printStackTrace();
				}
			} else {
				try {
					resp.getWriter()
							.println(
									"<a href='http://docs.oracle.com/javaee/5/tutorial/doc/bnbxw.html'>Establishing a Secure Connection Using SSL</a>");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return values;
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
	public void setClientProperty(ClientProperty clientProperty) {
		this.clientProperty = clientProperty;
	}

}
