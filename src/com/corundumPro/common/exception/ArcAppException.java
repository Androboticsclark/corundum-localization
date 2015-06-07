/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.exception;

/**
 * アプリケーション例外クラス.<br>
 * アプリケーション内で起こりうる例外に当該クラスを用いる。<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class ArcAppException extends ArcBaseException {

	/** シリアルID */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 */
	public ArcAppException(Integer resourceId) {
		super(resourceId);
	}

	/**
	 * コンストラクタ。
	 * @param cause 例外発生要因
	 */
	public ArcAppException(Throwable cause) {
		super(cause);
	}

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 * @param exitCode 終了コード
	 */
	public ArcAppException(Integer resourceId, Integer exitCode) {
		super(resourceId, exitCode);
	}

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 * @param cause 例外要因
	 */
	public ArcAppException(Integer resourceId, Throwable cause) {
		super(resourceId, cause);
	}

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 * @param exitCode 終了コード
	 * @param cause 例外発生要因
	 */
	public ArcAppException(Integer resourceId, Integer exitCode, Throwable cause) {
		super(resourceId, exitCode, cause);
	}
}
