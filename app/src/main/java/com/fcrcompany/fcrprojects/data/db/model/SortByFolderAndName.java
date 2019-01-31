package com.fcrcompany.fcrprojects.data.db.model;

import java.util.Comparator;

public class SortByFolderAndName implements Comparator<ProjectFileEntity> {
    private static final String FOLDER = "folder";

    @Override
    public int compare(ProjectFileEntity o1, ProjectFileEntity o2) {
        if (!o1.mimeType.equals(o2.mimeType)) {
            if (o1.mimeType.contains(FOLDER)) {
                return -1;
            } else if (o2.mimeType.contains(FOLDER)) {
                return 1;
            }
        }
        return o1.name.compareTo(o2.name);
    }
}
