package org.tensorflow.demo.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MypageDeleteRequest extends StringRequest {

    final static private String URL = "http://qwer456t.dothome.co.kr/MyRecipeDelete.php";
    private Map<String, String> parameters;

    public MypageDeleteRequest(String userID, int recipeID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);//Post방식임
        parameters = new HashMap<>();//해쉬맵 생성후 parameters 변수에 값을 넣어줌
        parameters.put("userID", userID);
        parameters.put("recipeID", recipeID + "");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}

