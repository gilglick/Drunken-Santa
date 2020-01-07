package com.example.hw1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;


public class SettingDialog extends Dialog {

    private int level = GamePlayFinals.EASY;
    private boolean soundOn = true;
    private boolean accelerometerIsOn = false;
    private MediaPlayer gameSong;

    /**
     * Define the number of game level options
     */
    public final int GAME_LEVELS_OPTIONS = 3;

    public SettingDialog(@NonNull Context context, MediaPlayer gameSong) {
        super(context);
        this.gameSong = gameSong;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        ImageView musicImage = findViewById(R.id.dialogMusicImage);
        Button howToPlayButton = findViewById(R.id.howToPlayButton);
        Button gameLevelButton = findViewById(R.id.levelButton);
        Button producer = findViewById(R.id.producerButton);
        Button accelerometer = findViewById(R.id.accelerometer);

        musicImage.setOnClickListener(r -> {
            soundOn = !soundOn;
            if (!soundOn) {
                musicImage.setBackgroundResource(R.drawable.soundoff);
                gameSong.pause();
            } else {
                musicImage.setBackgroundResource(R.drawable.soundon);
                gameSong.seekTo(0);
                gameSong.start();
            }
        });

        howToPlayButton.setOnClickListener(play ->
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.dialog_title)
                        .setMessage(R.string.dialog_msg)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show());

        gameLevelButton.setOnClickListener(x -> {
            switch (++level % GAME_LEVELS_OPTIONS) {
                case GamePlayFinals.EASY:
                    gameLevelButton.setText(R.string.level_easy);
                    break;
                case GamePlayFinals.MEDIUM:
                    gameLevelButton.setText(R.string.level_medium);
                    break;
                case GamePlayFinals.HARD:
                    gameLevelButton.setText(R.string.level_hard);
                    break;
                default:
                    break;
            }
            level %= GAME_LEVELS_OPTIONS;
        });


        producer.setOnClickListener(prod -> new AlertDialog.Builder(getContext())
                .setTitle(R.string.Producers)
                .setMessage(R.string.string_producer)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show());

        accelerometer.setOnClickListener(acc -> {
            accelerometerIsOn = !accelerometerIsOn;
            if (accelerometerIsOn) {
                Toast.makeText(getContext(), "Accelerometer ON", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Accelerometer OFF ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getChosenLevel() {
        return level;
    }

    public boolean getSound() {
        return soundOn;
    }

    public boolean getAccelerometer() {
        return accelerometerIsOn;
    }
}