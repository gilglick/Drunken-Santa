package com.example.hw1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.hw1.view.HighScoreFragment;
import com.example.hw1.view.UserMapFragment;

public class HighScoreActivity extends AppCompatActivity implements HighScoreFragment.FragmentHighScoreListener{


    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private UserMapFragment mapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragments_holder);
        addFragments();
    }

    // This function add the game score fragment to the HighScoreActivity
    public void addFragments(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        HighScoreFragment highScoreFragment = new HighScoreFragment();
        mapFragment = new UserMapFragment();
        fragmentTransaction.add(R.id.highScoreLayout, highScoreFragment).add(R.id.mapLayout, mapFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        fragmentTransaction.addToBackStack(null);
        fragmentManager.popBackStack();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onGameUserInfoSent(GameUser user) {
        mapFragment.getGameUserInfo(user);
        mapFragment.setButtonText(user.getUserName());
    }


}
