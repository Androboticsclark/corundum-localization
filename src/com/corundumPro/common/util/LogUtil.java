/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.util;

import android.util.Log;

/**
 * ログユーティリティクラス
 *
 * @author androbotics.ltd
 */
public class LogUtil {
	static final String TAG = "LogUtil";

	/*
	 * ログフラグ
	 */
	/** ログフラグ：verbose */
	static boolean isVerbose = false;

	/** ログフラグ:debug */
	static boolean isDebug = false;

	/** ログフラグ：information */
	static boolean isInfo = false;

	/** ログフラグ：warning */
	static boolean isWarn = true;

	/** ログフラグ：error */
	static boolean isError = true;

	/**
	 * デフォルトコンストラクタ.
	 */
	private LogUtil() {
	}

	/**
	 * ログ出力処理(verbose)
	 * <p>
	 * ログ出力を行う。<br>
	 * </p>
	 * @param tag タグ
	 * @param msg メッセージ
	 */
	public static void v(String tag, String msg){
		if (true == isVerbose) {
			Log.v(tag, msg);
		}
	}

	/**
	 * ログ出力処理(debug)
	 * <p>
	 * ログ出力を行う。<br>
	 * </p>
	 * @param tag タグ
	 * @param msg メッセージ
	 */
	public static void d(String tag, String msg){
		if (true == isDebug) {
			Log.d(tag, msg);
		}
	}

	/**
	 * ログ出力処理(info)
	 * <p>
	 * ログ出力を行う。<br>
	 * </p>
	 * @param tag タグ
	 * @param msg メッセージ
	 */
	public static void i(String tag, String msg){
		if (true == isInfo) {
			Log.i(tag, msg);
		}
	}

	/**
	 * ログ出力処理(warn)
	 * <p>
	 * ログ出力を行う。<br>
	 * </p>
	 * @param tag タグ
	 * @param msg メッセージ
	 */
	public static void w(String tag, String msg){
		if (true == isWarn) {
			Log.w(tag, msg);
		}
	}

	/**
	 * ログ出力処理(error)
	 * <p>
	 * ログ出力を行う。<br>
	 * </p>
	 * @param tag タグ
	 * @param msg メッセージ
	 */
	public static void e(String tag, String msg){
		if (true == isError) {
			Log.e(tag, msg);
		}
	}

	/**
	 * 開発環境用ログ出力設定
	 * <p>
	 * 開発環境用にログの出力を設定する。<br>
	 * </p>
	 */
	public static void initDev() {
		isVerbose = true;
		isDebug = true;
		isInfo = true;
		isWarn = true;
		isError = true;
	}

	/**
	 * テスト環境用ログ出力設定
	 * <p>
	 * テスト環境用にログの出力を設定する。<br>
	 * </p>
	 */
	public static void initStage() {
		isVerbose = false;
		isDebug = true;
		isInfo = true;
		isWarn = true;
		isError = true;
	}

	/**
	 * 本番環境用ログ出力設定
	 * <p>
	 * 本番環境用にログの出力を設定する。<br>
	 * </p>
	 */
	public static void initProd() {
		isVerbose = false;
		isDebug = false;
		isInfo = false;
		isWarn = true;
		isError = true;
	}
}
