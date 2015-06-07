/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.constant;

/**
 * アクティビティEnumクラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public enum EnumActionSheet {
	/** ActionSheetActivity(オプションメニュー) **/
	ACTION_SHEET_ACTIVITY("ActionSheetActivity", false);

	/** アクティビティ名 **/
	private String activityName;
	/** アクティビティ有効／無効フラグ **/
	private boolean isEnable;

	/**
	 * コンストラクタ
	 * @param paramName アクティビティ名
	 * @param isEnable アクティビティ有効／無効フラグ
	 */
	EnumActionSheet(String activityName, boolean isEnable) {
		this.activityName = activityName;
		this.isEnable = isEnable;
	}

	/**
	 * アクティビティ名を取得する。<br>
	 *
	 * @return アクティビティ名
	 */
	public String getActivityName() {
		return activityName;
	}

	/**
	 * アクティビティ有効／無効フラグを取得する。<br>
	 *
	 * @return アクティビティ有効／無効フラグ
	 */
	public boolean isEnable() {
		return isEnable;
	}
}
