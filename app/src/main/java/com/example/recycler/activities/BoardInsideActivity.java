package com.example.recycler.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.recycler.R;
import com.example.recycler.databinding.ActivityBoardInsideBinding;
import com.example.recycler.databinding.BoardaddBinding;
import com.example.recycler.utilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BoardInsideActivity extends AppCompatActivity {

    private String TAG = BoardInsideActivity.class.getSimpleName();

    ActivityBoardInsideBinding binding;
    BoardAddActivity boardAddActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBoardInsideBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        String title = getIntent().getStringExtra("title").toString();
        String content = getIntent().getStringExtra("content").toString();
        String time = getIntent().getStringExtra("time").toString();
        String key = BoardAddActivity.key;

//        String key = Constants.boardRef.push().getKey().toString();
//        Log.d(TAG, Constants.boardRef.push().getKey().toString());

        binding.titleArea.setText(title);
        binding.textArea.setText(content);
        binding.timeArea.setText(time);


        FirbaseGetImageData(key);

        Log.d(TAG, title);
        Log.d(TAG, content);
        Log.d(TAG, time);


    }

    public void FirbaseGetImageData(String key){
        // Reference to an image file in Cloud Storage
        ImageView imageView = binding.getImageArea;

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(key+".png");
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference("-NBHafkXFUOTgVVToEFL.png");

        // ImageView in your Activity

        //Test1
//        Glide.with(this /* context */)
//                .load(storageReference)
//                .centerCrop()
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(imageView);

//test3
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Glide.with(BoardInsideActivity.this)
                            .load(task.getResult())
                            .into(imageView);
               } else {

                }
            }
        });

        //Test2
//        storageRef.child("users/me/profile.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                // Got the download URL for 'users/me/profile.png'
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });

        //Test3
//        // Download directly from StorageReference using Glide
//        // (See MyAppGlideModule for Loader registration)
//        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener(Task<TResult> task) {
//            @Override
//            public void onComplete() {
//                if(task.isSuccessful()){
//
//                }
//            }
//        });)
    }

}
