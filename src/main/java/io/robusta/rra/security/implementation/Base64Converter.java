
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

package io.robusta.rra.security.implementation;


import java.io.UnsupportedEncodingException;

// ===========================================================================
// IMPORTS
// ===========================================================================

/**
 * A converter that allows to encode strings, byte arrays and char arrays to
 * BASE64 and vice versa.<p>
 * <b> 
 * Currently it is based on the class Base64 from Robert Harder 
 * (http://iharder.sourceforge.net/base64).
 * Thanks to him that he published his implementation as open source.
 * </b>
 * This class mainly adds some convenience methods for string handling.
 * 
 * @author M.Duchrow
 * @version 1.1
 *
 * TODO for Robusta Web Library : check compatibility of the licence
 */
class Base64Converter
{
  // =========================================================================
  // CONSTANTS
  // =========================================================================

  // =========================================================================
  // INSTANCE VARIABLES
  // =========================================================================

  // =========================================================================
  // PUBLIC CLASS METHODS
  // =========================================================================
	/**
	 * Returns a BASE64 encoded version of the given string
	 */
	public static String encode( String unencoded ) 
	{
		byte[] bytes ;
		
		bytes = unencoded.getBytes() ;
		return encodeToString( bytes ) ;
	} // encode() 

	// -------------------------------------------------------------------------

	/**
	 * Returns a BASE64 encoded version of the given character array
	 */
	public static char[] encode( char[] unencoded ) 
	{
		String str ;
		
		str = new String( unencoded ) ;
		return encode( str.getBytes() ) ;
	} // encode() 

	// -------------------------------------------------------------------------

	/**
	 * Returns a BASE64 encoded version of the given byte array
	 */
	public static char[] encode( byte[] unencoded ) 
	{
		return encodeToString( unencoded ).toCharArray() ;
	} // encode() 

	// -------------------------------------------------------------------------

	/**
	 * Returns a BASE64 encoded version of the given byte array as String
	 */
	public static String encodeToString( byte[] unencoded ) 
	{
		return Base64.encodeBytes( unencoded ) ;
	} // encodeToString() 

	// -------------------------------------------------------------------------

	/**
	 * Returns a BASE64 encoded version of the given char array as String
	 */
	public static String encodeToString( char[] unencoded ) 
	{
		char[] chars ;
		
		chars = encode( unencoded ) ;
		return new String(chars) ;
	} // encodeToString() 

	// -------------------------------------------------------------------------

	/**
	 * Returns a byte array decoded from the given BASE64 encoded char array
	 */
	public static byte[] decode( char[] encoded ) 
	{
		return decode( new String(encoded) ) ;
	} // decode() 

	// -------------------------------------------------------------------------

	/**
	 * Returns a byte array decoded from the given BASE64 encoded String
	 */
	public static byte[] decode( String encoded ) 
	{
		return Base64.decode( encoded ) ;
	} // decode() 

	// -------------------------------------------------------------------------

	/**
	 * Returns a string decoded from the given BASE64 encoded String
	 * 
	 * @param encoded The BASE64 encoded string
	 */
	public static String decodeToString( String encoded )
	{
		byte[] bytes ;
		
		bytes = decode( encoded ) ;
		return new String( bytes ) ;
	} // decode() 

	// -------------------------------------------------------------------------
	
	/**
	 * Returns a string decoded from the given BASE64 encoded String
	 * 
	 * @param encoded The BASE64 encoded string
	 * @param encoding The name of the reult string's encoding (e.g. "UTF-8"). 
	 */
	public static String decodeToString( String encoded, String encoding )
		throws UnsupportedEncodingException
	{
		byte[] bytes ;
		
		bytes = decode( encoded ) ;
		return new String( bytes, encoding ) ;
	} // decode() 

	// -------------------------------------------------------------------------

	// =========================================================================
  // CONSTRUCTORS
  // =========================================================================
  /**
   * Initialize the new instance with default values.
   */
  private Base64Converter()
  {
    super() ;
  } // Base64Converter() 

  // =========================================================================
  // PUBLIC INSTANCE METHODS
  // =========================================================================

  // =========================================================================
  // PROTECTED INSTANCE METHODS
  // =========================================================================

} // class Base64Converter 
