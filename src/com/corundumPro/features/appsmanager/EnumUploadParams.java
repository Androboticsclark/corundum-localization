/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.features.appsmanager;


/**
 * アップロードパラメータEnumクラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public enum EnumUploadParams {
	// パラメータ名, preferenceキー
	/** アプリ名 */
	APP_NAME		("appName"),
	/** アプリ説明 */
	APP_DESCRIPTION	("appDescription"),
	/** 公開レベル */
	PUBLISHED_LEVEL	("publishedLevel"),
	/** バージョン */
	VERSION			("version"),
	/** アイコンファイル */
	ICON_FILE		("iconFile"),
	/** zipファイル */
	ZIP_FILE		("zipFile");


	/** パラメータ名 */
	private String paramName;

	/**
	 * コンストラクタ
	 * @param paramName パラメータ名
	 */
	EnumUploadParams(String paramName) {
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
