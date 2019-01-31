package com.fcrcompany.fcrprojects.screens.access;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fcrcompany.fcrprojects.R;

public class NoAccessActivity extends AppCompatActivity {

    public static void startInNewTask(Context context) {
        Intent starter = new Intent(context, NoAccessActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_access);
    }
}
