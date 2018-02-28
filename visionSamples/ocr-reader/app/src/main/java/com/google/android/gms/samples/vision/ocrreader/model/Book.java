package com.google.android.gms.samples.vision.ocrreader.model;

/**
 * Created by tom.saju on 2/27/2018.
 */

public class Book {
    String id;
    volumeInfo volumeInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public com.google.android.gms.samples.vision.ocrreader.model.volumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(com.google.android.gms.samples.vision.ocrreader.model.volumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }
}
