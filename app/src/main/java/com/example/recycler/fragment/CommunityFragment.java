package com.example.recycler.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.recycler.adapters.ListViewAdapter;
import com.example.recycler.BearItem;
import com.example.recycler.R;

public class CommunityFragment extends Fragment {

    /**
     * 현재 프래그먼트 형태가 아닌 액티비티로 동작 중이므로 프래그먼트는 필요 없음
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ListView listview = getActivity().findViewById(R.id.listview);
        ListViewAdapter adapter = new ListViewAdapter();

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

        return inflater.inflate(R.layout.fragment_community, container, false);
    }


}