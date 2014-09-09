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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encode a String in MD5
 * @author Sup-Info (labo sun) : http://www.labo-sun.com/resource-FR-codesamples-150-0-java-texte-crypter-une-string-avec-md5.htm 
 */
public class MD5 {

    /*    
     * Encode  the String with MD5 algorithm
     * @param stringToEncode : String to encode
     * @return  hexadecimal String (32 bits)
     */
    public static String encodeMD5(String stringToEncode) {

        byte[] uniqueKey = stringToEncode.getBytes();

        byte[] hash = null;
        try {

            hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("no MD5 support in this VM");
        }

        StringBuffer hashString = new StringBuffer();
        for (int i = 0; i < hash.length; ++i) {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1) {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            } else {
                hashString.append(hex.substring(hex.length() - 2));
            }
        }

        return hashString.toString();

    }

    
    
}
