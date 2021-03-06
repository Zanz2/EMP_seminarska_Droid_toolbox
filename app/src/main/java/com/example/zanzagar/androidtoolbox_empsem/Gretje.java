package com.example.zanzagar.androidtoolbox_empsem;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Gretje extends AsyncTask<Object, Double, Boolean> {
    public interface OnProgressListener {
        void onStarted();

        void onStopped(boolean finished);
    }

    private OnProgressListener onProgressListener = null;
    private boolean keepRunning=true;
    private String HashValue;
    private Context kontekst;
    private String TopleBesede="Veseli prazniki in srecno novo leto, naj vas greje ta string. Bla bla kolo pisalo sir zelenjava neki bla";


    Gretje() {
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (onProgressListener != null)
            onProgressListener.onStarted();
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        boolean neki=  heat();
        return neki;
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        super.onProgressUpdate(values);

    }

    public void dejKontekst(Context kontekst1){
        this.kontekst=kontekst1;
    }
    @Override
    protected void onPostExecute(Boolean finished) {
        super.onPostExecute(finished);
        if (onProgressListener != null)
            onProgressListener.onStopped(finished);
    }

    public boolean heat() {
        do {
            computeSHAHash(TopleBesede);
            if(keepRunning==false){
                break;
            }
        }while(true);

        return true;
    }



    public void stop() {
        keepRunning = false;
    }
    public void computeSHAHash(String password)
    {

        MessageDigest mdSha1 = null;
        try
        {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            Log.e("Benchmark", "Error initializing heating");
        }
        try {
            mdSha1.update(password.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
        byte[] data = mdSha1.digest();
        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex = Base64.encodeToString(data, 0, data.length, 0);

        sb.append(hex);
        HashValue=sb.toString();

    }
}

