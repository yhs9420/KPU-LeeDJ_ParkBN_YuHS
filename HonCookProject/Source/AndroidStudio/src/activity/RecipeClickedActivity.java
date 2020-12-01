package org.tensorflow.demo.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.demo.R;
import org.tensorflow.demo.request.RecipeClickedRequest;

public class RecipeClickedActivity extends AppCompatActivity {
    private int recipeImage;
    public static String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipeclicked);

        Intent intent = getIntent();
        final String user_id = getIntent().getStringExtra("user_id");
        final String user_name = getIntent().getStringExtra("user_name");
        final int recipe_id = getIntent().getIntExtra("recipe_id", 0);

        TextView userName = (TextView) findViewById(R.id.userName);
        String userName2 = user_name +"님";
        userName.setText(userName2);

        //ImageView recipeImage
        //TextView recipe_id = (TextView) findViewById(R.id.recipe_id);
        TextView recipe_name = (TextView) findViewById(R.id.recipe_name);
        TextView recipe_category = (TextView) findViewById(R.id.recipe_category);
        TextView recipe_ingredient = (TextView) findViewById(R.id.recipe_ingredient);
        TextView recipe_content = (TextView) findViewById(R.id.recipe_content);

        //recipe_ingredient.setMovementMethod(new ScrollingMovementMethod());
        //recipe_content.setMovementMethod(new ScrollingMovementMethod());

        recipe_name.setText(intent.getStringExtra("recipe_name"));
        recipe_category.setText(intent.getStringExtra("recipe_category"));
        recipe_ingredient.setText(intent.getStringExtra("recipe_ingredient"));
        recipe_content.setText(intent.getStringExtra("recipe_content"));

        ImageView recipeImage = (ImageView) findViewById((R.id.recipeImage));
        String rCategory = recipe_category.getText().toString();

        if ("한식".equals(rCategory))
            recipeImage.setImageResource(R.drawable.korean);
        else if ("중식".equals(rCategory))
            recipeImage.setImageResource(R.drawable.chinese);
        else if ("일식".equals(rCategory))
            recipeImage.setImageResource(R.drawable.japanese);
        else if ("양식".equals(rCategory))
            recipeImage.setImageResource(R.drawable.western);

        setCustomActionBar();

        Button addRecipeButton = (Button) findViewById(R.id.myRecipeListAdd);

        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = user_id;
                String userName = user_name;
                int recipeID = recipe_id;

                if (userID != null) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {

                        //서버로부터 여기서 데이터를 받음
                        @Override
                        public void onResponse(String response) {
                            try {
                                //서버로부터 받는 데이터는 JSON타입의 객체이다.
                                JSONObject jsonResponse = new JSONObject(response);
                                //그중 Key값이 "success"인 것을 가져온다.
                                boolean success = jsonResponse.getBoolean("success");

                                //회원 가입 성공시 success값이 true임
                                if (success) {
                                    //알림상자를 만들어서 보여줌
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipeClickedActivity.this);
                                    builder.setMessage("내 레시피 목록에 추가되었습니다.")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //그리고 첫화면으로 돌아감
                                                    // Intent intent = new Intent(RecipeClickedActivity.this, LoginActivity.class);
                                                    // RecipeClickedActivity.this.startActivity(intent);
                                                }
                                            })
                                            .setCancelable(false)
                                            .create()
                                            .show();

                                }
                                //회원 가입 실패시 success값이 false임
                                else {
                                    //알림상자를 만들어서 보여줌
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipeClickedActivity.this);
                                    builder.setMessage("내 레시피 목록에 추가할 수 없습니다.")
                                            .setNegativeButton("확인", null)
                                            .setCancelable(false)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };//responseListener 끝
                    RecipeClickedRequest recipeClickedRequest = new RecipeClickedRequest(userID, recipeID, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RecipeClickedActivity.this);
                    queue.add(recipeClickedRequest);
                }
            }
        });

    }

    private void setCustomActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_recipe);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        //actionBar.setHomeAsUpIndicator(R.drawable.button_back); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
