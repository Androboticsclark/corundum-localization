/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.features.appsmanager;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.corundumPro.R;
import com.corundumPro.common.layout.CheckableLinearLayout;

/**
 * ApplicationListLayoutクラス
 * <p>
 * 「アプリケーションリストLayout」クラス
 * </p>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class ApplicationListLayout extends CheckableLinearLayout {
	static final String TAG = "ApplicationListView";

	private ImageView icon;
	private TextView applicationName;
	private TextView description;

	/**
	 * コンストラクタ
	 *
	 * @param context コンテキスト
	 * @param attrs 属性セット
	 */
	public ApplicationListLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * コンストラクタ
	 *
	 * @param context コンテキスト
	 */
	public ApplicationListLayout(Context context) {
		super(context);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		this.icon = (ImageView)findViewById(R.id.application_list_row_ImageView1);
		this.applicationName = (TextView)findViewById(R.id.application_list_row_TextView1);
		this.description = (TextView)findViewById(R.id.application_list_row_TextView2);

    }

	public void bindView(ApplicationListInfo applicationListInfo) {
		this.icon.setImageURI(Uri.parse(applicationListInfo.getIconPath()));
		this.applicationName.setText(applicationListInfo.getApplicationName());
		this.description.setText(applicationListInfo.getDescription());
	}
}
