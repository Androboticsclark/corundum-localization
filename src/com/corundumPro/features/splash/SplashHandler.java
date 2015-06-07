package com.corundumPro.features.splash;

import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.dao.DBAdapter;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.features.appsmanager.ApplicationListActivity;
import com.corundumPro.features.home.HomeActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * SplashHandlerクラス
 * <p>
 * 「スプラッシュハンドラ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class SplashHandler implements Runnable {
	static final String TAG = "SplashHandler";

	private Activity activity;

	public SplashHandler(Activity activity) {
		LogUtil.d(TAG, "[IN ] SplashHandler()");

		this.activity = activity;

		LogUtil.d(TAG, "[OUT] SplashHandler()");
	}

	/**
	 * 実行処理
	 * <p>
	 * HomeActivityの起動を行う。
	 * </p>
	 */
	public void run() {
		LogUtil.d(TAG, "[IN ] run()");

		Intent intent;

		SharedPreferences preference = activity.getApplicationContext().getSharedPreferences(SystemInfo.PREF_SYS_INFO, Context.MODE_PRIVATE);
		LogUtil.d(TAG, "SystemInfo.KEY_HOME_MODE:" + preference.getInt(SystemInfo.KEY_HOME_MODE, DBAdapter.HOME_MODE_LIST));

		if (DBAdapter.HOME_MODE_LIST == preference.getInt(SystemInfo.KEY_HOME_MODE, DBAdapter.HOME_MODE_LIST)) {
			// アプリリストの場合、ApplicationListActivityを起動
			LogUtil.d(TAG, "ApplicationListActivity");
			intent = new Intent(activity.getApplication(), ApplicationListActivity.class);
			activity.startActivity(intent);

		} else {
			// URLまたはアプリを指定の場合、HomeActivityを起動
			LogUtil.d(TAG, "HomeActivity");
			intent = new Intent(activity.getApplication(), HomeActivity.class);
			activity.startActivity(intent);
		}

		/* 終了 */
		activity.finish();

		LogUtil.d(TAG, "[OUT] run()");
	}
}
