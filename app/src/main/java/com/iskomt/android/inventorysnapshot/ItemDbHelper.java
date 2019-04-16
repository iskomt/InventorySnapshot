package com.iskomt.android.inventorysnapshot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.VisibleForTesting;
import com.iskomt.android.inventorysnapshot.ItemDbSchema.ItemTable;

public class ItemDbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "itemDb.db";

    public ItemDbHelper(Context context){super(context, DATABASE_NAME, null, VERSION);}

    @VisibleForTesting
    ItemDbHelper(Context context, String name, int version){
        super(context, name, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ItemTable.NAME + " (" + " _id integer primary key autoincrement, " +
                ItemTable.Cols.UUID + ", " +
                ItemTable.Cols.NAME + ", " +
                ItemTable.Cols.QUANTITY + ", " +
                ItemTable.Cols.PRICE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
