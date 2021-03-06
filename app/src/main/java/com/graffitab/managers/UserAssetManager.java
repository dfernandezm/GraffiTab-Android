package com.graffitab.managers;

import android.app.Activity;
import android.graphics.Bitmap;

import com.graffitab.R;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.OnYesNoHandler;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.utils.api.ApiUtils;
import com.graffitabsdk.model.GTExternalProvider;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.common.result.GTActionCompleteResult;
import com.graffitabsdk.network.service.assets.response.GTAssetResponse;
import com.graffitabsdk.sdk.GTSDK;

/**
 * Created by georgichristov on 10/02/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class UserAssetManager {

    public static void editAvatar(final Activity context, Bitmap bitmap) {
        editAvatar(context, bitmap, null);
    }

    public static void editAvatar(final Activity context, Bitmap bitmap, final OnAssetUpdatedListener listener) {
        TaskDialog.getInstance().showProcessingDialog(context);
        GTSDK.getMeManager().editAvatar(bitmap, new GTResponseHandler<GTAssetResponse>() {

            @Override
            public void onSuccess(GTResponse<GTAssetResponse> gtResponse) {
                TaskDialog.getInstance().hideDialog();
                DialogBuilder.buildOKToast(context, context.getString(R.string.profile_change_avatar_success));

                if (listener != null)
                    listener.onAssetUpdated();
            }

            @Override
            public void onFailure(GTResponse<GTAssetResponse> gtResponse) {
                TaskDialog.getInstance().hideDialog();
                DialogBuilder.buildAPIErrorDialog(context, context.getString(R.string.app_name),
                        ApiUtils.localizedErrorReason(gtResponse), true, gtResponse.getResultCode());
            }
        });
    }

    public static void importAvatar(final Activity context, GTExternalProvider.GTExternalProviderType type) {
        importAvatar(context, type, null);
    }

    public static void importAvatar(final Activity context, GTExternalProvider.GTExternalProviderType type, final OnAssetUpdatedListener listener) {
        TaskDialog.getInstance().showProcessingDialog(context);
        GTSDK.getMeManager().importAvatar(type, new GTResponseHandler<GTAssetResponse>() {

            @Override
            public void onSuccess(GTResponse<GTAssetResponse> gtResponse) {
                TaskDialog.getInstance().hideDialog();
                DialogBuilder.buildOKToast(context, context.getString(R.string.profile_change_avatar_success));

                if (listener != null)
                    listener.onAssetUpdated();
            }

            @Override
            public void onFailure(GTResponse<GTAssetResponse> gtResponse) {
                TaskDialog.getInstance().hideDialog();
                DialogBuilder.buildAPIErrorDialog(context, context.getString(R.string.app_name),
                        ApiUtils.localizedErrorReason(gtResponse), true, gtResponse.getResultCode());
            }
        });
    }

    public static void deleteAvatar(final Activity context) {
        deleteAvatar(context, null);
    }

    public static void deleteAvatar(final Activity context, final OnAssetUpdatedListener listener) {
        DialogBuilder.buildYesNoDialog(context, context.getString(R.string.app_name), context.getString(R.string.other_confirm_delete), context.getString(R.string.other_delete), context.getString(R.string.other_cancel), new OnYesNoHandler() {

            @Override
            public void onClickYes() {
                TaskDialog.getInstance().showProcessingDialog(context);
                GTSDK.getMeManager().deleteAvatar(new GTResponseHandler<GTActionCompleteResult>() {

                    @Override
                    public void onSuccess(GTResponse<GTActionCompleteResult> gtResponse) {
                        TaskDialog.getInstance().hideDialog();
                        DialogBuilder.buildOKToast(context, context.getString(R.string.profile_change_avatar_success));

                        if (listener != null)
                            listener.onAssetUpdated();
                    }

                    @Override
                    public void onFailure(GTResponse<GTActionCompleteResult> gtResponse) {
                        TaskDialog.getInstance().hideDialog();
                        DialogBuilder.buildAPIErrorDialog(context, context.getString(R.string.app_name),
                                ApiUtils.localizedErrorReason(gtResponse), true, gtResponse.getResultCode());
                    }
                });
            }

            @Override
            public void onClickNo() {}
        });
    }

    public static void editCover(final Activity context, Bitmap bitmap) {
        editCover(context, bitmap, null);
    }

    public static void editCover(final Activity context, Bitmap bitmap, final OnAssetUpdatedListener listener) {
        TaskDialog.getInstance().showProcessingDialog(context);
        GTSDK.getMeManager().editCover(bitmap, new GTResponseHandler<GTAssetResponse>() {

            @Override
            public void onSuccess(GTResponse<GTAssetResponse> gtResponse) {
                TaskDialog.getInstance().hideDialog();
                DialogBuilder.buildOKToast(context, context.getString(R.string.profile_change_cover_success));

                if (listener != null)
                    listener.onAssetUpdated();
            }

            @Override
            public void onFailure(GTResponse<GTAssetResponse> gtResponse) {
                TaskDialog.getInstance().hideDialog();
                DialogBuilder.buildAPIErrorDialog(context, context.getString(R.string.app_name),
                        ApiUtils.localizedErrorReason(gtResponse), true, gtResponse.getResultCode());
            }
        });
    }

    public static void deleteCover(final Activity context) {
        deleteCover(context, null);
    }

    public static void deleteCover(final Activity context, final OnAssetUpdatedListener listener) {
        DialogBuilder.buildYesNoDialog(context, context.getString(R.string.app_name), context.getString(R.string.other_confirm_delete), context.getString(R.string.other_delete), context.getString(R.string.other_cancel), new OnYesNoHandler() {

            @Override
            public void onClickYes() {
                TaskDialog.getInstance().showProcessingDialog(context);
                GTSDK.getMeManager().deleteCover(new GTResponseHandler<GTActionCompleteResult>() {

                    @Override
                    public void onSuccess(GTResponse<GTActionCompleteResult> gtResponse) {
                        TaskDialog.getInstance().hideDialog();
                        DialogBuilder.buildOKToast(context, context.getString(R.string.profile_change_cover_success));

                        if (listener != null)
                            listener.onAssetUpdated();
                    }

                    @Override
                    public void onFailure(GTResponse<GTActionCompleteResult> gtResponse) {
                        TaskDialog.getInstance().hideDialog();
                        DialogBuilder.buildAPIErrorDialog(context, context.getString(R.string.app_name),
                                ApiUtils.localizedErrorReason(gtResponse), true, gtResponse.getResultCode());
                    }
                });
            }

            @Override
            public void onClickNo() {}
        });
    }

    public interface OnAssetUpdatedListener {
        void onAssetUpdated();
    }
}
