package org.literacyapp.startguide.content.swipe;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import org.literacyapp.startguide.R;
import org.literacyapp.startguide.util.AnimationHelper;
import org.literacyapp.startguide.util.MediaPlayerHelper;

import static org.literacyapp.startguide.util.AnimationHelper.DEFAULT_ANIMATION_DELAY;


/**
 *
 */
public class SwipeUpDownActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private AnimationHelper mAnimationHelper;
    private GestureDetector mGestureDetector;
    private View mHandView;
    private boolean detectUp = true;
    private boolean detectDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_up_down);

        //List of images
        TypedArray imagesArray = getResources().obtainTypedArray(R.array.image_files);
        SwipeUpDownAdapter adapter = new SwipeUpDownAdapter(imagesArray);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Hand view
        mHandView = findViewById(R.id.hand);

        //Add gesture detector
        mGestureDetector = new GestureDetector(this, this);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });

        playMoveBottom();
    }

    private void playMoveBottom() {
        //TODO: 04/02/2017 update audio file (en, sw) "Move to the bottom of the list"
        MediaPlayerHelper.playWithDelay(this, R.raw.move_bottom, new MediaPlayerHelper.MediaPlayerListener() {
            @Override
            public void onCompletion() {
                showMoveBottom();
            }
        });
    }

    private void playMoveTop() {
        //TODO: 04/02/2017 update audio file (en, sw) "Move to the top of the list"
        MediaPlayerHelper.play(this, R.raw.move_top, new MediaPlayerHelper.MediaPlayerListener() {
            @Override
            public void onCompletion() {
                showMoveTop();
            }
        });
    }

    /**
     * Hand animation to explain scroll to the bottom
     */
    private void showMoveBottom() {
        mAnimationHelper = new AnimationHelper(this, R.anim.slide_up);
        mAnimationHelper.setRepeatMode(true);
        mAnimationHelper.animateView(mHandView, DEFAULT_ANIMATION_DELAY);
    }

    /**
     * Hand animation to explain scroll to the top
     */
    private void showMoveTop() {
        resetHandPosition();
        mAnimationHelper = new AnimationHelper(this, R.anim.slide_down);
        mAnimationHelper.setRepeatMode(true);
        mAnimationHelper.animateView(mHandView, DEFAULT_ANIMATION_DELAY);
    }

    private void resetHandPosition() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mHandView.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        mHandView.setLayoutParams(params);
        mHandView.setVisibility(View.VISIBLE);
    }

     private boolean isDetectScrollUpActive() {
        return detectUp;
    }

    private boolean isDetectScrollDownActive() {
        return detectDown;
    }

    private void setDetectUpActive(boolean activeUpDetect) {
        detectUp = activeUpDetect;
    }

    private void setDetectDownActive(boolean activeDownDetect) {
        detectDown = activeDownDetect;
    }

    //region GestureDetector.OnGestureListener
    @Override
    public boolean onDown(MotionEvent e) {
        if (mAnimationHelper != null) {
            mAnimationHelper.stopAnimation();
        }
        mHandView.setVisibility(View.GONE);
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

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        float yMov = Math.abs(e1.getY() - e2.getY());
        float xMov = Math.abs(e1.getX() - e2.getX());

        //Detect vertical scroll
        if (yMov > xMov) {

            if (isDetectScrollUpActive() && (velocityY < 0)) {
                //Detected scroll up
                resetHandPosition();
                playMoveTop();

                setDetectUpActive(false);
                setDetectDownActive(true);

            } else if (isDetectScrollDownActive() && (velocityY > 0)) {
                //Detected scroll down
                Intent intent = new Intent(this, SwipeRightLeftActivity.class);
                startActivity(intent);
            }
        }

        return true;
    }
    //endregion
}