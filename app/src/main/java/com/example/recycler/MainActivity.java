package com.example.recycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.recycler.activities.BoardAddActivity;
import com.example.recycler.activities.ChatMainActivity;
import com.example.recycler.activities.ListViewActivity;
import com.example.recycler.adapters.SearchAdapter;
import com.example.recycler.fragment.ChattingFragment;
import com.example.recycler.fragment.CommunityFragment;
import com.example.recycler.fragment.HomeFragment;
import com.example.recycler.fragment.MypageFragment;
import com.example.recycler.fragment.StoreFragment;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // 상단바 관련 변수
    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;
    private Button searchButton; // 검색 버튼

    // 하단 nav bar 관련 변수
    private HomeFragment homeFragment;
    private  MypageFragment mypageFragment;
    private StoreFragment storeFragment;
    private CommunityFragment communityFragment;
    private ChattingFragment chattingFragment;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 하단바 소스 코드
        homeFragment = new HomeFragment();
        mypageFragment = new MypageFragment();
        storeFragment = new StoreFragment();
        communityFragment = new CommunityFragment();
        chattingFragment = new ChattingFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.containers, homeFragment).commit();

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationview);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        transaction.replace(R.id.containers, homeFragment).commit();
                    case R.id.store:
                        transaction.replace(R.id.containers, storeFragment).commit();
                    case R.id.community:
//                        transaction.replace(R.id.containers, communityFragment).commit();
                        Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
                        startActivity(intent);
                    case R.id.chatting:
                        Intent intent2 = new Intent(MainActivity.this, ChatMainActivity.class);
                        startActivity(intent2);
//                        transaction.replace(R.id.containers, chattingFragment).commit();
                    case R.id.mypage:
//                        transaction.replace(R.id.containers, mypageFragment).commit();
                }

                return false;
            }
        });

        // 하단바 끝

        // 게시글 추가 버튼 시작
        Button button = (Button) findViewById(R.id.boardadd);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardAddActivity.class);
                startActivity(intent);
            }
        });

        // 게시글 추가 버튼 끝


        // 상단바 시작
        editSearch = ((EditText) findViewById(R.id.editSearch));
//        listView = ((ListView) findViewById(R.id.searchList));

        // 리스트를 생성한다.
        list = new ArrayList<>();

        // 검색에 사용할 데이터을 미리 저장한다.
//        settingList();

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<>();
        arraylist.addAll(list);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list, this);

        // 리스트뷰에 아답터를 연결한다.
//        listView.setAdapter(adapter);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 입력하기전 이벤트
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 입력란에 변화가 있을 때 이벤트
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 입력이 끝났을 때 이벤트
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });

    }

    public void search(String charText) { // 상단바 검색 함

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    private void settingList(){ // 검색 상단바 데이터 초기화 함수
        list.add("채수빈");
        list.add("박지현");
        list.add("수지");
        list.add("남태현");
        list.add("하성운");
        list.add("크리스탈");
        list.add("강승윤");
        list.add("손나은");
        list.add("남주혁");
        list.add("루이");
        list.add("진영");
        list.add("슬기");
        list.add("이해인");
        list.add("고원희");
        list.add("설리");
        list.add("공명");
        list.add("김예림");
        list.add("혜리");
        list.add("웬디");
        list.add("박혜수");
        list.add("카이");
        list.add("진세연");
        list.add("동호");
        list.add("박세완");
        list.add("도희");
        list.add("창모");
        list.add("허영지");
    }

}