package com.iskomt.android.inventorysnapshot.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iskomt.android.inventorysnapshot.ViewModels.ItemViewModel;
import com.iskomt.android.inventorysnapshot.Entity.Item;
import com.iskomt.android.inventorysnapshot.R;
import com.karumi.dexter.listener.single.PermissionListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
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
    private ItemViewModel mItemViewModel;
    private ImageLoader mImageLoader;
    private PermissionListener dialogPermissionListener;

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
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        //checkPermissions(Manifest.permission.MANAGE_DOCUMENTS,"Storage","You need storage permission to use this feature");
        mItemRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        mItemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        mItemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                mAdapter.setItems(items);
                //Toast.makeText(getContext(), "OnChanged ", Toast.LENGTH_SHORT).show();
            }
        });


        //mImageLoader = ImageLoader.getInstance();
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
                        final Item i = new Item();
                        final EditText edit = new EditText(getActivity());
                        edit.setText("Item " + mItemViewModel.getSize());
                        edit.setSelectAllOnFocus(true);
                        edit.selectAll();
                        final AlertDialog input = new AlertDialog.Builder(getContext())
                                .setTitle("New Item")
                                .setMessage("Enter item name")
                                .setView(edit)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, null)
                                .setNegativeButton(android.R.string.no, null).create();
                        input.show();
                        input.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean closeDialog = false;
                                try{
                                    String name = edit.getText().toString().trim();
                                    if(name.length()>0){
                                        i.setName(name);
                                        //ItemList.get(getActivity()).insertItem(i);
                                        mItemViewModel.insert(i);
                                        input.dismiss();
                                        closeDialog = true;
                                        //updateUI();
                                        mCallbacks.onItemSelected(i);

                                    } else {
                                        edit.setError("Name cannot be null");
                                        Toast.makeText(getContext(), "Name cannot be null ", Toast.LENGTH_SHORT).show();

                                    }
                                }catch(Exception e){

                                }
                                if(closeDialog){
                                    input.dismiss();
                                }
                            }
                        });

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
                        //ItemList.get(getActivity()).insertItem(i);
                        mItemViewModel.insert(i);
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

        if (mAdapter == null){
            mAdapter = new ItemAdapter();
            mItemRecyclerView.setAdapter(mAdapter);
        } else {

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
            if(mItem.getPhotoPath()!=null){
                Picasso.get().load(new File(mItem.getPhotoPath())).fit().centerCrop().into(mPhotoView);}

        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<Item> mItemList = new ArrayList<>();

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
                                                    //ItemList.get(getContext()).deleteItem(mItemList.get(position));
                                                    mItemViewModel.delete(mItemList.get(position));
                                                    //updateUI();
                                                }})
                                            .setNegativeButton(android.R.string.no, null).show();
                                    break;
                                case R.id.item_list_quick_qty:
                                    /*IncDecFragment mIncDecFragment = IncDecFragment.newInstance(mItemList.get(position).getQty());
                                    mIncDecFragment.show(getActivity().getSupportFragmentManager(), "fragment_edit_name");*/
                                    IncDecFragment mIncDecFragment = IncDecFragment.newInstance(mItemList.get(position).getOriginalUUID());
                                    mIncDecFragment.show(getActivity().getSupportFragmentManager(), "fragment_edit_name");
                                   /* mQty.setText(mItemList.get(position).getQty());
                                    mQty.setSelectAllOnFocus(true);
                                    mQty.selectAll();
                                    final AlertDialog input = new AlertDialog.Builder(getContext())
                                            .setTitle("Set Quantity")
                                            .setView(getLayoutInflater().inflate(R.layout.item_inc_dec,null))
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setPositiveButton(android.R.string.yes, null)
                                            .setNegativeButton(android.R.string.no, null).create();
                                    input.show();*/
                                    /*input.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            boolean closeDialog = false;
                                            try{
                                                String name = edit.getText().toString().trim();
                                                if(name.length()>0){
                                                    i.setName(name);
                                                    //ItemList.get(getActivity()).insertItem(i);
                                                    mItemViewModel.insert(i);
                                                    input.dismiss();
                                                    closeDialog = true;
                                                    //updateUI();
                                                    mCallbacks.onItemSelected(i);

                                                } else {
                                                    edit.setError("Name cannot be null");
                                                    Toast.makeText(getContext(), "Name cannot be null ", Toast.LENGTH_SHORT).show();

                                                }
                                            }catch(Exception e){

                                            }
                                            if(closeDialog){
                                                input.dismiss();
                                            }
                                        }
                                    });*/

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
    }
}
