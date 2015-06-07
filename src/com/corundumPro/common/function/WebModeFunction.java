/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.function;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;

import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.dao.DBAdapter;
import com.corundumPro.common.dto.WebModeDto;
import com.corundumPro.common.util.LogUtil;

/**
 * Webモードファンクション.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class WebModeFunction {
	static final String TAG = "WebModeFunction";

	/** Webモード情報DTOマップ */
	private SparseArray<WebModeDto> webModeDtoMap = new SparseArray<WebModeDto>();
	/** デフォルトWebモード情報DTO */
	private WebModeDto defaultWebModeDto;
	/** 現在のWebモード情報DTO */
	private WebModeDto currentWebModeDto;

	/**
	 * コンストラクタ<br>
	 *
	 */
	public WebModeFunction() {
		LogUtil.d(TAG, "[IN ] WebModeFunction()");

		// 初期化
		init();

		LogUtil.d(TAG, "[OUT] WebModeFunction()");
	}

	/**
	 * 現在の情報としてプリファレンスを読み込み、Webモードを設定します。<br>
	 *
	 * @param preference
	 */
	public void loadAsCurrent(SharedPreferences preference) {
		LogUtil.d(TAG, "[IN ] loadAsCurrent()");

		// Webモードの設定
		this.setWebMode(preference.getInt(SystemInfo.KEY_CURRENT_WEB_MODE, DBAdapter.WEB_MODE_OPERATION));
		// ツールバーの設定
		this.setVisibleToolBar(preference.getBoolean(SystemInfo.KEY_BOTTOM_BAR_FLAG, true));
		// ステータスバーの設定
		this.setVisibleStatusBar(preference.getBoolean(SystemInfo.KEY_STATUS_BAR_FLAG, true));

		LogUtil.d(TAG, "currentWebModeDto:" + this.currentWebModeDto.toString());
		LogUtil.d(TAG, "[OUT] loadAsCurrent()");
	}

	/**
	 * プリファレンスを読み込み、Webモードを設定します。<br>
	 *
	 * @param preference
	 */
	public void load(SharedPreferences preference) {
		LogUtil.d(TAG, "[IN ] load()");

		// Webモードの設定
		this.setWebMode(preference.getInt(SystemInfo.KEY_WEB_MODE, DBAdapter.WEB_MODE_OPERATION));
		// ツールバーの設定
		this.setVisibleToolBar(preference.getBoolean(SystemInfo.KEY_BOTTOM_BAR_FLAG, true));
		// ステータスバーの設定
		this.setVisibleStatusBar(preference.getBoolean(SystemInfo.KEY_STATUS_BAR_FLAG, true));

		LogUtil.d(TAG, "currentWebModeDto:" + this.currentWebModeDto.toString());
		LogUtil.d(TAG, "[OUT] load()");
	}

	/**
	 * 現在の情報としてプリファレンスにWebモード情報を保存します。<br>
	 *
	 * @param editor
	 */
	public void saveAsCurrent(Editor editor) {
		LogUtil.d(TAG, "[IN ] saveAsCurrent()");

		// Webモードの設定を保存
		editor.putInt(SystemInfo.KEY_CURRENT_WEB_MODE, this.currentWebModeDto.getWebMode());
		// ツールバーの設定を保存
		editor.putBoolean(SystemInfo.KEY_BOTTOM_BAR_FLAG, this.currentWebModeDto.isVisibleToolBar());
		// ステータスバーの設定を保存
		editor.putBoolean(SystemInfo.KEY_STATUS_BAR_FLAG, this.currentWebModeDto.isVisibleStatusBar());

		editor.commit();

		LogUtil.d(TAG, "SystemInfo.KEY_CURRENT_WEB_MODE:" + String.valueOf(this.currentWebModeDto.getWebMode()));
		LogUtil.d(TAG, "SystemInfo.KEY_BOTTOM_BAR_FLAG:" + String.valueOf(this.currentWebModeDto.isVisibleToolBar()));
		LogUtil.d(TAG, "SystemInfo.KEY_NO_STATUS_BAR_FLAG:" + String.valueOf(this.currentWebModeDto.isVisibleStatusBar()));
		LogUtil.d(TAG, "[OUT] saveAsCurrent()");
	}

	/**
	 * プリファレンスにWebモード情報を保存します。<br>
	 *
	 * @param editor
	 */
	public void save(Editor editor) {
		LogUtil.d(TAG, "[IN ] save()");

		// Webモードの設定を保存
		editor.putInt(SystemInfo.KEY_WEB_MODE, this.currentWebModeDto.getWebMode());
		// ツールバーの設定を保存
		editor.putBoolean(SystemInfo.KEY_BOTTOM_BAR_FLAG, this.currentWebModeDto.isVisibleToolBar());
		// ステータスバーの設定を保存
		editor.putBoolean(SystemInfo.KEY_STATUS_BAR_FLAG, this.currentWebModeDto.isVisibleStatusBar());

		editor.commit();

		LogUtil.d(TAG, "SystemInfo.KEY_WEB_MODE:" + String.valueOf(this.currentWebModeDto.getWebMode()));
		LogUtil.d(TAG, "SystemInfo.KEY_BOTTOM_BAR_FLAG:" + String.valueOf(this.currentWebModeDto.isVisibleToolBar()));
		LogUtil.d(TAG, "SystemInfo.KEY_NO_STATUS_BAR_FLAG:" + String.valueOf(this.currentWebModeDto.isVisibleStatusBar()));
		LogUtil.d(TAG, "[OUT] save()");
	}

	/**
	 * 現在のWebモードを取得します。<br>
	 *
	 * @return webMode
	 */
	public int getWebMode() {
		LogUtil.d(TAG, "[IN ] getWebMode()");

		int ret = this.currentWebModeDto.getWebMode();

		LogUtil.d(TAG, "return:" + String.valueOf(ret));
		LogUtil.d(TAG, "[OUT] getWebMode()");

		return ret;
	}

	/**
	 * 現在のWebモードを設定します。<br>
	 *
	 * @param webMode
	 */
	public void setWebMode(int webMode) {
		LogUtil.d(TAG, "[IN ] setWebMode()");

		// 現在のWebモード情報DTO設定
		this.currentWebModeDto = findByHomeMode(webMode);

		LogUtil.d(TAG, "currentWebModeDto:" + this.currentWebModeDto.toString());
		LogUtil.d(TAG, "[OUT] setWebMode()");
	}

	/**
	 * 現在のURLバーの表示可否を取得します。<br>
	 *
	 * @return 可：View.VISIBLE, 不可：View.GONE
	 */
	public int getVisibleUrlBar() {
		LogUtil.d(TAG, "[IN ] getVisibleUrlBar()");

		int ret = View.GONE;
		if (this.currentWebModeDto.isVisibleUrlBar()) {
			ret = View.VISIBLE;
		}

		LogUtil.d(TAG, "return:" + String.valueOf(ret));
		LogUtil.d(TAG, "[OUT] getVisibleUrlBar()");

		return ret;
	}

	/**
	 * 現在のURLバーの表示可否を取得します。<br>
	 *
	 * @return 可：true, 不可：false
	 */
	public boolean isVisibleUrlBar() {
		LogUtil.d(TAG, "[IN ] isVisibleUrlBar()");

		boolean ret = this.currentWebModeDto.isVisibleUrlBar();

		LogUtil.d(TAG, "return:" + String.valueOf(ret));
		LogUtil.d(TAG, "[OUT] isVisibleUrlBar()");

		return ret;
	}

	/**
	 * 現在のURLバーの表示可否を設定します。<br>
	 *
	 * @param isVisibleUrlBar 可：true, 不可：false
	 */
	public void setVisibleUrlBar(boolean isVisibleUrlBar) {
		LogUtil.d(TAG, "[IN ] setVisibleUrlBar()");

		this.currentWebModeDto.setVisibleUrlBar(isVisibleUrlBar);

		LogUtil.d(TAG, "currentWebModeDto:" + this.currentWebModeDto.toString());
		LogUtil.d(TAG, "[OUT] setVisibleUrlBar()");
	}

	/**
	 * 現在のツールバーの表示可否を取得します。<br>
	 *
	 * @return 可：View.VISIBLE, 不可：View.GONE
	 */
	public int getVisileToolBar() {
		LogUtil.d(TAG, "[IN ] getVisileToolBar()");

		int ret = View.GONE;
		if (this.currentWebModeDto.isVisibleToolBar()) {
			ret = View.VISIBLE;
		}

		LogUtil.d(TAG, "return:" + String.valueOf(ret));
		LogUtil.d(TAG, "[OUT] getVisileToolBar()");

		return ret;
	}

	/**
	 * 現在のツールバーの表示可否を取得します。<br>
	 *
	 * @return 可：true, 不可：false
	 */
	public boolean isVisileToolBar() {
		LogUtil.d(TAG, "[IN ] isVisileToolBar()");

		boolean ret = this.currentWebModeDto.isVisibleToolBar();

		LogUtil.d(TAG, "return:" + String.valueOf(ret));
		LogUtil.d(TAG, "[OUT] isVisileToolBar()");

		return ret;
	}

	/**
	 * 現在のツールバーの表示可否を設定します。<br>
	 *
	 * @param isVisibleToolBar 可：true, 不可：false
	 */
	public void setVisibleToolBar(boolean isVisibleToolBar) {
		LogUtil.d(TAG, "[IN ] setVisibleToolBar()");

		this.currentWebModeDto.setVisibleToolBar(isVisibleToolBar);

		LogUtil.d(TAG, "currentWebModeDto:" + this.currentWebModeDto.toString());
		LogUtil.d(TAG, "[OUT] setVisibleToolBar()");
	}

	/**
	 * ステータスバー表示可否のマスクを取得します。<br>
	 *
	 * @return
	 */
	public int getVisibleStatusBar() {
		LogUtil.d(TAG, "[IN ] getVisibleStatusBar()");

		int ret = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (this.currentWebModeDto.isVisibleStatusBar()) {
			ret = ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
		}

		LogUtil.d(TAG, "return:" + String.valueOf(ret));
		LogUtil.d(TAG, "[OUT] getVisibleStatusBar()");
		return ret;
	}

	/**
	 * 現在のステータスバーの表示可否を取得します。<br>
	 *
	 * @return 可：true, 不可：false
	 */
	public boolean isVisileStatusBar() {
		LogUtil.d(TAG, "[IN ] isVisileStatusBar()");

		boolean ret = this.currentWebModeDto.isVisibleStatusBar();

		LogUtil.d(TAG, "return:" + String.valueOf(ret));
		LogUtil.d(TAG, "[OUT] isVisileStatusBar()");

		return ret;
	}

	/**
	 * ステータスバー表示可否を設定します。<br>
	 *
	 * @param isVisibleStatusBar 可：true, 不可：false
	 */
	public void setVisibleStatusBar(boolean isVisibleStatusBar) {
		LogUtil.d(TAG, "[IN ] setVisibleStatusBar()");

		int webMode = 0;
		WebModeDto dto;
		for (int i = 0; i < this.webModeDtoMap.size(); i++) {
			webMode = webModeDtoMap.keyAt(i);
			dto = findByHomeMode(webMode);
			dto.setVisibleStatusBar(isVisibleStatusBar);
		}

		LogUtil.d(TAG, "currentWebModeDto:" + this.currentWebModeDto.toString());
		LogUtil.d(TAG, "[OUT] setVisibleStatusBar()");
	}

	/**
	 * メッセージ有無を取得します。<br>
	 *
	 * @return 有：true, 無：false
	 */
	public boolean hasMessage() {
		LogUtil.d(TAG, "[IN ] hasMessage()");

		boolean ret = false;

		if (this.currentWebModeDto.isVisibleToolBar() == false) {
			// ツールバーがない場合、メッセージを表示する
			ret = true;
		}

		LogUtil.d(TAG, "return:" + String.valueOf(ret));
		LogUtil.d(TAG, "[OUT] hasMessage()");

		return ret;
	}

	/**
	 * 初期化を行います。<br>
	 *
	 */
	private void init() {
		WebModeDto dto;

		// 管理者モード
		dto = new WebModeDto();
		dto.setWebMode(DBAdapter.WEB_MODE_ADMIN);
		dto.setVisibleUrlBar(true);
		dto.setVisibleToolBar(true);
		this.webModeDtoMap.put(dto.getWebMode(), dto);

		// オペレーションモード(デフォルト)
		dto = new WebModeDto();
		dto.setWebMode(DBAdapter.WEB_MODE_OPERATION);
		dto.setVisibleUrlBar(false);
		dto.setVisibleToolBar(true);
		this.webModeDtoMap.put(dto.getWebMode(), dto);
		this.defaultWebModeDto = dto;

		// フルスクリーンモード
		dto = new WebModeDto();
		dto.setWebMode(DBAdapter.WEB_MODE_FULL);
		dto.setVisibleUrlBar(false);
		dto.setVisibleToolBar(false);
		this.webModeDtoMap.put(dto.getWebMode(), dto);
	}

	/**
	 * Webモードで該当のWebモード情報DTOを取得します。<br>
	 *
	 * @param webMode
	 * @return Webモード情報DTO
	 */
	private WebModeDto findByHomeMode(int webMode) {
		return (WebModeDto)this.webModeDtoMap.get(webMode, this.defaultWebModeDto);
	}
}
