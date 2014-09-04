package io.robusta.rra.representation.implementation;

import io.robusta.rra.RepresentationTest;
import org.junit.Test;

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
public class XstreamRepresentationTest extends RepresentationTest{

    @Test
    public void testXstreamSet(){

        XstreamRepresentation representation = new XstreamRepresentation(whiteHouse);
        String newName = "The Brand new White House";
        representation.set("name", newName);
        assertTrue (representation.toString().contains(newName));

    }

}
