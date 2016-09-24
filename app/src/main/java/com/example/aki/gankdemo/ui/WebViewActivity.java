package com.example.aki.gankdemo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.aki.gankdemo.R;

/**
 * Created by Aki on 2016/9/24.
 */

public class WebViewActivity extends AppCompatActivity {

    public static final String NEWS_URL = "news_url";
    public static final String NEWS_TITLE = "news_title";

    private String mNewsUrl;
    private String mNewsTitle;
    private WebView mWebView;
    private Toolbar mToolbar;

    public static Intent newIntent (Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(NEWS_URL, url);
        intent.putExtra(NEWS_TITLE, title);
        return intent;
    }


    private void parseIntent() {
        Intent intent = getIntent();
        mNewsUrl = intent.getStringExtra(NEWS_URL);
        mNewsTitle = intent.getStringExtra(NEWS_TITLE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent();
        setContentView(R.layout.activity_web);

        initToolbar();
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mNewsUrl);



    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.web_tb);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle(mNewsTitle);
        Log.d("WebViewActivity", mNewsTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
