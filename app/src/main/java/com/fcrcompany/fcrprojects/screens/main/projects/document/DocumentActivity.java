package com.fcrcompany.fcrprojects.screens.main.projects.document;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.fcrcompany.fcrprojects.R;
import com.fcrcompany.fcrprojects.data.api.model.ProjectFile;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocumentActivity extends AppCompatActivity {

    private static final String TAG = "DocumentActivity";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 111;
    private static final int VIEW_DOCUMENT_CODE = 111;

    public static void start(Context context, ProjectFile document) {
        Intent starter = new Intent(context, DocumentActivity.class);
        starter.putExtra(ProjectFile.PROJECT_FILE, document);
        context.startActivity(starter);
    }

    @BindView(R.id.progress)
    ImageView progress;

    private ProjectFile projectFile;
    private File fileDir;
    private DocumentViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        ButterKnife.bind(this);

        projectFile = getIntent().getParcelableExtra(ProjectFile.PROJECT_FILE);
        if (projectFile != null) {

            viewModel = ViewModelProviders.of(this).get(DocumentViewModelImpl.class);
            downloadFile(projectFile.id);
            initInputs();
        } else {
            showErrorMsg(getText(R.string.failed_to_open_file));
            finish();
        }
    }

    private void downloadFile(String fileId) {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {

            if (isExternalStorageWritable()) {

                File storageDir = getPublicDocumentStorageDir();
                fileDir = new File(storageDir.getAbsolutePath(), projectFile.name);
                viewModel.downloadFile(fileId, fileDir);
            }
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private File getPublicDocumentStorageDir() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), getString(R.string.app_name));
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadFile(projectFile.id);
                } else {
                    showErrorMsg(getString(R.string.permission_storage_denied));
                    finish();
                }
        }
    }

    private void initInputs() {

        viewModel.isDownloaded().observe(this,
                isDownloaded -> {
                    if (isDownloaded != null && isDownloaded) {
                        openDocument(fileDir);
                    } else {
                        showErrorMsg(getString(R.string.failed_to_download_file, projectFile.name));
                    }
                });
    }

    private void openDocument(File fileDir) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", fileDir);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivityForResult(intent, VIEW_DOCUMENT_CODE);
            } catch (ActivityNotFoundException ex) {
                showErrorMsg(getString(R.string.no_app_to_view_file, fileDir.getName().substring(fileDir.getName().indexOf("."))));
                finish();
            }
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(fileDir.getAbsolutePath()), projectFile.mimeType);
            intent = Intent.createChooser(intent, getString(R.string.open_file_chooser_title));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, VIEW_DOCUMENT_CODE);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case VIEW_DOCUMENT_CODE:
                finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startAnimations();
    }

    private void startAnimations() {

        ObjectAnimator innerAnimator = ObjectAnimator.ofFloat(progress, "rotation", 0, 360);
        innerAnimator.setDuration(30000);
        innerAnimator.setRepeatMode(ValueAnimator.RESTART);
        innerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        innerAnimator.setInterpolator(new LinearInterpolator());
        innerAnimator.start();
    }

    private void showErrorMsg(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
