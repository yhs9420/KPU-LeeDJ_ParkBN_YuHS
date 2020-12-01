package org.tensorflow.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.tensorflow.demo.R;
import org.tensorflow.demo.activity.CookingInforBoardActivity;
import org.tensorflow.demo.activity.TestActivity;
import org.tensorflow.demo.list.CookingInfor;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by kch on 2018. 2. 17..
 */

public class CookingInforAdapter extends BaseAdapter {

    private String userName = TestActivity.userName;
    private Context context;
    private List<CookingInfor> cookingInforList;
    private List<CookingInfor> saveList;

    public CookingInforAdapter(Context context, List<CookingInfor> cookingInforList, List<CookingInfor> saveList) {
        this.context = context;
        this.cookingInforList = cookingInforList;
        this.saveList = saveList;

    }


    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return cookingInforList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int i) {
        return cookingInforList.get(i);
    }

    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int i) {
        return i;
    }

    //가장 중요한 부분
    //int i 에서 final int i 로 바뀜 이유는 deleteButton.setOnClickListener에서 이 값을 참조하기 때문
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.cookinginfor, null);

        final TextView board_name = (TextView) v.findViewById(R.id.board_name);
        TextView board_id = (TextView) v.findViewById(R.id.board_id);
        TextView board_writer = (TextView) v.findViewById(R.id.board_writer);

        board_name.setText(cookingInforList.get(i).getBoard_name());
        board_id.setText(String.valueOf(cookingInforList.get(i).getBoard_id()));
        board_writer.setText(cookingInforList.get(i).getBoard_writer());

        TextView boardComments_Count = (TextView) v.findViewById(R.id.boardCommentsCount);
        //boardComments_Count.setText(String.valueOf(boardCommentsCount));

        final View cookingInforListBody = v.findViewById(R.id.cookingInforListBody);

        cookingInforListBody.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CookingInforBoardActivity.class);
                intent.putExtra("userName", userName);
                intent.putExtra("board_writer", cookingInforList.get(i).getBoard_writer());
                intent.putExtra("board_id", cookingInforList.get(i).getBoard_id());
                intent.putExtra("board_name", cookingInforList.get(i).getBoard_name());
                intent.putExtra("board_content", cookingInforList.get(i).getBoard_content());
                context.startActivity(intent);
            }
        });

        v.setTag(cookingInforList.get(i).getBoard_name());
        return v;
    }

}

