package com.graffitab.utils.api;

import android.content.Context;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitabsdk.network.common.GTResultCode;
import com.graffitabsdk.network.common.response.GTResponse;

/**
 * Created by georgichristov on 16/01/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ApiUtils {

    public static String localizedErrorReason(GTResponse response) {
        Context context = MyApplication.getInstance();

        if (response.getResultCode() == GTResultCode.UNSUPPORTED_FILE_TYPE) {
            return context.getString(R.string.api_error_unsupported_file_type);
        }
        if (response.getResultCode() == GTResultCode.INVALID_TOKEN) {
            return context.getString(R.string.api_error_invalid_token);
        }
        if (response.getResultCode() == GTResultCode.STREAM_COULD_NOT_BE_READ) {
            return context.getString(R.string.api_error_stream_could_not_be_read);
        }
        if (response.getResultCode() == GTResultCode.INVALID_ARGUMENT) {
            return context.getString(R.string.api_error_invalid_argument);
        }
        if (response.getResultCode() == GTResultCode.MISSING_ARGUMENT) {
            return context.getString(R.string.api_error_missing_argument);
        }
        if (response.getResultCode() == GTResultCode.INVALID_JSON) {
            return context.getString(R.string.api_error_invalid_json);
        }
        if (response.getResultCode() == GTResultCode.INVALID_FOLLOWEE) {
            return context.getString(R.string.api_error_invalid_followee);
        }
        if (response.getResultCode() == GTResultCode.EMPTY_MANDATORY_FIELD) {
            return context.getString(R.string.api_error_empty_mandatory_field);
        }
        if (response.getResultCode() == GTResultCode.INVALID_USERNAME) {
            return context.getString(R.string.api_error_invalid_username);
        }
        if (response.getResultCode() == GTResultCode.INVALID_EMAIL) {
            return context.getString(R.string.api_error_invalid_email);
        }
        if (response.getResultCode() == GTResultCode.USERNAME_ALREADY_IN_USE) {
            return context.getString(R.string.api_error_username_already_in_use);
        }
        if (response.getResultCode() == GTResultCode.EMAIL_ALREADY_IN_USE) {
            return context.getString(R.string.api_error_email_already_in_use);
        }
        if (response.getResultCode() == GTResultCode.INVALID_ID) {
            return context.getString(R.string.api_error_invalid_id);
        }


        if (response.getResultCode() == GTResultCode.USER_NOT_LOGGED_IN) {
            return context.getString(R.string.api_error_user_not_logged_in);
        }
        if (response.getResultCode() == GTResultCode.USER_NOT_OWNER) {
            return context.getString(R.string.api_error_user_not_owner);
        }


        if (response.getResultCode() == GTResultCode.INCORRECT_PASSWORD) {
            return context.getString(R.string.api_error_incorrect_password);
        }
        if (response.getResultCode() == GTResultCode.MAXIMUM_LOGIN_ATTEMPTS) {
            return context.getString(R.string.api_error_max_login_attempts);
        }
        if (response.getResultCode() == GTResultCode.FORBIDDEN_RESOURCE) {
            return context.getString(R.string.api_error_forbidden);
        }


        if (response.getResultCode() == GTResultCode.EXTERNAL_PROVIDER_NOT_FOUND) {
            return context.getString(R.string.api_error_external_provider_not_found);
        }
        if (response.getResultCode() == GTResultCode.EXTERNAL_PROVIDER_NOT_LINKED) {
            return context.getString(R.string.api_error_external_provider_not_linked);
        }
        if (response.getResultCode() == GTResultCode.DEVICE_NOT_FOUND) {
            return context.getString(R.string.api_error_device_not_found);
        }
        if (response.getResultCode() == GTResultCode.USER_NOT_FOUND) {
            return context.getString(R.string.api_error_user_not_found);
        }
        if (response.getResultCode() == GTResultCode.ASSET_NOT_FOUND) {
            return context.getString(R.string.api_error_asset_not_found);
        }
        if (response.getResultCode() == GTResultCode.STREAMABLE_NOT_FOUND) {
            return context.getString(R.string.api_error_streamable_not_found);
        }
        if (response.getResultCode() == GTResultCode.COMMENT_NOT_FOUND) {
            return context.getString(R.string.api_error_comment_not_found);
        }
        if (response.getResultCode() == GTResultCode.LOCATION_NOT_FOUND) {
            return context.getString(R.string.api_error_location_not_found);
        }
        if (response.getResultCode() == GTResultCode.TOKEN_NOT_FOUND) {
            return context.getString(R.string.api_error_token_not_found);
        }


        if (response.getResultCode() == GTResultCode.TOKEN_EXPIRED) {
            return context.getString(R.string.api_error_token_expired);
        }
        if (response.getResultCode() == GTResultCode.USER_NOT_IN_EXPECTED_STATE) {
            return context.getString(R.string.api_error_user_not_in_expected_state);
        }


        if (response.getResultCode() == GTResultCode.DEVICE_ALREADY_EXISTS) {
            return context.getString(R.string.api_error_device_already_exists);
        }
        if (response.getResultCode() == GTResultCode.EXTERNAL_PROVIDER_ALREADY_LINKED) {
            return context.getString(R.string.api_error_external_provider_already_linked);
        }
        if (response.getResultCode() == GTResultCode.EXTERNAL_PROVIDER_ALREADY_LINKED_FOR_OTHER_USER) {
            return context.getString(R.string.api_error_external_provider_already_linked_for_another_user);
        }


        if (response.getResultCode() == GTResultCode.GENERAL_ERROR) {
            return context.getString(R.string.api_error_general);
        }


        if (response.getResultCode() == GTResultCode.OTHER) {
            return context.getString(R.string.api_error_other);
        }
            
        return context.getString(R.string.api_error);
    }
}
