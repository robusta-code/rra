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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * <p>
 * It's just an ArrayList of {@link Couple} objects, somewhat more simple to use
 * than regular lists of Object[2] and allowing generics : <br/>
 * [(3, "Three"),(4, "44"),( 5, "cinquo"), (6, "6")] is a CoupleList&lt;Integer,
 * String>
 * </p>
 * <p>
 * Using Hash or Couples is very convenient in Ajax world. For exemple if you
 * want all the students of your school, you will need id (for further calls)
 * and ScreenName for display .
 * </p>
 * <p>
 * This class is designed to be more simple than fast, but anyway with correct
 * speed.
 * </p>
 * 
 * @author Nicolas Zozol
 * @todo 2 : change some method names
 * @see Couple * Created by Nicolas Zozol for Robusta Code
 */
public class CoupleList<LEFT, RIGHT> extends ArrayList<Couple<LEFT, RIGHT>> {

    public CoupleList() {

    }

    public CoupleList( HashMap<LEFT, RIGHT> map ) {
        Set<LEFT> entries = map.keySet();
        for ( LEFT entry : entries ) {
            this.addCouple( entry, map.get( entry ) );
        }
    }

    @Override
    public Couple<LEFT, RIGHT> get( int index ) {
        return super.get( index );
    }

    /**
     * Adds a couple to your list
     *
     * @param e1
     *            LEFT element
     * @param e2
     *            RIGHT element
     */
    public void addCouple( LEFT e1, RIGHT e2 ) {
        this.add( new Couple<LEFT, RIGHT>( e1, e2 ) );

    }

    /**
     * returns the LEFT element at the specified index
     * 
     * @param index
     * @return the LEFT element at the specified index
     */
    public LEFT getLeftElement( int index ) {

        Couple<LEFT, RIGHT> couple = this.get( index );
        return couple.getLeft();
    }

    /**
     * returns the RIGHT element at the specified index
     * 
     * @param index
     * @return the RIGHT element at the specified index
     */
    public RIGHT getRightElement( int index ) {

        Couple<LEFT, RIGHT> couple = this.get( index );
        return couple.getRight();
    }

    /**
     * Returns an ArrayList&lt;LEFT> of LEFT elements
     * 
     * @return an ArrayList&lt;LEFT> of LEFT elements
     */
    public ArrayList<LEFT> getAllLeftElements() {
        ArrayList<LEFT> result = new ArrayList<LEFT>();
        for ( int i = 0; i < this.size(); i++ ) {
            result.add( this.getLeftElement( i ) );
        }
        return result;
    }

    /**
     * Returns an ArrayList&lt;RIGHT> of RIGHT elements
     * 
     * @return an ArrayList&lt;RIGHT> of RIGHT elements
     */
    public ArrayList<RIGHT> getAllRightElements() {
        ArrayList<RIGHT> result = new ArrayList<RIGHT>();
        for ( int i = 0; i < this.size(); i++ ) {
            result.add( this.getRightElement( i ) );
        }
        return result;
    }

    /**
     * <p>
     * Returns all RIGHT elements equals to Object o when o is a LEFT element,
     * and viceversa o can be sometimes on the left, sometime on the right.
     * </p>
     * <p>
     * For exemple <code>findMyLovers(3)</code> in [(3,"Three"),("Not Three",
     * 3),(4,5),(9,10)] will return ["Three", "Not Three"]
     * </p>
     * 
     * @param o
     *            The object to test
     * @return a list of matching objects
     */
    public ArrayList findMyLovers( Object o ) {

        ArrayList result = new ArrayList();

        /* Looking at first side */
        for ( int i = 0; i < this.size(); i++ ) {
            if ( getLeftElement( i ).equals( o ) ) {
                result.add( getRightElement( i ) );
            }
            if ( getRightElement( i ).equals( o ) ) {

                result.add( getLeftElement( i ) );

            }
        }
        return result;
    }

    /**
     *
     * <p>
     * Returns the first LEFT object matching the RIGHT object o
     * </p>
     * <p>
     * For exemple <code>findMyLovers(3)</code> in [(3,"Three"),("Not Three",
     * 3),(4,5),(9,10)] will return <strong>"Not Three"</strong>
     * </p>
     * 
     * @return first love of o
     */

    public LEFT findMyFirstLoveInLeft( RIGHT o ) {

        for ( int i = 0; i < this.size(); i++ ) {

            if ( getRightElement( i ).equals( o ) ) {
                return getLeftElement( i );
            }
        }
        return null;

    }

    /**
     * <p>
     * Returns the first RIGHT object matching the LEFT object o
     * </p>
     * <p>
     * For exemple <code>findMyLovers(3)</code> in [(3,"Three"),("Not Three",
     * 3),(4,5),(9,10)] will return <strong>"Three"</strong>
     * </p>
     * 
     * @return first love of o
     */
    public RIGHT findMyFirstLoveInRight( LEFT o ) {

        for ( int i = 0; i < this.size(); i++ ) {

            if ( getLeftElement( i ).equals( o ) ) {
                return getRightElement( i );
            }
        }
        return null;

    }

    /**
     * <p>
     * Returns the first object matching the object o, whatever if it's on the
     * Right or the Left
     * </p>
     * 
     * @return first love of o
     */
    public Object findMyFirstLove( Object o ) {

        for ( int i = 0; i < this.size(); i++ ) {
            if ( getLeftElement( i ).equals( o ) ) {
                return getRightElement( i );
            }
            if ( getRightElement( i ).equals( o ) ) {
                return getLeftElement( i );
            }
        }
        return null;
    }

    /**
     * <p>
     * Sometimes a CoupleList is close of a HashMap. You can get it with this
     * function. Of course <strong>you may loose datas</strong> in your HashMap
     * if a Left element is not Unique.
     * </p>
     * <p>
     * LEFT will be the key, RIGHT the value ; use the getReversedList()
     * function if you wish to switch.
     * </p>
     * 
     * @return a HashMap representation of the CoupleList, with possible loss of
     *         datas.
     */
    public HashMap<LEFT, RIGHT> getHashMap() {
        HashMap<LEFT, RIGHT> map = new HashMap<LEFT, RIGHT>();

        for ( int i = 0; i < this.size(); i++ ) {
            map.put( this.getLeftElement( i ), this.getRightElement( i ) );
        }
        return map;
    }

    /**
     * <p>
     * Using this function on [(3,"Three"),(4, "44"),( 5, "cinquo"), (6, "6")]
     * will return [("Three", 3),("44", 4),( "cinquo", 5), ("6", 6)]
     * </p>
     * <p>
     * You can't be confused if you use Generics.
     * </p>
     * 
     * @return a new list, but with inverted Couples.
     */
    public CoupleList<RIGHT, LEFT> reverse() {
        CoupleList<RIGHT, LEFT> result = new CoupleList<RIGHT, LEFT>();

        for ( int i = 0; i < this.size(); i++ ) {
            result.addCouple( this.getRightElement( i ), this.getLeftElement( i ) );
        }
        return result;

    }

    /**
     * <p>
     * Returns the Couples where the Object o is on the LEFT <strong>OR</strong>
     * the RIGHT.
     * </p>
     * <p>
     * It returns an empty list if nothing is found. It does NOT return null.
     * </p>
     * 
     * @param o
     *            Object to be found
     * @return couples containing o in LEFT <strong>or</strong> RIGHT
     */
    public CoupleList<LEFT, RIGHT> findWhere( Object o ) {

        CoupleList<LEFT, RIGHT> list = new CoupleList();
        for ( int i = 0; i < this.size(); i++ ) {
            if ( this.getLeftElement( i ).equals( o ) || this.getRightElement( i ).equals( o ) ) {
                list.add( this.get( i ) );
            }
        }
        return list;
    }

    /**
     * <p>
     * Returns the Couples where the <strong>left</strong> is on the LEFT
     * <strong>AND right</strong> is on the RIGHT.
     * </p>
     * Returns an EMPTY coupleList when can't find the couple, and nerver a NULL
     * coupleList
     * 
     * @param left
     *            LEFT object wanted
     * @param right
     *            RIGHT object wanted
     * @return couples containing left in LEFT <strong>and</strong> right in
     *         RIGHT
     */
    public CoupleList<LEFT, RIGHT> findWhere( LEFT left, RIGHT right ) {

        CoupleList<LEFT, RIGHT> list = new CoupleList();
        for ( int i = 0; i < this.size(); i++ ) {
            if ( this.getLeftElement( i ).equals( left ) && this.getRightElement( i ).equals( right ) ) {
                list.add( this.get( i ) );
            }
        }
        return list;
    }

    /**
     * <p>
     * This function removes all couples (left, right) from the list.
     * </p>
     * <p>
     * Only couples containg left <strong>AND</strong> right elements will be
     * removed.
     * </p>
     * 
     * @param left
     *            LEFT element to test
     * @param right
     *            RIGHT element to test
     */
    public void removeWhere( LEFT left, RIGHT right ) {
        CoupleList<LEFT, RIGHT> listToRemove = new CoupleList();
        for ( int i = 0; i < this.size(); i++ ) {
            if ( this.getLeftElement( i ).equals( left ) && this.getRightElement( i ).equals( right ) ) {
                listToRemove.add( this.get( i ) );
            }
        }

        /* Maintenant on enlève les éléments trouvés */
        for ( int i = 0; i < listToRemove.size(); i++ ) {
            this.remove( listToRemove.get( i ) );
        }
    }

    /**
     * removes all couples where o is in RIGHT or LEFT
     * 
     * @param o
     */
    public void removeWhere( Object o ) {

        ArrayList v = this.findWhere( o );
        this.removeAll( v );

    }

    /**
     * Easily builds a CoupleList : CoupleList.builder(3, "Three", 4, "44", 5,
     * "cinquo", 6, 6) will create the CoupleList :[(3, "Three"),(4, "44"),( 5,
     * "cinquo"), (6, 6)]
     * 
     * @throws IllegalArgumentException
     *             if number of params is not pair.
     */
    public static <L, R> CoupleList build( Object... x ) {

        CoupleList<L, R> cpList = new CoupleList<L, R>();
        int size = x.length;

        if ( size == 0 ) {
            return cpList;
        }
        if ( size % 2 != 0 ) {
            throw new IllegalArgumentException( "Var-args must be pair" );
        }

        int index = 0;
        while ( index < size / 2 ) {
            cpList.addCouple( (L) x[2 * index], (R) x[2 * index + 1] );
            index++;
        }

        return cpList;
    }

    /**
     * Returns a CoupleList where every objects are toString() values of initial
     * objects
     * 
     * @return a stringified CoupleList
     */
    public CoupleList<String, String> stringify() {
        CoupleList<String, String> result = new CoupleList<String, String>();

        for ( Couple c : this ) {
            result.addCouple( c.getLeft().toString(), c.getRight().toString() );
        }
        return result;
    }

    /**
     * Considers the CoupleList as a Name/Value list. Returns null if the name
     * is not found
     * 
     * @param name
     * @param ignoreCase
     * @return
     */
    public RIGHT getValue( String name, boolean ignoreCase ) {

        for ( Couple<LEFT, RIGHT> c : this ) {
            String val = c.getLeft().toString();
            if ( ignoreCase ) {
                if ( val.equalsIgnoreCase( name ) ) {
                    return c.getRight();
                }
            } else {
                if ( val.equals( name ) ) {
                    return c.getRight();
                }
            }
        }
        return null;

    }

}
