package com.example.tsing.map;

import com.esri.core.geometry.Point;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Tsing on 22/04/2017.
 */
public class navigationfuncTest {
    private navigationfunc func;
    @Before
    public void setUp() throws Exception {
        func=new navigationfunc();
    }

    @Test
    public void judgearea() throws Exception {
        int result =func.judgearea(100,10);
        assertEquals(0, result);
    }

    @Test
    public void collect() throws Exception {
        InputStream is =new FileInputStream("/Users/Tsing/AndroidStudioProjects/map/app/src/main/assets/junction11");
        List<Junction> junc=func.collect(is);
        List<Junction> test=new ArrayList<Junction>();

        assertEquals(test,junc);
    }

    @Test
    public void judgedist() throws Exception {
        List<Junction> junc =new ArrayList<Junction>();
        junc.add(new Junction(1,1));
        assertEquals(new Point(1,1),  func.judgedist(junc, 1,1));

    }

    @Test
    public void collectinfo() throws Exception {
        InputStream is = new FileInputStream("/Users/Tsing/AndroidStudioProjects/map/app/src/main/assets/schoolinfo");
        WLocation result=func.collectinfo(is,"Admission");
        WLocation test =new WLocation("Admssion", "A201", 120.64853491198981, 27.92085409874846, 0, 0);
        assertEquals(test.getLatitude(),result.getLatitude());
    }

}