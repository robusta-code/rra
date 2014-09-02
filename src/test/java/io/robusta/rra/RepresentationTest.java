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

package io.robusta.rra;

import com.google.gson.Gson;
import io.robusta.rra.files.Garden;
import io.robusta.rra.files.House;
import io.robusta.rra.files.Room;
import io.robusta.rra.representation.implementation.GsonRepresentation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by  Nicolas Zozol for Robusta Code
 * @author  Nicolas Zozol
 */
public class RepresentationTest {

    protected static String jsonContent;
    protected static String xml;
    protected boolean isJson;
    protected Representation representation;
    protected static House whiteHouse;

    @BeforeClass
    public static void setUpClassToto() throws Exception {
        String userDir = System.getProperty("user.dir");
        String mavenPath="/src/test/java";
        String packagePath = RepresentationTest.class.getPackage().getName().replaceAll("\\.", "/");
        String filePlace = userDir+mavenPath+"/"+packagePath+"/files/";


        jsonContent = readFile(filePlace + "representation.json");
        xml = readFile(filePlace + "representation.xml");

        whiteHouse = new House("White House", 12.25f);
        Room cuisine=new Room("cuisine",12.3f);
        Room salon=new Room("salon",45f);
        List<Room> rooms=new ArrayList<Room>();
        rooms.add(cuisine);
        rooms.add(salon);
        whiteHouse.setRooms(rooms);
        whiteHouse.setGarden(new Garden("jardin",300,true));

    }
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testToString() throws Exception {

    }

    @Test
    public void testGetWithKey() throws Exception {

    }

    @Test
    public void testGetWithClass() throws Exception {

    }

    @Test
    public void testHas() throws Exception {

    }

    @Test
    public void testHasNotEmpty() throws Exception {

    }

    @Test
    public void testGetMissingKeys() throws Exception {

    }

    @Test
    public void testSet() throws Exception {

        String json = "{name:'White House', price:12.25}";
        GsonRepresentation representation = new GsonRepresentation(json);
        Representation representationNew=representation.set("newChamp", "champ");

        String jsonNew = "{'name':'White House', 'price':12.25, 'newChamp':'champ'}";
        GsonRepresentation representationNew1 = new GsonRepresentation(jsonNew);

        assertTrue(representationNew.toString().equals(representationNew1.toString()));

    }

    @Test
    public void testSetWithClass() throws Exception {

    }

    @Test
    public void testGetValues() throws Exception {

    }

    @Test
    public void testGetValuesWithClass() throws Exception {

    }

    @Test
    public void testAdd() throws Exception {

    }

    @Test
    public void testAddResource() throws Exception {

    }

    @Test
    public void testAddAll() throws Exception {

    }

    @Test
    public void testMerge() throws Exception {

        GsonRepresentation representation = new GsonRepresentation(whiteHouse);
        Representation representationMerge=representation.merge("old","new",representation);

        assertTrue(representationMerge.toString().contains("new"));

    }

    @Test
    public void testRemove() throws Exception {


        GsonRepresentation representation = new GsonRepresentation(whiteHouse);
        Representation representationNew=representation.remove("price");
        assertTrue(!representationNew.toString().contains("price"));

        assertTrue(representationNew.toString().contains("cuisine"));
        assertTrue(representationNew.toString().contains("cloture"));

        representationNew=representation.remove("garden.cloture");
        assertTrue(!representationNew.toString().contains("cloture"));

    }

    @Test
    public void testFetch() throws Exception {

    }

    @Test
    public void testCopy() throws Exception {

    }

    @Test
    public void testGetRepresentation() throws Exception {

    }

    /**
     * Reads a file line after line.
     *
     * @param path Full path of the file ('c:/webapp/data.xml' or '/var/webapp/data.xml')
     * @return The content of the file.
     * @throws java.io.FileNotFoundException
     */
    public static String readFile(String path) throws IOException {

        FileReader reader = null;

        BufferedReader buffReader = null;

        StringBuilder text = new StringBuilder();

        try {
            reader = new FileReader(path);
            buffReader = new BufferedReader(reader);

            String tempLine;
            while ( (tempLine = buffReader.readLine())!=null ) {
                text.append(tempLine).append("\n");
            }
        } finally {
            reader.close();
            buffReader.close();
        }
        return text.toString();
    }
}
