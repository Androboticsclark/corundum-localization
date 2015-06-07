/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.constant;

import com.corundumPro.common.dto.FileListInfo;

/**
 * 拡張子Enumクラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public enum EnumExtension {
	// 拡張子, ファイル種別, HTMLフラグ
	EXT_JPG		("jpg"	,FileListInfo.FILE_TYPE_IMAGE		,false		),
	EXT_JPEG	("jpeg"	,FileListInfo.FILE_TYPE_IMAGE		,false		),
	EXT_PNG		("png"	,FileListInfo.FILE_TYPE_IMAGE		,false		),
	EXT_GIF		("gif"	,FileListInfo.FILE_TYPE_IMAGE		,false		),
	EXT_JS		("js"	,FileListInfo.FILE_TYPE_EDIT_ABLE	,false		),
	EXT_CSS		("css"	,FileListInfo.FILE_TYPE_EDIT_ABLE	,false		),
	EXT_HTML	("html"	,FileListInfo.FILE_TYPE_EDIT_ABLE	,true		),
	EXT_HTM		("htm"	,FileListInfo.FILE_TYPE_EDIT_ABLE	,true		),
	EXT_XML		("xml"	,FileListInfo.FILE_TYPE_EDIT_ABLE	,false		),
	EXT_TXT		("txt"	,FileListInfo.FILE_TYPE_EDIT_ABLE	,false		),
	EXT_ZIP		("zip"	,FileListInfo.FILE_TYPE_OTHER		,false		);

	/** 拡張子 **/
	private String extension;
	/** ファイルタイプ **/
	private int fileType;
	/** HTMLフラグ **/
	private boolean isHtml;

	/**
	 * コンストラクタ
	 * @param extension 拡張子
	 * @param fileType ファイルタイプ
	 * @param isHtml HTMLフラグ
	 */
	EnumExtension(String extension, int fileType, boolean isHtml) {
		this.extension = extension;
		this.fileType = fileType;
		this.isHtml = isHtml;
	}

	/**
	 * 拡張子を取得する。<br>
	 *
	 * @return 拡張子
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * ファイルタイプを取得する。<br>
	 *
	 * @return ファイルタイプ
	 */
	public int getFileType() {
		return fileType;
	}

	/**
	 * HTMLフラグを取得する。<br>
	 *
	 * @return HTMLフラグ
	 */
	public boolean isHtml() {
		return isHtml;
	}
}
