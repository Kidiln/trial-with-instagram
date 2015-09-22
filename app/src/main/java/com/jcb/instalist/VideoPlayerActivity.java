package com.jcb.instalist;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by jacobkoikkara on 9/21/15.
 */
public class VideoPlayerActivity extends Activity {


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = getIntent().getStringExtra(ApplicationData.INTENT_URL);

        setContentView(R.layout.video);
        final VideoView videoView =
                (VideoView) findViewById(R.id.videoView);

        videoView.setVideoPath(url);

        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                InstagramUtils.showLog("Duration = " +
                        videoView.getDuration());
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onBackPressed();
            }
        });
        videoView.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
