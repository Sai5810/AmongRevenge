package com.vakasai.a09animation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GhostS extends RectF {
    final int dX;
    final int dY;
    private Bitmap bitmap;

    public GhostS(Resources res, Canvas canvas, int minDX, int maxDX, int maxDY) {
        super(0, 0, 100, 100);
        left = new Random().nextBoolean() ? 0 : canvas.getWidth() - 100;
        top = ThreadLocalRandom.current().nextInt(0, canvas.getHeight() - 100);
        right += left;
        bottom += top;
        bitmap = BitmapFactory.decodeResource(res, R.drawable.ghost);
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        if (left != 0) {
            dX = ThreadLocalRandom.current().nextInt(-maxDX, -minDX);
        } else {
            dX = ThreadLocalRandom.current().nextInt(minDX, maxDX);
            Matrix mtx = new Matrix();
            mtx.preScale(-1, 1);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);
        }
        dY = ThreadLocalRandom.current().nextInt(-maxDY, maxDY);
    }

    public void draw(Canvas canvas, ArrayList<GhostS> ghosts, int i) {
        if (left < 0 || right > canvas.getWidth() || top < 0 || bottom > canvas.getHeight()) {
            ghosts.remove(i);
        } else {
            offset(dX, dY);
            canvas.drawBitmap(bitmap, null, this, null);
        }
    }
}
