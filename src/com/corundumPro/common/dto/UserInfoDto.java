/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.dto;

import com.corundumPro.common.util.StringUtil;

/**
 * ユーザ情報DTOクラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class UserInfoDto {

	/** ユーザID */
	private String userId = "";
	/** パスワード */
	private String password = "";

	/**
	 * ユーザIDを取得します。<br>
	 *
	 * @return ユーザID
	 */
	public String getUserId() {
		return StringUtil.nvl(userId, "");
	}

	/**
	 * ユーザIDを設定します。<br>
	 *
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * パスワードを取得します。<br>
	 *
	 * @return パスワード
	 */
	public String getPassword() {
		return StringUtil.nvl(password, "");
	}

	/**
	 * パスワードを設定します。<br>
	 *
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("userId:").append(userId);

		return sb.toString();
	}
}
