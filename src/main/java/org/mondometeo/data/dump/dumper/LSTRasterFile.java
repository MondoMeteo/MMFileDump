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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.staniscia.odynodatabus.DataBusServiceStatus;
import net.staniscia.odynodatabus.Subscriber;
import net.staniscia.odynodatabus.filters.Filter;
import net.staniscia.odynodatabus.filters.FilterFactory;
import net.staniscia.odynodatabus.msg.Envelop;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.mondometeo.common.MeteoFrame;
import org.mondometeo.common.formatter.MeteoFormatter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

// TODO: Auto-generated Javadoc
/**
 * The Class MatlabGridFiles.
 */
public class LSTRasterFile implements Subscriber<MeteoFrame, Filter<MeteoFrame>> {

    private final float delta;

    @Override
    public String getIdentification() {
        return "Simple-Raster-Dumper";
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
    public LSTRasterFile(String dirPath, float delta) {
        super();
        if (dirPath == null) {
            dirPath = System.getProperty("java.deflault.tempdir");
        }
        this.delta = delta;
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
                String name = MeteoFormatter.getFileName(e, "MeteoLST") + ".tiff";
                File u = new File(dirPath);
                URL file = u.toURI().resolve(name).toURL();
                createRasterFile(e, file);
            } catch (MalformedURLException ex) {
                Logger.getLogger(LSTRasterFile.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SchemaException ex) {
                Logger.getLogger(LSTRasterFile.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(LSTRasterFile.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(LSTRasterFile.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(LSTRasterFile.class.getName()).log(Level.SEVERE, null, ex);
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

    private void createRasterFile(MeteoFrame reader, URL file) throws Exception  {
        float[][] matrix = MeteoFormatter.getLSTMatrix(reader, reader.getNrows(), reader.getNcols());
        GridCoverageFactory builder = new GridCoverageFactory();
        Envelope2D envelop = new Envelope2D(CRS.decode("EPSG:4326"),
                Double.parseDouble("" + reader.getSector().getWest()),
                Double.parseDouble("" + reader.getSector().getEast()),
                Double.parseDouble("" + reader.getSector().getNorth()),
                Double.parseDouble("" + reader.getSector().getSouth()));
        GridCoverage2D orgCov = builder.create("LST", matrix, envelop);




        final GeoTiffWriteParams wp = new GeoTiffWriteParams();
        wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
        wp.setCompressionType("LZW");
        wp.setCompressionQuality(0.75F);
        wp.setTilingMode(GeoToolsWriteParams.MODE_DEFAULT);
        

        // TODO check file prior to writing
        GeoTiffWriter writer = new GeoTiffWriter(file);

        // setting the write parameters for this geotiff
        final ParameterValueGroup params = new GeoTiffFormat().getWriteParameters();
        params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
        
        final GeneralParameterValue[] wps = (GeneralParameterValue[]) params.values().toArray(
                new GeneralParameterValue[1]);
        try {
            writer.write(orgCov, wps);
        } finally {
            try {
                writer.dispose();
            } catch (Exception e) {
                // we tried, no need to fuss around this one
            }
        }

    }

}
