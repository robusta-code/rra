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

import io.robusta.rra.Representation;


 /**
 *
 * <p>This interface allows a Resource to choose a Representation with a Strategy (http://en.wikipedia.org/wiki/Strategy_pattern).
 * You can bypass complexity by ignoring Strategies and just implementing #getRepresentation()</p>
 *
 * <p>Most of the time, #getRepresentation() will be this snippet : </p>
 * <code>
 *     public Representation getRepresentation(){
 *         if (this.strategy == null){
 *             // choose a default Representation or throw exception
 *             return new StaxRepresentation(this);
 *         }else{
 *             return this.strategy.getRepresentation();
 *         }
 *     }
 *
 * </code>
 *
 * @author Nicolas Zozol
 */
public interface HasRepresentation {

    void setRepresentationStrategy(RepresentationStrategy strategy);
    RepresentationStrategy getRepresentationStrategy();
    Representation getRepresentation();

}
