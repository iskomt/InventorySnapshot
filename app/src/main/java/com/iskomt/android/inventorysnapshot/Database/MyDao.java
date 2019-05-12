package com.iskomt.android.inventorysnapshot.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.iskomt.android.inventorysnapshot.Entity.Item;

import java.util.List;

@Dao
public interface MyDao {//Data Access Object

    /*
    * Insert item into table*/
    @Insert
    public void addItem(Item item);

    @Update
    public void updateItem(Item item);

    @Delete
    public void deleteItem(Item item);

    @Query("select * from items")
    public List<Item> getItems();

    @Query("select * from items")
    public LiveData<List<Item>> getLiveItems();

    @Query("select * from items where :id = ITEM_UUID")
    public Item getItem(String id);

    @Query("select ITEM_NAME from items")
    public List<String> getItemNames();

    @Query("select * from items where :name = ITEM_NAME")
    public Item getItemFromName(String name);

}
