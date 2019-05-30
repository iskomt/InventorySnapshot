package com.iskomt.android.inventorysnapshot.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iskomt.android.inventorysnapshot.database.ItemDao;
import com.iskomt.android.inventorysnapshot.database.MyAppDatabase;
import com.iskomt.android.inventorysnapshot.entity.Item;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ItemRepository {
    private ItemDao mItemDao;
    private LiveData<List<Item>> mAllItems;

    public ItemRepository(Application application){
        MyAppDatabase database = MyAppDatabase.getInstance(application);
        mItemDao = database.itemDao();
        mAllItems = mItemDao.getAllItems();
    }

    public void insertItem(Item item) {
        new InsertItemAsyncTask(mItemDao).execute(item);
    }

    public void updateItem(Item item){
        new UpdateItemAsyncTask(mItemDao).execute(item);
    }

    public void deleteItem(Item item){
        new DeleteItemAsyncTask(mItemDao).execute(item);
    }

    public Item getItemFromId(String id) throws ExecutionException, InterruptedException {
        return new GetItemAsyncTask(mItemDao).execute(id).get();
    }

    public LiveData<List<Item>> getAllItems() {
        return mAllItems;
    }

    public LiveData<List<Item>> getSortedItems(String sortOrder){
        switch(sortOrder){
            case "ITEM_NAME_ASC":
                return mItemDao.getItemsNameASC();
            case "ITEM_NAME_DESC":
                return mItemDao.getItemsNameDESC();
            case "ITEM_QTY_ASC":
                return mItemDao.getItemsQtyASC();
            case "ITEM_QTY_DESC":
                return mItemDao.getItemsQtyASC();
            case "ITEM_PRICE_ASC":
                return mItemDao.getItemsPriceASC();
            case "ITEM_PRICE_DESC":
                return mItemDao.getItemsPriceDESC();
            case "ITEM_ADDED":
                return mItemDao.getAllItems();
            default:
                return null;
        }
    }

    private static class InsertItemAsyncTask extends AsyncTask<Item, Void, Void>{
        private ItemDao mItemDao;

        private InsertItemAsyncTask(ItemDao itemDao){
            mItemDao = itemDao;
        }
        @Override
        protected Void doInBackground(Item... items) {
            mItemDao.insertItem(items[0]);
            return null;
        }
    }

    private static class UpdateItemAsyncTask extends AsyncTask<Item, Void, Void>{
        private ItemDao mItemDao;

        private UpdateItemAsyncTask(ItemDao itemDao){
            mItemDao = itemDao;
        }
        @Override
        protected Void doInBackground(Item... items) {
            mItemDao.updateItem(items[0]);
            return null;
        }
    }

    private static class DeleteItemAsyncTask extends AsyncTask<Item, Void, Void>{
        private ItemDao mItemDao;

        private DeleteItemAsyncTask(ItemDao itemDao){
            mItemDao = itemDao;
        }
        @Override
        protected Void doInBackground(Item... items) {
            mItemDao.deleteItem(items[0]);
            return null;
        }
    }

    private static class GetItemAsyncTask extends AsyncTask<String, Void, Item>{
        private ItemDao mItemDao;

        private GetItemAsyncTask(ItemDao itemDao){
            mItemDao = itemDao;
        }

        @Override
        protected Item doInBackground(String... strings) {
            Item item = mItemDao.getItem(strings[0]);
            return item;
        }
    }
}
