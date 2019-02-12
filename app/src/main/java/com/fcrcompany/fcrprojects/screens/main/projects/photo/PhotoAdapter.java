package com.fcrcompany.fcrprojects.screens.main.projects.photo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fcrcompany.fcrprojects.R;
import com.fcrcompany.fcrprojects.data.api.model.ProjectFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<ProjectFile> data = new ArrayList<>();

    @NonNull
    @Override
    public PhotoAdapter.PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo, parent, false);
        return new PhotoAdapter.PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.PhotoViewHolder holder, int position) {
        ProjectFile project = data.get(position);
        holder.bind(project);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<ProjectFile> data) {
        if (data != null) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {

        private static final String MIME_TYPE_FOLDER = "application/vnd.google-apps.folder";
        private static final String MIME_TYPE_JPG = "image/jpeg";

        @BindView(R.id.photo)
        ImageView photo;
        @BindView(R.id.folder_with_name)
        TextView textView;

        private Context context;
        private int photoSize;

        PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            int width = context.getResources().getDisplayMetrics().widthPixels;
            photoSize = width / ProjectFile.COLUMN_NUMBER;
        }

        void bind(final ProjectFile photoFile) {
            switch (photoFile.mimeType) {
                case MIME_TYPE_FOLDER:
                    setupTextView(photoFile);
                    itemView.setOnClickListener(v -> PhotoActivity.start(context, photoFile));
                    break;

                case MIME_TYPE_JPG:
                    setupPhotoView(photoFile);
            }
        }

        private void setupTextView(ProjectFile photoFile) {
            textView.setVisibility(View.VISIBLE);
            textView.setMinimumWidth(photoSize);
            textView.setText(photoFile.name);
        }

        private void setupPhotoView(ProjectFile photoFile) {
            if (photoFile.webContentLink != null) {
                try {
                    Glide.with(context)
                            .load(photoFile.webContentLink)
                            .apply(new RequestOptions()
                                    .centerCrop()
                                    .override(photoSize, photoSize))
                            .into(this.photo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
