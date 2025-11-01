package edu.uiuc.cs427app;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {City.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    // Provides access to the City data access object.
    public abstract CityDao cityDao();
}
