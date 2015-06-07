/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.features.appsmanager;

import java.io.File;

import org.apache.http.impl.client.DefaultHttpClient;

import com.corundumPro.common.function.IAsyncCallback;
import com.corundumPro.common.task.AsyncHttpPostTask;
import com.corundumPro.common.util.EnvUtil;
import com.corundumPro.common.util.LogUtil;

/**
 * アップロードファンクション.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class UploadFunction {
	static final String TAG = "UploadFunction";

	/**
	 * コンストラクタ<br>
	 *
	 */
	public UploadFunction() {
		LogUtil.d(TAG, "[IN ] UploadFunction()");
		LogUtil.d(TAG, "[OUT] UploadFunction()");
	}


	/**
	 * アップロードを行います。<br>
	 *
	 * @param httpClient HTTPクライアント
	 * @param activity アクティビティ
	 * @param userInfoDto ユーザ情報
	 */
	public void upload(DefaultHttpClient httpClient, IUploadActivity activity, ApplicationListInfo applicationListInfo) {
		LogUtil.d(TAG, "[IN ] upload()");

		// リクエストURL
		String requestUrl = EnvUtil.getUploadUrl();
		LogUtil.d(TAG, "requestUrl:" + requestUrl);

		// アップロードを行う
		IAsyncCallback asyncCallback = new UploadCallback(activity);
		// 非同期POSTリクエストオブジェクト
		AsyncHttpPostTask task = new AsyncHttpPostTask(httpClient, asyncCallback);
		task.addStrParam(EnumUploadParams.APP_NAME.getParamName(), applicationListInfo.getApplicationName());
		task.addStrParam(EnumUploadParams.APP_DESCRIPTION.getParamName(), applicationListInfo.getDescription());
		task.addStrParam(EnumUploadParams.VERSION.getParamName(), applicationListInfo.getVersion());
		task.addStrParam(EnumUploadParams.PUBLISHED_LEVEL.getParamName(), applicationListInfo.getPublishedLevel());

		task.addFileParam(EnumUploadParams.ICON_FILE.getParamName(), new File(applicationListInfo.getIconPath()));
		task.addFileParam(EnumUploadParams.ZIP_FILE.getParamName(), new File(applicationListInfo.getZipPath()));

		// 非同期POSTリクエスト実行
		task.execute(requestUrl);

		LogUtil.d(TAG, "[OUT] upload()");
	}
}
