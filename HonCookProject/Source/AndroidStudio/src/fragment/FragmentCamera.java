package org.tensorflow.demo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.tensorflow.demo.activity.DetectorActivity;
import org.tensorflow.demo.R;
import org.tensorflow.demo.activity.GalleryActivity;
import org.tensorflow.demo.activity.TestActivity;


public class FragmentCamera extends Fragment {
    TestActivity activity;

    private String user_id;
    private String user_name;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //이 메소드가 호출될떄는 프래그먼트가 엑티비티위에 올라와있는거니깐 getActivity메소드로 엑티비티참조가능
        activity = (TestActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //이제 더이상 엑티비티 참초가안됨
        activity = null;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_camera, container, false);

        Bundle bundle = getArguments();
        user_id = bundle.getString("userID");
        user_name = bundle.getString("userName");

        Button cameraButton = rootView.findViewById(R.id.cameraButton);
        //Button galleryButton = rootView.findViewById(R.id.gallaryButton);

        //galleryButton.setEnabled(false);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetectorActivity.class);
                intent.putExtra("userName", user_name);
                startActivity(intent);
            }
        });
       /* galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GalleryActivity.class);
                startActivity(intent);
            }
        });*/
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if (activity != null) {
            ((TestActivity) activity).setCustomActionBar("식재료 인식");
        }
    }

}