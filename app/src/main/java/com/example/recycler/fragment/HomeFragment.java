package com.example.recycler.fragment;

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

import com.example.recycler.R;
import com.example.recycler.adapters.ListViewAdapter;
import com.example.recycler.adapters.ProductAdapter;
import com.example.recycler.adapters.StoreViewAdapter;
import com.example.recycler.databinding.FragmentStoreBinding;
import com.example.recycler.listeners.OnProductItemClickListener;
import com.example.recycler.models.BoardModel;
import com.example.recycler.models.StoreModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private String TAG = HomeFragment.class.getSimpleName();

    public RecyclerView recyclerView;
    public ListView story_recyclerView;
    public RecyclerView close_recyclerView;
    public RecyclerView md_recyclerView;

    public GridLayoutManager layoutManager;
    public GridLayoutManager md_layoutManager;
    public GridLayoutManager close_layoutManager;

    public ProductAdapter adapter;
    public ListViewAdapter ListViewadapter;

    public ArrayList<StoreModel> storeDataList = new ArrayList<>();
    public ArrayList<BoardModel> boardDataList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_home, container, false);

        getFireStoreStoreData();
        getBoardData();

        ListViewadapter = new ListViewAdapter(boardDataList);
        adapter = new ProductAdapter(storeDataList);

        //??????????????? Adapter ??????

        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        story_recyclerView = (ListView) inflate.findViewById(R.id.RecyclerStory_recyclerView);
        md_recyclerView = (RecyclerView) inflate.findViewById(R.id.MDRecycler_recyclerView);
        close_recyclerView = (RecyclerView) inflate.findViewById(R.id.close_recyclerView);

        layoutManager = new GridLayoutManager(getContext(),2);
        close_layoutManager = new GridLayoutManager(getContext(),2);
        md_layoutManager = new GridLayoutManager(getContext(),2);

        recyclerView.setLayoutManager(layoutManager);
        close_recyclerView.setLayoutManager(close_layoutManager);
        md_recyclerView.setLayoutManager(md_layoutManager);

        recyclerView.setAdapter(adapter);
        story_recyclerView.setAdapter(ListViewadapter);
        close_recyclerView.setAdapter(adapter);
        md_recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onItemClick(ProductAdapter.ViewHolder holder, View view, int position) {
                StoreModel item = adapter.getItem(position);
                Toast.makeText(getContext(), "?????? : " + item.getTitle() + "\n ?????? : " + item.getPrice() +
                        "\n ?????? : " + item.getTime(),Toast.LENGTH_LONG).show();
            }
        });

        return inflate;
    }

    public void getFireStoreStoreData(){

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

    public void getBoardData() {
        db.collection("board")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boardDataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                BoardModel item = document.toObject(BoardModel.class);
                                boardDataList.add(item);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            ListViewadapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}