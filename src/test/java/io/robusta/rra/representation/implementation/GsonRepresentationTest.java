package io.robusta.rra.representation.implementation;

import com.google.gson.Gson;
import io.robusta.rra.RepresentationTest;
import io.robusta.rra.files.House;
import org.junit.Test;

import static org.junit.Assert.*;

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
public class GsonRepresentationTest extends JsonRepresentationTest {


    public GsonRepresentationTest(){
        isJson = true;
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
}
