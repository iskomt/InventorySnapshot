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
    public abstract ItemDao itemDao();
    public abstract CategoryDao categoryDao();
    public static synchronized MyAppDatabase getInstance(Context context){
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MyAppDatabase.class, "item_db")
                    .fallbackToDestructiveMigration()
                    //.addCallback(roomCallback)
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
        private ItemDao mItemDao;

        private PopulateDbAsyncTask(MyAppDatabase myAppDatabase){
            mItemDao = myAppDatabase.itemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mItemDao.insertItem(new Item());
            mItemDao.insertItem(new Item());
            mItemDao.insertItem(new Item());
            return null;
        }
    }
}
