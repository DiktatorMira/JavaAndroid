package com.example.android213;
import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private final Random random = new Random();
    private TextView tvScore, tvBest;
    private long score, bestScore;
    private final int N = 4;
    private final int[][] tiles = new int[N][N];
    private final TextView[][] tvTiles = new TextView[N][N];

    @SuppressLint({"ClickableViewAccessibility","DiscouragedApi"})
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game_layout_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) tvTiles[i][j] = findViewById(getResources().getIdentifier("game_tv_tile_" + i + j, "id", getPackageName()));
        }
        tvScore = findViewById(R.id.game_tv_score);
        tvBest = findViewById(R.id.game_tv_best);

        LinearLayout gameField = findViewById(R.id.game_layout_field);
        gameField.post(() -> {
            int vw = this.getWindow().getDecorView().getWidth(), fieldMargin = 20;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    vw - 2 * fieldMargin,
                    vw - 2 * fieldMargin
            );
            layoutParams.setMargins(fieldMargin, fieldMargin, fieldMargin, fieldMargin);
            layoutParams.gravity = Gravity.CENTER;
            gameField.setLayoutParams(layoutParams);
        });

        gameField.setOnTouchListener( new OnSwipeListener( GameActivity.this ) {
            @Override public void onSwipeBottom() {
                Toast.makeText(GameActivity.this, "onSwipeBottom", Toast.LENGTH_SHORT).show();
            }
            @Override public void onSwipeLeft() {
                Toast.makeText(GameActivity.this, "onSwipeLeft", Toast.LENGTH_SHORT).show();
            }
            @Override public void onSwipeRight() {
                if (MoveRight()) {
                    SpawnTile();
                    UpdateField();
                } else Toast.makeText(GameActivity.this, "NO right move", Toast.LENGTH_SHORT).show();
            }
            @Override public void onSwipeTop() {
                Toast.makeText(GameActivity.this, "onSwipeTop", Toast.LENGTH_SHORT).show();
            }
        });
        bestScore = 0L;
        StartNewGame();
    }
    private void StartNewGame() {
        score = 0L;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) tiles[i][j] = 0;
        }
        SpawnTile();
        SpawnTile();
        UpdateField();
    }
    private boolean MoveRight() {
        boolean result = ShiftRight();
        for (int i = 0; i < N; i++) {
            for (int j = N - 1; j > 0; j--) {
                if (tiles[i][j] == tiles[i][j - 1] && tiles[i][j] != 0) {
                    tiles[i][j] += 2;
                    tiles[i][j - 1] = 0;
                    score += tiles[i][j];
                    result = true;
                }
            }
        }
        if (result) ShiftRight();
        return result;
    }
    private boolean ShiftRight() {
        boolean res = false;
        for (int i = 0; i < N; i++) {
            boolean wasReplase;
            do {
                wasReplase = false;
                for (int j = 0; j < N; j++) {
                    if (tiles[i][j] != 0 && tiles[i][j+1] == 0) {
                        tiles[i][j+1] = tiles[i][j];
                        tiles[i][j] = 0;
                        wasReplase = true;
                        res = true;
                    }
                }
            } while (wasReplase);
        }
        return res;
    }
    private boolean SpawnTile() {
        List<Integer> freeTiles = new ArrayList<>(N * N);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) freeTiles.add(N * i + j);
            }
        }
        if (freeTiles.isEmpty()) return false;
        int k = freeTiles.get(random.nextInt(freeTiles.size())), i = k / N, j = k % N;
        tiles[i][j] = random.nextInt(10) == 0 ? 4 : 2;
        return true;
    }
    @SuppressLint("DiscouragedApi") private void UpdateField() {
        tvScore.setText(getString(R.string.game_tv_score_tpl, ScoreToString(score)));
        tvBest.setText(getString(R.string.game_tv_best_tpl, ScoreToString(bestScore)));

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int tileValue = tiles[i][j];
                tvTiles[i][j].setText(ScoreToString(tileValue));
                float textSize;
                if (tileValue < 10) textSize = 64.0f;
                else if (tileValue < 100)  textSize = 56.0f;
                else if (tileValue < 1000) textSize = 48.0f;
                else if (tileValue < 10000) textSize = 40.0f;
                else textSize = 32.0f;

                tvTiles[i][j].setTextSize(textSize);
                tvTiles[i][j].setTextColor(getResources().getColor(getResources().getIdentifier(
                        String.format(Locale.ROOT, "game_tile%d_fg", tileValue),
                        "color", getPackageName()), getTheme())
                );
                tvTiles[i][j].getBackground().setColorFilter(getResources().getColor(getResources().getIdentifier(
                        String.format(Locale.ROOT, "game_tile%d_bg", tileValue),
                        "color", getPackageName()), getTheme()), PorterDuff.Mode.SRC_ATOP
                );
            }
        }
    }
    private String ScoreToString(long value) { return String.valueOf(value); }
}