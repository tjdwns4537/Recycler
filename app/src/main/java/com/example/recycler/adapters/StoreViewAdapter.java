package com.example.recycler.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.recycler.R;
import com.example.recycler.activities.BoardInsideActivity;
import com.example.recycler.activities.ListViewActivity;
import com.example.recycler.activities.StoreInsideActivity;
import com.example.recycler.models.BoardModel;
import com.example.recycler.models.StoreModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class StoreViewAdapter extends BaseAdapter {

    public String TAG = StoreViewAdapter.class.getSimpleName();
    public ArrayList<StoreModel> items = new ArrayList<>();

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference pathRef = storageRef.child("images/");

    public StoreViewAdapter(ArrayList<StoreModel>storeDataList) {
        items = storeDataList;
    }

    @Override
    public int getCount() {
        return items.size();
    }
    public void addItem(ArrayList<StoreModel> item) {
        this.items = item;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final StoreModel storeModel = items.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.store_list, viewGroup, false);

        } else {
            View view = new View(context);
            view = (View) convertView;
        }
        TextView store_title = (TextView) convertView.findViewById(R.id.store_title);
        TextView store_price = (TextView) convertView.findViewById(R.id.store_price);
        TextView store_registertime = (TextView) convertView.findViewById(R.id.store_registertime);
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);

        String boardText = storeModel.getContent();

        if(boardText.length() >= 7){
            boardText = boardText.substring(0, 7) + "...";
        }

        store_title.setText(boardText);
        store_price.setText(storeModel.getPrice());
        store_registertime.setText(storeModel.getTime());

        // 이미지 불러오기
        if ((storeModel.getPhotoName() != null) && (storeModel.getPhotoName() != "")) {
            String photoName = storeModel.getPhotoName();
            String resultPath = photoName.substring(photoName.lastIndexOf("/") + 1);

            StorageReference submitProfile = storageRef.child("images/" + resultPath);
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d(TAG, uri.toString());
                    Glide.with(ivIcon).load(uri).into(ivIcon);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

        //각 아이템 선택 event
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StoreInsideActivity.class);
                intent.putExtra("title", storeModel.getTitle());
                intent.putExtra("content", storeModel.getContent());
                intent.putExtra("time", storeModel.getTime());
                intent.putExtra("uid",storeModel.getUid());

                if(storeModel.getPhotoName() != null){
                    intent.putExtra("photoName", storeModel.getPhotoName());
                    intent.putExtra("photo", storeModel.getPhoto());
                }
                context.startActivity(intent);
            }
        });

        return convertView;  //뷰 객체 반환
    }
}
