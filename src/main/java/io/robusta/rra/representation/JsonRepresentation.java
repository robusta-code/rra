package io.robusta.rra.representation;

import io.robusta.rra.Representation;

import java.util.List;

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
public interface JsonRepresentation extends Representation {
    enum JsonType {
        OBJECT, BOOLEAN, STRING, NUMBER, ARRAY, NULL, UNDEFINED;
    }


    /**
     * Pluck extracting a list of property values.
     * <p>
     * From Underscore.js documentation :<br/>
     * var stooges = [{name : 'moe', age : 40}, {name : 'larry', age : 50}, {name : 'curly', age : 60}];<br/>
     * _.pluck(stooges, 'name');<br/>
     * => ["moe", "larry", "curly"]<br/>
     * </p>
     *
     * @param T   type of returned elements
     * @param key
     * @throws RepresentationException if the current Representation is not a JsonArray
     */
    public <T> List<T> pluck(Class T, String key) throws RepresentationException;


    public boolean isPrimitive();

    public boolean isObject();

    public boolean isBoolean();

    public boolean isString();

    public boolean isNumber();

    public boolean isArray();

    public boolean isNull();

    public JsonType getTypeof();

}
