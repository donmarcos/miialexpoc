package com.amazonaws.sample.lex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest.permission;
import android.content.pm.PackageManager;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.sample.lex.R;
import com.google.android.gms.vision.CameraSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class HealthyEyes extends AppCompatActivity {

    private RelativeLayout eyeCareLayout;
    private TextView eyeTrackerTextview;
    private boolean flag = false;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy_eyes);

        if (ActivityCompat.checkSelfPermission(this, permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{permission.CAMERA}, 1);
            Toast.makeText(this, "Permission for Camera Denied! Restart application and grant permission!", Toast.LENGTH_SHORT).show();
        }
        else{
            initilaize();
        }
    }

// method to initialize the Camera Source and call the eye_tracker daemon
    private void initilaize(){
        eyeCareLayout = findViewById(R.id.eyecare_layout);
        eyeTrackerTextview = findViewById(R.id.eyetracker_textview);
        flag = true;
        initializeCamerSource();
    }

//method to create camera source from faceFactoryDaemon class
    private void initializeCamerSource(){
        FaceDetector faceDetector = new FaceDetector.Builder(this).
                setTrackingEnabled(true)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        faceDetector.setProcessor(new MultiProcessor.Builder<>(new FaceTrackerDaemon(HealthyEyes.this)).build());

        cameraSource = new CameraSource.Builder(this, faceDetector)
                .setRequestedPreviewSize(1024, 768)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(45.0f)
                .build();

        try{
            if (ActivityCompat.checkSelfPermission(this, permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{permission.CAMERA}, 1);
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "The MIIA EyeChecker\nNoice! Let's start!", Toast.LENGTH_SHORT).show();
                cameraSource.start();
            }
        }
        catch(IOException io){Toast.makeText(HealthyEyes.this, Arrays.toString(io.getStackTrace()), Toast.LENGTH_SHORT).show();}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (String.valueOf(requestCode).equals(permission.CAMERA)) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getApplicationContext(),
                        "The MIIA EyeChecker will not be able to function \n without using the front camera", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(cameraSource != null){
            try{
                if (ActivityCompat.checkSelfPermission(this, permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{permission.CAMERA}, 1);
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "The MIIA EyeChecker\nNoice! Let's start!", Toast.LENGTH_SHORT).show();
                    cameraSource.start();
                }
            }
            catch(IOException io){Toast.makeText(HealthyEyes.this, Arrays.toString(io.getStackTrace()), Toast.LENGTH_SHORT).show();}
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraSource!=null)
            cameraSource.stop();

//        setBackgroundGrey();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSource!=null)
            cameraSource.release();
    }

//update the view when it is called from EyeTracker, whenever an eye-movement is detected
    public void updateMainView(Condition condition){
        switch(condition){
            case USER_EYES_OPEN:
        }
    }
}