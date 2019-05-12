package com.iskomt.android.inventorysnapshot.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.iskomt.android.inventorysnapshot.Entity.Item;

@Database(entities = {Item.class}, version = 1,exportSchema = false)
public abstract class MyAppDatabase extends RoomDatabase {
    public abstract MyDao myDao();
}
