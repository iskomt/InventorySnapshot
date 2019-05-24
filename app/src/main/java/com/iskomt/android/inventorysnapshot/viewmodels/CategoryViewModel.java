package com.iskomt.android.inventorysnapshot.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.iskomt.android.inventorysnapshot.entity.Category;
import com.iskomt.android.inventorysnapshot.repository.CategoryRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository mCategoryRepository;
    private LiveData<List<Category>> mSource;
    private MediatorLiveData<List<Category>> mMediatorLiveData;
    public CategoryViewModel(@NonNull Application application) {
        super(application);
        mCategoryRepository = new CategoryRepository(application);
        mSource = mCategoryRepository.getAllCategories();
        mMediatorLiveData = new MediatorLiveData<>();
        mMediatorLiveData.addSource(mSource, new Observer<List<Category>>() {
                    @Override
                    public void onChanged(List<Category> categories) {
                        mMediatorLiveData.setValue(categories);
                    }
                });
    }

    public void update(Category category){
        mCategoryRepository.updateCategory(category);
    }

    public void delete(Category category){
        mCategoryRepository.deleteCategory(category);
    }

    public Category getCategoryFromId(String id) throws ExecutionException, InterruptedException {
        return mCategoryRepository.getCategoryFromId(id);
    }

    public LiveData<List<Category>> getAllCategories() {
        return mMediatorLiveData;
    }

    public List<Category> Search(String query){
        List<Category> categories = mSource.getValue();
        List<Category> results = new ArrayList<>();
        for(Category category: categories){
            if(category.getName().toLowerCase().contains(query.toLowerCase())){
                results.add(category);
            }
        }
        return results;
    }

    public int getSize(){
        return mSource.getValue().size();
    }
}
