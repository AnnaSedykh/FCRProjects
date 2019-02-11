package com.fcrcompany.fcrprojects.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProjectFile implements Parcelable {

    public static final String PROJECT = "project";
    public static final String CURRENT_FOLDER_ID = "1G8ozUR7jyP3DOiU-it2sa1_4j-EYqxUB";
    public static final String ARCHIVE_FOLDER_ID = "1g1xmm-jbrVxiQZJv6myo0CMhsqILiKom";

    public static final String PHOTO_RU = "фото";
    public static final String PHOTO_EN = "photo";

    public static final String TYPE_CURRENT = "0";
    public static final String TYPE_ARCHIVE = "1";
    public static final String TYPE_UNKNOWN = "-1";

    public String id;

    public String name;

    public String mimeType;

    protected ProjectFile(Parcel in) {
        id = in.readString();
        name = in.readString();
        mimeType = in.readString();
    }

    public static final Creator<ProjectFile> CREATOR = new Creator<ProjectFile>() {
        @Override
        public ProjectFile createFromParcel(Parcel in) {
            return new ProjectFile(in);
        }

        @Override
        public ProjectFile[] newArray(int size) {
            return new ProjectFile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(mimeType);
    }
}
