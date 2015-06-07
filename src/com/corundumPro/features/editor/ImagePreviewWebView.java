/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.features.editor;

import com.corundumPro.common.util.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * ImagePreviewWebViewクラス
 * <p>
 * 「画像WebView」クラス
 * </p>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class ImagePreviewWebView extends WebView {
	static final String TAG = "ImagePreviewWebView";

	/** アクティビティ */
	private Activity activity;

	/**
	 * コンストラクタ
	 * <p>
	 * 画像WebViewのコンストラクタ。
	 * </p>
	 * @param context コンテキスト
	 * @param attributeSet 属性セット
	 */
	public ImagePreviewWebView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		LogUtil.d(TAG, "[IN ] ImagePreviewWebView()");
		LogUtil.d(TAG, "[OUT] ImagePreviewWebView()");
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
