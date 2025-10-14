package com.chaoji.im.ui.xpop.pay;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chaoji.common.R;
import com.chaoji.other.blankj.utilcode.util.AppUtils;
import com.chaoji.other.blankj.utilcode.util.ColorUtils;
import com.chaoji.other.blankj.utilcode.util.Logs;
import com.chaoji.other.blankj.utilcode.util.ScreenUtils;
import com.chaoji.other.hjq.toast.Toaster;
import com.chaoji.other.xpopup.XPopup;
import com.chaoji.other.xpopup.core.CenterPopupView;
import com.chaoji.other.xpopup.impl.FullScreenPopupView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;

public class XpopPayWebView extends CenterPopupView {
    private ScheduledExecutorService executorService;
    private TextView titleTextView;
    private String payUrl;
    private WebView webview;
    private ConstraintLayout _layout;

    public XpopPayWebView(@NonNull Context context) {
        super(context);
    }

    public XpopPayWebView(@NonNull AppCompatActivity context, String payUrl) {
        super(context);
        this.payUrl = payUrl;
    }

    @Override
    protected int getImplLayoutId() {
        popupInfo.isDismissOnTouchOutside = false;
        popupInfo.isDismissOnBackPressed = false;
        return R.layout.mod_pay_webview;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate() {
        super.onCreate();

        _layout = findViewById(R.id.webCTLayout);
        ImageView cacheClose = findViewById(R.id.ivCancel);
        ProgressBar progressBar = findViewById(R.id.progress);
        titleTextView = findViewById(R.id.title_text);
        cacheClose.setOnClickListener(view -> showDialog());


        View webBg = findViewById(R.id.web_bg);
        webview = findViewById(R.id.webview);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        webview.getSettings().setJavaScriptEnabled(true);
        // webview.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小 webview.getSettings().setDatabasePath(getCacheDir().getAbsolutePath());
        String userAgentString = webview.getSettings().getUserAgentString();
        webview.getSettings().setUserAgentString(userAgentString);
        //webview.getSettings().setSupportZoom(true); webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        WebView.setWebContentsDebuggingEnabled(true);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    if (title.length() > 10) {
                        title = title.substring(0, 10).concat("...");
                    }
                }
                titleTextView.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //Logs.e("process："+ newProgress);
                try {
                    progressBar.setProgress(newProgress);
                    if (newProgress == 100) {
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                return true;
            }

        });
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Logs.e("onPageFinished：" + url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("alipays://platformapi")) {
                    if (!AppUtils.isAppInstalled("com.eg.android.AlipayGphone")) {
                        Toaster.show("请先下载安装支付宝");
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        getContext().startActivity(intent);
                    }
                    return true;
                } else if (url.startsWith("weixin://wap/pay?")) {
                    if (!AppUtils.isAppInstalled("com.tencent.mm")) {
                        Toaster.show("请先下载安装微信");
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        getContext().startActivity(intent);
                        //exit.setText("温馨提示：支付成功将自动跳转，请耐心等待，您也可以选择提前退出，稍后查询充值结果。");
                    }
                    return true;
                } else if (url.startsWith("i939l3://")) {
                    if (!AppUtils.isAppInstalled("com.mc.bourse")) {
                        Toaster.show("请先下载安装YESGO");
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        getContext().startActivity(intent);
                        //exit.setText("温馨提示：支付成功将自动跳转，请耐心等待，您也可以选择提前退出，稍后查询充值结果。");
                    }
                    return true;
                } else {
//                    if (payInfo.getPayType() == PLAY_TYPE_WX) {
//                        HashMap<String, String> extraHeaders = new HashMap();
//                        extraHeaders.put("Referer", "https://cyfapi.chuanyf.com");
//                        view.loadUrl(url, extraHeaders);
//                        return true;
//                    } else {
                    view.loadUrl(url);
                    //}
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                if (webResourceRequest.isForMainFrame()) {
                    Logs.e(webResourceRequest.getUrl());
                }
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
            }
        });
        webview.loadUrl(payUrl);

//        if (payInfo.getPayType().equals(PAY_ALI)) {
//            webBg.setVisibility(VISIBLE);
//        }

    }


    private void showDialog() {
        dismiss();
//        new XPopup.Builder(getContext())
//                .isDestroyOnDismiss(true)
//                .hasStatusBar(true)
//                .animationDuration(5)
//                .navigationBarColor(ColorUtils.getColor(R.color.xpop_shadow_color))
//                .isLightStatusBar(true)
//                .hasNavigationBar(true)
//                .asConfirm(
//                        "提示", "如已支付，查询到支付结果以后将自动跳转！\n支付暂未完成，是否退出等待？",
//                        "等待", "退出", this::dismiss, this::dismiss, false, R.layout.xpopup_confirm
//                ).show();

    }



    @Override
    protected void doAfterShow() {
        super.doAfterShow();
    }



    @SuppressLint("SimpleDateFormat")
    private static void insertImage(Bitmap bitmap, Context context) {
        Date currentDate = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmmss");
        String name = "/myScreen_" + date.format(currentDate);
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, name, "");
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
        if (_layout != null && webview != null) {
            _layout.removeView(webview);
            webview.removeAllViews();
            webview.destroy();
        }

    }


    @Override
    protected int getPopupHeight() {
        return (int) (ScreenUtils.getScreenHeight() * 1f);
    }

    @Override
    protected int getPopupWidth() {
        return (int) (ScreenUtils.getScreenWidth() * 1f);
    }

}
