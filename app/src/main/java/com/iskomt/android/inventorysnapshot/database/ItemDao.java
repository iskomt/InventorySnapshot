package com.iskomt.android.inventorysnapshot.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.iskomt.android.inventorysnapshot.entity.Item;

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
    Item getItemFromName(String name);

    @Query("select * from items Order by upper(ITEM_NAME) ASC")
    LiveData<List<Item>> getItemsNameASC();

    @Query("select * from items Order by upper(ITEM_NAME) DESC")
    LiveData<List<Item>> getItemsNameDESC();

    @Query("select * from items Order by ITEM_QUANTITY ASC")
    LiveData<List<Item>> getItemsQtyASC();

    @Query("select * from items Order by ITEM_QUANTITY DESC")
    LiveData<List<Item>> getItemsQtyDESC();

    @Query("select * from items Order by ITEM_PRICE ASC")
    LiveData<List<Item>> getItemsPriceASC();

    @Query("select * from items Order by ITEM_PRICE DESC")
    LiveData<List<Item>> getItemsPriceDESC();
}
