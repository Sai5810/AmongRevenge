package com.vakasai.a09animation;

import static android.graphics.RectF.intersects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GameView extends View {
    final ImposterS imp;
    final KnifeS knife;
    final ArrayList<GhostS> ghosts;
    int maxDX = 8;
    final int maxDY = 4;
    int frame = 0;
    int spawnDelay = 100;
    final Handler rampingHandler = new Handler();
    final Handler powerUpHandler = new Handler();
    final Handler killTimeHandler = new Handler();
    final Handler attackHandler = new Handler();
    final killPowerUpS kpus;
    Runnable run;
    boolean iskillSpawned = false;
    int killClicked = 0;
    int killDelay = 5;
    boolean attacking = false;
    boolean isFirst = true;
    final RectF hitbox;
    final Paint paint = new Paint();
    int ctr = 0;
    final MediaPlayer[] ust;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        imp = new ImposterS(getResources());
        ghosts = new ArrayList<>();
        kpus = new killPowerUpS(getResources());
        knife = new KnifeS(getResources());
        rampingHandler.postDelayed(new Runnable() {
            public void run() {
                if (maxDX < 12) {
                    ++maxDX;
                }
                if (spawnDelay > 60) {
                    spawnDelay -= 10;
                }
                rampingHandler.postDelayed(this, 3000);
            }
        }, 3000);
        powerUpHandler.postDelayed(new Runnable() {
            public void run() {
                run.run();
                rampingHandler.postDelayed(this, ThreadLocalRandom.current().nextInt(3000, 5000));
            }
        }, ThreadLocalRandom.current().nextInt(3000, 5000));
        hitbox = new RectF();
        paint.setColor(Color.WHITE);
        paint.setTextSize(42);
        paint.setTextAlign(Paint.Align.CENTER);
        ust = new MediaPlayer[]{
                MediaPlayer.create(context, R.raw.bb_goat),
                MediaPlayer.create(context, R.raw.confusus),
                MediaPlayer.create(context, R.raw.moondai),
                MediaPlayer.create(context, R.raw.mssbu),
                MediaPlayer.create(context, R.raw.ssbu)
        };
        class myOnClickListener implements MediaPlayer.OnCompletionListener {
            int idx;

            public myOnClickListener(int idx) {
                this.idx = idx;
            }

            @Override
            public void onCompletion(MediaPlayer mp) {
                ust[(++idx) % ust.length].start();
            }
        }
        for (int i = 0; i < ust.length; ++i) {
            ust[i].setOnCompletionListener(new myOnClickListener(i));
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirst) {
            imp.top = getHeight() / 2f;
            imp.bottom = imp.top + imp.icHeight;
            imp.left = getWidth() / 2f;
            imp.right = imp.left + imp.icWidth;
            isFirst = false;
            ust[ThreadLocalRandom.current().nextInt(0, ust.length)].start();
        }
        run = () -> {
            kpus.randPos(canvas);
            iskillSpawned = true;
        };
        if (iskillSpawned) {
            if (intersects(kpus, imp)) {
                iskillSpawned = false;
                imp.killingPower = true;
                killTimeHandler.postDelayed(() -> imp.killingPower = false, 3000);
            } else {
                kpus.draw(canvas);
            }
        }
        ++frame;
        frame %= spawnDelay;
        if (frame == 0) {
            ghosts.add(new GhostS(getResources(), canvas, 4, maxDX, maxDY));
        }
        imp.draw(canvas);
        if (imp.killingPower) {
            if (killClicked > 0) {
                if (killDelay == 0) {
                    ++killClicked;
                    killDelay = 5;
                }
                if (killClicked == 3) {
                    killClicked = 0;
                } else {
                    knife.killFrame(imp, canvas, killClicked);
                }
                --killDelay;
            } else {
                knife.drawTo(imp, canvas);
            }
        }
        for (int i = ghosts.size() - 1; i >= 0 && !ghosts.isEmpty(); --i) {
            if (intersects(ghosts.get(i), imp)) {
                imp.reset(canvas);
                ghosts.clear();
                iskillSpawned = false;
                imp.killingPower = false;
                ctr = 0;
                maxDX = 8;
                frame = 0;
                spawnDelay = 100;
                break;
            }
            ghosts.get(i).draw(canvas, ghosts, i);
        }
        if (imp.killingPower && attacking) {
            hitbox.top = imp.top;
            hitbox.bottom = imp.bottom;
            if (imp.isLeft) {
                hitbox.left = imp.left - 40;
                hitbox.right = imp.right;
            } else {
                hitbox.left = imp.left;
                hitbox.right = imp.right + 40;
            }
            for (int i = ghosts.size() - 1; i >= 0 && !ghosts.isEmpty(); --i) {
                if (intersects(ghosts.get(i), hitbox)) {
                    ghosts.remove(i);
                    ++ctr;

                }
            }
        }
        canvas.drawText("Score: " + ctr, getWidth() / 2f, 30, paint);
        invalidate();
    }

    public void impMove(int angle, int strength) {
        imp.move(angle, strength);
    }

    public void kill() {
        if (imp.killingPower) {
            attacking = true;
            attackHandler.postDelayed(() -> attacking = false, 1000);
            ++killClicked;
            killDelay = 3;
        }
    }
}
