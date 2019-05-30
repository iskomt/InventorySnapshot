package com.iskomt.android.inventorysnapshot.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.iskomt.android.inventorysnapshot.entity.Item;
import com.iskomt.android.inventorysnapshot.viewmodels.ItemViewModel;
import com.iskomt.android.inventorysnapshot.R;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class IncDecFragment extends DialogFragment {
    private static final String ARG_ITEM_ID = "item_id";
    private Item mItem;
    private ImageButton mQtyDec,mQtyInc;
    private Button mCancel,mSave;
    private EditText mQty;
    private int Qty;
    private ItemViewModel mItemViewModel;
    public IncDecFragment(){}

    public static IncDecFragment newInstance(UUID itemId){
        IncDecFragment mIncDec = new IncDecFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_ID, itemId);
        mIncDec.setArguments(args);
        return mIncDec;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_fragment_item_inc_dec, container, false);
        showBackButton();
       mItemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
       UUID itemId = (UUID) getArguments().getSerializable(ARG_ITEM_ID);
       try {
           mItem = mItemViewModel.getItemFromId(itemId.toString());
           //Toast.makeText(getContext(), itemId.toString(), Toast.LENGTH_SHORT).show();
       } catch (ExecutionException e) {
           e.printStackTrace();
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
       mQtyDec = (ImageButton) view.findViewById(R.id.item_qty_dec);
       mQtyInc = (ImageButton) view.findViewById(R.id.item_qty_inc);
       mQty = (EditText) view.findViewById(R.id.item_qty_edit);
       mCancel = (Button) view.findViewById(R.id.item_inc_dec_cancel);
       mSave = (Button) view.findViewById(R.id.item_inc_dec_save);
       Qty = mItem.getQty();
       mQty.setText(Integer.toString(Qty));
       mQtyDec.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               decQty();
           }
       });
       mQtyInc.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               incQty();
           }
       });
       mCancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               dismiss();
           }
       });
       mSave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mItem.setQty(Qty);
               mItemViewModel.update(mItem);
               Toast.makeText(getContext(), "Successfully saved", Toast.LENGTH_SHORT).show();
               dismiss();
           }
       });


       return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void incQty(){
        Qty=Qty+1;
        mQty.setText(Integer.toString(Qty));
    }
    public void decQty(){
        if(Qty>0){
        Qty=Qty-1;
        mQty.setText(Integer.toString(Qty));}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

}
