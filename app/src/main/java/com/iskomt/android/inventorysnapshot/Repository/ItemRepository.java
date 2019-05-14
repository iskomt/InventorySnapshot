package com.iskomt.android.inventorysnapshot.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iskomt.android.inventorysnapshot.Database.MyAppDatabase;
import com.iskomt.android.inventorysnapshot.Database.MyDao;
import com.iskomt.android.inventorysnapshot.Entity.Item;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ItemRepository {
    private MyDao mMyDao;
    private LiveData<List<Item>> mAllItems;
    private static LiveData<Item> mItem;

    public ItemRepository(Application application){
        MyAppDatabase database = MyAppDatabase.getInstance(application);
        mMyDao = database.myDao();
        mAllItems = mMyDao.getAllItems();
    }

    public void insertItem(Item item) {
        new InsertItemAsyncTask(mMyDao).execute(item);
    }

    public void updateItem(Item item){
        new UpdateItemAsyncTask(mMyDao).execute(item);
    }

    public void deleteItem(Item item){
        new DeleteItemAsyncTask(mMyDao).execute(item);
    }

    public Item getItemFromId(String id) throws ExecutionException, InterruptedException {
        return new GetItemAsyncTask(mMyDao).execute(id).get();
    }

    public LiveData<Item> getItem(String id){
        new GetLiveItemAsyncTask(mMyDao).execute(id);
        return mItem;
    }

    public LiveData<List<Item>> getAllItems() {
        return mAllItems;
    }

    private static void setItem(LiveData<Item> item){
        mItem = item;
    }

    private static class InsertItemAsyncTask extends AsyncTask<Item, Void, Void>{
        private MyDao mMyDao;

        private InsertItemAsyncTask(MyDao myDao){
            mMyDao = myDao;
        }
        @Override
        protected Void doInBackground(Item... items) {
            mMyDao.insertItem(items[0]);
            return null;
        }
    }

    private static class UpdateItemAsyncTask extends AsyncTask<Item, Void, Void>{
        private MyDao mMyDao;

        private UpdateItemAsyncTask(MyDao myDao){
            mMyDao = myDao;
        }
        @Override
        protected Void doInBackground(Item... items) {
            mMyDao.updateItem(items[0]);
            return null;
        }
    }

    private static class DeleteItemAsyncTask extends AsyncTask<Item, Void, Void>{
        private MyDao mMyDao;

        private DeleteItemAsyncTask(MyDao myDao){
            mMyDao = myDao;
        }
        @Override
        protected Void doInBackground(Item... items) {
            mMyDao.deleteItem(items[0]);
            return null;
        }
    }

    private static class GetItemAsyncTask extends AsyncTask<String, Void, Item>{
        private MyDao mMyDao;

        private GetItemAsyncTask(MyDao myDao){
            mMyDao = myDao;
        }

        @Override
        protected Item doInBackground(String... strings) {
            Item item = mMyDao.getItem(strings[0]);
            return item;
        }

    }

    private static class GetLiveItemAsyncTask extends AsyncTask<String, Void, LiveData<Item>>{
        private MyDao mMyDao;

        private GetLiveItemAsyncTask(MyDao myDao){
            mMyDao = myDao;
        }

        @Override
        protected LiveData<Item> doInBackground(String... strings) {
            LiveData<Item> item = mMyDao.getLiveItem(strings[0]);
            return item;
        }

        @Override
        protected void onPostExecute(LiveData<Item> itemLiveData) {
            setItem(itemLiveData);
        }
    }
}
