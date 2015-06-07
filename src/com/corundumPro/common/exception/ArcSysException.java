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
public class ArcSysException extends ArcBaseException {

	/** シリアルID */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 */
	public ArcSysException(Integer resourceId) {
		super(resourceId);
	}

	/**
	 * コンストラクタ。
	 * @param cause 例外発生要因
	 */
	public ArcSysException(Throwable cause) {
		super(cause);
	}

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 * @param exitCode 終了コード
	 */
	public ArcSysException(Integer resourceId, Integer exitCode) {
		super(resourceId, exitCode);
	}

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 * @param cause 例外要因
	 */
	public ArcSysException(Integer resourceId, Throwable cause) {
		super(resourceId, cause);
	}

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 * @param exitCode 終了コード
	 * @param cause 例外発生要因
	 */
	public ArcSysException(Integer resourceId, Integer exitCode, Throwable cause) {
		super(resourceId, exitCode, cause);
	}
}
