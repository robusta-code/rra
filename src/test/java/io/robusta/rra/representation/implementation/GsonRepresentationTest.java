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

import com.google.gson.Gson;
import io.robusta.rra.files.House;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by  Nicolas Zozol for Robusta Code
 * @author  Nicolas Zozol
 */
public class GsonRepresentationTest extends JsonRepresentationTest<GsonRepresentation> {


    public GsonRepresentationTest(){
        isJson = true;
        this.emptyRepresentation = new GsonRepresentation();
        this.schoolRepresentation = new GsonRepresentation(readJson());
    }


    @Test
    public void testGetAs(){

        GsonRepresentation representation = new GsonRepresentation("12");
        assertTrue(representation.get(Long.class).equals(12L));

        representation = new GsonRepresentation("12");
        assertTrue(representation.get(Integer.class).equals(12));

        representation = new GsonRepresentation("12.345");
        assertTrue(representation.get(Float.class).equals(12.345f));

        representation = new GsonRepresentation("'12'");
        assertEquals(representation.get(String.class), "12");

    }

    @Test
    public void testGetAsObject(){


      House whiteHouse = new House("White House", 12.25f);

        String json = "{name:'White House', price:12.25}";
        assertTrue(new Gson().fromJson(json, House.class).equals(whiteHouse));

        GsonRepresentation representation = new GsonRepresentation(json);
        assertTrue(representation.get(House.class).equals(whiteHouse));



    }

    @Test
    public void testGetAsInputStream(){

        String user = "{\"email\":\"email\",\"name\":\"name\"}";

        InputStream inputStream=new ByteArrayInputStream(user.getBytes());


        GsonRepresentation representation = new GsonRepresentation(inputStream);
        System.out.println(representation.getDocument().toString());
        System.out.println(user);

        assertTrue(representation.getDocument().toString().equals(user));

    }

    @Test
    public void testIsObject(){


        House whiteHouse = new House("White House", 12.25f);

        String json = "{name:'White House', price:12.25}";
        //assertTrue(new Gson().fromJson(json, House.class).equals(whiteHouse));

        int i = 3;

        GsonRepresentation representation = new GsonRepresentation(i);
        assertFalse(representation.isObject());



    }
}
