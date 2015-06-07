/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.dto;

import com.corundumPro.common.util.StringUtil;

/**
 * セレクター情報DTOクラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class SelectorDto {

	/** 表示する文字列 */
	private String label = "";
	/** バリュー */
	private String value = "";

	/**
	 * 表示する文字列を取得します。<br>
	 *
	 * @return 表示する文字列
	 */
	public String getLabel() {
		return StringUtil.nvl(label, "");
	}

	/**
	 * 表示する文字列を設定します。<br>
	 *
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * バリューを取得します。<br>
	 *
	 * @return バリュー
	 */
	public String getValue() {
		return StringUtil.nvl(value, "");
	}

	/**
	 * バリューを設定します。<br>
	 *
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("label:").append(label).append(",");
		sb.append("value:").append(value);

		return sb.toString();
	}
}
