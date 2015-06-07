/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.dto;

import org.apache.http.HttpResponse;

import com.corundumPro.common.constant.Const;

/**
 * HTTPレスポンス結果DTOクラス.
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class HttpResponseResultDto {
	/** レスポンスキャラクタセット(デフォルト:UTF8) */
	private String responseCharset = Const.CHARSET_UTF8;
	/** HTTPレスポンスオブジェクト */
	private HttpResponse response = null;
	/** 例外オブジェクト */
	private Throwable exception = null;

	////////// 以下アクセッサー //////////
	/**
	 * レスポンスキャラクタセットを取得します。<br>
	 *
	 * @return キャラクタセット
	 */
	public String getResponseCharset() {
		return responseCharset;
	}

	/**
	 * レスポンスキャラクタセットを設定します。<br>
	 *
	 * @param responseCharset キャラクタセット
	 */
	public void setResponseCharset(String responseCharset) {
		this.responseCharset = responseCharset;
	}

	/**
	 * HTTPレスポンスオブジェクトを取得します。<br>
	 *
	 * @return HTTPレスポンスオブジェクト
	 */
	public HttpResponse getResponse() {
		return response;
	}

	/**
	 * HTTPレスポンスオブジェクトを設定します。<br>
	 *
	 * @param response HTTPレスポンスオブジェクト
	 */
	public void setResponse(HttpResponse response) {
		this.response = response;
	}

	/**
	 * 例外オブジェクトを取得します。<br>
	 *
	 * @return 例外オブジェクト
	 */
	public Throwable getException() {
		return exception;
	}

	/**
	 * 例外オブジェクトを設定します。<br>
	 *
	 * @param exception 例外オブジェクト
	 */
	public void setException(Throwable exception) {
		this.exception = exception;
	}
}
