package com.example.zanzagar.androidtoolbox_empsem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class LogCoin extends Activity {
    private Button btnBack3;
    private Button btnCoinClear;
    private ListView ListPrikaz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_coin);
        Intent intent = getIntent();
        final ArrayList podatki = intent.getStringArrayListExtra("list");
        final DBHelper db = new DBHelper(this);
        btnBack3 = (Button)findViewById(R.id.back3);
        btnCoinClear = (Button)findViewById(R.id.cleardata);
        ListPrikaz = (ListView) findViewById(R.id.coin_list);
        RefreshList(podatki);
        btnBack3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LogCoin.this, Tools.class);
                //myIntent.putExtra("key", value); //za pošiljanje drugam
                LogCoin.this.startActivity(myIntent);

            }
        });
        btnCoinClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                podatki.clear();
                RefreshList(podatki);
                db.deleteCoinLogs();


            }
        });
    }
    void RefreshList(ArrayList input){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                input );

        ListPrikaz.setAdapter(arrayAdapter);

    }
}
