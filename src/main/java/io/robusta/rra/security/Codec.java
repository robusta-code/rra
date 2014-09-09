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
package io.robusta.rra.security;


import io.robusta.rra.security.implementation.CodecException;

/**
 * Common interface for usual work in username/password encoding through the web
 * @author robusta web
 */
public interface Codec {

	/**
	 * Encode a Base64 string. Usually, the string to encode looks is "user:password"
	 * For newbbes, please note that Base64 does not <strong>encrypt</strong> but allows to submit 
	 * for exemple a password containint spaces or special caracters through the web 
	 * @param toEncode String to encode
	 * @return a Base64 encoded string
	 * @see #encodeBase64(String)
	 */
    public String encodeBase64(String toEncode);

    /**
	 * Decode a Base64 string
	 * @param encoded String to encode
	 * @return a Base64 encoded string
	 * @see #decodeBase64(String)
	 */
    public String decodeBase64(String encoded);

    /**
     * Create a MD5 hash. Be aware that MD5 alone is not a truly safe method to protect your users password.
     * See for exemple John the Ripper software.   
     * @param toHash String to hash
     * @return the hash
     */
    public String encodeMD5(String toHash);

    /**
     * Returns the username side of the B64 encoded String, when the B64 string is encoded like : "user:password"
     * @param b64String
     * @return the username
     * @throws CodecException if no username can be found in the enocoded string
     * @see #encodeBase64(String)
     */
    public String getUsername(String b64String) throws CodecException;

    /**
     * Returns the password side of the B64 encoded String, when the B64 string is encoded like : "user:password"
     * @param b64String
     * @return the password
     * @throws CodecException if no password can be found in the enocoded string
     * @see #encodeBase64(String) 
     */
    public String getPassword(String b64String) throws CodecException;
}
