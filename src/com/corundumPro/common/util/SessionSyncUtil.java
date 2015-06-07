/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.util;

import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * HTTPClient<->WebViewセッション同期ユーティリティクラス.
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class SessionSyncUtil {
	static final String TAG = "SessionSyncUtil";

	/**
	 * デフォルトコンストラクタ.
	 */
	private SessionSyncUtil() {
	}

	// COOKIE取得用のドメイン
	private static final String COOKIE_DOMAIN = EnvUtil.getCookieDomain();
	// COOKIEを送出するURL
	private static final String COOKIE_URL = EnvUtil.getRootUrl();
	// セッションIDを格納するパラメータ名
	private static final String SESSID = "JSESSIONID";

	/**
	 * HttpClient側のセッションIDをWebViewに同期します
	 *
	 * @param httpClient セッションを同期するHttpClient
	 */
	public static void httpClient2WebView(DefaultHttpClient httpClient) {
		CookieStore store = httpClient.getCookieStore();
		List<Cookie> cookies = store.getCookies();
		CookieManager cookieManager = CookieManager.getInstance();
		// セッションCookie削除
		cookieManager.removeSessionCookie();

		for (Cookie cookie : cookies) {
			if (cookie.getDomain().indexOf(COOKIE_DOMAIN) < 0) {
				continue;
			}
			if (!SESSID.equals(cookie.getName())) {
				continue;
			}

			String cookieStr = cookie.getName() + "=" + cookie.getValue();
			Log.d(TAG, "cookieStr:" + cookieStr);
			cookieManager.setCookie(COOKIE_DOMAIN, cookieStr);
			CookieSyncManager.getInstance().sync();
		}
	}

	/**
	 * WebView側のセッションIDをHttpClientに同期します
	 *
	 * @param httpClient セッションを同期するHttpClient
	 */
	public static void webView2HttpClient(DefaultHttpClient httpClient) {
		String cookie = CookieManager.getInstance().getCookie(COOKIE_URL);
		String[] cookies = cookie.split(";");
		for (String keyValue : cookies) {
			keyValue = keyValue.trim();
			String[] cookieSet = keyValue.split("=");
			String key = cookieSet[0];
			String value = cookieSet[1];
			if (!SESSID.equals(key)) {
				continue;
			}
			BasicClientCookie bCookie = new BasicClientCookie(key, value);
			bCookie.setDomain(COOKIE_DOMAIN);
			bCookie.setPath("/");
			CookieStore store = httpClient.getCookieStore();
			store.addCookie(bCookie);
		}
	}
}
