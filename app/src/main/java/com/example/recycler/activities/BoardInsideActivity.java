package com.example.recycler.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.recycler.R;
import com.example.recycler.databinding.ActivityBoardInsideBinding;
import com.example.recycler.databinding.BoardaddBinding;
import com.example.recycler.models.BoardModel;
import com.example.recycler.utilities.Constants;
import com.example.recycler.utilities.FBdatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class BoardInsideActivity extends AppCompatActivity {

    private String TAG = BoardInsideActivity.class.getSimpleName();

    ActivityBoardInsideBinding binding;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference pathRef = storageRef.child("images/");

    String title;
    String content;
    String time;
    String uid;

    ArrayList<String> IUri;
    BoardModel value;

    private DatabaseReference mDatabase;
    String r = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBoardInsideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDatabase = FirebaseDatabase.getInstance().getReference();

        title = getIntent().getStringExtra("title").toString();
        content = getIntent().getStringExtra("content").toString();
        time = getIntent().getStringExtra("time").toString();
        uid = getIntent().getStringExtra("uid").toString();

        binding.titleArea.setText(title);
        binding.textArea.setText(content);
        binding.timeArea.setText(time);

        Log.d(TAG, title);
        Log.d(TAG, content);
        Log.d(TAG, time);

        FirbaseGetImageData2();
    }


    public void FirbaseGetImageData2(){
        ImageView imageView = binding.ImageArea;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("board");

        IUri = new ArrayList<>();
        String uid = "";

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    value = dataSnapshot.getValue(BoardModel.class);
                    IUri.add(value.getPhotoName());
                }
                if (pathRef == null) {
                    Log.d(TAG, "저장된 사진 없음");
                } else {
                    StorageReference submitProfile = storageRef.child("images/" + value.getPhotoName());
                    submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(TAG, uri.toString());
                            Glide.with(BoardInsideActivity.this).load(uri).into(imageView);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//                BoardModel boardModel = dataSnapshot.getValue(BoardModel.class);
//                r = boardModel.getPhotoName();
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//            }
//        };
//        mDatabase.addValueEventListener(postListener);


        if (pathRef == null) {
            Log.d(TAG, "저장된 사진 없음");
        } else{
            StorageReference submitProfile = storageRef.child("images/"+FBdatabase.Imagepath);
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d(TAG, uri.toString());
                    Glide.with(BoardInsideActivity.this).load(uri).into(imageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

    }

//    public void FirbaseGetImageData(){
//        // Reference to an image file in Cloud Storage
//        ImageView imageView = binding.getImageArea;
//
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//
////        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(key+".png");
////        StorageReference storageReference = FirebaseStorage.getInstance().getReference("-NBHafkXFUOTgVVToEFL.png");
//
//        // ImageView in your Activity
//
//        storageRef.child("images/mountains.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
//
//
////        Test1
////        Glide.with(this /* context */)
////                .load(storageReference)
////                .into(imageView);
//
////test3
////        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener() {
////            @Override
////            public void onComplete(@NonNull Task task) {
////                if(task.isSuccessful()){
////                    Glide.with(BoardInsideActivity.this)
////                            .load(task.getResult())
////                            .into(imageView);
////               } else {
////
////                }
////            }
////        });
//
//        //Test2
////        storageRef.child("users/me/profile.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////            @Override
////            public void onSuccess(Uri uri) {
////                // Got the download URL for 'users/me/profile.png'
////            }
////        }).addOnFailureListener(new OnFailureListener() {
////            @Override
////            public void onFailure(@NonNull Exception exception) {
////                // Handle any errors
////            }
////        });
//
//        //Test3
////        // Download directly from StorageReference using Glide
////        // (See MyAppGlideModule for Loader registration)
////        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener(Task<TResult> task) {
////            @Override
////            public void onComplete() {
////                if(task.isSuccessful()){
////
////                }
////            }
////        });)
//    }

}
