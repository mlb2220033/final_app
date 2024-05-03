package com.example.finalproject.booking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityLoginBinding;
import com.example.finalproject.model.MyUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    ImageView imgShowHidePwdLogin;
    private ActivityLoginBinding binding;
    private static final String TAG_E = "LOGIN_EMAIL_TAG";
    private static final String TAG_G = "LOGIN_GOOGLE_TAG";
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String PREF_EMAIL = "pref_email";
    private static final String PREF_PASSWORD = "pref_password";
    private static final String PREF_CHECKBOX_STATE = "pref_checkbox_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        edtPassword = findViewById(R.id.edtPassword);
        imgShowHidePwdLogin = findViewById(R.id.imgShowHidePwdLogin);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

        // check internet
        if(!checkInternet()){
            findViewById(R.id.overlayView).setVisibility(View.VISIBLE);
            DialogNoInternet dialogNoInternet = new DialogNoInternet(LoginActivity.this);
            dialogNoInternet.setCancelable(false);
            dialogNoInternet.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            dialogNoInternet.show();
        }

        // Save login email info
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString(PREF_EMAIL, "");
        String savedPassword = sharedPreferences.getString(PREF_PASSWORD, "");
        binding.edtEmail.setText(savedEmail);
        binding.edtPassword.setText(savedPassword);

        // Retrieve and set checkbox state
        boolean isCheckboxChecked = sharedPreferences.getBoolean(PREF_CHECKBOX_STATE, false);
        binding.chkSaveLoginInfo.setChecked(isCheckboxChecked);
        binding.txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
        // Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("697452755216-5s86ps8hoo3o7bm9aui6r3s8329ebkpq.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // handle login btnGG click
        binding.imgbtnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginGoogleLogin();
            }
        });

        //handle login btnPhoneNumber
        binding.imgbtnLoginPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LoginPhoneActivity.class ));
            }
        });

        // Email
        imgShowHidePwdLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPassword != null && edtPassword.getTransformationMethod() != null) {
                    // Log values for debugging
                    Log.d("LoginActivity", "edtPassword: " + edtPassword);
                    Log.d("LoginActivity", "Transformation method: " + edtPassword.getTransformationMethod());

                    if (edtPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                        // If pwd is visible then Hide
                        edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        imgShowHidePwdLogin.setImageResource(R.drawable.eye_hide);
                    } else {
                        edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        imgShowHidePwdLogin.setImageResource(R.drawable.eye_show);
                    }
                } else {
                    // Handle null reference gracefully
                    Log.e("LoginActivity", "edtPassword or its transformation method is null");
                }
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {validateData();}
        });

        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterEmailActivity.class));
            }
        });
    }

    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    //Google
    private void beginGoogleLogin () {
        Log.d(TAG_G, "beginGoogleLogin");
        Intent googleSignInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInnARL.launch(googleSignInIntent);
    }
    private ActivityResultLauncher<Intent> googleSignInnARL = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    Log.d(TAG_G, "onActivityResult");
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            Log.d(TAG_G, "onActivityResult: AccountID:" + account.getId());
                            firebaseAuthWithGoogleAccount(account.getIdToken());
                        } catch (Exception e) {
                            Log.e(TAG_G, "onActivityResult", e);
                        }
                    } else {
                        Log.d(TAG_G, "onActivityResult: Cancelled..!");
                        MyUtils.toast(LoginActivity.this, "Cancelled..!");

                    }
                }
            }
    );

    private void firebaseAuthWithGoogleAccount (String idToken){
        Log.d(TAG_G, "firebaseAuthWithGoogleAccount: idToken: " + idToken);
        // AuthCredential to setup credential to signin
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult.getAdditionalUserInfo().isNewUser()) {
                            Log.d(TAG_G, "onSuccess: Account Created...");
                            // New User, acc created and save info in realtimedb
                            updateUserInfoDb();
                        } else {
                            Log.d(TAG_G, "onSuccess: Logged In...");
                            // New User, acc created
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finishAffinity();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG_G, "onFailure: ", e);
                    }
                });
    }

    private void updateUserInfoDb () {
        Log.d(TAG_G, "updateUserInfoDb: ");

        progressDialog.setMessage("Saving user Info..");
        progressDialog.show();

        long timestamp = MyUtils.timestamp();
        String registeredUserEmail = firebaseAuth.getCurrentUser().getEmail();
        String registeredUserUid = firebaseAuth.getUid();
        String name = firebaseAuth.getCurrentUser().getDisplayName();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", registeredUserEmail);
        hashMap.put("full name", name);
        hashMap.put("username", "");
        hashMap.put("phone code", "");
        hashMap.put("phone number", "");
        hashMap.put("profileImageUrl", "");
        hashMap.put("timestamp", timestamp);
        hashMap.put("token", "");
        hashMap.put("userType", MyUtils.USER_TYPE_GOOGLE);
        hashMap.put("uid", registeredUserUid);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(registeredUserUid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG_G, "onSuccess: Info Saved...");
                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finishAffinity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG_G, "onFailure", e);
                        MyUtils.toast(LoginActivity.this, "Failed to save due to" + e.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }


    // Email
    private String txtEmail, txtPwd;
    private void validateData() {
        txtEmail = binding.edtEmail.getText().toString().trim();
        txtPwd = binding.edtPassword.getText().toString();
        if (TextUtils.isEmpty(txtEmail)) {
            Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
            binding.edtEmail.setError("Email is required");
            binding.edtEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
            Toast.makeText(this, "Please Re-Enter Your Email", Toast.LENGTH_SHORT).show();
            binding.edtEmail.setError("Valid email is required");
            binding.edtEmail.requestFocus();
        } else if (TextUtils.isEmpty(txtPwd)) {
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
            binding.edtPassword.setError("Password is required");
            binding.edtPassword.requestFocus();
        } else {
            loginUser();
        }
    }
    private void loginUser() {
        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(txtEmail, txtPwd)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG_E, "onSuccess: Logged in...");
                        progressDialog.dismiss();

                        // Save checkbox state in SharedPreferences
                        saveCheckboxState(binding.chkSaveLoginInfo.isChecked());
                        if (binding.chkSaveLoginInfo.isChecked()) {
                            // Save email and password in SharedPreferences
                            saveLoginCredentials(txtEmail, txtPwd);
                        }
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG_E, "onFailure", e);
                        progressDialog.dismiss();
                        MyUtils.toast(LoginActivity.this, "Failed due to" + e.getMessage());
                    }
                });
    }


    // Method to save email and password in SharedPreferences
    private void saveLoginCredentials(String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_EMAIL, email);
        editor.putString(PREF_PASSWORD, password);
        editor.apply();
    }
    // Method to save checkbox state in SharedPreferences
    private void saveCheckboxState(boolean isChecked) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_CHECKBOX_STATE, isChecked);
        editor.apply();
    }

    public void signUpClicked(View view) {
        Intent intent = new Intent(this, RegisterEmailActivity.class);
        startActivity(intent);
    }
}