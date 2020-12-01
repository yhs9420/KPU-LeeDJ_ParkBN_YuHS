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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.tensorflow.demo.R;
import org.tensorflow.demo.activity.MypageClickedActivity;
import org.tensorflow.demo.activity.TestActivity;
import org.tensorflow.demo.list.Mypage;
import org.tensorflow.demo.request.MypageDeleteRequest;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * Created by kch on 2018. 2. 17..
 */

public class MypageListAdapter extends BaseAdapter {

    private String userID = TestActivity.userID;
    private String userName = TestActivity.userName;
    private Context context;
    private Activity activity;
    private List<Mypage> recipeList;
    private List<Mypage> saveList;
    private int re;

    public MypageListAdapter(Context context, Activity activity, List<Mypage> recipeList, List<Mypage> saveList) {
        this.context = context;
        this.activity = activity;
        this.recipeList = recipeList;
        this.saveList = saveList;

    }

    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return recipeList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int i) {
        return recipeList.get(i);
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
        View v = View.inflate(context, R.layout.mypage, null);
        //뷰에 다음 컴포넌트들을 연결시켜줌
        //final추가 안붙이면 에러남 리스너로 전달하고 싶은 지역변수는 final로 처리해야됨


        TextView recipe_name = (TextView) v.findViewById(R.id.recipe_name);
        TextView recipe_category = (TextView) v.findViewById(R.id.recipe_category);
        ImageView recipeImage = (ImageView) v.findViewById((R.id.recipeImage));

        //  userID.setText(recipeList.get(i).getUserID());
        // userAge.setText(recipeList.get(i).getUserAge());
        recipe_name.setText(recipeList.get(i).getRecipe_name());
        recipe_category.setText(recipeList.get(i).getRecipe_category());
        //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임

        v.setTag(recipeList.get(i).getUserID());

        String rCategory = recipe_category.getText().toString();

        if ("한식".equals(rCategory))
            recipeImage.setImageResource(R.drawable.korean);
        else if ("중식".equals(rCategory))
            recipeImage.setImageResource(R.drawable.chinese);
        else if ("일식".equals(rCategory))
            recipeImage.setImageResource(R.drawable.japanese);
        else if ("양식".equals(rCategory))
            recipeImage.setImageResource(R.drawable.western);
        //만든뷰를 반환함

        final View mypageListBodyView = v.findViewById(R.id.mypageListBody);

        mypageListBodyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MypageClickedActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userID", userID);
                intent.putExtra("userName", userName);
                intent.putExtra("recipe_id", recipeList.get(i).getRecipe_id());
                intent.putExtra("recipe_name", recipeList.get(i).getRecipe_name());
                intent.putExtra("recipe_category", recipeList.get(i).getRecipe_category());
                intent.putExtra("recipe_ingredient", recipeList.get(i).getRecipe_ingredient());
                intent.putExtra("recipe_content", recipeList.get(i).getRecipe_content());
                context.startActivity(intent);

            }
        });

        Button deleteButton = (Button) v.findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //4. 콜백 처리부분(volley 사용을 위한 ResponseListener 구현 부분)
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setMessage("내 레시피에서 정말 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");

                                            //받아온 값이 success면 정상적으로 서버로부터 값을 받은 것을 의미함
                                            if (success) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                                AlertDialog dialog = builder
                                                        .setMessage("레시피가 삭제되었습니다.")
                                                        .setCancelable(false)
                                                        .setPositiveButton("확인", null)
                                                        .create();
                                                dialog.show();
                                                recipeList.remove(i);
                                                notifyDataSetChanged();//데이터가 변경된 것을 어댑터에 알려줌
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                                AlertDialog dialog = builder.setMessage("레시피 삭제를 실패하였습니다.")
                                                        .setCancelable(false)
                                                        .setPositiveButton("확인", null)
                                                        .create();
                                                dialog.show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                //volley 사용법
                                //1. RequestObject를 생성한다. 이때 서버로부터 데이터를 받을 responseListener를 반드시 넘겨준다.
                                //위에서 userID를 final로 선언해서 아래 처럼 가능함
                                MypageDeleteRequest mypageDeleteRequest = new MypageDeleteRequest(userID, recipeList.get(i).getRecipe_id(), responseListener);
                                //2. RequestQueue를 생성한다.
                                //여기서 UserListAdapter는 Activity에서 상속받은 클래스가 아니므로 Activity값을 생성자로 받아서 사용한다
                                RequestQueue queue = Volley.newRequestQueue(context);
                                //3. RequestQueue에 RequestObject를 넘겨준다.
                                queue.add(mypageDeleteRequest);
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                //Toast.makeText(activity, "취소", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }//onclick
        });

        return v;
    }

}

