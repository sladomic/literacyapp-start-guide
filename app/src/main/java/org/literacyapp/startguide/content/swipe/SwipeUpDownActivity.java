package org.literacyapp.startguide.content.swipe;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
public class SwipeUpDownActivity extends AppCompatActivity implements View.OnTouchListener,
        SwipeUpDownAdapter.OnScrollListener {

    private AnimationHelper mAnimationHelper;
    private View mHandView;
    private SwipeUpDownAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_up_down);

        //List of images
        TypedArray imagesArray = getResources().obtainTypedArray(R.array.image_files);
        mAdapter = new SwipeUpDownAdapter(imagesArray, this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setOnTouchListener(this);

        //Hand view
        mHandView = findViewById(R.id.hand);

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

    //region View.OnTouchListener
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mAnimationHelper != null) {
            mAnimationHelper.stopAnimation();
        }
        mHandView.setVisibility(View.GONE);
        return false;
    }
    //endregion

    //region SwipeUpDownAdapter.OnScrollListener
    @Override
    public void onLastItemReached() {
        resetHandPosition();
        playMoveTop();
    }

    @Override
    public void onFirstItemReached() {
        Intent intent = new Intent(this, SwipeRightLeftActivity.class);
        startActivity(intent);
    }
    //endregion
}