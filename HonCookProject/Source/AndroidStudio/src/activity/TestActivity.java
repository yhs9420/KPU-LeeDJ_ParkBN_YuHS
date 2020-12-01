package org.tensorflow.demo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.demo.R;
import org.tensorflow.demo.fragment.FragmentAdd;
import org.tensorflow.demo.fragment.FragmentCamera;
import org.tensorflow.demo.fragment.FragmentHome;
import org.tensorflow.demo.fragment.FragmentMyPage;
import org.tensorflow.demo.fragment.FragmentCommunity;
import org.tensorflow.demo.fragment.Fragment_child_mypage1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class TestActivity extends AppCompatActivity {
    public static String userID;
    public String userAge;
    public String userPassword;
    public static String userName;
    private FragmentHome fragmentHome = new FragmentHome();
    // private FragmentManager fm = getSupportFragmentManager();
    private FragmentAdd fragmentAdd = new FragmentAdd();
    private FragmentCamera fragmentCamera = new FragmentCamera();
    private FragmentCommunity fragmentCommunity = new FragmentCommunity();
    private FragmentMyPage fragmentMyPage = new FragmentMyPage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        getFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();

        setCustomActionBar("");

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        //BottomNavigationHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        // TextView idText = (TextView) findViewById(R.id.idText);
        // TextView passwordText = (TextView) findViewById(R.id.passwordText);
        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);
        Button managementButton = (Button) findViewById(R.id.managementButton);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userAge = intent.getStringExtra("userAge");
        userPassword = intent.getStringExtra("userAge");
        userName = intent.getStringExtra("userName");
        //String userPassword = intent.getStringExtra("userPassword");
        String msg = userName + "님";

        //intent = new Intent(TestActivity.this, RecommendActivity.class);
        //intent.putExtra("userID", userID);


        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        bundle.putString("userAge", userAge);
        bundle.putString("userPassword", userPassword);
        bundle.putString("userName", userName);
        fragmentMyPage.setArguments(bundle);
        fragmentHome.setArguments(bundle);
        fragmentCommunity.setArguments(bundle);
        fragmentCamera.setArguments(bundle);
        Log.v("TestActivity 태그", "fragmentHome 값 : " + fragmentHome);
        Log.v("TestActivity 태그", "fragmentMyPage 값 : " + fragmentMyPage);
        Log.v("TestActivity 태그", "보내는 번들 값 : " + bundle);

        //idText.setText(userID);
        //passwordText.setText(userPassword);
        welcomeMessage.setText(msg);
        //welcomeMessage.setText("");

        //admin 계정이 아니면 버튼 안보이게 함
        if (!userID.equals("admin")) {
            //managementButton.setEnabled(false);
            managementButton.setVisibility(View.GONE);
        }

        //MANAGE버튼이 눌리면 여기로 옴
        managementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundTask().execute();
            }
        });


    }

    //모든회원에 대한 정보를 가져오기 위한 쓰레드
    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            target = "http://qwer456t.dothome.co.kr/List.php";
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
            Bundle bundle = new Bundle();
            bundle.putString("userList", result);
            bundle.putString("user_id", userID);
            fragmentMyPage.setArguments(bundle);

            Intent intent = new Intent(TestActivity.this, ManagementActivity.class);
            intent.putExtra("userList", result);//파싱한 값을 넘겨줌
            TestActivity.this.startActivity(intent);//ManagementActivity로 넘어감
        }

    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            //  FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.homeItem:
                    getFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();
                    break;
                case R.id.cameraItem:
                    getFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentCamera).commitAllowingStateLoss();
                    break;
                case R.id.addItem:
                    getFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentAdd).commitAllowingStateLoss();
                    break;
                case R.id.shareItem:
                    getFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentCommunity).commitAllowingStateLoss();
                    break;
                case R.id.mypageItem:
                    getFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentMyPage).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    /*public void onFragmentChange(int index){
        if(index == 0 ){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentHome).commit();
        }else if(index == 1){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentAdd).commit();
        }
    }*/
    private AlertDialog dialog;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
        dialog = builder.setMessage("혼쿡 앱을 종료하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog, int id) {
                        moveTaskToBack(true);
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid() );
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog, int id) {

                    }
                })
                .show();
        return;

    }

    public void setCustomActionBar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar_test);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        TextView titleText = findViewById(R.id.title);
        titleText.setText(title);
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        //actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
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


