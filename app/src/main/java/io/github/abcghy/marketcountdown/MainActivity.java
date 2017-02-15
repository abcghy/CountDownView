package io.github.abcghy.marketcountdown;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.abcghy.countdown.CountDownView;

public class MainActivity extends AppCompatActivity {

    private CountDownView countDownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countDownView = (CountDownView) findViewById(R.id.count_down_view);
        countDownView.setCountDownTime(System.currentTimeMillis() + 1000000);
        countDownView.start();
    }
}
