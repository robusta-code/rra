package io.robusta.rra.representation.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * @author Nicolas Zozol
 *
 */
@Provider
@Consumes( "application/json" )
@Produces( "text/plain" )
public class GsonJaxRsProvider implements MessageBodyReader<GsonRepresentation>, MessageBodyWriter<GsonRepresentation> {

    @Override
    public boolean isReadable( Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType ) {
        return aClass == GsonRepresentation.class;
    }

    @Override
    public GsonRepresentation readFrom( Class<GsonRepresentation> gsonRepresentationClass, Type type,
            Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> stringStringMultivaluedMap,
            InputStream inputStream ) throws IOException, WebApplicationException {
        try {
            JAXBContext ctx = JAXBContext.newInstance( gsonRepresentationClass );
            return (GsonRepresentation) ctx.createUnmarshaller().unmarshal( inputStream );
        } catch ( JAXBException e ) {
            e.printStackTrace();
            throw new RuntimeException( e );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.ws.rs.ext.MessageBodyWriter#isWriteable(java.lang.Class,
     * java.lang.reflect.Type, java.lang.annotation.Annotation[],
     * javax.ws.rs.core.MediaType)
     */
    @Override
    public boolean isWriteable( Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType ) {
        return type == GsonRepresentation.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.ws.rs.ext.MessageBodyWriter#getSize(java.lang.Object,
     * java.lang.Class, java.lang.reflect.Type,
     * java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)
     */
    @Override
    public long getSize( GsonRepresentation representation, Class<?> aClass, Type type, Annotation[] annotations,
            MediaType mediaType ) {
        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.ws.rs.ext.MessageBodyWriter#writeTo(java.lang.Object,
     * java.lang.Class, java.lang.reflect.Type,
     * java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType,
     * javax.ws.rs.core.MultivaluedMap, java.io.OutputStream)
     */
    @Override
    public void writeTo( GsonRepresentation representation, Class<?> aClass, Type type, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> stringObjectMultivaluedMap, OutputStream outputStream )
            throws IOException, WebApplicationException {
        try {
            JAXBContext ctx = JAXBContext.newInstance( aClass );
            ctx.createMarshaller().marshal( representation, outputStream );
        } catch ( JAXBException e ) {
            throw new RuntimeException( e );
        }
    }
}