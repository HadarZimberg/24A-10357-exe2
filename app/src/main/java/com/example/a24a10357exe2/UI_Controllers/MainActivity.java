package com.example.a24a10357exe2.UI_Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a24a10357exe2.Interfaces.TiltCallback;
import com.example.a24a10357exe2.Logic.DataManager;
import com.example.a24a10357exe2.Logic.GameManager;
import com.example.a24a10357exe2.R;
import com.example.a24a10357exe2.Utilities.SignalManager;
import com.example.a24a10357exe2.Utilities.Sound;
import com.example.a24a10357exe2.Utilities.TiltDetector;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final long DELAY = 1000;
    public static final String KEY_SPEED = "KEY_SPEED";
    public static final String KEY_MODE = "KEY_MODE";
    private ShapeableImageView main_IMG_background;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView[] main_IMG_chickens;
    private ShapeableImageView[] main_IMG_money;
    private ShapeableImageView[] main_IMG_bearrys;
    private ShapeableImageView[][] main_IMG_chicken_matrix;
    private ShapeableImageView[][] main_IMG_money_matrix;
    private MaterialButton main_BTN_left;
    private MaterialButton main_BTN_right;
    private MaterialTextView main_LBL_score;
    private MaterialTextView main_LBL_odometer;
    private Timer timer;
    private Random random = new Random();
    private long startTime;
    private long speed;
    private boolean isButtonsMode;
    private boolean timerOn = false;
    private GameManager gameManager;
    private Sound sound = new Sound(this);
    private SignalManager signalManager;
    private TiltDetector tiltDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        Intent menuScreen = getIntent();
        speed = menuScreen.getLongExtra(KEY_SPEED, DELAY);
        isButtonsMode = menuScreen.getBooleanExtra(KEY_MODE, true);
        selectMode();

        main_IMG_background.setImageResource(R.drawable.clouds);

        gameBeginning();

        gameManager = new GameManager(main_IMG_hearts.length);

        SignalManager.init(getApplicationContext());
        signalManager = SignalManager.getInstance();

        startTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
        if (!isButtonsMode)
            tiltDetector.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
        if (!isButtonsMode)
            tiltDetector.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer();
        if (!isButtonsMode)
            tiltDetector.stop();
    }

    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
        sound.stopSound();
        if (!isButtonsMode)
            tiltDetector.stop();
    }

    private void selectMode() {
        if (!isButtonsMode) {
            main_BTN_left.setVisibility(View.INVISIBLE);
            main_BTN_right.setVisibility(View.INVISIBLE);
            initTiltDetector();
        }
        else {
            main_BTN_left.setOnClickListener(view -> moveToLeft());
            main_BTN_right.setOnClickListener(view -> moveToRight());
        }
    }

    private void initTiltDetector() {
        tiltDetector = new TiltDetector(this, new TiltCallback() {
            @Override
            public void tiltLeft() {
                moveToLeft();
            }

            @Override
            public void tiltRight() {
                moveToRight();
            }
        });
    }

    private void moveToLeft() {
        int place = getPlace();
        if (place != 0) {
            main_IMG_bearrys[place - 1].setVisibility(View.VISIBLE);
            main_IMG_bearrys[place].setVisibility(View.INVISIBLE);
        }
    }

    private void moveToRight() {
        int place = getPlace();
        if (place != main_IMG_bearrys.length - 1) {
            main_IMG_bearrys[place + 1].setVisibility(View.VISIBLE);
            main_IMG_bearrys[place].setVisibility(View.INVISIBLE);
        }
    }

    private int getPlace() {
        int place = -1;
        for (int i = 0; i < main_IMG_bearrys.length; i++)
            if (main_IMG_bearrys[i].getVisibility() == View.VISIBLE)
                place = i;
        return place;
    }

    private void gameBeginning() { //Initialize the visibility of the objects
        randomColumn(); //Selects the first column from which the object will fall

        main_IMG_bearrys[main_IMG_bearrys.length / 2].setVisibility(View.VISIBLE); //At the beginning the player is placed in the center
        for (int i = 0; i < main_IMG_bearrys.length; i++)
            if (i != main_IMG_bearrys.length / 2)
                main_IMG_bearrys[i].setVisibility(View.INVISIBLE);

        for (int i = 0; i < main_IMG_chicken_matrix.length; i++)
            for (int j = 0; j < main_IMG_chicken_matrix[i].length; j++) {
                main_IMG_chicken_matrix[i][j].setVisibility(View.INVISIBLE);
                main_IMG_money_matrix[i][j].setVisibility(View.INVISIBLE);
            }
    }

    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);

        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };

        main_IMG_chickens = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_chicken1),
                findViewById(R.id.main_IMG_chicken2),
                findViewById(R.id.main_IMG_chicken3),
                findViewById(R.id.main_IMG_chicken4),
                findViewById(R.id.main_IMG_chicken5)
        };

        main_IMG_chicken_matrix = new ShapeableImageView[][]{
                {findViewById(R.id.main_IMG_chicken1_1), findViewById(R.id.main_IMG_chicken2_1), findViewById(R.id.main_IMG_chicken3_1), findViewById(R.id.main_IMG_chicken4_1), findViewById(R.id.main_IMG_chicken5_1)},
                {findViewById(R.id.main_IMG_chicken1_2), findViewById(R.id.main_IMG_chicken2_2), findViewById(R.id.main_IMG_chicken3_2), findViewById(R.id.main_IMG_chicken4_2), findViewById(R.id.main_IMG_chicken5_2)},
                {findViewById(R.id.main_IMG_chicken1_3), findViewById(R.id.main_IMG_chicken2_3), findViewById(R.id.main_IMG_chicken3_3), findViewById(R.id.main_IMG_chicken4_3), findViewById(R.id.main_IMG_chicken5_3)},
                {findViewById(R.id.main_IMG_chicken1_4), findViewById(R.id.main_IMG_chicken2_4), findViewById(R.id.main_IMG_chicken3_4), findViewById(R.id.main_IMG_chicken4_4), findViewById(R.id.main_IMG_chicken5_4)},
                {findViewById(R.id.main_IMG_chicken1_5), findViewById(R.id.main_IMG_chicken2_5), findViewById(R.id.main_IMG_chicken3_5), findViewById(R.id.main_IMG_chicken4_5), findViewById(R.id.main_IMG_chicken5_5)}
        };

        main_IMG_money = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_money1),
                findViewById(R.id.main_IMG_money2),
                findViewById(R.id.main_IMG_money3),
                findViewById(R.id.main_IMG_money4),
                findViewById(R.id.main_IMG_money5)
        };

        main_IMG_money_matrix = new ShapeableImageView[][]{
                {findViewById(R.id.main_IMG_money1_1), findViewById(R.id.main_IMG_money2_1), findViewById(R.id.main_IMG_money3_1), findViewById(R.id.main_IMG_money4_1), findViewById(R.id.main_IMG_money5_1)},
                {findViewById(R.id.main_IMG_money1_2), findViewById(R.id.main_IMG_money2_2), findViewById(R.id.main_IMG_money3_2), findViewById(R.id.main_IMG_money4_2), findViewById(R.id.main_IMG_money5_2)},
                {findViewById(R.id.main_IMG_money1_3), findViewById(R.id.main_IMG_money2_3), findViewById(R.id.main_IMG_money3_3), findViewById(R.id.main_IMG_money4_3), findViewById(R.id.main_IMG_money5_3)},
                {findViewById(R.id.main_IMG_money1_4), findViewById(R.id.main_IMG_money2_4), findViewById(R.id.main_IMG_money3_4), findViewById(R.id.main_IMG_money4_4), findViewById(R.id.main_IMG_money5_4)},
                {findViewById(R.id.main_IMG_money1_5), findViewById(R.id.main_IMG_money2_5), findViewById(R.id.main_IMG_money3_5), findViewById(R.id.main_IMG_money4_5), findViewById(R.id.main_IMG_money5_5)}
        };

        main_IMG_bearrys = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_bearry1),
                findViewById(R.id.main_IMG_bearry2),
                findViewById(R.id.main_IMG_bearry3),
                findViewById(R.id.main_IMG_bearry4),
                findViewById(R.id.main_IMG_bearry5)
        };

        main_LBL_score = findViewById(R.id.main_LBL_score);
        main_LBL_odometer = findViewById(R.id.main_LBL_odometer);

        main_BTN_left = findViewById(R.id.main_BTN_left);
        main_BTN_right = findViewById(R.id.main_BTN_right);
    }

    private void updateTimerUI(long delay) { //Advance the objects with each passing second
        long currentMillis = System.currentTimeMillis() - startTime;
        int seconds = (int) (currentMillis / delay);
        int minutes = seconds / 60;
        seconds %= 60;
        int hours = minutes / 60;
        minutes %= 60;
        hours %= 24;
        refreshUI(); //Refresh the view
        checkChickenCrash(); //Check if there is a chicken crash
        checkMoneyCrash();
        randomColumn(); //Choose a random column for the next object
        updateOdometer();
    }

    private void updateOdometer() {
        gameManager.updateOdometer();
        main_LBL_odometer.setText(gameManager.getOdometer() + "m");
    }

    private void randomColumn() { //Selects a random column from which the next object will fall
        int chickenOrMoney = random.nextInt(2);
        int visibleIndex = random.nextInt(main_IMG_chickens.length);
        if (chickenOrMoney == 0) { //it is a chicken
            setRandomVisibility(main_IMG_chickens, visibleIndex); //Make all the rest chickens invisible
            for (int i = 0; i < main_IMG_money.length; i++)
                main_IMG_money[i].setVisibility(View.INVISIBLE); //Make all the money invisible
        }
        else { //it is a money
            setRandomVisibility(main_IMG_money, visibleIndex); //Make all the rest money invisible
            for (int i = 0; i < main_IMG_chickens.length; i++)
                main_IMG_chickens[i].setVisibility(View.INVISIBLE); //Make all the chickens invisible
        }
    }

    private void setRandomVisibility(ShapeableImageView[] arr, int visibleIndex) {
        arr[visibleIndex].setVisibility(View.VISIBLE);
        for (int i = 0; i < arr.length; i++) {
            if (i != visibleIndex)
                arr[i].setVisibility(View.INVISIBLE);
        }
    }

    private void refreshUI() { //Advances the objects down the board
        if (gameManager.isGameEnded()) { //game over
            stopTimer();
            changeActivity("GAME OVER", gameManager.getScore());
        }

        for (int i = main_IMG_chicken_matrix.length - 1; i >= 0; i--) {
            for (int j = main_IMG_chicken_matrix[i].length - 1; j >= 0; j--) {
                if (i == 0) {
                    main_IMG_chicken_matrix[i][j].setVisibility(main_IMG_chickens[j].getVisibility());
                    main_IMG_money_matrix[i][j].setVisibility(main_IMG_money[j].getVisibility());
                } else {
                    main_IMG_chicken_matrix[i][j].setVisibility(main_IMG_chicken_matrix[i - 1][j].getVisibility());
                    main_IMG_money_matrix[i][j].setVisibility(main_IMG_money_matrix[i - 1][j].getVisibility());
                }
            }
        }
    }

    private void changeActivity(String gameOver, int score) {
        Intent scoreIntent = new Intent(this, GameOverActivity.class);
        scoreIntent.putExtra(GameOverActivity.KEY_SCORE, score);
        startActivity(scoreIntent);
        finish();
    }

    private void startTimer() {
        if (!timerOn) {
            timerOn = true;
            startTime = System.currentTimeMillis();
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> updateTimerUI(speed));
                }
            }, 0, speed);
        }
    }

    private void stopTimer() {
        timerOn = false;
        timer.cancel();
    }

    private void checkChickenCrash() { //Checks if there is a chicken crash
        for (int i = 0; i < main_IMG_chickens.length; i++) {
            if (isCrash(main_IMG_chicken_matrix, i)) {
                sound.playSound(R.raw.eating_sound);
                signalManager.vibrate();
                signalManager.toast(DataManager.getToastMessages()[random.nextInt(DataManager.getToastMessages().length)]);
                if (gameManager.getCrashes() < main_IMG_hearts.length) { //update life amount
                    gameManager.setCrashes(gameManager.getCrashes() + 1);
                    int heartIndexToHide = main_IMG_hearts.length - 1 - (main_IMG_hearts.length - gameManager.getCrashes());
                    main_IMG_hearts[heartIndexToHide].setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private void checkMoneyCrash() { //Checks if there is a money crash
        for (int i = 0; i < main_IMG_money.length; i++) {
            if (isCrash(main_IMG_money_matrix, i)) {
                sound.playSound(R.raw.money_sound);
                signalManager.vibrate();
                signalManager.toast("+" + gameManager.RIGHT_ANSWER_POINTS + "$");
                gameManager.updateScore();
                main_LBL_score.setText(gameManager.getScore() + "$");
            }
        }
    }

    private boolean isCrash(ShapeableImageView[][] matrix, int column) {
        return matrix[matrix.length - 1][column].getVisibility() == View.VISIBLE
                && main_IMG_bearrys[column].getVisibility() == View.VISIBLE;
    }
}