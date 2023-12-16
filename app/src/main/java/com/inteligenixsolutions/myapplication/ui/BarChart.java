package com.inteligenixsolutions.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.inteligenixsolutions.myapplication.Model.SensorData;
import com.inteligenixsolutions.myapplication.database.SensorDB;
import com.inteligenixsolutions.myapplication.databinding.BarChartBinding;

import com.inteligenixsolutions.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BarChart extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private BarChartBinding binding;
    private SensorDB sensorDB;
    private String sensorValue="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BarChartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       sensorDB = SensorDB.getSensorDataBase(BarChart.this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle!=null){
            sensorValue = (String) bundle.get("Sensor");
        }
          setLineChartData(sensorValue);







    }
    private void setLineChartData(String sensorName) {

        List<SensorData> proximityList = sensorDB.getSensor().getAllSensor();

        ArrayList<Entry> entryArrayList = new ArrayList<>();


        for (SensorData sensorData : proximityList) {

            long timeStampInMillis = sensorData.getTimeStamp();
            Date date = new Date(timeStampInMillis);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String formattedTime = sdf.format(date);
            // Convert the formatted time to minutes
            long timeInMinutes = getTimeInMinutes(formattedTime);

//            float sensorValue = switch (sensorName) {
//                case "Proximity" -> sensorData.getProximity();
//                case "Light" -> sensorData.getLight();
//                default -> 0f;
//            };

            float sensorValue;
            switch (sensorName.toLowerCase()) {
                case "Proximity":
                    sensorValue = sensorData.getProximity();
                    break;
                case "Light":
                    sensorValue = sensorData.getLight();
                    break;
                default :
                   throw new IllegalArgumentException("Unknown sensor name: " + sensorName);
            }
            Entry chartEntry = new Entry(timeInMinutes, sensorValue);
            entryArrayList.add(chartEntry);
        }
        settingUpTheLineChart(entryArrayList);
    }



    private void settingUpTheLineChart(ArrayList<Entry> entryArrayList) {
        LineDataSet lineDataSet = new LineDataSet(entryArrayList, "Sensor");
        lineDataSet.setColor(R.color.purple);
        lineDataSet.setCircleRadius(10f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setValueTextSize(20f);
        lineDataSet.setFillColor(R.color.green);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);


        XAxis xAxis = binding.getTheGraph.getXAxis();
        xAxis.setLabelCount(10, true);
        xAxis.setGranularity(1f);

        LineData data = new LineData(lineDataSet);
        binding.getTheGraph.setData(data);
        binding.getTheGraph.setBackgroundColor(getResources().getColor(R.color.white));
        binding.getTheGraph.animateXY(1000, 1000, Easing.EaseInCubic);

    }
    private long getTimeInMinutes(String formattedTime) {
        String[] timeComponents = formattedTime.split(":");
        int hours = Integer.parseInt(timeComponents[0]);
        int minutes = Integer.parseInt(timeComponents[1]);
        return hours * 60L + minutes;
    }

}