package com.fcrcompany.fcrprojects.screens.start;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.fcrcompany.fcrprojects.R;
import com.fcrcompany.fcrprojects.screens.access.NoAccessActivity;
import com.fcrcompany.fcrprojects.screens.auth.AuthActivity;
import com.fcrcompany.fcrprojects.screens.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";
    private static final int SIGN_IN_CODE = 1;

    @BindView(R.id.start_top_corner)
    ImageView topCorner;

    @BindView(R.id.start_bottom_corner)
    ImageView bottomCorner;

    private StartViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(StartViewModelImpl.class);

        Intent signInIntent = new Intent(this, AuthActivity.class);
        startActivityForResult(signInIntent, SIGN_IN_CODE);
    }

    private void initInputs() {
        viewModel.access().observe(this,
                haveAccess -> {
                    if (haveAccess != null && haveAccess) {
                        MainActivity.startInNewTask(this);
                    } else {
                        NoAccessActivity.startInNewTask(this);
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();

        startAnimations();
    }

    private void startAnimations() {

        ObjectAnimator innerAnimator = ObjectAnimator.ofFloat(topCorner, "rotation", 0, 360);
        innerAnimator.setDuration(30000);
        innerAnimator.setRepeatMode(ValueAnimator.RESTART);
        innerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        innerAnimator.setInterpolator(new LinearInterpolator());

        ObjectAnimator outerAnimator = ObjectAnimator.ofFloat(bottomCorner, "rotation", 0, -360);
        outerAnimator.setDuration(60000);
        outerAnimator.setRepeatMode(ValueAnimator.RESTART);
        outerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        outerAnimator.setInterpolator(new LinearInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.play(innerAnimator).with(outerAnimator);
        set.start();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SIGN_IN_CODE:
                if (resultCode == RESULT_OK) {

                    viewModel.getProjects();
                    initInputs();

                } else {
                    finish();
                }
        }
    }
}
