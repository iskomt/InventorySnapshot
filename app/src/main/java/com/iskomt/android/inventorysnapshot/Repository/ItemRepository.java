package com.iskomt.android.inventorysnapshot.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iskomt.android.inventorysnapshot.Database.ItemDao;
import com.iskomt.android.inventorysnapshot.Database.MyAppDatabase;
import com.iskomt.android.inventorysnapshot.Entity.Item;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ItemRepository {
    private ItemDao mItemDao;
    private LiveData<List<Item>> mAllItems;
    private static LiveData<Item> mItem;

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

    public LiveData<Item> getItem(String id){
        new GetLiveItemAsyncTask(mItemDao).execute(id);
        return mItem;
    }

    public LiveData<List<Item>> getAllItems() {
        return mAllItems;
    }

    private static void setItem(LiveData<Item> item){
        mItem = item;
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

    private static class GetLiveItemAsyncTask extends AsyncTask<String, Void, LiveData<Item>>{
        private ItemDao mItemDao;

        private GetLiveItemAsyncTask(ItemDao itemDao){
            mItemDao = itemDao;
        }

        @Override
        protected LiveData<Item> doInBackground(String... strings) {
            LiveData<Item> item = mItemDao.getLiveItem(strings[0]);
            return item;
        }

        @Override
        protected void onPostExecute(LiveData<Item> itemLiveData) {
            setItem(itemLiveData);
        }
    }
}
