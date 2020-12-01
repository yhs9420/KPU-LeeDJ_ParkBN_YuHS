package org.tensorflow.demo.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class FragmentAddRequest extends StringRequest {

    final static private String URL = "http://qwer456t.dothome.co.kr/RecipeAdd.php";
    private Map<String, String> parameters;

    //생성자
    public FragmentAddRequest(String recipe_Name, String recipe_Category, String recipe_Ingredient, String recipe_Content, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("recipe_Name", recipe_Name);
        parameters.put("recipe_Category", recipe_Category);
        parameters.put("recipe_Ingredient", recipe_Ingredient);
        parameters.put("recipe_Content", recipe_Content);

    }

    //추후 사용을 위한 부분
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
