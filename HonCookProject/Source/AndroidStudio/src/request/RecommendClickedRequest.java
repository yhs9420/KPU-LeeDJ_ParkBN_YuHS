package org.tensorflow.demo.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class RecommendClickedRequest extends StringRequest {

    final static private String URL = "http://qwer456t.dothome.co.kr/MyRecipeAdd2.php";
    private Map<String, String> parameters;

    //생성자
    public RecommendClickedRequest(String userID, int recipeID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("recipeID", recipeID + "");
    }

    //추후 사용을 위한 부분
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
