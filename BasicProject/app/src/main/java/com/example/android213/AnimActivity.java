package com.example.android213;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AnimActivity extends AppCompatActivity {
    private Animation alphaAnimtion, scaleAnimation, scaleAnimation2;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anim);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        alphaAnimtion = AnimationUtils.loadAnimation(this, R.anim.demo_alpha);
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.demo_scale);
        scaleAnimation2 = AnimationUtils.loadAnimation(this, R.anim.demo_scale2);
        findViewById( R.id.anim_v_alpha ).setOnClickListener( this::onAlphaClick );
        findViewById( R.id.anim_v_scale ).setOnClickListener( this::onScaleClick );
        findViewById( R.id.anim_v_scale_2 ).setOnClickListener( this::onScale2Click );
    }
    private void onAlphaClick(View view) {
        view.startAnimation(alphaAnimtion);
    }
    private void onScaleClick(View view) {
        view.startAnimation(scaleAnimation);
    }
    private void onScale2Click(View view) {
        view.startAnimation(scaleAnimation2);
    }
}