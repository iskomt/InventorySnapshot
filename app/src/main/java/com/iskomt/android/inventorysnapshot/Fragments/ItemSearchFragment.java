package com.iskomt.android.inventorysnapshot.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iskomt.android.inventorysnapshot.ItemList;
import com.iskomt.android.inventorysnapshot.Entity.Item;
import com.iskomt.android.inventorysnapshot.PictureUtils;
import com.iskomt.android.inventorysnapshot.R;

import java.util.ArrayList;
import java.util.List;

public class ItemSearchFragment extends Fragment {

    private String TAG = "com.iskomt.android.inventorysnapshot.itemlistfragment";
    private RecyclerView mItemRecyclerView;
    private ItemAdapter mAdapter;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onItemSearch(Item item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mItemRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //Log.d(TAG, "QueryTextChange: " + s);
                List<Item> results = ItemList.get(getActivity()).Search(s);
                mAdapter.setFilter(results);
                return false;
            }
        });
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void updateUI(){
        ItemList itemList = ItemList.get(getActivity());
        List<Item> items = itemList.getItems();

        if (mAdapter == null){
            mAdapter = new ItemAdapter(items);
            mItemRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Item mItem;
        private ImageView mPhotoView;
        private TextView mNameTextView;
        private TextView mQtyTextView;
        private TextView mItemOptionsTextView;

        public ItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_tem,parent,false));

            itemView.setOnClickListener(this);
            mPhotoView = (ImageView) itemView.findViewById(R.id.item_photoholder);
            mNameTextView = (TextView) itemView.findViewById(R.id.item_name);
            mQtyTextView = (TextView) itemView.findViewById(R.id.item_qty);
            mItemOptionsTextView = (TextView) itemView.findViewById(R.id.item_options);
        }

        @Override
        public void onClick(View view) {
            mCallbacks.onItemSearch(mItem);
        }

        public void bind(Item item) {
            mItem = item;
            mNameTextView.setText(mItem.getName());
            mQtyTextView.setText(Integer.toString(mItem.getQty()));
            if(retrieveImage(mItem.getSource())!=null) {
                mPhotoView.setImageBitmap(retrieveImage(mItem.getSource()));
            }
        }
        public Bitmap retrieveImage(int source){
            Bitmap bitmap = null;
            try {
                //0 means photo is from the camera
                if(source==0){
                    bitmap = PictureUtils.getScaledBitmap(mItem.getPhotoPath(), getActivity());
                }else if(source==1){
                    //1 means photo is from the gallery
                    if(mItem.getImage()!=null){
                        bitmap = BitmapFactory.decodeByteArray(mItem.getImage(),0,mItem.getImage().length);
                    }
                }else{
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_not_set);
                }
            }catch(Exception e){ }
            return bitmap;
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<Item> mItemList;

        public ItemAdapter(List<Item> itemList){mItemList = new ArrayList<>(); }
        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ItemHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
            holder.mItemOptionsTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Create a pop up menu
                    PopupMenu popup = new PopupMenu(getContext(),holder.mItemOptionsTextView);
                    popup.inflate(R.menu.item_list_options);
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch(menuItem.getItemId()){
                                case R.id.item_list_delete_item:
                                    //Toast.makeText(getContext(), "Test Delete Successful", Toast.LENGTH_SHORT).show();
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Delete Confirmation")
                                            .setMessage("Are you sure you want to delete this item?")
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    ItemList.get(getContext()).deleteItem(mItemList.get(position));
                                                    updateUI();
                                                }})
                                            .setNegativeButton(android.R.string.no, null).show();
                                    break;
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

        public void setItems(List<Item> items){mItemList = items;}

        public void setFilter(List<Item> newItems){
            mItemList = newItems;
            notifyDataSetChanged();
        }


    }

}
