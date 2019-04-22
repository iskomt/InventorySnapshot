package com.iskomt.android.inventorysnapshot;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;

public class ItemListActivity extends SingleFragmentActivity implements ItemListFragment.Callbacks, ItemFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        return new ItemListFragment();
    }

    @Override
    public void onItemSelected(Item item) {

    }

    @Override
    public void onItemUpdated(Item item) {
        ItemListFragment listFragment = (ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
