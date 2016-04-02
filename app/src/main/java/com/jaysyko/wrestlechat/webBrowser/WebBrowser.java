package com.jaysyko.wrestlechat.webBrowser;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author Jay Syko
 */
public class WebBrowser extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

}
