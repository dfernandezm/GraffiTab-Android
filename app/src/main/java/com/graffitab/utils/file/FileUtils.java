package com.graffitab.utils.file;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.graffitab.constants.Constants;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by georgichristov on 01/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class FileUtils {

    /**
     * Prepare image for cropping.
     */
    public static Uri saveImageToCrop(byte[] imageBytes) {
        File photo = new File(getApplicationDirectory(), "toCrop.jpg");
        if (photo.exists()) // Cleanup.
            photo.delete();

        try {
            FileOutputStream fos = new FileOutputStream(photo.getPath());
            fos.write(imageBytes);
            fos.close();

            return Uri.fromFile(photo);
        }
        catch (Exception e) {
            Log.e("FileUtils", "Failed to save image - ", e);
        }

        return null;
    }

    /**
     * Returns (and creates, if needed) the root app directory.
     */
    public static String getApplicationDirectory() {
        String appDirectory = Environment.getExternalStorageDirectory() + File.separator + Constants.FILE_APP_FOLDER;
        File file = new File(appDirectory);
        if (!file.exists())
            file.mkdirs();
        return appDirectory;
    }
}
