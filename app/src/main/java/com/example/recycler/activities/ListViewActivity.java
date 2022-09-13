package com.example.recycler.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.recycler.adapters.ListViewAdapter;
import com.example.recycler.R;
import com.example.recycler.models.BoardModel;
import com.example.recycler.utilities.Constants;
import com.example.recycler.utilities.FBdatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Value;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    public ListView listview;

    public ArrayList<BoardModel> boardDataList = new ArrayList<>();

    public ListViewAdapter adapter;

    public String TAG = ListViewActivity.class.getSimpleName();

//    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_community);

//        init();

        listview = (ListView) findViewById(R.id.listview);

        adapter = new ListViewAdapter(boardDataList);

        //리스트뷰에 Adapter 설정
        listview.setAdapter(adapter);

    }

//    public void init() {
//        getBoardData(); // 리스트에 데이터를 담는다.
//
//    }
//
//    public void getBoardData() {
//
//        db.collection("board")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//
//                            boardDataList.clear();
//
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                BoardModel item = document.toObject(BoardModel.class);
//
//                                boardDataList.add(item);
//
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//
//                            adapter.notifyDataSetChanged();
//
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//    }
}