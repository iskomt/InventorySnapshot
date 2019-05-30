package com.iskomt.android.inventorysnapshot.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iskomt.android.inventorysnapshot.R;
import com.iskomt.android.inventorysnapshot.entity.Item;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ItemHolder extends RecyclerView.ViewHolder {
    private Item mItem;
    private ImageView mPhotoView;
    private TextView mNameTextView;
    private TextView mQtyTextView;
    private TextView mItemOptionsTextView;

    public ItemHolder(LayoutInflater inflater, ViewGroup parent){
        super(inflater.inflate(R.layout.adapter_list_tem,parent,false));
        mPhotoView = (ImageView) itemView.findViewById(R.id.item_photoholder);
        mNameTextView = (TextView) itemView.findViewById(R.id.item_name);
        mQtyTextView = (TextView) itemView.findViewById(R.id.item_qty);
        mItemOptionsTextView = (TextView) itemView.findViewById(R.id.item_options);
    }

    public void bind(Item item) {
        mItem = item;
        mNameTextView.setText(mItem.getName());
        mQtyTextView.setText(Integer.toString(mItem.getQty()));
        if(mItem.getPhotoPath()!=null){
            Picasso.get().load(new File(mItem.getPhotoPath())).fit().centerCrop().into(mPhotoView);
        }

    }

    public void setItemOptionsListener(View.OnClickListener v){
        mItemOptionsTextView.setOnClickListener(v);
    }

    public TextView getItemOptions(){
        return mItemOptionsTextView;
    }

    public Item getItem() {
        return mItem;
    }
}
