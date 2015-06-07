/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.activity;

import org.apache.http.impl.client.DefaultHttpClient;

import android.content.res.Resources;


/**
 * 非同期処理を伴うアクティビティでimplementします。<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public interface IAsyncActivity {

	/**
	 * HTTPクライアントを取得します。
	 *
	 */
	public DefaultHttpClient getHttpClient();

	/**
	 * Resourcesを取得します。
	 */
	public Resources getResources();
}
