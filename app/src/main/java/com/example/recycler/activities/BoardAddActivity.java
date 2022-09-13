package com.example.recycler.activities;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.example.recycler.databinding.BoardaddBinding;
import com.example.recycler.models.BoardModel;
import com.example.recycler.utilities.FBAuth;
import com.example.recycler.utilities.FBdatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;

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
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

                String uriPath = getPath(imageUri);

                Uri file = Uri.fromFile(new File(uriPath)); // 절대경로uri를 file에 할당
                Log.d(TAG, "photo file : " + file);

                // stroage images에 절대경로파일 저장
                StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());

                riversRef.putFile(file).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        String tempPath = file.getLastPathSegment();
                        resultPath = tempPath.substring(tempPath.lastIndexOf("/")+1);

                        BoardModel boardModel = new BoardModel(title, content, uid, time, uriPath, resultPath);

//                        Toast.makeText(BoardAddActivity.this, resultPath,Toast.LENGTH_SHORT).show();

                        db.collection("board")
                                .add(boardModel)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });
                    }
                });

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
            imageUri = data.getData();
            pathUri = getPath(data.getData());
            binding.ImagePlus.setImageURI(data.getData());

        }
    }

    // uri 절대경로 가져오기
    public String getPath(Uri uri){

        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }
}
