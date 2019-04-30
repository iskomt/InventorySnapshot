package com.iskomt.android.inventorysnapshot.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Blob;
import java.util.UUID;

@Entity(tableName = "items")
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int mId;
    @ColumnInfo(name = "ITEM_UUID")
    private String mUUID;
    @ColumnInfo(name = "ITEM_NAME")
    private String mName;
    @ColumnInfo(name = "ITEM_QUANTITY")
    private double mQty;
    @ColumnInfo(name = "ITEM_PRICE")
    private double mPrice;
    @ColumnInfo(name = "ITEM_PHOTO_PATH")
    private String mPhotoPath;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;
    @ColumnInfo(name = "ITEM_PHOTO_SOURCE_FLAG")
    private int source;

    public Item() {
        this(UUID.randomUUID());
    }

    public Item(UUID UUID) {
        mUUID = UUID.toString();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getUUID() {
        return mUUID;
    }

    public void setUUID(String UUID) {
        mUUID = UUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID.toString();
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

    public String getPhotoFileName() {
        return "IMG_" + getOriginalUUID().toString() + ".jpg";
    }

    public void setPhotoPath(String path) {
        mPhotoPath = path;
    }

    public String getPhotoPath() {
        return mPhotoPath;
    }

    public UUID getOriginalUUID() {
        return UUID.fromString(mUUID);
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
