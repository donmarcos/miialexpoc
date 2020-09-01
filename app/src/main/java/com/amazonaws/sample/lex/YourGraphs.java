package com.amazonaws.sample.lex;

//import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobile.client.AWSMobileClient;


public class YourGraphs extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor sensor;
    private ImageButton ninjaEyes;
    private CognitoSettings cognitoSettings;
    private TextView title;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_graphs);
//        DOESN'T WORK
//        String username = AWSMobileClient.getInstance().getUsername();


        CognitoSettings cognitoSettings = new CognitoSettings(this);
//        Log.i("USERNAME IS THIS: ", AWSMobileClient.getInstance().getIdentityId()+ ", " + cognitoSettings.getUserPoolID());
//        username = String.valueOf(cognitoSettings.getUserPool().getUser());
        title = findViewById(R.id.profile_page_title);
        title.setText(("Hello User!"));

        ninjaEyes = findViewById(R.id.ninja_eyes);

        ninjaEyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(YourGraphs.this, "Let's check your eyes then!", Toast.LENGTH_SHORT).show();
               //  EyeTracker eyeTracker = new EyeTracker(YourGraphs.this);
//                eyeTracker.
                //                startActivity(new Intent(YourGraphs.this, EyeTracker.class));
            }
        });
    }

    /*
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ArrayList<String> messages = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter;
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        Log.i("BASE TIME:", String.valueOf(startTime));

        while((currentTime = System.currentTimeMillis()) - startTime < 12) {
            Log.i("CURRENT TIME:", String.valueOf(currentTime));
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            sensorMessages = findViewById(R.id.sensor_display);

            arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, messages);
            sensorMessages.setAdapter(arrayAdapter);
            messages.add(String.valueOf(sensor));
            arrayAdapter.notifyDataSetChanged();
        }
    */


}