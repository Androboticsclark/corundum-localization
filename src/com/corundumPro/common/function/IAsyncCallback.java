/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.function;

/**
 * 非同期タスクコールバックインターフェース.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public interface IAsyncCallback {

	/**
	 * 非同期処理前に実行する処理を定義します。
	 */
	public void onPreExecute();

	/**
	 * 非同期処理完了時に実行する処理を定義します。
	 *
	 * @param result AsyncTask#doInBackgroundの結果
	 */
	public void onPostExecute(Object result);

	/**
	 * 非同期処理中に実行する処理を定義します。
	 *
	 * @param progress
	 */
	public void onProgressUpdate(int progress);

	/**
	 * キャンセル時に実行する処理を定義します。
	 */
	public void onCancelled();
}
