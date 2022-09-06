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

    public HomeFragment homeFragment;
    public MypageFragment mypageFragment;
    public StoreFragment storeFragment;
    public CommunityFragment communityFragment;
    public ChattingFragment chattingFragment;
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

//        listview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("click ########################################################");
//                Intent intent = new Intent(ListViewActivity.this, BoardInsideActivity.class);
//                intent.putExtra("title", boardDataList.get(i).getTitle());
//                intent.putExtra("content", boardDataList.get(i).getContent());
//                intent.putExtra("time", boardDataList.get(i).getTime());
//                startActivity(intent);
//            }
//        });

        // insider board 로 이동



        // 하단바 설정 시작
        homeFragment = new HomeFragment();
        mypageFragment = new MypageFragment();
        storeFragment = new StoreFragment();
        communityFragment = new CommunityFragment();
        chattingFragment = new ChattingFragment();

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationview);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent1 = new Intent(ListViewActivity.this, MainActivity.class);
                        startActivity(intent1);
                    case R.id.store:
//                        Intent intent2 = new Intent(ListViewActivity.this, MainActivity.class);
//                        startActivity(intent2);
//                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, storeFragment).commit();
                    case R.id.community:
                        Intent intent3 = new Intent(ListViewActivity.this, ListViewActivity.class);
                        startActivity(intent3);
//                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, communityFragment).commit();
                    case R.id.chatting:
//                        Intent intent4 = new Intent(ListViewActivity.this, MainActivity.class);
//                        startActivity(intent4);
                    case R.id.mypage:
//                        Intent intent5 = new Intent(ListViewActivity.this, MainActivity.class);
//                        startActivity(intent5);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                return false;
            }
        });


        //Adapter 안에 아이템의 정보 담기
//        adapter.addItem(new BearItem("1", "1번 제목", R.drawable.ic_launcher_background,"1번 내용",1));
//        adapter.addItem(new BearItem("2", "2번 제목", R.drawable.ic_launcher_foreground,"2번 내용",2));
//        adapter.addItem(new BearItem("3", "3번 제목", R.drawable.ic_settings,"3번 내용",3));
//        adapter.addItem(new BearItem("4", "제목", R.drawable.ic_settings,"4번 내용",4));
//        adapter.addItem(new BearItem("5", "제목", R.drawable.ic_settings,"1번 내용",1));
//        adapter.addItem(new BearItem("6", "제목", R.drawable.ic_settings,"1번 내용",1));
//        adapter.addItem(new BearItem("7", "제목", R.drawable.ic_settings,"1번 내용",1));
//        adapter.addItem(boardDataList);
        // 하단바 설정 끝



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

        Constants.boardRef.addValueEventListener(postListener);
    }
}