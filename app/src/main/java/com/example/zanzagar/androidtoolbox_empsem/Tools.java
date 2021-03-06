package com.example.zanzagar.androidtoolbox_empsem;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private Button btnUpdate;
    private String CoinDb;
    final DBHelper db = new DBHelper(this);
    Button btnCoinLog;
    RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);


        btnCoinLog = (Button) findViewById(R.id.coin_log);
        requestQueue = Volley.newRequestQueue(this);

        mSensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        grav_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        lin_accel_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        rot_vect_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        prox_sens = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        btnBack2 = (Button)findViewById(R.id.back2);
        btnUpdate=(Button)findViewById(R.id.update);

        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new GetCoinPrice().execute();

            }
        });
        btnBack2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent3 = new Intent(Tools.this, MainActivity.class);
                //myIntent.putExtra("key", value); //za pošiljanje drugam
                Tools.this.startActivity(myIntent3);

            }
        });
        btnCoinLog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Tools.this, LogCoin.class);
                myIntent.putExtra("list", db.getAllCoinLogs()); //za pošiljanje drugam
                Tools.this.startActivity(myIntent);


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
            try {
                bubble_level_refresh(orientations[2]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
    void bubble_level_refresh(float z) throws InterruptedException {

        double change = 2.571428571;
        //String output="----------------------------------------------------------------------";
        StringBuilder output = new StringBuilder("----------------------------------------------------------------------");

        double value = (double)z/change;
        int res=0;
        int stevec=0;

        for(int i=-35;i<=35;i++){
            if(i<=(int)value&&value<(i+1)){
                res=i;
                res= -res;

                break;
            }
            stevec=stevec+1;

        }
        res=res+35;
        if(value>35||value<-35)
        {

        }else {
            if(res>=0&&res<70) {
                output.setCharAt(res, '+');
                ((TextView) findViewById(R.id.rot_val)).setText(String.valueOf(output));
            }
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(gravitySensorListener, grav_sens, 200000);
        mSensorManager.registerListener(accelerationSensorListener, lin_accel_sens,200000 );
        mSensorManager.registerListener(rotationSensorListener, rot_vect_sens, 200000);


    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(gravitySensorListener);
        mSensorManager.unregisterListener(accelerationSensorListener);
        mSensorManager.unregisterListener(rotationSensorListener);

    }
    private class GetCoinPrice extends AsyncTask<String, Void, String> {

        private String bitcoin;
        private String ethereum;
        public GetCoinPrice() {

        }

        @Override
        protected String doInBackground(String... strings) {
            if(getJSON(0)&&getJSON(1)){
                return "true";
            }else{
                return "false";
            }
        }

        @Override
        protected void onPostExecute(String temp) {
            ((TextView) findViewById(R.id.btc)).setText(String.valueOf(bitcoin));
            ((TextView) findViewById(R.id.eth)).setText(String.valueOf(ethereum));
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date currentTime = Calendar.getInstance().getTime();
            String s_currenttime = df.format(currentTime);
            CoinDb = "BTC: "+this.bitcoin+"\n"+"ETH: "+this.ethereum+"\n"+" TIME: "+s_currenttime;
            db.insertCoinLog(CoinDb);
        }
        private boolean getJSON(int i){
            try {
                String baseUrl="ne";
                if(i==0) {
                    baseUrl = "https://api.coinmarketcap.com/v1/ticker/bitcoin/";
                }else if(i==1) {
                    baseUrl = "https://api.coinmarketcap.com/v1/ticker/ethereum/";
                }

                URL url = new URL(baseUrl);
                HttpURLConnection connection =
                        (HttpURLConnection)url.openConnection();


                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();



                JSONArray mJsonArray = new JSONArray(json.toString());
                JSONObject mJsonObject = mJsonArray.getJSONObject(0);

                String chg = mJsonObject.getString("percent_change_24h");
                String usd = mJsonObject.getString("price_usd");
                String out="Price(usd): "+usd+" Change in 24hrs(%): "+chg;
                if(i==0){
                    this.bitcoin=out;
                }else if(i==1){
                    this.ethereum=out;
                }
                return true;
            }catch(Exception e){
                return false;
            }
        }
    }

    public void setTextoutput(String text){
        ((TextView) findViewById(R.id.btc)).setText(text);
        ((TextView) findViewById(R.id.eth)).setText(text);
    }





}

