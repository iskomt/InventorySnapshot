package com.iskomt.android.inventorysnapshot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.iskomt.android.inventorysnapshot.fragments.AboutFragment;
import com.iskomt.android.inventorysnapshot.fragments.HelpFragment;
import com.iskomt.android.inventorysnapshot.fragments.ItemFragment;
import com.iskomt.android.inventorysnapshot.fragments.ItemListFragment;
import com.iskomt.android.inventorysnapshot.fragments.ItemSearchFragment;
import com.iskomt.android.inventorysnapshot.fragments.LogFragment;
import com.iskomt.android.inventorysnapshot.fragments.OptionsFragment;
import com.iskomt.android.inventorysnapshot.fragments.SettingsFragment;
import com.iskomt.android.inventorysnapshot.fragments.SingleFragmentActivity;
import com.iskomt.android.inventorysnapshot.entity.Item;

public class ItemListActivity extends SingleFragmentActivity implements ItemListFragment.Callbacks, ItemFragment.Callbacks, ItemSearchFragment.Callbacks, OptionsFragment.Callbacks {
    public static final int REQUEST_DONE=1,REQUEST_SEARCH=2;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    protected Fragment createFragment() {
        return new ItemListFragment();
    }

    @Override
    public void onItemSelected(Item item) {
        Intent intent = ItemPagerActivity.newIntent(this, item.getOriginalUUID());
        //startActivityForResult(intent,REQUEST_DONE);
        startActivity(intent);
    }

    @Override
    public void onItemSearch(Item item) {
        //getSupportFragmentManager().beginTransaction().addToBackStack(null);
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .commit();
        Intent intent = ItemPagerActivity.newIntent(this, item.getOriginalUUID());
        startActivityForResult(intent,REQUEST_SEARCH);
    }

    @Override
    public void onOptionSelected(String option) {
        String[] options = getResources().getStringArray(R.array.options);
        //getSupportFragmentManager().beginTransaction().addToBackStack(null).commit();
        if(option.equalsIgnoreCase(options[0])){
            addFragmentOnTop(new SettingsFragment());
        } else if(option.equalsIgnoreCase(options[1])){
            addFragmentOnTop(new AboutFragment());
        } else if(option.equalsIgnoreCase(options[2])){
            addFragmentOnTop(new HelpFragment());
        } else if(option.equalsIgnoreCase(options[3])){
            addFragmentOnTop(new LogFragment());
        } else {}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        } else if (requestCode == REQUEST_DONE) {
            //ItemListFragment listFragment = (ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            //listFragment.updateUI();
            getSupportFragmentManager().popBackStack();
        } else if (requestCode == REQUEST_SEARCH) {
            ItemSearchFragment searchFragment = (ItemSearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            //searchFragment.updateUI();
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
