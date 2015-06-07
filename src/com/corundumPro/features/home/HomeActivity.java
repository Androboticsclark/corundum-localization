package com.corundumPro.features.home;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.dao.DBAdapter;
import com.corundumPro.common.dialog.OkDialog;
import com.corundumPro.common.dto.WhiteListInfo;
import com.corundumPro.common.exception.ArcAppException;
import com.corundumPro.common.exception.ArcBaseException;
import com.corundumPro.common.exception.ArcRuntimeException;
import com.corundumPro.common.function.WebModeFunction;
import com.corundumPro.common.util.CheckUtil;
import com.corundumPro.common.util.FileUtil;
import com.corundumPro.common.util.ImageUtil;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.common.util.NotificationUtil;
import com.corundumPro.common.util.StringUtil;
import com.corundumPro.features.actionsheet.ActionSheetActivity;
import com.corundumPro.features.appsmanager.ApplicationListActivity;
import com.corundumPro.features.menu.MenuActivity;
import com.corundumPro.features.screensaver.ScreenSaverReceivedActivity;

/**
 * HomeActivityクラス
 * <p>
 * 「ホームアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class HomeActivity extends BaseActivity implements SensorEventListener {
	static final String TAG = "HomeActivity";

	/*
	 * 画面
	 */
	/** URLバー */
	private LinearLayout home_LinearLayout1;

	/** URL入力エディットボックス */
	private EditText home_editText1;

	/** リロードボタン */
	private ImageButton home_imageButton1;

	/** WEB表示 */
	private HomeWebView home_webView1;

	/** ツールバー */
	private LinearLayout home_LinearLayout2;

	/** ホームボタン */
	private ImageButton home_imageButton2;

	/** Prevボタン */
	private ImageButton home_imageButton3;

	/** Nextボタン */
	private ImageButton home_imageButton4;

	/** リロードボタン */
	private ImageButton home_imageButton5;

	/** キャンセルボタン */
	private ImageButton home_imageButton6;

	/** プログレス */
	private ProgressBar home_progressBar1;

	/** オプションメニューボタン */
	private ImageButton home_imageButton7;

	/*
	 * コランダムAPI用
	 */
	/** センサーマネージャー */
	private SensorManager sensorManager;

	/** 加速度センサー(存在有無)フラグ */
	private boolean isSensorAccelerometer;

	/** 加速度センサー(有効/無効)フラグ */
	private boolean enableSensorAccelerometer;

	/** シェイクカウント数 */
	private int shakeCount;

	/** 前回シェイク時間 */
	private long shakeBeforeTime;

	/** シェイク座標(X軸) */
	private float shakeBeforeX;

	/** シェイク座標(Y軸) */
	private float shakeBeforeY;

	/** シェイク座標(Z軸) */
	private float shakeBeforeZ;

	/** リサイズ後のサイズ(横幅) */
	private int imageWidth;

	/** リサイズ後のサイズ(縦幅) */
	private int imageHight;

	/** 画像ファイル(非圧縮) */
	private File originalPicFile;

	/** 画像ファイル(リサイズ後) */
	private File resizePicFile;

	/** トリミング有無フラグ(false時は強制リサイズ) */
	private boolean isTrimming;

	/** 画像ファイル保存有無フラグ(非圧縮) */
	private boolean isSaveOrgPicFile;

	/** 画像ファイル保存有無フラグ(リサイズ後) */
	private boolean isSaveResPicFile;

	/** URLスキームテキスト */
	private String urlSchemeText;

	/*
	 * スクリーンセーバー
	 */
	/** アラームマネージャー */
	private AlarmManager screenSaverAlarmManager;

	/** ペンディングインテント */
	private PendingIntent screenSaverPendingIntent;

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

		/* スクリーンセーバー開始 */
		startScreenSaver();

		/* 画面のフォーカスを外す */
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		/* Viewにレイアウト設定 */
		setContentView(R.layout.home);

		/* View初期化 */
		initViews();

		/* WebView表示処理 */
		home_webView1.setActivity(this);

		/* センサー初期化 */
		initSensor();

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

		// Webモードロード
		webModeFunction.loadAsCurrent(preference);

		// 画面表示設定
		home_LinearLayout1.setVisibility(webModeFunction.getVisibleUrlBar());	// URLバー表示設定
		home_LinearLayout2.setVisibility(webModeFunction.getVisileToolBar());	// ツールバー表示設定
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
							,webModeFunction.getVisibleStatusBar());			// フルスクリーン表示設定

		// WebView更新
		refreshWebview(preference.getString(SystemInfo.KEY_CURRENT_URL, ""));

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

		// フルスクリーンでツールバーがない場合、ダイアログを表示する。
		showFullScreenDialog(webModeFunction.hasMessage());

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

		// Webモード情報を保存
		webModeFunction.saveAsCurrent(editor);

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

		/* スクリーンセーバー停止 */
		stopScreenSaver();

		/* センサーリスナー破棄 */
		sensorManager.unregisterListener(this);

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

		/* 上部URLバー */
		home_LinearLayout1 = (LinearLayout)findViewById(R.id.home_LinearLayout1);

		/* URL入力エディットボックス */
		home_editText1 = (EditText)findViewById(R.id.home_editText1);

		/* フォーカスリスナ */
		home_editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				LogUtil.d(TAG, "[IN ] onFocusChange()");
				LogUtil.d(TAG, "hasFocus:" + hasFocus);

				if (false == hasFocus) {
					// フォーカスが離れた時、WebView更新
					refreshWebview(home_editText1.getText().toString());
				}

				LogUtil.d(TAG, "[OUT] onFocusChange()");
			}
		});

		/* リロードボタン */
		home_imageButton1 = (ImageButton)findViewById(R.id.home_imageButton1);
		home_imageButton1.setOnClickListener(reloadListener);

		/* WEB表示 */
		home_webView1 = (HomeWebView)findViewById(R.id.home_webView1);
		home_webView1.requestFocus();
		home_webView1.getSettings().setBuiltInZoomControls(true);
		home_webView1.getSettings().setSupportZoom(true);
		home_webView1.getSettings().setUseWideViewPort(true);
		home_webView1.getSettings().setLoadWithOverviewMode(true);

		/* JavaScript有効化 */
		home_webView1.getSettings().setJavaScriptEnabled(true);

		/* Web Storage有効化 */
		home_webView1.getSettings().setDomStorageEnabled(true);

		/* 位置情報取得 */
		home_webView1.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
				callback.invoke(origin, true, false);

				LogUtil.d(TAG, "[IN ] onGeolocationPermissionsShowPrompt()");
				LogUtil.d(TAG, "origin:" + origin);
				LogUtil.d(TAG, "[OUT] onGeolocationPermissionsShowPrompt()");
			}

			@Override
			public void onExceededDatabaseQuota(String url,
												String databaseIdentifier,
												long currentQuota,
												long estimatedSize,
												long totalUsedQuota,
												QuotaUpdater quotaUpdater) {
				/* Web SQL Database有効化 */
				quotaUpdater.updateQuota(5 * 1024 * 1024);
			}
		});

		home_webView1.setWebViewClient(new WebViewClient(){
			/* ページ読み込み開始 */
			@Override
			public void onPageStarted(WebView webview, String url, Bitmap favicon) {
				LogUtil.d(TAG, "[IN ] onPageStarted()");
				LogUtil.d(TAG, "url:" + url);

				if (null == webview) {
					LogUtil.d(TAG, "webview  null");
					LogUtil.d(TAG, "[OUT] onPageStarted()");
					return;
				}

				super.onPageStarted(webview, url, favicon);
				LogUtil.d(TAG, "onPageStarted()");
				LogUtil.d(TAG, "url:" + url);

				// ツールバー表示初期化
				initToolBar(webModeFunction.getVisileToolBar());

				LogUtil.d(TAG, "[OUT] onPageStarted()");
			}

			/* ページ読み込み完了 */
			@Override
			public void onPageFinished(WebView webview, String url) {
				LogUtil.d(TAG, "[IN ] onPageFinished()");
				LogUtil.d(TAG, "url:" + url);

				if (null == webview) {
					LogUtil.d(TAG, "webview  null");
					LogUtil.d(TAG, "[OUT] onPageFinished()");
					return;
				}

				super.onPageFinished(webview, url);
				LogUtil.d(TAG, "onPageFinished()");
				LogUtil.d(TAG, "url:" + url);

				// ツールバー表示
				showToolBar(webModeFunction.getVisileToolBar());

				/* URLバーの更新 */
				home_editText1.setText(home_webView1.getUrl());
				editor.putString(SystemInfo.KEY_CURRENT_URL, home_webView1.getUrl());
				editor.commit();
				saveDataBase();

				LogUtil.d(TAG, "[OUT] onPageFinished()");
			}

			/* リダイレクト */
			@Override
			public boolean shouldOverrideUrlLoading(WebView webView, String url){
				LogUtil.d(TAG, "[IN ] shouldOverrideUrlLoading()");
				LogUtil.d(TAG, "url:" + url);

				/* 「about:blank」へのリダイレクト抑止 */
				if (true == url.equals("about:blank")) {
					LogUtil.d(TAG, "return true");
					LogUtil.d(TAG, "[OUT] shouldOverrideUrlLoading()");
					return true;
				}

				if (true == url.startsWith("corundum-api://")) {
					/* コランダムAPI実行 */
					corundumApiExec(url);

					LogUtil.d(TAG, "return true");
					LogUtil.d(TAG, "[OUT] shouldOverrideUrlLoading()");
					return true;
				}

				/* PDF表示 */
				if ((true == url.endsWith(".pdf")) || (true == url.endsWith(".PDF"))) {
					/* インテント準備 */
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse(url), "application/pdf");

					/* インテントチェック */
					if (true == CheckUtil.checkIntent(getApplicationContext(), intent)) {
						/* PDFリーダーアプリ呼び出し */
						LogUtil.d(TAG, "REQUEST_PDF_VIEWER:" + Const.REQUEST_PDF_VIEWER);
						startActivityForResult(intent, Const.REQUEST_PDF_VIEWER);
					} else {
						/* インテントなし */
						Toast.makeText(getApplicationContext(), R.string.err_no_intent_pdf_reader, Toast.LENGTH_LONG).show();
					}

					LogUtil.d(TAG, "return true");
					LogUtil.d(TAG, "[OUT] shouldOverrideUrlLoading()");
					return true;
				}

				/* URLからHOMモード判定 */
				editor.putString(SystemInfo.KEY_CURRENT_URL, url);
				editor.commit();

				LogUtil.d(TAG, "return false");
				LogUtil.d(TAG, "[OUT] shouldOverrideUrlLoading()");
				return false;
			}
		});

		/* Web SQL Database有効化 */
		WebSettings webSettings = home_webView1.getSettings();
		webSettings.setDatabaseEnabled(true);
		String databasePath = getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
		webSettings.setDatabasePath(databasePath);

		/* ツールバー */
		home_LinearLayout2 = (LinearLayout) findViewById(R.id.home_LinearLayout2);

		/* ホームボタン */
		home_imageButton2 = (ImageButton) findViewById(R.id.home_imageButton2);
		home_imageButton2.setOnClickListener(homeListener);

		/* Prevボタン */
		home_imageButton3 = (ImageButton) findViewById(R.id.home_imageButton3);
		home_imageButton3.setOnClickListener(prevListener);

		/* Nextボタン */
		home_imageButton4 = (ImageButton) findViewById(R.id.home_imageButton4);
		home_imageButton4.setOnClickListener(nextListener);

		/* リロードボタン */
		home_imageButton5 = (ImageButton) findViewById(R.id.home_imageButton5);
		home_imageButton5.setOnClickListener(reloadListener);

		/* キャンセルボタン */
		home_imageButton6 = (ImageButton) findViewById(R.id.home_imageButton6);
		home_imageButton6.setOnClickListener(cancelListener);

		/* プログレス */
		home_progressBar1 = (ProgressBar) findViewById(R.id.home_progressBar1);

		/* オプションメニューボタン */
		home_imageButton7 = (ImageButton) findViewById(R.id.home_imageButton7);
		home_imageButton7.setOnClickListener(menuListener);

		LogUtil.d(TAG, "[OUT] initViews()");
	}

	/**
	 * センサー初期化処理
	 * <p>
	 * センサーの初期化を行う。
	 * </p>
	 */
	public void initSensor() {
		LogUtil.d(TAG, "[IN ] initSensor()");

		/* 加速度(シェイク)センサー登録 **/
		if (true == CheckUtil.checkSensorAccelerometer(this)) {
			sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
			List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

			/* 加速度センサーが存在する場合 */
			if (0 < sensors.size()) {
				Sensor sensor = sensors.get(0);
				sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);

				/* 加速度センサー(存在有無) */
				isSensorAccelerometer = true;

				/* 加速度センサー(有効/無効) */
				enableSensorAccelerometer = false;
			}
		}

		LogUtil.d(TAG, "[OUT] initSensor()");
	}

	/**
	 * スクリーンセーバー開始処理
	 * <p>
	 * スクリーンセーバーの起動タイマー開始を行う。
	 * </p>
	 */
	public void startScreenSaver() {
		LogUtil.d(TAG, "[IN ] startScreenSaver()");

		/* スクリーンセーバーが無効の場合 */
		if (false == preference.getBoolean(SystemInfo.KEY_SCREEN_SAVER_FLAG, false)) {
			LogUtil.d(TAG, "[OUT] startScreenSaver()");
			return;
		}

		/* インテント準備 */
		Intent intent = new Intent(getApplicationContext(), ScreenSaverReceivedActivity.class);
		screenSaverPendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0, intent, 0);

		/* スクリーンセーバー起動タイミング設定 */
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, preference.getInt(SystemInfo.KEY_SCREEN_SAVER_TIME, 0));

		/* タイマー設定 */
		screenSaverAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		screenSaverAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), screenSaverPendingIntent);

		LogUtil.d(TAG, "[OUT] startScreenSaver()");
	}

	/**
	 * スクリーンセーバー停止処理
	 * <p>
	 * スクリーンセーバーの起動タイマー停止を行う。
	 * </p>
	 */
	public void stopScreenSaver() {
		LogUtil.d(TAG, "[IN ] stopScreenSaver()");

		/* スクリーンセーバーが無効の場合 */
		if (false == preference.getBoolean(SystemInfo.KEY_SCREEN_SAVER_FLAG, false)) {
			LogUtil.d(TAG, "[OUT] stopScreenSaver()");
			return;
		}

		/* タイマーキャンセル */
		screenSaverAlarmManager.cancel(screenSaverPendingIntent);

		LogUtil.d(TAG, "[OUT] stopScreenSaver()");
	}

	/**
	 * リロードボタンリスナ処理
	 * <p>
	 * リロードボタンが押下された時にコールされる。<br>
	 * Webviewのリロードを行う。
	 * </p>
	 */
	private View.OnClickListener reloadListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] reloadListener::onClick()");

			/* WebView更新 */
			refreshWebview(home_editText1.getText().toString());

			LogUtil.d(TAG, "[OUT] reloadListener::onClick()");
		}
	};

	/**
	 * ホームボタンリスナ処理
	 * <p>
	 * ホームボタンが押下された時にコールされる。<br>
	 * Webviewをホーム表示にする。
	 * </p>
	 */
	private View.OnClickListener homeListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] homeListener::onClick()");

			/* ApplicationListActivity起動 */
			LogUtil.d(TAG, "ApplicationListActivity");
			Intent intent = new Intent(getApplication(), ApplicationListActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] homeListener::onClick()");
		}
	};

	/**
	 * Prevボタンリスナ処理
	 * <p>
	 * Prevボタンが押下された時にコールされる。<br>
	 * Webviewを「戻る」する。<br>
	 * 履歴がない場合は動作しない。
	 * </p>
	 */
	private View.OnClickListener prevListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] prevListener::onClick()");

			/* 遷移可能な場合のみ「戻る」 */
			if (home_webView1.canGoBack()) {
				home_webView1.goBack();
			}

			LogUtil.d(TAG, "[OUT] prevListener::onClick()");
		}
	};

	/**
	 * Nextボタンリスナ処理
	 * <p>
	 * Nextボタンが押下された時にコールされる。<br>
	 * Webviewを「進む」する。<br>
	 * 履歴がない場合は動作しない。
	 * </p>
	 */
	private View.OnClickListener nextListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] nextListener::onClick()");

			/* 遷移可能な場合のみ「進む」 */
			if (home_webView1.canGoForward()) {
				home_webView1.goForward();
			}

			LogUtil.d(TAG, "[OUT] nextListener::onClick()");
		}
	};

	/**
	 * キャンセルボタンリスナ処理
	 * <p>
	 * キャンセルボタンが押下された時にコールされる。<br>
	 * Webviewの読み込みを停止する。
	 * </p>
	 */
	private View.OnClickListener cancelListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] cancelListener::onClick()");

			/* 読み込み停止 */
			home_webView1.stopLoading();
			Toast.makeText(getApplicationContext(), R.string.msg_stop_loading, Toast.LENGTH_LONG).show();

			LogUtil.d(TAG, "[OUT] cancelListener::onClick()");
		}
	};

	/**
	 * メニューボタンリスナ処理
	 * <p>
	 * メニューボタンが押下された時にコールされる。<br>
	 * オプションメニューを表示する。
	 * </p>
	 */
	private View.OnClickListener menuListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] menuListener::onClick()");

			/* ActionSheetActivity起動 */
			LogUtil.d(TAG, "ActionSheetActivity");
			Intent intent = new Intent(getApplication(), ActionSheetActivity.class);
			startActivityForResult(intent, Const.REQUEST_ACTION_SHEET);

			LogUtil.d(TAG, "[OUT] menuListener::onClick()");
		}
	};

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

			/* 遷移可能な場合のみ「戻る」 */
			if (home_webView1.canGoBack()) {
				home_webView1.goBack();

				LogUtil.d(TAG, "return false");
				LogUtil.d(TAG, "[OUT] onKeyDown()");
				return false;
			} else {
				/* ApplicationListActivity起動 */
				LogUtil.d(TAG, "ApplicationListActivity");
				Intent intent = new Intent(getApplication(), ApplicationListActivity.class);
				startActivity(intent);

				/* 終了 */
				finish();
			}
		}

		result = super.onKeyDown(keyCode, event);

		LogUtil.d(TAG, "result:" + result);
		LogUtil.d(TAG, "[OUT] onKeyDown()");
		return result;
	}

	/**
	 * オプションボタン押下イベント処理
	 * <p>
	 * onCreateOptionsMenu()のオーバーライド<br>
	 * オプションボタン押下時にコールされる。
	 * </p>
	 * @param menu メニューオブジェクト
	 * @return 処理結果
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		LogUtil.d(TAG, "[IN ] onCreateOptionsMenu()");

		/* ActionSheetActivity起動 */
		LogUtil.d(TAG, "ActionSheetActivity");
		Intent intent = new Intent(getApplication(), ActionSheetActivity.class);
		startActivityForResult(intent, Const.REQUEST_ACTION_SHEET);

		LogUtil.d(TAG, "return false");
		LogUtil.d(TAG, "[OUT] onCreateOptionsMenu()");
		return false;
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
		LogUtil.d(TAG, "new url:" + url);
		LogUtil.d(TAG, "old url:" + home_webView1.getUrl());

		/* ホワイトリストが有効 */
		if (true == preference.getBoolean(SystemInfo.KEY_WHITE_LIST_FLAG, false)) {
			/* ホワイトリストマッチング処理(遷移先URL) */
			if (false == matchingWhitelist(url)) {
				Toast.makeText(getApplicationContext(), R.string.err_cant_accsess, Toast.LENGTH_LONG).show();

				/* ホワイトリストマッチング処理(遷移元URL) */
				if (null == home_webView1.getUrl()) {
					editor.putString(SystemInfo.KEY_CURRENT_URL, url);
				} else if (false == matchingWhitelist(home_webView1.getUrl())) {
					/* ローカルHTMLへ強制遷移(ホームURLがブロック対象の可能性がある為) */
					editor.putString(SystemInfo.KEY_CURRENT_URL, preference.getString(SystemInfo.KEY_HOME_HTML_PATH, ""));
				} else {
					/* 現在の画面へ遷移 */
					editor.putString(SystemInfo.KEY_CURRENT_URL, home_webView1.getUrl());
				}
			} else {
				editor.putString(SystemInfo.KEY_CURRENT_URL, url);
			}
		} else {
			editor.putString(SystemInfo.KEY_CURRENT_URL, url);
		}

		/* URLからHOMEモード判定 */
//		if (true == preference.getString(SystemInfo.KEY_CURRENT_URL, "").equals(preference.getString(SystemInfo.KEY_HOME_HTML_PATH, ""))) {
//			editor.putInt(SystemInfo.KEY_CURRENT_HOME_MODE, DBAdapter.HOME_MODE_LIST);
//		} else {
//			editor.putInt(SystemInfo.KEY_CURRENT_HOME_MODE, DBAdapter.HOME_MODE_URL);
//		}

		editor.commit();

		/* DB保存 */
		saveDataBase();

		/* 更新 */
		home_webView1.loadUrl(preference.getString(SystemInfo.KEY_CURRENT_URL, ""));

		LogUtil.d(TAG, "[OUT] refreshWebview()");
	}

	/**
	 * 処理結果取得処理
	 * <p>
	 * onActivityResult()のオーバーライド<br>
	 * 別インテントからの処理結果受信を行う。
	 * </p>
	 * @param requestCode リクエストコード
	 * @param resultCode 処理結果コード
	 * @param intent インテント
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		LogUtil.d(TAG, "[IN ] onActivityResult()");
		LogUtil.d(TAG, "requestCode:" + requestCode);
		LogUtil.d(TAG, "resultCode:" + resultCode);

		/* アクションシート画面の処理結果の場合 */
		if (Const.REQUEST_ACTION_SHEET == requestCode) {
			switch (resultCode) {
			/* ツールバー表示/非表示 */
			case Const.ACTION_SHEET_TOOLBAR:
				// ツールバー表示、非表示設定
				webModeFunction.setVisibleToolBar(!webModeFunction.isVisileToolBar());
				// onPause後の処理なので、ここで保存しておく必要がある。
				webModeFunction.saveAsCurrent(editor);

				editor.commit();
				saveDataBase();
				break;

			/* WEBブラウザ起動 */
			case Const.ACTION_SHEET_WEB_BORWSER:
				/* Web URLの場合、標準ブラウザ起動 */
				if (preference.getString(SystemInfo.KEY_CURRENT_URL, "").startsWith("http://") || preference.getString(SystemInfo.KEY_CURRENT_URL, "").startsWith("https://")) {
					Toast.makeText(getApplicationContext(), R.string.msg_exec_web_browser, Toast.LENGTH_LONG).show();

					/* Webブラウザ起動 */
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse(preference.getString(SystemInfo.KEY_CURRENT_URL, "")));
					LogUtil.d(TAG, "REQUEST_WEB_BROWSER:" + Const.REQUEST_WEB_BROWSER);
					startActivityForResult(intent, Const.REQUEST_WEB_BROWSER);
				} else {
					Toast.makeText(getApplicationContext(), R.string.err_exec_web_browser, Toast.LENGTH_LONG).show();
				}
				break;

			/* 環境設定メニューへ */
			case Const.ACTION_SHEET_MENU:
				/* パスコード使用が有効 かつ 管理者用モード以外の場合、パスコード認証を行う */
				if (true == preference.getBoolean(SystemInfo.KEY_PASS_CODE_FLAG, false) && (DBAdapter.WEB_MODE_ADMIN != webModeFunction.getWebMode())) {
					/* 「パスコードを入力」ダイアログ作成 */
					AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(HomeActivity.this);
					alertdialogBuilder.setTitle(R.string.passcode_input_passcode);

					/* パスコードエディットボックスの設定 */
					final EditText pass_code = new EditText(HomeActivity.this);
					pass_code.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					pass_code.setHint(R.string.passcode_hint);
					pass_code.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(Const.CONFIG_PASSCODE_MAX_LENGTH) });
					alertdialogBuilder.setView(pass_code);

					/* 実行ボタン押下時 */
					alertdialogBuilder.setPositiveButton(R.string.passcode_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								LogUtil.d(TAG, "[IN ] setPositiveButton::onClick()");

								/* パスコード取得 */
								String save_pass_code = preference.getString(SystemInfo.KEY_PASS_CODE, "");

								/* パスコードマッチング */
								if (pass_code.getText().toString().equals(save_pass_code)) {
									/* MenuActivity起動 */
									LogUtil.d(TAG, "MenuActivity");
									Intent intent = new Intent(getApplication(), MenuActivity.class);
									startActivity(intent);
									finish();
								} else {
									/* パスコード誤り */
									Toast.makeText(HomeActivity.this, R.string.err_passcode, Toast.LENGTH_LONG).show();
								}

								LogUtil.d(TAG, "[OUT] setPositiveButton::onClick()");
							}
						});

					/* キャンセルボタン押下時 */
					alertdialogBuilder.setNegativeButton(R.string.passcode_cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								LogUtil.d(TAG, "[IN ] setNegativeButton::onClick()");
								Toast.makeText(getApplicationContext(), R.string.msg_cancel, Toast.LENGTH_LONG).show();
								LogUtil.d(TAG, "[OUT] setNegativeButton::onClick()");
							}
						});

					/* ダイアログ表示 */
					alertdialogBuilder.setCancelable(true);
					AlertDialog alertdialog = alertdialogBuilder.create();
					alertdialog.show();
				} else {
					/* MenuActivity起動 */
					LogUtil.d(TAG, "MenuActivity");
					intent = new Intent(this, MenuActivity.class);
					startActivity(intent);
					finish();
				}
				break;

			/* このアプリを強制終了 */
			case Const.ACTION_SHEET_EXIT:
				Toast.makeText(getApplicationContext(), R.string.msg_good_by, Toast.LENGTH_LONG).show();

				/* 終了 */
				finish();
				break;

			/* キャンセル */
			case Const.ACTION_SHEET_CANCEL:
			default:
				Toast.makeText(getApplicationContext(), R.string.msg_cancel, Toast.LENGTH_LONG).show();
				break;
			}

		} else if (Const.REQUEST_WEB_BROWSER != requestCode) {
			/* Webブラウザの処理結果以外の場合 */
			/* コランダムAPI結果処理 */
			corundumApiResult(requestCode, resultCode, intent);
		}

		LogUtil.d(TAG, "[OUT] onActivityResult()");
	}

	/**
	 * ホワイトリストマッチング処理
	 * <p>
	 * ホワイトリストのマッチングを行う。<br>
	 * ※非推奨メソッド「startManagingCursor()」「stopManagingCursor()」を使用。<br>
	 * 　Android 2.X.Xをサポートしている為、回避手段がない。
	 * </p>
	 * @param url URL
	 * @return 処理結果
	 */
	@SuppressWarnings("deprecation")
	public boolean matchingWhitelist(String url) {
		LogUtil.d(TAG, "[IN ] matchingWhitelist()");
		LogUtil.d(TAG, "url:" + url);

		/* ローカルHTMLの場合は判定対象外 */
		if (true == url.equals(preference.getString(SystemInfo.KEY_HOME_HTML_PATH, ""))) {
			LogUtil.d(TAG, "return true");
			LogUtil.d(TAG, "[OUT] matchingWhitelist()");
			return true;
		}

		/* ホワイトリスト取得 */
		dbAdapter.open();
		Cursor cursor = dbAdapter.getAllWhiteList();

		startManagingCursor(cursor);

		if (true == cursor.moveToFirst()) {
			do {
				if (DBAdapter.WHITE_LIST_MODE_FULL_URL == cursor.getInt(cursor.getColumnIndex(WhiteListInfo.COLUMN_WHITE_LIST_MODE))) {
					/* URL完全一致(完全一致比較) */
					if (true == url.equals(cursor.getString(cursor.getColumnIndex(WhiteListInfo.COLUMN_WHITE_LIST_URL)))) {
						LogUtil.d(TAG, "return true");
						LogUtil.d(TAG, "[OUT] matchingWhitelist()");
						return true;
					}
				} else {
					/* URL以下全て(前方一致比較) */
					if (true == url.startsWith(cursor.getString(cursor.getColumnIndex(WhiteListInfo.COLUMN_WHITE_LIST_URL)))) {
						LogUtil.d(TAG, "return true");
						LogUtil.d(TAG, "[OUT] matchingWhitelist()");
						return true;
					}
				}
			} while(cursor.moveToNext());
		}

		stopManagingCursor(cursor);
		dbAdapter.close();

		LogUtil.d(TAG, "return false");
		LogUtil.d(TAG, "[OUT] matchingWhitelist()");
		return false;
	}

	/**
	 * コランダムAPI実行処理
	 * <p>
	 * コランダムAPIの実行を行う。
	 * </p>
	 * @param url URL
	 */
	public void corundumApiExec(String url) {
		LogUtil.d(TAG, "[IN ] corundumApiExec()");
		LogUtil.d(TAG, "url:" + url);

		String utf8Str = "";

		/* URL -> Stringデコード */
		try {
			utf8Str = URLDecoder.decode(url,"utf-8");
		} catch (Exception e) {
			LogUtil.d(TAG, e.getMessage());
		}

		int top = utf8Str.indexOf('(');
		int end = utf8Str.indexOf(')');

		/* コランダムAPI先頭部分が含まれていない */
		if ((0 > top) || (0 > end)) {
			Toast.makeText(getApplicationContext(), R.string.err_api_format, Toast.LENGTH_LONG).show();
			LogUtil.d(TAG, "[OUT] corundumApiExec()");
			return;
		}

		try {
			/* JSONオブジェクト切り取り */
			String json = utf8Str.substring(top + 1, end);
			JSONObject jsonObj = new JSONObject(json);

			if (true == utf8Str.startsWith("corundum-api://imagePicker")) {
				/* コランダムAPI(イメージピッカー) */
				corundumApiImagePicker(jsonObj);
			} else if (true == utf8Str.startsWith("corundum-api://motionEvent")) {
				/* コランダムAPI(モーションイベント) */
				corundumApiMotionEvent(jsonObj);
			} else if (true == utf8Str.startsWith("corundum-api://urlScheme")) {
				/* コランダムAPI(URLスキーマ実行) */
				corundumApiUrlScheme(jsonObj);
			} else if (true == utf8Str.startsWith("corundum-api://localNotice")) {
				/* コランダムAPI(ローカル通知) */
				corundumApiLocalNotice(jsonObj);
			} else if (true == utf8Str.startsWith("corundum-api://autoSleep")) {
				/* コランダムAPI(オートスリープ) */
				corundumApiAutoSleep(jsonObj);
			} else if (true == utf8Str.startsWith("corundum-api://addressBook")) {
				/* コランダムAPI(アドレスブック) */
				corundumApiAddressBook(jsonObj);
			} else if (true == utf8Str.startsWith("corundum-api://operationMode")) {
				/* コランダムAPI(オペレーションモード) */
				corundumApiOperationMode(jsonObj);
			} else if (true == utf8Str.startsWith("corundum-api://fullScreenMode")) {
				/* コランダムAPI(フルスクリーンモード) */
				corundumApiFullScreenMode(jsonObj);
			} else {
				Toast.makeText(getApplicationContext(), R.string.err_api_format, Toast.LENGTH_LONG).show();
				return;
			}
		} catch (Exception e) {
			LogUtil.d(TAG, e.getMessage());
		}

		LogUtil.d(TAG, "[OUT] corundumApiExec()");
	}

	/**
	 * コランダムAPI(イメージピッカー)処理
	 * <p>
	 * カメラ or ギャラリーの起動を行い、指定サイズでの画像リサイズを行う。
	 * </p>
	 * @param jsonObj JSONオブジェクト
	 */
	public void corundumApiImagePicker(JSONObject jsonObj) {
		LogUtil.d(TAG, "[IN ] corundumApiImagePicker()");

		String value = null;
		String allowsEditing = null;
		String saveAlbum = null;

		try {
			/* パラメータ取得 */
			if (true == jsonObj.has("value")) {
				value = jsonObj.getString("value");
			} else {
				value = "camera";
			}

			if (true == jsonObj.has("height")) {
				imageHight = jsonObj.getInt("height");
			} else {
				imageHight = 200;
			}

			if (true == jsonObj.has("width")) {
				imageWidth = jsonObj.getInt("width");
			}else {
				imageWidth = 300;
			}

			if (true == jsonObj.has("allowsEditing")) {
				allowsEditing = jsonObj.getString("allowsEditing");
			} else {
				allowsEditing = "NO";
			}

			if (true == jsonObj.has("saveAlbum")) {
				saveAlbum = jsonObj.getString("saveAlbum");
			} else {
				saveAlbum = "NO";
			}

			LogUtil.d(TAG, "value:" + value);
			LogUtil.d(TAG, "width:" + imageWidth);
			LogUtil.d(TAG, "height:" + imageHight);
			LogUtil.d(TAG, "allowsEditing:" + allowsEditing);
			LogUtil.d(TAG, "saveAlbum:" + saveAlbum);
		} catch (Exception e) {
			LogUtil.d(TAG, e.getMessage());
			Toast.makeText(getApplicationContext(), R.string.err_api_format, Toast.LENGTH_LONG).show();
		}

		/* リサイズ有無フラグ設定 */
		if (null != allowsEditing) {
			if (true == allowsEditing.equals("YES")) {
				isTrimming = true;
			} else {
				isTrimming = false;
			}
		} else {
			isTrimming = false;
		}

		/* 保存有無フラグ設定 */
		if (null != saveAlbum) {
			if (true == saveAlbum.equals("originalImage")) {
				isSaveOrgPicFile = true;
				isSaveResPicFile = false;
			} else if (true == saveAlbum.equals("editedImage")) {
				isSaveOrgPicFile = false;
				isSaveResPicFile = true;
			} else if (true == saveAlbum.equals("bothImage")) {
				isSaveOrgPicFile = true;
				isSaveResPicFile = true;
			} else {
				isSaveOrgPicFile = false;
				isSaveResPicFile = false;
			}
		} else {
			isSaveOrgPicFile = false;
			isSaveResPicFile = false;
		}

		if (null != value) {
			if (true == value.equals("camera")) {
				/* ハードウェアチェック(カメラ) */
				if (true == CheckUtil.checkHardwareCamera(this)) {
					/* インテント準備 */
					Intent intent = new Intent();
					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.addCategory(Intent.CATEGORY_DEFAULT);

					/* SDカード上に元写真データファイル作成 */
					originalPicFile = new File(FileUtil.getBaseDirPath(getApplicationContext()) + "/www", ImageUtil.getOrgPicFileName());
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(originalPicFile));

					/* インテントチェック */
					if (true == CheckUtil.checkIntent(this, intent)) {
						/* カメラアプリ呼び出し */
						LogUtil.d(TAG, "REQUEST_CAMERA:" + Const.REQUEST_CAMERA);
						startActivityForResult(intent, Const.REQUEST_CAMERA);
					} else {
						/* インテントなし */
						Toast.makeText(getApplicationContext(), R.string.err_no_intent_camera, Toast.LENGTH_LONG).show();
					}
				} else {
					/* ハードウェアなし(カメラ) */
					Toast.makeText(getApplicationContext(), R.string.err_no_hw_camera, Toast.LENGTH_LONG).show();
				}
			} else if ((true == value.equals("photoLibrary")) || (true == value.equals("photoAlbum"))) {
				/* インテント準備 */
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);

				/* インテントチェック */
				if (true == CheckUtil.checkIntent(this, intent)) {
					/* ギャラリーアプリ呼び出し */
					LogUtil.d(TAG, "REQUEST_GALLERY:" + Const.REQUEST_GALLERY);
					startActivityForResult(intent, Const.REQUEST_GALLERY);
				} else {
					/* インテントなし(ギャラリー) */
					Toast.makeText(getApplicationContext(), R.string.err_no_intent_garally, Toast.LENGTH_LONG).show();
				}
			}
		}

		LogUtil.d(TAG, "[OUT] corundumApiImagePicker()");
	}

	/**
	 * コランダムAPI(モーションイベント)処理
	 * <p>
	 * 加速度センサーフラグを有効化する。
	 * </p>
	 * @param jsonObj JSONオブジェクト
	 */
	public void corundumApiMotionEvent(JSONObject jsonObj) {
		LogUtil.d(TAG, "[IN ] corundumApiMotionEvent()");

		/* 加速度センサーがない場合 */
		if (false == isSensorAccelerometer) {
			Toast.makeText(getApplicationContext(), R.string.err_no_hw_accelerometer, Toast.LENGTH_LONG).show();
			LogUtil.d(TAG, "[OUT] corundumApiMotionEvent()");
			return;
		}

		try {
			String value = jsonObj.getString("value");

			LogUtil.d(TAG, "value:" + value);

			if (true == value.equals("shake")) {
				/* 加速度センサー(シェイク検出)有効 */
				enableSensorAccelerometer = true;
			} else if (true == value.equals("unShake")) {
				/* 加速度センサー(シェイク検出)無効 */
				enableSensorAccelerometer = false;
			} else {
				Toast.makeText(getApplicationContext(), R.string.err_api_format, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			LogUtil.d(TAG, e.getMessage());
			Toast.makeText(getApplicationContext(), R.string.err_api_format, Toast.LENGTH_LONG).show();
		}

		LogUtil.d(TAG, "[OUT] corundumApiMotionEvent()");
	}

	/**
	 * コランダムAPI(URLスキーマ実行)処理
	 * <p>
	 * URLスキーマの実行を行う。
	 * </p>
	 * @param jsonObj JSONオブジェクト
	 */
	public void corundumApiUrlScheme(JSONObject jsonObj) {
		LogUtil.d(TAG, "[IN ] corundumApiUrlScheme()");

		try {
			urlSchemeText = jsonObj.getString("value");

			LogUtil.d(TAG, "value:" + urlSchemeText);

			/* インテント準備 */
			Uri uri = Uri.parse(urlSchemeText);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);

			/* インテントチェック */
			if (true == CheckUtil.checkIntent(getApplicationContext(), intent)) {
				startActivityForResult(intent, Const.REQUEST_URL_SHCEME);
			} else {
				/* インテントなし */
				Toast.makeText(getApplicationContext(), R.string.err_no_intent, Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			LogUtil.d(TAG, e.getMessage());
			Toast.makeText(getApplicationContext(), R.string.err_api_format, Toast.LENGTH_LONG).show();
		}

		LogUtil.d(TAG, "[OUT] corundumApiUrlScheme()");
	}

	/**
	 * コランダムAPI(ローカル通知)処理
	 * <p>
	 * 指定時間経過後、ローカル通知を行う。
	 * </p>
	 * @param jsonObj JSONオブジェクト
	 */
	public void corundumApiLocalNotice(JSONObject jsonObj) {
		LogUtil.d(TAG, "[IN ] corundumApiLocalNotice()");

		try {
			final String value = jsonObj.getString("value");
			final int time = jsonObj.getInt("time");

			LogUtil.d(TAG, "value:" + value);
			LogUtil.d(TAG, "time:" + time);

			/* 通知スレッド起動 */
			(new Thread(new Runnable() {
				@Override
				public void run() {

					try {
						/* 指定秒数スリープ */
						Thread.sleep((long)time * 1000);

						/* ローカル通知 */
						NotificationUtil.sendNotification(getApplicationContext(), getResources(), getString(R.string.msg_ticker), getString(R.string.app_name), value);
					} catch(Exception e) {
						LogUtil.d(TAG, e.getMessage());
					}
				}
			})).start();
		} catch (Exception e) {
			LogUtil.d(TAG, e.getMessage());
			Toast.makeText(getApplicationContext(), R.string.err_api_format, Toast.LENGTH_LONG).show();
		}

		/* JavaScriptコールバック */
		home_webView1.loadUrl("javascript:corundum.localNotice();");

		LogUtil.d(TAG, "[OUT] corundumApiLocalNotice()");
	}

	/**
	 * コランダムAPI(オートスリープ)処理
	 * <p>
	 * オートスリープ有効/無効の設定を行う。
	 * </p>
	 * @param jsonObj JSONオブジェクト
	 */
	public void corundumApiAutoSleep(JSONObject jsonObj) {
		LogUtil.d(TAG, "[IN ] corundumApiAutoSleep()");

		String value = "";

		try {
			value = jsonObj.getString("value");

			LogUtil.d(TAG, "value:" + value);

			if (true == value.equals("sleep")) {
				/* Keep screen off */
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			} else {
				/* Keep screen on */
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			}
		} catch (Exception e) {
			LogUtil.d(TAG, e.getMessage());
			Toast.makeText(getApplicationContext(), R.string.err_api_format, Toast.LENGTH_LONG).show();
		}

		/* JavaScriptコールバック */
		home_webView1.loadUrl("javascript:corundum.autoSleep(\" " + value + " \");");

		LogUtil.d(TAG, "[OUT] corundumApiAutoSleep()");
	}

	/**
	 * コランダムAPI(アドレスブック)処理
	 * <p>
	 * アドレスブック取得を行う。<br>
	 * ※非推奨メソッド「managedQuery()」を使用。<br>
	 * 　Android 2.X.Xをサポートしている為、回避手段がない。
	 * </p>
	 * @param jsonObj JSONオブジェクト
	 */
	public void corundumApiAddressBook(JSONObject jsonObj) {
		LogUtil.d(TAG, "[IN ] corundumApiAddressBook()");

		/* 電話帳情報の送信を許可しますか？ */
		new AlertDialog.Builder(this)
		.setIcon(R.drawable.ic_launcher)
		.setTitle(getString(R.string.addressbook_get_ok))
		/* 許可ボタン押下時 */
		.setPositiveButton(getString(R.string.addressbook_yes),
			new DialogInterface.OnClickListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					LogUtil.d(TAG, "[IN ] onClick()");

					String name = "";
					String phoneNumber = "";
					String enc64 = "";
					JSONArray jsonArray = new JSONArray();
					JSONObject jsonObject;
					try {
						Cursor cursor = managedQuery(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

						while (cursor.moveToNext()) {
							/* コンタクトID取得 */
							String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
							LogUtil.d(TAG, "contactId:" + contactId);

							/* 名前取得 */
							name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
							LogUtil.d(TAG, "name:" + name);

							/* 電話番号の有無取得 */
							String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

							/* 電話番号が登録されている場合 */
							if ("1".equals(hasPhone)) {
								Cursor phones = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);

								while (phones.moveToNext()) {
									/* 電話番号取得 */
									phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
									LogUtil.d(TAG, "phoneNumber:" + phoneNumber);
								}

								phones.close();
							}

							/* 電話番号の数字以外を除去 */
							phoneNumber = phoneNumber.replaceAll("[^0-9]","");

							jsonObject = new JSONObject();
							try {
								jsonObject.put("name", name);
								jsonObject.put("mobile", phoneNumber);
								jsonArray.put(jsonObject);

							} catch (JSONException e) {
								// 処理継続
								LogUtil.w(TAG, getResources().getString(R.string.err_exception_continue) + e.getMessage());
							}
						}
						cursor.close();

						/* URLエンコーディング */
						try {
							enc64 = URLEncoder.encode(jsonArray.toString(), Const.CHARSET_UTF8);

						} catch (UnsupportedEncodingException e) {
							throw new ArcAppException(R.string.err_exception, e);
						}

						/* JavaScriptコールバック */
						home_webView1.loadUrl("javascript:corundum.addressBook(\" " + enc64 + " \");");

					} catch (ArcBaseException e) {
						LogUtil.e(TAG, getResources().getString(e.getResourceId()));
						LogUtil.e(TAG, e.getMessage());

						// OS側でプロセスを終了する(「問題が発生したため、corundumを終了します。」)
						throw new ArcRuntimeException(e.getResourceId(), e);

					} finally {
						LogUtil.d(TAG, "[OUT] onClick()");
					}
				}
			})
		/* 許可しないボタン押下時 */
		.setNegativeButton(getString(R.string.addressbook_no),
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					LogUtil.d(TAG, "[IN ] onClick()");

					/* JavaScriptコールバック */
					home_webView1.loadUrl("javascript:corundum.addressBook(\"\");");

					LogUtil.d(TAG, "[OUT] onClick()");
				}
			}
		).show();

		LogUtil.d(TAG, "[OUT] corundumApiAddressBook()");
	}

	/**
	 * コランダムAPI(オペレーションモード)処理
	 * <p>
	 * 表示モードの変更を行う。
	 * 設定値は変更しない。
	 * </p>
	 * @param jsonObj JSONオブジェクト
	 */
	public void corundumApiOperationMode(JSONObject jsonObj) {
		LogUtil.d(TAG, "[IN ] corundumApiOperationMode()");

		String value = "";

		try {
			value = jsonObj.getString("value");

			LogUtil.d(TAG, "value:" + value);

			if (true == value.equals("true")) {
				// オペレーションモードに設定
				webModeFunction.setWebMode(DBAdapter.WEB_MODE_OPERATION);

			} else if (true == value.equals("false")) {
				// フルスクリーンモードに設定
				webModeFunction.setWebMode(DBAdapter.WEB_MODE_FULL);

			} else {
				/* 何もできない。 */
				Toast.makeText(getApplicationContext(), R.string.err_api_format, Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			LogUtil.d(TAG, e.getMessage());
			Toast.makeText(getApplicationContext(), R.string.err_api_format, Toast.LENGTH_LONG).show();
		}

		// ツールバー表示設定
		home_LinearLayout2.setVisibility(webModeFunction.getVisileToolBar());

		/* JavaScriptコールバック */
		home_webView1.loadUrl("javascript:corundum.operationMode(\" " + value + " \");");

		LogUtil.d(TAG, "[OUT] corundumApiOperationMode()");
	}

	/**
	 * コランダムAPI(フルスクリーンモード)処理
	 * <p>
	 * 表示モードの変更を行う。
	 * 設定値は変更しない。
	 * </p>
	 * @param jsonObj JSONオブジェクト
	 */
	public void corundumApiFullScreenMode(JSONObject jsonObj) {
		LogUtil.d(TAG, "[IN ] corundumApiFullScreenMode()");

		String value = "";

		try {
			value = jsonObj.getString("value");

			LogUtil.d(TAG, "value:" + value);

			if (true == value.equals("true")) {
				// フルスクリーンモードに設定
				webModeFunction.setWebMode(DBAdapter.WEB_MODE_FULL);

			} else if (true == value.equals("false")) {
				// オペレーションモードに設定
				webModeFunction.setWebMode(DBAdapter.WEB_MODE_OPERATION);

			} else {
				/* 何もできない。 */
				Toast.makeText(getApplicationContext(), R.string.err_api_format, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			LogUtil.d(TAG, e.getMessage());
			Toast.makeText(getApplicationContext(), R.string.err_api_format, Toast.LENGTH_LONG).show();
		}

		// ツールバー表示設定
		home_LinearLayout2.setVisibility(webModeFunction.getVisileToolBar());

		/* JavaScriptコールバック */
		home_webView1.loadUrl("javascript:corundum.fullScreenMode(\" " + value + " \");");

		LogUtil.d(TAG, "[OUT] corundumApiFullScreenMode()");
	}

	/**
	 * コランダムAPI結果処理
	 * <p>
	 * 別アプリからの処理結果を受け取り、コランダムAPIの処理結果を返す。
	 * </p>
	 * @param requestCode リクエストコード
	 * @param resultCode 処理結果コード
	 * @param intent インテント
	 */
	public void corundumApiResult(int requestCode, int resultCode, Intent intent) {
		LogUtil.d(TAG, "[IN ] corundumApiResult()");
		LogUtil.d(TAG, "requestCode:" + requestCode);
		LogUtil.d(TAG, "resultCode:" + resultCode);

		Bitmap img = null;
		Bitmap bmp = null;

		/* カメラからの応答時 */
		if ((Const.REQUEST_CAMERA == requestCode) && (RESULT_OK == resultCode)) {
			/* ファイル保存されていない場合の対処 */
			if (true != originalPicFile.exists()) {
				if ((null != intent) && (null != intent.getExtras())) {
					/* Bitmap取得 */
					Bundle bundle = intent.getExtras();
					img = (Bitmap) bundle.getParcelable("data");

					/* Bitmap保存 */
					ImageUtil.copyBitmap(img, new File(FileUtil.getBaseDirPath(getApplicationContext()) + "/www"), originalPicFile.getName());
				} else {
					LogUtil.e(TAG, "No Image File!!");
					LogUtil.d(TAG, "[OUT] corundumApiResult()");
					return;
				}
			} else {
				/* Bitmap取得 */
				img = ImageUtil.readBitmap(originalPicFile.getPath());
			}

			/* トリミング */
			if (true == isTrimming) {
				/* インテント準備 */
				intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(Uri.parse("file://" + originalPicFile.getPath()), "image/*");
				intent.putExtra("outputX", imageWidth);
				intent.putExtra("outputY", imageHight);
				intent.putExtra("scale", true);
				intent.putExtra("return-data", true);
				intent.putExtra("aspectX", imageWidth);
				intent.putExtra("aspectY", imageHight);

				/* SDカード上に写真データファイル作成 */
				resizePicFile = new File(FileUtil.getBaseDirPath(getApplicationContext()) + "/www", ImageUtil.getResPicFileName());
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(resizePicFile));

				/* インテントチェック */
				if (true == CheckUtil.checkIntent(getApplicationContext(), intent)) {
					/* ギャラリーアプリ呼び出し */
					LogUtil.d(TAG, "REQUEST_GALLERY_TRIMING:" + Const.REQUEST_GALLERY_TRIMING);
					startActivityForResult(intent, Const.REQUEST_GALLERY_TRIMING);
				} else {
					/* インテントなし */
					Toast.makeText(getApplicationContext(), R.string.err_no_intent_garally, Toast.LENGTH_LONG).show();
				}

			/* リサイズ */
			} else {
				/* リサイズ */
				bmp = ImageUtil.resizeBitmap(img, imageHight, imageWidth);

				/* 画像 -> ByteArray -> Base64変換 */
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				bmp.compress(CompressFormat.JPEG, 100, outStream);
				byte[] _bArray = outStream.toByteArray();
				String image64 = "data:image/jpg;base64," + android.util.Base64.encodeToString(_bArray, Base64.DEFAULT);

				/* URLエンコーディング */
				String encImage64 = "";
				try {
					encImage64 = URLEncoder.encode(image64, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					LogUtil.d(TAG, e.getMessage());
				}

				/* 画像ファイル保存有無フラグ(非圧縮) */
				if (true == isSaveOrgPicFile) {
					/* ギャラリーへ画像反映 */
					MediaScannerConnection.scanFile(this, new String[] { originalPicFile.getPath() }, new String[] { "image/jpeg" }, null);
				} else {
					if (true == originalPicFile.exists()) {
						try {
							/* ファイル削除 */
							boolean ret = originalPicFile.delete();
							LogUtil.d(TAG, "ret:" + ret);
						} catch (Exception e) {
							e.printStackTrace();
							LogUtil.d(TAG, e.getMessage());
						}
					}
				}

				/* 画像ファイル保存有無フラグ(リサイズ後) */
				if (true == isSaveResPicFile) {
					resizePicFile = new File(FileUtil.getBaseDirPath(getApplicationContext()) + "/www", ImageUtil.getResPicFileName());
					ImageUtil.copyBitmap(bmp, new File(FileUtil.getBaseDirPath(getApplicationContext()) + "/www"), resizePicFile.getName());

					/* ギャラリーへ画像反映 */
					MediaScannerConnection.scanFile(this, new String[] { resizePicFile.getPath() }, new String[] { "image/jpeg" }, null);
				} else {
					/* 何もしない */
				}

				home_webView1.loadUrl("javascript:corundum.imagePicker('" + encImage64 + "');");
			}

		/* ギャラリーからの応答時 */
		} else if ((Const.REQUEST_GALLERY == requestCode) && (RESULT_OK == resultCode)) {

			/* ギャラリーで選択されたURI取得 */
			Uri uri = intent.getData();

			/* Uri -> File Path変換 */
			String filePath = StringUtil.convertUriToFilePath(getApplicationContext(), uri);

			/* Bitmap取得 */
			img = ImageUtil.readBitmap(filePath);

			/* トリミング */
			if (true == isTrimming) {
				/* インテント準備 */
				intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(uri, "image/*");
				intent.putExtra("outputX", imageWidth);
				intent.putExtra("outputY", imageHight);
				intent.putExtra("scale", true);
				intent.putExtra("return-data", true);
				intent.putExtra("aspectX", imageWidth);
				intent.putExtra("aspectY", imageHight);

				/* SDカード上に写真データファイル作成 */
				resizePicFile = new File(FileUtil.getBaseDirPath(getApplicationContext()) + "/www", ImageUtil.getResPicFileName());
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(resizePicFile));

				/* インテントチェック */
				if (true == CheckUtil.checkIntent(getApplicationContext(), intent)) {
					/* ギャラリーアプリ呼び出し */
					LogUtil.d(TAG, "REQUEST_GALLERY_TRIMING:" + Const.REQUEST_GALLERY_TRIMING);
					startActivityForResult(intent, Const.REQUEST_GALLERY_TRIMING);
				} else {
					/* インテントなし */
					Toast.makeText(getApplicationContext(), R.string.err_no_intent_garally, Toast.LENGTH_LONG).show();
				}

			/* リサイズ */
			} else {
				/* リサイズ */
				bmp = ImageUtil.resizeBitmap(img, imageHight, imageWidth);

				/* 画像 -> ByteArray -> Base64変換 */
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				bmp.compress(CompressFormat.JPEG, 100, outStream);
				byte[] _bArray = outStream.toByteArray();
				String image64 = "data:image/jpg;base64," + android.util.Base64.encodeToString(_bArray, Base64.DEFAULT);

				String encImage64 = "";

				try {
					/* URLエンコーディング */
					encImage64 = URLEncoder.encode(image64, "UTF-8");
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.d(TAG, e.getMessage());
				}

				home_webView1.loadUrl("javascript:corundum.imagePicker('" + encImage64 + "');");
			}

		/* ギャラリー(トリミング)からの応答時 */
		} else if ((Const.REQUEST_GALLERY_TRIMING == requestCode) && (RESULT_OK == resultCode)) {
			/* ファイルが保存されていない場合の対処 */
			if (true != resizePicFile.exists()) {
				if ((null != intent) && (null != intent.getExtras())) {
					/* Bitmap取得 */
					Bundle bundle = intent.getExtras();
					bmp = (Bitmap) bundle.getParcelable("data");

					/* Bitmap保存 */
					ImageUtil.copyBitmap(bmp, new File(FileUtil.getBaseDirPath(getApplicationContext()) + "/www"), resizePicFile.getName());

				} else {
					LogUtil.e(TAG, "No Image File!!");
					LogUtil.d(TAG, "[OUT] corundumApiResult()");
					return;
				}
			} else {
				/* Bitmap取得 */
				bmp = ImageUtil.readBitmap(resizePicFile.getPath());
			}

			/* 画像 -> ByteArray -> Base64変換 */
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			bmp.compress(CompressFormat.JPEG, 100, outStream);
			byte[] _bArray = outStream.toByteArray();
			String image64 = "data:image/jpg;base64," + android.util.Base64.encodeToString(_bArray, Base64.DEFAULT);

			String encImage64 = "";

			try {
				/* URLエンコーディング */
				encImage64 = URLEncoder.encode(image64, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.d(TAG, e.getMessage());
			}

			/* 画像ファイル保存有無フラグ(非圧縮) */
			if (true == isSaveOrgPicFile) {
				/* 何もしない */
			} else {
				if (true == originalPicFile.exists()) {
					try {
						/* ファイル削除 */
						boolean ret = originalPicFile.delete();
						LogUtil.d(TAG, "ret:" + ret);
					} catch (Exception e) {
						e.printStackTrace();
						LogUtil.d(TAG, e.getMessage());
					}
				}
			}

			/* 画像ファイル保存有無フラグ(リサイズ後) */
			if (true == isSaveResPicFile) {
				/* ギャラリーへ画像反映 */
				MediaScannerConnection.scanFile(this, new String[] { resizePicFile.getPath() }, new String[] { "image/jpeg" }, null);
			} else {
				if (true == resizePicFile.canExecute()) {
					try {
						/* ファイル削除 */
						boolean ret = resizePicFile.delete();
						LogUtil.d(TAG, "ret:" + ret);
					} catch (Exception e) {
						e.printStackTrace();
						LogUtil.d(TAG, e.getMessage());
					}
				}
			}

			home_webView1.loadUrl("javascript:corundum.imagePicker('" + encImage64 + "');");

		/* URLスキーム(アプリ不明)からの応答 */
		} else if ((Const.REQUEST_URL_SHCEME == requestCode) && (RESULT_OK == resultCode)) {
			/* JavaScriptコールバック */
			home_webView1.loadUrl("javascript:corundum.autoSleep(\" " + urlSchemeText + " \");");
		}

		LogUtil.d(TAG, "[OUT] corundumApiResult()");
	}

	/**
	 * センサー精度変更イベント処理
	 * <p>
	 * onAccuracyChanged()のオーバーライド<br>
	 * センサー精度が変更された時にコールされる。
	 * </p>
	 * @param sensor センサーオブジェクト
	 * @param accuracy 精度
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		LogUtil.d(TAG, "[IN ] onAccuracyChanged()");
		LogUtil.d(TAG, "accuracy:" + accuracy);
		LogUtil.d(TAG, "[OUT] onAccuracyChanged()");
	}

	/**
	 * センサー変更イベント処理
	 * <p>
	 * onSensorChanged()のオーバーライド<br>
	 * センサー状態変更時にコールされる。<br>
	 * ※傾きセンサーの変更イベントが多発する為、ログ出力は行わない方針。
	 * </p>
	 * @param sensorEvent センサーイベントオブジェクト
	 */
	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {

		switch (sensorEvent.sensor.getType()) {
		/* 傾きセンサー */
		case Sensor.TYPE_ACCELEROMETER:
			long nNowTime   = System.currentTimeMillis();
			long nDiffTime  = nNowTime - shakeBeforeTime;

			if (300 < nDiffTime) {
				float x = sensorEvent.values[0];
				float y = sensorEvent.values[1];
				float z = sensorEvent.values[2];

				/* 前回の値との差からスピードを計算 */
				float speed = Math.abs(x + y + z - shakeBeforeX - shakeBeforeY - shakeBeforeZ) / nDiffTime * 10000;

				/* スピードが規定値以上 */
				if (80 < speed) {
					/* シェイクカウントを足す */
					shakeCount++;

					/* 2回連続スピードが規定値以上なら */
					if (shakeCount > 2) {
						if (true == enableSensorAccelerometer) {
							/* JavaScriptコールバック */
							home_webView1.loadUrl("javascript:corundum.motionShaked(\"speed:" + String.valueOf((int)speed) + "\");");
							shakeCount = 0;
						}
					}
				} else {
					/* 規定値以下ならリセット */
					shakeCount = 0;
				}

				/* 前回値として保存 */
				shakeBeforeTime = nNowTime;
				shakeBeforeX = x;
				shakeBeforeY = y;
				shakeBeforeZ = z;
			}
			break;

		default:
			break;
		}
	}

	/**
	 * フルスクリーンダイアログ表示<br>
	 *
	 * @param hasMessage メッセージ有無
	 */
	protected void showFullScreenDialog(boolean hasMessage) {
		LogUtil.d(TAG, "[IN ] showFullScreenDialog()");

		if (hasMessage) {
			// フルスクリーンダイアログ生成
			OkDialog dialog = OkDialog.newInstance(R.string.app_name, R.string.msg_full_screen);
			dialog.setOnOkClickListener(new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 終了
					dialog.dismiss();
				}
			});
			dialog.show(this.getSupportFragmentManager(), TAG);
		}

		LogUtil.d(TAG, "[OUT] showFullScreenDialog()");
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
			home_imageButton2.setEnabled(true);
			// Prevボタン
			home_imageButton3.setEnabled(false);
			// Nextボタン
			home_imageButton4.setEnabled(false);
			// リロードボタン
			home_imageButton5.setEnabled(true);
			// キャンセルボタン
			home_imageButton6.setEnabled(true);
			// オプションメニューボタン
			home_imageButton7.setEnabled(true);
			// プログレス
			home_progressBar1.setVisibility(View.VISIBLE);
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
			home_imageButton2.setEnabled(true);
			// Prevボタン
			home_imageButton3.setEnabled(home_webView1.canGoBack());
			// Nextボタン
			home_imageButton4.setEnabled(home_webView1.canGoForward());
			// リロードボタン
			home_imageButton5.setEnabled(true);
			// キャンセルボタン
			home_imageButton6.setEnabled(false);
			// オプションメニューボタン
			home_imageButton7.setEnabled(true);
			// プログレス非表示
			home_progressBar1.setVisibility(View.GONE);

		}
		LogUtil.d(TAG, "[OUT] showToolBar()");
	}
}