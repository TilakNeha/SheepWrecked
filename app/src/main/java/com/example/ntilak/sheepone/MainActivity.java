package com.example.ntilak.sheepone;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity implements GestureDetector.OnGestureListener ,
     GestureDetector.OnDoubleTapListener{

    Timer timer;
    TimerTask timerTask;
    CountDownTimer cdtimer, stagetimer;
    Handler myhandler ;

    // Variables for swipe detection
    private GestureDetectorCompat mDetector;
    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    // UI Elements
    private ImageView sheepimage, car1, car2, car3, car4, car5, car6,car7,car8,car9,car10,car11,car12,car13,
            fence1, fence2, fence3,homesheep1,homesheep2, homesheep3,float1, float2, float3,
            croc1, croc2,croc3, reddot;

    TextView scoretext, timeleft ;

    // Variables for the algorithm
    float sheeporiginX = -1, sheeporiginY = -1;
    int sheeplives = 3, sheepreached = 0;
    int score = 0, leftsec=0,level = 1;
    boolean donebefore = false;
    int carspeed1 = 6500, carspeed2 = 5000, carspeed3 = 5500;

    boolean open1=true, open2=true, open3=true, onfloat=false;
    boolean float1open = true, float2open = true, float3open = true,endgame = false, soundflag = true;
    boolean stagestop = false;

    // Animation objects
    ObjectAnimator car1XAnimation,car2XAnimation,car3XAnimation,car4XAnimation,car5XAnimation,car6XAnimation,car13XAnimation;
    ObjectAnimator car7XAnimation,car8XAnimation,car9XAnimation,car10XAnimation,car11XAnimation,car12XAnimation;
    AnimatorSet set1, set2, set3, set4, set5, set6, set7, set8, set9, set10,set11, set12, set13, crocset2;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoretext = (TextView) findViewById(R.id.scoreText);
        String ns = "Stage :" + level + " Sheep Left :" + Integer.toString(sheeplives) + "  Reached:" + Integer.toString(sheepreached);
        scoretext.setText(ns);
        timeleft = (TextView) findViewById(R.id.textView);
        mDetector = new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);
        myhandler = new Handler();

        final Intent myint = new Intent(this,EndActivity.class);
        initImageViews();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.baaaa);
        startCars();
        startCrocs();
        startTimer();

        cdtimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                leftsec = (int)millisUntilFinished/1000;
                timeleft.setText("Time left: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                endgame = true;
                timeleft.setText("Game Over!");
            }
        }.start();
        stagetimer = new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                leftsec = (int)millisUntilFinished/1000;
                timeleft.setText("Starting next stage in: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                endgame = true;
                timeleft.setText("Go!");
                level = level + 1;
                restartGame();
            }
        };
    }

    // Start timer task for collison detection
    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
            timer.schedule(timerTask, 2000, 50);
    }

    // Detect whether collision between objects occur
    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            @Override
            public void run() {
               final float sheepx = sheepimage.getX();
               final float sheepy = sheepimage.getY();
                final float c1x = car1.getX();
                final float c1y = car1.getY();
                final float c2x = car2.getX();
                final float c2y = car2.getY();
                final float c3x = car3.getX();
                final float c3y = car3.getY();
                final float c4x = car4.getX();
                final float c4y = car4.getY();
                final float c5x = car5.getX();
                final float c5y = car5.getY();
                final float c6x = car6.getX();
                final float c6y = car6.getY();
                final float c7x = car7.getX();
                final float c7y = car7.getY();
                final float c8x = car8.getX();
                final float c8y = car8.getY();
                final float c9x = car9.getX();
                final float c9y = car9.getY();
                final float c10x = car10.getX();
                final float c10y = car10.getY();
                final float c11x = car11.getX();
                final float c11y = car11.getY();
                final float c12x = car12.getX();
                final float c12y = car12.getY();
                final float c13x = car13.getX();
                final float c13y = car13.getY();
                final float cr1x = croc1.getX();
                final float cr1y = croc1.getY();
                final float cr2x = croc2.getX();
                final float cr2y = croc2.getY();
                final float cr3x = croc3.getX();
                final float cr3y = croc3.getY();

                myhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        boolean oncekilled = false;
                        String ns ;
                        boolean carcond1 = Math.abs(c1y - sheepy) < 40 && Math.abs((c1x) - sheepx) < 40;
                        boolean carcond2 = Math.abs(c2y - sheepy) < 40 && Math.abs(c2x - sheepx) < 40;
                        boolean carcond3 = Math.abs(c3y - sheepy) < 40 && Math.abs(c3x - sheepx) < 40;
                        boolean carcond4 = Math.abs(c4y - sheepy) < 40 && Math.abs(c4x - sheepx) < 40;
                        boolean carcond5 = Math.abs(c5y - sheepy) < 40 && Math.abs(c5x - sheepx) < 40;
                        boolean carcond6 = Math.abs(c6y - sheepy) < 40 && Math.abs(c6x - sheepx) < 40;
                        boolean carcond7 = Math.abs(c7y - sheepy) < 40 && Math.abs(c7x - sheepx) < 40;
                        boolean carcond8 = Math.abs(c8y - sheepy) < 40 && Math.abs(c8x - sheepx) < 40;
                        boolean carcond9 = Math.abs(c9y - sheepy) < 40 && Math.abs(c9x - sheepx) < 40;
                        boolean carcond10 = Math.abs(c10y - sheepy) < 40 && Math.abs(c10x - sheepx) < 40;
                        boolean carcond11 = Math.abs(c11y - sheepy) < 40 && Math.abs(c11x - sheepx) < 40;
                        boolean carcond12 = Math.abs(c12y - sheepy) < 40 && Math.abs(c12x - sheepx) < 40;
                        boolean carcond13 = Math.abs(c13y - sheepy) < 40 && Math.abs(c13x - sheepx) < 40;

                        boolean cond5 = Math.abs(fence1.getY() - sheepy) < 80 && Math.abs(fence1.getX() - sheepx) < 80 && open1;
                        boolean cond6 = Math.abs(fence2.getY() - sheepy) < 80 && Math.abs(fence2.getX() - sheepx) < 80 && open2;
                        boolean cond7 = Math.abs(fence3.getY() - sheepy) < 80 && Math.abs(fence3.getX() - sheepx) < 80 && open3;

                        if ((carcond1 || carcond2 || carcond3 || carcond4 || carcond5 || carcond6 || carcond7 || carcond8 || carcond9 || carcond10 || carcond11 || carcond12 || carcond13) &&
                                !oncekilled) {
                            if(soundflag)
                                mp.start();
                            sheeplives--;
                            ns = "Stage :" + level + " Sheep Left :" + Integer.toString(sheeplives) + "  Reached:" + Integer.valueOf(sheepreached).toString();
                            oncekilled = true;
                            scoretext.setText(ns);
                            reddot.setX(sheepx);
                            reddot.setY(sheepy);
                            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(reddot, "alpha",  1f, 0f);
                            fadeOut.setDuration(1000);
                            AnimatorSet fadeset = new AnimatorSet();
                            fadeset.playTogether(fadeOut);
                            fadeset.setDuration(1000);
                            fadeset.start();
                            sheepimage.setX(sheeporiginX);
                            sheepimage.setY(sheeporiginY);
                            sheepimage.setBackgroundResource(android.R.color.transparent);
                            onfloat = false;
                        }


                        if (cond5 || cond6 || cond7) {
                            if(soundflag)
                                mp.start();
                            sheeplives--;
                            sheepreached++;
                            score = score + 1000;
                            oncekilled = true;
                            ns = "Stage :" + level + " Sheep Left :" + Integer.toString(sheeplives) + "  Reached:" + Integer.toString(sheepreached);
                            scoretext.setText(ns);
                            if (cond5) {
                                homesheep1.setImageResource(R.drawable.sheep);
                                homesheep1.setVisibility(ImageView.VISIBLE);
                                open1 = false;
                            }
                            if (cond6) {
                                homesheep2.setImageResource(R.drawable.sheep);
                                homesheep2.setVisibility(ImageView.VISIBLE);
                                open2 = false;
                            }
                            if (cond7) {
                                homesheep3.setImageResource(R.drawable.sheep);
                                homesheep3.setVisibility(ImageView.VISIBLE);
                                open3 = false;
                            }
                            sheepimage.setX(sheeporiginX);
                            sheepimage.setY(sheeporiginY);
                            sheepimage.setBackgroundResource(android.R.color.transparent);
                            onfloat = false;
                            //sheepx = sheepimage.getX();
                            //sheepy = sheepimage.getY();

                        }

                        boolean eatcroc1 = Math.abs(cr1y - sheepy) < 50 && Math.abs(cr1x - sheepx) < 50;
                        boolean eatcroc2 = Math.abs(cr2y - sheepy) < 50 && Math.abs(cr2x - sheepx) < 50;
                        boolean eatcroc3 = Math.abs(cr3y - sheepy) < 50 && Math.abs(cr3x - sheepx) < 50;
                        if ((eatcroc1 || eatcroc2 || eatcroc3) && !oncekilled) {
                            if(soundflag)
                                mp.start();
                            sheeplives--;
                            oncekilled = true;
                            ns = "Stage :" + level + " Sheep Left :" + Integer.toString(sheeplives) + "  Reached:" + Integer.toString(sheepreached);

                            scoretext.setText(ns);
                            sheepimage.setX(sheeporiginX);
                            sheepimage.setY(sheeporiginY);

                            sheepimage.setBackgroundResource(android.R.color.transparent);
                            onfloat = false;
                        }


                        boolean onfloat1 = float1open && Math.abs(float1.getY() - sheepy) < 80 && Math.abs(float1.getX() - sheepx) < 80;
                        if (onfloat1) {
                            sheepimage.setBackgroundResource(R.drawable.floater);
                            float1.setVisibility(ImageView.INVISIBLE);
                            float1open = false;
                            onfloat = true;
                        }
                        boolean onfloat2 = float2open && Math.abs(float2.getY() - sheepy) < 80 && Math.abs(float2.getX() - sheepx) < 80;
                        if (onfloat2) {
                            sheepimage.setBackgroundResource(R.drawable.floater);
                            float2.setVisibility(ImageView.INVISIBLE);
                            float2open = false;
                            onfloat = true;
                        }
                        boolean onfloat3 = float3open && Math.abs(float3.getY() - sheepy) < 80 && Math.abs(float3.getX() - sheepx) < 80;
                        if (onfloat3) {
                            sheepimage.setBackgroundResource(R.drawable.floater);
                            float3.setVisibility(ImageView.INVISIBLE);
                            float3open = false;
                            onfloat = true;
                        }

                        if (sheepy < (float1.getY()+40) && !onfloat && !oncekilled) {
                            if(soundflag)
                                mp.start();
                            sheeplives--;
                            ns = "Stage :" + level + " Sheep Left :" + Integer.toString(sheeplives) + "  Reached:" + Integer.toString(sheepreached);

                            scoretext.setText(ns);
                            sheepimage.setX(sheeporiginX);
                            sheepimage.setY(sheeporiginY);
                            sheepimage.setBackgroundResource(android.R.color.transparent);
                            onfloat = false;
                        }
                        if (sheeplives <= 0) {
                            if (sheepreached == 3) {
                                // Do this only once:
                                if (!donebefore) {
                                    score = score + leftsec * 100;
                                    donebefore = true;
                                }
                                scoretext.setText("You Win!" + " Your Score: " + (new Integer(score)).toString());

                                cdtimer.cancel();
                                //stopCars();
                                //crocset2.cancel();
                                //endgame = false;

                                if (level == 1) {
                                    carspeed1 = 5800;
                                    carspeed2 = 4300;
                                    carspeed3 = 4800;
                                    crocset2.start();
                                    set13.start();

                                }
                                if (level == 2 ) {
                                    carspeed1 = 4800;
                                    carspeed2 = 4300;
                                    carspeed3 = 4800;
                                    crocset2.start();
                                }
                                stopCars();
                                startCars();
                                if (!stagestop) {
                                    stagetimer.start();
                                }
                                stagestop = true;

                                //sheepimage.setVisibility(Im);

                            } else {
                                scoretext.setText("Game Over!" + " Your Score: " + (new Integer(score)).toString());
                                endgame = true;
                                cdtimer.cancel();
                                //stopCars();

                            }
                            if (sheeplives <= 0 ) {
                                sheepimage.setVisibility(ImageView.INVISIBLE);
                            }
                        }

                    }
                });

            }
        };

    }

    public void startCars() {

        car1XAnimation= ObjectAnimator.ofFloat(car1, "translationX", 0f, 2500f);
        car1XAnimation.setRepeatCount(ValueAnimator.INFINITE);
        car2XAnimation= ObjectAnimator.ofFloat(car2, "translationX", 0f, 2500f);
        car2XAnimation.setRepeatCount(ValueAnimator.INFINITE);
        car3XAnimation= ObjectAnimator.ofFloat(car3, "translationX", 0f, 2500f);
        car3XAnimation.setRepeatCount(ValueAnimator.INFINITE);
        car4XAnimation= ObjectAnimator.ofFloat(car4, "translationX", 0f, 2600f);
        car4XAnimation.setRepeatCount(ValueAnimator.INFINITE);
        car4XAnimation.setStartDelay(1000);
        car5XAnimation= ObjectAnimator.ofFloat(car5, "translationX", 0f, 2600f);
        car5XAnimation.setRepeatCount(ValueAnimator.INFINITE);
        car5XAnimation.setStartDelay(1000);
        car6XAnimation= ObjectAnimator.ofFloat(car6, "translationX", 0f, 2600f);
        car6XAnimation.setRepeatCount(ValueAnimator.INFINITE);
        car6XAnimation.setStartDelay(1000);
        car7XAnimation= ObjectAnimator.ofFloat(car7, "translationX", 0f, -2700f);
        car7XAnimation.setRepeatCount(ValueAnimator.INFINITE);
        car7XAnimation.setStartDelay(4000);
        car8XAnimation= ObjectAnimator.ofFloat(car8, "translationX", 0f, -2700f);
        car8XAnimation.setRepeatCount(ValueAnimator.INFINITE);
        car8XAnimation.setStartDelay(4000);
        car9XAnimation= ObjectAnimator.ofFloat(car9, "translationX", 0f, -2700f);
        car9XAnimation.setRepeatCount(ValueAnimator.INFINITE);
        car9XAnimation.setStartDelay(4000);
        car10XAnimation= ObjectAnimator.ofFloat(car10, "translationX", 0f, -2700f);
        car10XAnimation.setRepeatCount(ValueAnimator.INFINITE);
        car10XAnimation.setStartDelay(2000);
        car11XAnimation= ObjectAnimator.ofFloat(car11, "translationX", 0f, -2700f);
        car11XAnimation.setRepeatCount(ValueAnimator.INFINITE);
        car11XAnimation.setStartDelay(2000);
        car12XAnimation= ObjectAnimator.ofFloat(car12, "translationX", 0f, -2700f);
        car12XAnimation.setRepeatCount(ValueAnimator.INFINITE);
        car12XAnimation.setStartDelay(2000);
        car13XAnimation= ObjectAnimator.ofFloat(car13, "translationX", 0f, 2500f);
        car13XAnimation.setRepeatCount(ValueAnimator.INFINITE);

        set1 = new AnimatorSet();
        set1.setDuration(carspeed1);
        set1.playTogether(car1XAnimation);
        set1.start();

        set2 = new AnimatorSet();
        set2.setDuration(carspeed1);
        set2.playTogether(car2XAnimation);
        set2.start();

        set3 = new AnimatorSet();
        set3.setDuration(carspeed1);
        set3.playTogether(car3XAnimation);
        set3.start();

        set4 = new AnimatorSet();
        set4.setDuration(carspeed2);
        set4.playTogether(car4XAnimation);
        set4.start();

        set5 = new AnimatorSet();
        set5.setDuration(carspeed2);
        set5.playTogether(car5XAnimation);
        set5.start();

        set6 = new AnimatorSet();
        set6.setDuration(carspeed2);
        set6.playTogether(car6XAnimation);
        set6.start();

        set7 = new AnimatorSet();
        set7.setDuration(carspeed1);
        set7.playTogether(car7XAnimation);
        set7.start();

        set8 = new AnimatorSet();
        set8.setDuration(carspeed1);
        set8.playTogether(car8XAnimation);
        set8.start();

        set9 = new AnimatorSet();
        set9.setDuration(carspeed1);
        set9.playTogether(car9XAnimation);
        set9.start();

        set10 = new AnimatorSet();
        set10.setDuration(carspeed3);
        set10.playTogether(car10XAnimation);
        set10.start();

        set11 = new AnimatorSet();
        set11.setDuration(carspeed3);
        set11.playTogether(car11XAnimation);
        set11.start();

        set12 = new AnimatorSet();
        set12.setDuration(carspeed3);
        set12.playTogether(car12XAnimation);
        set12.start();

        set13 = new AnimatorSet();
        set13.setDuration(8000);
        set13.playTogether(car13XAnimation);
        //set13.start();


    }

   public void stopCars() {
       set1.cancel();
       set2.cancel();
       set3.cancel();
       set4.cancel();
       set5.cancel();
       set6.cancel();
       set7.cancel();
       set8.cancel();
       set9.cancel();
       set10.cancel();
       set11.cancel();
       set12.cancel();

   }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Reset the game to the last stage.
        if (id == R.id.playagain) {
            score = 0;
            level = 1;
            stopCars();
            carspeed1 =6500;
            carspeed2 =5000;
            carspeed3 =5500;
            crocset2.cancel();
            //set13.cancel();
            startCars();

            restartGame();
        }

        // Turn off sounds
        if (id == R.id.sound) {
            if(soundflag == true)
                soundflag = false;
            else
                soundflag = true;
        }
        return super.onOptionsItemSelected(item);


    }

    // Reset the game state..
    private void restartGame() {
        sheeplives = 3;
        sheepreached = 0;
        //score = 0;
        donebefore = false;
        open1=true;
        open2=true;
        open3=true;
        onfloat=false;
        float1open = true;
        float2open = true;
        float3open = true;
        endgame = false;
        stagestop = false;
        sheepimage.setX(sheeporiginX);
        sheepimage.setY(sheeporiginY);
        sheepimage.setVisibility(ImageView.VISIBLE);
        float1.setVisibility(ImageView.VISIBLE);
        float2.setVisibility(ImageView.VISIBLE);
        float3.setVisibility(ImageView.VISIBLE);
        sheepimage.setVisibility(ImageView.VISIBLE);
        homesheep1.setVisibility(ImageView.INVISIBLE);
        homesheep2.setVisibility(ImageView.INVISIBLE);
        homesheep3.setVisibility(ImageView.INVISIBLE);
        String ns = "Stage :" + level + " Sheep Left :" + Integer.toString(sheeplives) + "  Reached:" + Integer.toString(sheepreached);
        scoretext.setText(ns);
        cdtimer.cancel();
        cdtimer.start();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    // Start Crocodile Animation
    public void startCrocs() {
        ObjectAnimator croc1XAnimation= ObjectAnimator.ofFloat(croc1, "translationX", -1000f, 1400f);
        croc1XAnimation.setRepeatCount(ValueAnimator.INFINITE);
        ObjectAnimator croc2XAnimation= ObjectAnimator.ofFloat(croc2, "translationX", 500f, -1200f);
        croc2XAnimation.setRepeatCount(ValueAnimator.INFINITE);
        croc2XAnimation.setStartDelay(2000);
        AnimatorSet crocset = new AnimatorSet();
        crocset.setDuration(10000);
        crocset.playTogether(croc1XAnimation);
        crocset.start();
        AnimatorSet crocset1 = new AnimatorSet();
        crocset1.setDuration(8000);
        crocset1.playTogether(croc2XAnimation);
        crocset1.start();

        ObjectAnimator croc3XAnimation= ObjectAnimator.ofFloat(croc3, "translationX", -1000f, 1400f);
        croc3XAnimation.setRepeatCount(ValueAnimator.INFINITE);
        crocset2 = new AnimatorSet();
        crocset2.setDuration(7500);
        crocset2.playTogether(croc3XAnimation);
    }

    // Initialize the image views
    public void initImageViews() {
        sheepimage = (ImageView) findViewById(R.id.sheep);
        car1 = (ImageView) findViewById(R.id.car1);
        car2 = (ImageView) findViewById(R.id.car2);
        car3 = (ImageView) findViewById(R.id.car3);
        car4 = (ImageView) findViewById(R.id.car4);
        car5 = (ImageView) findViewById(R.id.car5);
        car6 = (ImageView) findViewById(R.id.car6);
        car7 = (ImageView) findViewById(R.id.car7);
        car8 = (ImageView) findViewById(R.id.car8);
        car9 = (ImageView) findViewById(R.id.car9);
        car10 = (ImageView) findViewById(R.id.car10);
        car11 = (ImageView) findViewById(R.id.car11);
        car12 = (ImageView) findViewById(R.id.car12);
        car13 = (ImageView) findViewById(R.id.bike);

        fence1 = (ImageView) findViewById(R.id.fence1);
        fence2 = (ImageView) findViewById(R.id.fence2);
        fence3 = (ImageView) findViewById(R.id.fence3);

        homesheep1 = (ImageView) findViewById(R.id.homesheep1);
        homesheep2 = (ImageView) findViewById(R.id.homesheep2);
        homesheep3 = (ImageView) findViewById(R.id.homesheep3);

        float1 = (ImageView) findViewById(R.id.float1);
        float2 = (ImageView) findViewById(R.id.float2);
        float3 = (ImageView) findViewById(R.id.float3);

        croc1 = (ImageView) findViewById(R.id.croc1);
        croc2 = (ImageView) findViewById(R.id.croc2);
        croc3 = (ImageView) findViewById(R.id.croc3);

        reddot =(ImageView) findViewById(R.id.reddot);

    }
    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    // Detect the direction of the player swipe.
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if(sheeplives > 0 && endgame == false) {
            if (sheeporiginX == -1 || sheeporiginY == -1) {
                sheeporiginX = sheepimage.getX();
                sheeporiginY = sheepimage.getY();
            }
            try {

                // Up swipe
                if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    float y = sheepimage.getY();
                    if (y > fence1.getY()) {
                        sheepimage.setY(y - 120);
                    }
                    //car1XAnimation.end();

                    // Down swipe
                } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    float y = sheepimage.getY();
                    if (y + 40 <= sheeporiginY) {
                        sheepimage.setY(y + 120);
                    }

                    //Left Swipe
                } else if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    float x = sheepimage.getX();
                    if (x > 20) {
                        sheepimage.setX(x - 120);
                    }

                    // Right swipe
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    float x = sheepimage.getX();
                    if (x < 900) {
                        sheepimage.setX(x + 120);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }


}