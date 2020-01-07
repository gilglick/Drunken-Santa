package com.example.hw1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hw1.utilities.DataBase;
import com.example.hw1.utilities.MySharedPreferences;
import com.example.hw1.view.EndGameView;

import java.util.Comparator;

public class EndGameActivity extends AppCompatActivity {

    /* Playing the end game song in the game over screen */
    private MediaPlayer endGameSong;

    /* Playing the end game song in the game over screen */
    private GameUser gameUser;

    /* Database holding all data about the User */
    private DataBase gameDataBase;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configGameWindow();
        endGameSong = MediaPlayer.create(this, R.raw.endsound);
        EndGameView endGameView = new EndGameView(this);
        setContentView(endGameView);
        gameUser = getIntent().getParcelableExtra(getString(R.string.user_object));
        gameDataBase = new DataBase(this);
        insertDataToDataBase();
        MySharedPreferences shared = new MySharedPreferences(getApplicationContext());
        shared.add(gameUser, (u,v)-> {
            if (u.getScore() == v.getScore())
                return 0;
            return u.getScore() < v.getScore() ? 1 : -1;
        });

        /* On click event, which transfer the client play another game*/
        endGameView.getPlayAgain().setOnClickListener((e) -> {
            Intent intent = new Intent(this, GamePlayActivity.class);
            intent.putExtra(getString(R.string.user_object),gameUser);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        /* On click event, which transfer the client to main activity*/
        endGameView.getExitGame().setOnClickListener((e) -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        /* On click event, which transfer the client to HighScoreActivity*/
        endGameView.getHighScore().setOnClickListener((e)->{
            Intent intent = new Intent(this, HighScoreActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });
    }

    /**
     * This method configure the window in the game.
     * The game will be displayed on full screen.
     */
    public void configGameWindow() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public void insertDataToDataBase() {
        if (gameDataBase.addData(gameUser)) {
            Toast.makeText(this, "Data insert successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data insert unsuccessfully!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog,id)->{
                    if (endGameSong != null) { endGameSong.stop(); }
                    finish();
                })
                .setNegativeButton("No", (dialog,id)-> dialog.cancel()).show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (endGameSong != null) {
            endGameSong.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (endGameSong != null) {
            endGameSong.start();
        }
    }
}