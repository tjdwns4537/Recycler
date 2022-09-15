package com.example.recycler.activities;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.example.recycler.R;
import com.example.recycler.databinding.BoardaddBinding;
import com.example.recycler.models.BoardModel;
import com.example.recycler.models.MainPageData;
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
    public Uri imageUri;
    public String pathUri;
    public String uriPath;
    public String resultPath;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String[] category;
    AlertDialog.Builder builder;

    public String selectCategory;
    public int selectNum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BoardaddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    public void init() {
        mAuth = new FBAuth();
        registerBtnAction(); // 게시글 등록 버튼 클릭
        CameraButtonClick(); // 카메라 버튼 클릭
        CategoryButton(); // 카테고리 버튼 클릭
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
                selectCategory = category[selectNum];

                if(imageUri != null){
                    uriPath = getPath(imageUri);

                    Uri file = Uri.fromFile(new File(uriPath)); // 절대경로uri를 file에 할당
                    Log.d(TAG, "photo file : " + file);

                    // stroage images에 절대경로파일 저장
                    StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());

                    riversRef.putFile(file).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            String tempPath = file.getLastPathSegment();
                            resultPath = tempPath.substring(tempPath.lastIndexOf("/") + 1);
                        }
                    });
                } else {
                    uriPath = "";
                    resultPath = "";
                }

                BoardModel boardModel = new BoardModel(title, content, uid, time, uriPath, resultPath, selectCategory);
                MainPageData mainPageData = new MainPageData(2, title, content, time, resultPath);

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

                db.collection("homePage")
                        .add(mainPageData)
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

                finish();
            }
        });
    }

    public void CameraButtonClick() {
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
            binding.ImagePlusCamera.setImageURI(data.getData());
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

    public void CategoryButton() {
        binding.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    public void showDialog() {
        category = getResources().getStringArray(R.array.category);

        builder = new AlertDialog.Builder(BoardAddActivity.this);

        builder.setTitle("카테고리");
        builder.setIcon(R.drawable.category_bold);

        builder.setItems(category, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setArrayNumber(which); // 카테고리를 정해준다.
                binding.category.setText(category[selectNum]); // 카테고리명 변경
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.show();
    }

    public void setArrayNumber(int which) {
        for (int i = 0; i < category.length; i++) {
            if(category[i].equals(category[which])){
                selectNum = which;
            }
        }
    }
}
