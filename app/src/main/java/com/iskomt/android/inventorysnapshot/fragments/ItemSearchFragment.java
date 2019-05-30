package com.iskomt.android.inventorysnapshot.fragments;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iskomt.android.inventorysnapshot.viewmodels.ItemViewModel;
import com.iskomt.android.inventorysnapshot.entity.Item;
import com.iskomt.android.inventorysnapshot.R;

import java.util.List;

public class ItemSearchFragment extends Fragment {

    private String TAG = "com.iskomt.android.inventorysnapshot.itemlistfragment";
    private RecyclerView mItemRecyclerView;
    private ItemAdapter mAdapter;
    private ItemViewModel mItemViewModel;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onItemSearch(Item item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mItemRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler_view);
        initRecyclerView();
        mItemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                mAdapter.setItems(items);
                //Toast.makeText(getContext(), "OnChanged ", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_search, menu);

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
                List<Item> results = mItemViewModel.Search(s);
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

    public void initRecyclerView(){
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mItemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
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
            mCallbacks.onItemSearch(getItem());
        }
    }

    private class ItemAdapter extends com.iskomt.android.inventorysnapshot.adapters.ItemAdapter{

        public ItemAdapter(Context context, ItemViewModel itemViewModel) {
            super(context, itemViewModel);
        }
        @NonNull
        @Override
        public ItemSearchFragment.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new ItemSearchFragment.ItemHolder(layoutInflater,parent);
        }
    }

}
