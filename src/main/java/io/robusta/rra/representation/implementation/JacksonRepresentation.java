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


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.robusta.rra.Representation;
import io.robusta.rra.Resource;
import io.robusta.rra.representation.JsonRepresentation;
import io.robusta.rra.representation.RepresentationException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

/**
 * Created by  Nicolas Zozol for Robusta Code
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class JacksonRepresentation implements JsonRepresentation<ObjectNode> {

    ObjectNode document;
    ObjectMapper mapper = new ObjectMapper();

    /**
     * In that case, is serialization if not null, it's always a JsonObject
     *
     * @param serialization
     */
    public JacksonRepresentation(HashMap<String, Object> serialization) {
        this.document = mapper.convertValue(serialization, ObjectNode.class);
    }

    public JacksonRepresentation(Object object) {
        this.document = mapper.convertValue(object, ObjectNode.class);
    }

    public JacksonRepresentation(String json) {
        try {
            this.document = (ObjectNode) mapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JacksonRepresentation() {
        this.document = null;
    }

    @Override
    public ObjectNode getDocument() {
        return this.document;
    }

    protected void createObjectIfEmtpy() {
        if (this.document == null) {
            this.document = mapper.createObjectNode();
        }
    }

    //TODO
    @Override
    public <T> T get(Class<T> type) throws RepresentationException {
        return get(type, this.document);
    }

    protected <T> T get(Class<T> type, ObjectNode element) throws RepresentationException {
        try {
            return (T) mapper.readValue(element.traverse(), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Representation addToArray(Resource resource, boolean eager) {
        return null;
    }

    @Override
    public Representation addToArray(Object value) {
        return null;
    }

    @Override
    public <T> List<T> pluck(Class<T> type, String key) throws RepresentationException {
        return null;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public JsonType getTypeof() {
        return null;
    }

    @Override
    public Representation<ObjectNode> createObject() {
        return null;
    }

    @Override
    public Representation<ObjectNode> createArray() {
        return null;
    }

    @Override
    public String get(String key) throws RepresentationException {
        return null;
    }

    @Override
    public <T> T get(Class<T> type, String key) throws RepresentationException {
        return null;
    }

    @Override
    public boolean hasPossiblyEmpty(String... keys) {
        return false;
    }

    @Override
    public boolean has(String... keys) {
        return false;
    }

    @Override
    public List<String> getMissingKeys() {
        return null;
    }

    @Override
    public Representation set(String key, String value) {
        createObjectIfEmtpy();
        document.put(key, value);
        return this;
    }

    @Override
    public Representation set(String key, Object value) {
        return null;
    }

    @Override
    public List<String> getValues(String key) throws RepresentationException {
        return null;
    }

    @Override
    public <T> List<T> getValues(Class<T> type, String key) throws RepresentationException {
        return null;
    }

    @Override
    public Representation add(String key, Object value) {
        return null;
    }

    @Override
    public Representation add(String key, Resource resource, boolean eager) {
        return null;
    }

    @Override
    public Representation addAll(String key, List values) {
        return null;
    }

    @Override
    public Representation merge(String keyForCurrent, String keyForNew, Representation representation) {
        return null;
    }

    @Override
    public Representation remove(String key) throws RepresentationException {
        int i = 0;
        if (key.contains(".")) {
            String[] keys = key.split("\\.");
            if (keys.length == 0) {
                throw new IllegalArgumentException("Malformed key " + keys + " ; use something like user.school.id");
            }
            ObjectNode current = document;
            for (i = 0; i < keys.length - 1; i++) {
                current = (ObjectNode) current.get(keys[i]);
                if (current == null) {
                    throw new IllegalArgumentException("There is no valid object for key " + keys[i] + " in '" + key + "'");
                }
            }
            if (current.get(keys[keys.length - 1]) == null) {
                throw new IllegalArgumentException("There is no valid object for key " + keys[keys.length - 1] + " in '" + key + "'");
            }
            current.remove(keys[keys.length - 1]);
        } else {
            if (document.get(key) == null) {
                throw new IllegalArgumentException("There is no valid object for key " + key);
            }
            document.remove(key);
        }
        return this;
    }

    @Override
    public Representation fetch(String key) {
        return null;
    }

    @Override
    public Representation copy() {
        return null;
    }

    @Override
    public Representation createNewRepresentation(Object newObject) {
        return null;
    }

    @Override
    public Representation createNewRepresentation() {
        return null;
    }


    @Override
    public String toString() {
        return this.document.toString();
    }
}
