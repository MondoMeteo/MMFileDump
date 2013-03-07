/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mondometeo.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.mondometeo.common.MeteoFrame;
import org.mondometeo.common.MeteoFrameFactory;
import org.mondometeo.data.dump.SimpleShapeFileTest;

public class MockData {

    public static MeteoFrame getMeteoFrame() throws IOException {
        //MeteoFrameFactory
        URL AIT = SimpleShapeFileTest.class.getClassLoader().getResource("AIT.txt");
        URL CLM = SimpleShapeFileTest.class.getClassLoader().getResource("CLM.txt");
        URL LST = SimpleShapeFileTest.class.getClassLoader().getResource("LST.txt");
        URL RR = SimpleShapeFileTest.class.getClassLoader().getResource("RR.txt");
        return MeteoFrameFactory.makeMeteoFrame(48.0f, 35.5f, 18f, 7.0f, System.currentTimeMillis(),
                275, 344, getBuffReader(CLM), getBuffReader(LST), getBuffReader(RR), getBuffReader(AIT));

    }

    private static BufferedReader getBuffReader(URL cloudContentUrl) throws IOException {
        return new BufferedReader(new InputStreamReader(cloudContentUrl.openStream()));
    }

    public static String getTempDir() throws IOException {
        return System.getProperty("java.deflault.tempdir");
    }
}
