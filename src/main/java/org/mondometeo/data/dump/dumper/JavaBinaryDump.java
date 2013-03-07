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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.staniscia.odynodatabus.DataBusServiceStatus;
import net.staniscia.odynodatabus.Subscriber;
import net.staniscia.odynodatabus.filters.Filter;
import net.staniscia.odynodatabus.filters.FilterFactory;
import net.staniscia.odynodatabus.msg.Envelop;
import org.mondometeo.common.MeteoFrame;
import org.mondometeo.common.formatter.MeteoFormatter;

// TODO: Auto-generated Javadoc
/**
 * The Class MatlabGridFiles.
 */
public class JavaBinaryDump implements Subscriber<MeteoFrame, Filter<MeteoFrame>> {

    @Override
    public String getIdentification() {
        return "Java-Binary-Dumper";
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
    public JavaBinaryDump(String dirPath) {
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
        if (dataSample.getContent() != null) {
            try {
                MeteoFrame e = dataSample.getContent();
                String name=MeteoFormatter.getFileName(e,"MeteoFrame")+".bin";
                File u=new File(dirPath);
                URL file=u.toURI().resolve(name).toURL();
                serialize(e,file);
            }  catch (MalformedURLException ex) {
                Logger.getLogger(JavaBinaryDump.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    public static MeteoFrame deserialize(URL file) {
        MeteoFrame e = null;
        try {
            FileInputStream fileIn = new FileInputStream(file.getFile());
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (MeteoFrame) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            return null;
        }
        return e;
    }

    public static void serialize(MeteoFrame e, URL file) {
        try {
            final FileOutputStream fileOut = new FileOutputStream(file.getFile());
            final ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(e);
            out.close();
            fileOut.close();
        } catch (Exception i) {
            i.printStackTrace();
        }
    }


}
