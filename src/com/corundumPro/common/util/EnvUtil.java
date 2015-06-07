/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.util;

import android.content.Context;

import com.corundumPro.R;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.dao.DBAdapter;


/**
 * 環境設定クラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class EnvUtil {

	/** 開発環境 */
	public static final String ENV_DEV = "dev";

	/** テスト環境 */
	public static final String ENV_STAGE = "stage";

	/** 本番環境 */
	public static final String ENV_PROD = "prod";

	/** 環境設定値 */
	private static String environment;

	/** User Agent */
	private static String userAgent = "";
	/** URL(URLを指定) デフォルト：Google URL */
	private static String homeHtmlPath = Const.URL_GOOGLE;
	/** URL(アプリを指定) デフォルト：fileプロトコル */
	private static String homeAppPath = Const.PROTOCOL_FILE;
	/** ホームモード(デフォルト：アプリリスト) */
	private static int homeMode = DBAdapter.HOME_MODE_LIST;
	/** 現在のURL */
	private static String currentUrl = Const.URL_GOOGLE;
	/** 起動アイコンURL */
	private static String shortcutUrl = "";
	/** Corundum Apps Manger ドメイン */
	private static String camDomain = "";
	/** Corundum Apps Manger ポート */
	private static String camPort = "";
	/** Corundum Apps Manger Cookie ドメイン */
	private static String camCookieDomain = "";
	/** Corundum Apps Manager Root URL */
	private static String camRootUrl = "";
	/** ログインURL */
	private static String loginUrl = "";
	/** アップロードURL */
	private static String uploadUrl = "";
	/** ダウンロードURL */
	private static String downloadUrl = "";
	/** HTTP通信タイムアウト(秒) */
	private static int connectionTimeout;
	/** ソケット通信タイムアウト(秒) */
	private static int socketTimeout;
	/** リトライ回数 */
	private static int retry;
	/** 登録済みユーザ情報削除 */
	private static boolean isDeleteUserInfo = false;

	/**
	 * コンストラクタ<br>
	 */
	private EnvUtil() {
	}

	/**
	 * 環境設定に応じ、初期化する。<br>
	 *
	 */
	public static void initEnv(String env, Context context) {

		// 環境を設定
		environment = env;

		// User Agent設定
		userAgent = context.getString(R.string.user_agent);

		if (ENV_DEV.equals(environment)) {
			// 開発環境の場合の設定
			LogUtil.initDev();

			// Corundum Apps Manager ドメイン
			camDomain = context.getString(R.string.dev_cam_domain);

			// Corundum Apps Manager ポート
			camPort = context.getString(R.string.dev_cam_port);

			// Corundum Apps Manager Cookie ドメイン
			camCookieDomain = camDomain;

			// Corundum Apps Manager Root URL
			camRootUrl = Const.PROTOCOL_HTTP + camDomain + camPort;

			// URL(アプリを指定)を変更する
			if (false == CheckUtil.isHoneycombTablet(context)) {
				homeAppPath = Const.PROTOCOL_FILE + FileUtil.getBaseDirPath(context) + "/www/" + DBAdapter.LOCAL_HTMLM;
			} else {
				homeAppPath = Const.PROTOCOL_FILE + FileUtil.getBaseDirPath(context) + "/www/" + DBAdapter.LOCAL_HTMLH;
			}

			// 起動アイコンURLを変更
			shortcutUrl = homeAppPath;

			// ホームモードをアプリに変更
			homeMode = DBAdapter.HOME_MODE_APP;

			// 現在のURLをURL(アプリを指定)に変更
			currentUrl = homeAppPath;

			// ログインURL設定
			loginUrl = camRootUrl + context.getString(R.string.dev_cam_login_path);

			// アップロードURL設定
			uploadUrl = camRootUrl + context.getString(R.string.dev_cam_upload_path);

			// ダウンロードURL設定
			downloadUrl = camRootUrl + context.getString(R.string.dev_cam_download_path);

			// HTTP通信タイムアウト(秒)設定
			connectionTimeout = Integer.parseInt(context.getString(R.string.dev_http_connection_timeout));

			// ソケット通信タイムアウト(秒)設定
			socketTimeout = Integer.parseInt(context.getString(R.string.dev_socket_timeout));

			// リトライ回数設定
			retry = Integer.parseInt(context.getString(R.string.dev_retry));

			// 登録済みユーザ情報削除(dev環境のみ有効)
			isDeleteUserInfo = Boolean.parseBoolean(context.getString(R.string.delete_user_info));

		} else if (ENV_STAGE.equals(environment)) {
			// テスト環境の場合の設定
			LogUtil.initStage();

		} else {
			// 本番環境の場合の設定
			LogUtil.initProd();

			// Corundum Apps Manager ドメイン
			camDomain = context.getString(R.string.prod_cam_domain);

			// Corundum Apps Manager ポート
			camPort = context.getString(R.string.prod_cam_port);

			// Corundum Apps Manager Cookie ドメイン
			camCookieDomain = camDomain;

			// Corundum Apps Manager Root URL
			camRootUrl = Const.PROTOCOL_HTTP + camDomain + camPort;

			// ログインURL設定
			loginUrl = camRootUrl + context.getString(R.string.prod_cam_login_path);

			// アップロードURL設定
			uploadUrl = camRootUrl + context.getString(R.string.prod_cam_upload_path);

			// ダウンロードURL設定
			downloadUrl = camRootUrl + context.getString(R.string.prod_cam_download_path);

			// HTTP通信タイムアウト(秒)設定
			connectionTimeout = Integer.parseInt(context.getString(R.string.prod_http_connection_timeout));

			// ソケット通信タイムアウト(秒)設定
			socketTimeout = Integer.parseInt(context.getString(R.string.prod_socket_timeout));

			// リトライ回数設定
			retry = Integer.parseInt(context.getString(R.string.prod_retry));
		}
	}

	/**
	 * User Agentを取得します。
	 *
	 * @return User Agent
	 */
	public static String getUserAgent() {
		return userAgent;
	}

	/**
	 * ホームモードを取得します。
	 *
	 * @return ホームモード
	 */
	public static int getHomeMode() {
		return homeMode;
	}

	/**
	 * 現在のURLを取得します。
	 *
	 * @return 現在のURL
	 */
	public static String getCurrentUrl() {
		return currentUrl;
	}

	/**
	 * 起動アイコンURLを取得します。
	 *
	 * @return 起動アイコンURL
	 */
	public static String getShortcutUrl() {
		return shortcutUrl;
	}

	/**
	 * URL(URLを指定) を取得します。
	 *
	 * @return URL(URLを指定)
	 */
	public static String getHomeHtmlPath() {
		return homeHtmlPath;
	}

	/**
	 * URL(アプリを指定) を取得します。
	 *
	 * @return URLURL(アプリを指定)
	 */
	public static String getHomeAppPath() {
		return homeAppPath;
	}

	/**
	 * ドメインを取得します。
	 *
	 * @return ドメイン
	 */
	public static String getDomain() {
		return camDomain;
	}

	/**
	 * Cookieドメインを取得します。
	 *
	 * @return Cookieドメイン
	 */
	public static String getCookieDomain() {
		return camCookieDomain;
	}

	/**
	 * ルートURLを取得します。
	 *
	 * @return ルートURL
	 */
	public static String getRootUrl() {
		return camRootUrl;
	}

	/**
	 * ログインURLを取得します。
	 *
	 * @return ログインURL
	 */
	public static String getLoginUrl() {
		return loginUrl;
	}

	/**
	 * アップロードURLを取得します。
	 *
	 * @return アップロードURL
	 */
	public static String getUploadUrl() {
		return uploadUrl;
	}

	/**
	 * ダウンロードURLを取得します。
	 *
	 * @return ダウンロードURL
	 */
	public static String getDownloadUrl() {
		return downloadUrl;
	}

	/**
	 * HTTP通信タイムアウト(秒)を取得します。
	 *
	 * @return HTTP通信タイムアウト(秒)
	 */
	public static int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * ソケット通信タイムアウト(秒)を取得します。
	 *
	 * @return ソケット通信タイムアウト(秒)
	 */
	public static int getSocketTimeout() {
		return socketTimeout;
	}

	/**
	 * リトライ回数を取得します。
	 *
	 * @return リトライ回数
	 */
	public static int getRetry() {
		return retry;
	}

	/**
	 * 登録済みユーザ情報削除フラグ
	 *
	 * @return 削除OK:true, 削除NG:false
	 */
	public static boolean isDeleteUserInfo() {
		boolean result = false;
		if (ENV_DEV.equals(environment)) {
			result = isDeleteUserInfo;
		}
		return result;
	}
}
