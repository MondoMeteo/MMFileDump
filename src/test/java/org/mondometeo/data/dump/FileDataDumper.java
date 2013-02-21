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
import java.util.Date;
import java.util.Iterator;

import net.staniscia.odynodatabus.DataBusServiceStatus;
import net.staniscia.odynodatabus.Subscriber;
import net.staniscia.odynodatabus.filters.Filter;
import net.staniscia.odynodatabus.filters.FilterFactory;
import net.staniscia.odynodatabus.msg.Envelop;

import org.mondometeo.common.MeteoFrame;
import org.mondometeo.common.elementary.MeteoData;

// TODO: Auto-generated Javadoc
/**
 * The Class FileDataDumper.
 */
public class FileDataDumper implements Subscriber<MeteoFrame, Filter<MeteoFrame>> {
	
	/** The dir path. */
	private String dirPath;

	/**
	 * The Constructor.
	 *
	 * @param dirPath the dir path
	 */
	public FileDataDumper(String dirPath) {
		super();
		if (dirPath == null){
			dirPath = System.getProperty("java.deflault.tempdir");
		}
		this.dirPath = dirPath;		
	}

	/* (non-Javadoc)
	 * @see net.staniscia.odynodatabus.Subscriber#handle(net.staniscia.odynodatabus.msg.Envelop)
	 */
	@Override
	public void handle(Envelop<MeteoFrame> dataSample) {
         Date data=new Date();
         data.setTime(dataSample.getTimeOfOccurence());
         MeteoFrame mmData = dataSample.getContent();
         mmData.getDate();
         
         for (Iterator<MeteoData> iterator = mmData.iterator(); iterator.hasNext();) {
        	 MeteoData meteo =  iterator.next();
			
		}
         
         File f = new File(this.dirPath);
		
	}

	/* (non-Javadoc)
	 * @see net.staniscia.odynodatabus.Subscriber#getFilter()
	 */
	@Override
	public Filter<MeteoFrame> getFilter() {
		return FilterFactory.makeNoFilter(new MeteoFrame(0,null));
	}

	/* (non-Javadoc)
	 * @see net.staniscia.odynodatabus.Subscriber#onChangeSystemStatus(net.staniscia.odynodatabus.DataBusServiceStatus)
	 */
	@Override
	public void onChangeSystemStatus(DataBusServiceStatus status) {
	   //NONE
	}

}
