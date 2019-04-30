package com.iskomt.android.inventorysnapshot;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.iskomt.android.inventorysnapshot.Fragments.ItemFragment;
import com.iskomt.android.inventorysnapshot.Fragments.ItemListFragment;
import com.iskomt.android.inventorysnapshot.Fragments.SingleFragmentActivity;
import com.iskomt.android.inventorysnapshot.Model.Item;

public class ItemListActivity extends SingleFragmentActivity implements ItemListFragment.Callbacks, ItemFragment.Callbacks{
    public static final int REQUEST_DONE=1;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        } else if (requestCode == REQUEST_DONE) {

            ItemListFragment listFragment = (ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            listFragment.updateUI();
            Toast.makeText(getApplicationContext(), "I am from activity result", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemUpdated(Item item) {
        ItemListFragment listFragment = (ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
        Toast.makeText(getApplicationContext(), "ITem is updatesdadsfasdfa", Toast.LENGTH_SHORT).show();
    }
}
