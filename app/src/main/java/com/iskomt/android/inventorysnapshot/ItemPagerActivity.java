package com.iskomt.android.inventorysnapshot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.iskomt.android.inventorysnapshot.Fragments.ItemFragment;
import com.iskomt.android.inventorysnapshot.Fragments.ItemListFragment;
import com.iskomt.android.inventorysnapshot.Model.Item;

import java.util.List;
import java.util.UUID;

public class ItemPagerActivity extends AppCompatActivity implements ItemFragment.Callbacks {
    private static final String EXTRA_ITEM_ID = "com.iskomt.android.inventorysnapshot.item_id";

    private ViewPager mViewPager;
    private List<Item> mItems;

    public static Intent newIntent(Context packageContext, UUID itemId) {
        Intent intent = new Intent(packageContext, ItemPagerActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_pager);

        UUID itemId = (UUID) getIntent().getSerializableExtra(EXTRA_ITEM_ID);

        mViewPager = (ViewPager) findViewById(R.id.item_view_pager);

        mItems = ItemList.get(this).getItems();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Item item = mItems.get(position);
                return ItemFragment.newInstance(item.getOriginalUUID());
            }

            @Override
            public int getCount() {
                return mItems.size();
            }
        });

        for(int i=0;i<mItems.size();i++){
            if(mItems.get(i).getOriginalUUID().equals(itemId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }

    @Override
    public void onItemUpdated(Item item) {
        //Toast.makeText(getApplicationContext(), "Contextcontextcontext", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
