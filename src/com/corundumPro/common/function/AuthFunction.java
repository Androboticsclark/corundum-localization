/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.function;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.corundumPro.common.activity.AbstractAuthActivity;
import com.corundumPro.common.constant.EnumLoginParams;
import com.corundumPro.common.dto.UserInfoDto;
import com.corundumPro.common.task.AsyncHttpPostTask;
import com.corundumPro.common.util.EnvUtil;
import com.corundumPro.common.util.LogUtil;

/**
 * 認証ファンクション.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class AuthFunction {
	static final String TAG = "AuthFunction";

	/**
	 * コンストラクタ<br>
	 *
	 */
	public AuthFunction() {
		LogUtil.d(TAG, "[IN ] AuthFunction()");
		LogUtil.d(TAG, "[OUT] AuthFunction()");
	}

	/**
	 * ユーザ情報が存在するか判断します。。<br>
	 *
	 * @param userInfoDto ユーザ情報
	 * @return 存在する:true, 存在しない:false
	 */
	public boolean hastUserInfo(UserInfoDto userInfoDto) {
		LogUtil.d(TAG, "[IN ] hastUserInfo()");

		if (userInfoDto == null) {
			LogUtil.d(TAG, "userInfoDto:null");
			return false;
		}

		if (StringUtils.isEmpty(userInfoDto.getUserId())) {
			LogUtil.d(TAG, "userInfoDto.getUserId():empty");
			return false;
		}

		if (StringUtils.isEmpty(userInfoDto.getPassword())) {
			LogUtil.d(TAG, "userInfoDto.getPassword():empty");
			return false;
		}

		LogUtil.d(TAG, "[OUT] hastUserInfo()");

		return true;
	}

	/**
	 * プリファレンスからユーザ情報を取得します。<br>
	 *
	 * @param preference プリファレンス
	 * @param userInfoDto ユーザ情報
	 */
	public void loadUserInfo(SharedPreferences preference, UserInfoDto userInfoDto) {
		userInfoDto.setUserId(preference.getString(EnumLoginParams.USER_ID.getPreferenceKey(), ""));
		userInfoDto.setPassword(preference.getString(EnumLoginParams.PASSWORD.getPreferenceKey(), ""));
	}

	/**
	 * ユーザ情報をプリファレンスに保存します。<br>
	 *
	 * @param editor エディター
	 * @param userInfoDto ユーザ情報
	 */
	public void saveUserInfo(Editor editor, UserInfoDto userInfoDto) {

		editor.putString(EnumLoginParams.USER_ID.getPreferenceKey(), userInfoDto.getUserId());
		editor.putString(EnumLoginParams.PASSWORD.getPreferenceKey(), userInfoDto.getPassword());

		editor.commit();
	}

	/**
	 * ユーザ情報をプリファレンスから削除します。<br>
	 *
	 * @param editor エディター
	 * @param userInfoDto ユーザ情報
	 */
	public void deleteUserInfo(Editor editor, UserInfoDto userInfoDto) {

		editor.remove(EnumLoginParams.USER_ID.getPreferenceKey());
		editor.remove(EnumLoginParams.PASSWORD.getPreferenceKey());

		editor.commit();
	}

	/**
	 * Corundum Apps Manager認証を行います。<br>
	 *
	 * @param HTTPクライアント
	 * @param activity アクティビティ
	 * @param userInfoDto ユーザ情報
	 */
	public void authenticate(DefaultHttpClient httpClient, AbstractAuthActivity activity, UserInfoDto userInfoDto) {
		LogUtil.d(TAG, "[IN ] authenticate()");

		String requestUrl = EnvUtil.getLoginUrl();
		LogUtil.d(TAG, "requestUrl:" + requestUrl);

		// サーバ認証を行う
		IAsyncCallback asyncCallback = new AuthCallback(activity);
		// 非同期POSTリクエストオブジェクト
		AsyncHttpPostTask task = new AsyncHttpPostTask(httpClient, asyncCallback);
		task.addStrParam(EnumLoginParams.USER_ID.getParamName(), userInfoDto.getUserId());
		task.addStrParam(EnumLoginParams.PASSWORD.getParamName(), userInfoDto.getPassword());

		// 非同期POSTリクエスト実行
		task.execute(requestUrl);

		LogUtil.d(TAG, "[OUT] authenticate()");
	}
}
