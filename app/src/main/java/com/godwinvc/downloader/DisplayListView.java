package com.godwinvc.downloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisplayListView extends AppCompatActivity {
    String jsonString;
    JSONObject jsonObject;
    JSONArray jsonArray;
    AudioAdapter audioAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_listview_layout);
        audioAdapter = new AudioAdapter(this,R.layout.audio_items);
        jsonString = getIntent().getExtras().getString("jsonData");
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(audioAdapter);
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
