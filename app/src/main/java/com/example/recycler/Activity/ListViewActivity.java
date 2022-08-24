package com.example.recycler.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ListView;

import com.example.recycler.Adapter.ListViewAdapter;
import com.example.recycler.BearItem;
import com.example.recycler.R;

public class ListViewActivity extends AppCompatActivity {

    private ListView listview = null;
    private ListViewAdapter adapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_community);

        listview = (ListView) findViewById(R.id.listview);
        adapter = new ListViewAdapter();

        //Adapter 안에 아이템의 정보 담기
        adapter.addItem(new BearItem("1", "1번 제목", R.drawable.ic_launcher_background,"1번 내용",1));
        adapter.addItem(new BearItem("2", "2번 제목", R.drawable.ic_launcher_foreground,"2번 내용",2));
        adapter.addItem(new BearItem("3", "3번 제목", R.drawable.ic_settings,"3번 내용",3));
        adapter.addItem(new BearItem("4", "제목", R.drawable.ic_settings,"4번 내용",4));
        adapter.addItem(new BearItem("5", "제목", R.drawable.ic_settings,"1번 내용",1));
        adapter.addItem(new BearItem("6", "제목", R.drawable.ic_settings,"1번 내용",1));
        adapter.addItem(new BearItem("7", "제목", R.drawable.ic_settings,"1번 내용",1));
        adapter.addItem(new BearItem("8", "제목", R.drawable.ic_settings,"1번 내용",1));

        //리스트뷰에 Adapter 설정
        listview.setAdapter(adapter);
    }




}