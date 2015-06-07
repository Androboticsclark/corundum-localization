/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.dto;

import android.view.View;

import com.corundumPro.common.dao.DBAdapter;

/**
 * ホーム画面設定情報DTOクラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class HomeSettingDto {

	/** ホームモード */
	private int homeMode = DBAdapter.HOME_MODE_LIST;
	/** ラジオボタンID */
	private int radioId;
	/** ホームURL */
	private String homeUrl = "";
	/** URL編集テキストボックス可否 */
	private boolean isEnableEditUrl = false;
	/** プレビュー表示可否 */
	private int visiblePreview = View.GONE;
	/** プロトコル */
	private String protocol = "";

	/**
	 * ホームモードを取得します。<br>
	 *
	 * @return homeMode
	 */
	public int getHomeMode() {
		return homeMode;
	}

	/**
	 * ホームモードを設定します。<br>
	 *
	 * @param homeMode
	 */
	public void setHomeMode(int homeMode) {
		this.homeMode = homeMode;
	}

	/**
	 * ラジオボタンIDを取得します。<br>
	 *
	 * @return radioId
	 */
	public int getRadioId() {
		return radioId;
	}

	/**
	 * ラジオボタンIDを設定します。<br>
	 *
	 * @param radioId
	 */
	public void setRadioId(int radioId) {
		this.radioId = radioId;
	}

	/**
	 * ホームURLを取得します。<br>
	 *
	 * @return homeUrl
	 */
	public String getHomeUrl() {
		return homeUrl;
	}

	/**
	 * ホームURLを設定します。<br>
	 *
	 * @param homeUrl
	 */
	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}

	/**
	 * URL編集テキストボックス可否を取得します。<br>
	 *
	 * @return isEnableEditUrl 可：true, 不可：false
	 */
	public boolean isEnableEditUrl() {
		return isEnableEditUrl;
	}

	/**
	 * URL編集テキストボックス可否を設定します。<br>
	 *
	 * @param isEnableEditUrl 可：true, 不可：false
	 */
	public void setEnableEditUrl(boolean isEnableEditUrl) {
		this.isEnableEditUrl = isEnableEditUrl;
	}

	/**
	 * プレビュー表示可否を取得します。<br>
	 *
	 * @return visiblePreview 可：View.VISIBLE, 不可：View.GONE
	 */
	public int getVisiblePreview() {
		return visiblePreview;
	}

	/**
	 * プレビュー表示可否を設定します。<br>
	 *
	 * @param visiblePreview 可：View.VISIBLE, 不可：View.GONE
	 */
	public void setVisiblePreview(int visiblePreview) {
		this.visiblePreview = visiblePreview;
	}

	/**
	 * プロトコルを取得します。<br>
	 *
	 * @return protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * プロトコルを設定します。<br>
	 *
	 * @param protocol
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("homeMode:").append(homeMode).append(",");
		sb.append("radioId:").append(radioId).append(",");
		sb.append("homeUrl:").append(homeUrl).append(",");
		sb.append("isEnableEditUrl:").append(isEnableEditUrl).append(",");
		sb.append("visiblePreview:").append(visiblePreview).append(",");
		sb.append("protocol:").append(protocol);

		return sb.toString();
	}
}
