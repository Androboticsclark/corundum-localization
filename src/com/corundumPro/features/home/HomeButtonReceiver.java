package com.corundumPro.features.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.common.util.NotificationUtil;

public class HomeButtonReceiver extends BroadcastReceiver {
	static final String TAG = "HomeButtonReceive";

	BaseActivity activity;

	/**
	 * BaseActivity設定処理
	 * <p>
	 * BaseActivityを設定する。
	 * </p>
	 * @param baseActivity BaseActivity
	 */
	public void setBaseActivity(BaseActivity baseActivity) {
		LogUtil.d(TAG, "[IN ] setActivity()");

		this.activity = baseActivity;

		LogUtil.d(TAG, "[OUT] setActivity()");
	}

	/**
	 * 受信処理
	 * <p>
	 * onReceive()のオーバーライド<br>
	 * ホームボタン押下時にコールされる。
	 * </p>
	 * @param context コンテキスト
	 * @param intent インテント
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		LogUtil.d(TAG, "[IN ] onReceive()");

		/* プリファレンスの準備 */
		SharedPreferences preference = activity.getApplicationContext().getSharedPreferences(SystemInfo.PREF_SYS_INFO, Context.MODE_PRIVATE);

		/* 「終了通知フラグ」がONの場合、通知を行う。 */
		if (true == preference.getBoolean(SystemInfo.KEY_EXIT_FLAG, false)) {
			/* 通知処理 */
			NotificationUtil.sendNotification(activity.getApplicationContext(), activity.getResources(), activity.getString(R.string.msg_ticker), activity.getString(R.string.app_name), activity.getString(R.string.msg_restart));
		}

		/* ホームボタンレシーバ破棄 */
		activity.deleteReceiver();

		LogUtil.d(TAG, "[OUT] onReceive()");
	}
}
