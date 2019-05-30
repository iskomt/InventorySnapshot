package com.iskomt.android.inventorysnapshot.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iskomt.android.inventorysnapshot.R;


public class OptionsFragment extends Fragment {
    String[] mOptionsArray;
    TypedArray mIconsArray;
    private RecyclerView mOptionsRecyclerView;
    private OptionAdapter mAdapter;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onOptionSelected(String option);
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
    public void onResume(){
        super.onResume();
        //Toast.makeText(getContext(), "Resumed!", Toast.LENGTH_SHORT).show();
        updateUI();
        hideBackButton();
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void hideBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_options, container, false);
        mOptionsRecyclerView = (RecyclerView) view.findViewById(R.id.options_recycler_view);
        mOptionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mOptionsArray = getResources().getStringArray(R.array.options);
        mIconsArray = getResources().obtainTypedArray(R.array.options_icons);

        updateUI();
        return view;
    }

    public void updateUI(){
        if (mAdapter == null){
            mAdapter = new OptionAdapter(mOptionsArray,mIconsArray);
            mOptionsRecyclerView.setAdapter(mAdapter);
        } else {
            mOptionsRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(getContext(), "Test Delete Successful", Toast.LENGTH_SHORT).show();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }



    private class OptionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mIconView;
        private TextView mNameTextView;

        public OptionHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.adapter_list_options,parent,false));
            itemView.setOnClickListener(this);
            mIconView = (ImageView) itemView.findViewById(R.id.options_icon);
            mNameTextView = (TextView) itemView.findViewById(R.id.options_name);
        }

        @Override
        public void onClick(View view) {
            String option = mNameTextView.getText().toString().trim();
            mCallbacks.onOptionSelected(option);
        }

        public void bind(String name, int icon) {

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), icon);
            mIconView.setImageBitmap(bitmap);
            mNameTextView.setText(name);
        }
    }

    private class OptionAdapter extends RecyclerView.Adapter<OptionHolder> {
        private String[] mOptionsList;
        private TypedArray mIconsList;

        public OptionAdapter(String[] optionsList, TypedArray iconsList){
            mOptionsList = optionsList;
            mIconsList = iconsList;}
        @NonNull
        @Override
        public OptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new OptionHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull final OptionHolder holder, final int position) {
            String name = mOptionsList[position];
            int icon = mIconsList.getResourceId(position,0);
            holder.bind(name,icon);
        }

        @Override
        public int getItemCount() {
            return mOptionsList.length;
        }
    }

}
