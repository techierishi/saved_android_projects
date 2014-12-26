package com.krish.animation.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class RoundedLinearLayout extends LinearLayout {
    Path mPath;
    float mCornerRadius;

    public RoundedLinearLayout(Context context) {
        super(context);
    }

    public RoundedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(mPath);
        super.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        RectF r = new RectF(0, 0, w, h);
        mPath = new Path();
        mPath.addRoundRect(r, mCornerRadius, mCornerRadius, Direction.CW);
        mPath.close();
    }

    public void setCornerRadius(int radius) {
        mCornerRadius = radius;
        invalidate();
    }
  }
