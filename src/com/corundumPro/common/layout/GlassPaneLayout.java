/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * GlassPaneレイアウト.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class GlassPaneLayout extends FrameLayout {
	static final String TAG = "GlassPaneLayout";

	/** 画面全体を覆う膜 */
	protected Paint innerPaint;
	/** GlassPane上に配置するコンテナ */
	protected LinearLayout container;
	/** コンテナにタッチイベントをディスパッチ済みか判断するフラグ */
	protected boolean isDispatchedTouchEvent2Container = false;

	/**
	 * コンストラクタ
	 *
	 * @param context
	 */
	public GlassPaneLayout(Context context) {
		super(context);
		this.init();
	}

	/**
	 * コンストラクタ
	 *
	 * @param context
	 * @param attrs
	 */
	public GlassPaneLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}

	/**
	 * GlassPane上に配置するコンテナにViewを設定します。
	 *
	 * @param view
	 */
	public void setFloatingView(View view) {
        if ( this.container.getChildAt(0) != null ) {
            this.container.removeViewAt(0);
        }
        ViewParent parent = view.getParent();
        if ( parent != null ) {
            ((ViewGroup)parent).removeView(view);
        }
        this.container.addView(view, 0);
    }

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if ( event.isSystem() ) {
			return super.dispatchKeyEvent(event);
		}
		return true;
	}

	/* トラックボール */
	@Override
	public boolean dispatchTrackballEvent(MotionEvent event) {
		return true;
	}

	/* タッチ */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		boolean ret = false;
		if (this.isDispatchedTouchEvent2Container) {
			// コンテナへのタッチイベントディスパッチが済んでいる場合、配下へのディスパッチを終了する。
			ret = true;
		} else {
			// コンテナへのタッチイベントディスパッチが未済の場合、コンテナにディスパッチする。
			ret = this.container.dispatchTouchEvent(event);
			// コンテナへのタッチイベントディスパッチを済にする。
			this.isDispatchedTouchEvent2Container = true;
		}
		return ret;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        RectF drawRect = new RectF();
        drawRect.set(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());

        canvas.drawRoundRect(drawRect, 5, 5, this.innerPaint);

        this.container.draw(canvas);
	}

	/**
	 * GlassPane上に配置したコンテナにリスナをセットします。
	 *
	 * @param listener
	 */
	@Override
	public void setOnTouchListener(OnTouchListener listener) {
		// タッチイベントリスナーをコンテナにセットする。
		this.container.setOnTouchListener(listener);
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}

	/**
	 * 初期化処理を行います。
	 *
	 */
	private void init() {

        // コンテナへのタッチイベントディスパッチを未済にする
        isDispatchedTouchEvent2Container = false;

        // レイアウトを画面全体に設定
        this.setLayoutParams(new LayoutParams
                (LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        // 全体を覆う膜を設定
        this.innerPaint = new Paint();
        this.innerPaint.setARGB(0, 0, 0, 0);
        this.innerPaint.setAntiAlias(true);

        // GlassPane上に配置するコンテナを設定
        this.container = new LinearLayout(this.getContext());
        this.container.setBackgroundColor(Color.argb(0, 0, 0, 0));
        this.container.setLayoutParams(new LayoutParams
                (LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.container.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

        // GlassPane上に配置
        this.addView(this.container);
	}
}
