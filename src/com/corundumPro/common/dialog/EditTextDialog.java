/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditTextDialog extends DialogFragment {
	private DialogInterface.OnClickListener okClickListener = null;
	private DialogInterface.OnClickListener cancelClickListener = null;
	private SparseArray<EditText> editTextMap = new SparseArray<EditText>();

	/**
	 * インスタンスを生成します。<br>
	 *
	 * @param title タイトルID
	 * @param message メッセージID
	 * @param layout レイアウトID
	 * @return ダイアログオブジェクト
	 */
	public static EditTextDialog newInstance(int title, int message, int layout) {
		return newInstance(title, message, 0, android.R.string.ok, android.R.string.cancel, layout);
	}

	/**
	 * インスタンスを生成します。<br>
	 *
	 * @param title タイトルID
	 * @param message メッセージID
	 * @param positive ポジティブボタンラベル
	 * @param negative ネガティブボタンラベル
	 * @param layout レイアウトID
	 * @return ダイアログオブジェクト
	 */
	public static EditTextDialog newInstance(int title, int message, int positive, int negative, int layout) {
		return newInstance(title, message, 0, positive, negative, layout);
	}

	/**
	 * インスタンスを生成します。<br>
	 *
	 * @param title タイトルID
	 * @param message メッセージID
	 * @param icon アイコンID
	 * @param positive ポジティブボタンラベル
	 * @param negative ネガティブボタンラベル
	 * @param layout レイアウトID
	 * @return ダイアログオブジェクト
	 */
	public static EditTextDialog newInstance(int title, int message, int icon, int positive, int negative, int layout) {
		EditTextDialog fragment = new EditTextDialog();
		Bundle args = new Bundle();
		args.putInt("title", title);
		args.putInt("message", message);
		args.putInt("icon", icon);
		args.putInt("positive", positive);
		args.putInt("negative", negative);
		args.putInt("layout", layout);
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
		int layout = getArguments().getInt("layout");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// LayoutInflaterインスタンス生成
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(layout, null);

        // EditTextのマップを生成
        createEditTextMap(view);

		builder.setTitle(title)
		.setIcon(icon)
		.setMessage(message)
		.setView(view)
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

	/**
	 * 入力値を取得します。
	 *
	 * @param resId
	 * @return
	 */
	public String getInputText(int resId) {
		String inputText = "";

		EditText editText = editTextMap.get(resId);

		if (editText != null) {
			inputText = editText.getText().toString();
		}
		return inputText;
	}

	/**
	 * EditTextを取得し、マップを生成します。
	 *
	 * @param v
	 */
	private void createEditTextMap(View v) {
		if (v instanceof EditText) {
			EditText editText = (EditText)v;
			editTextMap.put(Integer.valueOf(editText.getId()), editText);

		} else if (v instanceof ViewGroup) {
			ViewGroup viewGroup = (ViewGroup) v;
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				createEditTextMap(viewGroup.getChildAt(i));
			}
		}
	}
}