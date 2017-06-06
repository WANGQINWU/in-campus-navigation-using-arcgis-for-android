package com.example.tsing.map;

import com.esri.core.geometry.Point;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tsing on 18/04/2017.
 */

public class navigationfunc {

    navigationfunc(){

    }

    int judgearea(double longitude,double latitude){
        int index=0;
        if(longitude<=120.65197691528971 && latitude<=27.923614944263733){
            index=0;
        }else if(longitude<=120.65284740167195 && latitude <=27.923614944263733){
            index=1;
        }else if(longitude>120.65284740167195 && latitude <=27.923614944263733){
            index=2;
        }else if(longitude<=120.65197691528971 && latitude <=27.922626087247007){
            index=3;
        }else if(longitude<=120.65284740167195 && latitude <=27.922626087247007){
            index=4;
        }else if(longitude>120.65284740167195 && latitude <=27.922626087247007){
            index=5;
        }else if(longitude<=120.65197691528971 && latitude <=27.920779493978607 ){
            index=6;
        }else if(longitude<=120.65284740167195 && latitude <= 27.920779493978607 ){
            index=7;
        }else if(longitude>120.65284740167195 && latitude <= 27.920779493978607 ){
            index=8;
        }else if(longitude<=120.65197691528971 && latitude <=  27.920779493978607){
            index=9;
        }else if(longitude<=120.65284740167195 && latitude <=  27.920779493978607){
            index=10;
        }else if(longitude>120.65284740167195 && latitude <=  27.920779493978607){
            index=11;
        }
        return index;
    }

    List<Junction> collect(InputStream is) throws IOException {
        List<Junction> junc=new ArrayList<Junction>();
        try {
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(reader);

            String line = "";
            int count=0;
            double x=0;
            double y=0;
            while((line = br.readLine())!=null){
                if(count%2==0){
                    //    System.out.println(line);
                    x=Double.parseDouble(line);
                    //    System.out.println(x);
                    count++;
                }else {
                    y = Double.parseDouble(line);
                    //    System.out.println(y);
                    junc.add(new Junction(x, y));
                    count++;
                }
            }
            reader.close();
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return junc;
    }

    Point judgedist(List<Junction> junc, double longitude, double latitude){
    Point here=new Point(longitude,latitude);
    double mindis=Double.MAX_VALUE;
    Point p=new Point(longitude,latitude);
    for(Junction each:junc){
        double distance=each.getDistance(p);
        if(mindis>distance){
            mindis=distance;
            here = each.getPoint();
        }
    }

    return here;
}
    WLocation collectinfo(InputStream is,String officeinfo) throws IOException {
        WLocation offica=new WLocation();
        int isfound=0;

        try {
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(reader);

            String line = "";
            int count=0;
            while((line = br.readLine())!=null){

                if(count%4==0){
                    //    System.out.println(line);
                    offica.setOfficename(line);
                    if (line.equals(officeinfo)){
                        isfound=1;
                    }
                    //    System.out.println(x);
                }else if(count%4==1) {
                        offica.setOffice(line);
                    if (line.equals(officeinfo)){
                        isfound=1;
                    }
                    //    System.out.println(y);
                }else if(count%4==2&&isfound==1){
                    offica.setLongitude(Double.parseDouble(line));
                }else if(isfound==1){
                    offica.setLatitude(Double.parseDouble(line));
                        return offica;

                }
                count++;

            }


            reader.close();
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(isfound ==0){offica=new WLocation();}
        return offica;

}}
