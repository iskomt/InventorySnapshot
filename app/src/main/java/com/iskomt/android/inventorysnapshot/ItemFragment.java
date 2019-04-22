package com.iskomt.android.inventorysnapshot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class ItemFragment extends Fragment {
    private static final String ARG_ITEM_ID = "item_id";
    private static final int REQUEST_DATE = 0, REQUEST_CONTACT = 1, REQUEST_PHOTO = 2;

    private Item mItem;
    private EditText mIdField,mNameField, mQtyField, mPriceField;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private ImageButton mPhotoButton;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onItemUpdated(Item item);
    }

    public static ItemFragment newInstance(UUID itemId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_ID, itemId);

        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID itemId = (UUID) getArguments().getSerializable(ARG_ITEM_ID);
        mItem = ItemList.get(getActivity()).getItem(itemId);
        mPhotoFile = ItemList.get(getActivity()).getPhotoFile(mItem);
    }

    @Override
    public void onPause(){
        super.onPause();

        ItemList.get(getActivity()).updateItem(mItem);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_item, container, false);

        mIdField = (EditText) v.findViewById(R.id.item_id_text);
        mIdField.setText(mItem.getId().toString());
        mIdField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mItem.setId(UUID.fromString(charSequence.toString()));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mNameField = (EditText) v.findViewById(R.id.item_name_text);
        mNameField.setText(mItem.getName());
        mNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mItem.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mQtyField = (EditText) v.findViewById(R.id.item_qty_text);
        mQtyField.setText(Double.toString(mItem.getQty()));
        mQtyField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mItem.setQty(Double.parseDouble(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPriceField = (EditText) v.findViewById(R.id.item_price_text);
        mPriceField.setText(Double.toString(mItem.getPrice()));
        mPriceField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mItem.setPrice(Double.parseDouble(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPhotoButton = (ImageButton) v.findViewById(R.id.item_camera);

        PackageManager packageManager = getActivity().getPackageManager();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "com.iskomt.android.inventorysnapshot.fileprovider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage,PackageManager.MATCH_DEFAULT_ONLY);

                for(ResolveInfo activity:cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.item_photoview);
        updatePhotoView();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode!= Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.iskomt.android.inventorysnapshot.fileprovider", mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updateCrime();
            updatePhotoView();
        }


    }

    private void updateCrime() {
        ItemList.get(getActivity()).updateItem(mItem);
        mCallbacks.onItemUpdated(mItem);
    }

    private void updatePhotoView() {
        if(mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
            mPhotoView.setContentDescription("Not set");
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
            mPhotoView.setContentDescription("Scene photo");
        }
    }


}
