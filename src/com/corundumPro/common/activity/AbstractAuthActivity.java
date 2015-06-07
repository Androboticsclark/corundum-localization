/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.activity;

import org.apache.http.impl.client.DefaultHttpClient;

import android.content.DialogInterface;

import com.corundumPro.R;
import com.corundumPro.common.constant.EnumLoginParams;
import com.corundumPro.common.dialog.EditTextDialog;
import com.corundumPro.common.dto.JsonResultDto;
import com.corundumPro.common.dto.UserInfoDto;
import com.corundumPro.common.function.AuthFunction;
import com.corundumPro.common.util.LogUtil;

/**
 * 認証アクティビティ抽象クラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public abstract class AbstractAuthActivity extends BaseActivity implements IAsyncActivity {
	static final String TAG = "IAuthActivity";

	/** ユーザ情報DTO */
	protected UserInfoDto userInfoDto = new UserInfoDto();

	/** 認証ファンクション */
	protected AuthFunction authFunction = new AuthFunction();

	/**
	 * 認証前に実行する処理を定義します。
	 *
	 */
	public abstract void onAuthPreparation();

	/**
	 * 認証成功時に実行する処理を定義します。
	 *
	 * @param JsonResultDto
	 *
	 */
	public abstract void onAuthSuccess(JsonResultDto json);

	/**
	 * 認証失敗時に実行する処理を定義します。
	 *
	 * @param JSONObject
	 *
	 */
	public abstract void onAuthFailure(JsonResultDto json);

	/**
	 * 例外発生時に実行する処理を定義します。
	 *
	 * @param Throwable
	 *
	 */
	public abstract void onAuthException(Throwable e);

	/**
	 * ログイン処理を行います<br>
	 *
	 * @param HTTPクライアント
	 */
	protected void login(DefaultHttpClient httpClient) {
		LogUtil.d(TAG, "[IN ] registUserInfo()");

		// ログインダイアログ生成
		final EditTextDialog dialog = EditTextDialog.newInstance(R.string.app_name
															,R.string.login_message
															,R.drawable.ic_launcher
															,R.string.login_ok
															,R.string.login_cancel
															,R.layout.login_dialog);

		dialog.setOnOkClickListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface d, int which) {
				LogUtil.d(TAG, "[IN ] setOnOkClickListener::onClick()");
				LogUtil.d(TAG, "which:" + which);

				// 入力値を取得し、ユーザ情報DTOにセット
				userInfoDto.setUserId(dialog.getInputText(EnumLoginParams.USER_ID.getResId()));
				userInfoDto.setPassword(dialog.getInputText(EnumLoginParams.PASSWORD.getResId()));

				// Corundum Apps Manager認証を行う (非同期開始)
				authFunction.authenticate(getHttpClient(), AbstractAuthActivity.this, userInfoDto);

				LogUtil.d(TAG, "userInfoDto.getUserId():" + userInfoDto.getUserId());

				LogUtil.d(TAG, "[OUT] setOnOkClickListener::onClick()");
			}

		});
		dialog.setOnCancelClickListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface d, int which) {
				LogUtil.d(TAG, "[IN ] setOnCancelClickListener::onClick()");
				// 終了
				d.dismiss();
				LogUtil.d(TAG, "[OUT] setOnCancelClickListener::onClick()");
			}
		});
		dialog.show(this.getSupportFragmentManager(), TAG);

		LogUtil.d(TAG, "[OUT] registUserInfo()");
	}
}
