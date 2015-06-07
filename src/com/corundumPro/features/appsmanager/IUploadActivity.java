/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.features.appsmanager;

import com.corundumPro.common.activity.IAsyncActivity;
import com.corundumPro.common.dto.JsonResultDto;

/**
 * アップロードアクティビティインターフェース.<br>
 * アップロードを行うアクティビティでimplementします。<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public interface IUploadActivity extends IAsyncActivity {

	/**
	 * アップロード前に実行する処理を定義します。
	 *
	 */
	public void onUploadPreparation();

	/**
	 * アップロード成功時に実行する処理を定義します。
	 *
	 * @param JsonResultDto
	 *
	 */
	public void onUploadSuccess(JsonResultDto json);

	/**
	 * アップロード失敗時に実行する処理を定義します。
	 *
	 * @param JSONObject
	 *
	 */
	public void onUploadFailure(JsonResultDto json);

	/**
	 * 例外発生時に実行する処理を定義します。
	 *
	 * @param Throwable
	 *
	 */
	public void onUploadException(Throwable e);

}
