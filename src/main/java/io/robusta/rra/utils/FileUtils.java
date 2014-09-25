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

import io.robusta.rra.exception.FileException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * FileUtilities brings frequently used funcions, to be used in a standard
 * Windows/unix/Android environements
 * <p/>
 * in a Linux, Windows & AppEngine environment
 * </p>
 *
 * Created by Nicolas Zozol for Robusta Code
 * 
 * @author Nicolas Zozol
 */
public class FileUtils {

    static boolean inClouds = false;

    /**
     * Reads a file line after line.
     *
     * @param path
     *            Full path of the file ('c:/webapp/data.xml' or
     *            '/var/webapp/data.xml')
     * @return The content of the file.
     * @throws java.io.FileNotFoundException
     */
    public static String readFile( String path ) throws IOException {

        FileReader reader = null;

        BufferedReader buffReader = null;

        StringBuilder text = new StringBuilder();

        try {
            reader = new FileReader( path );
            buffReader = new BufferedReader( reader );

            String tempLine;
            while ( ( tempLine = buffReader.readLine() ) != null ) {
                text.append( tempLine ).append( "\n" );
            }
        } finally {
            reader.close();
            buffReader.close();
        }
        return text.toString();
    }

    /**
     * @param path
     *            Full path of the file ('c:/webapp/data.xml' or
     *            '/var/webapp/data.xml')
     * @param content
     *            Content to be saved in the file
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     * @todo2 : It seems to save in only one line ! need to be tested
     */
    public static void saveFile( String path, String content ) throws FileNotFoundException, IOException {

        File f = new File( path );
        if ( !f.exists() ) {
            f.createNewFile();
        }
        FileWriter fstream = null;
        BufferedWriter out = null;
        try {
            fstream = new FileWriter( path );
            out = new BufferedWriter( fstream );
            out.write( content );
            fstream.flush();
        } finally {
            out.close();
            fstream.close();
        }
    }

    /**
     * Create a directory
     *
     * @param rootPath
     *            directory or mount point containing your future directory
     * @param directoryName
     *            name of the future directory
     * @throws FileException
     *
     */
    public static void createDirectory( String rootPath, String directoryName ) throws FileException {
        File f = new File( rootPath + "/" + directoryName );
        boolean result;
        if ( f.exists() ) {
            throw new FileException( "Directory already exists" );
        } else {
            result = f.mkdir();
            if ( !result ) {
                throw new FileException( "Directory could not be created for unknown reason" );
            }
        }
    }

    /**
     * <p>
     * Delete a directory. It first delete recursively all subdirectories, then
     * all files of this directory, then the directory.
     * </p>
     * <p>
     * The code was partially found in the net - it's very likely to be public
     * domain.
     * </p>
     *
     * @param path
     *            java.io.File representation of the directory
     * @throws FileException
     *             if the path does not exist or if the directory can't be
     *             deleted for any unknown reason.
     */
    static public void deleteDirectory( File path ) throws FileException {
        if ( path.exists() ) {
            File[] files = path.listFiles();
            for ( int i = 0; i < files.length; i++ ) {
                if ( files[i].isDirectory() ) {
                    // recursive mode
                    deleteDirectory( files[i] );
                }
            }
        } else {
            throw new FileException( "The path :'" + path + "' does't exist " );
        }

        if ( !path.delete() ) {
            throw new FileException( "Could not delete path " + path + "!" );

        } else {
            // nothing to do, it's ok
        }

    }

    /**
     * Delete the file, that is NOT a directory
     *
     * @param path
     * @return
     * @throws FileException
     *
     */
    static public boolean deleteFile( String path ) throws FileException {

        File f = new File( path );
        if ( f == null || !f.exists() ) {
            throw new FileException( "Can't find the file at path : " + path );
        } else {
            return f.delete();
        }

    }

    /**
     * returns true if the Path or File exists
     *
     * @param path
     * @return
     */
    public static boolean fileExists( String path ) {

        File f = new File( path );
        return f.exists();

    }

    /**
     * Read an InputStream and returns a String. Notice that it will close the
     * InputStream.
     *
     * @param inputStream
     *            InputStrem
     * @return
     * @throws java.io.IOException
     *             if it's impossible to read tje InputStram
     * @throws IllegalArgumentException
     *             if is is null
     */
    public static String readInputStream( InputStream inputStream ) throws IOException {

        BufferedReader in = new BufferedReader( new InputStreamReader( inputStream ) );
        StringBuilder builder = new StringBuilder();

        try {

            if ( inputStream == null ) {
                throw new IllegalArgumentException( "InputStream is null" );
            }

            String str = "";
            boolean firstLine = true;

            while ( str != null ) {
                if ( !firstLine ) {
                    builder.append( "\n" );
                }
                str = in.readLine();
                if ( str != null ) {
                    builder.append( str );
                }
                firstLine = false;
            }

        } finally {
            in.close();
            inputStream.close();
        }
        return builder.toString();
    }
}
