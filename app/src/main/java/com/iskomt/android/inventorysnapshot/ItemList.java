package com.iskomt.android.inventorysnapshot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iskomt.android.inventorysnapshot.Model.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.iskomt.android.inventorysnapshot.ItemDbSchema.*;

public class ItemList {
    private static ItemList sItemList;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ItemList get(Context context){
        if(sItemList == null){
            sItemList = new ItemList(context);
        }
        return sItemList;
    }

    private ItemList(Context context) {

        mContext = context.getApplicationContext();
        mDatabase = new ItemDbHelper(mContext).getWritableDatabase();
    }

    public void addItem(Item item){
        ContentValues values = getContentValues(item);
        mDatabase.insert(ItemTable.NAME, null, values);
    }

    public void deleteItem(Item item){
        mDatabase.delete(ItemTable.NAME, ItemTable.Cols.UUID + " = ?",new String[]{item.getId().toString()});
    }

    public void updateItem(Item item){
        String uuidString = item.getId().toString();
        ContentValues values = getContentValues(item);
        mDatabase.update(ItemTable.NAME, values, ItemTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    public void getItem(int position){
    }

    public File getPhotoFile(Item item){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, item.getPhotoFileName());
    }

    public List<Item> getItems(){
        List<Item> items = new ArrayList<>();

        ItemCursorWrapper cursor = queryItems(null,null);

        try{

            cursor.moveToFirst();

            while(!cursor.isAfterLast()){

                items.add(cursor.getItem());
                cursor.moveToNext();
            }
        }finally{
            cursor.close();
        }
        return items;
    }

    public Item getItem(UUID id) {
        ItemCursorWrapper cursor = queryItems(ItemTable.Cols.UUID + " = ?", new String[]{id.toString()});

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getItem();
        } finally {
            cursor.close();
        }
    }

    private ItemCursorWrapper queryItems(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ItemTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ItemCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Item item){
        ContentValues values = new ContentValues();
        values.put(ItemTable.Cols.UUID, item.getId().toString());
        values.put(ItemTable.Cols.NAME, item.getName());
        values.put(ItemTable.Cols.QUANTITY, item.getQty());
        values.put(ItemTable.Cols.PRICE, item.getPrice());
        values.put(ItemTable.Cols.PHOTO, item.getPhotoPath());

        return values;
    }


}
