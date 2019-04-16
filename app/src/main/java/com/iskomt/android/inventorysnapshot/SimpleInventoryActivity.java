package com.iskomt.android.inventorysnapshot;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class SimpleInventoryActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SimpleInventoryFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
