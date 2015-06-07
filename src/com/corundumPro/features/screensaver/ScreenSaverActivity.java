package com.corundumPro.features.screensaver;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.util.LogUtil;

/**
 * ScreenSaverActivityクラス
 * <p>
 * 「スクリーンセーバーアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class ScreenSaverActivity extends BaseActivity {
	static final String TAG = "ScreenSaverActivity";

	/*
	 * 画面
	 */
	/** WEB表示 */
	ScreenSaverWebView screen_saver_webView1;

	/**
	 * 作成処理
	 * <p>
	 * onCreate()のオーバーライド<br>
	 * アクティビティ生成時にコールされる。
	 * </p>
	 * @param bundle バンドル
	 */
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		LogUtil.d(TAG, "[IN ] onCreate()");

		/* タイトル非表示 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* 画面のフォーカスを外す */
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		/* Viewにレイアウト設定 */
		setContentView(R.layout.screen_saver);

		/* View初期化 */
		initViews();

		/* WebView更新 */
		refreshWebview(preference.getString(SystemInfo.KEY_SCREEN_SAVER_URL, ""));

		LogUtil.d(TAG, "[OUT] onCreate()");
	}

	/**
	 * View初期化処理
	 * <p>
	 * Viewの初期化を行う。
	 * </p>
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public void initViews() {
		LogUtil.d(TAG, "[IN ] initViews()");

		/* WEB表示 */
		screen_saver_webView1 = (ScreenSaverWebView)findViewById(R.id.screen_saver_webView1);
		screen_saver_webView1.setActivity(this);
		screen_saver_webView1.requestFocus();
		screen_saver_webView1.getSettings().setJavaScriptEnabled(true);
		screen_saver_webView1.getSettings().setDomStorageEnabled(true);
		screen_saver_webView1.getSettings().setUseWideViewPort(true);
		screen_saver_webView1.getSettings().setLoadWithOverviewMode(true);

		screen_saver_webView1.setWebViewClient(new WebViewClient() {
			/* ページ読み込み開始 */
			@Override
			public void onPageStarted(WebView webView, String url, Bitmap favicon) {
				super.onPageStarted(webView, url, favicon);
				LogUtil.d(TAG, "[IN ] onPageStarted()");
				LogUtil.d(TAG, "url:" + url);
				LogUtil.d(TAG, "[OUT] onPageStarted()");
			}

			/* ページ読み込み完了 */
			@Override
			public void onPageFinished(WebView webView, String url) {
				super.onPageFinished(webView, url);
				LogUtil.d(TAG, "[IN ] onPageFinished()");
				LogUtil.d(TAG, "url:" + url);
				LogUtil.d(TAG, "[OUT] onPageFinished()");
			}

			/* リダイレクト */
			public boolean shouldOverrideUrlLoading(WebView webView, String url) {
				LogUtil.d(TAG, "[IN ] shouldOverrideUrlLoading()");
				LogUtil.d(TAG, "url:" + url);

				/* WebView更新 */
				refreshWebview(url);

				LogUtil.d(TAG, "return true");
				LogUtil.d(TAG, "[OUT] shouldOverrideUrlLoading()");
				return true;
			}
		});

		LogUtil.d(TAG, "[OUT] initViews()");
	}

	/**
	 * WebView更新処理
	 * <p>
	 * WebViewの更新を行う。
	 * </p>
	 * @param url URL
	 */
	public void refreshWebview(String url) {
		LogUtil.d(TAG, "[IN ] refreshWebview()");
		LogUtil.d(TAG, "url:" + url);

		/* 更新 */
		screen_saver_webView1.loadUrl(url);

		LogUtil.d(TAG, "[OUT] refreshWebview()");
	}
}
