package com.godwinvc.downloader;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class downloadPage extends AppCompatActivity {
    public String jsonString;
    JSONObject jsonObject;
    JSONArray jsonArray;
    AudioAdapter audioAdapter;
    private static final int PERMS_REQUEST_CODE = 3885;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_page);
        ListView listView = (ListView) findViewById(R.id.listViewMain);
        audioAdapter = new AudioAdapter(this,R.layout.audio_items);
        listView.setAdapter(audioAdapter);
        if (checkConnection()) {
            new RunBackground().execute();
        } else {
            Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_LONG).show();
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions();
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
            parseJson();
        }
    }

    public void parseJson(){
       if(jsonString == null){
            Toast.makeText(getApplicationContext(),"Unable to  fetch data from Server",Toast.LENGTH_LONG).show();
        }else{
            /*Intent intent= new Intent(this,DisplayListView.class);
            intent.putExtra("jsonData", jsonString);
            startActivity(intent);*/
           try {
               jsonObject = new JSONObject(jsonString);
               jsonArray = jsonObject.getJSONArray("audios");
               int count = 0;
               String name, downloadLink;
               while (count < jsonArray.length()) {
                   JSONObject JO = jsonArray.getJSONObject(count);
                   name = JO.getString("name");
                   downloadLink = JO.getString("downloadLink");
                   Audios audios = new Audios(name, downloadLink);
                   audioAdapter.add(audios);
                   count++;
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }
        }
    }
    public void requestPermissions(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,3885);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        boolean allowed = true;
        switch (requestCode){
            case PERMS_REQUEST_CODE:
                for(int res: grantResults){
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                allowed = false;
                break;
        }
        if(!allowed){
            Toast.makeText(getApplicationContext(),"Can't download without permissions",Toast.LENGTH_LONG).show();
        }
    }
}
