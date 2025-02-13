package com.example.android213;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;

public class CalcActivity extends AppCompatActivity {
    private TextView tvExpression, tvResult;
    private double memory = 0, currentNumber = 0, lastNumber = 0;
    private String pendingOperation = "";
    private boolean isNewNumber = true;
    private DecimalFormat df = new DecimalFormat("#.##########");

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        tvExpression = findViewById(R.id.calc_tv_expression);
        tvResult = findViewById(R.id.calc_tv_result);

        findViewById(R.id.calc_btn_0).setOnClickListener(this::OnDigitClick);
        findViewById(R.id.calc_btn_1).setOnClickListener(this::OnDigitClick);
        findViewById(R.id.calc_btn_2).setOnClickListener(this::OnDigitClick);
        findViewById(R.id.calc_btn_3).setOnClickListener(this::OnDigitClick);
        findViewById(R.id.calc_btn_4).setOnClickListener(this::OnDigitClick);
        findViewById(R.id.calc_btn_5).setOnClickListener(this::OnDigitClick);
        findViewById(R.id.calc_btn_6).setOnClickListener(this::OnDigitClick);
        findViewById(R.id.calc_btn_7).setOnClickListener(this::OnDigitClick);
        findViewById(R.id.calc_btn_8).setOnClickListener(this::OnDigitClick);
        findViewById(R.id.calc_btn_9).setOnClickListener(this::OnDigitClick);

        findViewById(R.id.calc_btn_add).setOnClickListener(this::onOperationClick);
        findViewById(R.id.calc_btn_sub).setOnClickListener(this::onOperationClick);
        findViewById(R.id.calc_btn_mul).setOnClickListener(this::onOperationClick);
        findViewById(R.id.calc_btn_div).setOnClickListener(this::onOperationClick);
        findViewById(R.id.calc_btn_equal).setOnClickListener(this::onEqualsClick);

        findViewById(R.id.calc_btn_percent).setOnClickListener(this::onPercentClick);
        findViewById(R.id.calc_btn_sqrt).setOnClickListener(this::onSqrtClick);
        findViewById(R.id.calc_btn_sqr).setOnClickListener(this::onSquareClick);
        findViewById(R.id.calc_btn_inv).setOnClickListener(this::onInverseClick);
        findViewById(R.id.calc_btn_pm).setOnClickListener(this::onPlusMinusClick);
        findViewById(R.id.calc_btn_dot).setOnClickListener(this::onDotClick);

        findViewById(R.id.calc_btn_c).setOnClickListener(this::onClearClick);
        findViewById(R.id.calc_btn_ce).setOnClickListener(this::onClearEntryClick);
        findViewById(R.id.calc_btn_backspace).setOnClickListener(this::onBackspaceClick);

        findViewById(R.id.calc_btn_mc).setOnClickListener(this::onMemoryClear);
        findViewById(R.id.calc_btn_mr).setOnClickListener(this::onMemoryRecall);
        findViewById(R.id.calc_btn_mp).setOnClickListener(this::onMemoryAdd);
        findViewById(R.id.calc_btn_mm).setOnClickListener(this::onMemorySubtract);
    }
    private void calculateResult() {
        switch (pendingOperation) {
            case "+":
                currentNumber = lastNumber + currentNumber;
                break;
            case "-":
                currentNumber = lastNumber - currentNumber;
                break;
            case "×":
                currentNumber = lastNumber * currentNumber;
                break;
            case "÷":
                if (currentNumber != 0) currentNumber = lastNumber / currentNumber;
                else {
                    tvResult.setText("Error");
                    return;
                }
                break;
        }
        tvResult.setText(df.format(currentNumber));
        isNewNumber = true;
    }
    private void OnDigitClick(View view) {
        Button button = (Button) view;
        String digit = button.getText().toString();

        if (isNewNumber) {
            tvResult.setText(digit);
            isNewNumber = false;
        } else tvResult.setText(tvResult.getText().toString() + digit);
        currentNumber = Double.parseDouble(tvResult.getText().toString());
    }
    private void onOperationClick(View view) {
        Button button = (Button) view;
        if (!pendingOperation.isEmpty()) calculateResult();
        lastNumber = currentNumber;
        pendingOperation = button.getText().toString();
        tvExpression.setText(df.format(lastNumber) + " " + pendingOperation);
        isNewNumber = true;
    }
    private void onEqualsClick(View view) {
        if (!pendingOperation.isEmpty()) {
            tvExpression.setText(tvExpression.getText().toString() + " " + df.format(currentNumber) + " =");
            calculateResult();
            pendingOperation = "";
        }
    }
    private void onPercentClick(View view) {
        currentNumber = currentNumber / 100 * lastNumber;
        tvResult.setText(df.format(currentNumber));
    }
    private void onSqrtClick(View view) {
        if (currentNumber >= 0) {
            currentNumber = Math.sqrt(currentNumber);
            tvResult.setText(df.format(currentNumber));
        } else tvResult.setText("Error");
    }
    private void onSquareClick(View view) {
        currentNumber = currentNumber * currentNumber;
        tvResult.setText(df.format(currentNumber));
    }
    private void onInverseClick(View view) {
        if (currentNumber != 0) {
            currentNumber = 1 / currentNumber;
            tvResult.setText(df.format(currentNumber));
        } else tvResult.setText("Error");
    }
    private void onPlusMinusClick(View view) {
        currentNumber = -currentNumber;
        tvResult.setText(df.format(currentNumber));
    }
    private void onDotClick(View view) {
        String currentText = tvResult.getText().toString();
        if (!currentText.contains(".")) tvResult.setText(currentText + ".");
    }
    private void onClearClick(View view) {
        currentNumber = 0;
        lastNumber = 0;
        pendingOperation = "";
        tvResult.setText("0");
        tvExpression.setText("");
        isNewNumber = true;
    }
    private void onClearEntryClick(View view) {
        currentNumber = 0;
        tvResult.setText("0");
        isNewNumber = true;
    }
    private void onBackspaceClick(View view) {
        String currentText = tvResult.getText().toString();
        if (currentText.length() > 1) {
            currentText = currentText.substring(0, currentText.length() - 1);
            tvResult.setText(currentText);
            currentNumber = Double.parseDouble(currentText);
        } else {
            tvResult.setText("0");
            currentNumber = 0;
            isNewNumber = true;
        }
    }
    private void onMemoryAdd(View view) { memory += currentNumber; }
    private void onMemorySubtract(View view) { memory -= currentNumber; }
    private void onMemoryRecall(View view) {
        currentNumber = memory;
        tvResult.setText(df.format(memory));
        isNewNumber = true;
    }
    private void onMemoryClear(View view) { memory = 0; }
    private void onMemoryStore(View view) { memory = currentNumber; }
}