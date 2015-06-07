/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.dialog;

import org.apache.commons.lang3.StringUtils;

import com.corundumPro.R;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.common.util.StringUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class OkDialog extends DialogFragment {
	static final String TAG = "OkDialog";

	/** OKクリックリスナー */
	private DialogInterface.OnClickListener okClickListener = null;

	/**
	 * インスタンスを生成します。<br>
	 *
	 * @param title タイトルID
	 * @param message メッセージID
	 * @return ダイアログオブジェクト
	 */
	public static OkDialog newInstance(int title, int message) {
		return newInstance(title, message, 0, android.R.string.ok);
	}

	/**
	 * インスタンスを生成します。<br>
	 *
	 * @param title タイトルID
	 * @param message メッセージ
	 * @return ダイアログオブジェクト
	 */
	public static OkDialog newInstance(int title, String message) {
		return newInstance(title, message, 0, android.R.string.ok);
	}

	/**
	 * インスタンスを生成します。<br>
	 *
	 * @param title タイトルID
	 * @param message メッセージID
	 * @param positive ポジティブボタンラベル
	 * @return ダイアログオブジェクト
	 */
	public static OkDialog newInstance(int title, int message, int positive) {
		return newInstance(title, message, 0, positive);
	}

	/**
	 * インスタンスを生成します。<br>
	 *
	 * @param title タイトルID
	 * @param message メッセージ
	 * @param positive ポジティブボタンラベル
	 * @return ダイアログオブジェクト
	 */
	public static OkDialog newInstance(int title, String message, int positive) {
		return newInstance(title, message, 0, positive);
	}

	/**
	 * インスタンスを生成します。<br>
	 *
	 * @param title タイトルID
	 * @param message メッセージ
	 * @param icon アイコンID
	 * @param positive ポジティブボタンラベル
	 * @return ダイアログオブジェクト
	 */
	public static OkDialog newInstance(int title, String message, int icon, int positive) {
		LogUtil.d(TAG, "[IN ] newInstance()");

		OkDialog fragment = new OkDialog();
		Bundle args = new Bundle();
		args.putInt("title", title);
		args.putInt("messageInt", R.string.msg_blank);
		args.putString("messageStr", StringUtil.nvl(message, "").replaceAll(Const.SEPARATOR_COMMA, Const.CARRIAGE_RETURN));
		args.putInt("icon", icon);
		args.putInt("positive", positive);
		fragment.setArguments(args);
		fragment.setCancelable(false);

		LogUtil.d(TAG, "[OUT] newInstance()");

		return fragment;
	}

	/**
	 * インスタンスを生成します。<br>
	 *
	 * @param title タイトルID
	 * @param message メッセージID
	 * @param icon アイコンID
	 * @param positive ポジティブボタンラベル
	 * @return ダイアログオブジェクト
	 */
	public static OkDialog newInstance(int title, int message, int icon, int positive) {
		LogUtil.d(TAG, "[IN ] newInstance()");

		OkDialog fragment = new OkDialog();
		Bundle args = new Bundle();
		args.putInt("title", title);
		args.putInt("messageInt", message);
		args.putString("messageStr", "");
		args.putInt("icon", icon);
		args.putInt("positive", positive);
		fragment.setArguments(args);
		fragment.setCancelable(false);

		LogUtil.d(TAG, "[OUT] newInstance()");

		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle safedInstanceState) {
		LogUtil.d(TAG, "[IN ] onCreateDialog()");

		int title = getArguments().getInt("title");
		int messageInt = getArguments().getInt("messageInt");
		String messageStr = getArguments().getString("messageStr");
		int icon = getArguments().getInt("icon");
		int positive = getArguments().getInt("positive");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setIcon(icon);
		builder.setPositiveButton(positive, okClickListener);
		if (StringUtils.isEmpty(messageStr)) {
			// 文字列メッセージが空の場合、メッセージIDを使用する
			builder.setMessage(messageInt);
		} else {
			// 文字列メッセージが存在する場合、文字列メッセージを使用する
			builder.setMessage(messageStr);
		}


		LogUtil.d(TAG, "[OUT] onCreateDialog()");

		return builder.create();
	}

	/**
     * OKクリックリスナーの登録
     */
    public void setOnOkClickListener(DialogInterface.OnClickListener listener) {
        this.okClickListener = listener;
    }
}
