package com.inteligenixsolutions.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.inteligenixsolutions.myapplication.Model.SensorData;

@Database(entities = {SensorData.class}, version = 1)
public abstract class SensorDB extends RoomDatabase {

    public abstract SensorDAO getSensor();
    private static SensorDB sensorDatabase = null;

    public static SensorDB getSensorDataBase(Context context) {

        if (sensorDatabase == null) {
            sensorDatabase = Room.databaseBuilder(context,SensorDB.class,"sensor_database").allowMainThreadQueries().build();
        }
        return sensorDatabase;
    }




}
