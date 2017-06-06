package com.example.tsing.map;

import com.esri.core.geometry.Point;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Tsing on 22/04/2017.
 */
public class WLocationTest {

    WLocation loc=new WLocation("Admission","D201",1,2,3,4);

    @Test
    public void getLocation() throws Exception {

        assertEquals(new Point(1,2), loc.getLocation());
    }

    @Test
    public void getNaviLocation() throws Exception {
        assertEquals(new Point(3,4), loc.getNaviLocation());

    }

    @Test
    public void setOffice() throws Exception {
        loc.setOffice("D303");
        assertEquals("D303",loc.getOffice()) ;

    }

    @Test
    public void setOfficename() throws Exception {
        loc.setOfficename("D303");
        assertEquals("D303",loc.getOfficename()) ;
    }

    @Test
    public void setLatitude() throws Exception {
        loc.setLatitude(1);
        assertEquals(1.0,loc.getLatitude()) ;
    }

    @Test
    public void setLongitude() throws Exception {
        loc.setLongitude(1);
        assertEquals(1.0,loc.getLongitude()) ;
    }

    @Test
    public void setNavilatitude() throws Exception {
        loc.setNavilatitude(1);
        assertEquals(1.0,loc.getNavilatitude());
    }

    @Test
    public void setNavilongitude() throws Exception {
        loc.setNavilongitude(1);
        assertEquals(1.0,loc.getNavilongitude());
    }

    @Test
    public void getLatitude() throws Exception {
        assertEquals(2.0,loc.getLatitude());

    }

    @Test
    public void getLongitude() throws Exception {
        assertEquals(1.0,loc.getLongitude());

    }

    @Test
    public void getNavilatitude() throws Exception {
        assertEquals(3.0,loc.getNavilongitude());

    }

    @Test
    public void getNavilongitude() throws Exception {
        assertEquals(1.0,loc.getLongitude());

    }

    @Test
    public void getOfficename() throws Exception {
        assertEquals("Admission",loc.getOfficename());
    }

    @Test
    public void getOffice() throws Exception {
        assertEquals("D201",loc.getOffice());

    }

}