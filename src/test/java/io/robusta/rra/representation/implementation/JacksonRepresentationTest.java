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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.robusta.rra.files.House;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

/**
 * Created by Nicolas Zozol for Robusta Code
 *
 * @author Nicolas Zozol
 */
public class JacksonRepresentationTest extends JsonRepresentationTest<JacksonRepresentation> {

    public JacksonRepresentationTest() {
        isJson = true;
        this.emptyRepresentation = new JacksonRepresentation();
        this.schoolRepresentation = new JacksonRepresentation( readJson() );
    }

    @Test
    public void testGetAs() {

        JacksonRepresentation representation = new JacksonRepresentation( "12" );
        assertTrue( representation.get( Long.class ).equals( 12L ) );

        representation = new JacksonRepresentation( "12" );
        assertTrue( representation.get( Integer.class ).equals( 12 ) );

        representation = new JacksonRepresentation( "12.345" );
        assertTrue( representation.get( Float.class ).equals( 12.345f ) );

        representation = new JacksonRepresentation( "\"test String\"" );
        assertEquals( representation.get( String.class ), "test String" );

    }

    @Test
    public void testGetAsInputStream() {

        String user = "{\"email\":\"email\",\"name\":\"name\"}";

        InputStream inputStream = new ByteArrayInputStream( user.getBytes() );

        JacksonRepresentation representation = new JacksonRepresentation( inputStream );
        System.out.println( representation.getDocument().toString() );
        System.out.println( user );

        assertTrue( representation.getDocument().toString().equals( user ) );

    }

    @Test
    public void testGetAsObject() {

        House whiteHouse = new House( "White House", 12.25f );

        String json = "{\"name\":\"White House\", \"price\":12.25}";

        JacksonRepresentation representation = new JacksonRepresentation( json );
        // System.out.println( representation.get( House.class ).toString() );
        assertTrue( representation.get( House.class ).equals( whiteHouse ) );

    }
}
