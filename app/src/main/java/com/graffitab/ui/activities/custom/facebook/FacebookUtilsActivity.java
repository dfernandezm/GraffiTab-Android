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
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by georgichristov on 28/10/2016
 *
 * Copyright © GraffiTab Inc. 2016
 */
public class FacebookUtilsActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private FacebookLoginCallback loginCallback;

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

    public void facebookLogin(boolean forceLogin, FacebookLoginCallback callback) {
        loginCallback = callback;

        if (forceLogin)
            facebookLogout();

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

    public void getMe(final FacebookMeCallback callback) {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        final Profile profile = Profile.getCurrentProfile();

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String email = object.getString("email");

                    if (callback != null)
                        callback.profileFetched(profile.getFirstName(), profile.getLastName(), profile.getId(), email, accessToken.getToken());
                } catch (JSONException e) {
                    e.printStackTrace();
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
                    loginCallback.loginComplete();
            }

            @Override
            public void onCancel() {
                Log.i(getClass().getSimpleName(), "Facebook login cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(getClass().getSimpleName(), "Facebook login failed - " + exception.getMessage());
            }
        });
    }

    // Callbacks.

    public interface FacebookLoginCallback {

        public void loginComplete();
    }

    public interface FacebookMeCallback {

        public void profileFetched(String firstName, String lastName, String userId, String email, String accessToken);
    }
}
