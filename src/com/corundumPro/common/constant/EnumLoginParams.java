/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.constant;

import com.corundumPro.R;


/**
 * ユーザ情報パラメータEnumクラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public enum EnumLoginParams {
	// パラメータ名, preferenceキー, リソースID
	/** ユーザID */
	USER_ID		("userId"	,"login_user_id"	,R.id.login_user_id),
	/** パスワード */
	PASSWORD	("password"	,"login_password"	,R.id.login_password);

	/** パラメータ名 */
	private String paramName;
	/** preferenceキー */
	private String preferenceKey;
	/** リソースID */
	private int resId;

	/**
	 * コンストラクタ
	 * @param paramName パラメータ名
	 * @param preferenceKey preferenceキー
	 * @param resId リソースID
	 */
	EnumLoginParams(String paramName, String preferenceKey, int resId) {
		this.paramName = paramName;
		this.preferenceKey = preferenceKey;
		this.resId = resId;
	}

	/**
	 * パラメータ名を取得する。<br>
	 *
	 * @return パラメータ名
	 */
	public String getParamName() {
		return paramName;
	}

	/**
	 * preferenceキーを取得する。<br>
	 *
	 * @return preferenceキー
	 */
	public String getPreferenceKey() {
		return preferenceKey;
	}

	/**
	 * リソースIDを取得する。<br>
	 *
	 * @return リソースID
	 */
	public int getResId() {
		return resId;
	}
}
