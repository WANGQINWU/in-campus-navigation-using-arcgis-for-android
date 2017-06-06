package com.example.tsing.map;

import android.support.test.runner.AndroidJUnit4;

import com.esri.core.geometry.Point;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void TestuseAppContext() throws Exception {
        // Context of the app under test.
        navigationfunc func =new navigationfunc();
        int result =func.judgearea(100,10);
        assertEquals(0,result);
    }
    @Test
    public void Testjudgedis() throws Exception {
        // Context of the app under test.
        navigationfunc func =new navigationfunc();
        List<Junction> junc=new ArrayList<Junction>();
        junc.add(new Junction(1,0));
        junc.add(new Junction(1,3));
        Point result =func.judgedist(junc,1,1);
        Point point=new Point(1,0);
        assertEquals(point,result);
    }


    public void Testcollectinfo() throws Exception {
        // Context of the app under test.
        navigationfunc func =new navigationfunc();
        InputStream is = new FileInputStream("schoolinfo");
        WLocation ad=func.collectinfo(is,"Admission");
        WLocation test=new WLocation("IT","D205",0,0,0,0);
        assertEquals(test,ad);
    }

}
