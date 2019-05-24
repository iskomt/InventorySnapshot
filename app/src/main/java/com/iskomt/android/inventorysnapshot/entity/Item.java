package com.iskomt.android.inventorysnapshot.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "items", foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "CATEGORY_ID",
        childColumns = "CATEGORY_ID") )
public class Item implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ITEM_ID")
    private int mId;
    @ColumnInfo(name = "ITEM_UUID")
    private String mUUID;
    @ColumnInfo(name = "ITEM_NAME")
    private String mName;
    @ColumnInfo(name = "ITEM_QUANTITY")
    private int mQty;
    @ColumnInfo(name = "ITEM_PRICE")
    private double mPrice;
    @ColumnInfo(name = "ITEM_PHOTO_PATH")
    private String mPhotoPath;
    @ColumnInfo(name = "ITEM_PHOTO_SOURCE_FLAG")
    private int mSource;
    @ColumnInfo(name = "CATEGORY_ID")
    private String mCategoryId;


    public Item() { this(UUID.randomUUID());
    }

    public Item(UUID UUID) {
        mUUID = UUID.toString();
        mName = "";
        mQty = 0;
        mPrice = 0;
        mPhotoPath = "";
        mSource = -1;
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

    public UUID getOriginalUUID() {
        return UUID.fromString(mUUID);
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

    public int getQty() {
        return mQty;
    }

    public void setQty(int qty) {
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

    public int getSource() {
        return mSource;
    }

    public void setSource(int source) {
        this.mSource = source;
    }

    public String getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(String categoryId) {
        mCategoryId = categoryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Item(Parcel in) {
        mId = in.readInt();
        mUUID = in.readString();
        mName = in.readString();
        mQty = in.readInt();
        mPrice = in.readDouble();
        mPhotoPath = in.readString();
        mSource = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mUUID);
        dest.writeString(mName);
        dest.writeInt(mQty);
        dest.writeDouble(mPrice);
        dest.writeString(mPhotoPath);
        dest.writeInt(mSource);
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

}
