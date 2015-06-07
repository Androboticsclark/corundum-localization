package com.corundumPro.features.splash;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.Toast;

import com.corundumPro.R;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.dao.DBAdapter;
import com.corundumPro.common.dto.ShortcutInfo;
import com.corundumPro.common.util.EnvUtil;
import com.corundumPro.common.util.FileUtil;
import com.corundumPro.common.util.ImageUtil;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.common.util.ResourceUtil;

/**
 * SplashActivityクラス
 * <p>
 * 「スプラッシュアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class SplashActivity extends FragmentActivity {
	static final String TAG = "SplashActivity";

	/*
	 * DB
	 */
	/** DBアダプター */
	private DBAdapter dbAdapter;

	/** 「ショートカット情報」 */
	private ShortcutInfo shortcutInfo;

	/** プリファレンス */
	private SharedPreferences preference;
	private Editor editor;

	/**
	 * 作成前処理
	 * <p>
	 * onResume()のオーバーライド<br>
	 * アクティビティ生成前にコールされる。
	 * </p>
	 */
	@Override
	protected void onResume() {
		LogUtil.d(TAG, "[IN ] onResume()");

		super.onResume();

		LogUtil.d(TAG, "[OUT] onResume()");
	}

	/**
	 * 作成処理
	 * <p>
	 * onCreate()のオーバーライド<br>
	 * アクティビティ生成時にコールされる。
	 * </p>
	 * @param bundle バンドル
	 */
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		LogUtil.d(TAG, "[IN ] onCreate()");

		/** プリファレンスの準備 */
		preference = getApplicationContext().getSharedPreferences(SystemInfo.PREF_SYS_INFO, Context.MODE_PRIVATE);
		editor = preference.edit();

		/* SDカードチェック */
		String status = Environment.getExternalStorageState();
		if (true == status.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			/* SDカード正常 */
		} else {
			Toast.makeText(SplashActivity.this, R.string.err_no_sdcard, Toast.LENGTH_LONG).show();

			/* 終了 */
			this.moveTaskToBack(true);
		}

		/* ショートカットID受け取り */
		int shortcutId;
		Intent intent = getIntent();
		String action = intent.getAction();
		if (true == Intent.ACTION_VIEW.equals(action)) {
			Uri uri = intent.getData();
			shortcutId = Integer.parseInt(uri.toString());
		} else {
			shortcutId = 1;
		}
		LogUtil.d(TAG, "shortcutId:" + shortcutId);

		/** 現在のショートカットID */
		editor.putInt(SystemInfo.KEY_CURRENT_SHORTCUT_ID, shortcutId);
		editor.commit();

		if (false == preference.getBoolean(SystemInfo.KEY_LAUNCHED_FLAG, false)) {
			/** 初回起動時の処理 */

			/** 環境設定 */
			EnvUtil.initEnv(this.getResources().getString(R.string.env), getApplicationContext());

			/** 起動済フラグ */
			editor.putBoolean(SystemInfo.KEY_LAUNCHED_FLAG, true);

			/** 現在のホームモード */
			editor.putInt(SystemInfo.KEY_CURRENT_HOME_MODE, EnvUtil.getHomeMode());

			/** 現在のWEB表示モード */
			editor.putInt(SystemInfo.KEY_CURRENT_WEB_MODE, DBAdapter.WEB_MODE_OPERATION);

			/** WEB表示モード */
			editor.putInt(SystemInfo.KEY_WEB_MODE, DBAdapter.WEB_MODE_OPERATION);

			/** ツールバーフラグ */
			editor.putBoolean(SystemInfo.KEY_BOTTOM_BAR_FLAG, true);

			/** ホームモード */
			editor.putInt(SystemInfo.KEY_HOME_MODE, EnvUtil.getHomeMode());

			/** ステータスバー非表示フラグ */
			editor.putBoolean(SystemInfo.KEY_STATUS_BAR_FLAG, true);

			/** ホワイトリストフラグ */
			editor.putBoolean(SystemInfo.KEY_WHITE_LIST_FLAG, false);

			/** スクリーンセーバーフラグ */
			editor.putBoolean(SystemInfo.KEY_SCREEN_SAVER_FLAG, false);

			/** スクリーンセーバー起動時間(sec) */
			editor.putInt(SystemInfo.KEY_SCREEN_SAVER_TIME, 300);

			/** スクリーンセーバーURL */
			editor.putString(SystemInfo.KEY_SCREEN_SAVER_URL, Const.URL_VASDAQ_TV);

			/** パスコードフラグ */
			editor.putBoolean(SystemInfo.KEY_PASS_CODE_FLAG, false);

			/** パスコード */
			editor.putString(SystemInfo.KEY_PASS_CODE, "0000");

			/** 終了時通知フラグ */
			editor.putBoolean(SystemInfo.KEY_EXIT_FLAG, false);

			File root = new File(FileUtil.getBaseDirPath(this));
			try {
				boolean ret = root.mkdirs();
				LogUtil.d(TAG, "ret:" + ret);
			} catch (Exception e) {
				LogUtil.d(TAG, e.getMessage());
			}

			File html = new File(FileUtil.getBaseDirPath(this) + "/www");
			try {
				boolean ret = html.mkdirs();
				LogUtil.d(TAG, "ret:" + ret);
			} catch (Exception e) {
				LogUtil.d(TAG, e.getMessage());
			}

			File images = new File(FileUtil.getBaseDirPath(this) + "/www/images");
			try {
				boolean ret = images.mkdirs();
				LogUtil.d(TAG, "ret:" + ret);
			} catch (Exception e) {
				LogUtil.d(TAG, e.getMessage());
			}

			File app = new File(FileUtil.getBaseDirPath(this) + "/app");
			try {
				boolean ret = app.mkdirs();
				LogUtil.d(TAG, "ret:" + ret);
			} catch (Exception e) {
				LogUtil.d(TAG, e.getMessage());
			}

			/* 「ショートカットアイコン」をSDへコピー */
			Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);

			if (null != icon) {
				ImageUtil.copyBitmap(icon, root, DBAdapter.SHORTCUT_ICON);
			}

			/* 「asset配下ファイル,フォルダ」をSDへコピー */
			ResourceUtil.copyResourceFiles(this.getApplicationContext(), root);

			/* 現在のURL */
			editor.putString(SystemInfo.KEY_CURRENT_URL, EnvUtil.getCurrentUrl());

			/* ホームパス */
			editor.putString(SystemInfo.KEY_HOME_HTML_PATH, EnvUtil.getHomeHtmlPath());

			// ホームアプリパス
			editor.putString(SystemInfo.KEY_HOME_APP_PATH, EnvUtil.getHomeAppPath());

			editor.commit();

		} else {
			/* 2回目以降の処理 */

			/* DB読み出し */
			loadDataBase();

// 2015.05.07 H.Yamazaki 修正開始
			/* 現在のホームモード */
			int currentHomeMode = preference.getInt(SystemInfo.KEY_HOME_MODE, DBAdapter.HOME_MODE_URL);
			editor.putInt(SystemInfo.KEY_CURRENT_HOME_MODE, currentHomeMode);

			/* 現在のURL */
			if (1 != shortcutId) {

				editor.putString(SystemInfo.KEY_CURRENT_URL, shortcutInfo.getHomeWebUrl());
			} else {
				if (currentHomeMode == DBAdapter.HOME_MODE_URL) {
					// ホームモードがURLの場合、URLを現在のURLに設定
					editor.putString(SystemInfo.KEY_CURRENT_URL, preference.getString(SystemInfo.KEY_HOME_HTML_PATH, ""));
				} else if (currentHomeMode == DBAdapter.HOME_MODE_APP) {
					// ホームモードがアプリの場合、アプリパスを現在のURLに設定
					editor.putString(SystemInfo.KEY_CURRENT_URL, preference.getString(SystemInfo.KEY_HOME_APP_PATH, ""));
				}
			}
// 2015.05.07 H.Yamazaki 修正終了

			/* 現在のWEB表示モード */
			editor.putInt(SystemInfo.KEY_CURRENT_WEB_MODE, preference.getInt(SystemInfo.KEY_WEB_MODE, DBAdapter.WEB_MODE_OPERATION));

			if (DBAdapter.WEB_MODE_OPERATION == preference.getInt(SystemInfo.KEY_WEB_MODE, DBAdapter.WEB_MODE_OPERATION)) {
				/* ツールバーフラグ */
				editor.putBoolean(SystemInfo.KEY_BOTTOM_BAR_FLAG, true);
			} else {
				/* ツールバーフラグ */
				editor.putBoolean(SystemInfo.KEY_BOTTOM_BAR_FLAG, false);
			}

			editor.commit();
		}

		/* タイトル非表示 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* Viewにレイアウト設定 */
		setContentView(R.layout.splash);

		/* 5ms遅延でsplashHandlerコール */
		Handler hdl = new Handler();
		hdl.postDelayed(new SplashHandler(this), 5);

		LogUtil.d(TAG, "[OUT] onCreate()");
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
	 * DB読み出し処理
	 * <p>
	 * 「システム情報」「ショートカット情報」「一時情報」のDBを読み出す。
	 * </p>
	 */
	public void loadDataBase() {
		LogUtil.d(TAG, "[IN ] loadDataBase()");

		/* DBアダプタ初期化 */
		dbAdapter = new DBAdapter(this);

		/* DBオープン */
		dbAdapter.open();

		/* 「ショートカット情報」テーブル読み出し */
		shortcutInfo = new ShortcutInfo();
		dbAdapter.getShortcutInfo(preference.getInt(SystemInfo.KEY_CURRENT_SHORTCUT_ID, 0), shortcutInfo);

		/* DBクローズ */
		dbAdapter.close();

		LogUtil.d(TAG, "[OUT] loadDataBase()");
	}
}
