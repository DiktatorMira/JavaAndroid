package com.example.android213;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView tvCounter;
    private int counter = 10;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCounter = findViewById(R.id.text_counter);
        tvCounter.setText(String.valueOf(counter));

        findViewById(R.id.button_plus).setOnClickListener(this::OnButtonPlusClick);
        findViewById(R.id.button_minus).setOnClickListener(this::OnButtonMinusClick);
    }
    private void OnButtonPlusClick(View view) {
        counter++;
        tvCounter.setText(String.valueOf(counter));
    }
    private void OnButtonMinusClick(View view) {
        counter--;
        tvCounter.setText(String.valueOf(counter));
    }
}