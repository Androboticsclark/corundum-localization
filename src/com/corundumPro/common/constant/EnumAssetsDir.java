/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.constant;


/**
 * AssetsディレクトリEnumクラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public enum EnumAssetsDir {
	WWW		("www");

	/** ディレクトリ名 **/
	private String dirName;

	/**
	 * コンストラクタ
	 * @param dirName ディレクトリ名
	 */
	EnumAssetsDir(String dirName) {
		this.dirName = dirName;
	}

	/**
	 * ディレクトリ名を取得する。<br>
	 *
	 * @return ディレクトリ名
	 */
	public String getDirName() {
		return dirName;
	}
}
