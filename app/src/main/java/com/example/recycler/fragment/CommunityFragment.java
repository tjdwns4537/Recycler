package com.example.recycler.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.recycler.activities.ListViewActivity;
import com.example.recycler.adapters.ListViewAdapter;
import com.example.recycler.R;
import com.example.recycler.databinding.BoardaddBinding;
import com.example.recycler.databinding.ListviewListItemBinding;
import com.example.recycler.models.BoardModel;
import com.example.recycler.models.User;
import com.example.recycler.utilities.Constants;
import com.example.recycler.utilities.FBdatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CommunityFragment extends Fragment {

    public CommunityFragment binding;
    public ListView listview;
    public ArrayList<BoardModel> boardDataList = new ArrayList<>();
    public ListViewAdapter adapter;
    public String TAG = CommunityFragment.class.getSimpleName();

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static CommunityFragment newInstance(){
        return new CommunityFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_community, container, false);

        listview = (ListView) inflate.findViewById(R.id.listview);

        adapter = new ListViewAdapter(boardDataList);

        //리스트뷰에 Adapter 설정
        listview.setAdapter(adapter);

        init();

        return inflate;
    }

    public void init() {
        getBoardData(); // 리스트에 데이터를 담는다.
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
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}