package org.tensorflow.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {

    // 만약 CheckBox가 아닌 View를 추가한다면 아래의 변수 사용 가능.
    // private boolean mIsChecked ;

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        //mIsChecked = false ;
    }

    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox);

        return cb.isChecked();
        // return mIsChecked ;
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox);

        setChecked(!cb.isChecked());
        //setChecked(mIsChecked ? false : true) ;
    }

    @Override
    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox);

        if (cb.isChecked() != checked) {
            cb.setChecked(checked);
        }

        // CheckBox 가 아닌 View의 상태 변경.
    }
}