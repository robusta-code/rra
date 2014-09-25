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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicolas Zozol
 */
public interface ResourceCollection<IdType, T  extends Resource<IdType>> {


    /**
     * @param id id of the searched Resource
     * @return the resource with specified id, or null if not found
     */
    public T getById(IdType id) ;



    /**
     * Return true if one of the Resources has the id
     *
     * @param id id of the searched resource
     * @return true if a Resource is found
     * @throws IllegalArgumentException if id == null
     */
    public boolean containsById(IdType id);


    /**
     * <p>
     * returns something like :
     * <code>
     * <ids>
     *  <id>28</id>
     *  <id>29</id>
     * </ids>
     * </code>
     * </p>
     * @param withXmlHeader add xml headers with UTF-8 encoding if true
     * @return the xml representation of ids
     * @see #getJsonIds()
     */
    public String getXmlIds(boolean withXmlHeader);
    /**
     * <p>
     * returns something like : [28,29]
     *
     * @see #getXmlIds(boolean)
     * @return the xml representation of ids
     */
    public String getJsonIds();

    /**
     * @return a list of all ids
     */
    public List<IdType> getIds();
}
