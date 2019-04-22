package com.iskomt.android.inventorysnapshot;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by Jose Matundan on 1/16/2019.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    private BottomNavigationView mMainNav;

    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        ItemListFragment itemListFragment = new ItemListFragment();
                        setFragment(itemListFragment);
                        return true;
                    case R.id.nav_search:
                        ItemSearchFragment itemSearchFragment = new ItemSearchFragment();
                        setFragment(itemSearchFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

        private void setFragment(Fragment fragment){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
}
