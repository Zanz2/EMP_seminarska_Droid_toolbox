package com.example.zanzagar.androidtoolbox_empsem;

import android.app.Activity;
import android.os.Bundle;

public class LogDisplay extends Activity {

    @Override
    // for zanka da prebere vse iz baze pa da v listview od activity log displaya
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_display);
    }
}
