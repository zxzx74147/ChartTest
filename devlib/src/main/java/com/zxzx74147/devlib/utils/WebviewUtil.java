package com.zxzx74147.devlib.utils;

import android.app.Activity;
import android.content.Context;

import com.thefinestartist.finestwebview.FinestWebView;
import com.thefinestartist.finestwebview.listeners.WebViewListener;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class WebviewUtil {
    public static void showWebActivity(Context activity, String url) {
        FinestWebView.Builder builder = new FinestWebView.Builder(activity);
        builder.addWebViewListener(mWebViewListener);
        builder.disableIconMenu(true);
        builder.showUrl(false);
        builder.show(url);
    }

    private static WebViewListener mWebViewListener = new WebViewListener() {
        @Override
        public void onProgressChanged(int progress) {
            super.onProgressChanged(progress);
        }

        @Override
        public void onReceivedTitle(String title) {
            super.onReceivedTitle(title);
        }

        @Override
        public void onReceivedTouchIconUrl(String url, boolean precomposed) {
            super.onReceivedTouchIconUrl(url, precomposed);
        }

        @Override
        public void onPageStarted(String url) {
            super.onPageStarted(url);
        }

        @Override
        public void onPageFinished(String url) {
            super.onPageFinished(url);
        }

        @Override
        public void onLoadResource(String url) {
            super.onLoadResource(url);
        }

        @Override
        public void onPageCommitVisible(String url) {
            super.onPageCommitVisible(url);
        }

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
            super.onDownloadStart(url, userAgent, contentDisposition, mimeType, contentLength);
        }
    };


}
