package com.iskomt.android.inventorysnapshot.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.iskomt.android.inventorysnapshot.entity.Item;
import com.iskomt.android.inventorysnapshot.repository.ItemRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ItemViewModel extends AndroidViewModel {
    public final static String SORT_NAME_ASC = "ITEM_NAME_ASC";
    public final static String SORT_NAME_DESC = "ITEM_NAME_DESC";
    public final static String SORT_QTY_ASC = "ITEM_QTY_ASC";
    public final static String SORT_QTY_DESC = "ITEM_QTY_DESC";
    public final static String SORT_PRICE_ASC = "ITEM_PRICE_ASC";
    public final static String SORT_PRICE_DESC = "ITEM_PRICE_DESC";
    public final static String SORT_ADDED = "ITEM_ADDED";

    private ItemRepository mItemRepository;
    private LiveData<List<Item>> mSource;
    private MediatorLiveData<List<Item>> mMediatorLiveData;
    public ItemViewModel(@NonNull Application application) {
        super(application);
        mItemRepository = new ItemRepository(application);
        mSource = mItemRepository.getAllItems();
        mMediatorLiveData = new MediatorLiveData<>();
        mMediatorLiveData.addSource(mSource, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                mMediatorLiveData.setValue(items);
            }
        });
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

    public LiveData<List<Item>> getAllItems() {
        return mMediatorLiveData;
    }

    public File getPhotoFile(Context context, Item item){
        File filesDir = context.getFilesDir();
        return new File(filesDir, item.getPhotoFileName());}

    public List<Item> Search(String query){
        List<Item> items = mSource.getValue();
        List<Item> results = new ArrayList<>();
        for(Item item: items){
            if(item.getName().toLowerCase().contains(query.toLowerCase())){
                results.add(item);
            }
        }
        return results;
    }

    public void sortList(String sortOrder){
        mMediatorLiveData.removeSource(mSource);
        mSource = mItemRepository.getSortedItems(sortOrder);
        mMediatorLiveData.addSource(mSource, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                mMediatorLiveData.setValue(items);
            }
        });
    }

    public int getSize(){
        return mSource.getValue().size();
    }

}
