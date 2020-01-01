package com.example.battleship;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;

import java.util.jar.Attributes;

public class SquareGridLayout extends GridLayout {
    public SquareGridLayout(Context context) {
        super(context);
    }
    public SquareGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public SquareGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widhtSpec, int heightSpec) {
        super.onMeasure(widhtSpec, heightSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
