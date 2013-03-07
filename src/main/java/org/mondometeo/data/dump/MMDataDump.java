/*  
 Copyright 2012  Alessandro Staniscia ( alessandro@staniscia.net )

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License, version 2, as
 published by the Free Software Foundation.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.mondometeo.data.dump;

import java.io.File;
import net.staniscia.odynodatabus.DataBusService;
import org.mondometeo.data.dump.dumper.JavaBinaryDump;
import org.mondometeo.data.dump.dumper.LSTRasterFile;
import org.mondometeo.data.dump.dumper.LSTShapeFile;
import org.mondometeo.data.dump.dumper.MatlabGridFiles;
import org.mondometeo.data.dump.dumper.SimpleShapeFile;

/**
 * The Class MMDataDump.
 */
public class MMDataDump {

    /**
     * The data base service.
     */
    private DataBusService dataBaseService;
    /**
     * The file data dump.
     */
    //private final MatlabGridFiles matlabGridFiles;
    private final JavaBinaryDump binaryDump;
    private final SimpleShapeFile simpleShapeFile;
    private final LSTRasterFile lSTRasterFile;
    private final LSTShapeFile lSTShapeFile;

    /**
     * The Constructor.
     *
     * @param path the path
     */
    public MMDataDump(String path) {
        super();
        if (path == null) {
            path = System.getProperty("java.deflault.tempdir");
        }
        //this.matlabGridFiles = new MatlabGridFiles(path);
        this.binaryDump = new JavaBinaryDump(path);
        this.simpleShapeFile = new SimpleShapeFile(path+File.pathSeparator+"shapes");
        this.lSTShapeFile = new LSTShapeFile(path+File.pathSeparator+"shapes",30f);        
        this.lSTRasterFile = new LSTRasterFile(path,30f);
        
    }

    /**
     * Sets the data bus service.
     *
     * @param dataBaseService the data bus service
     */
    public void setDataBusService(DataBusService dataBaseService) {
        this.dataBaseService = dataBaseService;
        //dataBaseService.registerSubscriber(matlabGridFiles);
        dataBaseService.registerSubscriber(binaryDump);
        dataBaseService.registerSubscriber(simpleShapeFile);
        dataBaseService.registerSubscriber(lSTShapeFile);
        dataBaseService.registerSubscriber(lSTRasterFile);
    }

    /**
     * Unset data bus service.
     *
     * @param dataBaseService the data base service
     */
    public void unsetDataBusService(DataBusService dataBaseService) {
        //dataBaseService.unRegisterSubscriber(matlabGridFiles.getIdentification());
        dataBaseService.unRegisterSubscriber(binaryDump.getIdentification());
        dataBaseService.unRegisterSubscriber(simpleShapeFile.getIdentification());
        dataBaseService.unRegisterSubscriber(lSTShapeFile.getIdentification());
        dataBaseService.unRegisterSubscriber(lSTRasterFile.getIdentification());
        this.dataBaseService = null;
    }
}
