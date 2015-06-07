/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

/**
 * 判定ユーティリティクラス.
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class CheckUtil {
	static final String TAG = "CheckUtil";

	/**
	 * デフォルトコンストラクタ.
	 */
	private CheckUtil() {
	}

	/**
	 * Android3.0判定処理
	 * <p>
	 * Android3.0の場合trueを返す。
	 * </p>
	 * @return 処理結果
	 */
	public static boolean isHoneycomb() {
		LogUtil.d(TAG, "[IN ] isHoneycomb()");
		LogUtil.d(TAG, "SDK_INT:" + Build.VERSION.SDK_INT);
		LogUtil.d(TAG, "HONEYCOMB:" + Build.VERSION_CODES.HONEYCOMB);

		boolean result = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

		LogUtil.d(TAG, "result:" + result);
		LogUtil.d(TAG, "[OUT] isHoneycomb()");
		return result;
	}

	/**
	 * タブレット判定処理
	 * <p>
	 * タブレットの場合trueを返す。<br>
	 * Nexsus7のサイズもタブレットとして判断する。
	 * </p>
	 * @param context コンテキスト
	 * @return 処理結果
	 */
	public static boolean isHoneycombTablet(Context context) {
		LogUtil.d(TAG, "[IN ] isHoneycombTablet()");

		boolean result = isHoneycomb()
				&& (((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)
				|| ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE));

		LogUtil.d(TAG, "result:" + result);
		LogUtil.d(TAG, "[OUT] isHoneycombTablet()");
		return result;
	}

	/**
	 * HWチェック(カメラ)処理
	 * <p>
	 * カメラが利用可能かチェックを行う。
	 * </p>
	 * @param context コンテキスト
	 * @return 処理結果
	 */
	public static boolean checkHardwareCamera(Context context) {
		LogUtil.d(TAG, "[IN ] checkHardwareCamera()");

		/* HWでカメラが使用可能か? */
		boolean result = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);

		if (false == result) {
			result = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
		}

		LogUtil.d(TAG, "result:" + result);
		LogUtil.d(TAG, "[OUT] checkHardwareCamera()");
		return result;
	}

	/**
	 * センサーチェック(照度センサー)処理
	 * <p>
	 * 照度センサーが利用可能かチェックを行う。
	 * </p>
	 * @param context コンテキスト
	 * @return 処理結果
	 */
	public static boolean checkSensorLight(Context context) {
		LogUtil.d(TAG, "[IN ] checkSensorLight()");

		boolean result;

		/* センサーマネージャ取得 */
		SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

		/* 照度センサー取得 */
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_LIGHT);

		/* 「0」より大きければセンサー取得成功 */
		if (0 < sensors.size()) {
			result = true;
		} else {
			result = false;
		}

		LogUtil.d(TAG, "result:" + result);
		LogUtil.d(TAG, "[OUT] checkSensorLight()");
		return result;
	}

	/**
	 * センサーチェック(加速度センサー)処理
	 * <p>
	 * 加速度センサーが利用可能かチェックを行う。
	 * </p>
	 * @param context コンテキスト
	 * @return 処理結果
	 */
	public static boolean checkSensorAccelerometer(Context context) {
		LogUtil.d(TAG, "[IN ] checkSensorAccelerometer()");

		boolean result;

		/* センサーマネージャ取得 */
		SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

		/* 加速度センサー取得 */
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

		/* 「0」より大きければセンサー取得成功 */
		if (0 < sensors.size()) {
			result = true;
		} else {
			result = false;
		}

		LogUtil.d(TAG, "result:" + result);
		LogUtil.d(TAG, "[OUT] checkSensorAccelerometer()");
		return result;
	}

	/**
	 * インテントチェック処理
	 * <p>
	 * インテント使用しようとするアプリが存在するかチェックを行う。
	 * </p>
	 * @param context コンテキスト
	 * @param intent インテント
	 * @return 処理結果
	 */
	public static boolean checkIntent(Context context, Intent intent) {
		LogUtil.d(TAG, "[IN ] checkIntent()");

		boolean result;

		/* パッケージマネージャ取得 */
		PackageManager packageManager = context.getPackageManager();

		/* 指定インテントのアプリ取得 */
		List<ResolveInfo> apps = packageManager.queryIntentActivities(intent, 0);

		/* 「0」より大きければアプリインストール済 */
		if (0 < apps.size()) {
			result = true;
		} else {
			result = false;
		}

		LogUtil.d(TAG, "result:" + result);
		LogUtil.d(TAG, "[OUT] checkIntent()");
		return result;
	}
}
