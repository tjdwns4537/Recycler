package com.example.recycler.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycler.activities.ListViewActivity;
import com.example.recycler.BearItem;
import com.example.recycler.R;
import com.example.recycler.models.BoardModel;

import java.util.ArrayList;

/* 리스트뷰 어댑터 */
public class ListViewAdapter extends BaseAdapter {
    public String TAG = ListViewActivity.class.getSimpleName();
    public ArrayList<BoardModel> items = new ArrayList<>();
    BoardModel boardModel;

    public ListViewAdapter(ArrayList<BoardModel> boardDataList) {
        items = boardDataList;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(ArrayList<BoardModel> item) {
        this.items = item;
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
        final BoardModel boardModel = items.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_list_item, viewGroup, false);

        } else {
            View view = new View(context);
            view = (View) convertView;
        }

        TextView tv_uid = (TextView) convertView.findViewById(R.id.tv_uid);
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);

        //        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
        //        TextView tv_likeit = (TextView) convertView.findViewById(R.id.tv_likeit);


        tv_uid.setText(boardModel.getUid());
        tv_name.setText(boardModel.getTitle());
        tv_content.setText(boardModel.getContent());
        tv_time.setText(boardModel.getTime());

        //각 아이템 선택 event
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context,
//                        BoardModel.getTitle(),
//                        Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;  //뷰 객체 반환
    }
}