package com.vakasai.a09animation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.concurrent.ThreadLocalRandom;

public class killPowerUpS extends RectF {
    private Bitmap bitmap;

    public killPowerUpS(Resources res) {
        super(0, 0, 50, 50);
        bitmap = BitmapFactory.decodeResource(res, R.drawable.knifepower);
        bitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, this, null);
    }

    public void randPos(Canvas canvas) {
        left = ThreadLocalRandom.current().nextInt(0, canvas.getWidth() - 50);
        top = ThreadLocalRandom.current().nextInt(0, canvas.getHeight() - 50);
        right = left + 50;
        bottom = top + 50;
    }
}
