package com.godwinvc.downloader;

/**
 * Created by Godwin Vinny Carole on 005, Dec, 05, 16 -.
 */

public class Audios {
    private String name, downloadLink;

    public Audios(String name, String downloadLink){
        this.setName(name);
        this.setDownloadLink(downloadLink);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }
}
