package com.es.easysupervise.activity;

import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.es.easysupervise.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_discover)
public class DiscoverActivity extends AppCompatActivity {

    @ViewById
    TextView tvTitle;
    @ViewById
    WebView webDiscover;

    @AfterViews
    void init()
    {
        tvTitle.setText("发现");
        webDiscover.loadUrl("http://www.taobao.com");
        webDiscover.setWebViewClient(new WebViewClient() {
            // Load opened URL in the application instead of standard browser
            // application
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webDiscover.setWebChromeClient(new WebChromeClient() {
            // Set progress bar during loading
            public void onProgressChanged(WebView view, int progress) {
                DiscoverActivity.this.setProgress(progress * 100);
            }
        });

        // Enable some feature like Javascript and pinch zoom
        WebSettings websettings = webDiscover.getSettings();
        websettings.setJavaScriptEnabled(true);     // Warning! You can have XSS vulnerabilities!
        websettings.setBuiltInZoomControls(true);
    }

    @Click
    void imgLeft()
    {
        this.finish();
    }


}
