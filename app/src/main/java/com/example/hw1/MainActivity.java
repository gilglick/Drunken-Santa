package com.example.hw1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.hw1.GamePlayFinals.PLAYER_HEIGHT;
import static com.example.hw1.GamePlayFinals.PLAYER_WIDTH;


public class MainActivity extends AppCompatActivity {

    /* User location */
    private Location userLocation;

    /* Playing the game intro song in the main screen */
    private MediaPlayer introSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configGameWindow();
        setContentView(R.layout.activity_main);
        requestPermission();
        accessClientLocation();

        introSong = MediaPlayer.create(this, R.raw.introsound);
        introSong.setLooping(true);
        SettingDialog setting = new SettingDialog(this, introSong);
        Button startGame = findViewById(R.id.playButton);
        LinearLayout playersLayout = findViewById(R.id.gamePlayers);
        Button settingBtn = findViewById(R.id.settingButton);
        EditText userNameEditText = findViewById(R.id.userName);
        Button highScore = findViewById(R.id.highScoreButton);


         /* Start event, this event will trigger the game and gives the
            user list of player which will he could choose.
         */
        startGame.setOnClickListener(e -> {
            if (userNameEditText.getText().toString().trim().length() > 0) {
                startGame.setEnabled(false);
                userNameEditText.setEnabled(false);
                int[] images = {R.drawable.dragonsanta, R.drawable.elfsanta, R.drawable.santa};
                ImageView[] players = new ImageView[images.length];
                for (int i = 0; i < players.length; i++) {
                    final int j = i;
                    players[j] = new GameCharacter(this, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT);
                    players[j].setBackgroundResource(images[j]);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(GamePlayFinals.PLAYER_WIDTH, GamePlayFinals.PLAYER_HEIGHT);
                    params.setMargins(50, 50, 50, 50);
                    players[j].setLayoutParams(params);
                    playersLayout.addView(players[j]);
                    players[j].setOnClickListener(v -> {
                        if (userLocation == null) {
                            setDefaultLocation();
                        }
                        GameUser gameUser = new GameUser(userNameEditText.getText().toString(), userLocation.getLongitude(), userLocation.getLatitude(), GamePlayFinals.INITIALIZED_SCORE, images[j]);
                        startActivity(new Intent(getApplicationContext(), GamePlayActivity.class)
                                .putExtra(getString(R.string.user_object), gameUser)
                                .putExtra(getString(R.string.music), setting.getSound())
                                .putExtra(getString(R.string.accelerometer), setting.getAccelerometer())
                                .putExtra(getString(R.string.game_level), setting.getChosenLevel()));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    });
                }
            } else {
                Toast.makeText(this, R.string.enter_name, Toast.LENGTH_SHORT).show();
            }
        });

        highScore.setOnClickListener(score -> startActivity(new Intent(getApplicationContext(), HighScoreActivity.class)));
        settingBtn.setOnClickListener(e -> setting.show());
    }

    private void setDefaultLocation() {
        userLocation = new Location(getString(R.string.provider));
        userLocation.setLatitude(GamePlayFinals.AFEKA_LATITUDE);
        userLocation.setLongitude(GamePlayFinals.AFEKA_LONGITUDE);
    }

    /**
     * This method configure the window in the game, The game will be displayed on full screen.
     */
    private void configGameWindow() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void accessClientLocation() {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION);
        client.getLastLocation().addOnSuccessListener(MainActivity.this, location -> {
            if (location != null)
                userLocation = location;
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.exit_check)
                .setCancelable(false)
                .setPositiveButton(R.string.yes_option, (dialog, listener) -> {
                    finish();
                    if (introSong != null) {
                        introSong.stop();
                    }
                })
                .setNegativeButton(R.string.no_option, (dialog, listener) -> dialog.cancel()).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (introSong != null) {
            introSong.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (introSong != null) {
            introSong.pause();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (imm != null && view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

}