package io.robusta.rra;

import io.robusta.rra.representation.RepresentationException;

import java.util.List;

/**
 * Created by  Nicolas Zozol for Robusta Code
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public interface Representation {

    /**
     * Returns the representation usually written in http tubes
     * @return
     */
    @Override
    public String toString();

    /**
     * Returns a String value, or throw a RepresentationException if it's not found.
     * <p>Use #getValues() to return a collection</p>
     * @param key name of the searched node
     * @return the node value
     * @throws RepresentationException if the key is not found
     * @see #get(Class, String)
     */
    public String get(String key) throws RepresentationException;

    /**
     * Returns a  value, or throw a RepresentationException if it's not found.
     * <p>
     * Usually, the method will return a String or a Number, but
     * if this Representation implements also Mapper, then get() could return a complex object.
     * </p>
     * <p>Use #getValues() to return a collection</p>
     * @param T type of the returned object
     * @param key name of the searched node
     * @return the node value
     * @throws RepresentationException if the key is not found
     */
    public <T> T get(Class T, String key) throws RepresentationException;




    /**
     * Returns true if the Representation has the keys as <strong>direct</strong> child
     * @param keys searched keys
     * @return true if the Representation has all keys, false if not
     */
    public boolean has(String... keys);

    /**
     * Returns true if the Representation has not empty values for the specified keys
     * <p>Useful to validate a representation sent by a client</p>
     * @param name
     * @return
     */
    public boolean hasNotEmpty(String... name);

    /**
     * Returns the the missing keys when #has() and #hasNotEmpty() was called
     * @return the missing keys
     * @throws IllegalStateException if #has() or#hasNotEmpty() has not been called
     */
    public List<String> getMissingKeys();

    /**
     * Update a node with its value, or create a new node
     * @param key name of the node
     * @param value value set
     */
    public Representation set(String key, String value);

    public Representation set(String key, Object value);


    /**
     * Return String values whey the send points to a collection
     * @param key key of the collection
     * @throws IllegalArgumentException when key is not found or the value is not a collection
     */
    public List<String> getValues(String key) throws RepresentationException;

    /**
     * Return values whey the send points to a collection
     * @param key key of the collection
     * @throws IllegalArgumentException when key is not found or the value is not a collection
     */
    public <T> List<T> getValues(Class T, String key) throws RepresentationException;


    /**
     * Add a new element, even if one of the same key exists or a value to a Json array.
     * @param key
     * @param nodeValue
     * @return the updated representation
     */
    public Representation add(String key, String nodeValue);

    /**
     * Add a new element ( even if one of the same key exists) to the representation. It pushes a value if the current Json Element
     * is a Json array.
     * If eager is used, then resource.getRepresentation() must be 'compatible' with this Representation. The best compatibility is of course
     * the same class, but this may depend on the implementation.
     * @param resource
     * @param eager : if false, Resource dependencies will show only their ids
     * @return the updated representation
     */
    public Representation add(Resource resource, boolean eager);


    /**
     * Add a list of objects to the Representation.
     * If list is empty, an empty node <strong>is</strong> created.
     * @param listName
     * @param values
     * @return the updated representation
     */
    public Representation addAll(String listName, List values);


    /**
     * Build a new Representation containing this representation along with the representation parameter
     * @param representation Representation to merge
     * @return a new Representation
     */
    public Representation merge (Representation representation);



    /**
     * Removes the <strong>first</strong>  element found with this key
     * @param key
     * @return the new representation
     * @throws RepresentationException if the struncture is irrelevant for the removal
     */
    public Representation remove(String key) throws RepresentationException;


    /**
     * Find the element and retursn it detached
     * @param key
     * @return
     */
    public Representation fetch(String key);

    /**
     * Returns a deep copy. Each descending elements of the new structure have different memory adresses.
     * @return a new Representation
     */
    public Representation copy();


    /**
     * Returns a new Representation object of newObject. This current Representation object is NOT affected.
     * @return  a Representation of newObject
     */
    public Representation getRepresentation(Object newObject);



}
