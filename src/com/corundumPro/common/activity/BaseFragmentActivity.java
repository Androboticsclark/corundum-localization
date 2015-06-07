package com.corundumPro.common.activity;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;

import com.corundumPro.R;
import com.corundumPro.common.dialog.OkDialog;
import com.corundumPro.common.dialog.SpinningProgressDialog;
import com.corundumPro.common.util.LogUtil;

/**
 * BaseFragmentActivityクラス
 * <p>
 * アクティビティのベースフラグメントクラス
 * </p>
 * @author androbotics.ltd
 */
public class BaseFragmentActivity extends FragmentActivity {
	static final String TAG = "BaseFragmentActivity";

	/** プログレスダイアログ */
	protected SpinningProgressDialog spinningProgressDialog = null;

	/**
	 * エラーダイアログ表示<br>
	 * エラーダイアログを表示します。<br>
	 *
	 * @param message メッセージID
	 */
	public void showErrorDialog(int messageId) {
		showOkDialog(R.string.error_dialog_title, messageId);
	}

	/**
	 * エラーダイアログ表示<br>
	 * エラーダイアログを表示します。<br>
	 *
	 * @param message メッセージ
	 */
	public void showErrorDialog(String message) {
		showOkDialog(R.string.error_dialog_title, message);
	}

	/**
	 * メッセージダイアログ表示<br>
	 * メッセージダイアログを表示します。<br>
	 *
	 * @param message メッセージID
	 */
	public void showMsgDialog(int messageId) {
		showOkDialog(R.string.app_name, messageId);
	}

	/**
	 * メッセージダイアログ表示<br>
	 * メッセージダイアログを表示します。<br>
	 *
	 * @param message メッセージ
	 */
	public void showMsgDialog(String message) {
		showOkDialog(R.string.app_name, message);
	}

	/**
	 * OKダイアログ表示<br>
	 * OKダイアログを表示します。<br>
	 *
	 * @param title タイトルID
	 * @param message メッセージ
	 */
	private void showOkDialog(int title, int message) {
		showOkDialog(title, this.getString(message));
	}

	/**
	 * OKダイアログ表示<br>
	 * OKダイアログを表示します。<br>
	 *
	 * @param title タイトルID
	 * @param message メッセージ
	 */
	private void showOkDialog(int title, String message) {
		LogUtil.d(TAG, "[IN ] showOkDialog()");

		// エラーダイアログ生成
		final OkDialog dialog = OkDialog.newInstance(title
													,message
													,R.drawable.ic_launcher
													,R.string.dialog_ok);

		dialog.setOnOkClickListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface d, int which) {
				/* 終了 */
				dialog.dismiss();
			}
		});
		dialog.show(this.getSupportFragmentManager(), TAG);

		LogUtil.d(TAG, "[OUT] showErrorDialog()");
	}

	/**
	 * プログレスダイアログを表示します。
	 *
	 */
	public void showSpinningDialog() {
		showSpinningDialog(null);
	}

	/**
	 * プログレスダイアログを表示します。
	 *
	 * @param cancelClickListener キャンセルクリックリスナー
	 *
	 */
	public void showSpinningDialog(DialogInterface.OnClickListener cancelClickListener) {
		showSpinningDialog(R.string.msg_waiting, R.string.msg_connecting, cancelClickListener);
	}

	/**
	 * プログレスダイアログを表示します。
	 *
	 * @param title タイトルID
	 * @param message メッセージID
	 * @param cancelClickListener キャンセルクリックリスナー
	 */
	public void showSpinningDialog(int title, int message, DialogInterface.OnClickListener cancelClickListener) {
		LogUtil.d(TAG, "[IN ] showSpinningProgressDialog()");

		// プログレスダイアログを表示
		boolean cancel = false;
		if (cancelClickListener != null) {
			cancel = true;
		}
		spinningProgressDialog = SpinningProgressDialog.newInstance(title, message, cancel);
		spinningProgressDialog.setOnCancelClickListener(cancelClickListener);
		spinningProgressDialog.show(this.getSupportFragmentManager(), TAG);

		LogUtil.d(TAG, "[OUT] showSpinningProgressDialog()");
	}

	/**
	 * プログレスダイアログを消します。
	 */
	public void closeSpinningDialog() {
		LogUtil.d(TAG, "[IN ] closeSpinningDialog()");

		// プログレスダイアログを消す
		if (spinningProgressDialog != null) {
			spinningProgressDialog.getDialog().dismiss();
		}
		LogUtil.d(TAG, "[OUT] closeSpinningDialog()");
	}
}