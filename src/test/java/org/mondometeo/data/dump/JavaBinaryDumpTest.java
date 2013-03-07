/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mondometeo.data.dump;

import org.mondometeo.data.dump.dumper.JavaBinaryDump;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import net.staniscia.odynodatabus.DataBusServiceStatus;
import net.staniscia.odynodatabus.filters.Filter;
import net.staniscia.odynodatabus.msg.Envelop;
import net.staniscia.odynodatabus.msg.SerializableMessage;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mondometeo.common.MeteoFrame;
import org.mondometeo.common.MeteoFrameFactory;
import org.mondometeo.common.elementary.GeoSector;

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
     * Test of getIdentification method, of class JavaBinaryDump.
     *
     * @Test public void testGetIdentification() {
     * System.out.println("getIdentification"); JavaBinaryDump instance = null;
     * String expResult = ""; String result = instance.getIdentification();
     * assertEquals(expResult, result); // TODO review the generated test code
     * and remove the default call to fail. fail("The test case is a
     * prototype."); }
     */
    /**
     * Test of getFilter method, of class JavaBinaryDump.
     *
     * @Test public void testGetFilter() { System.out.println("getFilter");
     * JavaBinaryDump instance = null; Filter expResult = null; Filter result =
     * instance.getFilter(); assertEquals(expResult, result); // TODO review the
     * generated test code and remove the default call to fail. fail("The test
     * case is a prototype."); }
     */
    /**
     * Test of onChangeSystemStatus method, of class JavaBinaryDump.
     *
     * @Test public void testOnChangeSystemStatus() {
     * System.out.println("onChangeSystemStatus"); DataBusServiceStatus status =
     * null; JavaBinaryDump instance = null;
     * instance.onChangeSystemStatus(status); // TODO review the generated test
     * code and remove the default call to fail. fail("The test case is a
     * prototype."); }
     */
    /**
     * Test of deserialize method, of class JavaBinaryDump.
     */
    
    public void testDeserializeDeserialize() throws MalformedURLException, IOException {
        File f = File.createTempFile("test_", ".bin");
        f.deleteOnExit();
        //MeteoFrame expResult = new MeteoFrame(System.currentTimeMillis(), new GeoSector(42.0f, 41.10f, 16.0f, 15.0f));
        MeteoFrame expResult=getMeteoFrame();
        JavaBinaryDump.serialize(expResult, f.toURI().toURL());
        MeteoFrame result = JavaBinaryDump.deserialize(f.toURI().toURL());

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
    @Test
    public void testHandle() throws IOException {
        Envelop<MeteoFrame> dataSample = SerializableMessage.make(getMeteoFrame());
        JavaBinaryDump instance = new JavaBinaryDump("/tmp");
        instance.handle(dataSample);
    }
    
    
    
}
