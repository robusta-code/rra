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


import io.robusta.rra.files.Garden;
import io.robusta.rra.files.House;
import io.robusta.rra.files.Room;
import io.robusta.rra.representation.Representation;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Created by  Nicolas Zozol for Robusta Code
 * @author  Nicolas Zozol
 */
public abstract class RepresentationTest<T extends Representation>  {

    protected static String jsonContent;
    protected static String xml;
    protected boolean isJson;
    protected T schoolRepresentation;
    protected T emptyRepresentation;
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
        Representation whiteHouseRepresentation = createNewRepresentation(whiteHouse);
        assertTrue(whiteHouseRepresentation.toString().contains("White House"));
    }

    @Test
    public void testGetWithKey() throws Exception {
        Representation whiteHouseRepresentation = createNewRepresentation(whiteHouse);
        assertTrue(whiteHouseRepresentation.get("name").contains("White"));
    }

    @Test
    public void testGetWithClass() throws Exception {
        Representation whiteHouseRepresentation = createNewRepresentation(whiteHouse);
        assertTrue(whiteHouseRepresentation.get(House.class).equals(whiteHouse));
    }

    @Test
    public void testHas() throws Exception {
        Representation whiteHouseRepresentation = createNewRepresentation(whiteHouse);
        assertTrue(whiteHouseRepresentation.has("garden","rooms") == true);
    }


    @Test
    public void testGetMissingKeys() throws Exception {
        Representation whiteHouseRepresentation = createNewRepresentation(whiteHouse);
        whiteHouseRepresentation.set("test","");
        whiteHouseRepresentation.has("garden","test");

        assertTrue(whiteHouseRepresentation.getMissingKeys().contains("test"));

    }

    @Test
    public void testSet() throws Exception {

        String fieldValue = "some Value";
        Representation representation = createNewRepresentation(whiteHouse).set("newField", fieldValue);
        assertTrue(representation.get("newField").equals(fieldValue));

    }

    @Test
    public void testSetWithClass() throws Exception {
        Room bedroom=new Room("bedroom",12.3f);
        Room bathroom=new Room("bathroom",45f);
        List<Room> rooms=new ArrayList<Room>();
        rooms.add(bedroom);
        rooms.add(bathroom);
        Representation representation = createNewRepresentation(whiteHouse).set("rooms", rooms);

        //System.out.println(representation.toString());
        assertTrue(representation.fetch("rooms").toString().contains("bedroom"));
    }

    @Test
    public void testGetValues() throws Exception {
        Representation whiteHouseRepresentation = createNewRepresentation(whiteHouse);
//        System.out.println(whiteHouseRepresentation.getClass() + " - " +whiteHouseRepresentation.getValues("rooms"));
        assertTrue(whiteHouseRepresentation.getValues("rooms").toString().contains("cuisine"));

    }

    @Test
    public void testGetValuesWithClass() throws Exception {
        Representation whiteHouseRepresentation = createNewRepresentation(whiteHouse);
        //System.out.println(whiteHouseRepresentation.getClass() + " - " +whiteHouseRepresentation.getValues(Room.class,"rooms"));
        assertTrue(whiteHouseRepresentation.getValues(Room.class,"rooms").toString().contains("cuisine"));

    }

    @Test
    public void testAdd() throws Exception {
        Representation whiteHouseRepresentation = createNewRepresentation(whiteHouse);
        List<String> trees= new ArrayList<String>();
        trees.add("tree1");
        trees.add("tree2");
        whiteHouseRepresentation.set("trees", trees);
        whiteHouseRepresentation.add("trees","tree3");
        //System.out.println(whiteHouseRepresentation.getClass() + " - " +whiteHouseRepresentation.toString());
        assertTrue(whiteHouseRepresentation.toString().contains("tree3"));
    }

    @Test
    public void testAddResource() throws Exception {

    }

    @Test
    public void testAddAll() throws Exception {
        Representation whiteHouseRepresentation = createNewRepresentation(whiteHouse);
        List<String> trees= new ArrayList<String>();
        trees.add("tree1");
        trees.add("tree2");
        whiteHouseRepresentation.set("trees", trees);

        List<String> newTrees= new ArrayList<String>();
        newTrees.add("tree3");
        newTrees.add("tree4");
        whiteHouseRepresentation.addAll("trees",newTrees);
        //System.out.println(whiteHouseRepresentation.getClass() + " - " +whiteHouseRepresentation.toString());
        assertTrue(whiteHouseRepresentation.toString().contains("tree4"));
    }

    @Test
    public void testMerge() throws Exception {

        Representation whiteHouseRepresentation = createNewRepresentation(whiteHouse);
        Representation representationMerge=schoolRepresentation.merge("school", "house", whiteHouseRepresentation);

        assertTrue(representationMerge.fetch("school").toString().contains("students"));
        assertTrue(representationMerge.fetch("house").toString().contains("White House"));

    }

    @Test
    public void testRemove() throws Exception {


        Representation representation = createNewRepresentation(whiteHouse);
        Representation representationNew=representation.remove("price");
        assertFalse(representationNew.toString().contains("price"));

        assertTrue(representationNew.toString().contains("cuisine"));
        assertTrue(representationNew.toString().contains("cloture"));

        representationNew=representation.remove("garden.cloture");
        assertTrue(!representationNew.toString().contains("cloture"));

    }

    @Test
    public void testFetch() throws Exception {
        Representation whiteHouseRepresentation = createNewRepresentation(whiteHouse);
        assertTrue(whiteHouseRepresentation.fetch("rooms").toString().contains("cuisine"));
    }

    @Test
    public void testCopy() throws Exception {
        Representation whiteHouseRepresentation = createNewRepresentation(whiteHouse);

        //System.out.println(whiteHouseRepresentation.getClass() + " - " +whiteHouseRepresentation.copy().toString());
        assertTrue(whiteHouseRepresentation.copy().toString().equals(whiteHouseRepresentation.toString()));
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

    public  static String readTestFile(String file) throws IOException {
        String userDir = System.getProperty("user.dir");
        String mavenPath="/src/test/java";
        String packagePath = RepresentationTest.class.getPackage().getName().replaceAll("\\.", "/");
        String filePlace = userDir+mavenPath+"/"+packagePath+"/files/";


        return readFile(filePlace + file);

    }

    public  static String readJson(){
        try {
            return readTestFile("representation.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        fail("Can't read representation.json file");
        return null;
    }

    public static  String readXml(){
        try {
            return readTestFile("representation.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        fail("Can't read representation.xml file");
        return null;
    }

    public  Representation createNewRepresentation(Object o){
        return this.emptyRepresentation.createNewRepresentation(o);
    }

    public  Representation createNewRepresentation(String s){
        return this.emptyRepresentation.createNewRepresentation(s);
    }

    public  Representation createNewRepresentation(InputStream inputStream){
        return this.emptyRepresentation.createNewRepresentation(inputStream);
    }

    public  Representation createNewRepresentation(){
        return this.emptyRepresentation.createNewRepresentation();
    }
}
