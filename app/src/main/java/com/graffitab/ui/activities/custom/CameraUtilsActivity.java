package com.graffitab.ui.activities.custom;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.cocosw.bottomsheet.BottomSheet;
import com.graffitab.R;
import com.graffitab.utils.Utils;
import com.graffitab.utils.display.BitmapUtils;
import com.graffitab.utils.file.FileUtils;
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
                    byte[] bytes = BitmapUtils.getBitmapData(imageBitmap);
                    finishCapturingImage(bytes);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Failed to retrieve taken image.", e);
                }
            }
            else // Error
                Log.e(getClass().getSimpleName(), "Failed to retrieve captured image. ResultCode is not OK");
        }
        else if (requestCode == REQUEST_PICK_IMAGE) {
            if (resultCode == RESULT_OK) { // Success
                if (data == null) {
                    Log.e(getClass().getSimpleName(), "Failed to retrieve chose image. Data is null");
                    return;
                }

                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    byte[] bytes = BitmapUtils.getBytes(inputStream);
                    finishCapturingImage(bytes);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Failed to retrieve chose image.", e);
                }
            }
            else // Error
                Log.e(getClass().getSimpleName(), "Failed to retrieve chosen image. ResultCode is not OK");
        }
    }

    /**
     * Shows a dialog to prompt for the source of the image.
     *
     * @param view target view.
     */
    public void showImagePicker(ImageView view) {
        targetView = view;

        BottomSheet.Builder builder = buildImagePickerSheet();
        builder = builder.listener(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == R.id.action_take_new)
                    takePicture();
                else if (which == R.id.action_choose)
                    choosePicture();
                else if (which == R.id.action_remove)
                    finishPickingImage(null);
            }
        });
        builder.show();
    }

    /**
     * Builds the image source dialog.
     */
    public BottomSheet.Builder buildImagePickerSheet() {
        return new BottomSheet.Builder(this, R.style.BottomSheet_StyleDialog)
                .title(R.string.other_select_image)
                .sheet(R.menu.menu_camera_normal);
    }

    public void finishPickingImage(Bitmap bitmap) {
        if (bitmap != null)
            targetView.setImageBitmap(bitmap);
        else
            targetView.setImageDrawable(null);
    }

    // Image capture

    private void finishCapturingImage(byte[] bytes) {
        // Save full-size image to file.
        final Uri uri = FileUtils.saveImageToCrop(bytes);

        // Pause the calculation of the aspect ratio because of the interface orientation. The default
        // orientation in Android is landscape, so even though the camera is in portrait, when control
        // comes back to this activity, it will be landscape for a few ms, so we want to wait until
        // views are resized for portrait before calculating the aspect ratio.
        Utils.runWithDelay(new Runnable() {

            @Override
            public void run() {
                // Calculate aspect ratio.
                int ratioW = targetView.getWidth();
                int ratioH = (int) Math.ceil(targetView.getWidth() / ((double)targetView.getWidth() / targetView.getHeight()));

                // Start image cropper for saved image.
                CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(ratioW, ratioH)
                        .setFixAspectRatio(true)
                        .start(CameraUtilsActivity.this);
            }
        }, 300);
    }

    private void finishCroppingImage(Uri imageResource) {
        File file = new File(imageResource.getPath());

        // Compress bitmap to fit the target view's bounds.
        Bitmap bitmap = BitmapUtils.decodeSampledBitmapFileForSize(file, targetView.getWidth(), targetView.getHeight());
        finishPickingImage(bitmap);
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, REQUEST_PICK_IMAGE);
    }
}
