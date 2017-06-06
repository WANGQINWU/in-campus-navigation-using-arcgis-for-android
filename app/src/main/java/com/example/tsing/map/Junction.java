package com.example.tsing.map;
import com.esri.core.geometry.Point;

/**
 * Created by Tsing on 16/04/2017.
 */

public class Junction {
     double longitute;
     double latitude;
    private int index;
    private double distance;
    private final double EARTH_RADIUS = 6378137.0;

    Junction() {

    }

    Junction(double x, double y){
        this.longitute=x;
        this.latitude=y;
    }

    void setIndex(int index){
        this.index=index;
    }

    void setLongitute(int longitute){
        this.longitute=longitute;
    }

    void setLatitude(int latitude){
        this.latitude=latitude;
    }

    int getIndex(){
        return index;
    }

    Point getPoint(){
        Point p=new Point(this.longitute,this.latitude);
        return p;}

    void print(){
        System.out.print(longitute+" , ");
        System.out.print(latitude+" , ");
        System.out.println("finish printing");

    }

    double getDistance(Point p) {
        double radLat1 = (longitute * Math.PI / 180.0);
        double radLat2 = (p.getX() * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (latitude - p.getY()) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        this.distance=s;
        return distance;
    }

}
