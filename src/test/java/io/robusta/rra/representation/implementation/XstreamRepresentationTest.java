package io.robusta.rra.representation.implementation;

import io.robusta.rra.Representation;
import io.robusta.rra.RepresentationTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
public class XstreamRepresentationTest extends JsonRepresentationTest<XstreamRepresentation> {

    private String s = "I am here for the test";

    @Test
    public void testXstreamSet() {

        XstreamRepresentation representation = new XstreamRepresentation(whiteHouse);
        representation.remove("name");

        /*String newName = "The Brand new White House";
        representation.set("name", newName);
        assertTrue(representation.toString().contains(newName));
        System.out.println(representation.toString()); */

    }

    @Test
    public void testSet() throws Exception {

        String fieldValue = "some Value";
        //Representation representation = createNewRepresentation(whiteHouse).set("newField", fieldValue);
        //TODO

    }

    @Test
    public void testMerge() throws Exception {

        //TODO

    }

    @Test
    public void testRemove() throws Exception {


        //TODO

    }

    @Test
    public void testAdd() throws Exception {

    }

    @Override
    @Test
    public void testHas() throws Exception {
        //TODO
    }

    @Override
    @Test
    public void testCopy() throws Exception {
        //TODO
    }

    @Override
    @Test
    public void testGetValuesWithClass() throws Exception {
        //TODO
    }

    @Override
    @Test
    public void testGetMissingKeys() throws Exception {
        //TODO
    }

    @Override
    @Test
    public void testSetWithClass() throws Exception {
        //TODO
    }

    @Override
    @Test
    public void testFetch() throws Exception {
        //TODO
    }

    @Override
    @Test
    public void testToString() throws Exception {
        //TODO
    }

    @Override
    @Test
    public void testAddAll() throws Exception {
        //TODO
    }

    @Override
    @Test
    public void testGetValues() throws Exception {
        //TODO
    }

    @Override
    @Test
    public void testGetWithKey() throws Exception {
        //TODO
    }

    @Override
    @Test
    public void testGetWithClass() throws Exception {
        //TODO
    }

    @Override
    @Test
    public void testSetJson() throws Exception {
        //TODO
    }

    @Test
    public void testXstreamRemove() {

        XstreamRepresentation representation = new XstreamRepresentation(whiteHouse);
        //representation.remove("garden");
        representation.remove("name");
        //assertFalse(representation.toString().contains("garden"));
        //assertFalse(representation.toString().contains("name"));
        //System.out.println(representation.toString());
    }

    @Test
    public void testIsNull() {
        XstreamRepresentation representation = new XstreamRepresentation(null);
        assertTrue(representation.isNull());
    }

    @Test
    public void testIsBoolean() {
        Boolean b = false;
        //XstreamRepresentation representation = new XstreamRepresentation(whiteHouse);
        XstreamRepresentation representation = new XstreamRepresentation(b);
        assertTrue(representation.isBoolean());
    }

    @Test
    public void testIsString() {
        //XstreamRepresentation representation = new XstreamRepresentation(whiteHouse);
        XstreamRepresentation representation = new XstreamRepresentation(s);
        assertTrue(representation.isString());
    }

    @Test
    public void testIsNumber() {
        Long l = 15L;
        int i = 12;
        XstreamRepresentation representation = new XstreamRepresentation(l);
        assertTrue(representation.isNumber());
        representation = new XstreamRepresentation(i);
        assertTrue(representation.isNumber());
    }

    @Test
    public void testIsPrimitive() {
        XstreamRepresentation representation = new XstreamRepresentation(s);
        assertTrue(representation.isPrimitive());
    }

    @Test
    public void testIsArray() {
        Integer[] myTab = {1, 2};
        List<Integer> myList = new ArrayList<Integer>();

        myList.add(1);
        myList.add(2);
        myList.add(3);
        myList.add(4);

        XstreamRepresentation representation = new XstreamRepresentation(myList);
        assertTrue(representation.isArray());

        representation = new XstreamRepresentation(myTab);
        assertTrue(representation.isArray());
    }

    @Test
    public void testIsObject() {

        XstreamRepresentation representation = new XstreamRepresentation(whiteHouse);
        assertTrue(representation.isObject());
    }

    @Test
    public void testGetDocument() {
        XstreamRepresentation representation = new XstreamRepresentation(whiteHouse);
        System.out.println(representation.getDocument());
    }
}