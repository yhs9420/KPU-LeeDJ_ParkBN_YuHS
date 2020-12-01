package org.tensorflow.demo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tensorflow.demo.R;
import org.tensorflow.demo.adapter.RecipeListAdapter;
import org.tensorflow.demo.list.Recipe;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecommendActivity extends AppCompatActivity {
    //private ListView recommendListView;
    private GridView recommendGridView;
    private RecipeListAdapter adapter;
    private List<Recipe> recipeList;
    private List<Recipe> saveList;
    private String recommendText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recommend);
        final TextView emptyText = (TextView) findViewById(R.id.empty);
        emptyText.setVisibility(View.INVISIBLE);
        final TextView captureIngredient = (TextView) findViewById(R.id.captureIngredient);

        Intent intent = getIntent();
        recommendText = (intent.getStringExtra("recipe_ingredient"));
        captureIngredient.setText(recommendText + " 관련 레시피 추천");

        final String user_name = intent.getStringExtra("userName");

        TextView userName = (TextView) findViewById(R.id.userName);
        String userName2 = user_name +"님";
        userName.setText(userName2);

        recipeList = new ArrayList<Recipe>();
        saveList = new ArrayList<Recipe>();
        recommendGridView = (GridView) findViewById(R.id.recommendGridView);
        recommendGridView.setEmptyView(emptyText);
        adapter = new RecipeListAdapter(getApplicationContext(), recipeList, saveList);//로 수정됨
        recommendGridView.setAdapter(adapter);

        adapter.notifyDataSetChanged();


        setCustomActionBar();
        new BackgroundTask().execute();

        recommendGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = getIntent();
                //String userID = intent.getStringExtra("userID");
                Intent intent = new Intent(getApplicationContext(), RecommendClickedActivity.class);
                intent.putExtra("userName", user_name);
                intent.putExtra("recipe_id", recipeList.get(position).getRecipe_id());
                intent.putExtra("recipe_name", recipeList.get(position).getRecipe_name());
                intent.putExtra("recipe_category", recipeList.get(position).getRecipe_category());
                intent.putExtra("recipe_ingredient", recipeList.get(position).getRecipe_ingredient());
                intent.putExtra("recipe_content", recipeList.get(position).getRecipe_content());
                startActivity(intent);
            }
        });


    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            target = "http://qwer456t.dothome.co.kr/RecipeList.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(target);//URL 객체 생성

                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                //바이트단위 입력스트림 생성 소스는 httpURLConnection
                InputStream inputStream = httpURLConnection.getInputStream();

                //웹페이지 출력물을 버퍼로 받음 버퍼로 하면 속도가 더 빨라짐
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;

                //문자열 처리를 더 빠르게 하기 위해 StringBuilder클래스를 사용함
                StringBuilder stringBuilder = new StringBuilder();

                //한줄씩 읽어서 stringBuilder에 저장함
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
                }

                //사용했던 것도 다 닫아줌
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                recipeList.clear();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                int recipe_id;
                String recipe_name;
                String recipe_category;
                String recipe_ingredient;
                String recipe_content;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    recipe_id = object.getInt("recipe_id");
                    recipe_name = object.getString("recipe_name");
                    recipe_category = object.getString("recipe_category");
                    recipe_ingredient = object.getString("recipe_ingredient");
                    recipe_content = object.getString("recipe_content");
                    Recipe recipe = new Recipe(recipe_id, recipe_name, recipe_category, recipe_ingredient, recipe_content);
                    recipeList.add(recipe);
                    saveList.add(recipe);
                    count++;
                }
                if (count == 0) {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecommendActivity.this);
                    dialog = builder.setMessage("레시피가 존재하지 않습니다.")
                            .setCancelable(false)
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                }

                String rei = recommendText;
                String[] result2;
                result2 = rei.split(", ");
                searchRecipe(result2[0]);
                for (int i = 1; i < result2.length; i++) {
                    searchRecipe2(result2[i]);
                }
                recommendGridView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }

    public void searchRecipe(String search) {
        recipeList.clear();
        for (int i = 0; i < saveList.size(); i++) {
            if (saveList.get(i).getRecipe_ingredient().contains(search)) {//contains메소드로 search 값이 있으면 true를 반환함
                recipeList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();//어댑터에 값일 바뀐것을 알려줌

    }

    public void searchRecipe2(String search) {
        for (int j = 0; j < saveList.size(); j++) {
            if (saveList.get(j).getRecipe_ingredient().contains(search)) {//contains메소드로 search 값이 있으면 true를 반환함
                recipeList.add(saveList.get(j));
            }
        }
        adapter.notifyDataSetChanged();//어댑터에 값일 바뀐것을 알려줌

    }

    private void setCustomActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
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
