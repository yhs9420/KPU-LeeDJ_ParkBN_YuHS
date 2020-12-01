package org.tensorflow.demo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.tensorflow.demo.R;
import org.tensorflow.demo.activity.CookingInforActivity;
import org.tensorflow.demo.activity.RecipeClickedActivity;
import org.tensorflow.demo.activity.RecipeEvaluationActivity;
import org.tensorflow.demo.activity.TestActivity;

public class FragmentCommunity extends Fragment {
    private LinearLayout cookingInforLayout;
    private LinearLayout recipeEvaluationLayout;

    private String user_name;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_community, container, false);

        Bundle bundle = getArguments();
        user_name = bundle.getString("userName");


        cookingInforLayout = v.findViewById(R.id.cookingInforLayout);
        recipeEvaluationLayout= v.findViewById(R.id.recipeEvaluationLayout);

        cookingInforLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "요리 정보 게시판", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), CookingInforActivity.class);
                intent.putExtra("userName", user_name);
                startActivity(intent);
            }
        });

        recipeEvaluationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                AlertDialog dialog2;
                dialog2 = builder.setMessage("현재 해당 서비스를 이용하실 수 없습니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", null)
                        .create();
                dialog2.show();
                /*Toast.makeText(getActivity(), "레시피 평가 게시판", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), RecipeEvaluationActivity.class);
                intent.putExtra("userName", user_name);
                startActivity(intent);*/
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if (activity != null) {
            ((TestActivity) activity).setCustomActionBar("커뮤니티");
        }
    }
}