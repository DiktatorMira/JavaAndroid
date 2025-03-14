package com.example.android213;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_calc).setOnClickListener(this::OnButtonCalcClick);
        findViewById(R.id.button_game).setOnClickListener(this::OnButtonGameClick);
        findViewById(R.id.button_anim).setOnClickListener(this::OnButtonAnimClick);
        findViewById(R.id.button_rate).setOnClickListener(this::OnButtonRateClick);
    }
    private void OnButtonCalcClick(View view) {
        startActivity(new Intent(MainActivity.this, CalcActivity.class));
    }
    private void OnButtonGameClick(View view) {
        startActivity(new Intent(MainActivity.this, GameActivity.class));
    }
    private void OnButtonAnimClick(View view) {
        startActivity(new Intent(MainActivity.this, AnimActivity.class));
    }
    private void OnButtonRateClick(View view) {
        startActivity(new Intent(MainActivity.this, RateActivity.class));
    }
}