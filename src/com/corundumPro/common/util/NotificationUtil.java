/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.corundumPro.R;
import com.corundumPro.features.splash.SplashActivity;

/**
 * 通知ユーティリティクラス.
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class NotificationUtil {
	static final String TAG = "NotificationUtil";

	/**
	 * デフォルトコンストラクタ.
	 */
	private NotificationUtil() {
	}

	/**
	 * 通知送信処理
	 * <p>
	 * 通知送信処理を行う。
	 * </p>
	 * @param context コンテキスト
	 * @param resources リソース
	 * @param ticker ステータスバー表示文字
	 * @param title タイトル
	 * @param message メッセージ
	 */
	public static void sendNotification(Context context, Resources resources, String ticker, String title, String message) {
		LogUtil.d(TAG, "[IN ] sendNotification()");
		LogUtil.d(TAG, "ticker:" + ticker);
		LogUtil.d(TAG, "title:" + title);
		LogUtil.d(TAG, "message:" + message);

		/* 通知マネージャ取得 */
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		/* インテント設定 */
		Intent intent = new Intent(context, SplashActivity.class);

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

		builder.setContentIntent(contentIntent);

		/* ステータスバー表示文字設定 */
		builder.setTicker(ticker);

		/* ステータスバーアイコン設定 */
		builder.setSmallIcon(R.drawable.ic_launcher);

		/* タイトル設定 */
		builder.setContentTitle(title);

		/* メッセージ設定 */
		builder.setContentText(message);

		/* アイコン設定 */
		builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher));

		/* 通知タイミング設定 */
		builder.setWhen(System.currentTimeMillis());
		builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);

		/* タップでキャンセル可能に設定 */
		builder.setAutoCancel(true);

		/* 通知 */
		notificationManager.notify(R.string.app_name, builder.build());

		LogUtil.d(TAG, "[OUT] sendNotification()");
	}
}
