package com.fcrcompany.fcrprojects.screens.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.fcrcompany.fcrprojects.App;
import com.fcrcompany.fcrprojects.R;
import com.fcrcompany.fcrprojects.data.prefs.Prefs;
import com.fcrcompany.fcrprojects.screens.start.StartActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener{
    
    public static void startInNewTask(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        prefs = ((App) getApplication()).getPrefs();
        initToolbarAndTabs();
    }

    private void initToolbarAndTabs() {
        toolbar.inflateMenu(R.menu.menu_access);
        toolbar.setOnMenuItemClickListener(this);

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
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
}
