/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.features.editor;

import org.apache.commons.io.FilenameUtils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.common.util.StringUtil;

/**
 * ImagePreviewActivityクラス
 * <p>
 * 「画像プレビューアクティビティ」クラス
 * </p>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class ImagePreviewActivity extends BaseActivity {
	static final String TAG = "ImagePreviewActivity";

	/** プレビュー用画像パス */
	String previeImagePath = "";

	/*
	 * 画面
	 */
	/** WEB表示 */
	ImagePreviewWebView image_preview_webView1;

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
		setContentView(R.layout.image_preview);

		/* View初期化 */
		initViews();

		// 画像パス取得
		previeImagePath = getIntent().getStringExtra(SystemInfo.KEY_IMAGE_PREVIEW_URL);
		LogUtil.d(TAG, "previeImagePath:" + previeImagePath);

		LogUtil.d(TAG, "[OUT] onCreate()");
	}

	/**
	 * レジューム処理
	 * <p>
	 * onResume()のオーバーライド<br>
	 * アクティビティ再開時にコールされる。
	 * </p>
	 */
	@Override
	public void onResume() {
		LogUtil.d(TAG, "[IN ] onResume()");
		super.onResume();

		// 画面表示設定
		String filePathWithProtocol = Const.PROTOCOL_FILE + previeImagePath;

		// WebView更新
		refreshWebview(StringUtil.nvl(filePathWithProtocol, ""));

		LogUtil.d(TAG, "[OUT] onResume()");
	}

	/**
	 * フラグメントレジューム処理
	 * <p>
	 * onResumeFragments()のオーバーライド<br>
	 * アクティビティ表示時にコールされる。
	 * </p>
	 */
	@Override
	public void onResumeFragments() {
		LogUtil.d(TAG, "[IN ] onResumeFragments()");
		super.onResumeFragments();
		LogUtil.d(TAG, "[OUT] onResumeFragments()");
	}

	/**
	 * 一時停止処理
	 * <p>
	 * onPause()のオーバーライド<br>
	 * アクティビティ一時停止時にコールされる。
	 * </p>
	 */
	@Override
	public void onPause() {
		LogUtil.d(TAG, "[IN ] onPause()");
		super.onPause();
		LogUtil.d(TAG, "[OUT] onPause()");
	}

	/**
	 * 停止処理
	 * <p>
	 * onStop()のオーバーライド<br>
	 * アクティビティ停止時にコールされる。
	 * </p>
	 */
	@Override
	public void onStop() {
		LogUtil.d(TAG, "[IN ] onStop()");
		super.onStop();
		LogUtil.d(TAG, "[OUT] onStop()");
	}

	/**
	 * 終了処理
	 * <p>
	 * onDestroy()のオーバーライド<br>
	 * アクティビティ終了時にコールされる。
	 * </p>
	 */
	@Override
	public void onDestroy() {
		LogUtil.d(TAG, "[IN ] onDestroy()");
		super.onDestroy();
		LogUtil.d(TAG, "[OUT] onDestroy()");
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
		image_preview_webView1 = (ImagePreviewWebView)findViewById(R.id.image_preview_webView1);
		image_preview_webView1.setActivity(this);
		image_preview_webView1.requestFocus();
		image_preview_webView1.getSettings().setJavaScriptEnabled(true);
		image_preview_webView1.getSettings().setDomStorageEnabled(true);
		image_preview_webView1.getSettings().setUseWideViewPort(true);
		image_preview_webView1.getSettings().setLoadWithOverviewMode(true);

		image_preview_webView1.setWebViewClient(new WebViewClient() {
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

				// WebView更新
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

		// WebView更新
		String baseUrl = FilenameUtils.getFullPath(url);
		String imageFileName = FilenameUtils.getName(url);
		image_preview_webView1.loadDataWithBaseURL(	baseUrl
													,makeHtml(imageFileName)
													,"text/html"
													,Const.CHARSET_UTF8
													,null);

		LogUtil.d(TAG, "[OUT] refreshWebview()");
	}

	/**
	 * 画像表示用HTMLを作成します。
	 *
	 * @param imageFilePath
	 * @return 画像表示用HTML
	 */
	private String makeHtml(String imageFilePath) {
		StringBuilder sb = new StringBuilder();

		sb.append("<html>");
		sb.append("<head></head>");
		sb.append("<body>");
		sb.append(	"<table height=\"100%\" width=\"100%\" cellpadding=\"0\">");
		sb.append(		"<tr>");
		sb.append(			"<td align=\"center\" valign=\"middle\">");
		sb.append(				"<img src=\"").append(imageFilePath).append("\" alt=\"img\" border=\"1\">");
		sb.append(			"</td>");
		sb.append(		"</tr>");
		sb.append(	"</table>");
		sb.append("</body>");
		sb.append("</html>");

		return sb.toString();
	}
}
