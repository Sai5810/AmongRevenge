package com.vakasai.a09animation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImposterS extends RectF {
    int dX = 0;
    int dY = 0;
    int pDx = 0;
    private final Bitmap bitR;
    private final Bitmap bitL;
    private int frame = 0, delay = 1;
    final int icWidth;
    final int icHeight;
    boolean killingPower = false;
    boolean isLeft = false;

    public ImposterS(Resources res) {
        super(0, 0, 100, 100);
        bitR = BitmapFactory.decodeResource(res, R.drawable.redsus);
        Matrix mtx = new Matrix();
        mtx.preScale(-1, 1);
        bitL = Bitmap.createBitmap(bitR, 0, 0, bitR.getWidth(), bitR.getHeight(), mtx, true);
        icWidth = bitR.getWidth() / 16;
        icHeight = bitR.getHeight() / 14;
        right = left + icWidth;
        bottom = top + icHeight;
    }

    public void draw(Canvas canvas) {
        if (left + dX >= 0 && right + dX < canvas.getWidth()) {
            offset(dX, 0);
        }
        if (top + dY >= 0 && bottom + dY < canvas.getHeight()) {
            offset(0, dY);
        }
        if (dX == 0 && dY == 0) {
            frame = 0;
        } else if (delay-- < 0) {
            if (frame >= 15) {
                frame = 1;
            } else if (frame == 8) {
                frame = 10;
            } else {
                ++frame;
            }
            delay = 1;
        }
        if (dX < 0 || (frame == 0 && pDx < 0)) {
            isLeft = true;
            int srcX = (15 - frame) * icWidth;
            Rect src = new Rect(srcX, 0, srcX + icWidth, icHeight);
            canvas.drawBitmap(bitL, src, this, null);
        } else {
            isLeft = false;
            int srcX = frame * icWidth;
            Rect src = new Rect(srcX, 0, srcX + icWidth, icHeight);
            canvas.drawBitmap(bitR, src, this, null);
        }
    }

    public void move(int angle, int strength) {
        if (dX != 0) {
            pDx = dX;
        }
        dX = (int) Math.round(.2 * strength * Math.cos(angle * (Math.PI / 180.0)));
        dY = (int) Math.round(-.2 * strength * Math.sin(angle * (Math.PI / 180.0)));
    }

    public void reset(Canvas canvas) {
        top = canvas.getHeight() / 2f;
        bottom = top + icHeight;
        left = canvas.getWidth() / 2f;
        right = left + icWidth;
        dX = 0;
        dY = 0;
        pDx = 0;
        frame = 0;
        delay = 1;
    }
}
