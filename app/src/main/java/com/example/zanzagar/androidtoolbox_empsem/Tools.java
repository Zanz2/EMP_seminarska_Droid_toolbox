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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class Tools extends Activity {

    private Sensor grav_sens;
    private Sensor lin_accel_sens;
    private Sensor rot_vect_sens;
    private Sensor prox_sens;
    private SensorManager mSensorManager;
    private TriggerEventListener mTriggerEventListener;
    private String HashValue;
    private Button btnBack2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        mSensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        grav_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        lin_accel_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        rot_vect_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        prox_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        btnBack2 = (Button)findViewById(R.id.back2);
        btnBack2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent3 = new Intent(Tools.this, MainActivity.class);
                //myIntent.putExtra("key", value); //za pošiljanje drugam
                Tools.this.startActivity(myIntent3);

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
            bubble_level_refresh(orientations[2]);
            /*
            ((TextView)findViewById(R.id.TV6_alt)).setText(String.valueOf(orientations[0]));
            ((TextView)findViewById(R.id.TV7_alt)).setText(String.valueOf(orientations[1]));
            ((TextView)findViewById(R.id.TV8_alt)).setText(String.valueOf(orientations[2]));
            */
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    void bubble_level_refresh(float z){

    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(gravitySensorListener, grav_sens, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(accelerationSensorListener, lin_accel_sens, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(rotationSensorListener, rot_vect_sens, SensorManager.SENSOR_DELAY_UI);


    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(gravitySensorListener);
        mSensorManager.unregisterListener(accelerationSensorListener);
        mSensorManager.unregisterListener(rotationSensorListener);

    }
}
