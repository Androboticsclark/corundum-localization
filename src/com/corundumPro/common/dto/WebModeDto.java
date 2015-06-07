/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.dto;

import com.corundumPro.common.dao.DBAdapter;

/**
 * Webモード情報DTOクラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class WebModeDto {

	/** Webモード */
	private int webMode = DBAdapter.WEB_MODE_OPERATION;
	/** URLバー表示可否 */
	private boolean isVisibleUrlBar = false;
	/** ツールバー表示可否 */
	private boolean isVisibleToolBar = true;
	/** ステータスバー表示可否 */
	private boolean isVisibleStatusBar = true;

	/**
	 * Webモードを取得します。<br>
	 *
	 * @return webMode
	 */
	public int getWebMode() {
		return webMode;
	}

	/**
	 * Webモードを設定します。<br>
	 *
	 * @param webMode
	 */
	public void setWebMode(int webMode) {
		this.webMode = webMode;
	}

	/**
	 * URLバー表示可否を取得します。<br>
	 *
	 * @return 可：true, 不可：false
	 */
	public boolean isVisibleUrlBar() {
		return isVisibleUrlBar;
	}

	/**
	 * URLバー表示可否を設定します。<br>
	 *
	 * @param isVisibleUrlBar 可：true, 不可：false
	 */
	public void setVisibleUrlBar(boolean isVisibleUrlBar) {
		this.isVisibleUrlBar = isVisibleUrlBar;
	}

	/**
	 * ツールバー表示可否を取得します。<br>
	 *
	 * @return 可：true, 不可：false
	 */
	public boolean isVisibleToolBar() {
		return isVisibleToolBar;
	}

	/**
	 * ツールバー表示可否を設定します。<br>
	 *
	 * @param isVisibleToolBar 可：true, 不可：false
	 */
	public void setVisibleToolBar(boolean isVisibleToolBar) {
		this.isVisibleToolBar = isVisibleToolBar;
	}

	/**
	 * ステータスバー表示可否を取得します。<br>
	 *
	 * @return 可：true, 不可：false
	 */
	public boolean isVisibleStatusBar() {
		return isVisibleStatusBar;
	}

	/**
	 * ステータスバー表示可否を設定します。<br>
	 *
	 * @param isVisibleStatusBar 可：true, 不可：false
	 */
	public void setVisibleStatusBar(boolean isVisibleStatusBar) {
		this.isVisibleStatusBar = isVisibleStatusBar;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("webMode:").append(webMode).append(",");
		sb.append("isVisibleUrlBar:").append(isVisibleUrlBar).append(",");
		sb.append("isVisibleToolBar:").append(isVisibleToolBar);

		return sb.toString();
	}
}
