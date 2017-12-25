package com.example.zanzagar.androidtoolbox_empsem;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {
    private SensorManager mSensorManager;
    private TriggerEventListener mTriggerEventListener;

    private Sensor grav_sens;
    private Sensor lin_accel_sens;
    private Sensor rot_vect_sens;
    private Sensor prox_sens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        //int count=0;
        //for(Sensor sens_or: deviceSensors){

        //}

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
            ((TextView)findViewById(R.id.TV3_alt)).setText("Acceleration sensor sensor exists!");
            ((TextView)findViewById(R.id.TV4_alt)).setText("Acceleration sensor sensor exists!");
            ((TextView)findViewById(R.id.TV5_alt)).setText("Acceleration sensor sensor exists!");
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
        mSensorManager.registerListener(gravitySensorListener, grav_sens, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(accelerationSensorListener, lin_accel_sens, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(rotationSensorListener, rot_vect_sens, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(proximitySensorListener, prox_sens, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(gravitySensorListener);
        mSensorManager.unregisterListener(accelerationSensorListener);
        mSensorManager.unregisterListener(rotationSensorListener);
        mSensorManager.unregisterListener(proximitySensorListener);
    }

}
