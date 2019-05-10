package com.iskomt.android.inventorysnapshot.Fragments;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.iskomt.android.inventorysnapshot.R;

/**
 * Created by Jose Matundan on 1/16/2019.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
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
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav_home:
                        setFragment(new ItemListFragment());
                        break;
                    case R.id.bottom_nav_search:
                        setFragment(new ItemSearchFragment());
                        break;
                    case R.id.bottom_nav_options:
                        //Toast.makeText(getApplicationContext(), "Nearby", Toast.LENGTH_SHORT).show();
                        setFragment(new OptionsFragment());
                        break;
                }
                return true;
            }
        });

    }

        public void setFragment(Fragment fragment){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }

    public void addFragmentOnTop(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
