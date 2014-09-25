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

import io.robusta.rra.exception.ValidationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class gives a few function to waork on Strings.
 * <p>
 * Most importants are probably
 * <ul>
 * <li>{@link StringUtils#validateEmail(String) validateEmail()}
 * <li>{@link StringUtils#replaceAny(String, String, String) replaceAny()}
 * <li> {@link StringUtils#removeCharacters(String, String) removeCharacters()}
 * </ul>
 * </p>
 * Created by Nicolas Zozol for Robusta Code
 * 
 * @author Nicolas Zozol TODO : verify this class is GWT compatible TODO : check
 *         if any is NOT used inside the library
 */
public class StringUtils {

    /**
     * This function test if an email is valid. It's not the best validation of
     * the world : all good emails will pass, but some wrong emails (even if
     * it's very unlikely) may also pass.
     * <p>
     * This function is based on the Javascript pattern
     * /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/
     * </p>
     * <p>
     * Check <a href="http://www.ietf.org/rfc/rfc2822.txt">IETF</a> for more
     * informations.
     * </p>
     * 
     * @param email
     *            email to be tested
     * @return true if email is an email pattern, false if not
     * @throws IllegalArgumentException
     *             if the email is null
     */
    public static boolean validateEmail( String email ) {

        if ( email == null ) {
            throw new IllegalArgumentException( "The email is null" );
        }
        if ( email.equals( "" ) ) {
            return false;
        }
        String worker = new String( email );
        worker = worker.trim();

        return worker.matches( "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+" );
        /*
         * //Set the email pattern string //Pattern p =
         * Pattern.compile(".+@.+\\.[a-z]+"); Pattern p = Pattern.compile(
         * "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");
         * //Match the given string with the pattern Matcher m =
         * p.matcher(worker);
         * 
         * return m.matches();
         */
    }

    /**
     * Returns true if the String could be a md5
     * 
     * @param md5String
     *            String to test : could it be a MD5, or there is no way ?
     * @return true if the String could be a md5
     * @throws IllegalArgumentException
     *             if the md5String is null
     */
    public static boolean validateMD5( String md5String ) {

        if ( md5String == null ) {
            throw new IllegalArgumentException( "md5String is null" );
        }

        return md5String.length() == 32 && md5String.matches( "([a-z0-9])+" );

    }

    /**
     * Returns what is before the '@' character. For
     * <strong>nicolas.zozol@robustaweb.com</strong> email, we'll get here
     * <strong>nicolas.zozol</strong>
     * 
     * @param email
     * @return The email prefix
     * @throws IllegalArgumentException
     *             if the email is null
     * @throws ValidationException
     *             if email is not a valid email
     */
    public static String getEmailPrefix( String email ) throws ValidationException {

        if ( email == null ) {
            throw new IllegalArgumentException( "email is null" );
        }

        if ( !validateEmail( email ) ) {
            throw new ValidationException( "Email is not valid" );
        }

        int index = email.indexOf( '@' );
        return email.substring( 0, index );

    }

    /**
     *
     * Return true if the email is in two parts, like nicolas.zozol@gmail.com
     * Note that Bart.@simpsons.com will return true.
     * 
     * @param email
     * @return true if the email is in two parts, like nicolas.zozol@gmail.com
     * @throws ValidationException
     *             if the email is not valid
     */
    public static boolean isEmailSplitInTwoParts( String email ) throws ValidationException {

        if ( !validateEmail( email ) ) {
            throw new ValidationException( "Email is not valid" );
        }

        int index = email.indexOf( '@' );
        String prefix = email.substring( 0, index );

        return ( prefix.contains( "." ) );

    }

    /**
     * <p>
     * removes a character in the String
     * </p>
     *
     * <p>
     * String boss="Bruce Springsteen";<br/>
     * String result = StringUtils.removeCharacter(boss, 'e') returns
     * "Bruc Springstn".<br/>
     * But the original String boss is not modified.
     * </p>
     * 
     * @param string
     * @param c
     * @return
     */
    public static String removeCharacter( String string, char c ) {

        String work = new String( string );

        String s = "" + c;
        work = work.replace( s, "" );
        return work;
    }

    /**
     * <p>
     * Removes in the string every characters inside charactersToRemove.<br/>
     * For exemple removeCharacters("Napoleon","nou") returns Naple because it
     * will remove one 'n', two 'o' and zero 'u' <br/>
     * Like for removeCharacter(String string, char c), the original String is
     * not modified.
     * </p>
     * <p>
     * It's a recursive function, not the fastest you may have.
     * </p>
     *
     * @param original
     *            Original string
     * @param charactersToRemove
     *            characters that will be removed form the Original String
     * @return a new string without the specified characters
     * @throws IllegalArgumentException
     *             if original or charactersToRemove is null
     */
    public static String removeCharacters( String original, String charactersToRemove ) {

        if ( original == null ) {
            throw new IllegalArgumentException( "Your original parameter is null" );
        }
        if ( charactersToRemove == null ) {
            throw new IllegalArgumentException( "charactersToRemove parameter si null" );
        }

        String work = new String( original );

        char c;
        for ( int i = 0; i < charactersToRemove.length(); i++ ) {
            c = charactersToRemove.charAt( i );
            work = removeCharacter( work, c );
        }
        return work;

    }

    /**
     * Will remove all characters from the end. <br/>
     * <p>
     * removeStartingCharacters(iiiiMalagi, 'i') returns Malagi
     * </p>
     * If the string is empty, it returns also an empty string. If the string is
     * null, it will throw a IllegalArgumentException
     * 
     * @param originalString
     *            Original parameter
     * @param character
     * @throws IllegalArgumentException
     *             : if the string is null
     * @return Removes all the specified character found at the beginning of the
     *         string
     */
    public static String removeStartingCharacters( String originalString, char character ) {

        if ( originalString == null ) {
            throw new IllegalArgumentException( "original String is null" );
        }

        if ( originalString.length() == 0 ) {
            return originalString;
        }

        String result = new String( originalString );

        while ( result.indexOf( character ) == 0 ) {
            result = removeFirstCharacter( result );
            if ( result.length() == 0 ) {
                return "";
            }
        }

        return result;
    }

    /**
     * Will remove all characters from the end. <br/>
     * <p>
     * removeTrailingCharacters(Malagaaaa, 'a') returns Malag
     * </p>
     * If the string is empty, it returns also an empty string. If the string is
     * null, it will throw a IllegalArgumentException
     * 
     * @param originalString
     *            Original parameter
     * @param character
     * @throws IllegalArgumentException
     *             : if the string is null
     * @return Removes all the specified character found at the end of the
     *         string
     */
    public static String removeTrailingCharacters( String originalString, char character ) {

        if ( originalString == null ) {
            throw new IllegalArgumentException( "original String is null" );
        }

        if ( originalString.length() == 0 ) {
            return originalString;
        }

        String result = new String( originalString );

        while ( result.lastIndexOf( character ) == result.length() - 1 ) {
            result = removeLastCharacter( result );
            if ( result.length() == 0 ) {
                return "";
            }
        }

        return result;
    }

    /**
     * <p>
     * Simply removes the last character
     * </p>
     * 
     * @param originalString
     * @return a new String without the
     * @throws IllegalArgumentException
     *             if the string is null
     */
    public static String removeFirstCharacter( String originalString ) {

        if ( originalString == null ) {
            throw new IllegalArgumentException( "string is null" );
        }

        if ( originalString.length() <= 1 ) {
            return "";
        }
        return originalString.substring( 1 );

    }

    /**
     * <p>
     * Simply removes the last character
     * </p>
     * 
     * @param originalString
     * @return a new String without the
     * @throws IllegalArgumentException
     *             if the string is null
     */
    public static String removeLastCharacter( String originalString ) {

        if ( originalString == null ) {
            throw new IllegalArgumentException( "string is null" );
        }

        if ( originalString.length() <= 1 ) {
            return "";
        }
        return originalString.substring( 0, originalString.length() - 1 );

    }

    /**
     * <p>
     * This is a simpler alternative to original String.replaceAll function that
     * uses complex Regexp. Here, you modify a String by changing the
     * <strong>characters sequence</strong> sequenceToReplace by the replacement
     * string.
     * </p>
     *
     * <p>
     * Exemple :<br/>
     * &nbsp;<code>String original = "//cooluri//living.js///";</code><br/>
     * Then <br/>
     * &nbsp;<code>StringUtils.replaceAll(original,"//", "-");</code><br/>
     * will return :<br/>
     * -cooluri-living.js-/
     * </p>
     * <p>
     * Note that the '///' is always seen as ('//'+'/') and
     * <strong>never</strong> as ('/'+'//'). This function is
     * <strong>sequential</strong>, and will not always work like regular
     * expressions.
     * </p>
     * 
     * @param originalString
     *            The original string
     * @param sequenceToReplace
     *            The characters sequence to replace
     * @param replacement
     * @return
     * @throws IllegalArgumentException
     *             if a param is null
     */
    public static String replaceSequence( String originalString, String sequenceToReplace, String replacement ) {

        if ( originalString == null ) {
            throw new IllegalArgumentException( "originalString is null" );
        }
        if ( sequenceToReplace == null ) {
            throw new IllegalArgumentException( "charsToReplace is null" );
        }
        if ( replacement == null ) {
            throw new IllegalArgumentException( "replacement is null" );
        }
        if ( sequenceToReplace.equals( replacement ) ) {
            return originalString;
        }

        String newString = new String( originalString );
        if ( originalString == null || originalString.length() == 0 ) {
            return originalString;
        }
        if ( sequenceToReplace == null || sequenceToReplace.length() == 0 ) {
            return originalString;
        }
        int indexPiece = originalString.indexOf( sequenceToReplace );
        while ( indexPiece != -1 ) {
            newString = newString.substring( 0, indexPiece ) + replacement
                    + newString.substring( indexPiece + sequenceToReplace.length() );
            indexPiece = newString.indexOf( sequenceToReplace );
        }

        return newString;
    }

    /**
     * Replace any character defined in anyCharacter by a replacement sequence
     * Ex :
     * 
     * <pre>
     * replaceAny("john jack jim", "ji", "t") => "tohn tack ttm"
     * </pre>
     * 
     * @param originalString
     * @param anyCharacter
     * @param replacement
     * @return
     */
    public static String replaceAny( String originalString, String anyCharacter, String replacement ) {

        String worker = originalString;
        for ( Character c : anyCharacter.toCharArray() ) {
            String pattern = "" + c;
            worker = worker.replaceAll( pattern, replacement );
        }
        return worker;
    }

    /**
     * <p>
     * Keeps the 'number' first characters, and might be useful to keep your
     * user's privacy :<br/>
     * &nbsp;<code>truncate("Hernandez", 3)</code><br/>
     * will produce: "Her"
     * </p>
     * <p>
     * If number is bigger than the size of the String, the whole string is kept
     * : <br/>
     * &nbsp;<code>truncate("Hernandez", 24)</code><br/>
     * will produce: "Hernandez"
     * </p>
     * <p>
     * If number==0, the function returns an empty string.
     * </p>
     * 
     * @param original
     * @param number
     * @return a new smaller String.
     * @throws IllegalArgumentException
     *             if number<0 or original is null
     */
    public static String truncate( String original, int number ) {
        if ( number < 0 ) {
            throw new IllegalArgumentException( "number :" + number + " is <0" );
        }
        if ( original == null ) {
            throw new IllegalArgumentException( "original string is null" );
        }
        String newLastName = original.length() >= number ? original.substring( 0, number ) : original;
        return newLastName;

    }

    /**
     * If stringToTes contains any character in 'characatersToTest', the
     * function returns true, false if not
     * 
     * @param stringToTest
     * @param charatersToTest
     * @return true if stringToTes contains any character in 'characatersToTest'
     * @throws IllegalArgumentException
     *             if charactersToTest is null or empty
     */
    public static boolean containsCharacter( String stringToTest, String charatersToTest ) {

        if ( charatersToTest == null ) {
            throw new IllegalArgumentException( "characters is null" );
        }
        if ( charatersToTest.length() == 0 ) {
            throw new IllegalArgumentException( "No character to Test" );
        }

        for ( int i = 0; i < charatersToTest.length(); i++ ) {
            char c = charatersToTest.charAt( i );

            if ( stringToTest.contains( String.valueOf( c ) ) ) {
                return true;
            }
        }

        return false;

    }

    /**
     * Returns the concatenation of the absolute and relative paths, adding a
     * "/" character between them if needed Calling addPath(uri, "/") ensures
     * that the path will finish with only one "/" (as long as uri doesn't
     * finishes with more than one "/")
     * 
     * @param absolute
     * @param relative
     * @return
     */
    public static String addPath( String absolute, String relative ) {
        return removeTrailingCharacters( absolute, '/' ) + "/" + removeStartingCharacters( relative, '/' );
    }

    /**
     * Add the protocol to the String
     * 
     * @param protocol
     * @param uri
     * @return a new Stirng with the protocol
     */
    public static String addProtocol( String protocol, String uri ) {
        String worker = removeTrailingCharacters( protocol, '/' );
        worker = removeTrailingCharacters( worker, ':' );
        return worker + "://" + removeStartingCharacters( uri, '/' );
    }

    /**
     * Needed for Gwt Compatibility : GWT does not yet provide a
     * getSimpleClassName
     * 
     * @param o
     * @return
     */
    public static String getSimpleClassName( Object o ) {
        String className = o.getClass().getName();
        return className.substring(
                className.lastIndexOf( "." ) + 1, className.length() );

    }

    /**
     * Transform an Obejct array into a String array, by calling toString() on
     * each element.
     *
     * @param array
     * @param transformNullIntoBlank
     *            if true, a null is changed to blank. If false, we keep null
     * @return a String array
     */
    public String[] stringify( Object[] array, boolean transformNullIntoBlank ) {

        ArrayList<String> result = new ArrayList<String>();
        for ( Object o : array ) {
            if ( o != null ) {
                result.add( o.toString() );
            } else {
                if ( transformNullIntoBlank ) {
                    result.add( "" );
                } else {
                    result.add( null );
                }
            }
        }
        return (String[]) result.toArray();
    }

    /**
     * Return a String that starts with an Uppercase character, and ends with
     * LowerCase characters strictCapitalize("johnDoe") returns Johndoe
     * 
     * @param str
     *            string to capitalize
     * @return a String that starts with an Uppercase character, and ends with
     *         LowerCase characters
     * @see #simpleCapitalize(java.lang.String)
     */
    public static String strictCapitalize( String str ) {
        if ( str == null ) {
            throw new IllegalArgumentException( "String is null" );
        }
        if ( str.length() == 0 ) {
            return str;
        }
        String worker = str.toLowerCase();
        return worker.substring( 0, 1 ).toUpperCase() + worker.substring( 1 );
    }

    /**
     * Capitalize the first letter, and doesn't touch any other
     * simpleCapitalize("johnDoe") returns JohnDoe
     * 
     * @param str
     *            string to capitalize
     * @return the same string with capitalized first letter
     * @see #strictCapitalize(java.lang.String)
     */
    public static String simpleCapitalize( String str ) {
        if ( str == null ) {
            throw new IllegalArgumentException( "String is null" );
        }
        if ( str.length() == 0 ) {
            return str;
        }
        String work = new String( str );
        return work.substring( 0, 1 ).toUpperCase() + work.substring( 1 );
    }

    /**
     * TODO 1 : work on this ; Use StringTokenizer
     * <p>
     * Splits the provided text into an array ,* separators specified.
     * </p>
     *
     * <p>
     * The separator is not included in the returned String array. Adjacent
     * separators are treated as one separator.
     * </p>
     *
     * <pre>
     * StringUtils.split("ab de fg", " ")   = ["ab", "de", "fg"]
     * StringUtils.split("ab   de fg", " ") = ["ab", "cd", "ef"]
     * StringUtils.split("ab:cd:ef", ":")    = ["ab", "cd", "ef"]
     * </pre>
     *
     * @param string
     *            the String to parse, may be null
     * @param separatorChars
     *            the characters used as the delimiters
     * @return an array of parsed Strings
     * @throws IllegalArgumentException
     *             if string or separatorChars is null, or if separatorChars is
     *             empty
     */
    public static String[] split( String string, String separatorChars ) {
        if ( string == null ) {
            throw new IllegalArgumentException( "string argument is null" );
        }
        if ( separatorChars == null || separatorChars.length() == 0 ) {
            throw new IllegalArgumentException( "separators is null or empty" );
        }

        List<String> resultList = new ArrayList<String>();
        String worker = new String( string );
        String firstSeparator = separatorChars.substring( 0, 1 );
        assert firstSeparator.length() == 1;

        char[] charArray = separatorChars.toCharArray();
        // We replace multiple instance of other separator by only one
        // firstSeparator
        for ( int i = 1; i < charArray.length; i++ ) {
            String pattern = "(" + charArray[i] + ")+";
            worker = worker.replaceAll( pattern, firstSeparator );
        }

        // One more pass for the first separator
        String pattern = "(" + firstSeparator + ")+";
        worker = worker.replaceAll( pattern, firstSeparator );

        System.out.println( "separator : " + firstSeparator + " ; worker : " + worker );
        Collections.addAll( resultList, worker.split( firstSeparator ) );
        return (String[]) resultList.toArray( new String[resultList.size()] );
    }

    /**
     * @todo3 : to be tested Join elements
     * @param strings
     * @param glue
     * @return
     */
    public static String join( Object[] strings, String glue ) {

        if ( strings == null ) {
            throw new IllegalArgumentException( "strings array is null" );
        }
        if ( glue == null ) {
            throw new IllegalArgumentException( "No glue ; use empty String instead of null" );
        }
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < strings.length; i++ ) {
            sb.append( strings[i].toString() );
            // if not last
            if ( i < strings.length - 1 && glue.length() > 0 ) {
                sb.append( glue );
            }
        }
        return sb.toString();
    }

    public static String defaultJoints = " _-";

    /**
     * Transfom joints in capital characters Chateau De Versailles =>
     * ChateauDeVersaille white_house => whiteHouse (House is now capitalized)
     * groovyStyle_grails => groovyStyleGrails (inside capitals are untouched)
     * One difference with Java style is that the first letter might stay a
     * capital
     *
     * If specifiedJoints == null, the method uses default ones : " _-", thought
     * this can be changed with the {@link StringUtils#defaultJoints}
     *
     * @param string
     * @return
     */
    public static String normalize( String string, String specifiedJoints ) {
        if ( specifiedJoints == null ) {
            specifiedJoints = defaultJoints;
        }

        String worker = replaceAny( string, specifiedJoints, " " );
        String[] array = split( worker, " " );
        for ( int i = 0; i < array.length; i++ ) {
            String s = array[i];
            if ( i > 0 ) {
                array[i] = simpleCapitalize( s );
            }

        }

        return join( array, "" );
    }

}
