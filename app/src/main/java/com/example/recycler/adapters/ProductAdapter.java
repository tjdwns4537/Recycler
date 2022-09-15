package com.example.recycler.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recycler.R;
import com.example.recycler.fragment.HomeFragment;
import com.example.recycler.listeners.OnProductItemClickListener;
import com.example.recycler.models.BoardModel;
import com.example.recycler.models.StoreModel;
import com.example.recycler.models.ViewDataType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements OnProductItemClickListener{

    ArrayList<StoreModel> items = new ArrayList<>();
    OnProductItemClickListener listener;

    public ProductAdapter(ArrayList<StoreModel> item) {
        this.items = item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.home_item, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoreModel item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(StoreModel item){
        items.add(item);
    }
    public void setItems(ArrayList<StoreModel> items){
        this.items = items;
    }
    public StoreModel getItem(int position){
        return items.get(position);
    }
    public void setItem(int position,StoreModel item){
        items.set(position,item);
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }
    public void setOnItemClickListener(OnProductItemClickListener listener){
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textName;
        TextView textCost;
        TextView textTime;
        ImageView ivIcon;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathRef = storageRef.child("images/");

        public String TAG = ViewHolder.class.getSimpleName();

        public ViewHolder(View itemView, final OnProductItemClickListener listener){

            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textName = itemView.findViewById(R.id.textName);
            textCost = itemView.findViewById(R.id.textCost);
            textTime = itemView.findViewById(R.id.textTime);
            ivIcon = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, view,position);
                    }
                }
            });
        }

        public void setItem(StoreModel item){

            textTime.setText(item.getTime());
            textName.setText(item.getTitle());
            textCost.setText(item.getPrice());

            // 이미지 불러오기
            if ((item.getPhotoName() != null) && (item.getPhotoName() != "")) {
                String photoName = item.getPhotoName();
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

        }
    }
}