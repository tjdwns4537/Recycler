package com.example.recycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.recycler.Fragment.ChattingFragment;
import com.example.recycler.Fragment.CommunityFragment;
import com.example.recycler.Fragment.HomeFragment;
import com.example.recycler.Fragment.MypageFragment;
import com.example.recycler.Fragment.StoreFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    MypageFragment mypageFragment;
    StoreFragment storeFragment;
    CommunityFragment communityFragment;
    ChattingFragment chattingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        mypageFragment = new MypageFragment();
        storeFragment = new StoreFragment();
        communityFragment = new CommunityFragment();
        chattingFragment = new ChattingFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationview);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
                    case R.id.store:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, storeFragment).commit();
                    case R.id.community:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, communityFragment).commit();
                    case R.id.chatting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, chattingFragment).commit();
                    case R.id.mypage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, mypageFragment).commit();
                }

                return false;
            }
        });
    }
}