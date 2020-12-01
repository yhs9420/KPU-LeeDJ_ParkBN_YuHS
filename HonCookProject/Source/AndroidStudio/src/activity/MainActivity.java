package org.tensorflow.demo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.demo.R;
import org.tensorflow.demo.request.LoginRequest;

public class MainActivity extends AppCompatActivity {

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);*/

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView registerButton = (TextView) findViewById(R.id.registerButton);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(registerIntent);
                passwordText.setText("");
                idText.setText("");
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();

                if (userID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    dialog = builder.setMessage("혼쿡 아이디를 입력해주세요.")
                            .setNegativeButton("확인", null)
                            .setCancelable(false)
                            .create();
                    dialog.show();
                } else if (userPassword.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    dialog = builder.setMessage("비밀번호를 입력해주세요.")
                            .setNegativeButton("확인", null)
                            .setCancelable(false)
                            .create();
                    dialog.show();
                } else {
                    //4. 콜백 처리부분(volley 사용을 위한 ResponseListener 구현 부분)
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                //서버에서 보내준 값이 true이면?
                                if (success) {

                                    //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                                    String userID = jsonResponse.getString("userID");
                                    String userPassword = jsonResponse.getString("userPassword");
                                    String userName = jsonResponse.getString("userName");
                                    String userAge = jsonResponse.getString("userAge");

                                    //로그인에 성공했으므로 TestActivity로 넘어감
                                    Intent intent = new Intent(MainActivity.this, TestActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("userID", userID);
                                    intent.putExtra("userPassword", userPassword);
                                    intent.putExtra("userName", userName);
                                    intent.putExtra("userAge", userAge);
                                    MainActivity.this.startActivity(intent);
                                    passwordText.setText("");
                                    idText.setText("");
                                } else {//로그인 실패시
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("혼쿡 아이디 혹은 비밀번호를 잘못 입력하셨거나\n등록되지 않은 혼쿡 아이디 입니다.")
                                            .setNegativeButton("다시입력", null)
                                            .setCancelable(false)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(loginRequest);
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            dialog = builder.setMessage("혼쿡 앱을 종료하시겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialog, int id) {
                        }
                    })
                    .setCancelable(false)
                    .show();
            return;

    }
}
