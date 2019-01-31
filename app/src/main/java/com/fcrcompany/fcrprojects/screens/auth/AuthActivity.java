package com.fcrcompany.fcrprojects.screens.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.fcrcompany.fcrprojects.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;

/**
 * {@link AuthActivity} is used to sign in to Google account.
 */
public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";
    private static final int REQUEST_CODE_SIGN_IN = 555;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signIn();
    }

    //Sign in to Google account
    private void signIn() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(new Scope(Scopes.DRIVE_FULL))
                        .requestEmail()
                        .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, signInOptions);

        startActivityForResult(googleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK, getIntent());
                    finish();
                } else {
                    Toast.makeText(this, getString(R.string.sign_in_failed), Toast.LENGTH_SHORT).show();
                }
        }
    }
}