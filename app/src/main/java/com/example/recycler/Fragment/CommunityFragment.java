package com.example.recycler.Fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.recycler.Activity.ListViewActivity;
import com.example.recycler.Adapter.ListViewAdapter;
import com.example.recycler.Adapter.SearchAdapter;
import com.example.recycler.BearItem;
import com.example.recycler.MainActivity;
import com.example.recycler.R;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {

    /**
     * 현재 프래그먼트 형태가 아닌 액티비티로 동작 중이므로 프래그먼트는 필요 없음
     */
//    private ListView listview = null;
//    private ListViewAdapter adapter = new ListViewAdapter();
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        listview = (ListView) getActivity().findViewById(R.id.listview);
//
//        //Adapter 안에 아이템의 정보 담기
//        adapter.addItem(new BearItem("1", "파랑이", R.drawable.ic_settings));
//        adapter.addItem(new BearItem("2", "민트트", R.drawable.ic_settings));
//        adapter.addItem(new BearItem("3", "하늘이", R.drawable.ic_settings));
//        adapter.addItem(new BearItem("4", "하양이", R.drawable.ic_settings));
//        adapter.addItem(new BearItem("5", "분홍이", R.drawable.ic_settings));
//        adapter.addItem(new BearItem("6", "노랑이", R.drawable.ic_settings));
//        adapter.addItem(new BearItem("7", "보라라", R.drawable.ic_settings));
//        adapter.addItem(new BearItem("8", "믹스스", R.drawable.ic_settings));
//
//        //리스트뷰에 Adapter 설정
//        listview.setAdapter(adapter);
//
//        return inflater.inflate(R.layout.fragment_community, container, false);
//    }


}