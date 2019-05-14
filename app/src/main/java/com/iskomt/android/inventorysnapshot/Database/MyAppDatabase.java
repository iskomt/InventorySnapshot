package com.iskomt.android.inventorysnapshot.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.iskomt.android.inventorysnapshot.Entity.Item;

@Database(entities = {Item.class}, version = 1,exportSchema = false)
public abstract class MyAppDatabase extends RoomDatabase {
    private static MyAppDatabase instance;
    public abstract MyDao myDao();
    public static synchronized MyAppDatabase getInstance(Context context){
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MyAppDatabase.class, "item_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();;
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private MyDao mMyDao;

        private PopulateDbAsyncTask(MyAppDatabase myAppDatabase){
            mMyDao = myAppDatabase.myDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mMyDao.insertItem(new Item());
            mMyDao.insertItem(new Item());
            mMyDao.insertItem(new Item());
            return null;
        }
    }
}
