package com.example.recycler.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycler.Activity.ListViewActivity;
import com.example.recycler.BearItem;
import com.example.recycler.R;

import java.util.ArrayList;

/* 리스트뷰 어댑터 */
public class ListViewAdapter extends BaseAdapter {
    private String TAG = ListViewActivity.class.getSimpleName();
    ArrayList<BearItem> items = new ArrayList<>();

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(BearItem item) {
        items.add(item);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final BearItem bearItem = items.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_list_item, viewGroup, false);

        } else {
            View view = new View(context);
            view = (View) convertView;
        }

        TextView tv_num = (TextView) convertView.findViewById(R.id.tv_num);
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
        TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
//        TextView tv_likeit = (TextView) convertView.findViewById(R.id.tv_likeit);

        tv_num.setText(bearItem.getNum());
        tv_name.setText(bearItem.getName());
        iv_icon.setImageResource(bearItem.getResId());
        tv_content.setText(bearItem.getContent());
//        tv_likeit.setText(bearItem.getLikeit());

        Log.d(TAG, "getView() - [ "+position+" ] "+bearItem.getName());

        //각 아이템 선택 event
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,
                        bearItem.getNum()+" 번 - "+bearItem.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;  //뷰 객체 반환
    }
}