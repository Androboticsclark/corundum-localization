/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.exception;

/**
 * システム例外クラス.<br>
 * 致命的なシステム障害時の例外制御を行う際に当該クラスを用いる。<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class ArcSysMsgException extends ArcBaseMsgException {

	/** シリアルID */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 */
	public ArcSysMsgException(Integer resourceId) {
		super(resourceId);
	}

	/**
	 * コンストラクタ。
	 * @param cause 例外発生要因
	 */
	public ArcSysMsgException(Throwable cause) {
		super(cause);
	}

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 * @param exitCode 終了コード
	 */
	public ArcSysMsgException(Integer resourceId, Integer exitCode) {
		super(resourceId, exitCode);
	}

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 * @param cause 例外要因
	 */
	public ArcSysMsgException(Integer resourceId, Throwable cause) {
		super(resourceId, cause);
	}

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 * @param exitCode 終了コード
	 * @param cause 例外発生要因
	 */
	public ArcSysMsgException(Integer resourceId, Integer exitCode, Throwable cause) {
		super(resourceId, exitCode, cause);
	}
}
