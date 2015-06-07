/*
 *  コランダムアプリ管理システム
 *
 * 【Copyright】
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.constant;

/**
 * Json結果Enumクラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public enum EnumReturnCode {

	/** 成功 **/
	SUCCESS			("0"),
	/** 認証失敗 **/
	ERR_AUTH		("1"),
	/** 未ログイン **/
	ERR_NOT_LOGIN	("2"),
	/** アップロード失敗 **/
	ERR_UPLOAD		("3"),
	/** 入力エラー **/
	ERR_VALIDATION	("4"),
	/** アプリケーションエラー **/
	ERR_APPLICATION	("5"),
	/** システムエラー **/
	ERR_SYSTEM		("6");


	/** 終了コード **/
	private String returnCode;

	/**
	 * コンストラクタ
	 * @param returnCode 終了コード
	 */
	EnumReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * 終了コードを取得する。<br>
	 *
	 * @return 終了コード
	 */
	public String getReturnCode() {
		return returnCode;
	}
}
