package org.tensorflow.demo.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tensorflow.demo.R;
import org.tensorflow.demo.activity.RecipeClickedActivity;
import org.tensorflow.demo.activity.TestActivity;
import org.tensorflow.demo.adapter.RecipeListAdapter;
import org.tensorflow.demo.list.Recipe;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FragmentHome extends Fragment {

    //private ListView recipeListView;
    private GridView recipeGridView;
    private RecipeListAdapter adapter;
    private List<Recipe> recipeList;
    private List<Recipe> saveList;

    private EditText search;
    private ImageView recipeImage;
    private TextView recipeCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_home, container, false);
        final View v = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView emptyText = (TextView) v.findViewById(R.id.empty);
        emptyText.setVisibility(View.INVISIBLE);
        //처음 childfragment 지정
        // getFragmentManager().beginTransaction().add(R.id.fragment_child, new Fragment_child_home0()).commit();

        //recipeListView = (ListView) v.findViewById(R.id.recipeListView);
        recipeGridView = (GridView) v.findViewById(R.id.recipeGridView);
        recipeList = new ArrayList<Recipe>();
        saveList = new ArrayList<Recipe>();
        adapter = new RecipeListAdapter(getActivity().getApplicationContext(), recipeList, saveList);//로 수정됨
        recipeGridView.setAdapter(adapter);

        recipeGridView.setEmptyView(emptyText);
        recipeImage = (ImageView) v.findViewById(R.id.recipeImage);

        //하위버튼 밑줄
        final View allRecipeDivider = (View) v.findViewById(R.id.allRecipeDivider);
        final View koreanRecipeDivider = (View) v.findViewById(R.id.koreanRecipeDivider);
        final View japaneseRecipeDivider = (View) v.findViewById(R.id.japaneseRecipeDivider);
        final View chineseRecipeDivider = (View) v.findViewById(R.id.chineseRecipeDivider);
        final View westernRecipeDivider = (View) v.findViewById(R.id.westernRecipeDivider);

        //하위버튼 텍스트
        final TextView allRecipeText = (TextView) v.findViewById(R.id.allRecipeText);
        final TextView koreanRecipeText = (TextView) v.findViewById(R.id.koreanRecipeText);
        final TextView japaneseRecipeText = (TextView) v.findViewById(R.id.japaneseRecipeText);
        final TextView chineseRecipeText = (TextView) v.findViewById(R.id.chineseRecipeText);
        final TextView westernRecipeText = (TextView) v.findViewById(R.id.westernRecipeText);

        search = (EditText) v.findViewById(R.id.searchRecipe);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchRecipe(charSequence.toString());// 검색 기능용
                allRecipeDivider.setVisibility(View.VISIBLE);
                koreanRecipeDivider.setVisibility(View.INVISIBLE);
                japaneseRecipeDivider.setVisibility(View.INVISIBLE);
                chineseRecipeDivider.setVisibility(View.INVISIBLE);
                westernRecipeDivider.setVisibility(View.INVISIBLE);

                koreanRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                japaneseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                chineseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                westernRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));

                koreanRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                japaneseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                chineseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                westernRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                allRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
                allRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // 첫 접속시 설정
        allRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
        allRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        allRecipeDivider.setVisibility(View.VISIBLE);
        koreanRecipeDivider.setVisibility(View.INVISIBLE);
        japaneseRecipeDivider.setVisibility(View.INVISIBLE);
        chineseRecipeDivider.setVisibility(View.INVISIBLE);
        westernRecipeDivider.setVisibility(View.INVISIBLE);

        //레이아웃
        LinearLayout btn_allRecipe = (LinearLayout) v.findViewById(R.id.allRecipe);
        LinearLayout btn_korean = (LinearLayout) v.findViewById(R.id.korean);
        LinearLayout btn_japanese = (LinearLayout) v.findViewById(R.id.japanese);
        LinearLayout btn_chinese = (LinearLayout) v.findViewById(R.id.chinese);
        LinearLayout btn_western = (LinearLayout) v.findViewById(R.id.western);

        //버튼
        Button btn_allRecipe2 = (Button) v.findViewById(R.id.allRecipe2);
        Button btn_korean2 = (Button) v.findViewById(R.id.korean2);
        Button btn_japanese2 = (Button) v.findViewById(R.id.japanese2);
        Button btn_chinese2 = (Button) v.findViewById(R.id.chinese2);
        Button btn_western2 = (Button) v.findViewById(R.id.western2);

        //레이아웃 클릭 이벤트
        btn_allRecipe.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_child, new Fragment_child_home0()).commit();
                search.setText("");

                allRecipeDivider.setVisibility(View.VISIBLE);
                koreanRecipeDivider.setVisibility(View.INVISIBLE);
                japaneseRecipeDivider.setVisibility(View.INVISIBLE);
                chineseRecipeDivider.setVisibility(View.INVISIBLE);
                westernRecipeDivider.setVisibility(View.INVISIBLE);

                koreanRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                japaneseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                chineseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                westernRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));

                koreanRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                japaneseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                chineseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                westernRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                allRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
                allRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        });
        btn_korean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_child, new Fragment_child_home1()).commit();
                search.setText("");
                categoryRecipe("한식");

                allRecipeDivider.setVisibility(View.INVISIBLE);
                koreanRecipeDivider.setVisibility(View.VISIBLE);
                japaneseRecipeDivider.setVisibility(View.INVISIBLE);
                chineseRecipeDivider.setVisibility(View.INVISIBLE);
                westernRecipeDivider.setVisibility(View.INVISIBLE);

                allRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                japaneseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                chineseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                westernRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));

                allRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                japaneseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                chineseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                westernRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                koreanRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
                koreanRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        });
        btn_japanese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_child, new Fragment_child_home2()).commit();
                search.setText("");
                categoryRecipe("일식");

                allRecipeDivider.setVisibility(View.INVISIBLE);
                koreanRecipeDivider.setVisibility(View.INVISIBLE);
                japaneseRecipeDivider.setVisibility(View.VISIBLE);
                chineseRecipeDivider.setVisibility(View.INVISIBLE);
                westernRecipeDivider.setVisibility(View.INVISIBLE);

                koreanRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                allRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                chineseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                westernRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));

                koreanRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                allRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                chineseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                westernRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                japaneseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
                japaneseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        });
        btn_chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_child, new Fragment_child_home3()).commit();
                search.setText("");
                categoryRecipe("중식");

                allRecipeDivider.setVisibility(View.INVISIBLE);
                koreanRecipeDivider.setVisibility(View.INVISIBLE);
                japaneseRecipeDivider.setVisibility(View.INVISIBLE);
                chineseRecipeDivider.setVisibility(View.VISIBLE);
                westernRecipeDivider.setVisibility(View.INVISIBLE);

                koreanRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                japaneseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                allRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                westernRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));

                koreanRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                japaneseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                allRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                westernRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                chineseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
                chineseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        });
        btn_western.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_child, new Fragment_child_home4()).commit();
                search.setText("");
                categoryRecipe("양식");

                allRecipeDivider.setVisibility(View.INVISIBLE);
                koreanRecipeDivider.setVisibility(View.INVISIBLE);
                japaneseRecipeDivider.setVisibility(View.INVISIBLE);
                chineseRecipeDivider.setVisibility(View.INVISIBLE);
                westernRecipeDivider.setVisibility(View.VISIBLE);

                koreanRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                japaneseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                chineseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                allRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));

                koreanRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                japaneseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                chineseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                allRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                westernRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
                westernRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        });

        //버튼 클릭 이벤트
        btn_allRecipe2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_child, new Fragment_child_home0()).commit();
                search.setText("");

                allRecipeDivider.setVisibility(View.VISIBLE);
                koreanRecipeDivider.setVisibility(View.INVISIBLE);
                japaneseRecipeDivider.setVisibility(View.INVISIBLE);
                chineseRecipeDivider.setVisibility(View.INVISIBLE);
                westernRecipeDivider.setVisibility(View.INVISIBLE);

                koreanRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                japaneseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                chineseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                westernRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));

                koreanRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                japaneseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                chineseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                westernRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                allRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
                allRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        });
        btn_korean2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_child, new Fragment_child_home1()).commit();
                search.setText("");
                categoryRecipe("한식");

                allRecipeDivider.setVisibility(View.INVISIBLE);
                koreanRecipeDivider.setVisibility(View.VISIBLE);
                japaneseRecipeDivider.setVisibility(View.INVISIBLE);
                chineseRecipeDivider.setVisibility(View.INVISIBLE);
                westernRecipeDivider.setVisibility(View.INVISIBLE);

                allRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                japaneseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                chineseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                westernRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));

                allRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                japaneseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                chineseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                westernRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                koreanRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
                koreanRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        });
        btn_japanese2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_child, new Fragment_child_home2()).commit();
                search.setText("");
                categoryRecipe("일식");

                allRecipeDivider.setVisibility(View.INVISIBLE);
                koreanRecipeDivider.setVisibility(View.INVISIBLE);
                japaneseRecipeDivider.setVisibility(View.VISIBLE);
                chineseRecipeDivider.setVisibility(View.INVISIBLE);
                westernRecipeDivider.setVisibility(View.INVISIBLE);

                koreanRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                allRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                chineseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                westernRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));

                koreanRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                allRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                chineseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                westernRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                japaneseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
                japaneseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        });
        btn_chinese2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_child, new Fragment_child_home3()).commit();
                search.setText("");
                categoryRecipe("중식");

                allRecipeDivider.setVisibility(View.INVISIBLE);
                koreanRecipeDivider.setVisibility(View.INVISIBLE);
                japaneseRecipeDivider.setVisibility(View.INVISIBLE);
                chineseRecipeDivider.setVisibility(View.VISIBLE);
                westernRecipeDivider.setVisibility(View.INVISIBLE);

                koreanRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                japaneseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                allRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                westernRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));

                koreanRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                japaneseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                allRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                westernRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                chineseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
                chineseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        });
        btn_western2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_child, new Fragment_child_home4()).commit();
                search.setText("");
                categoryRecipe("양식");

                allRecipeDivider.setVisibility(View.INVISIBLE);
                koreanRecipeDivider.setVisibility(View.INVISIBLE);
                japaneseRecipeDivider.setVisibility(View.INVISIBLE);
                chineseRecipeDivider.setVisibility(View.INVISIBLE);
                westernRecipeDivider.setVisibility(View.VISIBLE);

                koreanRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                japaneseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                chineseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                allRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));

                koreanRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                japaneseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                chineseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                allRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                westernRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
                westernRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        });

        new BackgroundTask().execute();


        //Button detailButton = (Button)v.findViewById(R.id.detailButton);

        Bundle bundle = getArguments();
        final String userName = bundle.getString("userName");
        final String userID = bundle.getString("userID");
/*
        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), RecipeClickedActivity.class);
                intent.putExtra("user_id", userID);
                intent.putExtra("recipe_id", recipeList.get(position).getRecipe_id());
                intent.putExtra("recipe_name", recipeList.get(position).getRecipe_name());
                intent.putExtra("recipe_category", recipeList.get(position).getRecipe_category());
                intent.putExtra("recipe_ingredient", recipeList.get(position).getRecipe_ingredient());
                intent.putExtra("recipe_content", recipeList.get(position).getRecipe_content());
                startActivity(intent);
            }
        });
*/
        recipeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), RecipeClickedActivity.class);
                intent.putExtra("user_id", userID);
                intent.putExtra("user_name", userName);
                intent.putExtra("recipe_id", recipeList.get(position).getRecipe_id());
                intent.putExtra("recipe_name", recipeList.get(position).getRecipe_name());
                intent.putExtra("recipe_category", recipeList.get(position).getRecipe_category());
                intent.putExtra("recipe_ingredient", recipeList.get(position).getRecipe_ingredient());
                intent.putExtra("recipe_content", recipeList.get(position).getRecipe_content());
                startActivity(intent);
                /*search.setText("");
                
                allRecipeDivider.setVisibility(View.VISIBLE);
                koreanRecipeDivider.setVisibility(View.INVISIBLE);
                japaneseRecipeDivider.setVisibility(View.INVISIBLE);
                chineseRecipeDivider.setVisibility(View.INVISIBLE);
                westernRecipeDivider.setVisibility(View.INVISIBLE);

                koreanRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                japaneseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                chineseRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                westernRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));

                koreanRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                japaneseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                chineseRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                westernRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                allRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
                allRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));*/
            }
        });


        return v;
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
                search.setText("");
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(FragmentHome.this.getActivity());
                    dialog = builder.setMessage("레시피가 존재하지 않습니다.")
                            .setCancelable(false)
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                }
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

    public void categoryRecipe(String category) {
        recipeList.clear();
        for (int i = 0; i < saveList.size(); i++) {
            if (saveList.get(i).getRecipe_category().contains(category)) {//contains메소드로 search 값이 있으면 true를 반환함
                recipeList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();//어댑터에 값일 바뀐것을 알려줌

    }


    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if (activity != null) {
            ((TestActivity) activity).setCustomActionBar("레시피");
        }
    }
}

