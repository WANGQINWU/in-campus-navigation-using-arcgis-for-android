package com.example.tsing.map;

import com.esri.core.geometry.Point;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Tsing on 22/04/2017.
 */
public class JunctionTest {
    private Junction junc=new Junction(1,2);
    @Test
    public void setIndex() throws Exception {
        junc.setIndex(0);
        assertEquals(0,junc.getIndex());

    }

    @Test
    public void setLongitute() throws Exception {
        junc.setLongitute(3);
        assertEquals(3.0,junc.longitute);
    }

    @Test
    public void setLatitude() throws Exception {
        junc.setLatitude(4);
        assertEquals(4.0,junc.latitude);
    }

    @Test
    public void getIndex() throws Exception {
        assertEquals(0,junc.getIndex());

    }

    @Test
    public void getPoint() throws Exception {
        assertEquals(new Point(1,2),junc.getPoint());

    }


    @Test
    public void getDistance() throws Exception {
        assertEquals(0.0,junc.getDistance(new Point(1,2)));

    }

}