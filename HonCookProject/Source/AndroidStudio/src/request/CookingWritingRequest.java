package org.tensorflow.demo.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class CookingWritingRequest extends StringRequest {

    final static private String URL = "http://qwer456t.dothome.co.kr/CookingWritingAdd.php";
    private Map<String, String> parameters;

    //생성자
    public CookingWritingRequest(String board_Name, String board_Writer, String board_Content, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("board_Name", board_Name);
        parameters.put("board_Writer", board_Writer);
        parameters.put("board_Content", board_Content);

    }

    //추후 사용을 위한 부분
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
