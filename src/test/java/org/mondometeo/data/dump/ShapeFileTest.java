/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mondometeo.data.dump;

import org.mondometeo.data.dump.dumper.SimpleShapeFile;
import org.mondometeo.data.dump.dumper.LSTShapeFile;
import org.mondometeo.data.dump.dumper.LSTRasterFile;
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

/**
 *
 * @author odyssey
 */
public class ShapeFileTest {

    public ShapeFileTest() {
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

 



    public MeteoFrame getMeteoFrame() throws IOException {
        //MeteoFrameFactory
        // <north>48.0</north><south>36.5</south><east>18.0</east><west>7.0</west>

        File AIT = new File("/usr/Workspaces/WorkspacePersonale/MondoMeteo/repo/MMFileDump/src/test/java/AIT.txt");
        File CLM = new File("/usr/Workspaces/WorkspacePersonale/MondoMeteo/repo/MMFileDump/src/test/java/CLM.txt");
        File LST = new File("/usr/Workspaces/WorkspacePersonale/MondoMeteo/repo/MMFileDump/src/test/java/LST.txt");
        File RR = new File("/usr/Workspaces/WorkspacePersonale/MondoMeteo/repo/MMFileDump/src/test/java/RR.txt");
        return MeteoFrameFactory.makeMeteoFrame(48.0f, 35.5f, 18f, 7.0f, System.currentTimeMillis(),
                275, 344, getBuffReader(CLM),getBuffReader(LST),getBuffReader( RR),getBuffReader( AIT));
        
    }
    
    
    private BufferedReader getBuffReader(File cloudContentUrl) throws IOException{
        return new BufferedReader(new InputStreamReader( cloudContentUrl.toURI().toURL().openStream()));
    }
    
    
    
        /**
     * Test of handle method, of class JavaBinaryDump.
     */
    //@Test
    public void testHandle() throws IOException {
        Envelop<MeteoFrame> dataSample = SerializableMessage.make(getMeteoFrame());
        SimpleShapeFile instance = new SimpleShapeFile("/tmp");
        instance.handle(dataSample);
    }
    
            /**
     * Test of handle method, of class JavaBinaryDump.
     */
  //@Test
    public void testLSTSHAPEFIEL() throws IOException {
        Envelop<MeteoFrame> dataSample = SerializableMessage.make(getMeteoFrame());
        LSTShapeFile instance = new LSTShapeFile("/tmp",10);
        instance.handle(dataSample);
    }
  
    @Test
    public void testLSTSRaster() throws IOException {
        Envelop<MeteoFrame> dataSample = SerializableMessage.make(getMeteoFrame());
        LSTRasterFile instance = new LSTRasterFile("/tmp",10);
        instance.handle(dataSample);
    }
    
    
    
}
