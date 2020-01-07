package com.example.hw1;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.hw1.R;
import com.example.hw1.GamePlayFinals;

public class EndGameView extends LinearLayout  {


    private Button playAgain;

    private Button exitGame;

    private Button highScore;

    public EndGameView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.game_background);
        setGravity(Gravity.CENTER);
        configGameOverLayout();
    }

    /**
     * Configuration of GameOver Layout which consist of LinearLayout
     * His child are TextView to hold "Game Over" text, Play button to play the game again
     * and exit button which terminates the game
      */
    public void configGameOverLayout() {
        LinearLayout gameOverLayout = new LinearLayout(getContext());
        gameOverLayout.setLayoutParams(new LinearLayout.LayoutParams(GamePlayFinals.LINEAR_WRAPPED,GamePlayFinals.LINEAR_WRAPPED));
        gameOverLayout.setOrientation(LinearLayout.VERTICAL);
        gameOverLayout.setGravity(Gravity.CENTER);
        gameOverLayout.addView(configGameOverTextView());
        gameOverLayout.addView(configPlayButton());
        gameOverLayout.addView(configExitButton());
        gameOverLayout.addView(highScoreButton());
        addView(gameOverLayout);
    }

    /**
     * Configure play button
     * @return Button which will be placed on his parent gameOverView
     */
    private Button configPlayButton() {
        playAgain = new Button(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 20, 20, 20);
        playAgain.setBackgroundResource(R.drawable.custom_endgame_button);
        playAgain.setTextColor(Color.WHITE);
        playAgain.setText(R.string.play_again);
        playAgain.setLayoutParams(new LinearLayout.LayoutParams(300, 100));
        playAgain.setLayoutParams(params);
        return playAgain;
    }

    /**
     * Configure exit button
     * @return Button which will be placed on his parent gameOverView
     */
    private Button configExitButton() {
        exitGame = new Button(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 20, 20, 20);
        exitGame.setBackgroundResource(R.drawable.custom_endgame_button);
        exitGame.setTextColor(Color.WHITE);
        exitGame.setText(R.string.main_menu);
        exitGame.setLayoutParams(new LinearLayout.LayoutParams(300, 100));
        exitGame.setLayoutParams(params);
        return exitGame;
    }

    /**
     * Configure high score button
     * @return Button which will be placed on his parent gameOverView
     */
    private Button highScoreButton() {
        highScore = new Button(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 20, 20, 20);
        highScore.setBackgroundResource(R.drawable.custom_endgame_button);
        highScore.setTextColor(Color.WHITE);
        highScore.setText(R.string.high_score);
        highScore.setLayoutParams(new LinearLayout.LayoutParams(300, 100));
        highScore.setLayoutParams(params);
        return highScore;
    }

    /**
     * Configure the display on the screen of "Game Over"
     * @return TextView which will be placed on his parent gameOverView
     */
    private TextView configGameOverTextView() {
        TextView gameOverText = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(50, 20, 20, 20);
        gameOverText.setLayoutParams(params);
        gameOverText.setTextColor(Color.WHITE);
        gameOverText.setText(R.string.gameOver);
        gameOverText.setTextSize(50);
        params.setMargins(20, 20, 20, 20);
        return gameOverText;
    }



    // Return play button
    public Button getPlayAgain() {
        return playAgain;
    }

    // Return exit button
    public Button getExitGame() {
        return exitGame;
    }

    // Return high score button
    public Button getHighScore(){
        return highScore;
    }
}
