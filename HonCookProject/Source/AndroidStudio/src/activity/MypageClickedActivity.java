package org.tensorflow.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.demo.R;

public class MypageClickedActivity extends AppCompatActivity {
    private int recipeImage;
    public static String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mypageclicked);

        Intent intent = getIntent();
        final String user_name = intent.getStringExtra("userName");

        TextView userName = (TextView) findViewById(R.id.userName);
        String userName2 = user_name +"님";
        userName.setText(userName2);

        //final String user_id = getIntent().getStringExtra("user_id");
        //final int recipe_id = getIntent().getIntExtra("recipe_id",0);

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
    }

    private void setCustomActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_mypage);
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