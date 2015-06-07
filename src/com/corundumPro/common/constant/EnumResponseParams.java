/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.constant;


/**
 * サーバレスポンスパラメータEnumクラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public enum EnumResponseParams {
	// パラメータ名
	/** 終了コード */
	RETURN_CODE	("returnCode"),
	/** メッセージ */
	MESSAGE		("message"),
	/** 原因 */
	CAUSE		("cause");

	/** パラメータ名 */
	private String paramName;

	/**
	 * コンストラクタ
	 * @param paramName パラメータ名
	 */
	EnumResponseParams(String paramName) {
		this.paramName = paramName;
	}

	/**
	 * パラメータ名を取得する。<br>
	 *
	 * @return パラメータ名
	 */
	public String getParamName() {
		return paramName;
	}
}
