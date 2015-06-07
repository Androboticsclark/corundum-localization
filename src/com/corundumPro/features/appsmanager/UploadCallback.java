/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.features.appsmanager;

import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.corundumPro.R;
import com.corundumPro.common.constant.EnumReturnCode;
import com.corundumPro.common.dto.HttpResponseResultDto;
import com.corundumPro.common.dto.JsonResultDto;
import com.corundumPro.common.exception.ArcAppMsgException;
import com.corundumPro.common.exception.ArcBaseException;
import com.corundumPro.common.exception.ArcBaseMsgException;
import com.corundumPro.common.exception.ArcRuntimeException;
import com.corundumPro.common.function.IAsyncCallback;
import com.corundumPro.common.util.LogUtil;


/**
 * 非同期アップロードコールバッククラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class UploadCallback implements IAsyncCallback {
	static final String TAG = "UploadCallback";

	/** 呼び出し元アクティビティ */
	private IUploadActivity activity = null;

	/**
	 * コンストラクタ<br>
	 *
	 * @param activity アクティビティ
	 */
	public UploadCallback(IUploadActivity activity) {
		LogUtil.d(TAG, "[IN ] UploadCallback()");

		this.activity = activity;

		LogUtil.d(TAG, "[OUT] UploadCallback()");
	}

	@Override
	public void onPreExecute() {
		LogUtil.d(TAG, "[IN ] onPreExecute()");

		// アップロード前の処理を実行
		activity.onUploadPreparation();

		LogUtil.d(TAG, "[OUT] onPreExecute()");
	}

	@Override
	public void onPostExecute(Object result) {
		LogUtil.d(TAG, "[IN ] onPostExecute()");

		// HTTPレスポンス結果DTO取得
		HttpResponseResultDto resultDto = (HttpResponseResultDto) result;

		try {
			// 返却値からJSONオブジェクトを取得
			JSONObject json = getResultFromServer(resultDto);
			JsonResultDto jsonResultDto = new JsonResultDto();
			jsonResultDto.bindJson(json);

			// 結果取得
			String returnCode = jsonResultDto.getReturnCode();

			LogUtil.d(TAG, "returnCode:" + returnCode);

			if (EnumReturnCode.SUCCESS.getReturnCode().equals(returnCode)) {
				// アップロード成功時の処理
				activity.onUploadSuccess(jsonResultDto);

			} else {
				// アップロード失敗時の処理
				activity.onUploadFailure(jsonResultDto);
			}

		} catch (ArcBaseMsgException e) {
			LogUtil.e(TAG, activity.getResources().getString(e.getResourceId()));
			LogUtil.e(TAG, e.getMessage());

			// 例外発生時の処理
			activity.onUploadException(e);

		} catch (ArcBaseException e) {
			LogUtil.e(TAG, activity.getResources().getString(e.getResourceId()));
			LogUtil.e(TAG, e.getMessage());

			// OS側でプロセスを終了する(「問題が発生したため、corundumを終了します。」)
			throw new ArcRuntimeException(e.getResourceId(), e);

		} finally {
			LogUtil.d(TAG, "[OUT] onPostExecute()");

		}
	}

	@Override
	public void onProgressUpdate(int progress) {
		LogUtil.d(TAG, "[IN ] onProgressUpdate()");
		// 何もしない
		LogUtil.d(TAG, "[OUT] onProgressUpdate()");
	}

	@Override
	public void onCancelled() {
		LogUtil.d(TAG, "[IN ] onCancelled()");
		// 何もしない
		LogUtil.d(TAG, "[OUT] onCancelled()");
	}

	/**
	 * サーバ返却値からJSONオブジェクトを生成します。
	 *
	 * @param resultDto HTTPレスポンス結果DTO
	 * @return JSONオブジェクト
	 */
	protected JSONObject getResultFromServer(HttpResponseResultDto resultDto) throws ArcBaseException {
		LogUtil.d(TAG, "[IN ] getResultFromServer()");

		try {
			if (resultDto.getException() != null) {
				// 例外が発生していた場合、実行時例外をスローする
				throw resultDto.getException();
			}

			// ステータスコード取得
			int statusCode = resultDto.getResponse().getStatusLine().getStatusCode();
			LogUtil.d(TAG, "statusCode:" + statusCode);

			if (statusCode == HttpStatus.SC_OK) {
				// ステータスコード200(正常)の場合
				// Live++からの返却値を取得
				String entity = EntityUtils.toString(resultDto.getResponse().getEntity(), resultDto.getResponseCharset());
				LogUtil.d(TAG, "entity:" + entity);

				return new JSONObject(entity);

			} else {
				throw new ArcAppMsgException(R.string.err_connection);
			}

		} catch (ArcBaseException e) {
			throw e;
		} catch (Throwable e) {
			throw new ArcAppMsgException(R.string.err_server, e);
		} finally {
			LogUtil.d(TAG, "[OUT] getResultFromServer()");
		}
	}
}
