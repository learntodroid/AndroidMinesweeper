package com.learntodroid.androidminesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnCellClickListener {
    public static final long TIMER_LENGTH = 999000L;    // 999 seconds in milliseconds
    public static final int BOMB_COUNT = 10;
    public static final int GRID_SIZE = 10;

    private MineGridRecyclerAdapter mineGridRecyclerAdapter;
    private RecyclerView grid;
    private TextView smiley, timer, flag, flagsLeft;
    private MineSweeperGame mineSweeperGame;
    private CountDownTimer countDownTimer;
    private int secondsElapsed;
    private boolean timerStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid = findViewById(R.id.activity_main_grid);
        grid.setLayoutManager(new GridLayoutManager(this, 10));

        timer = findViewById(R.id.activity_main_timer);
        timerStarted = false;
        countDownTimer = new CountDownTimer(TIMER_LENGTH, 1000) {
            public void onTick(long millisUntilFinished) {
                secondsElapsed += 1;
                timer.setText(String.format("%03d", secondsElapsed));
            }

            public void onFinish() {
                mineSweeperGame.outOfTime();
                Toast.makeText(getApplicationContext(), "Game Over: Timer Expired", Toast.LENGTH_SHORT).show();
                mineSweeperGame.getMineGrid().revealAllBombs();
                mineGridRecyclerAdapter.setCells(mineSweeperGame.getMineGrid().getCells());
            }
        };

        flagsLeft = findViewById(R.id.activity_main_flagsleft);

        mineSweeperGame = new MineSweeperGame(GRID_SIZE, BOMB_COUNT);
        flagsLeft.setText(String.format("%03d", mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount()));
        mineGridRecyclerAdapter = new MineGridRecyclerAdapter(mineSweeperGame.getMineGrid().getCells(), this);
        grid.setAdapter(mineGridRecyclerAdapter);

        smiley = findViewById(R.id.activity_main_smiley);
        smiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mineSweeperGame = new MineSweeperGame(GRID_SIZE, BOMB_COUNT);
                mineGridRecyclerAdapter.setCells(mineSweeperGame.getMineGrid().getCells());
                timerStarted = false;
                countDownTimer.cancel();
                secondsElapsed = 0;
                timer.setText(R.string.default_count);
                flagsLeft.setText(String.format("%03d", mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount()));
            }
        });

        flag = findViewById(R.id.activity_main_flag);
        flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mineSweeperGame.toggleMode();
                if (mineSweeperGame.isFlagMode()) {
                    GradientDrawable border = new GradientDrawable();
                    border.setColor(0xFFFFFFFF);
                    border.setStroke(1, 0xFF000000);
                    flag.setBackground(border);
                } else {
                    GradientDrawable border = new GradientDrawable();
                    border.setColor(0xFFFFFFFF);
                    flag.setBackground(border);
                }
            }
        });
    }

    @Override
    public void cellClick(Cell cell) {
        mineSweeperGame.handleCellClick(cell);

        flagsLeft.setText(String.format("%03d", mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount()));

        if (!timerStarted) {
            countDownTimer.start();
            timerStarted = true;
        }

        if (mineSweeperGame.isGameOver()) {
            countDownTimer.cancel();
            Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
            mineSweeperGame.getMineGrid().revealAllBombs();
        }

        if (mineSweeperGame.isGameWon()) {
            countDownTimer.cancel();
            Toast.makeText(getApplicationContext(), "Game Won", Toast.LENGTH_SHORT).show();
            mineSweeperGame.getMineGrid().revealAllBombs();
        }

        mineGridRecyclerAdapter.setCells(mineSweeperGame.getMineGrid().getCells());
    }
}