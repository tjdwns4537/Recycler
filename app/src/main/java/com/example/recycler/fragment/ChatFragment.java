package com.example.recycler.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recycler.R;
import com.example.recycler.activities.BoardAddActivity;
import com.example.recycler.activities.ChatMainActivity;
import com.example.recycler.activities.MainActivity;

public class ChatFragment extends Fragment {

    public static ChatFragment newInstance(){
        return new ChatFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Intent intent = new Intent(getContext(), ChatMainActivity.class);
        startActivity(intent);
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }
}