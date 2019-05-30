package com.iskomt.android.inventorysnapshot.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.iskomt.android.inventorysnapshot.viewmodels.ItemViewModel;
import com.iskomt.android.inventorysnapshot.entity.Item;
import com.iskomt.android.inventorysnapshot.R;
import com.iskomt.android.inventorysnapshot.utils.UriParser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ItemFragment extends Fragment {
    private final String TAG = "itemfragment";
    private static final String ARG_ITEM_ID = "item_id" , IMAGE_DIRECTORY = "IMAGE_DIRECTORY";
    private static final int REQUEST_GALLERY = 0, REQUEST_CONTACT = 1, REQUEST_CAMERA = 2;

    private Item mItem;
    private EditText mIdField,mNameField, mQtyField, mPriceField;
    private ImageView mPhotoView,mZoomView;
    private File mPhotoFile;
    private ImageButton mPhotoButton, mGalleryButton;
    private Callbacks mCallbacks;
    private View.OnTouchListener mOnTouchListener;
    private boolean mItemChanged = false;
    private TextWatcher mTextWatcher;
    private PermissionListener dialogPermissionListener;
    private ItemViewModel mItemViewModel;
    private int mPhotoWidth,mPhotoHeight;

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
        mItemViewModel = ViewModelProviders.of(getActivity()).get(ItemViewModel.class);
        try {
            mItem = mItemViewModel.getItemFromId(itemId.toString());
            //Toast.makeText(getContext(), itemId.toString(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mPhotoFile = mItemViewModel.getPhotoFile(getContext(),mItem);


    }

    @Override
    public void onPause(){
        super.onPause();
        //ItemList.get(getActivity()).updateItem(mItem);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_fragment_item,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.save_item:
                saveItem();
                return true;
            case R.id.delete_item:
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Confirmation")
                        .setMessage("Are you sure you want to delete this item?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteItem();
                                mCallbacks.onItemUpdated(mItem);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
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
        mQtyField.setText(Integer.toString(mItem.getQty()));
        mQtyField.addTextChangedListener(mTextWatcher);

        mPriceField = (EditText) v.findViewById(R.id.item_price_text);
        mPriceField.setText(Double.toString(mItem.getPrice()));
        mPriceField.addTextChangedListener(mTextWatcher);

        mPhotoButton = (ImageButton) v.findViewById(R.id.item_camera);
        mPhotoView = (ImageView) v.findViewById(R.id.item_photoview);

        mZoomView = (ImageView) new ImageView(getContext());
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZoomFragment mZoomFragment = ZoomFragment.newInstance(mItem.getPhotoPath());
                mZoomFragment.show(getActivity().getSupportFragmentManager(), "fragment_edit_name");
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;

        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoFromCamera(captureImage);
            }
        });

        mGalleryButton = (ImageButton)  v.findViewById(R.id.item_choose_photo);
        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhotoFromGallery();
            }
        });

        mOnTouchListener = new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mItemChanged = true;
                return false;
            }
        };

        updatePhotoView();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CAMERA) {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.iskomt.android.inventorysnapshot.fileprovider", mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            mItem.setPhotoPath(mPhotoFile.getAbsolutePath());
            updatePhotoView();
        }
        else if (requestCode == REQUEST_GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                String path = UriParser.getPathFromUri(getContext(),contentURI);
                mItem.setPhotoPath(path);
                updatePhotoView();
            }
        }

    }

    private void updatePhotoView() {
        if(mItem.getPhotoPath() == null) {
            mPhotoView.setImageDrawable(null);
            mPhotoView.setContentDescription("Not set");}
        else {
            File f = new File(mItem.getPhotoPath());
            Picasso.get().load(f).noFade().fit().centerCrop().into(mPhotoView);
        }
    }

    private void takePhotoFromCamera(Intent captureImage) {
        checkPermissions(Manifest.permission.CAMERA,"Camera","You need camera permissions to use this feature");
        if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED) {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.iskomt.android.inventorysnapshot.fileprovider", mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo activity : cameraActivities) {
                getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            startActivityForResult(captureImage, REQUEST_CAMERA);
        }
    }

    public void choosePhotoFromGallery() {
        checkPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,"Storage","You need storage permission to use this feature");
        if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_GALLERY);
        }
    }

    public void checkPermissions(String permission, String permTitle, String permDeniedText){
        Dexter.withActivity(getActivity())
                .withPermission(permission)
                .withListener(dialogPermissionListener = DialogOnDeniedPermissionListener.Builder
                        .withContext(getContext())
                        .withTitle(permTitle)
                        .withMessage(permDeniedText)
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.drawable.baseline_home_black_24dp)
                        .build())
                .check();
    }

    private void saveItem(){
        //Read inputs
        //Remove trailing white spaces
        try {

            String id = mIdField.getText().toString().trim();
            String name = mNameField.getText().toString().trim();
            int quantity = Integer.parseInt(mQtyField.getText().toString());
            Double price = Double.parseDouble(mPriceField.getText().toString());

            if (quantity < 0) {
                mQtyField.setError("Invalid Quantity");
            } else if (price < 0) {
                mPriceField.setError("Invalid Price");
            } else if (name.length()<=0){
                mNameField.setError("Name cannot be empty");
            }
            else {
                mItem.setUUID(UUID.fromString(id));
                mItem.setName(name);
                mItem.setQty(quantity);
                mItem.setPrice(price);
                mItemViewModel.update(mItem);
                mCallbacks.onItemUpdated(mItem);
                Toast.makeText(getContext(), "Successfully saved", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Toast.makeText(getContext(), "There was an error saving this item", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteItem(){
        Toast.makeText(getContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();
        mItemViewModel.delete(mItem);
    }
}
