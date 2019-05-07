package com.iskomt.android.inventorysnapshot;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.iskomt.android.inventorysnapshot.Fragments.ItemFragment;
import com.iskomt.android.inventorysnapshot.Fragments.ItemListFragment;
import com.iskomt.android.inventorysnapshot.Fragments.ItemSearchFragment;
import com.iskomt.android.inventorysnapshot.Fragments.SingleFragmentActivity;
import com.iskomt.android.inventorysnapshot.Model.Item;

public class ItemListActivity extends SingleFragmentActivity implements ItemListFragment.Callbacks, ItemFragment.Callbacks, ItemSearchFragment.Callbacks {
    public static final int REQUEST_DONE=1,REQUEST_SEARCH=2;

    @Override
    protected Fragment createFragment() {
        return new ItemListFragment();
    }

    @Override
    public void onItemSelected(Item item) {
        Intent intent = ItemPagerActivity.newIntent(this, item.getOriginalUUID());
        startActivityForResult(intent,REQUEST_DONE);
    }

    @Override
    public void onItemSearch(Item item) {
        //getSupportFragmentManager().beginTransaction().addToBackStack(null);
        Intent intent = ItemPagerActivity.newIntent(this, item.getOriginalUUID());
        startActivityForResult(intent,REQUEST_SEARCH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        } else if (requestCode == REQUEST_DONE) {
            ItemListFragment listFragment = (ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            listFragment.updateUI();
        } else if (requestCode == REQUEST_SEARCH) {
            ItemSearchFragment searchFragment = (ItemSearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            searchFragment.updateUI();
        }
    }

    @Override
    public void onItemUpdated(Item item) {
        getSupportFragmentManager().popBackStack();
        /*ItemListFragment listFragment = (ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();*/
        //Toast.makeText(getApplicationContext(), "ITem is updatesdadsfasdfa", Toast.LENGTH_SHORT).show();
    }

}
