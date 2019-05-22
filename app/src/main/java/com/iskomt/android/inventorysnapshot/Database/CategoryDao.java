package com.iskomt.android.inventorysnapshot.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.iskomt.android.inventorysnapshot.Entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {//Data Access Object

    /*
     * Insert item into table*/
    @Insert
    void insertCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    @Query("select * from categories")
    List<Category> getCategories();

    @Query("select * from categories")
    LiveData<List<Category>> getAllCategories();

    @Query("select * from categories where :uuidString = CATEGORY_UUID")
    Category getCategory(String uuidString);

    @Query("select CATEGORY_NAME from categories")
    List<String> getCategoryNames();

    @Query("select * from categories where :name = CATEGORY_NAME")
    public Category getCategoryFromName(String name);

}