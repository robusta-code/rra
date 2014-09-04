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


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.robusta.rra.Representation;
import io.robusta.rra.Resource;
import io.robusta.rra.representation.JsonRepresentation;
import io.robusta.rra.representation.RepresentationException;

import java.io.IOException;
import java.util.ArrayList;
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
public class JacksonRepresentation implements JsonRepresentation<JsonNode> {

    JsonNode document;
    ObjectMapper mapper = new ObjectMapper();

    /**
     * In that case, is serialization if not null, it's always a JsonObject
     *
     * @param serialization
     */
    public JacksonRepresentation(HashMap<String, Object> serialization) {
        this.document = mapper.convertValue(serialization, JsonNode.class);
    }

    public JacksonRepresentation(Object object) {
 //this.document = mapper.convertValue(object, JsonNode.class);
        this.document = mapper.valueToTree(object);
    }

    public JacksonRepresentation(String json) {
        try {
            this.document = mapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JacksonRepresentation() {
        this.document = null;
    }

    @Override
    public JsonNode getDocument() {
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

    protected <T> T get(Class<T> type, JsonNode element) throws RepresentationException {
        try {
            return mapper.treeToValue(element, type);
            //return mapper.readValue(element.traverse(), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String get(String key) throws RepresentationException {
        return this.get(String.class, key);
    }

    @Override
    public <T> T get(Class<T> type, String key) throws RepresentationException {
        JsonNode element = this.document.get(key);
        return get(type, element);
    }

    protected ObjectNode asObject(){
        throwIfNotObject();
        return (ObjectNode) this.document;
    }

    protected ArrayNode asArray(){
        throwIfNotArray();
        return (ArrayNode) this.document;
    }

    protected void throwIfNotArray(JsonNode elt) throws RepresentationException {
        if (!elt.isArray()) {
            throw new RepresentationException("The current element is not a JSON array but a " + this.getTypeof() + " and it can't add an object the correct way");
        }
    }

    protected void throwIfNotArray() throws RepresentationException {
        throwIfNotArray(this.document);
    }

    protected void throwIfNotObject(JsonNode elt) throws RepresentationException {
        if (!elt.isObject()) {
            throw new RepresentationException("The current element is not a JSON object but a " + this.getTypeof() + " and thus has no key");
        }
    }

    protected void throwIfNotObject() throws RepresentationException {
        throwIfNotObject(this.document);
    }

    @Override
    public Representation addToArray(Resource resource, boolean eager) {
        return null;
    }

    @Override
    public Representation addToArray(Object value) {
        throwIfNotArray();
        asArray().add(mapper.valueToTree(value));
        return this;
    }

    //TODO
    @Override
    public <T> List<T> pluck(Class<T> type, String key) throws RepresentationException {
        throwIfNotArray();
        List<T> result = new ArrayList<T>();
        for (JsonNode elt : asArray()){
            result.add(get(type, elt));
        }
        return result;
    }

    @Override
    public boolean isPrimitive() {
        return (this.document.isBoolean() && this.document.isShort() && this.document.isInt() && this.document.isLong() && this.document.isFloat() && this.document.isDouble());
    }

    @Override
    public boolean isObject() {
        return this.document.isObject();
    }

    @Override
    public boolean isBoolean() {
        return this.document.isBoolean();
    }

    @Override
    public boolean isString() {
        return this.document.isTextual();
    }

    @Override
    public boolean isNumber() {
        return this.document.isNumber();
    }

    @Override
    public boolean isArray() {
        return this.document.isArray();
    }

    @Override
    public boolean isNull() {
        return this.document.isNull();
    }

    @Override
    public JsonType getTypeof() {
        if (this.isString()){
            return JsonType.STRING;
        }else if (this.isArray()){
            return JsonType.ARRAY;
        }else if (this.isBoolean()){
            return JsonType.BOOLEAN;
        }else if (this.isNumber()){
            return JsonType.NUMBER;
        }else if (this.isNull()){
            return JsonType.NULL;
        }else if (this.isObject()){
            return JsonType.OBJECT;
        }else throw new IllegalStateException("Can't find the type of this document");
    }

    @Override
    public Representation<JsonNode> createObject() {
        if (this.document != null){
            throw new IllegalStateException("This representation is not Empty. Use createNewRepresentation() to get a new empty representation");
        }
        this.document= mapper.createObjectNode();
        return this;
    }

    @Override
    public Representation<JsonNode> createArray() {
        if (this.document != null){
            throw new IllegalStateException("This representation is not Empty. Use createNewRepresentation() to get a new empty representation");
        }
        this.document= mapper.createArrayNode();
        return this;
    }


    protected boolean has(String key) {

        throwIfNotObject(this.document);
        return asObject().has(key);

    }

    protected boolean hasNotEmpty(String key) {
        throwIfNotObject(this.document);
        JsonNode elt = asObject().get(key);
        if (elt.isNull() || (elt.isTextual() && elt.textValue().isEmpty())) {
            //elt is null or empty string
            return false;
        } else {
            return true;
        }

    }

    List<String> missingKeys = new ArrayList<String>(5);

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

    @Override
    public List<String> getMissingKeys() {
        return missingKeys;
    }

    @Override
    public Representation set(String key, String value) {
        throwIfNotObject();
        createObjectIfEmtpy();
        asObject().put(key, value);
        return this;
    }

    @Override
    public Representation set(String key, Object value) {
        throwIfNotObject();
        createObjectIfEmtpy();
        asObject().set(key, mapper.valueToTree(value));
        return this;
    }

    @Override
    public List<String> getValues(String key) throws RepresentationException {
        List<String> list = new ArrayList<String>();
        for (JsonNode elt : asObject().get(key)) {
            list.add(elt.textValue());
        }
        return list;
    }

    @Override
    public <T> List<T> getValues(Class<T> type, String key) throws RepresentationException {
        List<T> list = new ArrayList<T>();
        for (JsonNode elt : asObject().get(key)) {
            list.add(get(type, elt));
        }
        return list;
    }

    @Override
    public Representation add(String key, Object value) {
        throwIfNotObject();
        throwIfNotArray(this.asObject().get(key));
        asObject().withArray(key).add(mapper.valueToTree(value));
        return this;
    }

    @Override
    public Representation add(String key, Resource resource, boolean eager) {
        throwIfNotObject();
        throwIfNotArray(this.asObject().get(key));
        JsonNode element = mapper.valueToTree(resource);
        asObject().withArray(key).add(element);
        return this;
    }

    @Override
    public Representation addAll(String key, List values) {
        throwIfNotObject();
        throwIfNotArray(this.asObject().get(key));
        for (Object value : values){
            asObject().withArray(key).add(mapper.valueToTree(value));
        }
        return this;
    }

    @Override
    public Representation merge(String keyForCurrent, String keyForNew, Representation representation) {
        if (! (representation instanceof JacksonRepresentation)){
            throw new IllegalArgumentException("Can't merge a JacksonRepresentation with a "+representation.getClass().getSimpleName());
        }
        JacksonRepresentation mergedRepresentation = new JacksonRepresentation();
        mergedRepresentation.createObject();
        ObjectNode object = (ObjectNode) mergedRepresentation.getDocument();
        object.set(keyForCurrent, this.document);
        object.set(keyForNew, ((JacksonRepresentation) representation).getDocument());
        return mergedRepresentation;
    }

    @Override
    public Representation remove(String key) throws RepresentationException {
        throwIfNotObject();
        int i = 0;
        if (key.contains(".")) {
            String[] keys = key.split("\\.");
            if (keys.length == 0) {
                throw new IllegalArgumentException("Malformed key " + keys + " ; use something like user.school.id");
            }
            ObjectNode current = asObject();
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
            asObject().remove(key);
        }
        return this;
    }

    @Override
    public Representation fetch(String key) {
        throwIfNotObject();
        JsonNode root;

        if (key.contains(".")){
            String[] keys = key.split("\\.");
            if (keys.length == 0){
                throw new IllegalArgumentException("Malformed key "+keys+" ; use something like user.school.id");
            }
            String lastKey=keys[0];
            JsonNode current = asObject();
            for (String newKey : keys){
                if (! current.isObject()){
                    throw new IllegalArgumentException("The key "+lastKey+ " in '"+key+"' doesn't point to a Json Object");
                };
                current = current.get(newKey);
                if (current == null){
                    throw new IllegalArgumentException("There is no valid object for key "+newKey+ "in '"+key+"'");
                }
            }
            root = current;
        }else{
            root = asObject().get(key);
        }

        if (root == null){
            throw new IllegalArgumentException("There is no valid object for key "+key);
        }else{
            JacksonRepresentation fetchedRepresentation = new JacksonRepresentation();
            fetchedRepresentation.document = root;
            return fetchedRepresentation;
        }

    }

    @Override
    public Representation copy() {
        String serialization = this.document.toString();
        JsonNode clone=null;
        try {
            clone = mapper.readTree(serialization);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JacksonRepresentation representation = new JacksonRepresentation();
        representation.document = clone;
        return representation;
    }

    @Override
    public Representation createNewRepresentation(Object newObject) {
        return new JacksonRepresentation(newObject);
    }

    @Override
    public Representation createNewRepresentation() {
        return new JacksonRepresentation();
    }


    @Override
    public String toString() {
        return this.document.toString();
    }
}
