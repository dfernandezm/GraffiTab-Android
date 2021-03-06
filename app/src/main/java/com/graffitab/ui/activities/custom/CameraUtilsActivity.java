package com.graffitab.ui.activities.custom;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.cocosw.bottomsheet.BottomSheet;
import com.graffitab.R;
import com.graffitab.permissions.GTPermissions;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.utils.Utils;
import com.graffitab.utils.file.FileUtils;
import com.graffitab.utils.image.BitmapUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.InputStream;

/**
 * Created by georgichristov on 30/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class CameraUtilsActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_PICK_IMAGE = 2;

    private ImageView targetView;
    private Uri cameraImageUri;
    private int actionId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) { // Success
                Uri resultUri = result.getUri();
                finishCroppingImage(resultUri);
                Log.i(getClass().getSimpleName(), "Finished cropping image. Path is: " + resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) { // Error
                Exception error = result.getError();
                Log.e(getClass().getSimpleName(), "Failed to retrieve cropped image.", error);
            }
        }
        else if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) { // Success
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), cameraImageUri);
                    finishCapturingImage(imageBitmap);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Failed to retrieve taken image.", e);
                }
            }
            else // Error
                Log.e(getClass().getSimpleName(), "Failed to retrieve captured image. GTResultCode is not OK");
        }
        else if (requestCode == REQUEST_PICK_IMAGE) {
            if (resultCode == RESULT_OK) { // Success
                if (data == null) {
                    Log.e(getClass().getSimpleName(), "Failed to retrieve chose image. Data is null");
                    return;
                }

                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    finishCapturingImage(inputStream);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Failed to retrieve chose image.", e);
                }
            }
            else // Error
                Log.e(getClass().getSimpleName(), "Failed to retrieve chosen image. GTResultCode is not OK");
        }
    }

    /**
     * Shows a dialog to prompt for the source of the image.
     *
     * @param view target view.
     */
    public void showImagePicker(final ImageView view) {
        final Runnable pickerRunnable = new Runnable() {
            @Override
            public void run() {
                targetView = view;

                BottomSheet.Builder builder = buildImagePickerSheet();
                builder = builder.listener(new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actionId = which;
                        if (which == R.id.action_take_new)
                            takePicture();
                        else if (which == R.id.action_choose)
                            choosePicture();
                        else if (which == R.id.action_remove)
                            finishPickingImage(null, actionId);
                        else if (which == R.id.action_facebook_import)
                            finishPickingImage(null, actionId);
                    }
                });
                builder.show();
            }
        };
        GTPermissions.manager.checkPermission(this, GTPermissions.PermissionType.CAMERA_STORAGE, new GTPermissions.OnPermissionResultListener() {

            @Override
            public void onPermissionGranted() {
                pickerRunnable.run();
            }

            @Override
            public void onPermissionDenied() {}

            @Override
            public void onDecideLater() {}
        });
    }

    /**
     * Builds the image source dialog.
     */
    public BottomSheet.Builder buildImagePickerSheet() {
        return new BottomSheet.Builder(this, R.style.BottomSheet_StyleDialog)
                .title(R.string.other_select_image)
                .sheet(R.menu.menu_camera_normal);
    }

    public void finishPickingImage(Bitmap bitmap, int actionId) {
        if (bitmap != null)
            targetView.setImageBitmap(bitmap);
        // We have a confirmation dialog first before clearing the image.
//        else
//            targetView.setImageDrawable(null);
    }

    public Pair<Integer, Integer> calculateAspectRatio(View targetView) {
        int ratioW = targetView.getWidth();
        int ratioH = (int) Math.ceil(targetView.getWidth() / ((double)targetView.getWidth() / targetView.getHeight()));
        return new Pair<>(ratioW, ratioH);
    }

    // Image capture

    private void finishCapturingImage(final Bitmap bitmap) {
        TaskDialog.getInstance().showProcessingDialog(this);

        // Perform image operations on another thread.
        new Thread() {

            @Override
            public void run() {
                Uri uri = null;
                try {
                    byte[] bytes = BitmapUtils.getBitmapData(bitmap);

                    // Save full-size image to file.
                    uri = FileUtils.saveImageToShare(bytes);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Failed to retrieve image.", e);
                }
                final Uri imageUri = uri;

                // Continue on UI thread.
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        TaskDialog.getInstance().hideDialog();
                        if (imageUri != null)
                            finishCapturingImage(imageUri);
                    }
                });
            }
        }.start();
    }

    private void finishCapturingImage(final InputStream inputStream) {
        TaskDialog.getInstance().showProcessingDialog(this);

        // Perform image operations on another thread.
        new Thread() {

            @Override
            public void run() {
                Uri uri = null;
                try {
                    byte[] bytes = BitmapUtils.getBytes(inputStream);

                    // Save full-size image to file.
                    uri = FileUtils.saveImageToShare(bytes);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Failed to retrieve image.", e);
                }
                final Uri imageUri = uri;

                // Continue on UI thread.
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        TaskDialog.getInstance().hideDialog();
                        if (imageUri != null)
                            finishCapturingImage(imageUri);
                    }
                });
            }
        }.start();
    }

    private void finishCapturingImage(final Uri uri) {
        // Pause the calculation of the aspect ratio because of the interface orientation. The default
        // orientation in Android is landscape, so even though the camera is in portrait, when control
        // comes back to this activity, it will be landscape for a few ms, so we want to wait until
        // views are resized for portrait before calculating the aspect ratio.
        Utils.runWithDelay(new Runnable() {

            @Override
            public void run() {
                // Calculate aspect ratio.
                Pair<Integer, Integer> aspectRatio = calculateAspectRatio(targetView);

                // Start image cropper for saved image.
                CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(aspectRatio.first, aspectRatio.second)
                        .setFixAspectRatio(true)
                        .start(CameraUtilsActivity.this);
            }
        }, 300);
    }

    private void finishCroppingImage(final Uri imageResource) {
        TaskDialog.getInstance().showProcessingDialog(this);
        final File file = new File(imageResource.getPath());

        // Perform image processing on another thread.
        new Thread() {

            @Override
            public void run() {
                // Compress bitmap to fit the target view's bounds.
                final Bitmap bitmap = BitmapUtils.decodeSampledBitmapFileForSize(file, targetView.getWidth(), targetView.getHeight());
                FileUtils.cleanupFile(imageResource); // Cleanup after operations.

                // Continue on the UI thread.
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        TaskDialog.getInstance().hideDialog();
                        finishPickingImage(bitmap, actionId);
                    }
                });
            }
        }.run();
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Utils.isIntentAvailable(this, takePictureIntent)) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

            cameraImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    private void choosePicture() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        if (Utils.isIntentAvailable(this, getIntent) && Utils.isIntentAvailable(this, pickIntent)) {
            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, REQUEST_PICK_IMAGE);
        }
    }
}
