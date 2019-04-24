package com.iskomt.android.inventorysnapshot;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.iskomt.android.inventorysnapshot.ItemDbSchema.ItemTable;
import com.iskomt.android.inventorysnapshot.Model.Item;

import java.util.UUID;

public class ItemCursorWrapper extends CursorWrapper {
    public ItemCursorWrapper(Cursor cursor){ super(cursor); }

    public Item getItem() {
        String uuidString = getString(getColumnIndex(ItemTable.Cols.UUID));
        String name = getString(getColumnIndex(ItemTable.Cols.NAME));
        double qty = getDouble(getColumnIndex(ItemTable.Cols.QUANTITY));
        double price = getDouble(getColumnIndex(ItemTable.Cols.PRICE));
         String photo = getString(getColumnIndex(ItemTable.Cols.PHOTO));

        Item item = new Item(UUID.fromString(uuidString));
        item.setName(name);
        item.setQty(qty);
        item.setPrice(price);
        item.setPhotoPath(photo);

        return item;
    }
}
