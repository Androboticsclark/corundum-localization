package com.corundumPro.features.screensaver;

import com.corundumPro.common.util.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * ScreenSaverWebViewクラス
 * <p>
 * 「スクリーンセーバーWebView」クラス
 * </p>
 * @author androbotics.ltd
 */
public class ScreenSaverWebView extends WebView {
	static final String TAG = "ScreenSaverWebView";

	/** アクティビティ */
	private Activity activity;

	/**
	 * コンストラクタ
	 * <p>
	 * ホームWebViewのコンストラクタ。
	 * </p>
	 * @param context コンテキスト
	 * @param attributeSet 属性セット
	 */
	public ScreenSaverWebView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		LogUtil.d(TAG, "[IN ] ScreenSaverWebView()");
		LogUtil.d(TAG, "[OUT] ScreenSaverWebView()");
	}

	/**
	 * アクティビティ設定処理
	 * <p>
	 * アクティビティを保持する。
	 * </p>
	 * @param activity アクティビティオブジェクト
	 */
	public void setActivity(Activity activity) {
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

		if (null != activity) {
			/* 終了 */
			activity.finish();
		}

		LogUtil.d(TAG, "return true");
		LogUtil.d(TAG, "[OUT] onTouchEvent()");
		return true;
	}
}
