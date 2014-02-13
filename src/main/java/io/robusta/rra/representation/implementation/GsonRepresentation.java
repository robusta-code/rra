package io.robusta.rra.representation.implementation;

import com.google.gson.*;
import io.robusta.rra.Representation;
import io.robusta.rra.Resource;
import io.robusta.rra.representation.JsonRepresentation;
import io.robusta.rra.representation.RepresentationException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

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
public class GsonRepresentation implements JsonRepresentation<JsonElement> {

    Gson gson = new Gson();
    JsonElement document;

    /**
     * In that case, is serialization if not null, it's always a JsonObject
     *
     * @param serialization
     */
    public GsonRepresentation(HashMap<String, Object> serialization) {
        this.document = gson.toJsonTree(serialization);
    }

    public GsonRepresentation(Object object) {
        this.document = gson.toJsonTree(object);
    }

    public GsonRepresentation(String json) {
        this.document = new JsonParser().parse(json);
    }

    public GsonRepresentation() {
        this.document = null;
    }

    @Override
    public JsonElement getDocument() {
        return this.document;
    }

    @Override
    public <T> T get(Class<T> type) throws RepresentationException {
        return get(type, this.document);
    }


    //TODO : rename to map() ?
    protected <T> T get(Class<T> type, JsonElement element) throws RepresentationException {

        if (type == Long.class) {
            return (T) (Long) element.getAsLong();
        } else if (type == Integer.class) {
            return (T) (Integer) element.getAsInt();
        } else if (type == Short.class) {
            return (T) (Short) element.getAsShort();
        } else if (type == Byte.class) {
            return (T) (Byte) element.getAsByte();
        } else if (type == BigInteger.class) {
            return (T) (BigInteger) element.getAsBigInteger();
        } else if (type == Double.class) {
            return (T) (Double) element.getAsDouble();
        } else if (type == Float.class) {
            return (T) (Float) element.getAsFloat();
        } else if (type == BigDecimal.class) {
            return (T) (BigDecimal) element.getAsBigDecimal();
        } else if (type == Boolean.class) {
            return (T) (Boolean) element.getAsBoolean();
        } else if (type == String.class) {
            return (T) element.getAsString();
        } else {
            return (T) gson.fromJson(this.document, type);
        }
    }



    @Override
    public String get(String key) throws RepresentationException {
        return this.get(String.class, key);
    }

    @Override
    public <T> T get(Class<T> type, String key) throws RepresentationException {

        JsonElement element = this.document.getAsJsonObject().get(key);
        return get(type, element);

    }

    protected boolean has(String key) {

        throwIfNotObject(this.document);

        JsonObject object = this.document.getAsJsonObject();
        return object.has(key);

    }

    protected boolean hasNotEmpty(String key) {
        throwIfNotObject(this.document);
        JsonObject object = this.document.getAsJsonObject();
        JsonElement elt = object.get(key);
        if (elt.isJsonNull() || (isString(elt) && elt.getAsString().isEmpty())) {
            //elt is null or empty string
            return false;
        } else {
            return true;
        }

    }

    List<String> missingKeys = new ArrayList<String>(5);

    @Override
    public boolean hasPossiblyEmpty(String... keys) {
        boolean result = true;
        for (String key : keys) {
            if (!has(key)) {
                missingKeys.add(key);
                result = false;
            }
        }
        return result;
    }


    @Override
    public boolean has(String... keys) {
        boolean result = true;
        for (String key : keys) {
            if (!hasNotEmpty(key)) {
                missingKeys.add(key);
                result = false;
            }
        }
        return result;
    }

    @Override
    public List<String> getMissingKeys() {
        return missingKeys;
    }

    protected void createObjectIfEmtpy(){
        if (this.document == null){
            this.document = new JsonObject();
        }
    }

    protected void throwIfNotObject(JsonElement elt) throws RepresentationException {
        if (!elt.isJsonObject()) {
            throw new RepresentationException("The current element is not a JSON object but a " + this.getTypeof() + " and thus has no key");
        }
    }

    protected void throwIfNotObject() throws RepresentationException {
        throwIfNotObject(this.document);
    }

    protected void throwIfNotArray(JsonElement elt) throws RepresentationException {
        if (!elt.isJsonArray()) {
            throw new RepresentationException("The current element is not a JSON array but a " + this.getTypeof() + " and it can't add an object the correct way");
        }
    }

    protected void throwIfNotArray() throws RepresentationException {
        throwIfNotArray(this.document);
    }


    protected JsonObject asObject(){
        throwIfNotObject();
        return this.document.getAsJsonObject();
    }

    protected JsonArray asArray(){
        throwIfNotArray();
        return this.document.getAsJsonArray();
    }

    @Override
    public Representation set(String key, String value) {
        createObjectIfEmtpy();
        asObject().add(key, new JsonPrimitive(value));
        return this;
    }

    @Override
    public Representation set(String key, Object value) {
        createObjectIfEmtpy();
        asObject().add(key, gson.toJsonTree(value));
        return this;
    }

    @Override
    public List<String> getValues(String key) throws RepresentationException {
        List<String> list = new ArrayList<String>();
        for (JsonElement elt : asObject().get(key).getAsJsonArray()) {
            list.add(elt.toString());
        }
        return list;
    }

    //TODO : Not tested. If it works here, that's great !!
    @Override
    public <T> List<T> getValues(Class<T> type, String key) throws RepresentationException {
        List<T> list = new ArrayList<T>();
        for (JsonElement elt : asObject().get(key).getAsJsonArray()) {
            list.add(get(type, elt));
        }
        return list;
    }

    @Override
    public Representation add(String key, Object value) {
        throwIfNotObject();
        throwIfNotArray(this.asObject().get(key));
        asObject().get(key).getAsJsonArray().add(gson.toJsonTree(value));
        return this;
    }

    @Override
    public Representation add(String key, Resource resource, boolean eager) {
        throwIfNotObject();
        throwIfNotArray(this.asObject().get(key));
        //TODO : to use eager, we must look at ReflectiveTypeAdapterFactory around line 82
        JsonElement element = gson.toJsonTree(resource);
        asObject().get(key).getAsJsonArray().add(element);
        return this;
    }


    @Override
    public Representation addAll(String key, List values) {
        throwIfNotObject();
        throwIfNotArray(this.asObject().get(key));
        JsonArray array = this.asObject().get(key).getAsJsonArray();
        for (Object value : values){
            array.add(gson.toJsonTree(value));
        }

        return this;
    }

    @Override
    public Representation merge (String keyForCurrent, String keyForNew, Representation representation){
        if (! (representation instanceof GsonRepresentation)){
            throw new IllegalArgumentException("Can't merge a GsonRepresentation with a "+representation.getClass().getSimpleName());
        }
        GsonRepresentation mergedRepresentation = new GsonRepresentation();
        mergedRepresentation.createObject();
        JsonObject object = mergedRepresentation.document.getAsJsonObject();
        object.add(keyForCurrent, this.document);
        object.add(keyForNew, ((GsonRepresentation) representation).document);
        return mergedRepresentation;
    }

    @Override
    public Representation remove(String key) throws RepresentationException {
        asObject().remove(key);
        return this;
    }

    @Override
    public Representation fetch(String key) {
        throwIfNotObject();
        JsonElement root;

        if (key.contains(".")){
            String[] keys = key.split(".");
            if (keys.length == 0){
                throw new IllegalArgumentException("Malformed key "+keys+" ; use something like user.school.id");
            }
            String lastKey=keys[0];
            JsonElement current = asObject();
            for (String newKey : keys){
                if (! current.isJsonObject()){
                    throw new IllegalArgumentException("The key "+lastKey+ " in '"+key+"' doesn't point to a Json Object");
                };
                current = current.getAsJsonObject().get(newKey);
                if (current == null){
                    throw new IllegalArgumentException("There is no valid object for key "+newKey+ "in '"+key+"'");
                }
            }
            root = current;
        }else{
            root = asObject().get(key);
        }

        if (root == null){
            throw new IllegalArgumentException("There is no valid object for key "+key);
        }else{
            GsonRepresentation fetchedRepresentation = new GsonRepresentation();
            fetchedRepresentation.document = root;
            return fetchedRepresentation;
        }
    }




    @Override
    public Representation copy() {
        String serialization = gson.toJson(this.document);
        JsonElement clone = new JsonParser().parse(serialization);
        GsonRepresentation representation = new GsonRepresentation();
        representation.document = clone;
        return representation;
    }

    @Override
    public Representation createNewRepresentation(Object newObject) {
        return new GsonRepresentation(newObject);
    }

    @Override
    public Representation createNewRepresentation() {
        return new GsonRepresentation();
    }


    @Override
    public Representation addToArray(Resource resource, boolean eager) {
        throwIfNotArray();
        //TODO : to use eager, we must look at ReflectiveTypeAdapterFactory around line 82
        JsonElement element = gson.toJsonTree(resource);
        asArray().add(element);
        return this;
    }

    @Override
    public Representation addToArray(Object value) {
        throwIfNotArray();
        JsonElement element = gson.toJsonTree(value);
        asArray().add(element);
        return this;
    }

    @Override
    public <T> List<T> pluck(Class<T> type, String key) throws RepresentationException {
        throwIfNotArray();
        List<T> result = new ArrayList<T>();
        for (JsonElement elt : asArray()){
                result.add(get(type, elt));
        }
        return result;
    }

    @Override
    public boolean isPrimitive() {
        return this.document.isJsonPrimitive();
    }

    @Override
    public boolean isObject() {
        return this.document.isJsonObject();
    }

    @Override
    public boolean isBoolean() {
        //checking that it' a primitive
        if (this.document.isJsonPrimitive()) {
            String json = this.document.toString();
            return json.equalsIgnoreCase("true") || json.equalsIgnoreCase("false");
        } else {
            return false;
        }
    }

    @Override
    public boolean isString() {
        return isString(this.document);
    }

    private boolean isString(JsonElement elt) {
        return elt.isJsonPrimitive() && (
                elt.toString().trim().startsWith("\"") || elt.toString().trim().startsWith("'"));
    }

    @Override
    public boolean isNumber() {
        return this.document.isJsonPrimitive() && !this.isBoolean() && !this.isString();
    }

    @Override
    public boolean isArray() {
        return this.document.isJsonArray();
    }

    @Override
    public boolean isNull() {
        return this.document.isJsonNull();
    }


    @Override
    public JsonType getTypeof() {
        if (this.isString()){
            return JsonType.STRING;
        }else if (this.isArray()){
            return JsonType.ARRAY;
        }else if (this.isBoolean()){
            return JsonType.BOOLEAN;
        }else if (this.isNumber()){
            return JsonType.NUMBER;
        }else if (this.isNull()){
            return JsonType.NULL;
        }else if (this.isObject()){
            return JsonType.OBJECT;
        }else throw new IllegalStateException("Can't find the type of this document");
    }

    @Override
    public Representation<JsonElement> createObject() {
        if (this.document != null){
            throw new IllegalStateException("This representation is not Empty. Use createNewRepresentation() to get a new empty representation");
        }
        this.document=new JsonObject();
        return this;
    }

    @Override
    public Representation<JsonElement> createArray() {
        if (this.document != null){
            throw new IllegalStateException("This representation is not Empty. Use createNewRepresentation() to get a new empty representation");
        }
        this.document=new JsonArray();
        return this;
    }

    protected JsonType getJsonType(Class type) {
        if (type == Integer.class || type == Long.class || type == Integer.class || type == Integer.class) {
            return JsonType.NUMBER;
        } else if (type == String.class) {

        }
        return null;
    }

}
