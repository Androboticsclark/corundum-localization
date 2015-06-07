/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.features.appsmanager;


/**
 * アプリケーション情報Enumクラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public enum EnumAppInfoFile {
	// ファイル名
	/** アプリ名 */
	APP_NAME		("title"),
	/** アプリ説明 */
	APP_DESCRIPTION	("description"),
	/** 公開レベル */
	PUBLISHED_LEVEL	("published"),
	/** バージョン */
	VERSION			("version");


	/** ファイル名 */
	private String fileName;

	/**
	 * コンストラクタ
	 * @param fileName ファイル名
	 */
	EnumAppInfoFile(String fileName) {
		this.fileName = fileName;

	}

	/**
	 * ファイル名を取得する。<br>
	 *
	 * @return ファイル名
	 */
	public String getFileName() {
		return fileName;
	}
}
