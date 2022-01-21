package com.vakasai.a09animation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.zerokol.views.joystickView.JoystickView;


public class MainActivity extends AppCompatActivity {
    GameView dv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        dv = findViewById(R.id.game);
               JoystickView joystick = findViewById(R.id.joystick);
        joystick.setOnJoystickMoveListener((angle, power, direction) -> {
            angle = (angle - 90) * -1;
            dv.impMove(angle, power);
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
    }

    public void kill(View v) {
        dv.kill();
    }


}