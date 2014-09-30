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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
 */
public class JacksonRepresentation implements JsonRepresentation<JsonNode> {

    JsonNode     document;
    ObjectMapper mapper      = new ObjectMapper();
    List<String> missingKeys = new ArrayList<String>( 5 );

    /**
     * In that case, is serialization if not null, it's always a JsonObject
     *
     * @param serialization
     */
    public JacksonRepresentation( HashMap<String, Object> serialization ) {
        this.document = mapper.convertValue( serialization, JsonNode.class );
    }

    /**
     * @param object
     */
    public JacksonRepresentation( Object object ) {
        // this.document = mapper.convertValue(object, JsonNode.class);
        this.document = mapper.valueToTree( object );
    }

    /**
     * @param json
     */
    public JacksonRepresentation( String json ) {
        try {
            // this.document = mapper.readTree( json );
            this.document = mapper.readValue( json, JsonNode.class );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * @param inputStream
     */
    public JacksonRepresentation( InputStream inputStream ) {
        try {
            this.document = mapper.readValue( inputStream, JsonNode.class );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    public JacksonRepresentation() {
        this.document = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.Representation#getDocument()
     */
    @Override
    public JsonNode getDocument() {
        return this.document;
    }

    /**
     * 
     */
    protected void createObjectIfEmtpy() {
        if ( this.document == null ) {
            this.document = mapper.createObjectNode();
        }
    }

    // TODO
    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.Representation#get(java.lang.Class)
     */
    @Override
    public <T> T get( Class<T> type ) throws RepresentationException {
        return get( type, this.document );
    }

    /**
     * @param type
     * @param element
     * @return
     * @throws RepresentationException
     */
    protected <T> T get( Class<T> type, JsonNode element ) throws RepresentationException {
        try {
            return mapper.treeToValue( element, type );
            // return mapper.readValue(element.traverse(), type);
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.Representation#get(java.lang.String)
     */
    @Override
    public String get( String key ) throws RepresentationException {
        return this.get( String.class, key );
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.Representation#get(java.lang.Class,
     * java.lang.String)
     */
    @Override
    public <T> T get( Class<T> type, String key ) throws RepresentationException {
        JsonNode element = this.document.get( key );
        return get( type, element );
    }

    /**
     * @return
     */
    protected ObjectNode asObject() {
        throwIfNotObject();
        return (ObjectNode) this.document;
    }

    /**
     * @return
     */
    protected ArrayNode asArray() {
        throwIfNotArray();
        return (ArrayNode) this.document;
    }

    /**
     * @param elt
     * @throws RepresentationException
     */
    protected void throwIfNull( JsonNode elt ) throws RepresentationException {
        if ( elt == null ) {
            throw new RepresentationException( "The current element is null" );
        }
    }

    /**
     * @param elt
     * @throws RepresentationException
     */
    protected void throwIfNotArray( JsonNode elt ) throws RepresentationException {
        if ( !elt.isArray() ) {
            throw new RepresentationException( "The current element is not a JSON array but a " + this.getTypeof()
                    + " and it can't add an object the correct way" );
        }
    }

    /**
     * @throws RepresentationException
     */
    protected void throwIfNotArray() throws RepresentationException {
        throwIfNull( this.document );
        throwIfNotArray( this.document );
    }

    /**
     * @param elt
     * @throws RepresentationException
     */
    protected void throwIfNotObject( JsonNode elt ) throws RepresentationException {
        if ( !elt.isObject() ) {
            throw new RepresentationException( "The current element is not a JSON object but a " + this.getTypeof()
                    + " and thus has no key" );
        }
    }

    /**
     * @throws RepresentationException
     */
    protected void throwIfNotObject() throws RepresentationException {
        throwIfNull( this.document );
        throwIfNotObject( this.document );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.robusta.rra.representation.JsonRepresentation#addToArray(io.robusta
     * .rra.resource.Resource, boolean)
     */
    @Override
    public Representation addToArray( Resource resource, boolean eager ) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.robusta.rra.representation.JsonRepresentation#addToArray(java.lang
     * .Object)
     */
    @Override
    public Representation addToArray( Object value ) {
        throwIfNotArray();
        asArray().add( mapper.valueToTree( value ) );
        return this;
    }

    // TODO
    /*
     * (non-Javadoc)
     * 
     * @see
     * io.robusta.rra.representation.JsonRepresentation#pluck(java.lang.Class,
     * java.lang.String)
     */
    @Override
    public <T> List<T> pluck( Class<T> type, String key ) throws RepresentationException {
        throwIfNotArray();
        List<T> result = new ArrayList<T>();
        for ( JsonNode elt : asArray() ) {
            result.add( get( type, elt ) );
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
        return ( this.document.isBoolean() && this.document.isShort() && this.document.isInt()
                && this.document.isLong() && this.document.isFloat() && this.document.isDouble() );
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.JsonRepresentation#isObject()
     */
    @Override
    public boolean isObject() {
        return this.document.isObject();
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.JsonRepresentation#isBoolean()
     */
    @Override
    public boolean isBoolean() {
        return this.document.isBoolean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.JsonRepresentation#isString()
     */
    @Override
    public boolean isString() {
        return this.document.isTextual();
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.JsonRepresentation#isNumber()
     */
    @Override
    public boolean isNumber() {
        return this.document.isNumber();
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.JsonRepresentation#isArray()
     */
    @Override
    public boolean isArray() {
        return this.document.isArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.JsonRepresentation#isNull()
     */
    @Override
    public boolean isNull() {
        return this.document.isNull();
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.JsonRepresentation#getTypeof()
     */
    @Override
    public JsonType getTypeof() {
        if ( this.isString() ) {
            return JsonType.STRING;
        } else if ( this.isArray() ) {
            return JsonType.ARRAY;
        } else if ( this.isBoolean() ) {
            return JsonType.BOOLEAN;
        } else if ( this.isNumber() ) {
            return JsonType.NUMBER;
        } else if ( this.isNull() ) {
            return JsonType.NULL;
        } else if ( this.isObject() ) {
            return JsonType.OBJECT;
        } else
            throw new IllegalStateException( "Can't find the type of this document" );
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.JsonRepresentation#createObject()
     */
    @Override
    public Representation<JsonNode> createObject() {
        if ( this.document != null ) {
            throw new IllegalStateException(
                    "This representation is not Empty. Use createNewRepresentation() to get a new empty representation" );
        }
        this.document = mapper.createObjectNode();
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.JsonRepresentation#createArray()
     */
    @Override
    public Representation<JsonNode> createArray() {
        if ( this.document != null ) {
            throw new IllegalStateException(
                    "This representation is not Empty. Use createNewRepresentation() to get a new empty representation" );
        }
        this.document = mapper.createArrayNode();
        return this;
    }

    /**
     * @param key
     * @return
     */
    protected boolean has( String key ) {

        throwIfNotObject( this.document );
        return asObject().has( key );

    }

    /**
     * @param key
     * @return
     */
    protected boolean hasNotEmpty( String key ) {
        throwIfNotObject();
        JsonNode elt = asObject().get( key );
        if ( elt == null || elt.isNull() || ( elt.isTextual() && elt.textValue().isEmpty() ) ) {
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
    public boolean hasPossiblyEmpty( String... keys ) {
        boolean result = true;
        for ( String key : keys ) {
            if ( !has( key ) ) {
                missingKeys.add( key );
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
    public boolean has( String... keys ) {
        boolean result = true;
        for ( String key : keys ) {
            if ( !hasNotEmpty( key ) ) {
                missingKeys.add( key );
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

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.Representation#set(java.lang.String,
     * java.lang.String)
     */
    @Override
    public Representation set( String key, String value ) {
        throwIfNotObject();
        createObjectIfEmtpy();
        asObject().put( key, value );
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.Representation#set(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public Representation set( String key, Object value ) {
        throwIfNotObject();
        createObjectIfEmtpy();
        asObject().set( key, mapper.valueToTree( value ) );
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.robusta.rra.representation.Representation#getValues(java.lang.String)
     */
    @Override
    public List<String> getValues( String key ) throws RepresentationException {
        List<String> list = new ArrayList<String>();
        for ( JsonNode elt : asObject().get( key ) ) {
            list.add( elt.toString() );
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.robusta.rra.representation.Representation#getValues(java.lang.Class,
     * java.lang.String)
     */
    @Override
    public <T> List<T> getValues( Class<T> type, String key ) throws RepresentationException {
        List<T> list = new ArrayList<T>();
        for ( JsonNode elt : asObject().get( key ) ) {
            list.add( get( type, elt ) );
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
    public Representation add( String key, Object value ) {
        throwIfNotObject();
        throwIfNotArray( this.asObject().get( key ) );
        asObject().withArray( key ).add( mapper.valueToTree( value ) );
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.Representation#add(java.lang.String,
     * io.robusta.rra.resource.Resource, boolean)
     */
    @Override
    public Representation add( String key, Resource resource, boolean eager ) {
        throwIfNotObject();
        throwIfNotArray( this.asObject().get( key ) );
        JsonNode element = mapper.valueToTree( resource );
        asObject().withArray( key ).add( element );
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
    public Representation addAll( String key, List values ) {
        throwIfNotObject();
        throwIfNotArray( this.asObject().get( key ) );
        for ( Object value : values ) {
            asObject().withArray( key ).add( mapper.valueToTree( value ) );
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
    public Representation merge( String keyForCurrent, String keyForNew, Representation representation ) {
        if ( !( representation instanceof JacksonRepresentation ) ) {
            throw new IllegalArgumentException( "Can't merge a JacksonRepresentation with a "
                    + representation.getClass().getSimpleName() );
        }
        JacksonRepresentation mergedRepresentation = new JacksonRepresentation();
        mergedRepresentation.createObject();
        ObjectNode object = (ObjectNode) mergedRepresentation.getDocument();
        object.set( keyForCurrent, this.document );
        object.set( keyForNew, ( (JacksonRepresentation) representation ).getDocument() );
        return mergedRepresentation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.robusta.rra.representation.Representation#remove(java.lang.String)
     */
    @Override
    public Representation remove( String key ) throws RepresentationException {
        throwIfNotObject();
        int i = 0;
        if ( key.contains( "." ) ) {
            String[] keys = key.split( "\\." );
            if ( keys.length == 0 ) {
                throw new IllegalArgumentException( "Malformed key " + keys + " ; use something like user.school.id" );
            }
            ObjectNode current = asObject();
            for ( i = 0; i < keys.length - 1; i++ ) {
                current = (ObjectNode) current.get( keys[i] );
                if ( current == null ) {
                    throw new IllegalArgumentException( "There is no valid object for key " + keys[i] + " in '" + key
                            + "'" );
                }
            }
            if ( current.get( keys[keys.length - 1] ) == null ) {
                throw new IllegalArgumentException( "There is no valid object for key " + keys[keys.length - 1]
                        + " in '" + key + "'" );
            }
            current.remove( keys[keys.length - 1] );
        } else {
            if ( document.get( key ) == null ) {
                throw new IllegalArgumentException( "There is no valid object for key " + key );
            }
            asObject().remove( key );
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.robusta.rra.representation.Representation#fetch(java.lang.String)
     */
    @Override
    public Representation fetch( String key ) {
        throwIfNotObject();
        JsonNode root;

        if ( key.contains( "." ) ) {
            String[] keys = key.split( "\\." );
            if ( keys.length == 0 ) {
                throw new IllegalArgumentException( "Malformed key " + keys + " ; use something like user.school.id" );
            }
            String lastKey = keys[0];
            JsonNode current = asObject();
            for ( String newKey : keys ) {
                if ( !current.isObject() ) {
                    throw new IllegalArgumentException( "The key " + lastKey + " in '" + key
                            + "' doesn't point to a Json Object" );
                }
                ;
                current = current.get( newKey );
                if ( current == null ) {
                    throw new IllegalArgumentException( "There is no valid object for key " + newKey + "in '" + key
                            + "'" );
                }
            }
            root = current;
        } else {
            root = asObject().get( key );
        }

        if ( root == null ) {
            throw new IllegalArgumentException( "There is no valid object for key " + key );
        } else {
            JacksonRepresentation fetchedRepresentation = new JacksonRepresentation();
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
        String serialization = this.document.toString();
        JsonNode clone = null;
        try {
            clone = mapper.readTree( serialization );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        JacksonRepresentation representation = new JacksonRepresentation();
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
    public Representation createNewRepresentation( Object newObject ) {
        return new JacksonRepresentation( newObject );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.robusta.rra.representation.Representation#createNewRepresentation(
     * java.io.InputStream)
     */
    @Override
    public Representation createNewRepresentation( InputStream inputStream ) {
        return new JacksonRepresentation( inputStream );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.robusta.rra.representation.Representation#createNewRepresentation(
     * java.lang.String)
     */
    @Override
    public Representation createNewRepresentation( String json ) {
        return new JacksonRepresentation( json );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.robusta.rra.representation.Representation#createNewRepresentation()
     */
    @Override
    public Representation createNewRepresentation() {
        return new JacksonRepresentation();
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
