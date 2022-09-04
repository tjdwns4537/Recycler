package com.example.recycler.activities;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.Arrays;
import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private FirebaseFirestore database;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        configSignIn();
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

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
                        Log.d(Constants.TAG, "signInWithEmail:success");
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if(firebaseUser != null) {
                            database.collection(Constants.KEY_COLLECTION_USERS)
                                    .document(firebaseUser.getUid())
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful() && task1.getResult().exists()) {
                                            User user = task1.getResult().toObject(User.class);
                                            Log.d(Constants.TAG, "사용자 ID:" + firebaseUser.getUid() + ", 사용자 이름:" + task1.getResult().toObject(User.class).getName());
                                            setPreferenceManager(user);
                                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, binding.checkBoxAutoSignIn.isChecked());
                                            goToMainActivity();
                                        } else {
                                            loading(false);
                                            showToast("Unable to sign in");
                                        }
                                    });
                        }
                    } else {
                        Log.w(Constants.TAG, "signInWithEmail:failure", task.getException());
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
                Log.d(Constants.TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(Constants.TAG, "Google sign in failed", e);
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
                        Log.d(Constants.TAG, "signInWithCredential:success");
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        database.collection(Constants.KEY_COLLECTION_USERS)
                                .whereEqualTo(Constants.KEY_USER, firebaseUser.getUid())
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful() && task1.getResult() != null){
                                        if (firebaseUser.getUid().equals(task1.getResult().getDocuments().isEmpty())){
                                            User mUser = task1.getResult().getDocuments().get(0).toObject(User.class);
                                            Log.d(Constants.TAG, "사용자 ID:" + firebaseUser.getUid() + ", 사용자 이름:" + mUser.getName());
                                            setPreferenceManager(mUser);
                                            goToMainActivity();
                                        } else {
                                            createUserInDatabase(firebaseUser);
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "Error getting documents: ", task1.getException());
                                    }
                                });
                    } else {
                        Log.w(Constants.TAG, "signInWithCredential:failure", task.getException());
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
                Log.d(Constants.TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(Constants.TAG, "facebook:onCancel");
            }

            @Override
            public void onError(@NonNull FacebookException error) {
                Log.d(Constants.TAG, "facebook:onError:" + error);
            }
        });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(Constants.TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(Constants.TAG, "signInWithCredential:success");
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        database.collection(Constants.KEY_COLLECTION_USERS)
                                .whereEqualTo(Constants.KEY_USER, firebaseUser.getUid())
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful() && task1.getResult() != null){
                                        if (firebaseUser.getUid().equals(task1.getResult().getDocuments().isEmpty())){
                                            User mUser = task1.getResult().getDocuments().get(0).toObject(User.class);
                                            Log.d(Constants.TAG, "사용자 ID:" + firebaseUser.getUid() + ", 사용자 이름:" + mUser.getName());
                                            setPreferenceManager(mUser);
                                            goToMainActivity();
                                        } else {
                                            createUserInDatabase(firebaseUser);
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "Error getting documents: ", task1.getException());
                                    }
                                });
                    } else {
                        Log.w(Constants.TAG, "signInWithCredential:failure", task.getException());
                        showToast("Authentication failed.");
                    }
                });
    }

    private void createUserInDatabase(FirebaseUser firebaseUser){
        if(firebaseUser != null){
            User user = new User(firebaseUser.getDisplayName(),
                    firebaseUser.getPhotoUrl().toString(),
                    firebaseUser.getEmail(),
                    firebaseUser.getUid());

            database.collection(Constants.KEY_COLLECTION_USERS)
                    .document(firebaseUser.getUid())
                    .set(user)
                    .addOnSuccessListener(unused -> {
                        Log.d(Constants.TAG, "database 접속 성공");
                        setPreferenceManager(user);
                        goToMainActivity();
                    })
                    .addOnFailureListener(e -> Log.d(Constants.TAG, e.getMessage()));
        }
    }

    private void setPreferenceManager(User user){
        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
        preferenceManager.putString(Constants.KEY_USER_ID, user.getId());
        preferenceManager.putString(Constants.KEY_USER_NAME, user.getName());
        preferenceManager.putString(Constants.KEY_USER_IMAGE_URI, user.getImageUrl());
    }



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
