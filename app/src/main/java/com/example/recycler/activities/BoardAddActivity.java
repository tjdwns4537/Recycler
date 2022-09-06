package com.example.recycler.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.recycler.MainActivity;
import com.example.recycler.R;
import com.example.recycler.databinding.ActivityBoardInsideBinding;
import com.example.recycler.databinding.BoardaddBinding;
import com.example.recycler.models.BoardModel;
import com.example.recycler.utilities.Constants;
import com.example.recycler.utilities.FBAuth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class BoardAddActivity extends AppCompatActivity {

    public BoardaddBinding binding;
    public FBAuth mAuth;
    public String TAG = "BoardWriteActivity";
    public String title;
    public String content;
    public String uid;
    public String time;
    static public String key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BoardaddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    public void init() {
        mAuth = new FBAuth();
        registerBtnAction(); // 게시글 등록 버튼
    }

    public void registerBtnAction() {
        binding.registerBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click");

                //변수
                title = binding.titleWrite.getText().toString();
                content = binding.contentWrite.getText().toString();
                uid = mAuth.getUid();
                time = mAuth.getTime();

                key = Constants.boardRef.push().getKey().toString(); // 이미지 키를 먼저 받아옴

                Log.d(TAG, title);
                Log.d(TAG, content);
                Log.d(TAG, uid);
                Log.d(TAG, time);

                // firebase database에 저장하는 부분
                Constants.boardRef
                        .child(key)
                        .setValue(new BoardModel(title, content, uid, time));

                FirebaseCameraUpload(key);
//                FirbaseGetImageData(key);

                finish();
            }
        });

        binding.ImagePlus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 100);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 100) {
            binding.ImagePlus.setImageURI(data.getData());
        }
    }

    public void FirebaseCameraUpload(String key) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child(key + ".png");

        this.key = key;

        ImageButton imageView = binding.ImagePlus;

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });


    }
}
