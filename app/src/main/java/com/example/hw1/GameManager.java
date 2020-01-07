package com.example.hw1;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.example.hw1.view.GameView;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameManager {

    /** Fixed Range for the enemy's movements*/
    private final int Y_ENEMY_DISTANCE = 10;

    /** Variable that indicate when the level's up*/
    private final int LEVEL_SPEED_UP = 20;

    /** Initialize the starting speed of the game */
    private final int GAME_STARTING_SPEED = 25;

    /**The maximum speed of the last level the game can hold*/
    private final int MAX_GAME_SPEED = 5;

    /** Define the rum game timer's delay*/
    private final int DELAY = 0;

    /** Define the rum game timer's period*/
    private final int PERIOD = 1000;

    /**
     * Variables that managing the flow of the game
     */
    public volatile boolean playing = true;
    private int numOfLives = GamePlayFinals.NUM_OF_LIVES;
    private int gameScore = GamePlayFinals.INITIALIZED_SCORE;

    /**
     * All the views in the game
     */
    private GameCharacter player;
    private GameCharacter[] enemies;

    /**
     * Handle the game runtime events
     */
    private Handler handler = new Handler();
    private Timer scoreTask = new Timer();
    private Timer gameTask = new Timer();

    /**
     * Use to randomize different locations in the game
     */
    private Random rand = new Random();

    /**
     * Hold the gamePlayActivity context
     */
    private Context context;

    /**
     * Hold the GameView instance to handle all event in the game
     */
    private GameView gamePlayView;

    /**
     * Sound effect in the game
     */
    private MediaPlayer boomEffect;
    private MediaPlayer addLifeSoundEffect;

    // Object hold all the data of the user whose playing the game
    private GameUser gameUser;


    /**
     * View Element in the game
     */
    private ImageView heart;
    private ImageView coins;

    /* Variable holding if life exist right now or not */
    private volatile boolean lifeExist = false;

    /* Variable holding if life exist right now or not */
    private volatile boolean coinExist = false;

    /* Variable holding the game level */
    private int gameLevel;

    /* Variable holding the game speed */
    private int gameSpeed = GAME_STARTING_SPEED;



    public GameManager(Context context, GameView gameView, GameUser gameUser, int gameLevel) {
        this.context = context;
        this.gamePlayView = gameView;
        this.gameUser = gameUser;
        this.gameLevel = gameLevel;
        boomEffect = MediaPlayer.create(context, R.raw.hit);
        addLifeSoundEffect = MediaPlayer.create(context, R.raw.get_life);
    }

    public void generateGame() {
        player = gamePlayView.createPlayer(gameUser);
        enemies = gamePlayView.createEnemies(gameLevel);
        heart = gamePlayView.createExtraLife();
        setLives(gamePlayView.getLifeList(), gameUser.getPlayerCharacter());
        coins = gamePlayView.createMoneyBag();
        runGame(gameSpeed);
    }

    private void runGame(int speed) {
        reScheduleTimer(speed);

        scoreTask.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!playing) {
                    handler.removeCallbacksAndMessages(null);
                    return;
                }
                handler.post(() -> {
                    gameScore += 1;
                    gamePlayView.getScore().setText("Score " + gameScore);
                    if (gameScore % (LEVEL_SPEED_UP - gameLevel * 2) == 0) {
                        gameTask.cancel();
                        gameSpeed -= 2;
                        reScheduleTimer(gameSpeed);
                    }
                });
            }
        }, DELAY, PERIOD);
    }

    private void reScheduleTimer(int speed) {
        if (speed < MAX_GAME_SPEED) {
            gameSpeed = MAX_GAME_SPEED;
        }

        gameTask = new Timer();
        TimerTask gamePlay = new TimerTask() {

            @Override
            public void run() {
                if (!playing) {
                    handler.removeCallbacksAndMessages(null);
                    return;
                }
                handler.post(() -> {
                    moveEnemy();
                    if (detectEnemyCollision()) {
                        boomEffect.start();
                        playerGetsHit();
                    }
                    randomizeLife();
                    randomizeCoins();
                });
            }
        };
        gameTask.schedule(gamePlay, DELAY, gameSpeed);
    }

    /**
     * Create random location which the enemy is going to be positioned
     */
    private void setRandomLoc(ImageView imageView) {
        imageView.setX(rand.nextInt(gamePlayView.getWidth() / GamePlayFinals.CALC_FRAME) * GamePlayFinals.CALC_FRAME);
        imageView.setY(rand.nextInt(GamePlayFinals.BOTTOM_BOUND) - GamePlayFinals.TOP_BOUND);
    }

    /**
     * This method make the main character move right or left on the screen
     */
    public boolean moveCharacter(MotionEvent event) {
        float centerX = player.getX() + (float) (GamePlayFinals.PLAYER_WIDTH / 2);
        boolean validRight = (event.getX() >= centerX) && (event.getX() <= centerX + GamePlayFinals.PLAYER_WIDTH * 2);
        boolean validLeft = (event.getX() <= centerX) && event.getX() >= (centerX - GamePlayFinals.PLAYER_WIDTH * 2);
        if ((event.getAction() == MotionEvent.ACTION_MOVE) && (event.getX() <= gamePlayView.getGamePlayFrame().getWidth() - (float) (GamePlayFinals.PLAYER_WIDTH / 2))
                && (event.getX() >= (float) (GamePlayFinals.PLAYER_WIDTH / 2)) && (validRight || validLeft)) {
            player.setX(event.getX() - (float) GamePlayFinals.PLAYER_WIDTH / 2);
            return true;
        }
        return false;
    }

    private void setLives(ImageView[] lifeList, int player) {
        numOfLives = GamePlayFinals.NUM_OF_LIVES;
        for (ImageView life : lifeList) {
            life.setBackgroundResource(player);
        }
    }


    /**
     * Move each  enemy by 10 pixels on each frame
     */
    private void moveEnemy() {
        for (GameCharacter enemy : enemies) {
            moveDown(enemy, Y_ENEMY_DISTANCE);
        }
        for (GameCharacter enemy : enemies) {
            if (enemy.getY() > gamePlayView.getGamePlayFrame().getHeight()) {
                setRandomLoc(enemy);
            }
        }
    }

    /**
     * This method check the position of player and the enemy and check for collide
     *
     * @return boolean if collision detected
     */
    private boolean detectEnemyCollision() {
        for (GameCharacter enemy : enemies) {
            if (collisionDetection(enemy, player)) {
                setRandomLoc(enemy);
                return true;
            }
        }
        return false;
    }

    /**
     * This method create animation if the player gets hit and check his number of lives every time he got hit
     */
    private void playerGetsHit() {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(player, "rotation", 0f, 20f, 0f, -20f, 0f); // rotate o degree then 20 degree and so on for one loop of rotation.
        rotate.setRepeatCount(6);
        rotate.setDuration(30);
        rotate.start();
        if (numOfLives > 0) {
            gamePlayView.getLifeList()[--numOfLives].setVisibility(View.INVISIBLE);
        }
        if (numOfLives == 0) {
            gameOver();
        }
    }

    private void moveDown(ImageView imageView, int speed) {
        imageView.setY(imageView.getY() + speed);
    }

    /**
     * Create random element(heart or coins) to the player and checks
     */
    private boolean raffleElement(ImageView imageView,int raffleNumber) {
        if(rand.nextInt(raffleNumber) == 10){
        setRandomLoc(imageView);
            imageView.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    private void checkElement(ImageView imageView) {
        if (imageView.getY() > gamePlayView.getGamePlayFrame().getHeight()) {
            imageView.setVisibility(View.INVISIBLE);
            if (imageView == heart) {
                lifeExist = false;
            }
            if(imageView == coins){
                coinExist = false;
            }
        }

    }

    /**
     * Detect collision of the player with the elements (heart or coins).
     */
    private boolean elementCollision(ImageView imageView) {
        if (imageView.getVisibility() == View.VISIBLE && collisionDetection(imageView, player)) {
            imageView.setY(0);
            imageView.setVisibility(View.INVISIBLE);
            return true;
        }
        return false;
    }

    /**
     * Move the life down on the screen
     */
    private void heartMoveDown() {
        checkElement(heart);
        moveDown(heart, 10);
    }

    /**
     * If collision detected the user gets another life, else the user gets nothing.
     * If the user have already three life he also gets nothing.
     */
    private void addPlayerLife() {
        if (numOfLives < GamePlayFinals.NUM_OF_LIVES) {
            gamePlayView.getLifeList()[numOfLives].setVisibility(View.VISIBLE);
            addLifeSoundEffect.start();
            numOfLives++;
        }
    }

    private void randomizeLife() {
        if (!lifeExist)
            lifeExist = raffleElement(heart,800);
        if (lifeExist && elementCollision(heart)) {
            addPlayerLife();
        } else {
            heartMoveDown();
        }
    }

    /**
     * Move the life down on the screen
     */
    private void coinsMoveDown() {
        checkElement(coins);
        moveDown(coins, 7);
    }

    /**
     * If collision detected the user gets another life, else the user gets nothing.
     * If the user have already three life he also gets nothing.
     */
    private void addPlayerCoins() {
        gameScore += 10;
    }

    private void randomizeCoins() {
        if (!coinExist)
            coinExist = raffleElement(coins,500);
        if (coinExist && elementCollision(coins)) {
            addPlayerCoins();
        } else {
            coinsMoveDown();
        }
    }

    private boolean collisionDetection(ImageView collideElement, ImageView player) {
        return collideElement.getY() + collideElement.getHeight() - 50 > player.getY()
                && collideElement.getY() <= player.getY()
                && (collideElement.getX() >= player.getX() &&
                collideElement.getX() <= player.getX() + player.getWidth()
                || collideElement.getX() <= player.getX()
                && collideElement.getX() + collideElement.getWidth() >= player.getX());

    }

    /**
     * Accelerometer working partially
     */
    public void playerAccelerometer(float x) {
        if (player.getX() <= gamePlayView.getWidth() - player.getWidth() && player.getX() >= 0) {
            player.setX(player.getX() - x);
            if (player.getX() >= gamePlayView.getWidth() - player.getWidth()) {
                player.setX(gamePlayView.getWidth() - player.getWidth());
            } else if (player.getX() < 0) {
                player.setX(0);
            }
        }
    }

    public void setGameStatus(boolean playing) {
        this.playing = playing;
    }

    /**
     * If game over detected move to game over view
     */
    private void gameOver() {
        gameUser.setScore(gameScore);
        playing = false;
        Intent intent = new Intent(context, EndGameActivity.class);
        intent.putExtra(context.getString(R.string.user_object), gameUser);
        context.startActivity(intent);
        ((GamePlayActivity) context).finish();

    }
}