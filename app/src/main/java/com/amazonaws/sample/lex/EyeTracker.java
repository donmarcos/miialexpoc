package com.amazonaws.sample.lex;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

public class EyeTracker extends Tracker<Face> {

    private final float THRESHOLD = 0.75f;
    private Context context;
    private final String TAG = "EYE TRACKER DIARIES: ";

    public EyeTracker(Context context) {
        this.context = context;
    }

    @Override
    public void onUpdate(Detector.Detections<Face> detections, Face face) {
        if (face.getIsLeftEyeOpenProbability() > THRESHOLD || face.getIsRightEyeOpenProbability() > THRESHOLD) {
            Log.i(TAG, "onUpdate: Open Eyes Detected");
//            ((MainActivity)context).updateMainView(Condition.USER_EYES_OPEN);

        }else {
            Log.i(TAG, "onUpdate: Close Eyes Detected");
//            ((MainActivity)context).updateMainView(Condition.USER_EYES_CLOSED);
        }
    }
    @Override
    public void onMissing(Detector.Detections<Face> detections) {
        super.onMissing(detections);
        Log.i(TAG, "onUpdate: Face Not Detected yet!");
        ((HealthyEyes)context).updateMainView(Condition.FACE_NOT_FOUND);
    }
    @Override
    public void onDone() {
        super.onDone();
    }
}