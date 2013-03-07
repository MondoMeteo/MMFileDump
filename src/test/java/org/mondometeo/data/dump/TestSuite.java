/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mondometeo.data.dump;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author odyssey
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    org.mondometeo.data.dump.LSTShapeFileTest.class, 
    org.mondometeo.data.dump.JavaBinaryDumpTest.class, 
    org.mondometeo.data.dump.SimpleShapeFileTest.class, 
    org.mondometeo.data.dump.LSTRasterFileTest.class})
public class TestSuite {
    
}
