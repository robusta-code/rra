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

package io.robusta.rra.files;

import io.robusta.rra.Resource;
import io.robusta.rra.resource.ResourceSerializer;

import java.util.Map;

/**
 * Created by dev on 02/09/14.
 */
public class Garden implements Resource<Long>{

    Long id;
    String name;
    float surface;
    boolean cloture;
    Object someNullObject = null;
    //TODO : Using Jackson, we should ignore these properties
    String prefix;
    String collectionPrefix;

    public Garden() {
    }

    public Garden(String name, float surface,boolean cloture) {
        this.name = name;
        this.surface = surface;
        this.cloture=cloture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSurface() {
        return surface;
    }

    public void setSurface(float surface) {
        this.surface = surface;
    }

    public boolean isCloture() {
        return cloture;
    }

    public void setCloture(boolean cloture) {
        this.cloture = cloture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Garden garden = (Garden) o;

        if (Float.compare(garden.surface, surface) != 0) return false;
        if (name != null ? !name.equals(garden.name) : garden.name != null) return false;

        return true;
    }



    public Object getSomeNullObject() {
        return someNullObject;
    }

    public void setSomeNullObject(Object someNullObject) {
        this.someNullObject = null;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getPrefix() {
        return "garden";
    }

    @Override
    public String getCollectionPrefix() {
        return "gardens";
    }

    @Override
    public Map<String, Object> serialize() {
        return ResourceSerializer.serialize(this);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (surface != +0.0f ? Float.floatToIntBits(surface) : 0);
        return result;
    }
}
