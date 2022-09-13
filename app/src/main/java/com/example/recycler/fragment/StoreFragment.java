package com.example.recycler.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.recycler.R;
import com.example.recycler.adapters.ListViewAdapter;
import com.example.recycler.adapters.StoreViewAdapter;
import com.example.recycler.databinding.FragmentStoreBinding;
import com.example.recycler.models.StoreModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StoreFragment extends Fragment {

    public FragmentStoreBinding binding;
    public ListView listview;
    public ArrayList<StoreModel> storeDataList = new ArrayList<>();
    public StoreViewAdapter adapter;
    public String TAG = StoreFragment.class.getSimpleName();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static StoreFragment newInstance(){
        return new StoreFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_store, container, false);

        listview = (ListView) inflate.findViewById(R.id.listview);

        adapter = new StoreViewAdapter(storeDataList);

        //리스트뷰에 Adapter 설정
        listview.setAdapter(adapter);

        init();

        return inflate;
    }

    public void init() {
        getBoardData(); // 리스트에 데이터를 담는다.
    }

    public void getBoardData() {
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