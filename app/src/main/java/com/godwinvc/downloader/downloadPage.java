package com.godwinvc.downloader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

public class downloadPage extends AppCompatActivity {
    RequestQueue queue;
    public String dataLink = "http://quicklearnsys.com/data/events.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_page);
        if (checkConnection()) {
            getData();
            populateAudios();
        } else {
            Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void populateAudios() {
        String[] audios = {"Audio 1", "Audio 2", "Audio 3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.audio_items, audios);
        ListView listView = (ListView) findViewById(R.id.audioListView);
        listView.setAdapter(adapter);
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
    public void getData (){
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache,network);
        queue.start();
        final TextView displayResponse = (TextView) findViewById(R.id.dataDisplay);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, dataLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        displayResponse.setText(processData(response));
                        queue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayResponse.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);
    }

    public String processData(String rawData){
        String data = rawData;
        try{
            JSONArray jsonArray = new JSONArray(data);
            String[] strArr = new String[jsonArray.length()];
            for (int i =0; i < jsonArray.length();i++){
                strArr[i] = jsonArray.getString(i);
            }
            data = strArr.toString();
        }catch(JSONException err){

        }

        return  data;
    }
}
