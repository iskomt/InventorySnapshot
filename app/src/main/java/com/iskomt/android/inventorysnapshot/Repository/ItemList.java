package com.iskomt.android.inventorysnapshot.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.iskomt.android.inventorysnapshot.Database.MyAppDatabase;
import com.iskomt.android.inventorysnapshot.Entity.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ItemList {
    private static ItemList sItemList;
    private Context mContext;
    private static MyAppDatabase mMyAppDatabase;

    public static ItemList get(Context context){
        if(sItemList == null){
            sItemList = new ItemList(context);
        }
        return sItemList;
    }

    private ItemList(Context context) {
        mContext = context.getApplicationContext();
        mMyAppDatabase = Room.databaseBuilder(context, MyAppDatabase.class, "itemDb").allowMainThreadQueries().build();
    }

    public void insertItem(Item item) {
        mMyAppDatabase.myDao().insertItem(item);
    }

    public void deleteItem(Item item){ mMyAppDatabase.myDao().deleteItem(item);}

    public void updateItem(Item item){ mMyAppDatabase.myDao().updateItem(item); }

    public Item getItem(UUID id){
        Item item = mMyAppDatabase.myDao().getItem(id.toString());
        return item;
    }

    public List<Item> getItems(){
        List<Item> items = mMyAppDatabase.myDao().getItems();
        return items;
    }

    public LiveData<List<Item>> getLiveItems(){
        LiveData<List<Item>> items = mMyAppDatabase.myDao().getAllItems();
        return items;
    }

    public File getPhotoFile(Item item){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, item.getPhotoFileName());}

    public int getLength(){
        return getItems().size();
    }

    public List<String> getItemNames(){
        List<String> itemNames = mMyAppDatabase.myDao().getItemNames();
        return itemNames;
    }

    public List<Item> Search(String query){
        List<Item> items = getItems();
        List<Item> results = new ArrayList<>();
        for(Item item: items){
            if(item.getName().toLowerCase().contains(query.toLowerCase())){
                results.add(item);
            }
        }
        return results;
    }


}