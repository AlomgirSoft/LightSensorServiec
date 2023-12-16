package com.inteligenixsolutions.myapplication.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.inteligenixsolutions.myapplication.Model.SensorData;

import java.util.List;

@Dao
public interface SensorDAO {
    @Insert
    void insertSensor(SensorData sensorData);
    @Query("SELECT* FROM sensor_data")
    List<SensorData>getAllSensor();





}
