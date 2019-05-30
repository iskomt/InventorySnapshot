package com.iskomt.android.inventorysnapshot.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.iskomt.android.inventorysnapshot.R;
import com.iskomt.android.inventorysnapshot.entity.Item;
import com.iskomt.android.inventorysnapshot.fragments.IncDecFragment;
import com.iskomt.android.inventorysnapshot.viewmodels.ItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
    private List<Item> mItemList = new ArrayList<>();
    private ItemViewModel mItemViewModel;
    private Context mContext;

    public ItemAdapter(Context context, ItemViewModel itemViewModel){
        mContext = context;
        mItemViewModel = itemViewModel;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ItemHolder(layoutInflater,parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
        holder.setItemOptionsListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Create a pop up menu
                PopupMenu popup = new PopupMenu(mContext,holder.getItemOptions());
                popup.inflate(R.menu.menu_fragment_item_list_options);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case R.id.item_list_delete_item:
                                //Toast.makeText(getContext(), "Test Delete Successful", Toast.LENGTH_SHORT).show();
                                new AlertDialog.Builder(mContext)
                                        .setTitle("Delete Confirmation")
                                        .setMessage("Are you sure you want to delete this item?")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                //ItemList.get(getContext()).deleteItem(mItemList.get(position));
                                                mItemViewModel.delete(mItemList.get(position));
                                                //updateUI();
                                            }})
                                        .setNegativeButton(android.R.string.no, null).show();
                                break;
                            case R.id.item_list_quick_qty:
                                IncDecFragment mIncDecFragment = IncDecFragment.newInstance(mItemList.get(position).getOriginalUUID());
                                mIncDecFragment.show(((FragmentActivity)mContext).getSupportFragmentManager(), "fragment_edit_name");
                            default:
                                break;
                        }
                        return false;
                    }
                });

            }
        });

        Item item = mItemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void setItems(List<Item> items){
        mItemList = items;
        notifyDataSetChanged();
    }

    public void setFilter(List<Item> newItems){
        mItemList = newItems;
        notifyDataSetChanged();
    }
}