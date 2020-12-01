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
import org.tensorflow.demo.request.RegisterRequest;
import org.tensorflow.demo.request.RegisterValidateRequest;

public class RegisterActivity extends AppCompatActivity {
    private String userID;
    private String userPassword;
    private String userPassword_chk;
    private String userName;
    private String userAge;
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final EditText passwordchkTest = (EditText) findViewById(R.id.passwordchkText);
        final EditText nameText = (EditText) findViewById(R.id.nameText);
        final EditText ageText = (EditText) findViewById(R.id.ageText);

        setCustomActionBar();

        final Button validateButton = (Button) findViewById(R.id.validateButton);
        final Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setEnabled(false);
        registerButton.setBackgroundResource(R.drawable.buttonshape_false);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = idText.getText().toString();
                if (validate) {
                    return;//검증 완료
                }
                //ID 값을 입력하지 않았다면
                if (userID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디가 입력되지 않았습니다.")
                            .setCancelable(false)
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                //검증시작
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {//사용할 수 있는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("이 아이디는 사용 가능합니다.")
                                        .setCancelable(false)
                                        .setPositiveButton("사용하기", new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog, int id) {
                                                idText.setEnabled(false);//아이디값을 바꿀 수 없도록 함
                                                validate = true;//검증완료
                                                idText.setBackgroundResource(R.drawable.edittext_login_false);
                                                validateButton.setBackgroundResource(R.drawable.buttonshape_false);
                                                registerButton.setBackgroundResource(R.drawable.buttonshape);
                                                registerButton.setEnabled(true);
                                            }
                                        })
                                        .setNegativeButton("다시입력", null)
                                        .create();
                                dialog.show();

                            } else {//사용할 수 없는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("이미 사용중인 아이디입니다.")
                                        .setCancelable(false)
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };//Response.Listener 완료

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                RegisterValidateRequest registerValidateRequest = new RegisterValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerValidateRequest);
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                userID = idText.getText().toString();
                userPassword = passwordText.getText().toString();
                userPassword_chk = passwordchkTest.getText().toString();
                userName = nameText.getText().toString();
                userAge = ageText.getText().toString();

                if (userID.equals("") || userPassword.equals("") || userPassword_chk.equals("") || userName.equals("") || userAge.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("회원정보가 모두 입력되지 않았습니다.")
                            .setCancelable(false)
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                } else if (userPassword.equals(userPassword_chk)) {
                    /* 패스워드 확인이 정상적으로 됨 */
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

                                    //Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();

                                    //알림상자를 만들어서 보여줌
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("회원가입을 성공하였습니다.")
                                            .setCancelable(false)
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //그리고 첫화면으로 돌아감
                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    RegisterActivity.this.startActivity(intent);
                                                }
                                            })
                                            .create()
                                            .show();

                                    idText.setText("");
                                    passwordText.setText("");
                                    passwordchkTest.setText("");
                                    nameText.setText("");
                                    ageText.setText("");
                                }
                                //회원 가입 실패시 success값이 false임
                                else {
                                    Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();

                                    //알림상자를 만들어서 보여줌
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("회원가입을 실패하였습니다.")
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
                    //volley 사용법
                    //1. RequestObject를 생성한다. 이때 서버로부터 데이터를 받을 responseListener를 반드시 넘겨준다.
                    RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userName, userAge, responseListener);
                    //2. RequestQueue를 생성한다.
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    //3. RequestQueue에 RequestObject를 넘겨준다.
                    queue.add(registerRequest);
                } else {
                    /* 패스워드 확인이 불일치 함 */
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("비밀번호가 불일치합니다.")
                            .setNegativeButton("확인", null)
                            .setCancelable(false)
                            .create()
                            .show();
                }
            }
        });

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


