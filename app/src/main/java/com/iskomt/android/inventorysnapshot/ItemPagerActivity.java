package com.iskomt.android.inventorysnapshot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.iskomt.android.inventorysnapshot.Fragments.ItemFragment;
import com.iskomt.android.inventorysnapshot.Entity.Item;
import com.iskomt.android.inventorysnapshot.ViewModels.ItemViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemPagerActivity extends AppCompatActivity implements ItemFragment.Callbacks {
    private static final String EXTRA_ITEM_ID = "com.iskomt.android.inventorysnapshot.item_id";

    private ViewPager mViewPager;
    private List<Item> mItems;
    private ItemViewModel mItemViewModel;
    private ViewPagerAdapter mViewPagerAdapter;

    public static Intent newIntent(Context packageContext, UUID itemId) {
        Intent intent = new Intent(packageContext, ItemPagerActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_pager);


        mViewPager = (ViewPager) findViewById(R.id.item_view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPagerAdapter = new ViewPagerAdapter(fragmentManager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mItemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        mItemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                //Toast.makeText(getApplicationContext(), "OnChanged ", Toast.LENGTH_SHORT).show();
                mViewPagerAdapter.setItems(items);
                UUID itemId = (UUID) getIntent().getSerializableExtra(EXTRA_ITEM_ID);
                mViewPagerAdapter.setCurrentItem(itemId);
            }
        });
    }

    @Override
    public void onItemUpdated(Item item) {
        //setResult(RESULT_OK);
        finish();
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter{
    private List<Item> mItems = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Item item = mItems.get(position);
            return ItemFragment.newInstance(item.getOriginalUUID());
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        public void setItems(List<Item> items){
            mItems = items;
            notifyDataSetChanged();
        }

        public void setCurrentItem(UUID itemId){
            for(int i=0;i<getCount();i++){
                if(mItems.get(i).getOriginalUUID().equals(itemId)){
                    mViewPager.setCurrentItem(i);
                    break;
                }
            }
        }
    }
}
