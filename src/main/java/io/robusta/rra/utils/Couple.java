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

package io.robusta.rra.utils;

/**
 * <p>
 * This class represents a couple of object. It is used in {@link CoupleList}
 * and allows generics.
 * </p>
 * 
 * @author Nicolas Zozol
 * @see CoupleList
 * @todo 2 : change some method names Created by Nicolas Zozol for Robusta Code
 */
public class Couple<LEFT, RIGHT> {

    LEFT  left;
    RIGHT right;

    /**
     * Create a Couple of two objects
     * 
     * @param left
     * @param right
     */
    public Couple( LEFT left, RIGHT right ) {
        this.left = left;
        this.right = right;
    }

    /**
     *
     * @return the LEFT element
     */
    public LEFT getLeft() {
        return this.left;
    }

    /**
     *
     * @return the RIGHT element
     */
    public RIGHT getRight() {
        return this.right;
    }

    /**
     * Replaces the LEFT element by the left param
     * 
     * @param left
     *            new Left element
     */
    public void setLeft( LEFT left ) {
        this.left = left;
    }

    /**
     * Replaces the Right element by the right param
     * 
     * @param right
     *            new Right element
     */
    public void setRight( RIGHT right ) {
        this.right = right;
    }

    @Override
    /**
     * Returns a '(left.toString(), right.toString())' representation of the Couple
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        String strLeft = "" + left;
        String strRight = "" + right;

        return sb.append( '(' ).append( strLeft ).append( ", " ).append( strRight ).append( ')' ).toString();

    }

    /**
     * <p>
     * Creates a new Couple&lt;RIGHT, LEFT> where Left goes to Right, and Right
     * goes to Left.
     * </p>
     *
     */
    public Couple<RIGHT, LEFT> reverse() {

        Couple<RIGHT, LEFT> c = new Couple<RIGHT, LEFT>( right, left );
        return c;

    }

    @Override
    /**
     * <p>Two couples are equals if their left elements are equals and their right elements are equals. One of the couple might use generic, and the other not
     * without affecting the result of this function.
     * </p>
     */
    public boolean equals( Object obj ) {
        if ( obj instanceof Couple ) {
            Couple c = (Couple) obj;
            return ( c.getLeft().equals( left ) && c.getRight().equals( right ) );
        }
        return super.equals( obj );
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + ( this.left != null ? this.left.hashCode() : 0 );
        hash = 61 * hash + ( this.right != null ? this.right.hashCode() : 0 );
        return hash;
    }

    /**
     * Returns an array containing left and right
     * 
     * @return
     * @throws ClassCastException
     *             if T Type is not appropriate
     */
    public <T> T[] flat() throws ClassCastException {
        return (T[]) new Object[] { getLeft(), getRight() };
    }

    /**
     * Returns a couple with the string values of each object
     * 
     * @return
     */
    public Couple<String, String> stringify() {
        return new Couple<String, String>( getLeft().toString(), getRight().toString() );
    }

}
