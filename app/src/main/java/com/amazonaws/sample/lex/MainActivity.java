/*
 * Copyright 2016-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.sample.lex;

//import android.app.Activity;
//import android.content.Intent;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_RECORDING_PERMISSIONS_RESULT = 75;
    private Button speechDemoButton;
    private Toolbar toolbar;

    /**
     * This function consists of all that happens when the activity is created
     * A call to the content view, which is activity_main.xml, is made.
     * A call to the function that initializes the toolbar is made
     * A call to the function that initialized the graphs and charts is made
     * */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intializeToolbar();
        initializeCharts();
//        init();

    }

/**
 * This is the code for the graphs. It includes a CombinedChart instance which is responsible for creating the
 * combined charts in the relevant XML section of the page.
 * The classes and the resources pertaining to all graphs and charts have been borrowed from the Github Repo belonging
 * to PhilJay, namely PhilJay/MPAndroidChart
*/
    private void initializeCharts(){

        chart =  new CombinedChart(MainActivity.this);
//        tfLight = Typeface.createFromAsset(getAssets(), "app/src/main/res/drawable/lobster_regular.ttf");

        setTitle("MIIA Health Checkup");

        chart = findViewById(R.id.chart1);
        chart.getDescription().setEnabled(false);
//        chart.setBackgroundColor(Color.rgb(172,239,255));
        chart.setBackgroundColor(Color.rgb(250,250,255));
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);

        // draw bars behind lines
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return months[(int) value % months.length];
            }
        });

        CombinedData combinedData = new CombinedData();
        combinedData.setData(generateLineData());
        combinedData.setData(generateBarData());
        combinedData.setData(generateBubbleData());
        combinedData.setData(generateScatterData());
        combinedData.setData(generateCandleData());

        xAxis.setAxisMaximum(combinedData.getXMax() + 0.25f);

        chart.setData(combinedData);
        chart.invalidate();

    // The code for graphs ends here. The functions that have been called in this class have been defined below
    }

    /**
     * This is the code to read thew file for user data
     * It reads the file and returns an ArrayList of strings of comma-separated values indicating, UpperBP, LowerBP, BloodSugar and Temperature(Fahrenheit)
     * @return ArrayList<String></String>
     * */
    private ArrayList<String> readFile(){
        ArrayList<String> values = new ArrayList<>();
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(getApplicationContext().getFilesDir().getPath() + "\\data.csv"));
            String value = null;
            while ((value = bufferedReader.readLine()) != null){
                values.add(value);
            }

            Log.d("\nLINES IN THE USER FILE", bufferedReader.readLine());
            bufferedReader.close();
        }
        catch (IOException io){ Log.e("IO EXCEPTION", Arrays.toString(io.getStackTrace())); }

        return values;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void intializeToolbar() {
        toolbar = findViewById(R.id.nav_toolbar);
        setSupportActionBar(toolbar);
    }

    public void buttonClick(MenuItem item){
        /*
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) clicked.getLayoutParams();
            params.width += 40;
            params.height += 40;
            params.topMargin -= 20;
            params.leftMargin -= 20;

            // Converting the size in pixels to sp
            clicked.setTextSize(clicked.getTextSize()/MainActivity.this.getResources().getDisplayMetrics().scaledDensity + 5);
            clicked.setLayoutParams(params);
        */

        if (item.getItemId() == R.id.button_select_text){
                    /*
                        params.width -= 40;
                        params.height -= 40;
                        params.topMargin += 20;
                        params.leftMargin += 20;

                        // Converting the size in pixels to sp
                        clicked.setTextSize(clicked.getTextSize()/MainActivity.this.getResources().getDisplayMetrics().scaledDensity - 5);
                        clicked.setLayoutParams(params);
                    */
            Intent textIntent = new Intent(MainActivity.this, TextActivity.class);
            startActivity(textIntent);
        }
        else if (item.getItemId() == R.id.button_select_voice){
            Intent voiceIntent = new Intent(MainActivity.this, InteractiveVoiceActivity.class);
            startActivity(voiceIntent);
        }
        else if (item.getItemId() == R.id.button_select_profile){
            Intent voiceIntent = new Intent(MainActivity.this, YourGraphs.class);
            startActivity(voiceIntent);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        moveTaskToBack(true);
    }


    private void init() {

        Log.e(TAG, "Initializing app");
        Log.e(TAG,  BuildConfig.API_URL );

        speechDemoButton = (Button) findViewById(R.id.button_select_voice);

        // Starting with Marshmallow we need to explicitly ask if we can record audio
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED) {
                speechDemoButton.setEnabled(true);
            } else {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORDING_PERMISSIONS_RESULT);
            }
        } else {
            speechDemoButton.setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_RECORDING_PERMISSIONS_RESULT) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                        "LexSample will not be able to use the voice feature", Toast.LENGTH_SHORT).show();

                // Disable the button
                speechDemoButton.setEnabled(false);
            } else {
                speechDemoButton.setEnabled(true);
            }
        }
    }

    CombinedChart chart;
    protected final String[] months = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };
    Typeface tfLight;
    private final int count = 12;


/*
 * These are the functions that have been borrowed from Phil Jay's Github repo as well
 * They act as supporters for whatever is being done in the onCreate function, and whatever calls are made to
 * construct the charts and generate random data for the same
*/

    /**
     * This function generates random Line-plot data for the line chart using the getRandom function
     * @return LineData
     * */
    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < count; index++)
            entries.add(new Entry(index + 0.5f, getRandom(15, 5)));

        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.rgb(248, 246, 70));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(248, 246, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(248, 246, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(12f);
        set.setValueTextColor(Color.rgb(250, 250, 10));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    /**
     * This function generates random Bar-plot data for the bar chart using the getRandom function
     * @return BarData
     * */
    private BarData generateBarData() {

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();

        for (int index = 0; index < count; index++) {
            entries1.add(new BarEntry(0, getRandom(25, 25)));

            // stacked
            entries2.add(new BarEntry(0, new float[]{getRandom(13, 12), getRandom(13, 12)}));
        }

        BarDataSet set1 = new BarDataSet(entries1, "Bar 1");
        set1.setColor(Color.rgb(10, 117, 0));
        set1.setValueTextColor(Color.rgb(10, 117, 0));
        set1.setValueTextSize(10.5f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarDataSet set2 = new BarDataSet(entries2, "");
        set2.setStackLabels(new String[]{"Stack 1", "Stack 2"});
        set2.setColors(Color.rgb(61, 148, 255), Color.rgb(50, 50, 255));
        set2.setValueTextColor(Color.rgb(50, 50, 255));
        set2.setValueTextSize(10.5f);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1, set2);
        d.setBarWidth(barWidth);

        // make this BarData object grouped
        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;
    }

    /**
     * This function generates random Scatter-plot data for the scatter chart using the getRandom function
     * @return ScatterData
     * */
    private ScatterData generateScatterData() {

        ScatterData d = new ScatterData();

        ArrayList<Entry> entries = new ArrayList<>();

        for (float index = 0; index < count; index += 0.5f)
            entries.add(new Entry(index + 0.25f, getRandom(10, 55)));

        ScatterDataSet set = new ScatterDataSet(entries, "Scatter DataSet");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setScatterShapeSize(12f);
        set.setDrawValues(false);
        set.setValueTextSize(10.5f);
        d.addDataSet(set);

        return d;
    }

    /**
     * This function generates random Candle-plot data data for the candle chart using the getRandom function
     * @return CandleData
     * */
    private CandleData generateCandleData() {

        CandleData d = new CandleData();

        ArrayList<CandleEntry> entries = new ArrayList<>();

        for (int index = 0; index < count; index += 2)
            entries.add(new CandleEntry(index + 1f, 90, 70, 85, 75f));

        CandleDataSet set = new CandleDataSet(entries, "Candle DataSet");
        set.setDecreasingColor(Color.rgb(90, 90, 108));
        set.setShadowColor(Color.LTGRAY);
        set.setBarSpace(0.15f);
        set.setValueTextSize(12f);
        set.setDrawValues(false);
        d.addDataSet(set);

        return d;
    }

    /**
     * This function generates Bubble-plot data for the bubble chart using the getRandom function
     * @return BubbleData
     * */
    private BubbleData generateBubbleData() {

        BubbleData bd = new BubbleData();

        ArrayList<BubbleEntry> entries = new ArrayList<>();

        for (int index = 0; index < count; index++) {
            float y = getRandom(10, 105);
            float size = getRandom(100, 105);
            entries.add(new BubbleEntry(index + 0.5f, y, size));
        }

        BubbleDataSet set = new BubbleDataSet(entries, "Bubble DataSet");
        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set.setValueTextSize(10.5f);
        set.setValueTextColor(Color.BLACK);
        set.setHighlightCircleWidth(1.5f);
        set.setDrawValues(true);
        bd.addDataSet(set);

        return bd;
    }

    /**
     * This function generates random data in the range [start, start + range]
     * @return float
     * */

    protected float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }


    /*
    @Override
    public void onClick(final View v) {
        switch ((v.getId())) {
            case R.id.button_select_text:
                Intent textIntent = new Intent(this, TextActivity.class);
                startActivity(textIntent);
                break;
            case R.id.button_select_voice:
                Intent voiceIntent = new Intent(this, InteractiveVoiceActivity.class);
                startActivity(voiceIntent);
                break;
        }
    }     */

}
