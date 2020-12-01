package org.tensorflow.demo.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.demo.R;
import org.tensorflow.demo.list.Capture;
import org.tensorflow.demo.list.Mypage;
import org.tensorflow.demo.list.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kch on 2018. 2. 17..
 */

public class CaptureListAdapter extends BaseAdapter {

    private ArrayList<Capture> captureIngredientList = new ArrayList<Capture>();
    private Context context;

    public CaptureListAdapter() {
    }
/*
    public CaptureListAdapter(Context context, ArrayList<Capture> captureIngredientList ){
        this.context = context;
        this.captureIngredientList = captureIngredientList;
    }

 */

    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return captureIngredientList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int position) {
        return captureIngredientList.get(position);
    }

    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int position) {
        return position;
    }

    //가장 중요한 부분
    //int i 에서 final int i 로 바뀜 이유는 deleteButton.setOnClickListener에서 이 값을 참조하기 때문
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.capture, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.captureIngredientImage);
        TextView textTextView = (TextView) convertView.findViewById(R.id.captureIngredientText);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Capture captureIngredientItem = captureIngredientList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(captureIngredientItem.getIcon());
        textTextView.setText(captureIngredientItem.getText());

        return convertView;
    }

    public void addItem(Drawable icon, String text) {
        Capture item = new Capture();
        item.setIcon(icon);
        item.setText(text);
        captureIngredientList.add(item);
    }
}

