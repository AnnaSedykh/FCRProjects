package com.fcrcompany.fcrprojects.screens.main.projects.photo;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.fcrcompany.fcrprojects.R;
import com.fcrcompany.fcrprojects.data.api.model.ProjectFile;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends AppCompatActivity {

    public static void start(Context context, ProjectFile project) {
        Intent starter = new Intent(context, PhotoActivity.class);
        starter.putExtra(ProjectFile.PROJECT_FILE, project);
        context.startActivity(starter);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.photo_layout)
    View layout;


    private ProjectFile photoFolder;
    private PhotoAdapter photoAdapter;
    private PhotoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        photoFolder = getIntent().getParcelableExtra(ProjectFile.PROJECT_FILE);
        if (photoFolder != null) {

            setupToolbar();
            viewModel = ViewModelProviders.of(this).get(PhotoViewModelImpl.class);
            photoAdapter = new PhotoAdapter();
            recycler.setLayoutManager(new GridLayoutManager(this, ProjectFile.COLUMN_NUMBER));
            recycler.setAdapter(photoAdapter);

            viewModel.getPhotos(photoFolder.id);
            initInputs();

        } else {
            showErrorMsg();
        }
    }

    private void initInputs() {

        viewModel.photos().observe(this,
                photos -> {
                    if (photos != null && !photos.isEmpty()) {
                        photoAdapter.setData(photos);
                        progressBar.setVisibility(View.GONE);
                    }
                }
        );
    }

    private void showErrorMsg() {
        Snackbar snackbar = Snackbar
                .make(layout, R.string.failed_to_open_project, Snackbar.LENGTH_LONG);

        snackbar.show();
        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                finish();
            }
        });
    }

    private void setupToolbar() {
        toolbar.setTitle(photoFolder.name);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
