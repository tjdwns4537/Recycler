package com.example.recycler.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.recycler.R;
import com.example.recycler.databinding.ActivityMainBinding;
import com.example.recycler.fragment.ChatFragment;
import com.example.recycler.fragment.CommunityFragment;
import com.example.recycler.fragment.HomeFragment;
import com.example.recycler.fragment.MypageFragment;
import com.example.recycler.fragment.StoreFragment;

public class MainActivity extends AppCompatActivity {

    // 프래그먼트 객체 선언
    private HomeFragment homeFragment;
    private  MypageFragment mypageFragment;
    private StoreFragment storeFragment;
    private CommunityFragment communityFragment;
    private ChatFragment chatFragment;

    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 프래그먼트 초기화
        homeFragment = HomeFragment.newInstance();
        storeFragment = StoreFragment.newInstance();
        chatFragment = ChatFragment.newInstance();
        communityFragment = CommunityFragment.newInstance();
        mypageFragment = MypageFragment.newInstance();

        // 프래그먼트 트랜잭션 초기화
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(binding.container.getId(), homeFragment).commit();
        setListeners();

    }

    private void setListeners(){
        // 바텀내비 아이템 클릭 리스너
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            fragmentTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.home:
                    fragmentTransaction.replace(binding.container.getId(), homeFragment).commit();
                    break;
                case R.id.store:
                    fragmentTransaction.replace(binding.container.getId(), storeFragment).commit();
                    break;
                case R.id.community:
                    fragmentTransaction.replace(binding.container.getId(), communityFragment).commit();
                    break;
                case R.id.chat:
                    fragmentTransaction.replace(binding.container.getId(), chatFragment).commit();
                    break;
                case R.id.mypage:
                    fragmentTransaction.replace(binding.container.getId(), mypageFragment).commit();
                    break;
            }
            return false;
        });

        // 툴바 아이템 클릭 리스너
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.bookmark:
                    showToast("bookmark 클릭");
                    break;
                case R.id.cart:
                    showToast("cart 클릭");
                    break;
            }
            return false;
        });
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}