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
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.staniscia.odynodatabus.DataBusServiceStatus;
import net.staniscia.odynodatabus.Subscriber;
import net.staniscia.odynodatabus.filters.Filter;
import net.staniscia.odynodatabus.filters.FilterFactory;
import net.staniscia.odynodatabus.msg.Envelop;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.mondometeo.common.MeteoFrame;
import org.mondometeo.common.elementary.MeteoData;
import org.mondometeo.common.formatter.MeteoFormatter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

// TODO: Auto-generated Javadoc
/**
 * The Class MatlabGridFiles.
 */
public class LSTShapeFile implements Subscriber<MeteoFrame, Filter<MeteoFrame>> {

    private final float delta;

    @Override
    public String getIdentification() {
        return "Simple-Shape-Dumper";
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
    public LSTShapeFile(String dirPath, float delta) {
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
                String name = MeteoFormatter.getFileName(e, "MeteoLST") + ".shp";
                File u = new File(dirPath);
                URL file = u.toURI().resolve(name).toURL();
                createShapeFile(e, file);
            } catch (MalformedURLException ex) {
                Logger.getLogger(LSTShapeFile.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SchemaException ex) {
                Logger.getLogger(LSTShapeFile.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(LSTShapeFile.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(LSTShapeFile.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(LSTShapeFile.class.getName()).log(Level.SEVERE, null, ex);
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

    private void createShapeFile(MeteoFrame reader, URL file) throws SchemaException, URISyntaxException, IOException, Exception {


        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        SortedMap<Float, List<Coordinate>> theIsobar = new TreeMap<Float, List<Coordinate>>();

        for (Iterator<MeteoData> it = reader.iterator(); it.hasNext();) {
            MeteoData meteodata = it.next();
            double latitude = Double.parseDouble("" + meteodata.getLatitude());
            double longitude = Double.parseDouble("" + meteodata.getLongitude());
            float lst = meteodata.getLst();
            if (lst > -60f) {
                addElementToSet(theIsobar, lst, longitude, latitude);
            }
        }




        final SimpleFeatureType TYPE = createLSTFeatureType();
        List<SimpleFeature> features = new ArrayList<SimpleFeature>();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

        float last = -6000f;

        for (Iterator<Float> it = theIsobar.keySet().iterator(); it.hasNext();) {
            float lst = it.next();
            if ( ((lst % delta) == 0)){
            List<Coordinate> isobar = theIsobar.get(lst);
            for (Coordinate coordinate : isobar) {
                Point line = geometryFactory.createPoint(coordinate);
                featureBuilder.add(line);
                featureBuilder.add(lst);
                SimpleFeature feature = featureBuilder.buildFeature(null);
                features.add(feature);
            }
            last = lst;
            }
        }

        storeToShapeFormat(file, TYPE, features);
        
    }

    /**
     * Here is how you can use a SimpleFeatureType builder to create the schema
     * for your shapefile dynamically. <p> This method is an improvement on the
     * code used in the main method above (where we used
     * DataUtilities.createFeatureType) because we can set a Coordinate
     * Reference System for the FeatureType and a a maximum field length for the
     * 'name' field dddd
     */
    private static SimpleFeatureType createLSTFeatureType() {

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Location");
        builder.setCRS(DefaultGeographicCRS.WGS84); // <- Coordinate reference system

        // add attributes in order
        builder.add("line", Point.class);
        builder.length(15).add("value", Float.class); // <- 15 chars width for name field

        // build the type
        final SimpleFeatureType LOCATION = builder.buildFeatureType();

        return LOCATION;
    }

    private void storeToShapeFormat(URL file, final SimpleFeatureType TYPE, List<SimpleFeature> features) throws Exception, IOException, URISyntaxException {
        /*
         * Get an output file name and create the new shapefile
         */
        File newFile = new File(file.toURI());

        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", file);
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
        newDataStore.createSchema(TYPE);

        /*
         * You can comment out this line if you are using the createFeatureType method (at end of
         * class file) rather than DataUtilities.createType
         */
        newDataStore.forceSchemaCRS(DefaultGeographicCRS.WGS84);

        /*
         * Write the features to the shapefile
         */
        Transaction transaction = new DefaultTransaction("create");

        String typeName = newDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);

        if (featureSource instanceof SimpleFeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

            /*
             * SimpleFeatureStore has a method to add features from a
             * SimpleFeatureCollection object, so we use the ListFeatureCollection
             * class to wrap our list of features.
             */
            SimpleFeatureCollection collection = new ListFeatureCollection(TYPE, features);
            featureStore.setTransaction(transaction);
            try {
                featureStore.addFeatures(collection);
                transaction.commit();

            } catch (Exception problem) {
                problem.printStackTrace();
                transaction.rollback();
            } finally {
                transaction.close();
            }
        } else {
            throw new Exception(typeName + " does not support read/write access");
        }
    }

    private void addElementToSet(SortedMap<Float, List<Coordinate>> theIsobar, float lst, double longitude, double latitude) {
        List<Coordinate> list = null;
        if (!theIsobar.containsKey(lst)) {
            list = new ArrayList<Coordinate>();
            theIsobar.put(lst, list);
        } else {
            list = theIsobar.get(lst);
        }
        list.add(new Coordinate(longitude, latitude));
    }
}
