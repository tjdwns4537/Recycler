package com.example.recycler.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recycler.databinding.ActivitySignUpBinding;
import com.example.recycler.models.User;
import com.example.recycler.utilities.Constants;
import com.example.recycler.utilities.PreferenceManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private Bitmap bitmap;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private String downloadUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        setListeners();
    }

    private void setListeners(){
        binding.buttonBack.setOnClickListener(view -> onBackPressed());
        binding.textSignInToContinue.setOnClickListener(view -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(view -> {
            if (isValidSignUpDetails()){
                createAccount();
            }
        });
        binding.layoutImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void createAccount() {
        loading(true);
        String email = binding.inputEamil.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();
        String name = binding.inputName.getText().toString().trim();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "createUserWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        StorageReference storageRef = storage.getReference();
                        StorageReference usersRef = storageRef.child("images/"+ user.getUid() + "_profile_image.jpg");
                        Log.d("TAG", "usersRef.getPath():" + usersRef.getPath());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Log.d("TAG", bitmap.toString());
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        UploadTask uploadTask = usersRef.putBytes(data);

                        uploadTask.continueWithTask(subTask -> {
                            if (!subTask.isSuccessful()){
                                throw subTask.getException();
                            }
                            return usersRef.getDownloadUrl();
                        }).addOnCompleteListener(subTask -> {
                            if (subTask.isSuccessful()){
                                downloadUri = subTask.getResult().toString();
                                Log.d("TAG", "Download Uri:" + downloadUri);

                                if (downloadUri != null){
                                    // 유저 객체 생성한다.
                                    User newUser = new User(name, downloadUri, email, user.getUid());

                                    // 디비 초기화
                                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                                    // 디비에 유저 객체를 저장한다.
                                    database.collection(Constants.KEY_COLLECTION_USERS)
                                            .document(user.getUid())
                                            .set(newUser)
                                            .addOnSuccessListener(unused -> {
                                                loading(false);
                                                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                                preferenceManager.putString(Constants.KEY_USER_ID, user.getUid());
                                                preferenceManager.putString(Constants.KEY_USER_NAME, name);
                                                preferenceManager.putString(Constants.KEY_USER_IMAGE_URI, downloadUri);
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                intent.addFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                                startActivity(intent);
                                            }).addOnFailureListener(e -> {
                                                loading(false);
                                                showToast(e.getMessage());
                                            });
                                }
                            }
                        });

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        showToast("Authentication failed.");
                    }
                });
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                        } catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            });

    private Boolean isValidSignUpDetails(){
        if (binding.imageProfile.getDrawable() == null){
            showToast("Select profile image");
            return false;
        } else if(binding.inputName.getText().toString().trim().isEmpty()){
            showToast("Enter name");
            return false;
        } else if(binding.inputEamil.getText().toString().trim().isEmpty()){
            showToast("Enter email");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEamil.getText().toString().trim()).matches()){
            showToast("Enter vaild email");
            return false;
        } else if(binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Enter password");
            return false;
        } else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
            showToast("Password & confirm password must be same");
            return false;
        } else {
            return true;
        }
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        }
    }
}
