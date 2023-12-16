package com.inteligenixsolutions.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.inteligenixsolutions.myapplication.Model.SensorData;
import com.inteligenixsolutions.myapplication.database.SensorDB;
import com.inteligenixsolutions.myapplication.databinding.ActivityMainBinding;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private ActivityMainBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private SensorManager sensorManager;
    private Sensor lightSensor, proximitySensor, accelerometerSensor, gyroscopeSensor;
    private  SensorDB sensorDB;

    float proximityValue, lightSensorValue;
    float[] accelerometerValue, gyroscopeValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorDB = SensorDB.getSensorDataBase(this);



        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, (int) TimeUnit.MINUTES.toMicros(5));
        }

        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, (int) TimeUnit.MINUTES.toMicros(5));
        }

        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, (int) TimeUnit.MINUTES.toMicros(5));
        }

        if (gyroscopeSensor != null) {
            sensorManager.registerListener(this, gyroscopeSensor, (int) TimeUnit.MINUTES.toMicros(5));
        }




   handler.postDelayed(new Runnable() {
       @Override
       public void run() {
           sensorDB.getSensor().insertSensor(new SensorData(new Date().getTime(),proximityValue,lightSensorValue,accelerometerValue[0],accelerometerValue[1],accelerometerValue[2],gyroscopeValue[0],gyroscopeValue[1],gyroscopeValue[2]));
       }
   }, TimeUnit.MINUTES.toMillis(4));


  binding.proximity.setOnClickListener(v->{
       Intent intent = new Intent(MainActivity.this, BarChart.class);
       intent.putExtra( "Sensor","proximitySensor");
       startActivity(intent);


  });

    }






    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proximityValue = event.values[0];
            binding.proximity.setText("PROXIMITY SENSOR: " + proximityValue);
        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightSensorValue = event.values[0];
           binding.lightSensor.setText("LIGHT SENSOR: " + lightSensorValue);
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValue = event.values;
          binding.accelerometer.setText("ACCELEROMETER: X=" + accelerometerValue[0] + "\nY=" + accelerometerValue[1] + "\nZ=" + accelerometerValue[2]);
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroscopeValue = event.values;
         binding.gyroscope.setText("GYROSCOPE: X=" + gyroscopeValue[0] + "\nY=" + gyroscopeValue[1] + "\nZ=" + gyroscopeValue[2]);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }




}

