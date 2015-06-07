/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class OkCancelDialog extends DialogFragment {
	static final String TAG = "OkCancelDialog";

	private DialogInterface.OnClickListener okClickListener = null;
	private DialogInterface.OnClickListener cancelClickListener = null;

	/**
	 * インスタンスを生成します。<br>
	 *
	 * @param title タイトルID
	 * @param message メッセージID
	 * @return ダイアログオブジェクト
	 */
	public static OkCancelDialog newInstance(int title, int message) {
		return newInstance(title, message, 0, android.R.string.ok, android.R.string.cancel);
	}

	/**
	 * インスタンスを生成します。<br>
	 *
	 * @param title タイトルID
	 * @param message メッセージID
	 * @param positive ポジティブボタンラベル
	 * @param negative ネガティブボタンラベル
	 * @return ダイアログオブジェクト
	 */
	public static OkCancelDialog newInstance(int title, int message, int positive, int negative) {
		return newInstance(title, message, 0, positive, negative);
	}

	/**
	 * インスタンスを生成します。<br>
	 *
	 * @param title タイトルID
	 * @param message メッセージID
	 * @param icon アイコンID
	 * @param positive ポジティブボタンラベル
	 * @param negative ネガティブボタンラベル
	 * @return ダイアログオブジェクト
	 */
	public static OkCancelDialog newInstance(int title, int message, int icon, int positive, int negative) {
		OkCancelDialog fragment = new OkCancelDialog();
		Bundle args = new Bundle();
		args.putInt("title", title);
		args.putInt("message", message);
		args.putInt("icon", icon);
		args.putInt("positive", positive);
		args.putInt("negative", negative);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle safedInstanceState) {
		int title = getArguments().getInt("title");
		int message = getArguments().getInt("message");
		int icon = getArguments().getInt("icon");
		int positive = getArguments().getInt("positive");
		int negative = getArguments().getInt("negative");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title)
		.setIcon(icon)
		.setMessage(message)
		.setPositiveButton(positive, this.okClickListener)
		.setNegativeButton(negative, this.cancelClickListener);

		return builder.create();
	}

	/**
     * OKクリックリスナーの登録
     */
	public void setOnOkClickListener(DialogInterface.OnClickListener listener) {
		this.okClickListener = listener;
	}

	/**
     * Cancelクリックリスナーの登録
     */
	public void setOnCancelClickListener(DialogInterface.OnClickListener listener) {
		this.cancelClickListener = listener;
	}
}
