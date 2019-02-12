package com.fcrcompany.fcrprojects.screens.main.projects;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fcrcompany.fcrprojects.R;
import com.fcrcompany.fcrprojects.data.api.model.ProjectFile;
import com.fcrcompany.fcrprojects.screens.main.projects.files.FilesActivity;
import com.fcrcompany.fcrprojects.screens.main.projects.photo.PhotoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectFilesAdapter extends RecyclerView.Adapter<ProjectFilesAdapter.ProjectViewHolder> {

    private List<ProjectFile> data = new ArrayList<>();

    @NonNull
    @Override
    public ProjectFilesAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_file, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        ProjectFile project = data.get(position);
        holder.bind(project);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<ProjectFile> data) {
        if(data != null) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        private static final String OPEN_URL = "https://drive.google.com/open?id=";
        private static final String MIME_TYPE_FOLDER = "application/vnd.google-apps.folder";
        private static final String MIME_TYPE_JPG = "image/jpeg";
        private static final String MIME_TYPE_PDF = "application/pdf";

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.changes)
        ImageView changes;

        private Context context;


        ProjectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            context = itemView.getContext();
        }

        void bind(final ProjectFile projectFile) {

            if (name != null) {
                name.setText(projectFile.name);
            }

            //Set image icon and click listener according to file mime type
            switch (projectFile.mimeType) {
                case MIME_TYPE_FOLDER:
                    String nameLowCase = projectFile.name.toLowerCase();
                    if (nameLowCase.contains(ProjectFile.PHOTO_RU) || nameLowCase.contains(ProjectFile.PHOTO_EN)) {
                        image.setImageResource(R.drawable.ic_photo);
                        itemView.setOnClickListener(v -> PhotoActivity.start(context, projectFile));
                    } else {
                        image.setImageResource(R.drawable.ic_mime_folder);
                        itemView.setOnClickListener(v -> FilesActivity.start(context, projectFile));
                    }
                    break;
                case MIME_TYPE_JPG:
                    image.setImageResource(R.drawable.ic_mime_image);
//                    startViewInBrowserOnClick(projectFile);
                    break;
                case MIME_TYPE_PDF:
                    image.setImageResource(R.drawable.ic_mime_pdf);
//                    startViewInBrowserOnClick(projectFile);
                    break;
                default:
                    image.setImageResource(R.drawable.ic_mime_other);
//                    startViewInBrowserOnClick(projectFile);
            }
        }

    }
}
