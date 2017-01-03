package com.longdo.map.longdomapandroidsdksample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.longdo.mjpegviewer.MjpegView;

public class CameraActivity extends AppCompatActivity {

    public static final String VDO_URL = "vdo_url";
    public static final String CAMERA_NAME = "camera_name";
    private MjpegView viewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        String cameraName = getIntent().getStringExtra(CAMERA_NAME);
        String vdoUrl = getIntent().getStringExtra(VDO_URL);

        setTitle(cameraName);

        viewer = (MjpegView) findViewById(R.id.mjpegview);
        viewer.setMode(MjpegView.MODE_FIT_WIDTH);
        viewer.setAdjustHeight(true);
        viewer.setUrl(vdoUrl);
    }

    @Override
    protected void onResume() {
        viewer.startStream();
        super.onResume();
    }

    @Override
    protected void onPause() {
        viewer.stopStream();
        super.onPause();
    }
}
