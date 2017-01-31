package com.graffitab.ui.activities.home.me.edit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.graffitab.R;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.ui.dialog.handlers.OnOkHandler;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.api.ApiUtils;
import com.graffitab.utils.input.InputValidator;
import com.graffitab.utils.input.KeyboardUtils;
import com.graffitabsdk.config.GTSDK;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.common.result.GTEditPasswordResult;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 08/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class EditPasswordActivity extends AppCompatActivity {

    @BindView(R.id.password) MaterialEditText password;
    @BindView(R.id.newPassword) MaterialEditText newPassword;
    @BindView(R.id.confirmPassword) MaterialEditText confirmPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        setupTopBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        ActivityUtils.colorMenu(this, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            KeyboardUtils.hideKeyboard(this);
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.action_save) {
            Log.i(getClass().getSimpleName(), "Changing password");
            String pw = password.getText().toString();
            String npw = newPassword.getText().toString();
            String cpw = confirmPassword.getText().toString();

            if (InputValidator.validateChangePassword(this, pw, npw, cpw)) {
                KeyboardUtils.hideKeyboard(this);
                TaskDialog.getInstance().showDialog(null, this, null);

                GTSDK.getMeManager().editPassword(pw, npw, new GTResponseHandler<GTEditPasswordResult>() {

                    @Override
                    public void onSuccess(GTResponse<GTEditPasswordResult> responseObject) {
                        Log.i(getClass().getSimpleName(), "Successfully changed password");
                        TaskDialog.getInstance().hideDialog();
                        DialogBuilder.buildOKDialog(EditPasswordActivity.this, getString(R.string.app_name), getString(R.string.change_password_success), new OnOkHandler() {

                            @Override
                            public void onClickOk() {
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onFailure(GTResponse<GTEditPasswordResult> responseObject) {
                        Log.e(getClass().getSimpleName(), "Failed to edit password");
                        TaskDialog.getInstance().hideDialog();
                        DialogBuilder.buildOKDialog(EditPasswordActivity.this, getString(R.string.app_name), ApiUtils.localizedErrorReason(responseObject));
                    }
                });
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Setup

    private void setupTopBar() {
        getSupportActionBar().setTitle(getString(R.string.edit_profile_change_password));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
