/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.exception;

import com.corundumPro.R;

/**
 * ランタイム例外クラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class ArcRuntimeException extends RuntimeException {

	/** シリアルID */
	private static final long serialVersionUID = 1L;

	/** 異常終了コード */
	private static final int ERROR_CODE	= 1;

	/** リソースID */
	private final Integer resourceId;

	/** 終了コード */
	private final int exitCode;

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 */
	public ArcRuntimeException(Integer resourceId) {
		this(resourceId, ERROR_CODE, null);
	}

	/**
	 * コンストラクタ。
	 * @param cause 例外発生要因
	 */
	public ArcRuntimeException(Throwable cause) {
		this(R.string.err_exception, ERROR_CODE, cause);
	}

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 * @param exitCode 終了コード
	 */
	public ArcRuntimeException(Integer resourceId, int exitCode) {
		this(resourceId, exitCode, null);
	}

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 * @param cause 例外発生要因
	 */
	public ArcRuntimeException(Integer resourceId, Throwable cause) {
		this(resourceId, ERROR_CODE, cause);
	}

	/**
	 * コンストラクタ。
	 * @param resourceId リソースID
	 * @param exitCode 終了コード
	 * @param cause 例外発生要因
	 */
	public ArcRuntimeException(Integer resourceId, int exitCode, Throwable cause) {
		super(cause);
		this.resourceId = resourceId;
		this.exitCode = exitCode;
	}

	/**
	 * リソースIDを取得する。
	 * @return リソースID
	 */
	public Integer getResourceId() {
		if (this.resourceId == null) {
			return R.string.err_exception;
		}
		return this.resourceId;
	}

	/**
	 * 終了コードを取得する。
	 * @return 終了コード
	 */
	public int getExitCode() {
		return this.exitCode;
	}
}
