import java.util.ArrayList;

/**
 * Created by minhvu on 3/6/17.
 */

public class IntersectionPoint {

    gpxPoint intersectionpoint;

    ArrayList<neighboursOfIntersection> neighbours = new ArrayList<neighboursOfIntersection>();


    public ArrayList<neighboursOfIntersection> getNeighbours() {

        return neighbours;

    }

    public void setNeighbours(ArrayList<neighboursOfIntersection> neighbours) {

        this.neighbours = neighbours;

    }

    public gpxPoint getIntersectionpoint() {

        return intersectionpoint;

    }

    public void setIntersectionpoint(gpxPoint intersectionpoint) {

        this.intersectionpoint = intersectionpoint;

    }



    public IntersectionPoint(){

        intersectionpoint = new gpxPoint();

    }






}
