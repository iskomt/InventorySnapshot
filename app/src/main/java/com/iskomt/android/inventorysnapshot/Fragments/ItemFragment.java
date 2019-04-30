package com.iskomt.android.inventorysnapshot.Fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.iskomt.android.inventorysnapshot.ItemList;
import com.iskomt.android.inventorysnapshot.ItemListActivity;
import com.iskomt.android.inventorysnapshot.Model.Item;
import com.iskomt.android.inventorysnapshot.PictureUtils;
import com.iskomt.android.inventorysnapshot.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class ItemFragment extends Fragment {
    private final String TAG = "itemfragment";
    private static final String ARG_ITEM_ID = "item_id" , IMAGE_DIRECTORY = "IMAGE_DIRECTORY";
    private static final int REQUEST_PHOTO_FILE = 0, REQUEST_CONTACT = 1, REQUEST_PHOTO = 2;

    private Item mItem;
    private EditText mIdField,mNameField, mQtyField, mPriceField;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private ImageButton mPhotoButton, mGalleryButton;
    private Callbacks mCallbacks;
    private View.OnTouchListener mOnTouchListener;
    private boolean mItemChanged = false;
    private TextWatcher mTextWatcher;

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
        setHasOptionsMenu(true);
        /*
        Initialize the item from the arguments obtained
         */
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_item,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.save_item:
                saveItem();
                return true;
            case R.id.delete_item:
                deleteItem();
                mCallbacks.onItemUpdated(mItem);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_item, container, false);

        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mItemChanged = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        mIdField = (EditText) v.findViewById(R.id.item_id_text);
        mIdField.setText(mItem.getOriginalUUID().toString());
        mIdField.setEnabled(false);
        mIdField.addTextChangedListener(mTextWatcher);

        mNameField = (EditText) v.findViewById(R.id.item_name_text);
        mNameField.setText(mItem.getName());
        mNameField.addTextChangedListener(mTextWatcher);

        mQtyField = (EditText) v.findViewById(R.id.item_qty_text);
        mQtyField.setText(Double.toString(mItem.getQty()));
        mQtyField.addTextChangedListener(mTextWatcher);

        mPriceField = (EditText) v.findViewById(R.id.item_price_text);
        mPriceField.setText(Double.toString(mItem.getPrice()));
        mPriceField.addTextChangedListener(mTextWatcher);

        mPhotoButton = (ImageButton) v.findViewById(R.id.item_camera);
        mPhotoView = (ImageView) v.findViewById(R.id.item_photoview);

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

                //takePhotoFromCamera();
            }
        });

        mGalleryButton = (ImageButton)  v.findViewById(R.id.item_choose_photo);
        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Photo"),REQUEST_PHOTO_FILE);*/

                choosePhotoFromGallery();
            }
        });

        updatePhotoView();


        mOnTouchListener = new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mItemChanged = true;
                return false;
            }
        };

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_PHOTO_FILE) {
            if (data != null) {
                mItem.setSource(0);
                Uri contentURI = data.getData();
                mItem.setPhotoPath(contentURI.toString());
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    mPhotoView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == REQUEST_PHOTO) {
            /*mItem.setSource(1);
            Uri contentURI = data.getData();
            mItem.setPhotoPath(contentURI.toString());
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            mPhotoView.setImageBitmap(thumbnail);
            saveImage(thumbnail);*/
            mItem.setSource(1);
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.iskomt.android.inventorysnapshot.fileprovider", mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            mItem.setPhotoPath(mPhotoFile.getPath());
            Log.d(TAG, "REQUEST_PHOTO: ItemPhotoPath is: " + mItem.getPhotoPath());
            updatePhotoView();
            Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }



    }

    private void updateItem() {
        ItemList.get(getContext()).updateItem(mItem);
        mCallbacks.onItemUpdated(mItem);
    }

    private void updatePhotoView() {
        if(mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
            mPhotoView.setContentDescription("Not set");
        } else {
             /*Uri selectedUri = Uri.parse(mItem.getPhotoPath());
                mPhotoView.setImageURI(selectedUri);
                mPhotoView.setContentDescription("Scene photo");*/
            if(mItem.getSource()==1) {
                Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
                mPhotoView.setImageBitmap(bitmap);
            }
            else if(mItem.getSource()==0){
                if(mItem.getImage()!=null)
                {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(mItem.getImage(),0,mItem.getImage().length);
                    mPhotoView.setImageBitmap(bitmap);
                }
            }
        }
    }

    private void deleteItem(){
        Toast.makeText(getContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();
        ItemList.get(getActivity()).deleteItem(mItem);
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


    private void saveItem(){
        //Read inputs
        //Remove trailing white spaces
        String id = mIdField.getText().toString().trim();
        String name = mNameField.getText().toString().trim();
        Double quantity = Double.parseDouble(mQtyField.getText().toString());
        Double price = Double.parseDouble(mPriceField.getText().toString());

        if(quantity<0) {
            mQtyField.setError("Quantity cannot be less than 0");
        } else if(price<0) {
            mPriceField.setError("Price cannot be less than 0");
        } else {
            mItem.setUUID(UUID.fromString(id));
            mItem.setName(name);
            mItem.setQty(quantity);
            mItem.setPrice(price);


            Toast.makeText(getContext(), "Successfully saved", Toast.LENGTH_SHORT).show();
            //ItemList.get(getActivity()).updateItem(mItem);
            addPhoto();
            updateItem();
        }
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_PHOTO_FILE);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_PHOTO);
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    public void addPhoto(){
        mPhotoView.setDrawingCacheEnabled(true);
        mPhotoView.buildDrawingCache();
        Bitmap bitmap = mPhotoView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        mItem.setImage(data);

    }


}
