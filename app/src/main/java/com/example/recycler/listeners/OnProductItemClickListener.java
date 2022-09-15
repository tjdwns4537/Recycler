package com.example.recycler.listeners;

import android.view.View;

import com.example.recycler.adapters.ProductAdapter;

public interface OnProductItemClickListener {
    public void onItemClick(ProductAdapter.ViewHolder holder, View view, int position);
}
