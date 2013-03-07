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
package org.mondometeo.data.dump.dumper;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.staniscia.odynodatabus.DataBusServiceStatus;
import net.staniscia.odynodatabus.Subscriber;
import net.staniscia.odynodatabus.filters.Filter;
import net.staniscia.odynodatabus.filters.FilterFactory;
import net.staniscia.odynodatabus.msg.Envelop;

import org.mondometeo.common.MeteoFrame;
import org.mondometeo.common.elementary.MeteoData;
import org.mondometeo.common.formatter.MeteoFormatter;

// TODO: Auto-generated Javadoc
/**
 * The Class MatlabGridFiles.
 */
public class MatlabGridFiles implements Subscriber<MeteoFrame, Filter<MeteoFrame>> {

    @Override
    public String getIdentification() {
        return "Matlab-Matrix-Dumper";
    }
    /**
     * The dir path.
     */
    private String dirPath;

    /**
     * The Constructor.
     *
     * @param dirPath the dir path
     */
    public MatlabGridFiles(String dirPath) {
        super();
        if (dirPath == null) {
            dirPath = System.getProperty("java.deflault.tempdir");
        }
        this.dirPath = dirPath;
    }

    /* (non-Javadoc)
     * @see net.staniscia.odynodatabus.Subscriber#handle(net.staniscia.odynodatabus.msg.Envelop)
     */
    @Override
    public void handle(Envelop<MeteoFrame> dataSample) {
        try {
            Date data = new Date();
            data.setTime(dataSample.getTimeOfOccurence());
            MeteoFrame mmData = dataSample.getContent();

            File u = new File(dirPath);
            String nameAIT = MeteoFormatter.getFileName(mmData, "AIT") + ".txt";
            URL fileAIT = u.toURI().resolve(nameAIT).toURL();

            String nameCLM = MeteoFormatter.getFileName(mmData, "CLM") + ".txt";
            URL fileCLM = u.toURI().resolve(nameCLM).toURL();

            String nameLST = MeteoFormatter.getFileName(mmData, "LST") + ".txt";
            URL fileLST = u.toURI().resolve(nameLST).toURL();

            String nameRR = MeteoFormatter.getFileName(mmData, "RR") + ".txt";
            URL fileRR = u.toURI().resolve(nameRR).toURL();


            for (Iterator<MeteoData> iterator = mmData.iterator(); iterator.hasNext();) {
                MeteoData meteo = iterator.next();
                
                //TODO
                
            }

            File f = new File(this.dirPath);
        } catch (MalformedURLException ex) {
            Logger.getLogger(MatlabGridFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* (non-Javadoc)
     * @see net.staniscia.odynodatabus.Subscriber#getFilter()
     */
    @Override
    public Filter<MeteoFrame> getFilter() {
        return FilterFactory.makeNoFilter(new MeteoFrame(0, null));
    }

    /* (non-Javadoc)
     * @see net.staniscia.odynodatabus.Subscriber#onChangeSystemStatus(net.staniscia.odynodatabus.DataBusServiceStatus)
     */
    @Override
    public void onChangeSystemStatus(DataBusServiceStatus status) {
    }
}
