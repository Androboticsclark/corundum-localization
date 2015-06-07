/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.features.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.corundumPro.common.util.LogUtil;

/**
 * EditorPreviewWebViewクラス
 * <p>
 * 「コランダムエディタWebView」クラス
 * </p>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class EditorPreviewWebView extends WebView {
	static final String TAG = "EditorPreviewWebView";

	/**
	 * コンストラクタ
	 * <p>
	 * コランダムエディタWebViewのコンストラクタ。
	 * </p>
	 * @param context コンテキスト
	 * @param attributeSet 属性セット
	 */
	public EditorPreviewWebView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		LogUtil.d(TAG, "[IN ] EditorPreviewWebView()");
		LogUtil.d(TAG, "[OUT] EditorPreviewWebView()");
	}
}
