package com.iskomt.android.inventorysnapshot.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iskomt.android.inventorysnapshot.database.CategoryDao;
import com.iskomt.android.inventorysnapshot.database.MyAppDatabase;
import com.iskomt.android.inventorysnapshot.entity.Category;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CategoryRepository {
    private CategoryDao mCategoryDao;
    private LiveData<List<Category>> mAllCategories;

    public CategoryRepository(Application application){
        MyAppDatabase database = MyAppDatabase.getInstance(application);
        mCategoryDao = database.categoryDao();
        mAllCategories = mCategoryDao.getAllCategories();
    }

    public void insertCategory(Category category) {
        new InsertCategoryAsyncTask(mCategoryDao).execute(category);
    }

    public void updateCategory(Category category){
        new UpdateCategoryAsyncTask(mCategoryDao).execute(category);
    }

    public void deleteCategory(Category category){
        new DeleteCategoryAsyncTask(mCategoryDao).execute(category);
    }

    public Category getCategoryFromId(String uuidString) throws ExecutionException, InterruptedException {
        return new GetCategoryAsyncTask(mCategoryDao).execute(uuidString).get();
    }

    public LiveData<List<Category>> getAllCategories() {
        return mAllCategories;
    }

    private static class InsertCategoryAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao mCategoryDao;

        private InsertCategoryAsyncTask(CategoryDao categoryDao){
            mCategoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            mCategoryDao.insertCategory(categories[0]);
            return null;
        }
    }

    private static class UpdateCategoryAsyncTask extends AsyncTask<Category, Void, Void>{
        private CategoryDao mCategoryDao;

        private UpdateCategoryAsyncTask(CategoryDao categoryDao){
            mCategoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            mCategoryDao.updateCategory(categories[0]);
            return null;
        }
    }

    private static class DeleteCategoryAsyncTask extends AsyncTask<Category, Void, Void>{
        private CategoryDao mCategoryDao;

        private DeleteCategoryAsyncTask(CategoryDao categoryDao){
            mCategoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            mCategoryDao.deleteCategory(categories[0]);
            return null;
        }
    }

    private static class GetCategoryAsyncTask extends AsyncTask<String, Void, Category>{
        private CategoryDao mCategoryDao;

        private GetCategoryAsyncTask(CategoryDao categoryDao){
            mCategoryDao = categoryDao;
        }

        @Override
        protected Category doInBackground(String... strings) {
            Category category = mCategoryDao.getCategory(strings[0]);
            return category;
        }

    }
}
