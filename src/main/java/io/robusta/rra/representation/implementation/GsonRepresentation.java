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

import io.robusta.rra.representation.JsonRepresentation;
import io.robusta.rra.representation.Representation;
import io.robusta.rra.representation.RepresentationException;
import io.robusta.rra.resource.Resource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

/**
 * Created by Nicolas Zozol for Robusta Code
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @author Nicolas Zozol
 *
 */
public class GsonRepresentation implements JsonRepresentation<JsonElement> {

	Gson gson = new Gson();
	JsonElement document;
	List<String> missingKeys = new ArrayList<String>(5);

	/**
	 * In that case, is serialization if not null, it's always a JsonObject
	 *
	 * @param serialization
	 */
	public GsonRepresentation(HashMap<String, Object> serialization) {
		this.document = gson.toJsonTree(serialization);
	}

	/**
	 * contructor
	 * 
	 * @param object
	 */
	public GsonRepresentation(Object object) {
		this.document = gson.toJsonTree(object);
	}

	/**
	 * contructor
	 * 
	 * @param json
	 */
	public GsonRepresentation(String json) {
		this.document = new JsonParser().parse(json);
	}

	/**
	 * contructor
	 * 
	 * @param inputStream
	 */
	public GsonRepresentation(InputStream inputStream) {
		this.document = new JsonParser().parse(new JsonReader(new InputStreamReader(inputStream)));
	}

	/**
	 * contructor
	 */
	public GsonRepresentation() {
		this.document = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.Representation#getDocument()
	 */
	@Override
	public JsonElement getDocument() {
		return this.document;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.Representation#get(java.lang.Class)
	 */
	@Override
	public <T> T get(Class<T> type) throws RepresentationException {
		return get(type, this.document);
	}

	// TODO : rename to map() ?
	/**
	 * convert a jsonElement into Object
	 * 
	 * @param type
	 * @param element
	 * @return
	 * @throws RepresentationException
	 */
	protected <T> T get(Class<T> type, JsonElement element) throws RepresentationException {

		if (type == Long.class) {
			return (T) (Long) element.getAsLong();
		} else if (type == Integer.class) {
			return (T) (Integer) element.getAsInt();
		} else if (type == Short.class) {
			return (T) (Short) element.getAsShort();
		} else if (type == Byte.class) {
			return (T) (Byte) element.getAsByte();
		} else if (type == BigInteger.class) {
			return (T) (BigInteger) element.getAsBigInteger();
		} else if (type == Double.class) {
			return (T) (Double) element.getAsDouble();
		} else if (type == Float.class) {
			return (T) (Float) element.getAsFloat();
		} else if (type == BigDecimal.class) {
			return (T) (BigDecimal) element.getAsBigDecimal();
		} else if (type == Boolean.class) {
			return (T) (Boolean) element.getAsBoolean();
		} else if (type == String.class) {
			return (T) element.getAsString();
		} else {
			return (T) gson.fromJson(element, type);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.Representation#get(java.lang.String)
	 */
	@Override
	public String get(String key) throws RepresentationException {
		return this.get(String.class, key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.Representation#get(java.lang.Class,
	 * java.lang.String)
	 */
	@Override
	public <T> T get(Class<T> type, String key) throws RepresentationException {

		JsonElement element = this.document.getAsJsonObject().get(key);
		return get(type, element);

	}

	/**
	 * check if document has the specified keys
	 * 
	 * @param key
	 * @return
	 */
	protected boolean has(String key) {

		throwIfNotObject(this.document);

		JsonObject object = this.document.getAsJsonObject();
		return object.has(key);

	}

	/**
	 * check if document is not null
	 * 
	 * @param key
	 * @return
	 */
	protected boolean hasNotEmpty(String key) {
		throwIfNotObject();
		JsonObject object = this.document.getAsJsonObject();
		JsonElement elt = object.get(key);
		if (elt == null || elt.isJsonNull() || (isString(elt) && elt.getAsString().isEmpty())) {
			// elt is null or empty string
			return false;
		} else {
			return true;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.robusta.rra.representation.Representation#hasPossiblyEmpty(java.lang
	 * .String[])
	 */
	@Override
	public boolean hasPossiblyEmpty(String... keys) {
		boolean result = true;
		for (String key : keys) {
			if (!has(key)) {
				missingKeys.add(key);
				result = false;
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.Representation#has(java.lang.String[])
	 */
	@Override
	public boolean has(String... keys) {
		boolean result = true;
		for (String key : keys) {
			if (!hasNotEmpty(key)) {
				missingKeys.add(key);
				result = false;
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.Representation#getMissingKeys()
	 */
	@Override
	public List<String> getMissingKeys() {
		return missingKeys;
	}

	/**
	 * if document is null, create a new document
	 */
	protected void createObjectIfEmtpy() {
		if (this.document == null) {
			this.document = new JsonObject();
		}
	}

	/**
	 * throw an exception if the jsonElement is null
	 * 
	 * @param elt
	 * @throws RepresentationException
	 */
	protected void throwIfNull(JsonElement elt) throws RepresentationException {
		if (elt == null) {
			throw new RepresentationException("The current element is null");
		}
	}

	/**
	 * throw an exception if the jsonElement is not an object
	 * 
	 * @param elt
	 * @throws RepresentationException
	 */
	protected void throwIfNotObject(JsonElement elt) throws RepresentationException {
		if (!elt.isJsonObject()) {
			throw new RepresentationException("The current element is not a JSON object but a " + this.getTypeof()
					+ " and thus has no key");
		}
	}

	/**
	 * throw an exception if the document is not an object
	 * 
	 * @throws RepresentationException
	 */
	protected void throwIfNotObject() throws RepresentationException {
		throwIfNull(this.document);
		throwIfNotObject(this.document);
	}

	/**
	 * throw an exception if the jsonElement is not an array
	 * 
	 * @param elt
	 * @throws RepresentationException
	 */
	protected void throwIfNotArray(JsonElement elt) throws RepresentationException {
		if (!elt.isJsonArray()) {
			throw new RepresentationException("The current element is not a JSON array but a " + this.getTypeof()
					+ " and it can't add an object the correct way");
		}
	}

	/**
	 * throw an exception if the document is not an array
	 * @throws RepresentationException
	 */
	protected void throwIfNotArray() throws RepresentationException {
		throwIfNull(this.document);
		throwIfNotArray(this.document);
	}

	/**
	 * convert the document into a JsonArray and return it. 
	 * throw an exception if the document is not an object
	 * @return
	 */
	protected JsonObject asObject() {
		throwIfNotObject();
		return this.document.getAsJsonObject();
	}

	/**
	 * convert the document into a JsonArray and return it. 
	 * throw an exception if the document is not an array
	 * @return
	 */
	protected JsonArray asArray() {
		throwIfNotArray();
		return this.document.getAsJsonArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.Representation#set(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Representation set(String key, String value) {
		createObjectIfEmtpy();
		asObject().add(key, new JsonPrimitive(value));
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.Representation#set(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public Representation set(String key, Object value) {
		createObjectIfEmtpy();
		asObject().add(key, gson.toJsonTree(value));
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.robusta.rra.representation.Representation#getValues(java.lang.String)
	 */
	@Override
	public List<String> getValues(String key) throws RepresentationException {
		List<String> list = new ArrayList<String>();
		for (JsonElement elt : asObject().get(key).getAsJsonArray()) {
			list.add(elt.toString());
		}
		return list;
	}

	// TODO : Not tested. If it works here, that's great !!
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.robusta.rra.representation.Representation#getValues(java.lang.Class,
	 * java.lang.String)
	 */
	@Override
	public <T> List<T> getValues(Class<T> type, String key) throws RepresentationException {
		List<T> list = new ArrayList<T>();
		for (JsonElement elt : asObject().get(key).getAsJsonArray()) {
			list.add(get(type, elt));
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.Representation#add(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public Representation add(String key, Object value) {
		throwIfNotObject();
		throwIfNotArray(this.asObject().get(key));
		asObject().get(key).getAsJsonArray().add(gson.toJsonTree(value));
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.Representation#add(java.lang.String,
	 * io.robusta.rra.resource.Resource, boolean)
	 */
	@Override
	public Representation add(String key, Resource resource, boolean eager) {
		throwIfNotObject();
		throwIfNotArray(this.asObject().get(key));
		// TODO : to use eager, we must look at ReflectiveTypeAdapterFactory
		// around line 82
		JsonElement element = gson.toJsonTree(resource);
		asObject().get(key).getAsJsonArray().add(element);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.robusta.rra.representation.Representation#addAll(java.lang.String,
	 * java.util.List)
	 */
	@Override
	public Representation addAll(String key, List values) {
		throwIfNotObject();
		throwIfNotArray(this.asObject().get(key));
		JsonArray array = this.asObject().get(key).getAsJsonArray();
		for (Object value : values) {
			array.add(gson.toJsonTree(value));
		}

		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.Representation#merge(java.lang.String,
	 * java.lang.String, io.robusta.rra.representation.Representation)
	 */
	@Override
	public Representation merge(String keyForCurrent, String keyForNew, Representation representation) {
		if (!(representation instanceof GsonRepresentation)) {
			throw new IllegalArgumentException("Can't merge a GsonRepresentation with a "
					+ representation.getClass().getSimpleName());
		}
		GsonRepresentation mergedRepresentation = new GsonRepresentation();
		mergedRepresentation.createObject();
		JsonObject object = mergedRepresentation.document.getAsJsonObject();
		object.add(keyForCurrent, this.document);
		object.add(keyForNew, ((GsonRepresentation) representation).document);
		return mergedRepresentation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.robusta.rra.representation.Representation#remove(java.lang.String)
	 */
	@Override
	public Representation remove(String key) throws RepresentationException {
		int i = 0;
		if (key.contains(".")) {
			String[] keys = key.split("\\.");
			if (keys.length == 0) {
				throw new IllegalArgumentException("Malformed key " + keys + " ; use something like user.school.id");
			}
			JsonElement current = asObject();
			for (i = 0; i < keys.length - 1; i++) {
				current = current.getAsJsonObject().get(keys[i]);
				if (current == null) {
					throw new IllegalArgumentException("There is no valid object for key " + keys[i] + " in '" + key
							+ "'");
				}
			}
			if (current.getAsJsonObject().get(keys[keys.length - 1]) == null) {
				throw new IllegalArgumentException("There is no valid object for key " + keys[keys.length - 1]
						+ " in '" + key + "'");
			}
			current.getAsJsonObject().remove(keys[keys.length - 1]);
		} else {
			if (asObject().get(key) == null) {
				throw new IllegalArgumentException("There is no valid object for key " + key);
			}
			asObject().remove(key);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.Representation#fetch(java.lang.String)
	 */
	@Override
	public Representation fetch(String key) {
		throwIfNotObject();
		JsonElement root;

		if (key.contains(".")) {
			String[] keys = key.split("\\.");
			if (keys.length == 0) {
				throw new IllegalArgumentException("Malformed key " + keys + " ; use something like user.school.id");
			}
			String lastKey = keys[0];
			JsonElement current = asObject();
			for (String newKey : keys) {
				if (!current.isJsonObject()) {
					throw new IllegalArgumentException("The key " + lastKey + " in '" + key
							+ "' doesn't point to a Json Object");
				}
				;
				current = current.getAsJsonObject().get(newKey);
				if (current == null) {
					throw new IllegalArgumentException("There is no valid object for key " + newKey + "in '" + key
							+ "'");
				}
			}
			root = current;
		} else {
			root = asObject().get(key);
		}

		if (root == null) {
			throw new IllegalArgumentException("There is no valid object for key " + key);
		} else {
			GsonRepresentation fetchedRepresentation = new GsonRepresentation();
			fetchedRepresentation.document = root;
			return fetchedRepresentation;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.Representation#copy()
	 */
	@Override
	public Representation copy() {
		String serialization = gson.toJson(this.document);
		JsonElement clone = new JsonParser().parse(serialization);
		GsonRepresentation representation = new GsonRepresentation();
		representation.document = clone;
		return representation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.robusta.rra.representation.Representation#createNewRepresentation(
	 * java.lang.Object)
	 */
	@Override
	public Representation createNewRepresentation(Object newObject) {
		return new GsonRepresentation(newObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.robusta.rra.representation.Representation#createNewRepresentation(
	 * java.lang.String)
	 */
	@Override
	public Representation createNewRepresentation(String json) {
		return new GsonRepresentation(json);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.robusta.rra.representation.Representation#createNewRepresentation(
	 * java.io.InputStream)
	 */
	@Override
	public Representation createNewRepresentation(InputStream inputStream) {
		return new GsonRepresentation(inputStream);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.robusta.rra.representation.Representation#createNewRepresentation()
	 */
	@Override
	public Representation createNewRepresentation() {
		return new GsonRepresentation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.robusta.rra.representation.JsonRepresentation#addToArray(io.robusta
	 * .rra.resource.Resource, boolean)
	 */
	@Override
	public Representation addToArray(Resource resource, boolean eager) {
		throwIfNotArray();
		// TODO : to use eager, we must look at ReflectiveTypeAdapterFactory
		// around line 82
		JsonElement element = gson.toJsonTree(resource);
		asArray().add(element);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.robusta.rra.representation.JsonRepresentation#addToArray(java.lang
	 * .Object)
	 */
	@Override
	public Representation addToArray(Object value) {
		throwIfNotArray();
		JsonElement element = gson.toJsonTree(value);
		asArray().add(element);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.robusta.rra.representation.JsonRepresentation#pluck(java.lang.Class,
	 * java.lang.String)
	 */
	@Override
	public <T> List<T> pluck(Class<T> type, String key) throws RepresentationException {
		throwIfNotArray();
		List<T> result = new ArrayList<T>();
		for (JsonElement elt : asArray()) {
			result.add(get(type, elt));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.JsonRepresentation#isPrimitive()
	 */
	@Override
	public boolean isPrimitive() {
		return this.document.isJsonPrimitive();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.JsonRepresentation#isObject()
	 */
	@Override
	public boolean isObject() {
		return this.document.isJsonObject();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.JsonRepresentation#isBoolean()
	 */
	@Override
	public boolean isBoolean() {
		// checking that it' a primitive
		if (this.document.isJsonPrimitive()) {
			String json = this.document.toString();
			return json.equalsIgnoreCase("true") || json.equalsIgnoreCase("false");
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.JsonRepresentation#isString()
	 */
	@Override
	public boolean isString() {
		return isString(this.document);
	}

	/**
	 * @param elt
	 * @return
	 */
	private boolean isString(JsonElement elt) {
		return elt.isJsonPrimitive()
				&& (elt.toString().trim().startsWith("\"") || elt.toString().trim().startsWith("'"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.JsonRepresentation#isNumber()
	 */
	@Override
	public boolean isNumber() {
		return this.document.isJsonPrimitive() && !this.isBoolean() && !this.isString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.JsonRepresentation#isArray()
	 */
	@Override
	public boolean isArray() {
		return this.document.isJsonArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.JsonRepresentation#isNull()
	 */
	@Override
	public boolean isNull() {
		return this.document.isJsonNull();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.JsonRepresentation#getTypeof()
	 */
	@Override
	public JsonType getTypeof() {
		if (this.isString()) {
			return JsonType.STRING;
		} else if (this.isArray()) {
			return JsonType.ARRAY;
		} else if (this.isBoolean()) {
			return JsonType.BOOLEAN;
		} else if (this.isNumber()) {
			return JsonType.NUMBER;
		} else if (this.isNull()) {
			return JsonType.NULL;
		} else if (this.isObject()) {
			return JsonType.OBJECT;
		} else
			throw new IllegalStateException("Can't find the type of this document");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.JsonRepresentation#createObject()
	 */
	@Override
	public Representation<JsonElement> createObject() {
		if (this.document != null) {
			throw new IllegalStateException(
					"This representation is not Empty. Use createNewRepresentation() to get a new empty representation");
		}
		this.document = new JsonObject();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.robusta.rra.representation.JsonRepresentation#createArray()
	 */
	@Override
	public Representation<JsonElement> createArray() {
		if (this.document != null) {
			throw new IllegalStateException(
					"This representation is not Empty. Use createNewRepresentation() to get a new empty representation");
		}
		this.document = new JsonArray();
		return this;
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	protected JsonType getJsonType(Class type) {
		if (type == Integer.class || type == Long.class || type == Integer.class || type == Integer.class) {
			return JsonType.NUMBER;
		} else if (type == String.class) {

		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.document.toString();
	}
}
