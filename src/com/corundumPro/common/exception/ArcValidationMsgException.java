/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.exception;

/**
 * 検証例外クラス.<br>
 * 入力検証時の例外に当該クラスを用いる。<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class ArcValidationMsgException extends ArcBaseMsgException {

	/** シリアルID */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 */
	public ArcValidationMsgException(Integer resourceId) {
		super(resourceId);
	}

	/**
	 * コンストラクタ。
	 * @param cause 例外発生要因
	 */
	public ArcValidationMsgException(Throwable cause) {
		super(cause);
	}

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 * @param exitCode 終了コード
	 */
	public ArcValidationMsgException(Integer resourceId, Integer exitCode) {
		super(resourceId, exitCode);
	}

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 * @param cause 例外要因
	 */
	public ArcValidationMsgException(Integer resourceId, Throwable cause) {
		super(resourceId, cause);
	}

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 * @param exitCode 終了コード
	 * @param cause 例外発生要因
	 */
	public ArcValidationMsgException(Integer resourceId, Integer exitCode, Throwable cause) {
		super(resourceId, exitCode, cause);
	}
}
