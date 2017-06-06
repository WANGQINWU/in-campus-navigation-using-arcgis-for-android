package com.example.tsing.map;

import android.hardware.SensorManager;
import android.location.Location;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;

/**
 * Created by Tsing on 16/04/2017.
 */

public class Function {


    public Point updata(Location location) {
        Graphic graphic = null;
        Point mapPoint = null;
        if (location != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("实时的位置信息:\n");
            sb.append("经度:");
            sb.append(location.getLongitude());
            sb.append("\n纬度:");
            sb.append(location.getLatitude());
            sb.append("\b高度:");
            sb.append(location.getAltitude());
            sb.append("\n速度：");
            sb.append(location.getSpeed());
            sb.append("\n方向：");
            sb.append(location.getBearing());
            System.out.println(sb.toString());
            Point wgsPoint = new Point(location.getLongitude(), location.getLatitude());
            mapPoint = (Point) GeometryEngine.project(wgsPoint, SpatialReference.create(4490), SpatialReference.create(4490));

        }

        return mapPoint;

    }

    public float updateOrientationAngles(float[] mAccelerometerReading, float[] mMagnetometerReading
                                         ) {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, mAccelerometerReading,
                mMagnetometerReading);
        SensorManager.getOrientation(R, values);
        values[0] = (float) Math.toDegrees(values[0]);
        return values[0];
    }



}
