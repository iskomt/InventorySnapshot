package com.iskomt.android.inventorysnapshot;

import java.util.UUID;

public class Item {

    private UUID mId;
    private String mName;
    private double mQty;
    private double mPrice;

    public Item(){
        this(UUID.randomUUID());
    }

    public Item(UUID id){
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getQty() {
        return mQty;
    }

    public void setQty(double qty) {
        mQty = qty;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public String getPhotoFileName() {return "IMG_" + getId().toString() + ".jpg";}
}
