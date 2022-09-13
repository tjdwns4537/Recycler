package com.example.recycler.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.recycler.activities.BoardInsideActivity;
import com.example.recycler.activities.ListViewActivity;
import com.example.recycler.R;
import com.example.recycler.databinding.ActivityListviewBinding;
import com.example.recycler.databinding.ListviewListItemBinding;
import com.example.recycler.fragment.CommunityFragment;
import com.example.recycler.models.BoardModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/* 리스트뷰 어댑터 */
public class ListViewAdapter extends BaseAdapter {

    public String TAG = ListViewActivity.class.getSimpleName();
    public ArrayList<BoardModel> items = new ArrayList<>();

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference pathRef = storageRef.child("images/");

    public ListViewAdapter(ArrayList<BoardModel> boardDataList) {
        items = boardDataList;
    }

    @Override
    public int getCount() {
        return items.size();
    }
    public void addItem(ArrayList<BoardModel> item) {
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
        final BoardModel boardModel = items.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_list_item, viewGroup, false);

        } else {
            View view = new View(context);
            view = (View) convertView;
        }
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);

        tv_name.setText(boardModel.getTitle());
        tv_content.setText(boardModel.getContent());
        tv_time.setText(boardModel.getTime());

        // 이미지 불러오기
        if ((boardModel.getPhotoName() != null) && (boardModel.getPhotoName() != "")) {
            String photoName = boardModel.getPhotoName();
            String resultPath = photoName.substring(photoName.lastIndexOf("/") + 1);

            Toast.makeText(context, boardModel.getTitle(),Toast.LENGTH_SHORT).show();

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
                Intent intent = new Intent(context,BoardInsideActivity.class);
                intent.putExtra("title", boardModel.getTitle());
                intent.putExtra("content", boardModel.getContent());
                intent.putExtra("time", boardModel.getTime());
                intent.putExtra("uid",boardModel.getUid());

                if(boardModel.getPhotoName() != null){
                    intent.putExtra("photoName", boardModel.getPhotoName());
                    intent.putExtra("photo", boardModel.getPhoto());
                }
                context.startActivity(intent);
            }
        });

        return convertView;  //뷰 객체 반환
    }
}