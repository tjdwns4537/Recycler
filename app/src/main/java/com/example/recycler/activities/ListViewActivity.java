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
import com.example.recycler.BearItem;
import com.example.recycler.databinding.ActivityListviewBinding;
import com.example.recycler.databinding.BoardaddBinding;
import com.example.recycler.databinding.ListviewListItemBinding;
import com.example.recycler.fragment.ChattingFragment;
import com.example.recycler.fragment.CommunityFragment;
import com.example.recycler.fragment.HomeFragment;
import com.example.recycler.fragment.MypageFragment;
import com.example.recycler.fragment.StoreFragment;
import com.example.recycler.MainActivity;
import com.example.recycler.R;
import com.example.recycler.models.BoardModel;
import com.example.recycler.utilities.Constants;
import com.example.recycler.utilities.FBdatabase;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.Value;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    public ListView listview;
    public ArrayList<BoardModel> boardDataList = new ArrayList<>();
    public ArrayList<String> boardKeyList = new ArrayList<>();

    public BoardModel item;

    public ListViewAdapter adapter;

    public String TAG = ListViewActivity.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_community);

        init();

        listview = (ListView) findViewById(R.id.listview);

        adapter = new ListViewAdapter(boardDataList);

        //리스트뷰에 Adapter 설정
        listview.setAdapter(adapter);

    }

    public void init() {
        getBoardData(); // 리스트에 데이터를 담는다.

    }

    public void getBoardData() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                boardDataList.clear();

                for (DataSnapshot dataModel: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    item = dataModel.getValue(BoardModel.class);
                    boardDataList.add(item);
                }

                for(BoardModel i : boardDataList){
                    Log.d(TAG, i.getTitle());Log.d(TAG, i.getContent());Log.d(TAG, i.getUid());Log.d(TAG, i.getTime());
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        FBdatabase.boardRef.addValueEventListener(postListener);
    }
}