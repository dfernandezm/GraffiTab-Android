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

    private static final String APP_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Constants.FILE_APP_FOLDER;

    public static Uri saveImageToShare(byte[] imageBytes) {
        File photo = new File(getApplicationDirectory(), "toShare_" + System.currentTimeMillis() + ".jpg");
        if (photo.exists()) // Cleanup.
            photo.delete();

        try {
            FileOutputStream fos = new FileOutputStream(photo.getPath());
            fos.write(imageBytes);
            fos.flush();
            fos.close();

            return Uri.fromFile(photo);
        }
        catch (Exception e) {
            Log.e("FileUtils", "Failed to save image - ", e);
        }

        return null;
    }

    public static boolean cleanupFile(Uri file) {
        if (file == null) return false;
        File f = new File(file.getPath());
        if (f.exists())
            return f.delete();
        return false;
    }

    /**
     * Returns (and creates, if needed) the root app directory.
     */
    public static String getApplicationDirectory() {
        File file = new File(APP_DIRECTORY);
        if (!file.exists())
            file.mkdirs();
        return APP_DIRECTORY;
    }

    public static void clearApplicationDirectory() {
        new Thread() {

            @Override
            public void run() {
                deleteRecursive(new File(getApplicationDirectory()));
            }
        }.start();
    }

    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
}
