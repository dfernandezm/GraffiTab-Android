package com.graffitab.ui.activities.custom.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.graffitab.R;
import com.graffitab.ui.dialog.TaskDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by georgichristov on 11/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class FacebookUtilsActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private FacebookLoginCallback loginCallback;
    protected boolean forceLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupCallbacks();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (callbackManager.onActivityResult(requestCode, resultCode, data))
            return;
    }

    public void getFacebookMe(boolean forceLogin, final FacebookMeCallback callback) {
        // Setup callbacks.
        this.forceLogin = forceLogin;
        this.loginCallback = new FacebookLoginCallback() {

            @Override
            public void onSuccess(LoginResult result) {
                TaskDialog.getInstance().showDialog(getString(R.string.other_processing), FacebookUtilsActivity.this, null);
                getMe(callback);
            }

            @Override
            public void onCancel() {
                if (callback != null)
                    callback.onCancel();
            }

            @Override
            public void onError(FacebookException exception) {
                if (callback != null)
                    callback.onError(exception);
            }
        };

        if (forceLogin) // Logout if necessary;
            facebookLogout();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) // We already have a token, so use it to login.
            getMe(callback);
        else // No token yet or it has been cleared, so obtain a new token.
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile", "user_friends"));
    }

    public void facebookLogout() {
        if (AccessToken.getCurrentAccessToken() == null)
            return; // Already logged out.

        LoginManager.getInstance().logOut();
    }

    public boolean isLoggedInToFacebook() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    private void getMe(final FacebookMeCallback callback) {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String name = object.getString("name");
                    String[] nameComponents = name.split(" ");
                    String firstName = nameComponents[0];
                    String lastName = nameComponents[1];
                    String email = object.getString("email");
                    String id = object.getString("id");

                    if (callback != null)
                        callback.onProfileFetched(firstName, lastName, id, email, accessToken.getToken());
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (callback != null)
                        callback.onError(new FacebookException(e));
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    // Setup

    private void setupCallbacks() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(getClass().getSimpleName(), "Facebook login complete");
                if (loginCallback != null)
                    loginCallback.onSuccess(loginResult);
            }

            @Override
            public void onCancel() {
                Log.i(getClass().getSimpleName(), "Facebook login cancelled");
                if (loginCallback != null)
                    loginCallback.onCancel();
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(getClass().getSimpleName(), "Facebook login failed - " + exception.getMessage());
                if (loginCallback != null)
                    loginCallback.onError(exception);
            }
        });
    }

    interface FacebookLoginCallback {
        void onSuccess(LoginResult result);
        void onCancel();
        void onError(FacebookException exception);
    }

    public interface FacebookMeCallback {
        void onProfileFetched(String firstName, String lastName, String userId, String email, String accessToken);
        void onCancel();
        void onError(FacebookException exception);
    }
}
