package com.example.mobiletechapp2;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;

public class MultimediaActivity extends AppCompatActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri fileUri;
    int position = 0;
    MediaPlayer mediaPlayer;
    VideoView videoView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia);
    }

    public void audioOn(View view) {
        try {
            Uri audioUri = Uri.parse("android.resource://"
                    + getPackageName() + "/"
                    + R.raw.abba_fernando);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getApplicationContext(), audioUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }

    public void audioPause(View view) {
        if (mediaPlayer == null)
            return;
        if (mediaPlayer.isPlaying()) {
            position = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
    }

    public void audioResume(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
            mediaPlayer.start();
        }
    }

    public void audioOff(View view) {
        if (mediaPlayer == null)
            return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            position = 0;
        }
    }

    public void videoOn(View view) {
        videoView = (VideoView) findViewById(R.id.videoView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Buffering...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            MediaController mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(videoView);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoPath(
                    "android.resource://" + getPackageName() + "/"
                            + R.raw.abba_fernando);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                videoView.start();
            }
        });
    }

    public void videoOff(View view) {
        if (videoView == null)
            return;
        if (videoView.isPlaying()) {
            videoView.stopPlayback();
            videoView = null;
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}