/**
 * Created by minhvu on 3/6/17.
 */
import java.util.ArrayList;
import java.util.Date;

public class neighboursOfIntersection {

    public String timestamp = "";
    public String direction = "";
    public Double latitide = 0.0;
    public Double longitude = 0.0;
    public String Id = "";
    public int TripSegment = 0;
    public int QuarterCircleBelong = 0;

    public neighboursOfIntersection(String Id, Double latitude, Double longitude
                                    , int tripSegment, String timestamp, String direction) {
        this.timestamp = timestamp;
        this.direction = direction;
        this.longitude = longitude;
        this.latitide = latitude;
        this.Id = Id;
        this.TripSegment = tripSegment;

        int course = Integer.parseInt(this.direction);
        if (0 <= course && course <= 90) {
            this.QuarterCircleBelong = 1;
        } else if (90 < course && course <= 180) {
            this.QuarterCircleBelong = 2;
        } else if (180 < course && course <= 270) {
            this.QuarterCircleBelong = 3;
        } else if (270 < course && course <= 360) {
            this.QuarterCircleBelong = 4;
        }

    }

    public String getTimestamp() {return timestamp;}

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDirection() {return direction;}

    public void setDirection(String direction) {this.direction = direction;}

    public Double getLatitude() {return latitide;    }

    public void setLatitude(Double latitude) {this.latitide = latitude;}

    public Double getLongitude() {return longitude;}
    public void setLongitude(Double longitude) {this.longitude = longitude;}

    public String getId() {return Id;}
    public void setId(String id) {this.Id = id;}

    public int getTripSegment() {return TripSegment;}
    public void setTripSegment(int tripSegment) {this.TripSegment = tripSegment;}

    public int getQuarterCircleBelong() {return QuarterCircleBelong;}
    public void setQuarterCircleBelong(int quarterCircleBelong) {this.QuarterCircleBelong = quarterCircleBelong;}
}

class ListOfNeighbors {
    ArrayList<neighboursOfIntersection> listOfNeighBor = new ArrayList<neighboursOfIntersection>();

}
