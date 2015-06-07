/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.features.editor;

import java.io.File;

import org.apache.commons.io.FileUtils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.dao.DBAdapter;
import com.corundumPro.common.exception.ArcAppException;
import com.corundumPro.common.exception.ArcRuntimeException;
import com.corundumPro.common.function.FileFunction;
import com.corundumPro.common.function.WebModeFunction;
import com.corundumPro.common.layout.GlassPaneLayout;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.common.util.StringUtil;

/**
 * EditorPreviewActivityクラス
 * <p>
 * 「コランダムエディタプレビューアクティビティ」クラス
 * </p>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class EditorPreviewActivity extends BaseActivity {
	static final String TAG = "EditorPreviewActivity";

	/** URLバー */
	private LinearLayout editorPreviewLinearLayout1;

	/** URL入力エディットボックス */
	private EditText editorPreviewEditText1;

	/** リロードボタン */
	private ImageButton editorPreviewImageButton1;

	/** WEB表示 */
	private EditorPreviewWebView editorPreviewWebView1;

	/** ツールバー */
	private LinearLayout editorPreviewLinearLayout2;

	/** ホームボタン */
	private ImageButton editorPreviewImageButton2;

	/** Prevボタン */
	private ImageButton editorPreviewImageButton3;

	/** Nextボタン */
	private ImageButton editorPreviewImageButton4;

	/** リロードボタン */
	private ImageButton editorPreviewImageButton5;

	/** キャンセルボタン */
	private ImageButton editorPreviewImageButton6;

	/** プログレス */
	private ProgressBar editorPreviewProgressBar1;

	/** オプションメニューボタン */
	private ImageButton editorPreviewImageButton7;

	/** グラスペイン */
	GlassPaneLayout editorPreviewGlassPane1;

	/** Webモードファンクション */
	private WebModeFunction webModeFunction = new WebModeFunction();

	/** プレビュー用ファイルパス */
	private String previewFilePath = "";

	/** 選択されたWebモード */
	private int selectedWebMode = -1;

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
		LogUtil.d(TAG, "[IN ] onCreate()");

		super.onCreate(bundle);

		// タイトル非表示
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 画面のフォーカスを外す
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Viewにレイアウト設定
		setContentView(R.layout.editor_preview);

		// View初期化
		initViews();

		// ファイルパス取得
		String filePath = getIntent().getStringExtra(SystemInfo.KEY_EDITOR_PREVIEW_URL);
		LogUtil.d(TAG, "filePath:" + filePath);

		// コンテンツ取得
		String contents = getIntent().getStringExtra(SystemInfo.KEY_EDITOR_PREVIEW_CONTENTS);

		// 選択されたWebモード取得
		this.selectedWebMode = getIntent().getIntExtra(SystemInfo.KEY_SELECTED_WEB_MODE, DBAdapter.WEB_MODE_OPERATION);
		LogUtil.d(TAG, "selectedWebMode:" + String.valueOf(selectedWebMode));

		// プレビュー用に一時ファイルを作成する
		FileFunction fileFunction = new FileFunction();
		try {
			this.previewFilePath = fileFunction.makeTemporaryFile(filePath, contents);
			LogUtil.d(TAG, "previewFilePath:" + String.valueOf(previewFilePath));

		} catch (ArcAppException e) {
			LogUtil.e(TAG, getString(e.getResourceId()) + e.getMessage());
			throw new ArcRuntimeException(e);
		}

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

		// 選択されたWebモードを設定
		webModeFunction.setWebMode(selectedWebMode);

		// 画面表示設定
		String filePathWithProtocol = Const.PROTOCOL_FILE + previewFilePath;
		editorPreviewEditText1.setText(filePathWithProtocol);	// URL設定
		editorPreviewLinearLayout1.setVisibility(webModeFunction.getVisibleUrlBar());	// URLバー表示設定
		editorPreviewLinearLayout2.setVisibility(webModeFunction.getVisileToolBar());	// ツールバー表示設定
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
							,webModeFunction.getVisibleStatusBar());					// フルスクリーン表示設定


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

		// プレビュー用一時ファイルを削除
		if (!FileUtils.deleteQuietly(new File(previewFilePath)) ) {
			LogUtil.w(TAG, getString(R.string.log_err_delete_file) + ":" + previewFilePath);
		}
		LogUtil.d(TAG, "[OUT] onDestroy()");
	}

	/**
	 * View初期化処理
	 * <p>
	 * Viewの初期化を行う。
	 * </p>
	 */
	@SuppressLint("SetJavaScriptEnabled")
	protected void initViews() {
		LogUtil.d(TAG, "[IN ] initViews()");

		/* 上部URLバー */
		editorPreviewLinearLayout1 = (LinearLayout)findViewById(R.id.editor_preview_LinearLayout1);

		/* URL入力エディットボックス */
		editorPreviewEditText1 = (EditText)findViewById(R.id.editor_preview_editText1);

		/* リロードボタン */
		editorPreviewImageButton1 = (ImageButton)findViewById(R.id.editor_preview_imageButton1);
		editorPreviewImageButton1.setEnabled(true);

		// WEB表示設定
		editorPreviewWebView1 = (EditorPreviewWebView)findViewById(R.id.editor_preview_webView1);
		editorPreviewWebView1.requestFocus();
		editorPreviewWebView1.getSettings().setJavaScriptEnabled(true);
		editorPreviewWebView1.getSettings().setDomStorageEnabled(true);
		editorPreviewWebView1.getSettings().setUseWideViewPort(true);
		editorPreviewWebView1.getSettings().setLoadWithOverviewMode(true);

		editorPreviewWebView1.setWebViewClient(new WebViewClient() {
			/**
			 * ページ読み込み開始処理
			 */
			@Override
			public void onPageStarted(WebView webView, String url, Bitmap favicon) {
				super.onPageStarted(webView, url, favicon);
				LogUtil.d(TAG, "[IN ] onPageStarted()");
				LogUtil.d(TAG, "url:" + url);

				// ツールバー表示初期化
				initToolBar(webModeFunction.getVisileToolBar());

				LogUtil.d(TAG, "[OUT] onPageStarted()");
			}

			/**
			 * ページ読み込み完了処理
			 */
			@Override
			public void onPageFinished(WebView webView, String url) {
				super.onPageFinished(webView, url);
				LogUtil.d(TAG, "[IN ] onPageFinished()");
				LogUtil.d(TAG, "url:" + url);

				// ツールバー表示
				showToolBar(webModeFunction.getVisileToolBar());

				LogUtil.d(TAG, "[OUT] onPageFinished()");
			}

			/**
			 * リダイレクト処理
			 */
			public boolean shouldOverrideUrlLoading(WebView webView, String url) {
				LogUtil.d(TAG, "[IN ] shouldOverrideUrlLoading()");
				LogUtil.d(TAG, "url:" + url);
				LogUtil.d(TAG, "return true");
				LogUtil.d(TAG, "[OUT] shouldOverrideUrlLoading()");
				return true;
			}
		});

		/* ツールバー */
		editorPreviewLinearLayout2 = (LinearLayout) findViewById(R.id.editor_preview_LinearLayout2);

		/* ホームボタン */
		editorPreviewImageButton2 = (ImageButton) findViewById(R.id.editor_preview_imageButton2);

		/* Prevボタン */
		editorPreviewImageButton3 = (ImageButton) findViewById(R.id.editor_preview_imageButton3);

		/* Nextボタン */
		editorPreviewImageButton4 = (ImageButton) findViewById(R.id.editor_preview_imageButton4);

		/* リロードボタン */
		editorPreviewImageButton5 = (ImageButton) findViewById(R.id.editor_preview_imageButton5);

		/* キャンセルボタン */
		editorPreviewImageButton6 = (ImageButton) findViewById(R.id.editor_preview_imageButton6);

		/* プログレス */
		editorPreviewProgressBar1 = (ProgressBar) findViewById(R.id.editor_preview_progressBar1);

		/* オプションメニューボタン */
		editorPreviewImageButton7 = (ImageButton) findViewById(R.id.editor_preview_imageButton7);

		/** グラスペイン */
		editorPreviewGlassPane1 = (GlassPaneLayout)this.findViewById(R.id.editor_preview_glass_pane1);
		editorPreviewGlassPane1.setOnTouchListener(glassPaneListener);

		LogUtil.d(TAG, "[OUT] initViews()");
	}

	/**
	 * WebView更新処理
	 * <p>
	 * WebViewの更新を行う。
	 * </p>
	 * @param url URL
	 */
	protected void refreshWebview(String url) {
		LogUtil.d(TAG, "[IN ] refreshWebview()");
		LogUtil.d(TAG, "url:" + url);

		// WebView更新
		editorPreviewWebView1.loadUrl(url);

		LogUtil.d(TAG, "[OUT] refreshWebview()");
	}

	/**
	 * ツールバー表示初期化<br>
	 *
	 * @param visible 表示可:View.VISIBLE, 表示不可:View.GONE
	 */
	protected void initToolBar(int visible) {
		LogUtil.d(TAG, "[IN ] initToolBar()");

		if (visible == View.VISIBLE) {
			// ホームボタン
			editorPreviewImageButton2.setEnabled(true);
			// Prevボタン
			editorPreviewImageButton3.setEnabled(false);
			// Nextボタン
			editorPreviewImageButton4.setEnabled(false);
			// リロードボタン
			editorPreviewImageButton5.setEnabled(true);
			// キャンセルボタン
			editorPreviewImageButton6.setEnabled(true);
			// オプションメニューボタン
			editorPreviewImageButton7.setEnabled(true);
			// プログレス
			editorPreviewProgressBar1.setVisibility(View.VISIBLE);
		}
		LogUtil.d(TAG, "[OUT] initToolBar()");
	}

	/**
	 * ツールバー表示<br>
	 *
	 * @param visible 表示可:View.VISIBLE, 表示不可:View.GONE
	 */
	protected void showToolBar(int visible) {
		LogUtil.d(TAG, "[IN ] showToolBar()");

		if (visible == View.VISIBLE) {
			// ホームボタン
			editorPreviewImageButton2.setEnabled(true);
			// Prevボタン
			editorPreviewImageButton3.setEnabled(editorPreviewWebView1.canGoBack());
			// Nextボタン
			editorPreviewImageButton4.setEnabled(editorPreviewWebView1.canGoForward());
			// リロードボタン
			editorPreviewImageButton5.setEnabled(true);
			// キャンセルボタン
			editorPreviewImageButton6.setEnabled(false);
			// オプションメニューボタン
			editorPreviewImageButton7.setEnabled(true);
			// プログレス非表示
			editorPreviewProgressBar1.setVisibility(View.GONE);

		}
		LogUtil.d(TAG, "[OUT] showToolBar()");
	}

	/**
	 * GlassPaneタッチリスナ処理
	 * <p>
	 * GlassPaneがタッチされた時にコールされる。<br>
	 * </p>
	 */
	private View.OnTouchListener glassPaneListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			LogUtil.d(TAG, "[IN ] glassPaneListener::onTouch()");
			view.performClick();
			finish();
			LogUtil.d(TAG, "[OUT] glassPaneListener::onTouch()");
			return true;
		}
	};
}
