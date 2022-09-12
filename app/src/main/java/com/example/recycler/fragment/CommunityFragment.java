package com.example.recycler.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.recycler.activities.ListViewActivity;
import com.example.recycler.adapters.ListViewAdapter;
import com.example.recycler.BearItem;
import com.example.recycler.R;
import com.example.recycler.databinding.BoardaddBinding;
import com.example.recycler.models.BoardModel;
import com.example.recycler.utilities.FBdatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommunityFragment extends Fragment {

    public CommunityFragment binding;
    public ListView listview;
    public ArrayList<BoardModel> boardDataList = new ArrayList<>();
    public BoardModel item;
    public ListViewAdapter adapter;
    public String TAG = ListViewActivity.class.getSimpleName();

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