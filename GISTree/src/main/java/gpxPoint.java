/**
 * Created by minhvu on 3/6/17.
 */

import java.util.Date;
public class gpxPoint  implements Comparable{




    double latitude ;



    String nameOfCar;





    double longitude ;


    double speed;

    double course;

    String direction;


    Date timestamp;


    public gpxPoint()

    {


        timestamp = new Date();


    }


    public String getNameOfCar() {

        return nameOfCar;

    }



    public void setNameOfCar(String nameOfCar) {

        this.nameOfCar = nameOfCar;

    }




    public double getLatitude() {

        return latitude;

    }



    public void setLatitude(double latitude) {

        this.latitude = latitude;

    }



    public double getLongitude() {

        return longitude;

    }



    public void setLongitude(double longitude) {

        this.longitude = longitude;

    }



    public double getSpeed() {

        return speed;

    }



    public void setSpeed(double speed) {

        this.speed = speed;

    }



    public double getCourse() {

        return course;

    }



    public void setCourse(double course) {

        this.course = course;

    }



    public String getDirection() {

        return direction;

    }



    public void setDirection(String direction) {

        this.direction = direction;

    }



    public Date getTimestamp() {

        return timestamp;

    }



    public void setTimestamp(Date timestamp) {

//System.out.println("Setting " + timestamp );

        this.timestamp = timestamp;

    }


    public boolean equals(Object object)

    {

        gpxPoint otherPoint = (gpxPoint) object;




//System.out.println("Equals called - Comparing" );

//System.out.println(this.getLatitude()+","+this.getLongitude()+",course: "+this.getCourse()+",Time:"+this.getTimestamp()+",Speed:"+this.getSpeed());

//System.out.println(otherPoint.getLatitude()+","+otherPoint.getLongitude()+",course: "+otherPoint.getCourse()+",Time:"+otherPoint.getTimestamp()+",Speed:"+otherPoint.getSpeed());

//System.out.println((this.getLatitude() == otherPoint.getLatitude()) + "-" + (this.getLongitude() == otherPoint.getLongitude()) + " - " + (this.getCourse() == otherPoint.getCourse())  + " - " + (this.getTimestamp().equals(otherPoint.getTimestamp())) + " - "+ (this.getSpeed() == otherPoint.getSpeed()));

        if(	this.getTimestamp().compareTo(otherPoint.getTimestamp()) == 0

                && (this.getCourse() == otherPoint.getCourse())

                && this.getSpeed() == otherPoint.getSpeed()

                && (this.getLatitude() == otherPoint.getLatitude())

                && (this.getTimestamp().equals(otherPoint.getTimestamp()))

                && (this.getLongitude() == otherPoint.getLongitude())

                )

        {

//System.out.println("Returning true");

            return true;

        }



//System.out.println("Returning false");

        return false;

    }


    public int compareTo(gpxPoint otherPoint) {

        final int BEFORE = -1;

        final int EQUAL = 0;

        final int AFTER = 1;





        if (this == otherPoint) return EQUAL;



        if(	this.getTimestamp().compareTo(otherPoint.getTimestamp()) == 0

                && (this.getCourse() == otherPoint.getCourse())

                && this.getSpeed() == otherPoint.getSpeed()

                && (this.getLatitude() == otherPoint.getLatitude())

                && (this.getTimestamp() == otherPoint.getTimestamp()))

        {

            return EQUAL;

        }



        return this.getTimestamp().compareTo(otherPoint.getTimestamp());

    }



    @Override

    public int compareTo(Object o) {

        gpxPoint otherPoint = (gpxPoint) o;

        if(	this.getTimestamp().compareTo(otherPoint.getTimestamp()) == 0

                && (this.getCourse() == otherPoint.getCourse())

                && this.getSpeed() == otherPoint.getSpeed()

                && (this.getLatitude() == otherPoint.getLatitude())

                && (this.getTimestamp().equals(otherPoint.getTimestamp()))

                && (this.getLongitude() == otherPoint.getLongitude())

                )

        {


            return 0;

        }

        else

        {

            return this.getTimestamp().compareTo(otherPoint.getTimestamp());

        }




    }
}
