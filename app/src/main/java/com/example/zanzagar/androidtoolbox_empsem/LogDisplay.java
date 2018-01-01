package com.example.zanzagar.androidtoolbox_empsem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class LogDisplay extends Activity {

    private Button btnClear;
    private Button btnBack;
    private ListView ListPrikaz;
    @Override
    // for zanka da prebere vse iz baze pa da v listview od activity log displaya
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_display);
        final DBHelper db = new DBHelper(this);
        Intent intent = getIntent();
        final ArrayList podatki = intent.getStringArrayListExtra("list");
        btnClear = (Button)findViewById(R.id.clear);
        btnBack = (Button)findViewById(R.id.back);
        ListPrikaz = (ListView) findViewById(R.id.list_prikaz);
        RefreshList(podatki);
        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                podatki.clear();
                RefreshList(podatki);
                db.deleteLogs();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LogDisplay.this, MainActivity.class);
                //myIntent.putExtra("key", value); //za pošiljanje drugam
                LogDisplay.this.startActivity(myIntent);

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
