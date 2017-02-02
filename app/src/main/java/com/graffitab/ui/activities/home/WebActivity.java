package com.graffitab.ui.activities.home;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.graffitab.R;
import com.graffitab.constants.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 12/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class WebActivity extends AppCompatActivity {

    @BindView(R.id.webView) WebView webView;

    private String htmlFile;
    private String title;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            htmlFile = extras.getString(Constants.EXTRA_HTML_FILE);
            title = extras.getString(Constants.EXTRA_TITLE);
        }
        else {
            finish();
            return;
        }

        setupTopBar();
        setupWebView();

        loadHtml();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Loading

    private void loadHtml() {
        webView.loadUrl("file:///android_asset/html/" + htmlFile);
    }

    // Setup

    private void setupTopBar() {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupWebView() {
        webView.setWebChromeClient(new WebChromeClient());

        allowAccess(webView);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void allowAccess(WebView wvReader) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            wvReader.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
    }
}
