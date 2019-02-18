package com.fcrcompany.fcrprojects.screens.main.projects.files;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.fcrcompany.fcrprojects.R;
import com.fcrcompany.fcrprojects.data.api.model.ProjectFile;
import com.fcrcompany.fcrprojects.screens.main.projects.ProjectFilesAdapter;
import com.fcrcompany.fcrprojects.screens.main.projects.ProjectFilesViewModel;
import com.fcrcompany.fcrprojects.screens.main.projects.ProjectFilesViewModelImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilesActivity extends AppCompatActivity {

    public static void start(Context context, ProjectFile project) {
        Intent starter = new Intent(context, FilesActivity.class);
        starter.putExtra(ProjectFile.PROJECT_FILE, project);
        context.startActivity(starter);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.files_layout)
    View layout;

    private ProjectFile project;
    private ProjectFilesAdapter filesAdapter;
    private ProjectFilesViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        ButterKnife.bind(this);

        project = getIntent().getParcelableExtra(ProjectFile.PROJECT_FILE);
        if (project != null) {

            setupToolbar();
            viewModel = ViewModelProviders.of(this).get(ProjectFilesViewModelImpl.class);
            filesAdapter = new ProjectFilesAdapter();
            recycler.setLayoutManager(new LinearLayoutManager(this));
            recycler.setAdapter(filesAdapter);

            viewModel.getProjectFiles(project.id);
            initInputs();

        } else {
            showErrorMsg();
        }
    }

    private void initInputs() {

        viewModel.projectFiles().observe(this,
                projectFiles -> {
                    filesAdapter.setData(projectFiles);
                    progressBar.setVisibility(View.GONE);
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
        toolbar.setTitle(project.name);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

}
