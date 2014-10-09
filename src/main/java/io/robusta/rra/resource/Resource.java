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

import java.util.Map;

/**
 * Created by Nicolas Zozol for Robusta Code
 * 
 * @author Nicolas Zozol
 */
public interface Resource<IdType> {

    /**
     * Returns the id of the Resource.
     * <p>
     * In REST, a Resource might have an id as a Number, UUID String,
     * CompositeKey or even an url
     * </p>
     * 
     * @return the Resource id
     */
    public IdType getId();

    /**
     * Name of the prefix used to represent a resource. For exemple, if the
     * getPrefix() returns "house" and getCollectionPrefix() returns "houses"
     * for a resource House, a ResourceList<House>.getRepresentation() will
     * return : <houses> <house><id>1</id><name>House 1</name></house>
     * <house><id>2</id><name>Small House</name></house>
     * <house><id>3</id><name>Big House</name></house> </houses> Or :
     * {houses:[{id:"1", name:"House 1"}, {id:"2", name:"Small House"}, {id:"3",
     * name:"Big House"}]}
     * 
     * @return resource prefix
     */
    public String getPrefix();

    /**
     * Name of the prefix used to represent a list of this resource. For
     * exemple, if the function returns "houses" for a resource House, a
     * ResourceList<House>.getRepresentation() will return : <houses>
     * <house><id>1</id><name>House 1</name></house>
     * <house><id>2</id><name>Small House</name></house>
     * <house><id>3</id><name>Big House</name></house> </houses> Or :
     * {houses:[{id:"1", name:"House 1"}, {id:"2", name:"Small House"}, {id:"3",
     * name:"Big House"}]}
     * 
     * @return resource list prefix
     */
    public String getCollectionPrefix();

    /**
     * Serialize this Resource. You can do it manually or use a Mapper
     * 
     * @return all name/values of this object
     */
    public Map<String, Object> serialize();

}
