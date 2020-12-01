package org.tensorflow.demo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.demo.R;
import org.tensorflow.demo.adapter.CookingInforAdapter;
import org.tensorflow.demo.adapter.RecipeListAdapter;
import org.tensorflow.demo.fragment.FragmentHome;
import org.tensorflow.demo.list.CookingInfor;
import org.tensorflow.demo.list.Recipe;
import org.tensorflow.demo.request.RecipeClickedRequest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CookingInforActivity extends AppCompatActivity {
    public static String userID;
    private String userName = TestActivity.userName;

    private ListView cookingInforListView;
    private Button writingButton;
    private CookingInforAdapter adapter;
    private List<CookingInfor> cookingInforList;
    private List<CookingInfor> saveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookinginfor);

        final TextView emptyText = (TextView) findViewById(R.id.empty);
        emptyText.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        final String user_name = intent.getStringExtra("userName");

       /* TextView userId = (TextView) findViewById(R.id.userId);
        String userId2 = user_id +"님";
        userId.setText(userId2);*/

        setCustomActionBar();

        writingButton = (Button) findViewById(R.id.writing);
        cookingInforListView = (ListView) findViewById(R.id.cookingInforListView);
        cookingInforList = new ArrayList<CookingInfor>();
        cookingInforListView.setEmptyView(emptyText);
        saveList = new ArrayList<CookingInfor>();
        adapter = new CookingInforAdapter(this, cookingInforList, saveList);//로 수정됨
        cookingInforListView.setAdapter(adapter);

        new BackgroundTask().execute();

        writingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CookingInforActivity.this, CookingWritingActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });


    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            target = "http://qwer456t.dothome.co.kr/CookInforBoardList.php";
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
                cookingInforList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                int board_id;
                String board_name;
                String board_writer;
                String board_content;

                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    board_id = object.getInt("board_id");
                    board_name = object.getString("board_name");
                    board_writer = object.getString("board_writer");
                    board_content = object.getString("board_content");

                    CookingInfor cookingInfor = new CookingInfor(board_id, board_name, board_writer, board_content);
                    cookingInforList.add(cookingInfor);
                    saveList.add(cookingInfor);
                    count++;
                }
                Comparator<CookingInfor> noDesc = new Comparator<CookingInfor>() {
                    @Override
                    public int compare(CookingInfor item1, CookingInfor item2) {
                        int ret = 0 ;

                        if (item1.getBoard_id() < item2.getBoard_id())
                            ret = 1 ;
                        else if (item1.getBoard_id() == item2.getBoard_id())
                            ret = 0 ;
                        else
                            ret = -1 ;

                        return ret ;

                        // 위의 코드를 간단히 만드는 방법.
                        // return (item2.getNo() - item1.getNo()) ;
                    }
                } ;

                Collections.sort(cookingInforList, noDesc) ;
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }

    public void searchBoardName(String search) {
        cookingInforList.clear();
        for (int i = 0; i < saveList.size(); i++) {
            if (saveList.get(i).getBoard_name().contains(search)) {//contains메소드로 search 값이 있으면 true를 반환함
                cookingInforList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();//어댑터에 값일 바뀐것을 알려줌
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
