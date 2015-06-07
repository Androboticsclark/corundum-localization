package com.corundumPro.features.home;

import com.corundumPro.common.constant.Const;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.features.actionsheet.ActionSheetActivity;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * HomeWebViewクラス
 * <p>
 * 「ホームWebView」クラス
 * </p>
 * @author androbotics.ltd
 */
public class HomeWebView extends WebView {
	static final String TAG = "HomeWebView";

	/** タッチパネルの状態変化カウント */
	private int actionCount = 0;

	/** アクティビティ(スクリーンセーバー操作、オプションメニュー表示に使用する) */
	private HomeActivity activity;

	/**
	 * コンストラクタ
	 * <p>
	 * ホームWebViewのコンストラクタ。
	 * </p>
	 * @param context コンテキスト
	 * @param attributeSet 属性セット
	 */
	public HomeWebView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		LogUtil.d(TAG, "[IN ] HomeWebView()");
		LogUtil.d(TAG, "[OUT] HomeWebView()");
	}

	/**
	 * アクティビティ設定処理
	 * <p>
	 * スクリーンセーバー操作、オプションメニュー表示のためのアクティビティを保持する。
	 * </p>
	 * @param activity アクティビティオブジェクト
	 */
	public void setActivity(HomeActivity activity) {
		LogUtil.d(TAG, "[IN ] setActivity()");
		this.activity = activity;
		LogUtil.d(TAG, "[OUT] setActivity()");
	}

	/**
	 * タッチベント処理
	 * <p>
	 * onTouchEvent()のオーバーライド<br>
	 * 画面タッチ時にコールされる。
	 * </p>
	 * @param motionEvent モーションイベントオブジェクト
	 * @return 処理結果
	 */
	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		LogUtil.d(TAG, "[IN ] onTouchEvent()");
		LogUtil.d(TAG, "motionEvent.getAction():" + motionEvent.getAction());
		LogUtil.d(TAG, "motionEvent.getPointerCount():" + motionEvent.getPointerCount());

		boolean result;

		if (null != activity) {
			/* スクリーンセーバー再起動 */
			activity.stopScreenSaver();
			activity.startScreenSaver();
		}

		/* マルチタッチを離した */
		if ((5 <= actionCount) && ((262 == motionEvent.getAction()) || (6 == motionEvent.getAction()))) {

			actionCount = 0;

			/* ActionSheetActivity起動 */
			LogUtil.d(TAG, "ActionSheetActivity");
			Intent intent = new Intent(activity.getApplication(), ActionSheetActivity.class);
			activity.startActivityForResult(intent, Const.REQUEST_ACTION_SHEET);

			LogUtil.d(TAG, "return false");
			LogUtil.d(TAG, "[OUT] onTouchEvent()");
			return false;
		}

		/* 指をはなした */
		if (1 == motionEvent.getAction()) {
			actionCount = 0;
		}

		/* タッチした */
		if (2 == motionEvent.getPointerCount()) {
			/* マルチタッチ検出 */
			actionCount++;
		} else {
			actionCount--;
		}

		result = super.onTouchEvent(motionEvent);

		LogUtil.d(TAG, "result:" + result);
		LogUtil.d(TAG, "[OUT] onTouchEvent()");
		return result;
	}
}
