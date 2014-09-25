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


import io.robusta.rra.resource.Resource;
import io.robusta.rra.resource.ResourceSerializer;

import java.util.List;
import java.util.Map;

/**
 * Created by  Nicolas Zozol for Robusta Code
 * @author  Nicolas Zozol
 */
public class House {
    String name;
    float price;
    List<Room> rooms;
    Garden garden;

    //Introducing the dummy constructor
    public House() {
    }

    public House(String name, float price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof House){
            return ((House)obj).name.equals(this.name);
        }else {
            return false;
        }

    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Garden getGarden() {
        return garden;
    }

    public void setGarden(Garden garden) {
        this.garden = garden;
    }

    @Override
    public String toString() {
        return "House{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", rooms=" + rooms +
                ", garden=" + garden +
                '}';
    }
}
