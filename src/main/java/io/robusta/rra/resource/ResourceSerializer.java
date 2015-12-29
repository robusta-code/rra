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

package io.robusta.rra.resource;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Workaround support for simple Reflection using Java Reflection API
 *
 * @author Nicolas Zozol
 */
public class ResourceSerializer {
    /**
     * Any IllegalAccessException will be written in the Couple right value
     *
     * @param object
     * @return
     */
    public static HashMap<String, Object> serialize(Object object) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        //Checking first all superclass of the object
        Class current = object.getClass();
        while (current != null) {
            try {
                map.putAll(getClassItems(current, object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Illegal Access to object fields using Java Reflection.");
            }
            current = current.getSuperclass();
        }
        return map;
    }

    public static HashMap<String, Object> simplify(HashMap<String, Object> map) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        Set<String> keys = map.keySet();
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static Object getSimpleValue(Object item) {
        if (item instanceof Boolean || item instanceof Number || item instanceof String || item == null || item instanceof Character) {
            return item;
        } else if (item instanceof CharSequence) {
            return item.toString();
        } else if (item instanceof Resource) {
            return ((Resource) item).getId();
        } else {
            return item.toString();
        }
    }

    /**
     * Any error will be written in the couples
     *
     * @param c
     * @param object
     * @return
     */
    private static HashMap<String, Object> getClassItems(Class c, Object object) throws IllegalAccessException {
        HashMap<String, Object> map = new HashMap<String, Object>();

        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            Object value = f.get(object);
            //TODO : we may use different strategy : eager for all, eager for Resource, just lazy (objects -> toString)
            //TODO could be here value.toString() for immediate use, depending on params
            map.put(f.getName(), value);

        }

        return map;
    }

    /**
     * @param prefix
     * @param obj
     * @return
     */
    public String toXml(String prefix, Object obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Problem of Json is that we need to distinguish
     *
     * @return
     */
    public static String toJson() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
