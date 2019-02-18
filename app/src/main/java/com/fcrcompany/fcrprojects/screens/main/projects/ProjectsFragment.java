package com.fcrcompany.fcrprojects.screens.main.projects;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fcrcompany.fcrprojects.R;
import com.fcrcompany.fcrprojects.data.api.model.ProjectFile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * {@link ProjectsFragment} displays a scrolling list of {@link ProjectFile} objects using RecyclerView.
 */
public class ProjectsFragment extends Fragment {
    public static final String TYPE_KEY = "type";

    private ProjectFilesAdapter projectAdapter;
    private String type;
    private Unbinder unbinder;
    private ProjectFilesViewModel viewModel;

    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    public static ProjectsFragment createProjectFragment(String type) {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ProjectsFragment.TYPE_KEY, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            type = args.getString(TYPE_KEY, ProjectFile.TYPE_UNKNOWN);
            if (type.equals(ProjectFile.TYPE_UNKNOWN)) {
                throw new IllegalArgumentException("Unknown type");
            }
        }
        viewModel = ViewModelProviders.of(this).get(ProjectFilesViewModelImpl.class);
        projectAdapter = new ProjectFilesAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_projects, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(projectAdapter);

        viewModel.getProjectFiles(getFolderId(type));
        initInputs();
    }

    private void initInputs() {

        viewModel.projectFiles().observe(this,
                projectFiles -> {
                    projectAdapter.setData(projectFiles);
                    progressBar.setVisibility(View.GONE);
                }
        );
    }

    private String getFolderId(String type) {
        String folderId = "";
        switch (type){
            case ProjectFile.TYPE_CURRENT:
                folderId = ProjectFile.CURRENT_FOLDER_ID;
                break;
            case ProjectFile.TYPE_ARCHIVE:
                folderId = ProjectFile.ARCHIVE_FOLDER_ID;
        }
        return folderId;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
