package com.example.hw1;

import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class GamePlayFinals {

    /**
     * Specify fixed size for the player image height
     */
    public static final int PLAYER_HEIGHT = 175;

    /**
     * Specify fixed size for the player image width
     */
    public static final int PLAYER_WIDTH = 150;

    /**
     * Define the number of lives the user start with
     */
    public static final int NUM_OF_LIVES = 3;

    /**
     * Define the bottom bound which calculated when positioning the views enemies
     */
    public static final int BOTTOM_BOUND = 1000;

    /**
     * Define the top bound which calculated when positioning the views enemies
     */
    public static final int TOP_BOUND = 1400;

    /**
     * Helper variable for calculation of top bound and bottom bound
     */
    public static final int CALC_FRAME = 180;

    /**
     * Initializing the score of the player to 0
     */
    public static final int INITIALIZED_SCORE = 0;

    /**
     * Game levels variables
     */
    public static final int EASY = 0;
    public static final int MEDIUM = 1;
    public static final int HARD = 2;

    public static final int LINEAR_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    public static final int LINEAR_WRAPPED = LinearLayout.LayoutParams.WRAP_CONTENT;
    public static final int FRAME_PARENT = FrameLayout.LayoutParams.MATCH_PARENT;
    public static final int FRAME_WRAPPED = FrameLayout.LayoutParams.WRAP_CONTENT;

    /* Variables holding Afeka longitude and latitude for default value location */
    public static final double AFEKA_LONGITUDE = 34.817816499999935;
    public static final double AFEKA_LATITUDE = 32.1133371;


//    // GOOGLE MAPS CONSTANTS
//    public static final int ERROR_DIALOG_REQUEST = 9001;
//    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;
//    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9003;

}