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

package io.robusta.rra.representation.implementation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * Created by Stephanie Pitou on 15/09/14.
 */
@Provider
@Consumes("application/json")
@Produces("application/json")
public class GsonJaxRsProvider implements MessageBodyReader<GsonRepresentation>, MessageBodyWriter<GsonRepresentation> {

	private ObjectOutputStream oos;

	@Override
	public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
		return aClass == GsonRepresentation.class;
	}

	@Override
	public GsonRepresentation readFrom(Class<GsonRepresentation> gsonRepresentationClass, Type type,
			Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> stringStringMultivaluedMap,
			InputStream inputStream) throws IOException, WebApplicationException {
		return new GsonRepresentation(inputStream);
	}

	@Override
	public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
		return type == GsonRepresentation.class;
	}

	@Override
	public long getSize(GsonRepresentation representation, Class<?> aClass, Type type, Annotation[] annotations,
			MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(GsonRepresentation representation, Class<?> aClass, Type type, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, Object> stringObjectMultivaluedMap, OutputStream outputStream)
			throws IOException, WebApplicationException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);

		oos.writeObject(representation.toString());

		oos.flush();
		oos.close();
	}

	public void closeStream() {
		try {
			oos.flush();
			oos.close();
			System.out.println("Steam has been closed successfully !");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
