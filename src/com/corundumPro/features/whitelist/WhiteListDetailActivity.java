package com.corundumPro.features.whitelist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.dao.DBAdapter;
import com.corundumPro.common.dto.WhiteListInfo;
import com.corundumPro.common.util.LogUtil;

/**
 * WhiteListDetailActivityクラス
 * <p>
 * 「ホワイトリストディテールアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class WhiteListDetailActivity extends BaseActivity {
	static final String TAG = "WhiteListDetailActivity";

	/*
	 * アイテムID
	 */
	private int itemId;

	/*
	 * DB
	 */
	private DBAdapter dbAdapter;
	private WhiteListInfo whiteListInfo;

	/*
	 * 画面
	 */
	/** URLエディットボックス */
	private EditText white_list_detail_editText1;

	/** ラジオボタン */
	private RadioGroup white_list_detail_radioGroup1;

	/** URL完全一致 */
	private RadioButton white_list_detail_radio0;

	/** ページタイトルエディットボックス */
	private EditText white_list_detail_editText2;

	/** プログレス */
	private ProgressBar white_list_detail_progressBar1;

	/** WebView */
	private WebView white_list_detail_webView1;

	/** 設定ボタン */
	private Button white_list_detail_Button1;

	/** キャンセルボタン */
	private Button white_list_detail_Button2;

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

		/* アイテムID受け取り */
		Intent intent = getIntent();
		String data = intent.getStringExtra("itemId");
		itemId = Integer.parseInt(data);
		LogUtil.d(TAG, "itemId:" + itemId);

		/* DB初期化 */
		dbAdapter = new DBAdapter(this);
		whiteListInfo = new WhiteListInfo();

		/* ホワイトリストデータ読み出し */
		if (0 != itemId) {
			loadDataBase(itemId);
		}

		/* Viewにレイアウト設定 */
		setContentView(R.layout.white_list_detail);

		/* View初期化 */
		initViews();

		/* URL設定 */
		white_list_detail_editText1.setText(whiteListInfo.getWhiteListUrl());

		/* モード設定 */
		if (DBAdapter.WHITE_LIST_MODE_FULL_URL == whiteListInfo.getWhiteListMode()) {
			/* URL完全一致 */
			white_list_detail_radioGroup1.check(R.id.white_list_detail_radio0);
		} else {
			/* URL以下全て */
			white_list_detail_radioGroup1.check(R.id.white_list_detail_radio1);
		}

		/* ページタイトル設定 */
		white_list_detail_editText2.setText(whiteListInfo.getWhiteListTitle());

		/* プログレス非表示 */
		white_list_detail_progressBar1.setVisibility(View.GONE);

		/* プレビュー更新 */
		refreshWebview(whiteListInfo.getWhiteListUrl());

		LogUtil.d(TAG, "[OUT] onCreate()");
	}

	/**
	 * DB読み出し処理
	 * <p>
	 * 「ホワイトリスト情報」のDBを読み出す。
	 * </p>
	 * @param itemId アイテムID
	 */
	public void loadDataBase(int itemId) {
		LogUtil.d(TAG, "[IN ] loadDataBase()");
		LogUtil.d(TAG, "itemId:" + itemId);

		/* DBオープン */
		dbAdapter.open();

		/* 「ホワイトリスト情報」テーブル読み出し */
		dbAdapter.getWhiteListInfo(itemId, whiteListInfo);

		/* DBクローズ */
		dbAdapter.close();

		LogUtil.d(TAG, "[OUT] loadDataBase()");
	}

	/**
	 * DB保存処理
	 * <p>
	 * 「システム情報」「ショートカット情報」「一時情報」のDBを保存する。
	 * </p>
	 */
	public void saveDataBase() {
		LogUtil.d(TAG, "[IN ] saveDataBase()");

		whiteListInfo.setWhiteListTitle(white_list_detail_editText2.getText().toString());
		whiteListInfo.setWhiteListUrl(white_list_detail_editText1.getText().toString());

		/* モード */
		if (true == white_list_detail_radio0.isChecked()) {
			whiteListInfo.setWhiteListMode(DBAdapter.WHITE_LIST_MODE_FULL_URL);
		} else {
			whiteListInfo.setWhiteListMode(DBAdapter.WHITE_LIST_MODE_URL);
		}

		/* DBオープン */
		dbAdapter.open();

		if (0 != itemId) {
			/* UPDATE */
			dbAdapter.updateWhiteListInfo(whiteListInfo);
		} else {
			/* INSERT */
			dbAdapter.insertWhiteListInfo(whiteListInfo);
		}

		/* DBクローズ */
		dbAdapter.close();

		LogUtil.d(TAG, "[OUT] saveDataBase()");
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

		/* URLエディットボックス */
		white_list_detail_editText1 = (EditText) findViewById(R.id.white_list_detail_editText1);
		white_list_detail_editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				LogUtil.d(TAG, "[IN ] onFocusChange()");
				LogUtil.d(TAG, "hasFocus:" + hasFocus);

				if (true == hasFocus) {
					/* フォーカスを受け取った時 */
				} else {
					/* フォーカスが離れた時 */
					refreshWebview(white_list_detail_editText1.getText().toString());
				}

				LogUtil.d(TAG, "[OUT] onFocusChange()");
			}
		});

		/* ラジオボタン */
		white_list_detail_radioGroup1 = (RadioGroup)findViewById(R.id.white_list_detail_radioGroup1);

		/* URL完全一致 */
		white_list_detail_radio0 = (RadioButton)findViewById(R.id.white_list_detail_radio0);

		/* ページタイトルエディットボックス */
		white_list_detail_editText2 = (EditText)findViewById(R.id.white_list_detail_editText2);

		/* プログレス */
		white_list_detail_progressBar1 = (ProgressBar)findViewById(R.id.white_list_detail_progressBar1);

		/* WEB表示 */
		white_list_detail_webView1 = (WebView)findViewById(R.id.white_list_detail_webView1);
		white_list_detail_webView1.getSettings().setJavaScriptEnabled(true);
		white_list_detail_webView1.getSettings().setDomStorageEnabled(true);
		white_list_detail_webView1.getSettings().setUseWideViewPort(true);
		white_list_detail_webView1.getSettings().setLoadWithOverviewMode(true);
		white_list_detail_webView1.setWebViewClient(new WebViewClient(){
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

			/* ページ読み込み開始 */
			@Override
			public void onPageStarted(WebView webView, String url, Bitmap favicon) {
				super.onPageStarted(webView, url, favicon);
				LogUtil.d(TAG, "[IN ] onPageStarted()");
				LogUtil.d(TAG, "url:" + url);

				/* プログレス表示 */
				white_list_detail_progressBar1.setVisibility(View.VISIBLE);

				LogUtil.d(TAG, "[OUT] onPageStarted()");
			}

			/* ページ読み込み完了 */
			@Override
			public void onPageFinished(WebView webView, String url) {
				super.onPageFinished(webView, url);
				LogUtil.d(TAG, "[IN ] onPageFinished()");
				LogUtil.d(TAG, "url:" + url);

				/* URLバーの更新(ページがリダイレクトされた時の救済措置) */
				white_list_detail_editText1.setText(white_list_detail_webView1.getUrl());

				/* プログレス非表示 */
				white_list_detail_progressBar1.setVisibility(View.GONE);

				LogUtil.d(TAG, "[OUT] onPageFinished()");
			}
		});

		/* 設定ボタン */
		white_list_detail_Button1 = (Button)findViewById(R.id.white_list_detail_Button1);
		white_list_detail_Button1.setOnClickListener(saveListener);

		/* キャンセルボタン */
		white_list_detail_Button2 = (Button)findViewById(R.id.white_list_detail_Button2);
		white_list_detail_Button2.setOnClickListener(cancelListener);

		LogUtil.d(TAG, "[OUT] initViews()");
	}

	/**
	 * 設定ボタンリスナ処理
	 * <p>
	 * 設定ボタンが押下された時にコールされる。<br>
	 * 編集中内容の保存を行う。
	 * </p>
	 */
	private View.OnClickListener saveListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] saveListener()");

			/* 保存 */
			saveDataBase();

			/* 終了 */
			setResult(RESULT_OK);
			finish();

			LogUtil.d(TAG, "[OUT] saveListener()");
		}
	};

	/**
	 * キャンセルボタンリスナ処理
	 * <p>
	 * キャンセルボタンが押下された時にコールされる。<br>
	 * 編集中の内容を破棄する。
	 * </p>
	 */
	private View.OnClickListener cancelListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] cancelListener()");

			/* 終了 */
			setResult(RESULT_OK);
			finish();

			LogUtil.d(TAG, "[OUT] cancelListener()");
		}
	};

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

		if ((null != url) && (0 < url.length())) {
			white_list_detail_webView1.loadUrl(url);
		}

		LogUtil.d(TAG, "[OUT] refreshWebview()");
	}

	/**
	 * 物理ボタン押下イベント処理
	 * <p>
	 * onKeyDown()のオーバーライド<br>
	 * 物理ボタン押下時にコールされる。
	 * </p>
	 * @param keyCode ボタンのキーコード
	 * @param event イベントオブジェクト
	 * @return 処理結果
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtil.d(TAG, "[IN ] onKeyDown()");
		LogUtil.d(TAG, "keyCode:" + keyCode);
		LogUtil.d(TAG, "event:" + event.getAction());

		boolean result;

		/* 戻るボタン処理 */
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			/* 終了 */
			setResult(RESULT_OK);
			finish();
		}

		result = super.onKeyDown(keyCode, event);

		LogUtil.d(TAG, "result:" + result);
		LogUtil.d(TAG, "[OUT] onKeyDown()");
		return result;
	}
}
