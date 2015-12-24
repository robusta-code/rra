package io.robusta.rra.cache.http;

/**
 * Created by Nicolas Zozol on 10/04/15
 */
import io.robusta.rra.security.implementation.MD5;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Easily create an etag using by default SHA-1 on the String.valueOf(objects)
 */
public class EtagFactory {

    static EtagFactory instance = new EtagFactory();


    /**
     * Returns an ETag with SHA-1 if possible, or MD5 if not
     * @param inputs : inputs that will change the ETag result
     * @return the etag
     */
    public static String getEtag(Object... inputs){

        try{
            return instance.fastDigest(inputs);
        }catch (Exception e){//nio or jaxb not there

            return MD5.encodeMD5(instance.appendStrings(inputs).toString());
        }
    }

    protected StringBuilder appendStrings(Object... inputs){
        int initialCapacity = 0;
        for (Object input : inputs) {
            initialCapacity += String.valueOf(input).length();
        }
        StringBuilder builder = new StringBuilder(initialCapacity);
        for (Object  input : inputs) {
            builder.append(String.valueOf(input));
        }
        return builder;
    }

    protected String fastDigest(Object... inputs) {

        final java.nio.ByteBuffer buf = java.nio.charset.StandardCharsets.UTF_8
                .encode(appendStrings(inputs).toString());

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA1");
            buf.mark();
            digest.update(buf);
            buf.reset();
            return DatatypeConverter.printHexBinary(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
