package io.robusta.rra.cache.http;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Nicolas Zozol on 24/12/2015.
 */
public class EtagFactoryTest {

    @Test
    public void testGetEtag() throws Exception {

        SomeData someData = new SomeData();
        String etag1 = EtagFactory.getEtag(someData);
        String etag2 = EtagFactory.getEtag(someData);

        assertEquals(etag1, etag2);
        assertNotNull(etag1);
        assertTrue(etag1.length() > 16);

        SomeData otherData = new SomeData("xyz", 48);

        etag1 = EtagFactory.getEtag(someData, otherData);
        etag2 = EtagFactory.getEtag(someData, otherData);

        assertEquals(etag1, etag2);
        assertNotNull(etag1);
        assertTrue(etag1.length() > 16);

    }

    class SomeData{

        String name ="data";
        int id = 12;

        public SomeData() {
        }

        public SomeData(String name, int id) {
            this.name = name;
            this.id = id;
        }

        @Override
        public String toString() {
            return "SomeData{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    '}';
        }
    }
}