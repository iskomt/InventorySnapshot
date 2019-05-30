package com.iskomt.android.inventorysnapshot.fragments;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iskomt.android.inventorysnapshot.viewmodels.ItemViewModel;
import com.iskomt.android.inventorysnapshot.entity.Item;
import com.iskomt.android.inventorysnapshot.R;
import com.karumi.dexter.listener.single.PermissionListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

import static com.iskomt.android.inventorysnapshot.viewmodels.ItemViewModel.*;

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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        //checkPermissions(Manifest.permission.MANAGE_DOCUMENTS,"Storage","You need storage permission to use this feature");

        mItemRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler_view);
        initRecyclerView();
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
        inflater.inflate(R.menu.menu_activity_item_list,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.main_menu_sort_name_asc:
                mItemViewModel.sortList(SORT_NAME_ASC);
                return true;
            case R.id.main_menu_sort_name_desc:
                mItemViewModel.sortList(SORT_NAME_DESC);
                return true;
            case R.id.main_menu_sort_qty_asc:
                mItemViewModel.sortList(SORT_QTY_ASC);
                return true;
            case R.id.main_menu_sort_qty_desc:
                mItemViewModel.sortList(SORT_QTY_DESC);
                return true;
            case R.id.main_menu_sort_price_asc:
                mItemViewModel.sortList(SORT_PRICE_ASC);
                return true;
            case R.id.main_menu_sort_price_desc:
                mItemViewModel.sortList(SORT_PRICE_DESC);
                return true;
            case R.id.main_menu_sort_added:
                mItemViewModel.sortList(SORT_ADDED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initRecyclerView(){
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mItemViewModel = ViewModelProviders.of(getActivity()).get(ItemViewModel.class);
        if (mAdapter == null){
            mAdapter = new ItemAdapter(getContext(),mItemViewModel);
            mItemRecyclerView.setAdapter(mAdapter);
        } else {

        }
    }

    private class ItemHolder extends com.iskomt.android.inventorysnapshot.adapters.ItemHolder implements View.OnClickListener{
        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater, parent);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            mCallbacks.onItemSelected(getItem());
        }
    }
    private class ItemAdapter extends com.iskomt.android.inventorysnapshot.adapters.ItemAdapter{

        public ItemAdapter(Context context, ItemViewModel itemViewModel) {
            super(context, itemViewModel);
        }
        @NonNull
        @Override
        public ItemListFragment.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new ItemListFragment.ItemHolder(layoutInflater,parent);
        }
    }



}
