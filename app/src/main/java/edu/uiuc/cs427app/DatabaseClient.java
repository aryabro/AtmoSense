package edu.uiuc.cs427app;

import android.content.Context;
import androidx.room.Room;

public class DatabaseClient {
    private Context context;
    private static DatabaseClient instance;
    private AppDatabase appDatabase;

    // Private constructor for the singleton instance.
    private DatabaseClient(Context context) {
        this.context = context;
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "cities_db")
                .fallbackToDestructiveMigration()
                .build();
    }

    // Gets the singleton instance of the DatabaseClient.
    public static synchronized DatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    // Returns the AppDatabase instance.
    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
