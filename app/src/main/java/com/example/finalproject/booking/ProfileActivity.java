package com.example.finalproject.booking;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.finalproject.databinding.ActivityProfileBinding;
import com.example.finalproject.model.MyUtils;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends FireBaseActivity {
    private ActivityProfileBinding binding;
    private static final String TAG ="PROFILE_EDIT_TAG";

    private ProgressDialog progressDialog;

    private ImageView btnBack;
    private String userType="";
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        addViews();
        addEvents();
        loadMyInfo();

        binding.imgvChangeProImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgChange();
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();

            }
        });
    }
    private String name = "";
    private String dob = "";
    private String email = "";
    private String phoneCode = "";
    private String phoneNumber = "";
    private String location = "";

    private void validateData(){
        name = binding.edtName.getText().toString().trim();
        dob = binding.edtDOB.getText().toString().trim();
        location = binding.edtLocation.getText().toString().trim();
        email = binding.edtEmail.getText().toString().trim();
        phoneCode = binding.countryCode.getSelectedCountryCodeWithPlus();
        phoneNumber = binding.edtPhone.getText().toString().trim();

        if(imageUri == null){
            updateProfileDb(null);

        } else {
            updateProfileImageStorage();

        }

    }
    private void updateProfileImageStorage(){
        Log.d(TAG, "updateProfileImageStorage: ");

        progressDialog.setMessage("Uploading user profile image...");
        progressDialog.show();

        String filePathAndName = "UserImages/"+ firebaseAuth.getUid();

        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
        ref.putFile(imageUri)
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        Log.d(TAG, "onSuccess: Progress: "+progress);

                        progressDialog.setMessage("Uploading profile image. Progress :"+ (int)progress + "%");

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG,"onSuccess: Uploaded");

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!uriTask.isSuccessful());
                        String uploadedImageUrl = uriTask.getResult().toString();

                        if (uriTask.isSuccessful()){
                            updateProfileDb(uploadedImageUrl);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Failed to upload image
                        Log.e(TAG, "onFaiture: ", e);
                        progressDialog.dismiss();
                        MyUtils.toast(  ProfileActivity.this, "Failed to upload profile image due to "+e.getMessage());

                    }
                });

    }
    private void updateProfileDb(String imageUrl){
        progressDialog.setMessage("Updating user info ... ");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put ("full name", "" + name) ;
        hashMap.put("dob", ""+ dob);
        hashMap.put("location", ""+ location);

        if (imageUrl != null) {
            hashMap.put("profileImageUrl", ""+ imageUrl);
        }
        if (userType.equals(MyUtils.USER_TYPE_EMAIL) || userType.equals(MyUtils.USER_TYPE_GOOGLE)){

            hashMap.put("phone code", "" + phoneCode);
            hashMap.put("phone number", ""+ phoneNumber);
        } else if (userType.equals(MyUtils.USER_TYPE_PHONE)) {
            hashMap.put("email","" + email);

        }

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        ref.child(""+firebaseAuth.getUid())
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Info updated");
                        progressDialog.dismiss();
                        MyUtils.toast(  ProfileActivity.this,"Profile updated ... ");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed to update
                        Log.e(TAG, "onFailure; ", e);
                        progressDialog.dismiss();
                        MyUtils.toast( ProfileActivity.this,"Failed to update due to "+e.getMessage());

                    }
                });


    }

    private void imgChange() {
        PopupMenu popupMenu= new PopupMenu(this,binding.imgvChangeProImg);
        popupMenu.getMenu().add(Menu.NONE,1,1,"Camera");
        popupMenu.getMenu().add(Menu.NONE,2,2,"Gallery");

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int itemId = item.getItemId();
                if(itemId ==1){
                    //
                    Log.d(TAG, "onMenuItemClick: Camera Clicked, checking permissions");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){

                        requestCameraPermissions.launch(new String[]{Manifest.permission.CAMERA});
                    } else {
                        requestCameraPermissions.launch(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
                    }
                } else if ( itemId == 2){
                    Log.d(TAG, "onMenuItemClick: Gallery Clicked...");
                    pickImageGallery();
                }
                return true;
            }
        });

    }

    private final ActivityResultLauncher<String[]> requestCameraPermissions = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    Log.d(TAG, "onActivityResult: " + result.toString());

                    boolean areAllGranted = true;
                    for (Boolean isGranted : result.values()) {

                        areAllGranted = areAllGranted && isGranted;
                    }

                    if (areAllGranted) {
                        pickImageCamera();
                    } else {

                        Log.d(TAG, "onActivityResult:  All or either one permission denied...");
                        MyUtils.toast(ProfileActivity.this,"Camera or Storage or both permissions denied...");
                    }
                }
            }
    );

    private void pickImageCamera() {
        Log.d(TAG, "pickImageCamera: ");

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "temp_image");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "temp_image_description");
        //store the camera image in imageUri variable
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        //Intent to launch camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //Check if image is captured or not
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //Image Captured, we have image in imageUri as assigned in pickImageCamera()
                        Log.d(TAG, "onActivityResult: Image Picked: " + imageUri);

                        //set to profileIv
                        try {
                            Glide.with(ProfileActivity.this)
                                    .load(imageUri)
                                    .placeholder(R.drawable.ic_avatar)
                                    .into(binding.imgProfile);
                        } catch (Exception e) {
                            Log.e(TAG, "onActivityResult: ", e);
                        }

                    } else {
                        //CanceLled
                        Log.d(TAG, "onActivityResult: Cancelled ... ");
                        MyUtils.toast(ProfileActivity.this, "Cancelled ... ");
                    }
                }
            }
    );

    private void pickImageGallery() {
        Log.d(TAG, "pickImageGallery: ");
        //Intent to launch Image Picker e.g. Gallery
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK){

                        Intent data = result.getData();

                        imageUri = data.getData();

                        Log.d(TAG,"onActivityResult: Image Picked From Gallery: "+imageUri);

                        try {
                            //set to profileIv
                            try {
                                Glide.with( ProfileActivity.this)
                                        .load(imageUri)
                                        .placeholder(R.drawable.ic_avatar)
                                        .into(binding.imgProfile);
                            } catch (Exception e) {
                                Log.e(TAG, "onActivityResult: ", e);
                            }

                        } catch (Exception e) {
                            Log.e(TAG, "onActivityResult: ", e);
                        }

                    } else {

                        Log.d(TAG, "onActivityResult: Cancelled ... ");
                        MyUtils.toast( ProfileActivity.this, "Cancelled ...! ");
                    }
                }
            }
    );


    private void loadMyInfo() {
        Log.d(TAG,"loadMyInfo:");
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
        myRef.child(""+firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String dob = "" + snapshot.child("dob").getValue();
                            String email = "" + snapshot.child("email").getValue();
                            String name = "" + snapshot.child("full name").getValue();
                            String phoneCode = "" + snapshot.child("phone code").getValue();
                            String phoneNumber = "" + snapshot.child("phone number").getValue();
                            String profileImageUrl = "" + snapshot.child("profileImageUrl").getValue();
                            String timestamp = "" + snapshot.child("timestamp").getValue();
                            userType = "" + snapshot.child("userType").getValue();
                            String location = "" + snapshot.child("location").getValue();

                            //String phone = phoneCode + " " + phoneNumber;

                            if (timestamp.equals("null")) {
                                timestamp = "0";
                            }

                            String formattedDate = MyUtils.formattedTimestampData(Long.parseLong(timestamp));




                            if (userType.equals(MyUtils.USER_TYPE_EMAIL) | userType.equals(MyUtils.USER_TYPE_GOOGLE)) {

                                binding.tilEmail.setEnabled(false);
                                binding.edtEmail.setEnabled(false);
                            } else {
                                binding.tilPhone.setEnabled(false);
                                binding.edtPhone.setEnabled(false);
                                binding.countryCode.setEnabled(false);

                            }

                            binding.edtEmail.setText(email);
                            binding.edtName.setText(name);
                            binding.txtName.setText(name);
                            binding.edtDOB.setText(dob);
                            binding.edtPhone.setText(phoneNumber);
                            binding.txtMemberSince.setText("Member since " +formattedDate);
                            binding.edtLocation.setText(location);



                            try {
                                int phoneCodeInt = Integer.parseInt(phoneCode.replace("+",""));
                                binding.countryCode.setCountryForPhoneCode(phoneCodeInt);
                            } catch (Exception e){
                                Log.e(TAG,"onDataChange: ",e);
                            }

                            try {
                                Glide.with(ProfileActivity.this)
                                        .load(profileImageUrl)
                                        .placeholder(R.drawable.ic_avatar)
                                        .into(binding.imgProfile);

                            } catch (Exception e){
                                Log.e(TAG,"onDataChange: ",e);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Handle database error
                    }
                });
    }

    private void addViews() {
        btnBack = findViewById(R.id.btnBack);
    }

    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileMainActivity.class);
                startActivity(intent);
            }
        });
    }
}
