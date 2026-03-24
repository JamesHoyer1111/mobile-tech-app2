package com.example.mobiletechapp2;

import android.net.Uri;

public class MLKitResult {

    private String reader;
    private String result;
    private Uri imageUri;

    // Constructor
    public MLKitResult(String reader, String result, Uri imageUri) {
        this.reader = reader;
        this.result = result;
        this.imageUri = imageUri;
    }

    // Getters
    public String getReader() {
        return reader;
    }

    public String getResult() {
        return result;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    // Setters
    public void setReader(String reader) {
        this.reader = reader;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}