package com.iskomt.android.inventorysnapshot;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.iskomt.android.inventorysnapshot.repository.ItemList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ItemListTest {
    private ItemList mItemList;


    @Before
    public void setUp() throws Exception{
        mItemList = ItemList.get(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testPreCondition(){
        assertNotNull(mItemList);
    }
}
