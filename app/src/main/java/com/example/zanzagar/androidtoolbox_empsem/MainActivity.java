package com.example.zanzagar.androidtoolbox_empsem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainActivity extends Activity implements Gretje.OnProgressListener {
    private SensorManager mSensorManager;
    private TriggerEventListener mTriggerEventListener;
    private String HashValue;
    private boolean warming=false;
    private String TopleBesede="Veseli prazniki in srecno novo leto, naj vas greje ta string. Bla bla kolo pisalo sir zelenjava neki bla";

    private Button btnGrelec;
    private Button btnStop;
    private Button btnShrani;
    private Button btnPodatkiLog;
    private Button btnDrugaOrodja;
    private Sensor grav_sens;
    private Sensor lin_accel_sens;
    private Sensor rot_vect_sens;
    private Sensor prox_sens;

    private Sensor temp_sens;
    private Sensor pressure_sens;
    private Sensor light_sens;
    private Sensor humid_sens;

    private Gretje gretje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DBHelper db = new DBHelper(this);
        mSensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        //int count=0;
        //for(Sensor sens_or: deviceSensors){

        //}

        btnGrelec = (Button)findViewById(R.id.grelec);
        btnStop = (Button)findViewById(R.id.stop);
        btnShrani = (Button)findViewById(R.id.shrani);
        btnStop.setClickable(false);

        btnPodatkiLog = (Button)findViewById(R.id.podatki_log);
        btnDrugaOrodja = (Button)findViewById(R.id.orodja);
        grav_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        lin_accel_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        rot_vect_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        prox_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        temp_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        pressure_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        light_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        humid_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if(grav_sens == null){
            ((TextView)findViewById(R.id.TV_alt)).setText("Gravity sensor not found.");
            ((TextView)findViewById(R.id.TV1_alt)).setText("Gravity sensor not found.");
            ((TextView)findViewById(R.id.TV2_alt)).setText("Gravity sensor not found.");
        }else{
            ((TextView)findViewById(R.id.TV_alt)).setText("Gravity sensor exists!");
            ((TextView)findViewById(R.id.TV1_alt)).setText("Gravity sensor exists!");
            ((TextView)findViewById(R.id.TV2_alt)).setText("Gravity sensor exists!");
        }
        if(lin_accel_sens == null){
            ((TextView)findViewById(R.id.TV3_alt)).setText("Acceleration sensor not found.");
            ((TextView)findViewById(R.id.TV4_alt)).setText("Acceleration sensor not found.");
            ((TextView)findViewById(R.id.TV5_alt)).setText("Acceleration sensor not found.");
        }else{
            ((TextView)findViewById(R.id.TV3_alt)).setText("Acceleration sensor exists!");
            ((TextView)findViewById(R.id.TV4_alt)).setText("Acceleration sensor exists!");
            ((TextView)findViewById(R.id.TV5_alt)).setText("Acceleration sensor exists!");
        }
        if(rot_vect_sens == null){
            ((TextView)findViewById(R.id.TV6_alt)).setText("Rotation sensor not found.");
            ((TextView)findViewById(R.id.TV7_alt)).setText("Rotation sensor not found.");
            ((TextView)findViewById(R.id.TV8_alt)).setText("Rotation sensor not found.");
        }else{
            ((TextView)findViewById(R.id.TV6_alt)).setText("Rotation sensor exists!");
            ((TextView)findViewById(R.id.TV7_alt)).setText("Rotation sensor exists!");
            ((TextView)findViewById(R.id.TV8_alt)).setText("Rotation sensor exists!");
        }
        if(prox_sens == null){
            ((TextView)findViewById(R.id.TV9_alt)).setText("Proximity sensor not found.");
        }else{
            ((TextView)findViewById(R.id.TV9_alt)).setText("Proximity sensor exists!");
        }
        if(temp_sens == null){
            ((TextView)findViewById(R.id.TV10_alt)).setText("Temperature sensor not found.");
        }else{
            ((TextView)findViewById(R.id.TV10_alt)).setText("Temperature sensor exists!");
        }
        if(pressure_sens == null){
            ((TextView)findViewById(R.id.TV11_alt)).setText("Pressure sensor not found.");
        }else{
            ((TextView)findViewById(R.id.TV11_alt)).setText("Pressure sensor exists!");
        }
        if(light_sens == null){
            ((TextView)findViewById(R.id.TV12_alt)).setText("Light sensor not found.");
        }else{
            ((TextView)findViewById(R.id.TV12_alt)).setText("Light sensor exists!");
        }
        if(humid_sens == null){
            ((TextView)findViewById(R.id.TV13_alt)).setText("Humidity sensor not found.");
        }else{
            ((TextView)findViewById(R.id.TV13_alt)).setText("Humidity sensor exists!");
        }

        btnGrelec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!warming){
                    warming=true;
                    // https://stackoverflow.com/questions/7478941/implementing-a-while-loop-in-android
                    //for(int i=0;i<100000;i++) {
                    //    computeSHAHash(TopleBesede);
                    //}
                    btnGrelec.setClickable(false);
                    btnStop.setClickable(true);
                    Toast.makeText(MainActivity.this,
                            "Heating", Toast.LENGTH_LONG).show();
                    gretje = new Gretje();
                    gretje.dejKontekst(MainActivity.this);
                    gretje.setOnProgressListener(MainActivity.this);
                    gretje.execute();
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(warming){
                    //Toast.makeText(MainActivity.this,
                    //        "Stopped heating.", Toast.LENGTH_LONG).show();
                    warming=false;
                    btnGrelec.setClickable(true);
                    btnStop.setClickable(false);
                    if (gretje != null)
                        gretje.stop();
                    gretje = null;
                    Toast.makeText(MainActivity.this,
                            "Stopped heating.", Toast.LENGTH_LONG).show();
                }

            }
        });
        btnShrani.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TV_alt do TV9_alt
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date currentTime = Calendar.getInstance().getTime();
                String s_currenttime = df.format(currentTime);


                String grav_sens_name = "Gravity Sensor";
                String grav_sens_info = " x: ";
                grav_sens_info+=String.valueOf(((TextView)findViewById(R.id.TV_alt)).getText());
                grav_sens_info+= " y: ";
                grav_sens_info+=String.valueOf(((TextView)findViewById(R.id.TV1_alt)).getText());
                grav_sens_info+= " z: ";
                grav_sens_info+=String.valueOf(((TextView)findViewById(R.id.TV2_alt)).getText());
                db.insertLog(grav_sens_name,grav_sens_info,s_currenttime);

                String accel_sens_name = "Acceleration Sensor";
                String accel_sens_info = " x: ";
                accel_sens_info+=String.valueOf(((TextView)findViewById(R.id.TV3_alt)).getText());
                accel_sens_info+= " y: ";
                accel_sens_info+=String.valueOf(((TextView)findViewById(R.id.TV4_alt)).getText());
                accel_sens_info+= " z: ";
                accel_sens_info+=String.valueOf(((TextView)findViewById(R.id.TV5_alt)).getText());
                db.insertLog(accel_sens_name,accel_sens_info,s_currenttime);

                String rot_sens_name = "Rotation Sensor";
                String rot_sens_info = " x: ";
                rot_sens_info+=String.valueOf(((TextView)findViewById(R.id.TV6_alt)).getText());
                rot_sens_info+= " y: ";
                rot_sens_info+=String.valueOf(((TextView)findViewById(R.id.TV7_alt)).getText());
                rot_sens_info+= " z: ";
                rot_sens_info+=String.valueOf(((TextView)findViewById(R.id.TV8_alt)).getText());
                db.insertLog(rot_sens_name,rot_sens_info,s_currenttime);

                String prox_sens_name = "Proximity Sensor";
                String prox_sens_info = " distance (usually in cm): ";
                prox_sens_info+=String.valueOf(((TextView)findViewById(R.id.TV9_alt)).getText());
                db.insertLog(prox_sens_name,prox_sens_info,s_currenttime);

                String temp_sens_name = "Temperature Sensor";
                String temp_sens_info = " value (in celsius): ";
                temp_sens_info+=String.valueOf(((TextView)findViewById(R.id.TV10_alt)).getText());
                db.insertLog(temp_sens_name,temp_sens_info,s_currenttime);

                String press_sens_name = "Pressure Sensor";
                String press_sens_info = " value (in hpa): ";
                press_sens_info+=String.valueOf(((TextView)findViewById(R.id.TV11_alt)).getText());
                db.insertLog(press_sens_name,press_sens_info,s_currenttime);

                String light_sens_name = "Light Sensor";
                String light_sens_info = " value (in lux SI): ";
                light_sens_info+=String.valueOf(((TextView)findViewById(R.id.TV12_alt)).getText());
                db.insertLog(light_sens_name,light_sens_info,s_currenttime);

                String humid_sens_name = "Humidity sensor";
                String humid_sens_info = " value (relative): ";
                humid_sens_info+=String.valueOf(((TextView)findViewById(R.id.TV13_alt)).getText());
                db.insertLog(humid_sens_name,humid_sens_info,s_currenttime);
                String break1 = "-----";
                db.insertLog(break1,break1,break1);
            }
        });
        btnPodatkiLog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, LogDisplay.class);
                myIntent.putExtra("list", db.getAllLogs()); //za pošiljanje drugam
                MainActivity.this.startActivity(myIntent);
            }
        });
        btnDrugaOrodja.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent1 = new Intent(MainActivity.this, Tools.class);
                //myIntent.putExtra("list", db.getAllLogs()); //za pošiljanje drugam
                MainActivity.this.startActivity(myIntent1);
            }
        });
    }

    SensorEventListener gravitySensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            ((TextView)findViewById(R.id.TV_alt)).setText(String.valueOf(sensorEvent.values[0]));
            ((TextView)findViewById(R.id.TV1_alt)).setText(String.valueOf(sensorEvent.values[1]));
            ((TextView)findViewById(R.id.TV2_alt)).setText(String.valueOf(sensorEvent.values[2]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    SensorEventListener accelerationSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            ((TextView)findViewById(R.id.TV3_alt)).setText(String.valueOf(sensorEvent.values[0]));
            ((TextView)findViewById(R.id.TV4_alt)).setText(String.valueOf(sensorEvent.values[1]));
            ((TextView)findViewById(R.id.TV5_alt)).setText(String.valueOf(sensorEvent.values[2]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    SensorEventListener rotationSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] rotationMatrix = new float[16];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);
            float[] remappedRotationMatrix = new float[16];
            SensorManager.remapCoordinateSystem(rotationMatrix,
                    SensorManager.AXIS_X,
                    SensorManager.AXIS_Z,
                    remappedRotationMatrix);
            float[] orientations = new float[3];
            SensorManager.getOrientation(remappedRotationMatrix, orientations);
            for(int i = 0; i < 3; i++) {
                orientations[i] = (float)(Math.toDegrees(orientations[i]));
            }
            ((TextView)findViewById(R.id.TV6_alt)).setText(String.valueOf(orientations[0]));
            ((TextView)findViewById(R.id.TV7_alt)).setText(String.valueOf(orientations[1]));
            ((TextView)findViewById(R.id.TV8_alt)).setText(String.valueOf(orientations[2]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    SensorEventListener proximitySensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            ((TextView)findViewById(R.id.TV9_alt)).setText(String.valueOf(sensorEvent.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    SensorEventListener temperatureSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            ((TextView)findViewById(R.id.TV10_alt)).setText(String.valueOf(sensorEvent.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    SensorEventListener pressureSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            ((TextView)findViewById(R.id.TV11_alt)).setText(String.valueOf(sensorEvent.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    SensorEventListener lightSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            ((TextView)findViewById(R.id.TV12_alt)).setText(String.valueOf(sensorEvent.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    SensorEventListener humidSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            ((TextView)findViewById(R.id.TV13_alt)).setText(String.valueOf(sensorEvent.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(gravitySensorListener, grav_sens, 200000);
        mSensorManager.registerListener(accelerationSensorListener, lin_accel_sens, 200000);
        mSensorManager.registerListener(rotationSensorListener, rot_vect_sens, 200000);
        mSensorManager.registerListener(proximitySensorListener, prox_sens, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(temperatureSensorListener, temp_sens, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(pressureSensorListener, pressure_sens, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(lightSensorListener, light_sens, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(humidSensorListener, humid_sens, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(gravitySensorListener);
        mSensorManager.unregisterListener(accelerationSensorListener);
        mSensorManager.unregisterListener(rotationSensorListener);
        mSensorManager.unregisterListener(proximitySensorListener);
        mSensorManager.unregisterListener(temperatureSensorListener);
        mSensorManager.unregisterListener(pressureSensorListener);
        mSensorManager.unregisterListener(lightSensorListener);
        mSensorManager.unregisterListener(humidSensorListener);
    }
    public void computeSHAHash(String password)
    {

        MessageDigest mdSha1 = null;
        try
        {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            Log.e("Benchmark", "Error initializing SHA1");
        }
        try {
            mdSha1.update(password.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] data = mdSha1.digest();
        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex = Base64.encodeToString(data, 0, data.length, 0);

        sb.append(hex);
        HashValue=sb.toString();

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onStopped(boolean finished) {

    }
    /*
    // za različne prioritete: https://developer.android.com/reference/android/os/Process.html
    public class Sha1Grelec_start implements Runnable {
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);

            // do stuff here
        }
    }
    public class Sha1Grelec_stop implements Runnable {
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);

            // do stuff here
        }
    }
    */
}
