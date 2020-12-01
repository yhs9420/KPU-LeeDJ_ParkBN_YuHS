package org.tensorflow.demo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.demo.R;
import org.tensorflow.demo.activity.TestActivity;
import org.tensorflow.demo.request.FragmentAddRequest;

public class FragmentAdd extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);

        Spinner categorySpinner = (Spinner) v.findViewById(R.id.recipeCategory);

        final EditText recipeName = (EditText) v.findViewById(R.id.recipeName);
        final Spinner recipeCategory = (Spinner) v.findViewById(R.id.recipeCategory);
        final EditText recipeIngredient = (EditText) v.findViewById(R.id.recipeIngredient);
        final EditText recipeContent = (EditText) v.findViewById(R.id.recipeContent);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.분류, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


        //spinner 이벤트 리스너
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //Toast.makeText(getActivity().getApplicationContext(),Integer.toString(i), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //recipeIngredient.setMovementMethod(new ScrollingMovementMethod());
        //recipeContent.setMovementMethod(new ScrollingMovementMethod());

        Button recipeAddButton = (Button) v.findViewById(R.id.recipeAddButton);

        recipeAddButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String recipe_Name = recipeName.getText().toString();
                String recipe_Category = recipeCategory.getSelectedItem().toString();
                String recipe_Ingredient = recipeIngredient.getText().toString();
                String recipe_Content = recipeContent.getText().toString();

                if (recipe_Name.equals("") || recipe_Ingredient.equals("") || recipe_Content.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("레시피 정보가 모두 입력되지 않았습니다.")
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage("레시피가 추가되었습니다.")
                                            .setCancelable(false)
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            })
                                            .create()
                                            .show();

                                    recipeName.setText("");
                                    recipeIngredient.setText("");
                                    recipeContent.setText("");


                                }
                                //회원 가입 실패시 success값이 false임
                                else {
                                    Toast.makeText(getActivity().getApplicationContext(), "레시피 추가 실패", Toast.LENGTH_SHORT).show();

                                    //알림상자를 만들어서 보여줌
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage("레시피 추가를 실패하셨습니다.")
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
                    FragmentAddRequest fragmentAddRequest = new FragmentAddRequest(recipe_Name, recipe_Category, recipe_Ingredient, recipe_Content, responseListener);
                    //2. RequestQueue를 생성한다.
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    //3. RequestQueue에 RequestObject를 넘겨준다.
                    queue.add(fragmentAddRequest);
                }

            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if (activity != null) {
            ((TestActivity) activity).setCustomActionBar("레시피 추가");
        }
    }

}
