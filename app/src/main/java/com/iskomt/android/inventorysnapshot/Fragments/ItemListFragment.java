package com.iskomt.android.inventorysnapshot.Fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.DrawableUtils;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iskomt.android.inventorysnapshot.ItemList;
import com.iskomt.android.inventorysnapshot.ItemPagerActivity;
import com.iskomt.android.inventorysnapshot.Model.Item;
import com.iskomt.android.inventorysnapshot.PictureUtils;
import com.iskomt.android.inventorysnapshot.R;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ItemListFragment extends Fragment {

    private String TAG = "com.iskomt.android.inventorysnapshot.itemlistfragment";
    private RecyclerView mItemRecyclerView;
    private ItemAdapter mAdapter;

    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onItemSelected(Item item);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        mItemRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        rfaLayout = view.findViewById(R.id.activity_main_rfal);
        rfaBtn = view.findViewById(R.id.activity_main_rfab);

        //Floating action button
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(getContext());
        rfaContent.setOnRapidFloatingActionContentLabelListListener(new RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener() {
            @Override
            public void onRFACItemLabelClick(int position, RFACLabelItem item) {
                rfabHelper.toggleContent();
                switch(position){
                    case 0:
                        Item i = new Item();
                        i.setName("Item " + ItemList.get(getActivity()).getLength());
                        ItemList.get(getActivity()).addItem(i);
                        updateUI();
                        mCallbacks.onItemSelected(i);
                        break;
                    default:
                    break;
                }

            }

            @Override
            public void onRFACItemIconClick(int position, RFACLabelItem item) {
                //Toast.makeText(getContext(), "clicked icon: " + position, Toast.LENGTH_SHORT).show();
                rfabHelper.toggleContent();
                switch(position){
                    case 0:
                        Item i = new Item();
                        ItemList.get(getActivity()).addItem(i);
                        updateUI();
                        mCallbacks.onItemSelected(i);
                        break;
                    default:
                        break;

                }

            }
        });

        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Add Item")
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(0)
        );

        rfaContent
                .setItems(items)
                .setIconShadowColor(0xff888888);
        rfabHelper = new RapidFloatingActionHelper(
                getContext(),
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();


        if(savedInstanceState != null) {
        }

        updateUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.main_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.main_menu_settings:
                return true;
            case R.id.main_menu_account:
                return true;
            case R.id.main_menu_about:
                return true;
            case R.id.main_menu_help:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI(){
        ItemList itemList = ItemList.get(getActivity());
        List<Item> items = itemList.getItems();

        if (mAdapter == null){
            mAdapter = new ItemAdapter(items );
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
            mCallbacks.onItemSelected(mItem);

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

        public ItemAdapter(List<Item> itemList){mItemList = itemList; }
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
    }

    private String getPathFromURI(Uri contentUri){
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);

        }

        cursor.close();
        Toast.makeText(getContext(), "Res is " + res, Toast.LENGTH_SHORT).show();
        return res;
    }
}
