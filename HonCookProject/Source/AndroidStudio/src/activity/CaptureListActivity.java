package org.tensorflow.demo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tensorflow.demo.R;
import org.tensorflow.demo.adapter.CaptureListAdapter;
import org.tensorflow.demo.adapter.RecipeListAdapter;
import org.tensorflow.demo.fragment.FragmentMyPage;
import org.tensorflow.demo.list.Recipe;
import org.tensorflow.demo.list.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CaptureListActivity extends AppCompatActivity {
    private AlertDialog dialog;
    private Button recommendButton;
    private CaptureListAdapter adapter;
    private List label = new ArrayList<>();

    private String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capturelist);

        setCustomActionBar();
        final ListView captureListView;

        // Adapter 생성
        adapter = new CaptureListAdapter();
        final ArrayList<String> items = new ArrayList<String>();

        // 리스트뷰 참조 및 Adapter달기
        captureListView = (ListView) findViewById(R.id.captureListView);
        captureListView.setAdapter(adapter);
        final TextView emptyText = (TextView) findViewById(R.id.empty);
        captureListView.setEmptyView(emptyText);
        recommendButton = findViewById(R.id.recommendButton);

        new BackgroundTask().execute();


        recommendButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                SparseBooleanArray checkedItems = captureListView.getCheckedItemPositions();
                int count = adapter.getCount();
                final ArrayList<String> selectedItems = new ArrayList<String>();

                for (int i = count - 1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        selectedItems.add(label.get(i).toString());
                    }
                }

                if (selectedItems.size() == 0) {
                    android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(CaptureListActivity.this);
                    dialog = builder.setMessage("선택된 식재료가 없습니다.")
                            .setCancelable(false)
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                } else {
                    String items = "";
                    for (String selectedItem : selectedItems) {
                        items += (selectedItem + ", ");
                    }
                    items = items.substring(0, items.length() - 2);
                    Intent intent = new Intent(getApplicationContext(), RecommendActivity.class);
                    intent.putExtra("recipe_ingredient", items);
                    intent.putExtra("userName", user_name);
                    startActivity(intent);
                    checkedItems.clear();

                    items = items.substring(0, items.length() - 2);
                }

                // 모든 선택 상태 초기화.
                captureListView.clearChoices();

                adapter.notifyDataSetChanged();
            }
        });


    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                user_name = getIntent().getStringExtra("userName");
                TextView userName = (TextView) findViewById(R.id.userName);
                String userName2 = user_name + "님";
                userName.setText(userName2);

                //intent로 값을 가져옵니다 이때 JSONObject타입으로 가져옵니다
                label = (List) getIntent().getSerializableExtra("captureIngredient");
                HashSet arr2 = new HashSet(label);
                label = new ArrayList(arr2);
                for (int i = 0; i < label.size(); i++) {
                    if (label.get(i).toString().equals("apple")) {
                        label.set(i, "사과");
                        adapter.addItem(ContextCompat.getDrawable(CaptureListActivity.this, R.drawable.apple), label.get(i).toString());
                    } else if (label.get(i).toString().equals("banana")) {
                        label.set(i, "바나나");
                        adapter.addItem(ContextCompat.getDrawable(CaptureListActivity.this, R.drawable.banana), label.get(i).toString());
                    } else if (label.get(i).toString().equals("orange")) {
                        label.set(i, "오렌지");
                        adapter.addItem(ContextCompat.getDrawable(CaptureListActivity.this, R.drawable.orange), label.get(i).toString());
                    } else if (label.get(i).toString().equals("broccoli")) {
                        label.set(i, "브루콜리");
                        adapter.addItem(ContextCompat.getDrawable(CaptureListActivity.this, R.drawable.broccoli), label.get(i).toString());
                    } else if (label.get(i).toString().equals("carrot")) {
                        label.set(i, "당근");
                        adapter.addItem(ContextCompat.getDrawable(CaptureListActivity.this, R.drawable.carrot), label.get(i).toString());
                    } else {
                        adapter.addItem(ContextCompat.getDrawable(CaptureListActivity.this, R.drawable.homeicon), label.get(i).toString());
                    }
                }

                adapter.notifyDataSetChanged();


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

                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


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
