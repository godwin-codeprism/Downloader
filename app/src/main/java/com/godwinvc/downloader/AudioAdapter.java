package com.godwinvc.downloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Godwin Vinny Carole on 005, Dec, 05, 16 -.
 */

public class AudioAdapter extends ArrayAdapter {
    List list = new ArrayList();
    public AudioAdapter(Context context, int resource){
        super (context, resource);
    }

    public void add (Audios audio){
        super.add(audio);
        list.add(audio);
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position){
        return list.get(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row;
        row = convertView;
        AudioHolder audioHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.audio_items,parent,false);
            audioHolder = new AudioHolder();
            audioHolder.audioName = (TextView) row.findViewById(R.id.audioNameTextView);
            audioHolder.downloadBtn = (Button) row.findViewById(R.id.audioDownloadButton);
            row.setTag(audioHolder);

        }else{
            audioHolder = (AudioHolder) row.getTag();

        }
        Audios audios = (Audios) this.getItem(position);
        audioHolder.audioName.setText(audios.getName());
        audioHolder.downloadBtn.setText(audios.getDownloadLink());
        return row;
    }

    static class AudioHolder{
        TextView audioName;
        Button downloadBtn;

    }
}
