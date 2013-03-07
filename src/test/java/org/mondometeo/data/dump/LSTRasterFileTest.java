/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mondometeo.data.dump;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import net.staniscia.odynodatabus.msg.Envelop;
import net.staniscia.odynodatabus.msg.SerializableMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mondometeo.common.MeteoFrame;
import org.mondometeo.common.MeteoFrameFactory;
import org.mondometeo.data.MockData;
import org.mondometeo.data.dump.dumper.LSTRasterFile;
import org.mondometeo.data.dump.dumper.LSTShapeFile;
import org.mondometeo.data.dump.dumper.SimpleShapeFile;

/**
 *
 * @author odyssey
 */
public class LSTRasterFileTest {
    private SerializableMessage<MeteoFrame> dataSample;

    public LSTRasterFileTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException {
       dataSample = SerializableMessage.make(MockData.getMeteoFrame());
    }

    @After
    public void tearDown() {
        dataSample = null;
    }


    @Test
    public void testLSTRasterFile() throws IOException {
        LSTRasterFile instance = new LSTRasterFile(MockData.getTempDir(), 10);
        instance.handle(dataSample);
    }
}
