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
public interface ItemDao {//Data Access Object

    /*
    * Insert item into table*/
    @Insert
    void insertItem(Item item);

    @Update
    void updateItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Query("select * from items")
    List<Item> getItems();

    @Query("select * from items")
    LiveData<List<Item>> getAllItems();

    @Query("select * from items where :id = ITEM_UUID")
    Item getItem(String id);

    @Query("select * from items where :id = ITEM_UUID")
    LiveData<Item> getLiveItem(String id);

    @Query("select ITEM_NAME from items")
    List<String> getItemNames();

    @Query("select * from items where :name = ITEM_NAME")
    public Item getItemFromName(String name);

}
