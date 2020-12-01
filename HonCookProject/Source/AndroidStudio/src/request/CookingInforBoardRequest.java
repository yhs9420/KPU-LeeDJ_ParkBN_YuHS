package org.tensorflow.demo.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class CookingInforBoardRequest extends StringRequest {

    final static private String URL = "http://qwer456t.dothome.co.kr/CommentsAdd.php";
    private Map<String, String> parameters;

    //생성자
    public CookingInforBoardRequest(int board_id, String comments_content, String user_name, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("board_Id", board_id +"");
        parameters.put("comments_Content", comments_content);
        parameters.put("user_Name", user_name);
    }

    //추후 사용을 위한 부분
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
