package org.tensorflow.demo.list;

import android.graphics.drawable.Drawable;

public class Capture {
    private Drawable icon;
    private String text;

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public String getText() {
        return this.text;
    }
}