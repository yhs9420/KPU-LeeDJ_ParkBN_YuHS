package org.tensorflow.demo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.demo.R;
import org.tensorflow.demo.activity.CookingInforBoardActivity;
import org.tensorflow.demo.activity.TestActivity;
import org.tensorflow.demo.list.CookingInfor;
import org.tensorflow.demo.list.CookingInforComments;
import org.tensorflow.demo.request.CookingInforBoardRequest;

import java.util.List;

/**
 * Created by kch on 2018. 2. 17..
 */

public class CookingInforCommentsAdapter extends BaseAdapter {

    private String userName = TestActivity.userName;
    private String user_name = CookingInforBoardActivity.user_name;
    private Context context;
    private Activity activity;
    private List<CookingInforComments> cookingInforCommentsList;
    private List<CookingInforComments> saveList;


    public CookingInforCommentsAdapter(Context context, Activity activity, List<CookingInforComments> cookingInforCommentsList, List<CookingInforComments> saveList) {
        this.context = context;
        this.activity = activity;
        this.cookingInforCommentsList = cookingInforCommentsList;
        this.saveList = saveList;

    }


    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return cookingInforCommentsList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int i) {
        return cookingInforCommentsList.get(i);
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
        View v = View.inflate(context, R.layout.cookinginforcomments, null);

        final TextView comments_writer = (TextView) v.findViewById(R.id.comments_writer);
        TextView comments_content = (TextView) v.findViewById(R.id.comments_content);

        comments_writer.setText(cookingInforCommentsList.get(i).getUser_name());
        comments_content.setText(cookingInforCommentsList.get(i).getComments_content());

        final View cookingInforCommentsListBody = v.findViewById(R.id.cookingInforCommentsListBody);

        /*
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
        });*/



        v.setTag(cookingInforCommentsList.get(i).getUser_name());
        return v;
    }

}

