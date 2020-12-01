package org.tensorflow.demo.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tensorflow.demo.R;
import org.tensorflow.demo.activity.TestActivity;
import org.tensorflow.demo.adapter.MypageListAdapter;
import org.tensorflow.demo.list.Mypage;
import org.tensorflow.demo.list.User;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FragmentMyPage extends Fragment {

    private ListView mypageListView;
    private MypageListAdapter adapter;
    private List<Mypage> mypageList;
    private List<Mypage> saveList;
    private String user_id;
    private String user_age;
    private String user_password;
    private String user_name;
    private TextView emptyText;
    private TextView mypageName;
    private TextView mypageId;
    private TextView mypageAge;
    private Fragment fragment_child_mypage1 = new Fragment_child_mypage1();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // return inflater.inflate(R.layout.fragment_mypage, container, false);
        final View v = inflater.inflate(R.layout.fragment_mypage, container, false);

        emptyText = (TextView) v.findViewById(R.id.empty);
        //emptyText.setVisibility(View.INVISIBLE);

        mypageListView = (ListView) v.findViewById(R.id.mypageListView);
        mypageListView.setVisibility(View.VISIBLE);
        mypageList = new ArrayList<Mypage>();
        saveList = new ArrayList<Mypage>();
        adapter = new MypageListAdapter(getActivity().getApplicationContext(), getActivity(), mypageList, saveList);//로 수정됨

        mypageName = (TextView) v.findViewById(R.id.mypageName);
        mypageId = (TextView) v.findViewById(R.id.mypageId);
        mypageAge = (TextView) v.findViewById(R.id.mypageAge);

        final LinearLayout myRecipeLayout = (LinearLayout) v.findViewById(R.id.myRecipeLayout);
        final LinearLayout myInforModifyLayout = (LinearLayout) v.findViewById(R.id.myInforModifyLayout);
        final TextView myRecipeText = (TextView) v.findViewById(R.id.myRecipeText);
        final TextView myInforModifyText = (TextView) v.findViewById(R.id.myInforModifyText);

        final View myRecipeDivider = (View) v.findViewById(R.id.myRecipeDivider);
        final View myInforModifyDivider = (View) v.findViewById(R.id.myInforModifyDivider);

        myRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
        myInforModifyText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
        myRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        myInforModifyText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        myRecipeDivider.setVisibility(View.VISIBLE);
        myInforModifyDivider.setVisibility(View.INVISIBLE);

        //클릭 이벤트
        myRecipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_child_mypage, new Fragment_child_mypage0()).commit();
                mypageListView.setVisibility(View.VISIBLE);
                emptyText = (TextView) v.findViewById(R.id.empty);
                emptyText.setVisibility(View.INVISIBLE);
                mypageListView.setEmptyView(emptyText);
                myRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
                myInforModifyText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                myRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                myInforModifyText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                myRecipeDivider.setVisibility(View.VISIBLE);
                myInforModifyDivider.setVisibility(View.INVISIBLE);
            }
        });
        myInforModifyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Bundle bundle = new Bundle();
                bundle.putString("userID", user_id);
                bundle.putString("userAge", user_age);
                bundle.putString("userPassword", user_name);
                bundle.putString("userName", user_password);
                FragmentTransaction transaction = ((AppCompatActivity)getActivity()).getSupportFragmentManager().beginTransaction();
                fragment_child_mypage1.setArguments(bundle);
                Log.v("FragmentMyPage 태그", "frag_child_mypage1 값 : " + fragment_child_mypage1);
                Log.v("FragmentMyPage 태그", "보내는 번들 값 : " + bundle);
*/

                getFragmentManager().beginTransaction().replace(R.id.fragment_child_mypage, new Fragment_child_mypage1()).commit();
                myRecipeText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack2));
                myInforModifyText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButton));
                myRecipeText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                myInforModifyText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                myRecipeDivider.setVisibility(View.INVISIBLE);
                myInforModifyDivider.setVisibility(View.VISIBLE);
                mypageListView.setVisibility(View.INVISIBLE);
                emptyText.setVisibility(View.INVISIBLE);

            }
        });


        new BackgroundTask().execute();

        /*mypageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MypageClickedActivity.class);
                Bundle bundle = getArguments();
                String userID = bundle.getString("userID");
                intent.putExtra("user_id", userID);
                intent.putExtra("recipe_id", mypageList.get(position).getRecipe_id());
                intent.putExtra("recipe_name", mypageList.get(position).getRecipe_name());
                intent.putExtra("recipe_category", mypageList.get(position).getRecipe_category());
                intent.putExtra("recipe_ingredient", mypageList.get(position).getRecipe_ingredient());
                intent.putExtra("recipe_content", mypageList.get(position).getRecipe_content());
                startActivity(intent);
            }
        });*/


        return v;
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://qwer456t.dothome.co.kr/MyPage.php";

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

            } catch (
                    Exception e) {
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
                Bundle bundle = getArguments();
                user_id = bundle.getString("userID");
                user_age = bundle.getString("userAge");
                user_name = bundle.getString("userName");
                user_password = bundle.getString("userPassword");

                mypageName.setText(user_name);
                mypageId.setText(user_id);
                mypageAge.setText(user_age);
                Log.v("FragmentMyPage 태그", "받는 번들 값 : " + bundle);
                mypageList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String userID;
                String userAge;
                int recipe_id;
                String recipe_name;
                String recipe_category;
                String recipe_ingredient;
                String recipe_content;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    userID = object.getString("userID");
                    userAge = object.getString("userAge");
                    recipe_id = object.getInt("recipe_id");
                    recipe_name = object.getString("recipe_name");
                    recipe_category = object.getString("recipe_category");
                    recipe_ingredient = object.getString("recipe_ingredient");
                    recipe_content = object.getString("recipe_content");
                    Mypage mypage = new Mypage(userID, userAge, recipe_id, recipe_name, recipe_category, recipe_ingredient, recipe_content);
                    mypageList.add(mypage);
                    saveList.add(mypage);
                    count++;
                }
                if (count == 0) {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(FragmentMyPage.this.getActivity());
                    dialog = builder.setMessage("레시피가 존재하지 않습니다.")
                            .setCancelable(false)
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                }

                myPageRecipe(user_id);
                mypageListView.setEmptyView(emptyText);
                mypageListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    public void myPageRecipe(String userID) {
        mypageList.clear();
        for (int i = 0; i < saveList.size(); i++) {
            if (saveList.get(i).getUserID().contentEquals(userID)) {//contains메소드로 search 값이 있으면 true를 반환함
                mypageList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();//어댑터에 값일 바뀐것을 알려줌
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if (activity != null) {
            ((TestActivity) activity).setCustomActionBar("내 정보");
        }
    }
}