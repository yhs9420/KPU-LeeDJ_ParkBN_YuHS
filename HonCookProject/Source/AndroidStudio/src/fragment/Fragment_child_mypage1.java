package org.tensorflow.demo.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.demo.R;
import org.tensorflow.demo.activity.MainActivity;
import org.tensorflow.demo.activity.RecipeClickedActivity;

public class Fragment_child_mypage1 extends android.app.Fragment {
    private AlertDialog dialog;
    private AlertDialog dialog2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_child_mypage1, container, false);

        Button nameChangeButton = (Button) v.findViewById(R.id.nameChangeButton);
        Button withdrawlButton = (Button) v.findViewById(R.id.withdrawlButton);
        Button logoutButton = (Button) v.findViewById(R.id.logoutButton);

        nameChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout namechangelayout = (LinearLayout) vi.inflate(R.layout.namechange, null);

                final EditText nameChangeText = (EditText) view.findViewById(R.id.nameChangeText);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                dialog = builder.setTitle("이름 변경")
                        .setView(namechangelayout)
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                AlertDialog dialog2;
                                dialog2 = builder.setMessage("현재 이름 변경을 하실 수 없습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog2.show();
                                //Toast.makeText(getActivity(), "미구현", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소",null)
                        .create();
                dialog.show();
            }
        });

        withdrawlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                dialog = builder.setMessage("정말 회원 탈퇴를 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                AlertDialog dialog2;
                                dialog2 = builder.setMessage("현재 회원 탈퇴를 하실 수 없습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog2.show();
                                //Toast.makeText(getActivity(), "미구현", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소",null)
                        .create();
                dialog.show();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                dialog = builder.setMessage("정말 로그아웃 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("취소",null)
                        .create();
                dialog.show();
            }
        });

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(dialog != null) {
            dialog.dismiss();
            dialog=null;
        }else if(dialog2 != null) {
            dialog2.dismiss();
            dialog2 = null;
        }
    }

}
