package com.fcrcompany.fcrprojects.screens.access;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fcrcompany.fcrprojects.App;
import com.fcrcompany.fcrprojects.BuildConfig;
import com.fcrcompany.fcrprojects.R;
import com.fcrcompany.fcrprojects.data.prefs.Prefs;
import com.fcrcompany.fcrprojects.screens.start.StartActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoAccessActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    public static void startInNewTask(Context context) {
        Intent starter = new Intent(context, NoAccessActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    private static final int SEND_REQUEST_CODE = 456;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.access)
    TextView access;

    @BindView(R.id.request)
    TextView request;

    @BindView(R.id.btn_send)
    Button btnSend;

    private String lastViewedDate = new SimpleDateFormat("dd.MM.yyyy", Locale.US).format(new Date());
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_access);
        ButterKnife.bind(this);

        prefs = ((App) getApplication()).getPrefs();

        toolbar.inflateMenu(R.menu.menu_access);
        toolbar.setOnMenuItemClickListener(this);

        if(prefs.isAccessRequestSent()){
            pendingApproval();
        }

        btnSend.setOnClickListener(v -> sendAccessRequest());
    }

    @Override
    protected void onRestart() {
        //TODO check access job
        super.onRestart();
        String newDate = new SimpleDateFormat("dd.MM.yyyy", Locale.US).format(new Date());
        if (!lastViewedDate.equals(newDate)) {
            StartActivity.startInNewTask(this);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_access:
                clearAccountData();
                StartActivity.startInNewTask(this);
                return true;
        }
        return false;
    }

    private void clearAccountData() {
        prefs.setToken(null);
        prefs.setAccountName(null);
        prefs.setAccessRequestSent(false);
    }

    /**
     * Starts activity to send email asking permission to view project files on Google Drive.
     */
    private void sendAccessRequest() {

        String email = prefs.getAccountName();
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", BuildConfig.FCR_ACCOUNT, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.request_permission));
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_template,
                email, getString(R.string.email_text_read_access)));

        startActivityForResult(Intent.createChooser(emailIntent, getString(R.string.email_chooser)), SEND_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SEND_REQUEST_CODE:
                prefs.setAccessRequestSent(true);
                pendingApproval();
        }
    }

    private void pendingApproval() {
        access.setVisibility(View.INVISIBLE);
        btnSend.setVisibility(View.INVISIBLE);
        request.setText(getString(R.string.request_sent));
    }
}
