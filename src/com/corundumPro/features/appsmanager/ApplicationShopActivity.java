package com.corundumPro.features.appsmanager;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.corundumPro.R;
import com.corundumPro.common.activity.AbstractAuthActivity;
import com.corundumPro.common.dto.JsonResultDto;
import com.corundumPro.common.exception.ArcAppException;
import com.corundumPro.common.exception.ArcBaseMsgException;
import com.corundumPro.common.util.ArchiveUtil;
import com.corundumPro.common.util.EnvUtil;
import com.corundumPro.common.util.FileUtil;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.common.util.SessionSyncUtil;
import com.corundumPro.common.util.StringUtil;

/**
 * ApplicationShopActivityクラス
 * <p>
 * 「アプリケーションショップアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class ApplicationShopActivity extends AbstractAuthActivity {
	static final String TAG = "ApplicationShopActivity";

	public static final String REGEXP_FILE_NAME = "^.*filename=\"(.+)\".*$";

	/*
	 * 画面
	 */
	/** WEB表示 */
	private WebView home_webView1;

	/*
	 * ダウンロードマネージャー
	 */
	/** リクエストID */
	private long requestId;

	/** ダウンロードマネージャー */
	private DownloadManager downloadManager;

	/** ダウンロードマネージャーレシーバ */
	private DownloadManagerReceiver downloadManagerReceiver;

	/** ファイル名 */
	private String fileName;

	/** プリケーション名 */
	private String applicationName;

	/** アプリケーションルートディレクトリ */
	private File rootAppDir;

	/** アプリケーションディレクトリ */
	private File appDir;

	/** ダウンロードファイル */
	private File downloadFile;

//	/** プログレスダイアログ */
//	private static ProgressDialog progressDialog;

	/** メニューボタン */
	private Button application_shop_Button1;

	/** HTTP Client */
	DefaultHttpClient httpClient;

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

		/* 画面のフォーカスを外す */
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		/* Viewにレイアウト設定 */
		setContentView(R.layout.application_shop);

		/* View初期化 */
		initViews();

		// HTTPクライアント生成
		httpClient = new DefaultHttpClient();

		// ダウンロードレシーバ
		downloadManagerReceiver = new DownloadManagerReceiver();

		// ローカルからユーザ情報を取得
		authFunction.loadUserInfo(preference, userInfoDto);

		// 登録済みユーザ情報削除(Dev環境のみ設定可)
		if (EnvUtil.isDeleteUserInfo()) {
			authFunction.deleteUserInfo(editor, userInfoDto);
		}

		if (!authFunction.hastUserInfo(userInfoDto)) {
			// ユーザ情報が存在しない場合、入力ダイアログ経由で認証処理を行う。
			login(getHttpClient());

		} else {
			// ユーザ情報が存在する場合、直接Corundum Apps Manager認証を行う (非同期開始)
			authFunction.authenticate(getHttpClient(), ApplicationShopActivity.this, userInfoDto);
		}

		LogUtil.d(TAG, "[OUT] onCreate()");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtil.d(TAG, "[IN ] onResume()");

		// ダウンロードマネージャ取得
		downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
		// ダウンロードレシーバ登録
		registerReceiver(downloadManagerReceiver, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));

		LogUtil.d(TAG, "[OUT] onResume()");
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtil.d(TAG, "[IN ] onPause()");

		// ダウンロードレシーバ削除
		unregisterReceiver(downloadManagerReceiver);

		LogUtil.d(TAG, "[OUT] onPause()");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtil.d(TAG, "[IN ] onStop()");
		LogUtil.d(TAG, "[OUT] onStop()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(TAG, "[IN ] onDestroy()");

		// HTTPクライアントシャットダウン
		if (this.httpClient != null) {
			this.httpClient.getConnectionManager().shutdown();
		}

		LogUtil.d(TAG, "[OUT] onDestroy()");
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

			/* 遷移可能な場合のみ「戻る」 */
			if (home_webView1.canGoBack()) {
				home_webView1.goBack();

				LogUtil.d(TAG, "return false");
				LogUtil.d(TAG, "[OUT] onKeyDown()");
				return false;
			} else {
				/* ApplicationListActivity起動 */
				LogUtil.d(TAG, "ApplicationListActivity");
				Intent intent = new Intent(this, ApplicationListActivity.class);
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

	@Override
	public DefaultHttpClient getHttpClient() {
		return this.httpClient;
	}

	@Override
	public void onAuthPreparation() {
		LogUtil.d(TAG, "[IN ] onAuthPreparation()");

		// プログレスダイアログを表示
		super.showSpinningDialog();

		LogUtil.d(TAG, "[OUT] onAuthPreparation()");
	}

	@Override
	public void onAuthSuccess(JsonResultDto json) {
		LogUtil.d(TAG, "[IN ] onAuthSuccess()");
		LogUtil.d(TAG, "json:" + json.toString());

		// プログレスダイアログを閉じる
		super.closeSpinningDialog();

		// プリファレンスにユーザ情報を保存
		authFunction.saveUserInfo(editor, userInfoDto);

		// HTTPクライアントのセッション情報をWebViewと共有する
		SessionSyncUtil.httpClient2WebView(httpClient);

		/* ダウンロードページに遷移 */
		home_webView1.loadUrl(EnvUtil.getDownloadUrl());

		LogUtil.d(TAG, "[OUT] onAuthSuccess()");
	}

	@Override
	public void onAuthFailure(JsonResultDto json) {
		LogUtil.d(TAG, "[IN ] onAuthFailure()");
		LogUtil.d(TAG, "json:" + json.toString());

		// プログレスダイアログを閉じる
		super.closeSpinningDialog();

		// 処理に失敗した場合、エラーダイアログを表示
		showErrorDialog(json.getMessage());

		LogUtil.d(TAG, "[OUT] onAuthFailure()");
	}

	@Override
	public void onAuthException(Throwable e) {
		LogUtil.d(TAG, "[IN ] onAuthException()");
		LogUtil.e(TAG, "Throwable:" + e.getMessage());

		// プログレスダイアログを閉じる
		super.closeSpinningDialog();

		// 処理中に例外が発生した場合、エラーダイアログを表示
		if (e instanceof ArcBaseMsgException) {
			ArcBaseMsgException ex = (ArcBaseMsgException)e;
			this.showErrorDialog(ex.getResourceId());
		}

		LogUtil.d(TAG, "[OUT] onAuthException()");
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

		/* メニューボタン */
		application_shop_Button1 = (Button)findViewById(R.id.application_shop_Button1);
		application_shop_Button1.setOnClickListener(menuListener);

		/* WEB表示 */
		home_webView1 = (WebView)findViewById(R.id.application_shop_webView1);
		home_webView1.requestFocus();
		home_webView1.getSettings().setJavaScriptEnabled(true);
		home_webView1.getSettings().setDomStorageEnabled(true);
		home_webView1.getSettings().setBuiltInZoomControls(true);
		home_webView1.getSettings().setSupportZoom(true);
		home_webView1.getSettings().setUseWideViewPort(true);
		home_webView1.getSettings().setLoadWithOverviewMode(true);
		// User Agent設定
		home_webView1.getSettings().setUserAgentString(EnvUtil.getUserAgent());

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

				super.shouldOverrideUrlLoading(webView, url);
				LogUtil.d(TAG, "return false");
				LogUtil.d(TAG, "[OUT] shouldOverrideUrlLoading()");
				return false;
			}
		});

		/* ダウンロードリスナ */
		home_webView1.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				LogUtil.d(TAG, "[IN ] onDownloadStart()");
				LogUtil.d(TAG, "url:" + url);
				LogUtil.d(TAG, "contentDisposition:" + contentDisposition);
				LogUtil.d(TAG, "mimetype:" + mimetype);
				LogUtil.d(TAG, "contentLength:" + contentLength);

				/* URLチェック(コランダムアプリ配信サーバー以外のダウンロード抑止) */
				if (false == url.startsWith(EnvUtil.getRootUrl())) {
					LogUtil.d(TAG, "Unknown Server.");
					LogUtil.d(TAG, "[OUT] onDownloadStart()");
					return;
				}

				/* ファイル,アプリ名取得 */
				fileName = StringUtil.extractMatchString(REGEXP_FILE_NAME, contentDisposition, 1);
				applicationName = FilenameUtils.getBaseName(fileName);
				rootAppDir = new File(FileUtil.getBaseDirPath(getApplicationContext()) + "/app");
				appDir = new File(rootAppDir, applicationName);
				downloadFile = new File(rootAppDir, fileName);

				// プログレスダイアログ表示
				showSpinningDialog(
					 R.string.app_name
					,R.string.downloading
					,new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							LogUtil.d(TAG, "[IN ] onClick()");
							LogUtil.d(TAG, "which:" + which);

							// ダウンロードマネージャー停止
							downloadManager.remove(requestId);

							// ダウンロードファイル削除
							FileUtil.deleteQuietly(downloadFile);

							// プログレスダイアログ終了
							closeSpinningDialog();

							Toast.makeText(getApplicationContext(), R.string.msg_download_cancel, Toast.LENGTH_LONG).show();

							LogUtil.d(TAG, "[OUT] onClick()");
						}
					}
				);

				DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
				// タイトル設定
				request.setTitle(getResources().getString(R.string.app_name) + " " + getResources().getString(R.string.downloading));
				// ダウンロードタスクの詳細記述を設定
				request.setDescription(url);
				// クッキーを設定
				request.addRequestHeader("Cookie", CookieManager.getInstance().getCookie(url));
				// UserAgentを設定
				request.addRequestHeader("User-Agent", EnvUtil.getUserAgent());
				// ダウンロード先を設定
				request.setDestinationUri(Uri.fromFile(downloadFile));

				// キューにダウンロードタスクを追加、タスクIDを同時に取得
				requestId = downloadManager.enqueue(request);

				LogUtil.d(TAG, "requestId:" + requestId);
			}
		});

		LogUtil.d(TAG, "[OUT] initViews()");
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

			/* ApplicationListActivity起動 */
			LogUtil.d(TAG, "ApplicationListActivity");
			Intent intent = new Intent(ApplicationShopActivity.this, ApplicationListActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] menuListener::onClick()");
		}
	};

	/**
	 * DownloadManagerReceiverクラス
	 * <p>
	 * 「ダウンロードマネージャーレシーバー」クラス
	 * </p>
	 * @author androbotics.ltd
	 */
	public class DownloadManagerReceiver extends BroadcastReceiver {
		static final String TAG = "DownloadManagerReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtil.d(TAG, "[IN ] onReceive()");

			try {
				// ダウンロードファイルが存在しない
				if (!downloadFile.exists()) {
					LogUtil.d(TAG, "Not found Download File.");
					LogUtil.d(TAG, "tmpFile:" + downloadFile.getName());
					throw new ArcAppException(R.string.log_err_no_download_file);
				}

				// ZIPファイル解凍
				ArchiveUtil.unCompress(rootAppDir, downloadFile);

			} catch (ArcAppException e) {
				LogUtil.e(TAG, context.getString(e.getResourceId()));

				// アプリフォルダ削除
				FileUtil.deleteQuietly(appDir);

				// エラーダイアログを表示
				ApplicationShopActivity.this.showErrorDialog(R.string.err_download_failure);

			} finally {
				// ダウンロードファイル削除
				FileUtil.deleteQuietly(downloadFile);

				// プログレスダイアログ終了
				ApplicationShopActivity.this.closeSpinningDialog();

				LogUtil.d(TAG, "[OUT] onReceive()");
			}
		}
	}

}