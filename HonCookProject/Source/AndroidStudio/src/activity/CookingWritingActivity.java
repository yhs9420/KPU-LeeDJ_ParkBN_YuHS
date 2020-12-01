package org.tensorflow.demo.activity;

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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.demo.R;
import org.tensorflow.demo.request.CookingWritingRequest;
import org.tensorflow.demo.request.FragmentAddRequest;

public class CookingWritingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookingwriting);

        Intent intent = getIntent();
        final String user_name = intent.getStringExtra("userName");

        setCustomActionBar();

        final EditText cookInforName = (EditText) findViewById(R.id.cookInforName);
        final EditText cookInforContent = (EditText) findViewById(R.id.cookInforContent);

        Button writingRegistrationButton = (Button) findViewById(R.id.writingRegistration);

        writingRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cookinfor_writer = user_name;
                String cookinfor_name = cookInforName.getText().toString();
                String cookinfor_content = cookInforContent.getText().toString();

                if (cookinfor_name.equals("") || cookinfor_content.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CookingWritingActivity.this);
                    builder.setMessage("정보가 모두 입력되지 않았습니다.")
                            .setNegativeButton("확인", null)
                            .setCancelable(false)
                            .create()
                            .show();
                    return;
                } else {
                    //4. 콜백 처리부분(volley 사용을 위한 ResponseListener 구현 부분)
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
                                    //Toast.makeText(getActivity().getApplicationContext(), "레시피 추가 성공", Toast.LENGTH_SHORT).show();

                                    //알림상자를 만들어서 보여줌
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CookingWritingActivity.this);
                                    builder.setMessage("글이 등록되었습니다.")
                                            .setCancelable(false)
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(CookingWritingActivity.this, CookingInforActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    CookingWritingActivity.this.startActivity(intent);
                                                }
                                            })
                                            .create()
                                            .show();

                                    cookInforName.setText("");
                                    cookInforContent.setText("");

                                }
                                //회원 가입 실패시 success값이 false임
                                else {
                                    Toast.makeText(CookingWritingActivity.this, "글 등록 실패", Toast.LENGTH_SHORT).show();

                                    //알림상자를 만들어서 보여줌
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CookingWritingActivity.this);
                                    builder.setMessage("글 등록이 실패하셨습니다.")
                                            .setCancelable(false)
                                            .setNegativeButton("확인", null)
                                            .create()
                                            .show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };//responseListener 끝

                    //volley 사용법
                    //1. RequestObject를 생성한다. 이때 서버로부터 데이터를 받을 responseListener를 반드시 넘겨준다.
                    CookingWritingRequest cookingWritingRequest = new CookingWritingRequest(cookinfor_name, cookinfor_writer, cookinfor_content, responseListener);
                    //2. RequestQueue를 생성한다.
                    RequestQueue queue = Volley.newRequestQueue(CookingWritingActivity.this);
                    //3. RequestQueue에 RequestObject를 넘겨준다.
                    queue.add(cookingWritingRequest);
                }

            }
        });
        return;


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
