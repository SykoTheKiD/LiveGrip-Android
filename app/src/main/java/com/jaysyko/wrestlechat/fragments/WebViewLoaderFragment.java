package com.jaysyko.wrestlechat.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.webBrowser.WebBrowser;

public class WebViewLoaderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        getActivity().setTitle("r/SquaredCircle");
        WebView webView = (WebView) view.findViewById(R.id.webView1);
        webView.setWebViewClient(new WebBrowser());
        webView.loadUrl("https://m.reddit.com/r/squaredcircle");
        return view;
    }
}
