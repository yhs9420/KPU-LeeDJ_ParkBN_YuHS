package org.tensorflow.demo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.Session;
import org.tensorflow.demo.R;
import org.tensorflow.demo.adapter.CookingInforAdapter;
import org.tensorflow.demo.adapter.CookingInforCommentsAdapter;
import org.tensorflow.demo.list.CookingInfor;
import org.tensorflow.demo.list.CookingInforComments;
import org.tensorflow.demo.request.CookingInforBoardDeleteRequest;
import org.tensorflow.demo.request.CookingInforBoardRequest;
import org.tensorflow.demo.request.CookingWritingRequest;
import org.tensorflow.demo.request.FragmentAddRequest;
import org.tensorflow.demo.request.MypageDeleteRequest;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CookingInforBoardActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static String userID;
    public static String user_name;

    private ListView cookingCommentsListView;
    private TextView emptyText;
    private CookingInforCommentsAdapter adapter;
    private List<CookingInforComments> cookingInforCommentsList;
    private List<CookingInforComments> saveList;

    private String boardName;
    private String boardWriter;
    private String boardContent;
    private int boardId;
    private String boardId2;

    private Button commentsAddButton;
    private EditText commentsText;
    private Button board_deleteButton;
    private Button board_modifyButton;

    private TextView board_name;
    private TextView board_writer;
    private TextView board_id;
    private TextView board_content;

    private int boardCommentsCount;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookingboard);

        emptyText = (TextView) findViewById(R.id.empty);
        emptyText.setVisibility(View.INVISIBLE);
        cookingCommentsListView = (ListView) findViewById(R.id.cookingCommentsListView);
        cookingCommentsListView.setEmptyView(emptyText);
        cookingCommentsListView.setVisibility(View.VISIBLE);

        cookingInforCommentsList = new ArrayList<CookingInforComments>();
        saveList = new ArrayList<CookingInforComments>();
        adapter = new CookingInforCommentsAdapter(this, this, cookingInforCommentsList, saveList);//로 수정됨
        cookingCommentsListView.setAdapter(adapter);


        new BackgroundTask().execute();

        Intent intent = getIntent();
        user_name = intent.getStringExtra("userName");
        boardName = getIntent().getStringExtra("board_name");
        boardWriter = getIntent().getStringExtra("board_writer");
        boardContent = getIntent().getStringExtra("board_content");
        boardId = getIntent().getIntExtra("board_id", 0);
        boardId2 = String.valueOf(boardId);

        /*
        TextView userName = (TextView) findViewById(R.id.userName);
        String userName2 = user_name + "님";
        userName.setText(userName2);*/

        setCustomActionBar();

        board_name = (TextView) findViewById(R.id.board_name);
        board_writer = (TextView) findViewById(R.id.board_writer);
        board_id = (TextView) findViewById(R.id.board_id);
        board_content = (TextView) findViewById(R.id.board_content);


        board_deleteButton = (Button) findViewById(R.id.board_deleteButton);
        board_modifyButton = (Button) findViewById(R.id.board_modifyButton);
        commentsAddButton = (Button) findViewById(R.id.commentsAddButton);

        board_name.setText(boardName);
        board_writer.setText(boardWriter);
        board_id.setText(boardId2);
        board_content.setText(boardContent);

        if (!user_name.equals(boardWriter)) {
            //managementButton.setEnabled(false);
            board_modifyButton.setVisibility(View.GONE);
            board_deleteButton.setVisibility(View.GONE);
        }

        commentsText = (EditText) findViewById(R.id.commentsText);
        commentsText.setMovementMethod(new ScrollingMovementMethod());

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(CookingInforBoardActivity.this);


        board_modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //4. 콜백 처리부분(volley 사용을 위한 ResponseListener 구현 부분)
                AlertDialog.Builder dialog = new AlertDialog.Builder(CookingInforBoardActivity.this);
                dialog.setMessage("현재 글을 수정할 수 없습니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", null)
                        .show();
            }//onclick
        });

        board_deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //4. 콜백 처리부분(volley 사용을 위한 ResponseListener 구현 부분)
                AlertDialog.Builder dialog = new AlertDialog.Builder(CookingInforBoardActivity.this);
                dialog.setMessage("이 글을 정말 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");

                                            //받아온 값이 success면 정상적으로 서버로부터 값을 받은 것을 의미함
                                            if (success) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(CookingInforBoardActivity.this);
                                                AlertDialog dialog = builder
                                                        .setMessage("해당 글이 삭제되었습니다.")
                                                        .setCancelable(false)
                                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                            public void onClick(
                                                                    DialogInterface dialog, int id) {
                                                                Intent intent = new Intent(CookingInforBoardActivity.this, CookingInforActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                CookingInforBoardActivity.this.startActivity(intent);
                                                            }
                                                        })
                                                        .create();
                                                dialog.show();

                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(CookingInforBoardActivity.this);
                                                AlertDialog dialog = builder.setMessage("글 삭제를 실패하였습니다.")
                                                        .setPositiveButton("확인", null)
                                                        .setCancelable(false)
                                                        .create();
                                                dialog.show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                //volley 사용법
                                //1. RequestObject를 생성한다. 이때 서버로부터 데이터를 받을 responseListener를 반드시 넘겨준다.
                                //위에서 userID를 final로 선언해서 아래 처럼 가능함
                                CookingInforBoardDeleteRequest cookingInforBoardDeleteRequest = new CookingInforBoardDeleteRequest(boardId, boardWriter, responseListener);
                                //2. RequestQueue를 생성한다.
                                //여기서 UserListAdapter는 Activity에서 상속받은 클래스가 아니므로 Activity값을 생성자로 받아서 사용한다
                                RequestQueue queue = Volley.newRequestQueue(CookingInforBoardActivity.this);
                                //3. RequestQueue에 RequestObject를 넘겨준다.
                                queue.add(cookingInforBoardDeleteRequest);
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                //Toast.makeText(activity, "취소", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }//onclick
        });
    }

    //새로고침
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter = new CookingInforCommentsAdapter(CookingInforBoardActivity.this, CookingInforBoardActivity.this, cookingInforCommentsList, saveList);//로 수정됨
                adapter.notifyDataSetChanged();
                searchBoardName(boardId);
                cookingCommentsListView.setAdapter(adapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            target = "http://qwer456t.dothome.co.kr/CommentsList.php";
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
                Button commentsAddButton = (Button) findViewById(R.id.commentsAddButton);
                Intent intent = getIntent();
                user_name = intent.getStringExtra("userName");
                commentsText = (EditText) findViewById(R.id.commentsText);
                board_id = (TextView) findViewById(R.id.board_id);
                commentsAddButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String comments_text = commentsText.getText().toString();
                        String boardId = board_id.getText().toString();
                        final int boardId2 = Integer.parseInt(boardId);
                        if (comments_text.equals("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CookingInforBoardActivity.this);
                            builder.setMessage("댓글이 입력되지 않았습니다.")
                                    .setCancelable(false)
                                    .setNegativeButton("확인", null)
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
                                            AlertDialog.Builder builder = new AlertDialog.Builder(CookingInforBoardActivity.this);
                                            builder.setMessage("댓글이 추가되었습니다.")
                                                    .setCancelable(false)
                                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            searchBoardName(boardId2);
                                                            //cookingInforCommentsList.add(1);
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                    })
                                                    .create()
                                                    .show();

                                            commentsText.setText("");
                                        }
                                        //회원 가입 실패시 success값이 false임
                                        else {
                                            Toast.makeText(CookingInforBoardActivity.this.getApplicationContext(), "댓글 추가 실패", Toast.LENGTH_SHORT).show();

                                            //알림상자를 만들어서 보여줌
                                            AlertDialog.Builder builder = new AlertDialog.Builder(CookingInforBoardActivity.this);
                                            builder.setMessage("댓글 추가를 실패하였습니다.")
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
                            CookingInforBoardRequest cookingInforBoardRequest = new CookingInforBoardRequest(boardId2, comments_text, user_name, responseListener);
                            //2. RequestQueue를 생성한다.
                            RequestQueue queue = Volley.newRequestQueue(CookingInforBoardActivity.this);
                            //3. RequestQueue에 RequestObject를 넘겨준다.
                            queue.add(cookingInforBoardRequest);

                        }

                    }
                });
                cookingInforCommentsList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                int comments_id;
                int board_id;
                String comments_content;
                String user_name;

                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    comments_id = object.getInt("comments_id");
                    board_id = object.getInt("board_id");
                    comments_content = object.getString("comments_content");
                    user_name = object.getString("user_name");

                    CookingInforComments cookingInforComments = new CookingInforComments(comments_id, board_id, comments_content, user_name);
                    cookingInforCommentsList.add(cookingInforComments);
                    saveList.add(cookingInforComments);
                    count++;
                }
                /* 내림차순 정렬
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

                Collections.sort(cookingInforList, noDesc) ;*/
                boardCommentsCount = searchBoardName(boardId);
                TextView boardComments_Count = (TextView) findViewById(R.id.boardCommentsCount);
                boardComments_Count.setText(String.valueOf(boardCommentsCount));
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }

    public int searchBoardName(int search) {
        int count = 0;
        cookingInforCommentsList.clear();
        for (int i = 0; i < saveList.size(); i++) {
            if (saveList.get(i).getBoard_id() == search) {//contains메소드로 search 값이 있으면 true를 반환함
                cookingInforCommentsList.add(saveList.get(i));
                count++;
            }
        }
        adapter.notifyDataSetChanged();//어댑터에 값일 바뀐것을 알려줌

        return count;
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
