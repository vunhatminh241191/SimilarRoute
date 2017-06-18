/**
 * Created by minhvu on 3/6/17.
 */
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.*;

import org.apache.commons.math.FunctionEvaluationException;
import org.neo4j.gis.spatial.EditableLayer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.gis.spatial.pipes.GeoPipeline;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.internal.EmbeddedGraphDatabase;
import com.vividsolutions.jts.geom.Coordinate;

import net.sourceforge.gpstools.GPSDings;
import net.sourceforge.gpstools.gpx.Trk;
import net.sourceforge.gpstools.TrackAnalyzer;
import net.sourceforge.gpstools.gpx.Gpx;
import net.sourceforge.gpstools.gpx.Trkseg;


public class ATB {

    private static ArrayList<String> intersectionPoints = new ArrayList<String>();
    private static ArrayList<IntersectionPoint> allIntersections = new ArrayList<IntersectionPoint>();

    private static final String DB_PATH = "/Users/minhvu/Documents/Neo4j/default.graphdb";

    private static final File DB_FILE = new File( DB_PATH ).getAbsoluteFile();

    private EmbeddedGraphDatabase graphDb = null;
    private SpatialDatabaseService spatialDb = null;
    private EditableLayer runningLayer = null;

    private static enum RelTypes implements RelationshipType { NEXT }

    private static ArrayList<Node> allNearestNodes = new ArrayList<Node>();


    public static void loadIntersectionDetails(File file)
    {
        try{
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                intersectionPoints.add(strLine);
            }
            //Close the input stream
            in.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Import the data from a GPX file. Boolean indicates whether data has been imported before
    public void addData(File file, GraphDatabaseService graphDb,SpatialDatabaseService spatialDb) throws
            IOException, FunctionEvaluationException {

        //System.out.println("Processing file"+file);
        Coordinate intersectionCoordinate = new Coordinate(-122.1354174,47.6029868);
        EditableLayer runningLayer = (EditableLayer) spatialDb.getOrCreateEditableLayer("defaultLayer");


        Gpx gpx = GPSDings.readGPX(new FileInputStream(file));
        // Start a new transaction
        Transaction tx = graphDb.beginTx();
        // Contains the record that was added previously (in order to create a relation between the new and the previous node)
        SpatialDatabaseRecord fromrecord = null;
        TrackAnalyzer analyzer = new TrackAnalyzer();
        analyzer.addAllTracks(gpx);

        for(Trk tk : gpx.getTrk())
        {
            String nameOfCar = tk.getName();
            for(int j=0; j< tk.getTrksegCount(); j++)
            {
                Trkseg tkseg = tk.getTrkseg(j);
                for (int i = 0; i < tkseg.getTrkptCount(); i++)
                {

                    // Create a new coordinate for this point
                    Coordinate to = new Coordinate(tkseg.getTrkpt(i).getLon().doubleValue(),tkseg.getTrkpt(i).getLat().doubleValue());
                    SpatialDatabaseRecord torecord = null;
                    torecord = runningLayer.add(runningLayer.getGeometryFactory().createPoint(to));
                    torecord.setProperty("lat", to.y);
                    torecord.setProperty("lon", to.x);
                    torecord.setProperty("nameOfCar", nameOfCar);
                    torecord.setProperty("time", tkseg.getTrkpt(i).getTime().toString());
                    torecord.setProperty("course", analyzer.getBearing(tkseg.getTrkpt(i).getTime()));
                    torecord.setProperty("speed", "0");
                    torecord.setProperty("occurences", 1);
                    torecord.setProperty("trksegPosition", j);
                    if (fromrecord != null && (!fromrecord.equals(torecord)))  {
                        Map<String,Object> fromProp = fromrecord.getProperties();
                        Set<String> fromPropKey = fromProp.keySet();
                        Iterator<String> itrKeys = fromPropKey.iterator();
                        Relationship next = fromrecord.getGeomNode().createRelationshipTo(torecord.getGeomNode(), RelTypes.NEXT);
                    }
                    // Previous record is put on new record
                    fromrecord = torecord;
                }
            }
        }
        for(int intersectLoop = 0;intersectLoop < intersectionPoints.size(); intersectLoop++){
            String coordinates [] = intersectionPoints.get(intersectLoop).split(",");
            String lat =		coordinates[1];
            String lon = 	coordinates[0];

            intersectionCoordinate = new Coordinate(Double.parseDouble(lon),Double.parseDouble(lat));
            System.out.println("Current coordinate"+intersectionCoordinate.x+","+intersectionCoordinate.y);

            gpxPoint intersectionGpxPoint = new gpxPoint();
            intersectionGpxPoint.setLatitude(intersectionCoordinate.x);
            intersectionGpxPoint.setLongitude(intersectionCoordinate.y);

            IntersectionPoint intersectionPoint = new IntersectionPoint();
            intersectionPoint.setIntersectionpoint(intersectionGpxPoint);

            List<SpatialDatabaseRecord> snodes = GeoPipeline.
                    startNearestNeighborLatLonSearch(runningLayer, intersectionCoordinate, 0.05).sort(
                            "OrthodromicDistance").toSpatialDatabaseRecordList();

            ArrayList<neighboursOfIntersection> neighbours = new ArrayList<neighboursOfIntersection>();

            System.out.println("Finding neighbours"+snodes.size());
            if(snodes.size() > 0 )
            {
                System.out.println("Listing neighbours" );
            }
            for(int snodeLoop = 0; snodeLoop<snodes.size(); snodeLoop++)
            {
                SpatialDatabaseRecord node = snodes.get(snodeLoop);
                neighboursOfIntersection neighboursOfIntersection = new neighboursOfIntersection(
                        node.getProperty("nameOfCar").toString(), node.getGeometry().getCoordinate().y,
                        node.getGeometry().getCoordinate().x, (Integer) node.getProperty("trksegPosition"),
                        node.getProperty("time").toString(), node.getProperty("course").toString()
                );

                neighbours.add(neighboursOfIntersection);
            }
        }
        tx.success();
        tx.close();
    }

    public static void main(String[] args) throws IOException, FunctionEvaluationException {
        try {
            System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("/Users/minhvu/Desktop/GPXoutputfile.txt")),true));
        } catch (FileNotFoundException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        System.out.println("hehehe");
        GraphDatabaseService db_old = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(DB_FILE)
                .setConfig(GraphDatabaseSettings.pagecache_memory, "4096M")
                .newGraphDatabase();
        db_old.shutdown();

        GraphDatabaseService graphDb = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(DB_FILE)
                .setConfig(GraphDatabaseSettings.pagecache_memory, "4096M")
                .newGraphDatabase();

        SpatialDatabaseService spatialDb = new SpatialDatabaseService(graphDb);

        File intersectionFile = new File("/Users/minhvu/Desktop/SimilarRouteDetection/GISTree/input.csv");
        loadIntersectionDetails(intersectionFile);

        File path = new File("/Users/minhvu/Desktop/SimilarRouteDetection/GISTree/GPX/");
        File[] files = path.listFiles();
        ATB importer = new ATB();
        // Add the first one, detailing that we have not imported data yet

        // Add the rest
        for (int i = 0; i < files.length; i++) {
            System.out.println("File name "+files[i]);
            System.out.println("importer"+importer);
            importer.addData(files[i],graphDb,spatialDb);
        }

    }

}
