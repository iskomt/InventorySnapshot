package com.iskomt.android.inventorysnapshot.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.iskomt.android.inventorysnapshot.Entity.Item;
import com.iskomt.android.inventorysnapshot.Repository.ItemRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ItemViewModel extends AndroidViewModel {
    private ItemRepository mItemRepository;
    private LiveData<List<Item>> mAllItems;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        mItemRepository = new ItemRepository(application);
        mAllItems = mItemRepository.getAllItems();
    }

    public void insert(Item item){
        mItemRepository.insertItem(item);
    }

    public void update(Item item){
        mItemRepository.updateItem(item);
    }

    public void delete(Item item){
        mItemRepository.deleteItem(item);
    }

    public Item getItemFromId(String id) throws ExecutionException, InterruptedException {
        return mItemRepository.getItemFromId(id);
    }

    public LiveData<Item> getItem(String id){
        return mItemRepository.getItem(id);
    }
    public LiveData<List<Item>> getAllItems() {
        return mAllItems;
    }

    public File getPhotoFile(Context context, Item item){
        File filesDir = context.getFilesDir();
        return new File(filesDir, item.getPhotoFileName());}

    public List<Item> Search(String query){
        List<Item> items = mAllItems.getValue();
        List<Item> results = new ArrayList<>();
        for(Item item: items){
            if(item.getName().toLowerCase().contains(query.toLowerCase())){
                results.add(item);
            }
        }
        return results;
    }

    public void sortList(String choice){
        List<Item> items = mAllItems.getValue();
    }



    public int getSize(){
        return mAllItems.getValue().size();
    }
}
