package com.example.tsing.map;

import com.esri.core.geometry.Point;

/**
 * Created by Tsing on 18/04/2017.
 */

public class WLocation {
    double longitude=0;
    double latitude=0;
    double navilongitude=0;
    double navilatitude=0;
    String Officename;
    String Office;
    WLocation(){

    }
    WLocation(String Officename, String Office,double longitude,double latitude,double navilongitude,double navilatitude){
        this.latitude=latitude;
        this.longitude=longitude;
        this.navilatitude=navilatitude;
        this.navilongitude=navilongitude;
        this.Office=Office;
        this.Officename=Officename;
    }
    Point getLocation(){
        return new Point(longitude,latitude);
    }
    Point getNaviLocation(){
        return new Point(navilongitude,navilatitude);
    }

    public void setOffice(String office) {
        Office = office;
    }

    public void setOfficename(String officename) {
        Officename = officename;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setNavilatitude(double navilatitude) {
        this.navilatitude = navilatitude;
    }

    public void setNavilongitude(double navilongitude) {
        this.navilongitude = navilongitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getNavilatitude() {
        return navilatitude;
    }

    public double getNavilongitude() {
        return navilongitude;
    }

    public String getOfficename() {
        return Officename;
    }

    public String getOffice() {
        return Office;
    }
}

