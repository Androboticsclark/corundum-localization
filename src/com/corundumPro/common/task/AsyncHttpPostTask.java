/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.task;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.corundumPro.R;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.dto.HttpResponseResultDto;
import com.corundumPro.common.exception.ArcAppException;
import com.corundumPro.common.exception.ArcAppMsgException;
import com.corundumPro.common.exception.ArcBaseException;
import com.corundumPro.common.function.IAsyncCallback;
import com.corundumPro.common.util.EnvUtil;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.common.util.StringUtil;


/**
 * POSTリクエストクラス.<br>
 *
 * HTTP通信でPOSTリクエストを投げる処理を非同期で行う。
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class AsyncHttpPostTask extends AsyncTask<String, Integer, HttpResponseResultDto> {
	static final String TAG = "AsyncHttpPostTask";

	/** HTTPクライアント */
	private DefaultHttpClient httpClient = null;
	/** コールバックオブジェクト */
	private IAsyncCallback asyncCallback = null;
	/** リクエストパラメータ */
	private List<NameValuePair> strParamList = null;
	/** リクエストパラメータ */
	private Map<String, File> fileParamMap = null;
	/** リクエストキャラクタセット(デフォルト:UTF-8) */
	private String requestCharset = Const.CHARSET_UTF8;
	/** レスポンスキャラクタセット(デフォルト:UTF-8) */
	private String responseCharset = Const.CHARSET_UTF8;
	/** HTTP通信タイムアウト */
	private int connectionTimeout = EnvUtil.getConnectionTimeout();
	/** ソケット通信タイムアウト */
	private int socketTimeout = EnvUtil.getSocketTimeout();
	/** UserAgent */
	private String userAgent = EnvUtil.getUserAgent();

	/**
	 * コンストラクタ<br>
	 *
	 * @param httpClient HTTPクライアント
	 * @param asyncCallback コールバックオブジェクト
	 */
	public AsyncHttpPostTask(DefaultHttpClient httpClient, IAsyncCallback asyncCallback) {
		LogUtil.d(TAG, "[IN ] AsyncHttpPostTask()");

		this.httpClient = httpClient;
		this.asyncCallback = asyncCallback;
		this.strParamList = new ArrayList<NameValuePair>();
		this.fileParamMap = new HashMap<String, File>();

		LogUtil.d(TAG, "[OUT] AsyncHttpPostTask()");
	}

	/**
	 * 非同期処理前に実行する処理を行います。
	 *
	 */
	@Override
	protected void onPreExecute() {
		LogUtil.d(TAG, "[IN ] onPreExecute()");

		super.onPreExecute();
		asyncCallback.onPreExecute();

		LogUtil.d(TAG, "[OUT] onPreExecute()");
	}

	/**
	 * 非同期で実行する処理を行います。<br>
	 *
	 * @param args リクエストURL
	 */
	@Override
	protected HttpResponseResultDto doInBackground(String... args) {
		LogUtil.d(TAG, "[IN ] doInBackground()");
		LogUtil.d(TAG, "args:" + TextUtils.join(",", args));

		// HTTPレスポンス結果DTO
		HttpResponseResultDto resultDto = new HttpResponseResultDto();
		URI url = null;

		try {
			// URL設定
			try {
				url = new URI(args[0]);

			} catch (URISyntaxException e) {
				throw new ArcAppException(R.string.err_invalid_url, e);
		    }

			// POSTパラメータ付きでPOSTリクエストを構築
			HttpPost request = new HttpPost(url);
			try {
				// 送信パラメータのエンコードを指定
				Charset charset = Charset.forName(StringUtil.nvl(requestCharset, Const.CHARSET_UTF8));
				MultipartEntity multipartEntity =
				        new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, charset);

				// 文字列パラメータ設定
				for (NameValuePair pair : strParamList) {
					multipartEntity.addPart(pair.getName(), new StringBody(pair.getValue(), charset));
					LogUtil.d(TAG, pair.getName() + ":" + pair.getValue());
				}

				// ファイルパラメータ設定
				for(Map.Entry<String, File> entry : fileParamMap.entrySet()) {
					multipartEntity.addPart(entry.getKey(), new FileBody(entry.getValue()));
					LogUtil.d(TAG, entry.getKey() + ":" + entry.getValue().getAbsolutePath());
				}
				// 全パラメータを設定
				request.setEntity(multipartEntity);

			} catch (UnsupportedEncodingException e) {
				throw new ArcAppException(R.string.err_invalid_charset, e);
		    }

			// HTTPクライアント作成
			if (this.httpClient == null) {
				this.httpClient = new DefaultHttpClient();
			}
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, connectionTimeout * 1000);
			HttpConnectionParams.setSoTimeout(httpParams, socketTimeout * 1000);
			HttpProtocolParams.setContentCharset(httpParams, StringUtil.nvl(responseCharset, Const.CHARSET_UTF8));
			HttpProtocolParams.setUserAgent(httpParams, userAgent);
			this.httpClient.setParams(httpParams);

			try {
				// POSTリクエストを実行
				resultDto.setResponse(this.httpClient.execute(request));
				resultDto.setResponseCharset(StringUtil.nvl(responseCharset, Const.CHARSET_UTF8));

			} catch (ClientProtocolException e) {
				throw new ArcAppMsgException(R.string.err_connection, e);
		    } catch (ConnectTimeoutException e) {
				throw new ArcAppMsgException(R.string.err_connection, e);
		    } catch (IOException e) {
				throw new ArcAppMsgException(R.string.err_io, e);
		    }

		} catch(ArcBaseException e) {
			resultDto.setException(e);

		} finally {
			LogUtil.d(TAG, "[OUT] doInBackground()");
		}

		return resultDto;
	}

	/**
	 * 非同期処理中に実行する処理を行います。<br>
	 *
	 * @param values
	 */
	@Override
	protected void onProgressUpdate(Integer... progress) {
		LogUtil.d(TAG, "[IN ] onProgressUpdate()");
		LogUtil.d(TAG, "args:" + TextUtils.join(",", progress));

		super.onProgressUpdate(progress);
		asyncCallback.onProgressUpdate(progress[0]);

		LogUtil.d(TAG, "[OUT] onProgressUpdate()");
	}

	/**
	 * 非同期処理完了時に実行する処理を行います。<br>
	 *
	 * @param result HTTPレスポンス結果DTO
	 */
	@Override
	protected void onPostExecute(HttpResponseResultDto result) {
		LogUtil.d(TAG, "[IN ] onPostExecute()");

		super.onPostExecute(result);
		asyncCallback.onPostExecute(result);

		LogUtil.d(TAG, "[OUT] onPostExecute()");
	}

	/**
	 * キャンセル時に実行する処理を行います。<br>
	 *
	 */
	@Override
	protected void onCancelled() {
		LogUtil.d(TAG, "[IN ] onCancelled()");

		super.onCancelled();
		asyncCallback.onCancelled();

		LogUtil.d(TAG, "[OUT] onCancelled()");
	}

	////////// 以下アクセッサー //////////
	/**
	 * パラメータを追加します。
	 *
	 * @param paramName パラメータ名
	 * @param paramValue パラメータ値
	 */
	public void addStrParam(String paramName, String paramValue) {
		strParamList.add(new BasicNameValuePair(paramName, paramValue));
	}

	/**
	 * パラメータを追加します。
	 *
	 * @param paramName パラメータ名
	 * @param paramValue パラメータ値
	 */
	public void addFileParam(String paramName, File paramValue) {
		fileParamMap.put(paramName, paramValue);
	}

	/**
	 * リクエストキャラクタセットを設定します。<br>
	 * デフォルト：UTF-8
	 *
	 * @param requestCharset キャラクタセット
	 */
	public void setRequestCharset(String requestCharset) {
		this.requestCharset = requestCharset;
	}

	/**
	 * レスポンスキャラクタセットを設定します。<br>
	 * デフォルト：UTF-8
	 *
	 * @param responseCharset キャラクタセット
	 */
	public void setResponseCharset(String responseCharset) {
		this.responseCharset = responseCharset;
	}

	/**
	 * HTTP通信タイムアウトを設定します。<br>
	 * デフォルト：{@link Const#HTTP_CONNECTION_TIMEOUT}
	 *
	 * @param connectionTimeout タイムアウト値(秒)
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * ソケット通信タイムアウトを設定します。<br>
	 * デフォルト：{@link Const#SOCKET_TIMEOUT}
	 *
	 * @param socketTimeout タイムアウト値(秒)
	 */
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
}
