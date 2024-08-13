package com.github.lzyzsd.randomcolorexample;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bruce on 15/2/9.
 */
public class RoundView extends View {
    private int roundColor = Color.GREEN;
    Paint paint = new Paint();
    public RoundView(Context context) {
        super(context);
    }

    public RoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getRoundColor() {
        return roundColor;
    }

    public void setRoundColor(int roundColor) {
        this.roundColor = roundColor;
    }

    protected void onDraw(android.graphics.Canvas canvas)
    {
        paint.setColor(this.roundColor);
        paint.setAntiAlias(true);
        canvas.drawCircle(getWidth()/2 - getPaddingLeft(), getHeight()/2 - getPaddingLeft(), getWidth()/2 - getPaddingLeft(), paint);
    }
}