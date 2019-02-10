package com.fcrcompany.fcrprojects.screens.start;

import android.accounts.AccountManager;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.fcrcompany.fcrprojects.App;
import com.fcrcompany.fcrprojects.R;
import com.fcrcompany.fcrprojects.data.prefs.Prefs;
import com.fcrcompany.fcrprojects.screens.access.NoAccessActivity;
import com.fcrcompany.fcrprojects.screens.main.MainActivity;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.DriveScopes;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity {

    public static void startInNewTask(Context context) {
        Intent starter = new Intent(context, StartActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    private static final int SIGN_IN_CODE = 123;

    @BindView(R.id.start_top_corner)
    ImageView topCorner;

    @BindView(R.id.start_bottom_corner)
    ImageView bottomCorner;

    private Prefs prefs;
    private StartViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(StartViewModelImpl.class);
        initInputs();

        prefs = ((App) getApplication()).getPrefs();
        String token = prefs.getToken();

        if (token == null) {
            signIn();
        } else {
            viewModel.checkAccess(token);
        }
    }

    private void signIn() {
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(DriveScopes.DRIVE));
        startActivityForResult(credential.newChooseAccountIntent(), SIGN_IN_CODE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SIGN_IN_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getExtras() != null) {
                        String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                        if (accountName != null) {
                            prefs.setAccountName(accountName);
                            viewModel.receiveToken(accountName);
                        }
                    }
                } else {
                    showQuitDialog();
                }
        }
    }

    private void showQuitDialog() {
        QuitDialog quitDialog = new QuitDialog();
        quitDialog.setListener(new QuitDialogListener());
        quitDialog.show(getSupportFragmentManager(), QuitDialog.QUIT_DIALOG_TAG);
    }

    private void initInputs() {

        viewModel.token().observe(this,
                token -> {
                    if (token == null) {
                        signIn();
                    } else {
                        viewModel.checkAccess(token);
                    }
                });

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


    private class QuitDialogListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    setResult(RESULT_CANCELED, getIntent());
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    signIn();
            }
        }
    }
}
