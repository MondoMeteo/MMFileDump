/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mondometeo.data.dump;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import net.staniscia.odynodatabus.msg.Envelop;
import net.staniscia.odynodatabus.msg.SerializableMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mondometeo.common.MeteoFrame;
import org.mondometeo.common.MeteoFrameFactory;
import org.mondometeo.data.MockData;
import org.mondometeo.data.dump.dumper.JavaBinaryDump;

/**
 *
 * @author odyssey
 */
public class JavaBinaryDumpTest {

    public JavaBinaryDumpTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of deserialize method, of class JavaBinaryDump.
     */
    @Test
    public void testDeserializeDeserialize() throws MalformedURLException, IOException {
        File f = File.createTempFile("test_", ".bin");
        f.deleteOnExit();
        //MeteoFrame expResult = new MeteoFrame(System.currentTimeMillis(), new GeoSector(42.0f, 41.10f, 16.0f, 15.0f));
        MeteoFrame expResult = MockData.getMeteoFrame();
        JavaBinaryDump.serialize(expResult, f.toURI().toURL());
        MeteoFrame result = JavaBinaryDump.deserialize(f.toURI().toURL());

    }

    /**
     * Test of handle method, of class JavaBinaryDump.
     */
    @Test
    public void testHandle() throws IOException {
        Envelop<MeteoFrame> dataSample = SerializableMessage.make(MockData.getMeteoFrame());
        JavaBinaryDump instance = new JavaBinaryDump(MockData.getTempDir());
        instance.handle(dataSample);
    }
}
