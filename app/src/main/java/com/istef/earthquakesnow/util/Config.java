package com.istef.earthquakesnow.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.istef.earthquakesnow.R;

public class Config {

    public static void showWebDetail(Activity context, String url) {

        View view = context.getLayoutInflater().inflate(R.layout.detail_web, null);
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();
        WebView webView = view.findViewById(R.id.wvDetail);
        ImageButton btnClose = view.findViewById(R.id.btnCloseDetailWeb);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(url);
        btnClose.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();
    }
}
