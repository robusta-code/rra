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

import io.robusta.rra.Resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Add features to a Resource Collection without creating a new Collection
 * @author Nicolas Zozol
 */
public class ResourceCollectionWrapper<IdType, R extends Resource<IdType>> implements ResourceCollection<IdType, R> {

    Collection<R> collection;

    public ResourceCollectionWrapper(Collection<R> collection) {
        this.collection = collection;
    }

    @Override
    public R getById(IdType id) {
        if (id == null){
            throw new IllegalArgumentException("null id");
        }
        for (R r : collection ){
            if (r.getId().equals(id)){
                return r;
            }
        }
        return null;
    }



    @Override
    public boolean containsById(IdType id) {
        if (id == null){
            throw new IllegalArgumentException("null id");
        }
        for (R r : collection ){
            if (r.getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String getXmlIds(boolean withXmlHeader) {
        final String headers = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        StringBuilder result = new StringBuilder();

        if (withXmlHeader){
            result.append(headers);
        }
        result.append("<ids>\n") ;

        for (R resource : this.collection) {
            result.append("<id>");
            if (resource==null){
                result.append(String.valueOf(null));
            }else{
                result.append(String.valueOf(resource.getId()));
            }
            result.append("</id>\n");
        }

        result.append("\n</ids>");
        return result.toString();
    }


    @Override
    public String getJsonIds() {
        StringBuilder result = new StringBuilder();


        result.append('[') ;
        result.append(joinIds(","));
        result.append(']');
        return result.toString();
    }

    @Override
    public List<IdType> getIds() {
        ArrayList<IdType> ids = new ArrayList<IdType>();
        for (Resource<IdType> r : this.collection) {
            ids.add(r.getId());
        }
        return ids;
    }

    StringBuilder joinIds (String glue){
        if (glue == null){
            throw new IllegalArgumentException("Glue is null");
        }
        StringBuilder result = new StringBuilder();

        for (R resource : this.collection) {

            if (resource==null){
                result.append(String.valueOf(null));
            }else{
                result.append(String.valueOf(resource.getId()));
            }
            result.append(glue);
        }
        result.setLength(result.length() - glue.length());
        return result;
    }
}
