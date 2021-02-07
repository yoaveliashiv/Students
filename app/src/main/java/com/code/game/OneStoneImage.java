package com.code.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageView;

import com.code.R;

public class OneStoneImage extends View {
    private Bitmap stoneW;
    private Bitmap stoneB;
    public OneStoneImage(Context context) {
        super(context);
        stoneB= BitmapFactory.decodeResource(getResources(), R.drawable.black);
        stoneW= BitmapFactory.decodeResource(getResources(), R.drawable.white);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
