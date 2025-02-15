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
    }
    private void OnButtonCalcClick(View view) {
        startActivity(new Intent(MainActivity.this, CalcActivity.class));
    }
}