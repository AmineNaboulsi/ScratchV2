package com.aadevelopers.onlyscratchiapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.aadevelopers.onlyscratchiapp.R;
import com.aadevelopers.onlyscratchiapp.utils.Constant;

public class PolicyActivity extends AppCompatActivity {

    private String type = "", url = "";
    private PolicyActivity activity;
    private WebView webView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        activity = this;
        swipeRefreshLayout = findViewById(R.id.swipe);
        webView = findViewById(R.id.webView);
        type = getIntent().getStringExtra("type");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (type != null) {
            switch (type) {
                case "Privacy Policy":
                    url = getResources().getString(R.string.privacy_policy_link);
                    break;
                case "Instruction":
                    url = getResources().getString(R.string.instruction_link);
                    break;
                case "About Us":
                    url = getResources().getString(R.string.about_link);
                    break;
            }
            try {
                TextView titleText = toolbar.findViewById(R.id.toolbarText);
                titleText.setText(type);
                LinearLayout lyt=toolbar.findViewById(R.id.points_lyt);
                lyt.setVisibility(View.GONE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            onClick();
        } else {
            Constant.showToastMessage(activity, "Something Went Wrong...");
        }
    }

    private void onClick() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                LoadPage(url);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadPage(url);
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void LoadPage(String Url) {
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                swipeRefreshLayout.setRefreshing(progress != 100);
            }
        });
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(Url);
    }

    private static class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String myUrl) {
            view.loadUrl(myUrl);
            return false;
        }
    }
}