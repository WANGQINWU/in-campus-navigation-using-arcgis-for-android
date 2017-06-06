package com.example.tsing.map;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.popup.Popup;
import com.esri.android.map.popup.PopupContainer;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.map.popup.PopupInfo;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.na.CostAttribute;
import com.esri.core.tasks.na.NAFeaturesAsFeature;
import com.esri.core.tasks.na.NetworkDescription;
import com.esri.core.tasks.na.Route;
import com.esri.core.tasks.na.RouteDirection;
import com.esri.core.tasks.na.RouteParameters;
import com.esri.core.tasks.na.RouteResult;
import com.esri.core.tasks.na.RouteTask;
import com.esri.core.tasks.na.StopGraphic;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.makeText;
import static com.example.tsing.map.R.id.map;
import static com.example.tsing.map.R.id.textview;

public class MainActivity extends AppCompatActivity {
    //ArcGISDynamicMapServiceLayer local = new ArcGISDynamicMapServiceLayer
    //       ("http://gis.wku.edu.cn:6080/arcgis/rest/services/test001/campus/MapServer");
    final String url = "http://gis.wku.edu.cn:6080/arcgis/rest/services/test005/campus/MapServer/10";
    MapView mMapView = null;
    ArcGISTiledMapServiceLayer local =
            new ArcGISTiledMapServiceLayer("http://gis.wku.edu.cn:6080/arcgis/rest/services/test005/campus/MapServer");
    //ArcGISTiledMapServiceLayer local=new ArcGISTiledMapServiceLayer("http://192.168.20.33:6080/arcgis/rest/services/test008/campus/MapServer");

    Function func;
    int weatherflag = 0;
    int navilayoutflag = 0;
    int searlayoutflag = 0;
    int infolayoutflag = 0;
    int istour = 0;
    List<Point> tourp = new ArrayList<Point>();

    private TextView tv_label;
    private SpatialReference mapSR = null;
    int num = 0;
    private SpatialReference NASR = SpatialReference.create(4490);//4490
    private GraphicsLayer routeLayer;//查询到的整条路径
    private GraphicsLayer semLayer;//查询到的路径片段
    private double navilongitude=120.64814067567144;
    private double navilatitude=27.92176505810735;
    private int isjudged;
    private navigationfunc navifunc = new navigationfunc();
    AutoCompleteTextView to;
    AutoCompleteTextView from;

    private List<Point> stopPoints;//保存所有的停靠点
    private Symbol stopSymbol;//停靠点的符号
    private SimpleLineSymbol hiderSym;//查询到的路径的片段，设置透明将其“隐藏”
    private SimpleLineSymbol showSym;//当路径片段被选中的时候，显示的符号
    private int selectID = -1;//被选中的路径片段的ID（-1表示没有变选中）
    String naurl = "http://gis.wku.edu.cn:6080/arcgis/rest/services/test008/campus/NAServer/Route";


    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor magnetic;
    private PictureMarkerSymbol mLocationSymbol;//当前位置的图标
    AutoCompleteTextView searchbox;

    private LocationManager locationManager;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
    private int mMyUID;
    private int markerid = Integer.MAX_VALUE;
    GraphicsLayer mMylayer;

    private float predegree = 0;
    double longitude = 120.64814067567144;
    double latitude = 27.92176505810735;

    private float[] mAccelerometerReading = new float[3];
    private float[] mMagnetometerReading = new float[3];
    private MySensorEventListener sensorEventListener = new MySensorEventListener();

    PopupContainer container;
    int count;
    PopupDialog popDio;
    private Callout mCallout;
    String phonenumber;
    int buildingtype;

    Button Weather;
    Button navi;
    Button Solve;
    Button clear;
    LinearLayout navilayout;
    LinearLayout infolayout;
    LinearLayout searlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);


        mMapView = (MapView) findViewById(map);
        mMapView.enableWrapAround(true);
        //mMapView.addLayer(atl);
        mMapView.addLayer(local);

        tv_label = (TextView) findViewById(textview);
        mapSR = mMapView.getSpatialReference();
        routeLayer = new GraphicsLayer();
        semLayer = new GraphicsLayer();
        stopPoints = new ArrayList<Point>();
        mMapView.addLayer(routeLayer);
        mMapView.addLayer(semLayer);
        stopSymbol = new SimpleMarkerSymbol(Color.RED, 3, SimpleMarkerSymbol.STYLE.CIRCLE);
        hiderSym = new SimpleLineSymbol(Color.WHITE, 1);
        //设置成透明，使其处于“隐藏”状态
        hiderSym.setAlpha(100);
        showSym = new SimpleLineSymbol(Color.RED, 4);
        mMylayer = new GraphicsLayer();

        mLocationSymbol = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.ic_heading));


        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            public void onStatusChanged(Object arg0, STATUS arg1) {
                if (arg0 == mMapView && arg1 == STATUS.INITIALIZED) {
                   // mMapView.setOnSingleTapListener(new MyOnSintTapLis());
                    mMapView.setOnLongPressListener(new Longpress());
                }
            }
        });

        tourp.add(new Point(120.64943244188605, 27.924464860486935));
        tourp.add(new Point(120.64821029861928, 27.921731705736143));
        tourp.add(new Point(120.6477113514225, 27.921558756817852));
        tourp.add(new Point(120.64985195766265, 27.91952986611654));

        clear = (Button) findViewById(R.id.clearmap);
        clear.setOnClickListener(new Button_Clear());

        Button zoomin = (Button) findViewById(R.id.zoomin);
        zoomin.setOnClickListener(new Button_Zoomin());

        Button zoomout = (Button) findViewById(R.id.zoomout);
        zoomout.setOnClickListener(new Button_Zoomout());

        Button position = (Button) findViewById(R.id.position);
        position.setOnClickListener(new Button_position());

        Solve = (Button) findViewById(R.id.solve);
        Solve.setOnClickListener(new solve());

        Button data = (Button) findViewById(R.id.database);
        data.setOnClickListener(new Button_data());

        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new Button_sear());

        Button startsearch = (Button) findViewById(R.id.searchbutton);
        startsearch.setOnClickListener(new Button_startsearch());

        TextView call = (TextView) findViewById(R.id.call);
        call.setOnClickListener(new Textview_call());

        Button infobutton=(Button) findViewById(R.id.info);
        infobutton.setOnClickListener(new Button_info());

        Weather = (Button) findViewById(R.id.weather);
        Weather.setOnClickListener(new Button_Weather());

        navi = (Button) findViewById(R.id.navi);
        navi.setOnClickListener(new Button_navi());

        navilayout = (LinearLayout) findViewById(R.id.navilayout);

        searlayout = (LinearLayout) findViewById(R.id.searlayout);

        infolayout=(LinearLayout) findViewById(R.id.infolayout);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 初始化加速度传感器
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 初始化地磁场传感器
        magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        func = new Function();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_ACCESS_FINE_LOCATION);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        //获取到LocationManager对象
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //根据设置的Criteria对象，获取最符合此标准的provider对象

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 3, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                func.updata(location);
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                setLocation();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Point loc = func.updata(locationManager.getLastKnownLocation(provider));
                longitude = locationManager.getLastKnownLocation(provider).getLongitude();
                latitude = locationManager.getLastKnownLocation(provider).getLatitude();
                setLocation();
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

        //增加GPS状态监听器
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 3, new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
                // 当GPS Location Provider可用时，更新位置
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Point loc = func.updata(locationManager.getLastKnownLocation(provider));
                longitude = locationManager.getLastKnownLocation(provider).getLongitude();
                latitude = locationManager.getLastKnownLocation(provider).getLatitude();
                setLocation();


            }

            @Override
            public void onProviderDisabled(String provider) {

            }

            @Override
            public void onLocationChanged(Location location) {
                // 当GPS定位信息发生改变时，更新位置
                func.updata(location);
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                setLocation();

            }
        });

        func.updata(location);
//        longitude=location.getLongitude();
        //      latitude=location.getLatitude();
        Point p = new Point(longitude, latitude);
        Graphic locationSymbol = new Graphic(p, mLocationSymbol);
        mMyUID = mMylayer.addGraphic(locationSymbol);
        mMapView.addLayer(mMylayer);
        setLocation();


        from = (AutoCompleteTextView) findViewById(R.id.fromauto);
        to = (AutoCompleteTextView) findViewById(R.id.toauto);
        searchbox = (AutoCompleteTextView) findViewById(R.id.searchbox);

        to.requestFocus();
        //注意ArrayAdapter与SimpleAdapter的区别
        //创建一个ArrayAdapter适配器
        String[] info = getResources().getStringArray(R.array.shcoolinfo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, info);
        from.setAdapter(adapter);//为自动完成文本框设置适配器
        to.setAdapter(adapter);//为自动完成文本框设置适配器
        searchbox.setAdapter(adapter);//为自动完成文本框设置适配器

        Switch tour = (Switch) findViewById(R.id.switch2);
        tour.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked && searlayoutflag == 0 && navilayoutflag == 0) {
                    istour = 1;
                    clear.performClick();

                    for (Point tourPointinmap : tourp) {
                        stopPoints.add((Point) GeometryEngine.project(tourPointinmap, mapSR, NASR));
                    }

                    Solve.performClick();
                } else {
                    istour = 0;
                    clear.performClick();
                }

            }
        });
    }
    //oncreate

    protected void onResume() {
        super.onResume();

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        mSensorManager.registerListener(sensorEventListener,
                accelerometer, Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(sensorEventListener, magnetic,
                Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(sensorEventListener);
    }

    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(sensorEventListener);
    }

    public void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(sensorEventListener);
    }

    private class MySensorEventListener implements SensorEventListener {

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Do something here if sensor accuracy changes.
            // You must implement this callback in your code.
        }

        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, mAccelerometerReading,
                        0, mAccelerometerReading.length);
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, mMagnetometerReading,
                        0, mMagnetometerReading.length);
            }
            float degree = func.updateOrientationAngles(mAccelerometerReading, mMagnetometerReading);
            if (predegree - degree > 5 || predegree - degree < -5) {
                Point wgsPoint = new Point(longitude, latitude);
                Point mapPoint = (Point) GeometryEngine.project(wgsPoint, NASR, mapSR);
                mLocationSymbol.setAngle(degree);
                Graphic graphic = new Graphic(mapPoint, mLocationSymbol);
                mMylayer.updateGraphic(mMyUID, graphic);
                predegree = degree;
            }
        }
    }

    class MyOnSintTapLis implements OnSingleTapListener {
        public void onSingleTap(float arg0, float arg1) {

            if (semLayer.getNumberOfGraphics() == 0) {//增加停靠点
                Point mapPoint = mMapView.toMapPoint(arg0, arg1);
                routeLayer.addGraphic(new Graphic(mapPoint, stopSymbol));
                // stopPoints.add((Point) GeometryEngine.project(mapPoint, mapSR, NASR));
                //num++;
                System.out.println(mapPoint.getX() + " , " + mapPoint.getY());
            } else {//选中路径片段
                int[] ids = semLayer.getGraphicIDs(arg0, arg1, 20);
                if (ids.length > 0) {//有路径片段被选中
                    //回复已经被选中的路径片段为透明
                    semLayer.updateGraphic(selectID, hiderSym);
                    //设置新的被选中的路径片段为被选中状态
                    selectID = ids[0];
                    semLayer.updateGraphic(selectID, showSym);
                    //取得被选中的路径片段和其属性
                    Graphic selectGraphic = semLayer.getGraphic(selectID);
                    String text = (String) selectGraphic.getAttributeValue("text");
                    double time = (Double) selectGraphic.getAttributeValue("time");
                    double length = (Double) selectGraphic.getAttributeValue("length");
                    tv_label.setText("长度：" + length + "米,  时间：" + time + "分钟,描述：" + text);
                    //缩放到被选中的片段
                    mMapView.setExtent(selectGraphic.getGeometry(), 100);
                }
            }

        }
    }

    class solve implements View.OnClickListener {
        public void onClick(View v) {
            if ((to.getText().length() != 0 && from.getText().length() != 0) || istour == 1) {
                navilayout.setVisibility(View.INVISIBLE);
                navilayoutflag = 0;
                clear.setVisibility(View.VISIBLE);
                if (istour == 0) {
                    try {
                        InputStream is = getResources().getAssets().open("schoolinfo");
                        WLocation dest = navifunc.collectinfo(is, to.getText().toString());
                        if (dest.getLatitude() != 0) {
                            stopPoints.add((Point) GeometryEngine.project(dest.getLocation(), mapSR, NASR));
                            searchmarker(dest);

                        }
                        is.close();

                        String f = from.getText().toString();
                        if (from.getText().toString().equals("MyLocation") == false) {
                            InputStream isfrom = getResources().getAssets().open("schoolinfo");
                            WLocation startpoint = navifunc.collectinfo(isfrom, from.getText().toString());

                            if (startpoint.getLatitude()!= 0) {
                                stopPoints.add((Point) GeometryEngine.project(startpoint.getLocation(), mapSR, NASR));
                            }

                            isfrom.close();
                        } else {

                            int area = navifunc.judgearea(longitude, latitude);
                            if (area == 0 || area == 1 || area == 9 || area == 10) {
                                InputStream readjunction = getResources().getAssets().open("junction" + String.valueOf(area));
                                List<Junction> junc = navifunc.collect(readjunction);
                                //System.out.println("list size "+junc.size());
                                //junc.get(0).print();
                                Point close = navifunc.judgedist(junc, longitude, latitude);
                                readjunction.close();
                                navilongitude = close.getX();
                                navilatitude = close.getY();
                                isjudged = 1;
                            } else {
                                if (stopPoints.size() < 2 && isjudged == 0) {
                                    navilongitude = longitude;
                                    navilatitude = latitude;
                                }

                            }
                            Point mapPoint = new Point(navilongitude, navilatitude);
                            routeLayer.addGraphic(new Graphic(mapPoint, stopSymbol));
                            stopPoints.add((Point) GeometryEngine.project(mapPoint, mapSR, NASR));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Toast toast =Toast.makeText(getApplicationContext(), "       我正在为你翻山越岭\nthe road ahead will be long \nand our climb will be steep", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                LinearLayout toastView = (LinearLayout) toast.getView();
                ImageView imageCodeProject = new ImageView(getApplicationContext());
                imageCodeProject.setImageResource(R.drawable.ic_toast1);
                toastView.addView(imageCodeProject, 0);
                toast.show();

                new Thread() {//网络分析不能再主线程中进行
                    public void run() {
                        //准备参数

                        //String badweather = "http://sampleserver3.arcgisonline.com/ArcGIS/rest/services/Network/USA/NAServer/Route";
                        try {
                            //RouteTask rt = new RouteTask(url);
                            RouteTask rt = RouteTask.createOnlineRouteTask(naurl, null);
                            RouteParameters rp = rt.retrieveDefaultRouteTaskParameters();
                            rp.setImpedanceAttributeName("Length");

                            NAFeaturesAsFeature naferture = new NAFeaturesAsFeature();
                            NetworkDescription description = null;

                            description = rt.getNetworkDescription();
                            List<CostAttribute> costAttributes = description.getCostAttributes();
                            if (costAttributes.size() > 0)
                                rp.setImpedanceAttributeName(costAttributes.get(0).getName());

                            //设置查询停靠点，至少要两个
                            for (Point p : stopPoints) {
                                StopGraphic sg = new StopGraphic(p);
                                naferture.addFeature(sg);
                            }
                            naferture.setCompressedRequest(true);
                            rp.setStops(naferture);
                            //设置查询输入的坐标系跟底图一样
                            rp.setOutSpatialReference(mapSR);

                            //执行操作
                            RouteResult rr = rt.solve(rp);
                            rt.solve(rp);
                            isjudged = 0;
                            runOnUiThread(new MyRun(rr));

                        } catch (Exception e) {
                            Looper.prepare();
                            makeText(getApplicationContext(), "ah, check the input", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                }.start();

            }
        }

    }

    class MyRun implements Runnable {
        private com.esri.core.tasks.na.RouteResult rr;

        public MyRun(RouteResult rr) {
            this.rr = rr;
        }

        public void run() {
            stopPoints.clear();
            //得到查询到的路径
            Route r = rr.getRoutes().get(0);
            //往tv_label中赋值
            tv_label.setText("中长度：" + r.getTotalKilometers() + "米,  总时间：" + r.getTotalMinutes() + "分钟,描述：" + r.getRouteName());
            //将这条查询到路径放入routeLayer中
            Graphic routeGraphic = new Graphic(r.getRouteGraphic().getGeometry(), new SimpleLineSymbol(Color.BLUE, 3));
            routeLayer.addGraphic(routeGraphic);
            mMapView.setExtent(routeGraphic.getGeometry(), 100);
            List<RouteDirection> routeDirs = rr.getRoutes().get(0).getRoutingDirections();

            //取得查询到的路径片段，和其属性，放入到semLayer中，供点击查询每个片断信息
            Map<String, Object> atts = new HashMap<String, Object>();
            for (RouteDirection routeDir : routeDirs) {
                atts.put("text", routeDir.getText());
                atts.put("length", routeDir.getLength());
                atts.put("time", routeDir.getMinutes());
                Graphic dirGraphic = new Graphic(routeDir.getGeometry(), hiderSym, atts, 1);
                semLayer.addGraphic(dirGraphic);
            }

        }
    }

    class Button_Clear implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            routeLayer.removeAll();
            semLayer.removeAll();
            stopPoints = new ArrayList<Point>();
            tv_label.setText("");
            clear.setVisibility(View.INVISIBLE);
            if (markerid != Integer.MAX_VALUE) {
                mMylayer.removeGraphic(markerid);
                markerid = 0;
                mCallout.hide();
                mCallout.refresh();
            }
        }
    }

    class Button_Zoomin implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mMapView.zoomin();
        }
    }

    class Button_Zoomout implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mMapView.zoomout();
        }
    }

    class Button_Weather implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //System.out.print("change");
            if (weatherflag == 0) {
                Weather.setBackground(getResources().getDrawable(R.drawable.icon_rain));
                naurl = "http://gis.wku.edu.cn:6080/arcgis/rest/services/test001/campus/NAServer/Route";
                weatherflag = 1;
            } else {
                Weather.setBackground(getResources().getDrawable(R.drawable.icon_cloud));
                naurl = "http://gis.wku.edu.cn:6080/arcgis/rest/services/test008/campus/NAServer/Route";
                weatherflag = 0;
            }
        }
    }

    class Button_navi implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (searlayoutflag == 1) {
                searlayout.setVisibility(View.INVISIBLE);
                searlayoutflag = 0;
            }

            if (infolayoutflag == 1) {
                infolayout.setVisibility(View.INVISIBLE);
                infolayoutflag = 0;
            }

            if (navilayoutflag == 1) {
                navilayout.setVisibility(View.INVISIBLE);
                to.setText("");
                from.setText("MyLocation");

                navilayoutflag = 0;
            } else if (navilayoutflag == 0) {
                navilayout.setVisibility(View.VISIBLE);
                navilayoutflag = 1;
                to.setText("");
                from.setText("MyLocation");

            }
            if (markerid != Integer.MAX_VALUE) {
                mMylayer.removeGraphic(markerid);
                markerid = 0;
                mCallout.hide();
                mCallout.refresh();
            }
        }
    }

    class Button_sear implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (markerid != Integer.MAX_VALUE) {
                mMylayer.removeGraphic(markerid);
                markerid = 0;
                mCallout.hide();
                mCallout.refresh();
            }


            if (navilayoutflag == 1) {
                navilayout.setVisibility(View.INVISIBLE);
                navilayoutflag = 0;
                to.setText("");
            }

            if (searlayoutflag == 1) {
                searlayout.setVisibility(View.INVISIBLE);
                searchbox.setText("");
                searlayoutflag = 0;
            } else if (searlayoutflag == 0) {
                searlayout.setVisibility(View.VISIBLE);
                searlayoutflag = 1;
            }
            if (infolayoutflag == 1) {
                infolayout.setVisibility(View.INVISIBLE);
                infolayoutflag = 0;
            }
        }
    }

    class Button_info implements View.OnClickListener{
        public void onClick(View v) {
            if (navilayoutflag == 1) {
                navilayout.setVisibility(View.INVISIBLE);
                navilayoutflag = 0;
                to.setText("");
            }

            if (searlayoutflag == 1) {
                searlayout.setVisibility(View.INVISIBLE);
                searchbox.setText("");
                searlayoutflag = 0;
            }
            if(infolayoutflag==0){
                infolayout.setVisibility(View.VISIBLE);
                infolayoutflag=1;
            }else if(infolayoutflag==1){
                infolayout.setVisibility(View.INVISIBLE);
                infolayoutflag=0;
            }
        }
    }

    class Button_position implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Point p = new Point(longitude, latitude);
            mMapView.zoomToResolution((Point) GeometryEngine.project(p, NASR, mapSR), 5);

        }
    }

    class Textview_call implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:057755870110"));
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);

        }
    }

    class Button_data implements View.OnClickListener {
        @Override
        public void onClick(View v) {
                try{
                    new Mytask().execute();
            } catch (Exception e) {
                makeText(getApplicationContext(),
                        "Nooooooo!" /* \nI don't find what you want \nPlease tell my GREAT Maker!! to update" */, Toast.LENGTH_SHORT).show();
            }
        }
    }

    class Button_startsearch implements View.OnClickListener {
        public void onClick(View v) {
            if(searchbox.getText().length()!=0) {
                try {
                    InputStream is = getResources().getAssets().open("schoolinfo");
                    searchmarker(navifunc.collectinfo(is, searchbox.getText().toString()));
                    is.close();
                    searlayout.setVisibility(View.INVISIBLE);
                    searchbox.setText("");
                    searlayoutflag = 0;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class PopupDialog extends Dialog {
        private PopupContainer popupContainer;
        public PopupDialog(Context context, PopupContainer popupContainer) {
            super(context);
            this.popupContainer = popupContainer;

        }
        @Override

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LinearLayout layout=new LinearLayout(getContext());
            layout.addView(container.getPopupContainerView(), 700, 700);
            setContentView(layout, params);
        }
    }

    class MyQuery extends AsyncTask<String, Void, FeatureResult> {
        Point point;
        SpatialReference sr;

        public MyQuery(Point point, SpatialReference sr) {
            this.point = point;
            this.sr=sr;
        }

        @Override
        protected FeatureResult doInBackground(String... params) {
                //设置查询条件
                QueryParameters query = new QueryParameters();
                query.setInSpatialReference(sr);
                query.setOutSpatialReference(sr);
                query.setGeometry(point);
                query.setMaxFeatures(100);
                query.setOutFields(new String[]{"*"});
                QueryTask queryTask = new QueryTask(url);
                try {
                    FeatureResult results = queryTask.execute(query);
                    return results;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            return null;
        }


        protected void onPostExecute(FeatureResult result) {
            count--;
            int size = (int) result.featureCount();
            Graphic[] graphics = new Graphic[size];
            int i = 0;
            for (Object element : result) {
               if (element instanceof Feature) {
                    Feature feature = (Feature) element;
                    // turn feature into graphic
                    graphics[i] = new Graphic(feature.getGeometry(),
                            feature.getSymbol(), feature.getAttributes());
                    //System.out.println("attention");
                   phonenumber="";
                   phonenumber=feature.getAttributes().get(new String("number")).toString();
                   buildingtype=Integer.parseInt(feature.getAttributes().get(new String("type")).toString());

                    i++;
               }}

                if (graphics == null || graphics.length == 0) {
                    return;
                }

                if(buildingtype==1){
                    buildingtype=0;
                    new AlertDialog.Builder(MainActivity.this).setTitle("Security booth")//set titile
                            .setMessage("Do you want to call security")//set context
                            .setPositiveButton("Sure",new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//event
                                    Intent in = new Intent(Intent.ACTION_DIAL, Uri.parse(String.valueOf(phonenumber)));
                                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        return;
                                    }
                                    startActivity(in);
                                }

                            }).setNegativeButton("No",new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override

                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            dialog.dismiss();
                        }

                    }).show();//show evevt
                    return;
                }

                for (Graphic graphic : graphics) {

                    //get Popup
                    if(graphic!=null){
                    Popup popup = new Popup(mMapView, graphic);
                    //add into PopupContainer
                    container.addPopup(popup);}
                }
                if (count == 0) {
                    popDio = new PopupDialog(mMapView.getContext(), container);
                    popDio.show();
                }
            //}

        }
    }

    class Mytask extends AsyncTask<Void,Void, Void>{
        private double databaselongitude;
        private double databaselatitude;
        @Override
        protected Void doInBackground(Void... params) {
            try{
                //Class.forName("net.sourceforge.jtds.jdbc.Driver");// 加载驱动程序
                Class.forName("org.gjt.mm.mysql.Driver" );// 加载驱动程序
                // setup the connection with the DB.
                System.out.println("no");
                Connection connect = DriverManager.getConnection("jdbc:mysql://10.33.132.45:3306/WKU","tsing", "black");
                // statements allow to issue SQL queries to the database

                Statement statement = connect.createStatement();
                // resultSet gets the result of the SQL query

                String sql="select * from WKU.location";
                final ResultSet resultSet = statement
                        .executeQuery(sql); //+ "' or Office = '"+name+
                //
                databaselongitude=resultSet.getDouble("longitude");
                databaselatitude=resultSet.getDouble("latitude");
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            tv_label.setText(Double.toString(databaselongitude)+ " la " +Double.toString(databaselatitude));
        }
    }

    class Longpress implements OnLongPressListener {
        @Override
        public boolean onLongPress(float v, float v1) {

            container = new PopupContainer(mMapView);
            count = 0;
            final Point p=mMapView.toMapPoint(v, v1);

                final ArcGISTiledMapServiceLayer ads=local;

                final ArcGISLayerInfo[] infos= ads.getLayers();

            new Thread(){                    //把网络访问的代码放在这里
                public void run()
                {
                ArcGISLayerInfo layerinfo=infos[5];

                    PopupInfo popupInfo=ads.createPopupInfo(layerinfo.getId());

                    if (true) {
                        count++;
                       // System.out.println(count);
                        new MyQuery(p,ads.getSpatialReference()).execute(ads.getUrl() + "/" + layerinfo.getId());
                    }
                }

            }.start();
            return true;
        }}

    void setLocation(){
        Point wgsPoint = new Point(longitude, latitude);
        Point mapPoint = (Point) GeometryEngine.project(wgsPoint, NASR, SpatialReference.create(4490));
        mMapView.centerAt(mapPoint, true);
        Graphic graphic = new Graphic(mapPoint, mLocationSymbol);
        mMylayer.updateGraphic(mMyUID, graphic);
    }

    void searchmarker(WLocation marker){

        Point markerp = new Point(marker.getLongitude(),marker.getLatitude());
        // convert the coordinates to military grid strings

//                String mgrsPoint = CoordinateConversion.pointToMgrs(mapPoint,
//                        mMapView.getSpatialReference(),
//                        MGRSConversionMode.NEW_STYLE, 6, true, true);


        TextView calloutContent = new TextView(getApplicationContext());
        calloutContent.setTextColor(Color.BLACK);
        calloutContent.setSingleLine();
        calloutContent.setText(marker.getOfficename()+" \n "+marker.getOffice());
        mMapView.centerAt(marker.getLocation(),true);
        // get callout, set content and show
        mCallout = mMapView.getCallout();
        mCallout.setCoordinates(markerp);
        mCallout.setContent(calloutContent);
        mCallout.show();

        // String mPoint = String.valueOf(mPointX) + "," + String.valueOf(mPointY);
        // String[] mgrsPoint = mMapView.getSpatialReference()
        // .toMilitaryGrid(MGRSConversionMode.NEW_STYLE, 6,
        // true, true, new Point[] { mapPoint });
        // create a symbol for point
        SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.BLACK,
        12, SimpleMarkerSymbol.STYLE.DIAMOND);
        // create a text symbol for point coords
        //TextSymbol txtSym = new TextSymbol(12, mPoint, Color.BLACK);
        // txtSym.setOffsetX(10);
        // add point and symbol to Graphic
        Graphic gMarker = new Graphic(markerp, sms);
        // add text symbol to graphic
        // Graphic gText = new Graphic(mapPoint, txtSym);
        // add graphics to graphics alyer
        // gl.addGraphics(new Graphic[] { gMarker, gText });
        markerid=mMylayer.addGraphic(gMarker);
        }

}

