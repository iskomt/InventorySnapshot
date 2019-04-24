package com.iskomt.android.inventorysnapshot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iskomt.android.inventorysnapshot.Model.Item;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

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
                Toast.makeText(getContext(), "clicked label: " + position, Toast.LENGTH_SHORT).show();
                rfabHelper.toggleContent();
            }

            @Override
            public void onRFACItemIconClick(int position, RFACLabelItem item) {
                Toast.makeText(getContext(), "clicked icon: " + position, Toast.LENGTH_SHORT).show();
                rfabHelper.toggleContent();

            }
        });

        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Github: wangjiegulu")
                .setResId(R.drawable.baseline_home_black_24dp)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(0)
        );
        rfaContent
                .setItems(items)
                .setIconShadowColor(0xff888888)
        ;
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
        inflater.inflate(R.menu.fragment_item_list,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.add_item:
                Item i = new Item();
                ItemList.get(getActivity()).addItem(i);
                updateUI();
                mCallbacks.onItemSelected(i);
                Log.d(TAG, "Item added successfully");
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

        public ItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_tem,parent,false));

            itemView.setOnClickListener(this);
            mPhotoView = (ImageView) itemView.findViewById(R.id.item_photoholder);
            mNameTextView = (TextView) itemView.findViewById(R.id.item_name);
            mQtyTextView = (TextView) itemView.findViewById(R.id.item_qty);
        }

        @Override
        public void onClick(View view) {
            //mCallbacks.onItemSelected(mItem);
            Intent intent = ItemPagerActivity.newIntent(getActivity(), mItem.getId());
            startActivity(intent);

        }

        public void bind(Item item) {
            mItem = item;
            mNameTextView.setText(mItem.getName());
            mQtyTextView.setText(Double.toString(mItem.getQty()));
            //Bitmap bitmap = PictureUtils.getScaledBitmap(mItem.getPhotoPath(),getActivity());
            ItemList itemList = ItemList.get(getContext());
            Bitmap bitmap = PictureUtils.getScaledBitmap(itemList.getPhotoFile(mItem).getAbsolutePath(),getActivity());

                Log.d(TAG, itemList.getPhotoFile(mItem).getAbsolutePath().toString());

            //mPhotoView.setImageDrawable(getResources().getDrawable(android.R.drawable.arrow_up_float));
            mPhotoView.setImageBitmap(bitmap);
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
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            Item item = mItemList.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }

        public void setItems(List<Item> items){mItemList = items;}
    }
}
