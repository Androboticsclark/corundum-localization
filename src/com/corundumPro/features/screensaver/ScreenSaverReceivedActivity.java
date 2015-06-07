package com.corundumPro.features.screensaver;

import com.corundumPro.common.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * ScreenSaverReceivedActivityクラス
 * <p>
 * 「スクリーンセーバーレシーブドアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class ScreenSaverReceivedActivity extends BroadcastReceiver {
	static final String TAG = "ScreenSaverReceivedActivity";

	/**
	 * 受信イベント処理
	 * <p>
	 * onReceive()のオーバーライド<br>
	 * ブロードキャスト受信時時にコールされる。
	 * </p>
	 * @param context コンテキスト
	 * @param intent インテント
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		LogUtil.d(TAG, "[IN ] onReceive()");

		int shortcutId = 0;

		/* ScreenSaverActivity起動 */
		LogUtil.d(TAG, "ScreenSaverActivity");
		Intent screensaverIntent = new Intent(context, ScreenSaverActivity.class);
		screensaverIntent.putExtra("shortcutId", String.valueOf(shortcutId));
		screensaverIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(screensaverIntent);

		LogUtil.d(TAG, "[OUT] onReceive()");
	}
}