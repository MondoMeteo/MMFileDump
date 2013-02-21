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

import net.staniscia.odynodatabus.DataBusService;


/**
 * The Class MMDataDump.
 */
public class MMDataDump {
	
	
	
	/** The data base service. */
	private DataBusService dataBaseService;
	
	/** The file data dump. */
	private FileDataDumper fileDataDump;

	/**
	 * The Constructor.
	 *
	 * @param path the path
	 */
	public MMDataDump(String path) {
		super();
		this.fileDataDump = new FileDataDumper(path);
	}

	/**
	 * Sets the data bus service.
	 *
	 * @param dataBaseService the data bus service
	 */
	public void setDataBusService(DataBusService dataBaseService) {
		this.dataBaseService = dataBaseService;
		dataBaseService.registerSubscriber(fileDataDump);
	}
	
	/**
	 * Unset data bus service.
	 *
	 * @param dataBaseService the data base service
	 */
	public void unsetDataBusService(DataBusService dataBaseService){
		dataBaseService.unRegisterSubscriber(fileDataDump);
		this.dataBaseService = null;
	}

}
