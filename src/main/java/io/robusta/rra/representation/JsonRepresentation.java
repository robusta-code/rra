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

package io.robusta.rra.representation;

import io.robusta.rra.resource.Resource;

import java.util.List;

/**
 * @author Nicolas Zozol
 *
 * @param <Document>
 */
/**
 * @author dev
 *
 * @param <Document>
 */
/**
 * @author dev
 *
 * @param <Document>
 */
public interface JsonRepresentation<Document> extends Representation<Document> {
    enum JsonType {
        OBJECT, BOOLEAN, STRING, NUMBER, ARRAY, NULL, UNDEFINED;
    }

    /**
     * Add a new element ( even if one of the same key exists) to the
     * representation. It pushes a value if the current Json Element is a Json
     * array. If it does not exist, the array is created. If it exists and is
     * not an array, we throw an exception. In XML, the rule is different. If
     * eager is used, then resource.getRepresentation() must be 'compatible'
     * with this Representation. The best compatibility is of course the same
     * class, but this may depend on the implementation.
     *
     * Suppose we have a Topic with User and Comments, each comment have a User
     * We want to add a Comment to the Topic
     *
     * add comments eagerly: { user :{id:12, name:'jo'} comments:[ {id:1,
     * content:"Hello from Jo", user:{id:12, name:'jo'}}, {id:2,
     * content:"Hello from Jack", user:{id:13, name:'jack'}}, ] }
     *
     * add comments not eagerly { user :{id:12, name:'jo'} comments:[ {id:1,
     * content:"Hello from Jo", user:12}, {id:2, content:"Hello from Jack",
     * user:13}, ] }
     *
     *
     * @param resource
     * @param eager
     *            : if false, Resource dependencies will show only their ids
     * @return the updated representation
     */
    public Representation addToArray( Resource resource, boolean eager );

    /**
     * Add a new element. In Json, to an array, in Xml to the current node
     * 
     * @param value
     * @return the updated representation
     */
    public Representation addToArray( Object value );

    /**
     * Pluck extracting a list of property values.
     * <p>
     * From Underscore.js documentation :<br/>
     * var stooges = [{name : 'moe', age : 40}, {name : 'larry', age : 50},
     * {name : 'curly', age : 60}];<br/>
     * _.pluck(stooges, 'name');<br/>
     * => ["moe", "larry", "curly"]<br/>
     * </p>
     *
     * @param type
     *            type of returned elements
     * @param key
     * @throws RepresentationException
     *             if the current Representation is not a JsonArray
     */
    public <T> List<T> pluck( Class<T> type, String key ) throws RepresentationException;

    /**
     * check if it's a primitive
     * @return
     */
    public boolean isPrimitive();

    /**
     * check if it's an object
     * @return
     */
    public boolean isObject();

    /**
     * check if it's a boolean
     * @return
     */
    public boolean isBoolean();

    /**
     * check if it's a string
     * @return
     */
    public boolean isString();

    /**
     * check if it's a number
     * @return
     */
    public boolean isNumber();

    /**
     * check if it's an array
     * @return
     */
    public boolean isArray();

    /**
     * check if it's null
     * @return
     */
    public boolean isNull();

    /**
     * return the type of the object
     * @return
     */
    public JsonType getTypeof();

    /**
     * Create an empty object
     * 
     * @return a Representation bound to an empty object
     * @throws IllegalStateException
     *             when a document already exists
     */
    public Representation<Document> createObject();

    /**
     * Create an empty array
     * 
     * @return a Representation bound to an empty array
     * @throws IllegalStateException
     *             when a document already exists
     */
    public Representation<Document> createArray();

}
