package com.godwinvc.downloader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class downloadPage extends AppCompatActivity {
    public String jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_page);
        if (checkConnection()) {
            new RunBackground().execute();
        } else {
            Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    class RunBackground extends AsyncTask<Void, Void, String> {
        public String dataLink;

        @Override
        protected void onPreExecute() {
            dataLink = "http://godwinvc.com/android/downloader/data.json";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(dataLink);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((jsonString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(jsonString + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (IOException e) {

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String response) {
            jsonString = response;
        }
    }

    public void parseJson(View v){
       if(jsonString == null){
            Toast.makeText(getApplicationContext(),"Unable to  fetch data from Server",Toast.LENGTH_LONG).show();
        }else{
            Intent intent= new Intent(this,DisplayListView.class);
            intent.putExtra("jsonData", jsonString);
            startActivity(intent);
        }
       // Toast.makeText(getApplicationContext(),"Unable to  fetch data from Server",Toast.LENGTH_LONG).show();
    }
}
