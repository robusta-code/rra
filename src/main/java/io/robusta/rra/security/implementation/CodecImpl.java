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


import io.robusta.rra.security.Codec;

/**
 * This class makes some Base64 and MD5 encoding/decoding <br>
 * Thanx to SupInfo Labs, Robert Harder and M.Duchrow
 * @author Nicolas Zozol for Robusta Web ToolKit & <a href="http://www.edupassion.com">Edupassion.com</a> - nzozol@robustaweb.com
 * @see
 *
 * // SHA-1 Java : http://www.anyexample.com/programming/java/java_simple_class_to_compute_sha_1_hash.xml
 * //
 *
 */
public class CodecImpl implements Codec {

    /**
     * 
     * @param toEncode
     * @return the Base64 string
     */
    @Override
    public  String encodeBase64(String toEncode){                
        return Base64Converter.encode(toEncode);
    }

    
    
    /**
     * 
     * @param encoded
     * @return
     */
    @Override
    public  String decodeBase64(String encoded){
        return Base64Converter.decodeToString(encoded);
    }
    
    /**
     * 
     * @return the md5 encoded String
     */
    @Override
    public String encodeMD5(String toEncode){
        return MD5.encodeMD5(toEncode);
    }

    
    
    /**
     * When you decode dfskdjflk==, you retrieve (kslater:mypassword)
     * This function retrieve mypassword, or throw a CodecException if the ':' doesn't exist
     * @param b64String
     * @return the password of the encoded pattern
     */
    public  String getUsername(String b64String) throws CodecException{
        
        String decoded = Base64Converter.decodeToString(b64String);
        int index = decoded.indexOf(":");
        if (index < 0) {
            throw new  CodecException("can't find ':' character in decoded credentials for :" + decoded);
        }
        
        return decoded.substring(0, index);
    }
    
    
    
    /**
     * When you decode dfskdjflk==, you retrieve (kslater:mypassword)
     * This function retrieve mypassword, or throw a CodecException if the ':' doesn't exist
     * @param b64String
     * @return the password of the encoded pattern
     */
    public  String getPassword(String b64String) throws CodecException{
        
        String decoded = Base64Converter.decodeToString(b64String);
        int index = decoded.indexOf(":");
        if (index < 0) {
            throw new  CodecException("can't find ':' character in decoded credentials for :" + decoded);
        }
        
        return decoded.substring(index + 1);
    }
    
    
    
}
