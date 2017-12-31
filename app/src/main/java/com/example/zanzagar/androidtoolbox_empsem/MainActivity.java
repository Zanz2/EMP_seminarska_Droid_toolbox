package com.example.zanzagar.androidtoolbox_empsem;

import android.app.Activity;
import android.content.Context;
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

public class MainActivity extends Activity {
    private SensorManager mSensorManager;
    private TriggerEventListener mTriggerEventListener;
    private String HashValue;
    private boolean warming=false;
    private String TopleBesede="Veseli prazniki in srecno novo leto, naj vas greje ta string. Bla bla kolo pisalo sir zelenjava neki bla";

    private Button btnGrelec;
    private Button btnShrani;
    private Button btnPodatkiLog;
    private Sensor grav_sens;
    private Sensor lin_accel_sens;
    private Sensor rot_vect_sens;
    private Sensor prox_sens;

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
        btnShrani = (Button)findViewById(R.id.shrani);
        btnPodatkiLog = (Button)findViewById(R.id.podatki_log);
        grav_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        lin_accel_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        rot_vect_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        prox_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
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

        btnGrelec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!warming){
                    // https://stackoverflow.com/questions/7478941/implementing-a-while-loop-in-android
                    for(int i=0;i<100000;i++) {
                        computeSHAHash(TopleBesede);
                    }
                    Toast.makeText(MainActivity.this,
                            "Pogrelo", Toast.LENGTH_LONG).show();
                }
                warming=!warming;
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
            }
        });
    }
    SensorEventListener gravitySensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    SensorEventListener accelerationSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // More code goes here
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
            // More code goes here
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(gravitySensorListener, grav_sens, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(accelerationSensorListener, lin_accel_sens, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(rotationSensorListener, rot_vect_sens, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(proximitySensorListener, prox_sens, SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(gravitySensorListener);
        mSensorManager.unregisterListener(accelerationSensorListener);
        mSensorManager.unregisterListener(rotationSensorListener);
        mSensorManager.unregisterListener(proximitySensorListener);
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
