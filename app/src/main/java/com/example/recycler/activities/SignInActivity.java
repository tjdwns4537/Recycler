package com.example.recycler.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recycler.R;
import com.example.recycler.databinding.ActivitySignInBinding;
import com.example.recycler.models.User;
import com.example.recycler.utilities.Constants;
import com.example.recycler.utilities.PreferenceManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private FirebaseFirestore database;
    private CallbackManager callbackManager;
//    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        configSignIn();
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
//        encodedImage = null;

        // 페이스북 CallbackManager 초기화
        callbackManager = CallbackManager.Factory.create();

        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(), ChatMainActivity.class);
            startActivity(intent);
            finish();
        }

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();

    }

    private void setListeners(){
        binding.buttonSignIn.setOnClickListener(view -> {
            if (isVaildSignDetails()) signInWithEmailAndPassword();
        });
        binding.textSearchAccount.setOnClickListener(view -> {});
        binding.textUpdatePassword.setOnClickListener(view -> {});
        binding.textCreateNewAccount.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        binding.buttonGoogleSignin.setOnClickListener(view -> signInWithGoogle());
        binding.buttonFacebookSignin.setOnClickListener(view -> singInWithFacebook());
    }

    private void signInWithEmailAndPassword() {
        loading(true);
        String email = binding.inputEamil.getText().toString().trim();
        String password =  binding.inputPassword.getText().toString().trim();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "signInWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        if(user != null) {
                            database.collection(Constants.KEY_COLLECTION_USERS)
                                    .document(user.getUid())
                                    .get()
                                    .addOnCompleteListener(databaseTask -> {
                                        if (databaseTask.isSuccessful() && databaseTask.getResult().exists()) {
                                            databaseTask.getResult().toObject(User.class);
                                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, binding.checkBoxAutoSignIn.isChecked());
                                            preferenceManager.putString(Constants.KEY_USER_ID, user.getUid());
                                            preferenceManager.putString(Constants.KEY_USER_NAME, databaseTask.getResult().toObject(User.class).getName());
                                            preferenceManager.putString(Constants.KEY_USER_IMAGE_URI, databaseTask.getResult().toObject(User.class).getImageUrl());
                                            Log.d("TAG", "사용자 ID:" + user.getUid() + ", 사용자 이름:" + databaseTask.getResult().toObject(User.class).getName());
                                            Intent intent = new Intent(getApplicationContext(), ChatMainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        } else {
                                            loading(false);
                                            showToast("Unable to sign in");
                                        }
                                    });
                        }
                    } else {
                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                        showToast("Authentication failed.");
                    }
                });
    }

    private void configSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Constants.GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }
        } else if (requestCode == Constants.FACEBOOK_SIGN_IN){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success");
                        FirebaseUser user = auth.getCurrentUser();
                        if(!preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
                            createDatabaseForUser(user);
                        } else {
                            database.collection(Constants.KEY_COLLECTION_USERS)
                                    .document(user.getUid())
                                    .get().addOnSuccessListener(documentSnapshot -> {
                                        User mUser = documentSnapshot.toObject(User.class);
                                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, binding.checkBoxAutoSignIn.isChecked());
                                        preferenceManager.putString(Constants.KEY_USER_ID, user.getUid());
                                        preferenceManager.putString(Constants.KEY_USER_NAME, mUser.getName());
                                        preferenceManager.putString(Constants.KEY_USER_IMAGE_URI, mUser.getImageUrl());
                                        Log.d("TAG", "사용자 ID:" + user.getUid() + ", 사용자 이름:" + mUser.getName());
                                        goToMainActivity();
                                    }).addOnFailureListener(e -> Log.d("TAG", e.getMessage()));
                        }

//                        if(curUser != null){
//                            if(!preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
//
//                                new DownloadFilesTask().execute(curUser.getPhotoUrl().toString());
//
//                                if (encodedImage != null) {
//                                    User user = new User(curUser.getDisplayName(),
//                                                        encodedImage,
//                                                        curUser.getEmail());
//                                    user.setId(curUser.getUid());
//
//                                    database.collection(Constants.KEY_COLLECTION_USERS)
//                                            .document(curUser.getUid())
//                                            .set(user)
//                                            .addOnSuccessListener(unused -> {
//                                                Log.d("TAG", "사용자 생성");
//                                                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
//                                                preferenceManager.putString(Constants.KEY_USER_ID, curUser.getUid());
//                                                preferenceManager.putString(Constants.KEY_NAME, curUser.getDisplayName());
//                                                preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
//                                                Intent intent = new Intent(getApplicationContext(), ChatMainActivity.class);
//                                                intent.addFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                                                startActivity(intent);
//                                            })
//                                            .addOnFailureListener(e -> Log.d("TAG", e.getMessage()));
//                                } else {
//                                }
//                            }
//                        }
                    } else {
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                    }
                });
    }

    private void singInWithFacebook(){
        // LoginManager instance 얻어오기
        LoginManager mLoginManager = LoginManager.getInstance();
        mLoginManager.logInWithReadPermissions(this, Arrays.asList(Constants.EMAIL, Constants.PUBLIC_PROFILE)); // 권한 설정하기(사용자의 어떤 항목을 조회할 수 있는지)
        mLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {   // callback 등록

            @Override
            // 파일로 따로 만들어도 되고, 내부 클래스로 구현해도 된다.
            public void onSuccess(LoginResult loginResult) {                  // 로그인 성공 시
                Log.d("TAG", "facebook:onSuccess:" + loginResult);

                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("TAG", "facebook:onCancel");
            }

            @Override
            public void onError(@NonNull FacebookException error) {
                Log.d("TAG", "facebook:onError:" + error);
            }
        });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "signInWithCredential:success");
                        FirebaseUser user = auth.getCurrentUser();

                            if (!preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
                                createDatabaseForUser(user);
                            } else {
                                database.collection(Constants.KEY_COLLECTION_USERS)
                                        .document(user.getUid())
                                        .get().addOnSuccessListener(documentSnapshot -> {
                                            User mUser = documentSnapshot.toObject(User.class);
                                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, binding.checkBoxAutoSignIn.isChecked());
                                            preferenceManager.putString(Constants.KEY_USER_ID, user.getUid());
                                            preferenceManager.putString(Constants.KEY_USER_NAME, mUser.getName());
                                            preferenceManager.putString(Constants.KEY_USER_IMAGE_URI, mUser.getImageUrl());
                                            Log.d("TAG", "사용자 ID:" + user.getUid() + ", 사용자 이름:" + mUser.getName());
                                            goToMainActivity();
                                        }).addOnFailureListener(e -> Log.d("TAG", e.getMessage()));
                            }

//                        if(curUser != null){
//                            if(!preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
//
//                                new DownloadFilesTask().execute(curUser.getPhotoUrl().toString());
//
//                                if (encodedImage != null) {
//                                    User user = new User(curUser.getDisplayName(),
//                                            encodedImage,
//                                            curUser.getEmail());
//                                    user.setId(curUser.getUid());
//
//                                    database.collection(Constants.KEY_COLLECTION_USERS)
//                                            .document(curUser.getUid())
//                                            .set(user)
//                                            .addOnSuccessListener(unused -> {
//                                                Log.d("TAG", "사용자 생성");
//                                                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
//                                                preferenceManager.putString(Constants.KEY_USER_ID, curUser.getUid());
//                                                preferenceManager.putString(Constants.KEY_NAME, curUser.getDisplayName());
//                                                preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
//                                                Intent intent = new Intent(getApplicationContext(), ChatMainActivity.class);
//                                                intent.addFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                                                startActivity(intent);
//                                            })
//                                            .addOnFailureListener(e -> Log.d("TAG", e.getMessage()));
//                                } else {
//                                }
//                            }
//                        }

                    } else {
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        showToast("Authentication failed.");
                    }
                });
    }

    private void createDatabaseForUser(FirebaseUser curUser){
        if(curUser != null){
//                new DownloadFilesTask().execute(curUser.getPhotoUrl().toString());

//                if (encodedImage != null) {
                    User user = new User(curUser.getDisplayName(),
                                        curUser.getPhotoUrl().toString(),
                                        curUser.getEmail(),
                                        curUser.getUid());
//                    user.setId(curUser.getUid());

                    database.collection(Constants.KEY_COLLECTION_USERS)
                            .document(curUser.getUid())
                            .set(user)
                            .addOnSuccessListener(unused -> {
                                Log.d("TAG", "database 접속 성공");
                                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                preferenceManager.putString(Constants.KEY_USER_ID, curUser.getUid());
                                preferenceManager.putString(Constants.KEY_USER_NAME, curUser.getDisplayName());
                                preferenceManager.putString(Constants.KEY_USER_IMAGE_URI, curUser.getPhotoUrl().toString());
                                goToMainActivity();
                            })
                            .addOnFailureListener(e -> Log.d("TAG", e.getMessage()));
//                } else {
//                    return;
//                }

        }
    }

//    private class DownloadFilesTask extends AsyncTask<String,Void, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String result= null;
//            try {
//                String img_url = strings[0]; //url of the image
//                URL url = new URL(img_url);
//                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                result = encodeImage(bitmap);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            Log.d("TAG", "DownloadFilesTask 시작");
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            Log.d("TAG", "DownloadFilesTask 끝");
////            encodedImage = result;
//        }
//    }

//    private String encodeImage(Bitmap bitmap){
//        int previewWidth = 150;
//        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
//        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
//        byte[] bytes = byteArrayOutputStream.toByteArray();
//        return Base64.encodeToString(bytes, Base64.DEFAULT);
//    }

/*
    private void signIn(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEamil.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        Intent intent = new Intent(getApplicationContext(), ChatMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        loading(false);
                        showToast("Unable to sign in");
                    }
                });

    }
*/

    private void goToMainActivity(){
        Intent intent = new Intent(this, ChatMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isVaildSignDetails(){
        if(binding.inputEamil.getText().toString().trim().isEmpty()){
            showToast("Enter vaild email");
            return false;
        } else if(binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Enter password");
            return false;
        } else {
            return true;
        }
    }


}
