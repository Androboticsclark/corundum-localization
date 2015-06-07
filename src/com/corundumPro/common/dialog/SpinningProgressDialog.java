/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.dialog;

import com.corundumPro.R;
import com.corundumPro.common.util.LogUtil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class SpinningProgressDialog extends DialogFragment {
	static final String TAG = "SpinningProgressDialog";

	private DialogInterface.OnClickListener cancelClickListener = null;

	/** プログレスダイアログオブジェクト **/
	private ProgressDialog progressDialog;

	/**
	 * インスタンスを生成します。<br>
	 *
	 * @return ダイアログオブジェクト
	 */
	public static SpinningProgressDialog newInstance(int title, int message, boolean cancel) {
		LogUtil.d(TAG, "[IN ] newInstance()");

		SpinningProgressDialog fragment = new SpinningProgressDialog();
		Bundle args = new Bundle();
		args.putInt("title", title);
		args.putInt("message", message);
		fragment.setArguments(args);
		fragment.setCancelable(false);

		LogUtil.d(TAG, "[OUT] newInstance()");

		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle safedInstanceState) {
		LogUtil.d(TAG, "[IN ] onCreateDialog()");

		int title = getArguments().getInt("title");
		int message = getArguments().getInt("message");
		LogUtil.d(TAG, "title:" + title);
		LogUtil.d(TAG, "message:" + message);

		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setTitle(title);
		progressDialog.setMessage(getResources().getText(message));
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE
								,getResources().getString(R.string.download_cancel)
								,this.cancelClickListener);

		LogUtil.d(TAG, "[OUT] onCreateDialog()");

		return progressDialog;
	}

	@Override
	public Dialog getDialog(){
		return progressDialog;
	}


	@Override
	public void onDestroy(){
		LogUtil.d(TAG, "[IN ] onDestroy()");

		super.onDestroy();
		progressDialog = null;

		LogUtil.d(TAG, "[OUT] onDestroy()");
	}

	/**
     * Cancelクリックリスナーの登録
     */
	public void setOnCancelClickListener(DialogInterface.OnClickListener listener) {
		this.cancelClickListener = listener;
	}
}
