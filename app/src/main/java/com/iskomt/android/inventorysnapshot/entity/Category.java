package com.iskomt.android.inventorysnapshot.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "categories")
public class Category implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "CATEGORY_ID")
    private int mId;
    @ColumnInfo(name = "CATEGORY_UUID")
    private String mUUID;
    @ColumnInfo(name = "CATEGORY_NAME")
    private String mName;
    @ColumnInfo(name = "CATEGORY_DESCRIPTION")
    private String mDescription;
    @ColumnInfo(name = "CATEGORY_COLOR")
    private String mColor;
    @ColumnInfo(name = "CATEGORY_COUNT")
    private int mCount;

    public Category() {
    }

    public Category(int id, String UUID, String name, String description, String color, int count) {
        mId = id;
        mUUID = UUID;
        mName = name;
        mDescription = description;
        mColor = color;
        mCount = count;
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mUUID);
        parcel.writeString(mName);
        parcel.writeString(mDescription);
        parcel.writeString(mColor);
        parcel.writeInt(mCount);
    }

    protected Category(Parcel in) {
        mId = in.readInt();
        mUUID = in.readString();
        mName = in.readString();
        mDescription = in.readString();
        mColor = in.readString();
        mCount = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
