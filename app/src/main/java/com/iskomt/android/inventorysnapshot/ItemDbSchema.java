package com.iskomt.android.inventorysnapshot;

public class ItemDbSchema {
    public static final class ItemTable {
        public static final String NAME = "items";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String QUANTITY = "quantity";
            public static final String PRICE = "price";
        }
    }
}
