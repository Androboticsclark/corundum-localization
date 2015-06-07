package com.corundumPro.features.setting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.dao.DBAdapter;
import com.corundumPro.common.function.HomeSettingFunction;
import com.corundumPro.common.function.WebModeFunction;
import com.corundumPro.common.util.FileUtil;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.common.util.StringUtil;
import com.corundumPro.features.menu.MenuActivity;

/**
 * MainSettingActivityクラス
 * <p>
 * 「メインセッティングアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class MainSettingActivity extends BaseActivity {
	static final String TAG = "MainSettingActivity";

	/*
	 * 画面
	 */
	/** ラジオグループ1(アプリリスト、アプリを指定、URLを指定) */
	private RadioGroup main_setting_radioGroup1;

	/** URLエディットボックス */
	private EditText main_setting_editText1;

	/** プログレス */
	private ProgressBar main_setting_progressBar1;

	/** WEB表示 */
	private WebView main_setting_webView1;

	/** ラジオボタン2 */
	private RadioGroup main_setting_radioGroup2;

	/** オペレーションモード */
	private RadioButton main_setting_radio2;

	/** ステータスバーを非表示 */
	private CheckBox main_setting_checkBox1;

	/** ホワイトリストを使用する */
	private CheckBox main_setting_checkBox2;

	/** メニューボタン */
	private Button main_setting_button1;

	/** ホーム画面設定ファンクション */
	private HomeSettingFunction homeSettingFunction = new HomeSettingFunction();

	/** Webモードファンクション */
	private WebModeFunction webModeFunction = new WebModeFunction();

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

		/* Viewにレイアウト設定 */
		setContentView(R.layout.main_setting);

		/* View初期化 */
		initView();

		/* 画面のフォーカスを外す */
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Webモード設定
		webModeFunction.load(preference);
		if (DBAdapter.WEB_MODE_OPERATION == webModeFunction.getWebMode()) {
			// オペレーションモード
			main_setting_radioGroup2.check(R.id.main_setting_radio2);
		} else {
			// フルスクリーン
			main_setting_radioGroup2.check(R.id.main_setting_radio3);
		}
		// ステータスバー表示/非表示設定
		main_setting_checkBox1.setChecked(!webModeFunction.isVisileStatusBar());

		/* ホワイトリストフラグ取得 */
		if (true == preference.getBoolean(SystemInfo.KEY_WHITE_LIST_FLAG, false)) {
			main_setting_checkBox2.setChecked(true);
		} else {
			main_setting_checkBox2.setChecked(false);
		}

		editor.commit();

		LogUtil.d(TAG, "[OUT] onCreate()");
	}

	/**
	 * レジューム処理
	 * <p>
	 * onResume()のオーバーライド<br>
	 * アクティビティ表示時にコールされる。
	 * </p>
	 */
	@Override
	public void onResume() {
		LogUtil.d(TAG, "[IN ] onResume()");

		super.onResume();

		// 起動アイコン設定情報URLをもとにホームモードを決定
		int homeMode = homeSettingFunction.getHomeModeFromShortcutUrl(shortcutInfo.getHomeWebUrl());
		editor.putInt(SystemInfo.KEY_HOME_MODE, homeMode);

		// 選択されているホーム画面設定情報に変更
		homeSettingFunction.changeCurrentHomeSettingByHomeMode(preference.getInt(SystemInfo.KEY_HOME_MODE, DBAdapter.HOME_MODE_LIST));
		homeSettingFunction.setCurrentHomeUrl(shortcutInfo.getHomeWebUrl());

		// ホーム画面設定
		main_setting_editText1.setText(homeSettingFunction.getCurrentHomeUrl());
		((RadioButton)findViewById(homeSettingFunction.getCurrentRadioId())).setChecked(true);

		LogUtil.d(TAG, "[OUT] onResume()");
	}

	/**
	 * DB保存処理
	 * <p>
	 * 「システム情報」「ショートカット情報」「一時情報」のDBを保存する。
	 * </p>
	 */
    @Override
	public void saveDataBase() {
		LogUtil.d(TAG, "[IN ] saveDataBase()");

		// 現在のURL編集テキストボックス入力内容を保持
		homeSettingFunction.setCurrentHomeUrl(main_setting_editText1.getText().toString());

		// ショートカット用に現在のURLを保存
		shortcutInfo.setHomeWebUrl(homeSettingFunction.getCurrentHomeUrl());

		// ホーム画面設定情報を保存
		homeSettingFunction.save(editor);

		// WEBモード情報設定
		if (true == main_setting_radio2.isChecked()) {
			// オペレーション
			webModeFunction.setWebMode(DBAdapter.WEB_MODE_OPERATION);
			webModeFunction.setVisibleToolBar(true);
		} else {
			// フルスクリーン
			webModeFunction.setWebMode(DBAdapter.WEB_MODE_FULL);
			webModeFunction.setVisibleToolBar(false);
		}
		// ステータスバー非表示設定
		webModeFunction.setVisibleStatusBar(!main_setting_checkBox1.isChecked());
		// Webモード情報保存
		webModeFunction.save(editor);

		/* ホワイトリストフラグ保存 */
		if (true == main_setting_checkBox2.isChecked()) {
			editor.putBoolean(SystemInfo.KEY_WHITE_LIST_FLAG, true);
		} else {
			editor.putBoolean(SystemInfo.KEY_WHITE_LIST_FLAG, false);
		}

		editor.commit();

		super.saveDataBase();

		LogUtil.d(TAG, "[OUT] saveDataBase()");
	}

	/**
	 * View初期化処理
	 * <p>
	 * Viewの初期化を行う。
	 * </p>
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public void initView() {
		LogUtil.d(TAG, "[IN ] initView()");

		// ホーム画面設定情報登録
		homeSettingFunction.putHomeSetting(R.id.main_setting_radio0
											,DBAdapter.HOME_MODE_LIST
											,""
											,""
											,true);
		homeSettingFunction.putHomeSetting(R.id.main_setting_radio1
											,DBAdapter.HOME_MODE_URL
											,preference.getString(SystemInfo.KEY_HOME_HTML_PATH, Const.PROTOCOL_HTTP)
											,Const.PROTOCOL_HTTP
											,false);
		homeSettingFunction.putHomeSetting(R.id.main_setting_radio1_app
											,DBAdapter.HOME_MODE_APP
											,preference.getString(SystemInfo.KEY_HOME_APP_PATH, FileUtil.getBaseDirPath(getApplicationContext()))
											,Const.PROTOCOL_FILE + FileUtil.getBaseDirPath(getApplicationContext())
											,false);

		/* ラジオグループ1 */
		main_setting_radioGroup1 = (RadioGroup)findViewById(R.id.main_setting_radioGroup1);
		main_setting_radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			/**
			 * ホーム画面設定ラジオボタン変更時の処理
			 */
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				LogUtil.d(TAG, "[IN ] onCheckedChanged()");
				LogUtil.d(TAG, "checkedId:" + checkedId);

				// ホーム画面設定情報更新
				homeSettingFunction.setCurrentHomeUrl(main_setting_editText1.getText().toString());
				homeSettingFunction.changeCurrentHomeSettingByRadioId(checkedId);

				// ホーム画面設定表示
				main_setting_editText1.setEnabled(homeSettingFunction.isEnableEditUrl());
				main_setting_webView1.setVisibility(homeSettingFunction.getVisiblePreview());

				// WebView更新
				refreshWebview();

				LogUtil.d(TAG, "[OUT] onCheckedChanged()");
			}
		});

		/* URLエディットボックス*/
		main_setting_editText1 = (EditText)findViewById(R.id.main_setting_editText1);
		main_setting_editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			/**
			 * URLエディットボックスフォーカス移動時の処理
			 */
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				LogUtil.d(TAG, "[IN ] onFocusChange()");
				LogUtil.d(TAG, "hasFocus:" + hasFocus);

				if (true == hasFocus) {
					/* フォーカス受け取った時 */
				} else {
					/* フォーカスが離れた時 */
					homeSettingFunction.setCurrentHomeUrl(main_setting_editText1.getText().toString());
					// WebView更新
					refreshWebview();
				}

				LogUtil.d(TAG, "[OUT] onFocusChange()");
			}
		});

		/* プログレス*/
		main_setting_progressBar1 = (ProgressBar)findViewById(R.id.main_setting_progressBar1);

		/* WEB表示 */
		main_setting_webView1 = (WebView)findViewById(R.id.main_setting_webView1);

		main_setting_webView1.getSettings().setJavaScriptEnabled(true);
		main_setting_webView1.getSettings().setDomStorageEnabled(true);
		main_setting_webView1.getSettings().setUseWideViewPort(true);
		main_setting_webView1.getSettings().setLoadWithOverviewMode(true);

		main_setting_webView1.setWebViewClient(new WebViewClient(){
			/**
			 * リダイレクト時の処理<br>
			 * WebView表示時に指定したURLでリダイレクトされた場合に呼ばれる。
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView webView, String url) {
				LogUtil.d(TAG, "[IN ] shouldOverrideUrlLoading()");
				LogUtil.d(TAG, "url:" + url);

				// リダイレクト先のurlを取得
				// URLをブランクで表示しようとした場合、リダイレクト先はabout:blankになる
				String redirectUrl = url;
				if (StringUtil.isEmptyWithAboutBlank(redirectUrl)) {
					// リダイレクト先URLがブランクまたはabout:blankの場合、ブランクとする。
					redirectUrl = "";
				}
				// ホーム画面設定情報にはリダイレクト先URLを保持する
				homeSettingFunction.setCurrentHomeUrl(redirectUrl);

				LogUtil.d(TAG, "return true");
				LogUtil.d(TAG, "[OUT] shouldOverrideUrlLoading()");
				return super.shouldOverrideUrlLoading(webView, url);
			}

			/* ページ読み込み開始 */
			@Override
			public void onPageStarted(WebView webview, String url, Bitmap favicon) {
				super.onPageStarted(webview, url, favicon);
				LogUtil.d(TAG, "[IN ] onPageStarted()");
				LogUtil.d(TAG, "url:" + url);

				main_setting_editText1.setText(homeSettingFunction.getCurrentHomeUrl());

				/* プログレス表示 */
				main_setting_progressBar1.setVisibility(View.VISIBLE);

				LogUtil.d(TAG, "[OUT] onPageStarted()");
			}

			/* ページ読み込み完了 */
			@Override
			public void onPageFinished(WebView webview, String url) {
				super.onPageFinished(webview, url);
				LogUtil.d(TAG, "[IN ] onPageFinished()");
				LogUtil.d(TAG, "url:" + url);

				// リダイレクトでURLが異なっている場合があるので、URLエディットボックス更新
				main_setting_editText1.setText(homeSettingFunction.getCurrentHomeUrl());

				/* プログレス非表示 */
				main_setting_progressBar1.setVisibility(View.GONE);

				LogUtil.d(TAG, "[OUT] onPageFinished()");
			}
		});

		/* ラジオボタン2 */
		main_setting_radioGroup2 = (RadioGroup)findViewById(R.id.main_setting_radioGroup2);

		/* オペレーションモード */
		main_setting_radio2 = (RadioButton)findViewById(R.id.main_setting_radio2);

		/* ステータスバーを非表示 */
		main_setting_checkBox1 = (CheckBox)findViewById(R.id.main_setting_checkBox1);

		/* ホワイトリストを使用する */
		main_setting_checkBox2 = (CheckBox)findViewById(R.id.main_setting_checkBox2);

		/* メニューボタン */
		main_setting_button1 = (Button)findViewById(R.id.main_setting_button1);
		main_setting_button1.setOnClickListener(menuListener);

		LogUtil.d(TAG, "[OUT] initView()");
	}

	/**
	 * WebView更新処理
	 * <p>
	 * WebViewの更新を行う。
	 * </p>
	 */
	public void refreshWebview() {
		LogUtil.d(TAG, "[IN ] refreshWebview()");

		// 表示するホーム画面URL取得
		String url = homeSettingFunction.getCurrentHomeUrl();
		LogUtil.d(TAG, "homeUrl:" + url);

		if (StringUtil.isEmpty(url)) {
			// 表示するホーム画面URLがブランクの場合、about:blankに設定
			url = Const.ABOUT_BLANK;
		}
		LogUtil.d(TAG, "url:" + url);

		// WebView表示
		main_setting_webView1.loadUrl(url);

		LogUtil.d(TAG, "[OUT] refreshWebview()");
	}

	/**
	 * 一時停止時処理
	 * <p>
	 * onPause()のオーバーライド<br>
	 * アクティビティ一時停止時にコールされる。
	 * </p>
	 */
	@Override
	public void onPause() {
		super.onPause();
		LogUtil.d(TAG, "[IN ] onPause()");

		/* DB保存 */
		saveDataBase();

		LogUtil.d(TAG, "[OUT] onPause()");
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

			/* MenuActivity起動 */
			LogUtil.d(TAG, "MenuActivity");
			Intent intent = new Intent(this, MenuActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();
		}

		result = super.onKeyDown(keyCode, event);

		LogUtil.d(TAG, "result" + result);
		LogUtil.d(TAG, "[OUT] onKeyDown()");
		return result;
	}

	/**
	 * メニューボタンリスナ処理
	 * <p>
	 * メニューボタンが押下された時にコールされる。<br>
	 * MenuActivityを起動する。
	 * </p>
	 */
	private View.OnClickListener menuListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] menuListener::onClick()");

			/* MenuActivity起動 */
			LogUtil.d(TAG, "MenuActivity");
			Intent intent = new Intent(MainSettingActivity.this, MenuActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] menuListener::onClick()");
		}
	};
}
