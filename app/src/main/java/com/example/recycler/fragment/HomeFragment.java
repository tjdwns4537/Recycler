package com.example.recycler.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recycler.R;
import com.example.recycler.adapters.ProductAdapter;
import com.example.recycler.adapters.StoreViewAdapter;
import com.example.recycler.databinding.FragmentStoreBinding;
import com.example.recycler.listeners.OnProductItemClickListener;
import com.example.recycler.models.Product;
import com.example.recycler.models.StoreModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private String TAG = HomeFragment.class.getSimpleName();
    public FragmentStoreBinding binding;
    public RecyclerView recyclerView;
    public GridLayoutManager layoutManager;
    public ProductAdapter adapter;
    public ArrayList<StoreModel> storeDataList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);

        layoutManager = new GridLayoutManager(getContext(),2);

        recyclerView.setLayoutManager(layoutManager);

        getFireStoreData();

        adapter = new ProductAdapter(storeDataList);

//        adapter.addItem(new Product("구찌", "90만" , "세상에 12개 밖에 없음", R.drawable.scrap));
//        adapter.addItem(new Product("루이", "190만" , "세상에 1개 밖에 없음", R.drawable.scrap));
//        adapter.addItem(new Product("휠라", "9만" , "세상에 145개 밖에 없음", R.drawable.scrap));
//        adapter.addItem(new Product("슈프림", "90만" , "세상에 11개 밖에 없음", R.drawable.scrap));
//        adapter.addItem(new Product("아보키", "0.9만" , "세상에 11234개 밖에 없음", R.drawable.scrap));
//        adapter.addItem(new Product("바보", "100만" , "세상에 1111개 밖에 없음", R.drawable.scrap));
//        adapter.addItem(new Product("아메리카노", "90만" , "세상에 12개 밖에 없음", R.drawable.scrap));
//        adapter.addItem(new Product("횟초리", "190만" , "세상에 1개 밖에 없음", R.drawable.scrap));
//        adapter.addItem(new Product("찰싹", "9만" , "세상에 145개 밖에 없음", R.drawable.scrap));

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onItemClick(ProductAdapter.ViewHolder holder, View view, int position) {
                StoreModel item = adapter.getItem(position);
                Toast.makeText(getContext(), "이름 : " + item.getTitle() + "\n 가격 : " + item.getPrice() +
                        "\n 시간 : " + item.getTime(),Toast.LENGTH_LONG).show();
            }
        });

        return inflate;
    }

    public void getFireStoreData(){

        db.collection("store")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            storeDataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                StoreModel item = document.toObject(StoreModel.class);
                                storeDataList.add(item);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}