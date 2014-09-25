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

import com.google.gson.JsonElement;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.SerializableConverter;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.mapper.Mapper;

import io.robusta.rra.representation.JsonRepresentation;
import io.robusta.rra.representation.Representation;
import io.robusta.rra.representation.RepresentationException;
import io.robusta.rra.resource.Resource;
import io.robusta.rra.resource.ResourceSerializer;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

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
public class XstreamRepresentation  implements JsonRepresentation<String>{

    String document;
    Object data;
    XStream xStream;
    RraSerializerConverter converter;

    public XstreamRepresentation(Object data) {

        this.data = data;
        xStream = new XStream(new JettisonMappedXmlDriver());
       if(data != null){
            this.converter = new RraSerializerConverter(data, xStream.getMapper(), xStream.getReflectionProvider());
            xStream.registerConverter(converter);
            xStream.setMode(XStream.NO_REFERENCES);
            xStream.alias("object", Object.class);
        }
    }

    @Override
    public String getDocument() {
        return this.converter.serialization.toString();
    }

    @Override
    public <T> T get(Class<T> type) throws RepresentationException {
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
    public boolean isNull() {
        return this.data == null;
    }


    @Override
    public boolean isBoolean() {
        return this.data instanceof Boolean;
    }

    @Override
    public boolean isString() {
        return this.data instanceof String;
    }

    @Override
    public boolean isNumber() {
        return this.data instanceof Number;
    }


    @Override
    public boolean isPrimitive() {
        return (this.data instanceof Character) || (this.data instanceof Boolean) || (this.data instanceof Number) || (this.data instanceof String);
    }

    @Override
    public boolean isArray() {
        return this.data instanceof Collection || this.data instanceof Object[];
    }

    @Override
    public boolean isObject() {
        return this.data instanceof Object;
    }

    @Override
    public JsonType getTypeof() {
        return null;
    }

    @Override
    public Representation<String> createObject() {
        return null;
    }

    @Override
    public Representation<String> createArray() {
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
        //this.converter.setters.put(key, value);
       /* this.converter.serialization.put(key, value);
        return this;*/
        return null;
    }

    @Override
    public Representation set(String key, Object value) {
        //this.converter.setters.put(key, value);
        /*this.converter.serialization.put(key, value);
        return this;*/
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
       /* this.converter.setters.putAll(this.converter.serialization);
        this.converter.serialization.remove(key); */
        return null;
    }

    @Override
    public Representation fetch(String key) {

        Object obj= this.converter.serialization.get(key);
        XstreamRepresentation newRepresentation = new XstreamRepresentation(obj);
        return newRepresentation;
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
    public Representation createNewRepresentation(InputStream inputStream) {
        return null;
    }

    @Override
    public Representation createNewRepresentation(String json) {
        return null;
    }

    @Override
    public Representation createNewRepresentation() {
        return null;
    }


    @Override
    public String toString() {
        this.document=xStream.toXML(this.data);
        return this.document;
    }



    class RraSerializerConverter extends ReflectionConverter{

        HashMap<String, Object> setters = new HashMap<String, Object>();
        HashMap<String, Object> serialization = new HashMap<String, Object>();

        public RraSerializerConverter(Object resource, Mapper mapper, ReflectionProvider reflectionProvider) {
            super(mapper, reflectionProvider);

        }


        @Override
        public void marshal(Object original, HierarchicalStreamWriter writer, MarshallingContext context) {

            serialization = ResourceSerializer.serialize(original);
            System.out.println("Marshalling "+original +" - "+serialization.toString());


            for (Map.Entry<String, Object> entry : serialization.entrySet()){
                writer.startNode(entry.getKey());
                if(entry.getValue() == null){
                    writer.setValue(null);
                    context.put(entry.getKey(), null);
                }else {
                    context.convertAnother(entry.getValue());
                }
                writer.endNode();
            }
        }

        @Override
        public boolean canConvert(Class type) {
            boolean result = Resource.class.isAssignableFrom(type);
            System.out.println("trying convert "+type.getSimpleName() + " to Resource : "+result);
            return result;
        }
    }

}


