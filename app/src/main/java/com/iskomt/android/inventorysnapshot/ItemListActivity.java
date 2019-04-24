package com.iskomt.android.inventorysnapshot;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.iskomt.android.inventorysnapshot.Model.Item;

public class ItemListActivity extends SingleFragmentActivity implements ItemListFragment.Callbacks, ItemFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        return new ItemListFragment();
    }

    @Override
    public void onItemSelected(Item item) {
        Intent intent = ItemPagerActivity.newIntent(this, item.getId());
        startActivity(intent);
    }

    @Override
    public void onItemUpdated(Item item) {
        ItemListFragment listFragment = (ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
