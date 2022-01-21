package com.vakasai.a09animation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Arrays;

public class KnifeS extends RectF {
    private final ArrayList<Bitmap> leftAL;
    private final ArrayList<Bitmap> rightAL;
    final int icWidth;
    final int icHeight;

    public KnifeS(Resources res) {
        super(0, 0, 100, 100);
        Matrix flip = new Matrix();
        flip.preScale(-1, 1);
        Bitmap redBit = BitmapFactory.decodeResource(res, R.drawable.redsus);
        icWidth = redBit.getWidth() / 16;
        icHeight = redBit.getHeight() / 14;
        leftAL = new ArrayList<>(Arrays.asList(
                Bitmap.createBitmap(redBit, 12 * icWidth, 10 * icHeight, icWidth, icHeight),
                Bitmap.createBitmap(redBit, 13 * icWidth, 10 * icHeight, icWidth, icHeight),
                Bitmap.createBitmap(redBit, 14 * icWidth, 10 * icHeight, icWidth, icHeight)));
        Bitmap slash = Bitmap.createBitmap(redBit, 15 * icWidth, 10 * icHeight, icWidth, icHeight);
        leftAL.add(Bitmap.createBitmap(slash, 0, 0, slash.getWidth(), slash.getHeight(), flip, true));
        rightAL = new ArrayList<>();
        for (Bitmap b : leftAL) {
            rightAL.add(Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), flip, true));
        }
    }

    public void drawTo(ImposterS imp, Canvas canvas) {
        if (imp.isLeft) {
            left = imp.left + 40;
            top = imp.top - 40;
            bottom = imp.bottom - 40;
            right = imp.right + 40;
            canvas.drawBitmap(rightAL.get(0), null, this, null);
        } else {
            left = imp.left - 40;
            top = imp.top - 40;
            bottom = imp.bottom - 40;
            right = imp.right - 40;
            canvas.drawBitmap(leftAL.get(0), null, this, null);
        }
    }

    public void killFrame(ImposterS imp, Canvas canvas, int frame) {
        if (imp.isLeft) {
            left = imp.left - 40;
            top = imp.top - 40;
            bottom = imp.bottom - 40;
            right = imp.right - 40;
            canvas.drawBitmap(rightAL.get(frame), null, this, null);
        } else {
            left = imp.left + 40;
            top = imp.top - 40;
            bottom = imp.bottom - 40;
            right = imp.right + 40;
            canvas.drawBitmap(leftAL.get(frame), null, this, null);
        }
    }
}
