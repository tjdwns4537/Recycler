package com.example.recycler.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.recycler.MainActivity;
import com.example.recycler.R;
import com.example.recycler.databinding.ActivityBoardInsideBinding;
import com.example.recycler.databinding.BoardaddBinding;
import com.example.recycler.models.BoardModel;
import com.example.recycler.utilities.Constants;
import com.example.recycler.utilities.FBAuth;
import com.example.recycler.utilities.FBdatabase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BoardAddActivity extends AppCompatActivity {

    public BoardaddBinding binding;
    public FBAuth mAuth;
    public String TAG = "BoardWriteActivity";
    public String title;
    public String content;
    public String uid;
    public String time;
    public static String path;
    public static String key;
    public Uri imageUri;
    public String pathUri;
    public String resultPath;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    BoardModel boardModel = new BoardModel();

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
//        binding.registerBtn.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "click");
//
//                //변수
//                title = binding.titleWrite.getText().toString();
//                content = binding.contentWrite.getText().toString();
//                uid = mAuth.getUid();
//                time = mAuth.getTime();
//
//                key = FBdatabase.boardRef.push().getKey().toString(); // 이미지 키를 먼저 받아옴
//
////                uploadKey(key);
////                uploadUri();
//
//                Log.d(TAG, title);
//                Log.d(TAG, content);
//                Log.d(TAG, uid);
//                Log.d(TAG, time);
//
//                // firebase database에 저장하는 부분
////                FBdatabase.boardRef
////                        .child(key)
////                        .setValue(new BoardModel(title, content, uid, time));
//
//                finish();
//            }
//        });
        binding.registerBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click");

                //변수
                title = binding.titleWrite.getText().toString();
                content = binding.contentWrite.getText().toString();
                uid = mAuth.getUid();
                time = mAuth.getTime();
//                key = FBdatabase.boardRef.push().getKey().toString(); // 이미지 키를 먼저 받아옴

                String uriPath = getPath(imageUri);

                Uri file = Uri.fromFile(new File(uriPath)); // 절대경로uri를 file에 할당
                Log.d(TAG, "photo file : " + file);

                // stroage images에 절대경로파일 저장
                StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());

                riversRef.putFile(file).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        final Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();

                        boardModel.setPhoto(uriPath);

                        String tempPath = file.getLastPathSegment();
                        resultPath = tempPath.substring(tempPath.lastIndexOf("/")+1);

                        boardModel.setPhotoName(resultPath);
                        boardModel.photoName = resultPath;

                        FBdatabase.Imagepath = resultPath; // 전역 변수라 사용 못함

                        Toast.makeText(BoardAddActivity.this, boardModel.photoName,Toast.LENGTH_SHORT).show();

                        FBdatabase.boardRef
                                .push()
                                .setValue(new BoardModel(title, content, uid, time, resultPath));

                        Log.d(TAG, boardModel.getPhoto());
                        Log.d(TAG, boardModel.getPhotoName());

                    }
                });

//                // origin
//                UploadTask uploadTask = riversRef.putFile(file);
//                Log.d(TAG, "uploadTask : " + uploadTask);
//
//                // Register observers to listen for when the download is done or if it fails
//                uploadTask.addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle unsuccessful uploads
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        BoardModel boardModel = new BoardModel();
//
////                    boardModel.photo = uriPath;
//
//                        // 게시글 내용 저장
//                        FBdatabase.boardRef
//                                .child(key)
//                                .setValue(new BoardModel(title, content, uid, time, uriPath));
//
//                    }
//                });

                // firebase database에 저장하는 부분
//                FBdatabase.boardRef
//                        .child(key)
//                        .setValue(new BoardModel(title, content, uid, time));

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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK && requestCode == 100) {
//            Uri file = data.getData();
//            binding.ImagePlus.setImageURI(data.getData());
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data.getData();
            pathUri = getPath(data.getData());
            binding.ImagePlus.setImageURI(data.getData());

        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK && requestCode == 100) {
//
//            Uri file = data.getData();
//
//            String uriPath = getPath(file);
//
//            // #2
//            Uri file2 = Uri.fromFile(new File(uriPath));
//
//            binding.ImagePlus.setImageURI(data.getData());
//            StorageReference storageRef = storage.getReference();
//
////            StorageReference reiversRef = storageRef.child(file.toString());
//            StorageReference reiversRef = storageRef.child("images/"+file.getLastPathSegment());
//
//
//            UploadTask uploadTask = reiversRef.putFile(file);
//
//            try{
//                InputStream in = getContentResolver().openInputStream(data.getData());
//                Bitmap img = BitmapFactory.decodeStream(in);
//                in.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                }
//            });
//        }
//    }

    // uri 절대경로 가져오기
    public String getPath(Uri uri){

        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);

    }

//    public void uploadKey(String key) {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//
//        StorageReference storageRef = storage.getReference();
//
//        StorageReference mountainsRef = storageRef.child(key + ".png");
//
//        ImageButton imageView = binding.ImagePlus;
//
//        imageView.setDrawingCacheEnabled(true);
//        imageView.buildDrawingCache();
//        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = mountainsRef.putBytes(data);
//
////        path = mountainsRef.getPath();
//
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                // ...
//            }
//        });
//    }
//
//    public void uploadUri() {
//
//        storage = FirebaseStorage.getInstance();
//
//        StorageReference storageRef = storage.getReference();
//
//        final StorageReference ref = storageRef.child("images/mountains.jpg");
//
//        Uri uri = Uri.fromFile(new File(ref.getPath()));
//
//        UploadTask uploadTask = ref.putFile(uri);
//
//        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    throw task.getException();
//                }
//
//                // Continue with the task to get the download URL
//                return ref.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//                } else {
//                    // Handle failures
//                    // ...
//                }
//            }
//        });
//    }
}
