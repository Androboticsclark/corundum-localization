/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.function;

import android.content.SharedPreferences.Editor;
import android.util.SparseArray;
import android.view.View;

import com.corundumPro.common.constant.Const;
import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.dao.DBAdapter;
import com.corundumPro.common.dto.HomeSettingDto;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.common.util.StringUtil;

/**
 * ホーム画面設定ファンクション.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class HomeSettingFunction {
	static final String TAG = "HomeSettingFunction";

	/** 現ホーム画面設定情報DTO */
	private HomeSettingDto currentHomeSettingDto;
	/** ホーム画面設定DTOマップ */
	private SparseArray<HomeSettingDto> homeSettingDtoMap = new SparseArray<HomeSettingDto>();
	/** デフォルトホーム画面設定DTO */
	private HomeSettingDto defaultHomeSettingDto;

	/**
	 * コンストラクタ<br>
	 *
	 */
	public HomeSettingFunction() {
		LogUtil.d(TAG, "[IN ] HomeSettingFunction()");

		// デフォルトホーム画面設定情報DTO作成
		this.defaultHomeSettingDto = new HomeSettingDto();
		this.defaultHomeSettingDto.setHomeMode(DBAdapter.HOME_MODE_LIST);
		this.defaultHomeSettingDto.setRadioId(-1);
		this.defaultHomeSettingDto.setHomeUrl("");
		this.defaultHomeSettingDto.setEnableEditUrl(false);
		this.defaultHomeSettingDto.setVisiblePreview(View.GONE);
		this.defaultHomeSettingDto.setProtocol("");

		LogUtil.d(TAG, "[OUT] HomeSettingFunction()");
	}

	/**
	 * ホーム画面設定情報を保存します。
	 *
	 * @param editor
	 */
	public void save(Editor editor) {
		editor.putInt(SystemInfo.KEY_HOME_MODE, this.getCurrentHomeMode());
		editor.putInt(SystemInfo.KEY_CURRENT_HOME_MODE, this.getCurrentHomeMode());
		editor.putString(SystemInfo.KEY_HOME_HTML_PATH, this.getHomeUrl(DBAdapter.HOME_MODE_URL));
		editor.putString(SystemInfo.KEY_HOME_APP_PATH, this.getHomeUrl(DBAdapter.HOME_MODE_APP));

		editor.commit();
	}

	/**
	 * ホーム画面設定情報を登録します。<br>
	 *
	 * @param radioId
	 * @param homeMode
	 * @param homeUrl
	 * @param protocol
	 * @param isDefault デフォルトのホーム画面設定情報とする場合、trueを指定
	 */
	public void putHomeSetting(int radioId, int homeMode, String homeUrl, String protocol, boolean isDefault) {
		LogUtil.d(TAG, "[IN ] putHomeSetting()");

		boolean isEnableEditUrl = false;
		int visiblePreview = View.GONE;

		// ホームモードによる表示方法を設定
		if (homeMode == DBAdapter.HOME_MODE_LIST) {
			isEnableEditUrl = false;
			visiblePreview = View.GONE;
		} else if (homeMode == DBAdapter.HOME_MODE_URL) {
			isEnableEditUrl = true;
			visiblePreview = View.VISIBLE;
		} else if (homeMode == DBAdapter.HOME_MODE_APP) {
			isEnableEditUrl = true;
// 2015.05.07 H.Yamazaki 修正開始
			visiblePreview = View.VISIBLE;
// 2015.05.07 H.Yamazaki 修正終了
		} else {
			LogUtil.w(TAG, "Invalid homeMode:" + homeMode);
			LogUtil.w(TAG, "[OUT] putHomeSetting()");
			return;
		}

		// ホーム画面設定情報DTO作成
		HomeSettingDto dto = new HomeSettingDto();
		dto.setHomeMode(homeMode);
		dto.setRadioId(radioId);
		dto.setHomeUrl(homeUrl);
		dto.setEnableEditUrl(isEnableEditUrl);
		dto.setVisiblePreview(visiblePreview);
		dto.setProtocol(protocol);

		if (isDefault) {
			this.defaultHomeSettingDto = dto;
		}

		this.homeSettingDtoMap.put(radioId, dto);

		LogUtil.d(TAG, dto.toString());
		LogUtil.d(TAG, "[OUT] putHomeSetting()");
	}

	/**
	 * 現在のホームモードを設定します。<br>
	 *
	 * @param homeMode
	 */
	public void changeCurrentHomeSettingByHomeMode(int homeMode) {
		LogUtil.d(TAG, "[IN ] changeCurrentHomeSettingByHomeMode()");

		this.currentHomeSettingDto = findByHomeMode(homeMode);

		LogUtil.d(TAG, currentHomeSettingDto.toString());
		LogUtil.d(TAG, "[OUT] changeCurrentHomeSettingByHomeMode()");
	}

	/**
	 * 現在のホーム画面設定を変更します。<br>
	 *
	 * @param radioId
	 */
	public void changeCurrentHomeSettingByRadioId(int radioId) {
		LogUtil.d(TAG, "[IN ] changeCurrentHomeSettingByRadioId()");

		this.currentHomeSettingDto = findByRadioId(radioId);

		LogUtil.d(TAG, currentHomeSettingDto.toString());
		LogUtil.d(TAG, "[OUT] changeCurrentHomeSettingByRadioId()");
	}

	/**
	 * 現在のホームモードを取得します。<br>
	 *
	 * @return homeMode
	 */
	public int getCurrentHomeMode() {
		LogUtil.d(TAG, "[IN ] getCurrentHomeMode()");

		int ret = this.currentHomeSettingDto.getHomeMode();

		LogUtil.d(TAG, "return:" + String.valueOf(ret));
		LogUtil.d(TAG, "[OUT] getCurrentHomeMode()");

		return ret;
	}

	/**
	 * 選択するラジオボタンIDを取得します。<br>
	 *
	 * @return 選択するラジオボタンID
	 */
	public int getCurrentRadioId() {
		LogUtil.d(TAG, "[IN ] getCurrentRadioId()");

		int ret = this.currentHomeSettingDto.getRadioId();

		LogUtil.d(TAG, "return:" + String.valueOf(ret));
		LogUtil.d(TAG, "[OUT] getCurrentRadioId()");

		return ret;
	}

	/**
	 * 現在のホームURLを設定します。<br>
	 *
	 * @param homeUrl
	 */
	public void setCurrentHomeUrl(String homeUrl) {
		LogUtil.d(TAG, "[IN ] setCurrentHomeUrl()");

		String url = homeUrl;
		if (StringUtil.isEmptyWithAboutBlank(homeUrl)) {
			url = "";
		}
		this.currentHomeSettingDto.setHomeUrl(url);

		LogUtil.d(TAG, currentHomeSettingDto.toString());
		LogUtil.d(TAG, "[OUT] setCurrentHomeUrl()");
	}

	/**
	 * 現在のホームURLを取得します。<br>
	 *
	 * @return homeUrl
	 */
	public String getCurrentHomeUrl() {
		LogUtil.d(TAG, "[IN ] getCurrentHomeUrl()");

		String homeUrl = this.currentHomeSettingDto.getHomeUrl();
		if (StringUtil.isEmpty(homeUrl)) {
			homeUrl = this.currentHomeSettingDto.getProtocol();
		}

		LogUtil.d(TAG, "return:" + homeUrl);
		LogUtil.d(TAG, "[OUT] getCurrentHomeUrl()");

		return homeUrl;
	}

	/**
	 * ホームURLを取得します。<br>
	 *
	 * @param homeMode
	 * @return homeUrl
	 */
	public String getHomeUrl(int homeMode) {
		LogUtil.d(TAG, "[IN ] getHomeUrl()");

		HomeSettingDto dto = findByHomeMode(homeMode);
		String homeUrl = dto.getHomeUrl();

		if (StringUtil.isEmpty(homeUrl)) {
			homeUrl = dto.getProtocol();
		}

		LogUtil.d(TAG, "return:" + homeUrl);
		LogUtil.d(TAG, "[OUT] getHomeUrl()");

		return homeUrl;
	}

	/**
	 * URL編集テキストボックスの可否を取得します。<br>
	 *
	 * @return URL編集テキストボックスの可否(可：true, 不可：false)
	 */
	public boolean isEnableEditUrl() {
		LogUtil.d(TAG, "[IN ] isEnableEditUrl()");

		boolean ret = this.currentHomeSettingDto.isEnableEditUrl();

		LogUtil.d(TAG, "return:" + String.valueOf(ret));
		LogUtil.d(TAG, "[OUT] isEnableEditUrl()");

		return ret;
	}

	/**
	 * 起動アイコン設定情報URLをもとにホームモードを決定します。<br>
	 *
	 * @param url 起動アイコン設定情報URL
	 * @return ホームモード
	 */
	public int getHomeModeFromShortcutUrl(String url) {
		LogUtil.d(TAG, "[IN ] getHomeModeFromShortcutUrl()");

		int ret = DBAdapter.HOME_MODE_URL;
		if (StringUtil.isEmpty(url)) {
			// URLがブランクの場合、アプリリストとする
			ret = DBAdapter.HOME_MODE_LIST;

		} else if (url.startsWith(Const.PROTOCOL_FILE)) {
			// URLがfileプロトコルの場合、アプリを指定とする
			ret = DBAdapter.HOME_MODE_APP;
		}

		LogUtil.d(TAG, "return:" + String.valueOf(ret));
		LogUtil.d(TAG, "[OUT] getHomeModeFromShortcutUrl()");

		// 上記以外は、URLを指定とする
		return ret;
	}

	/**
	 * プレビューの表示可否を取得します。<br>
	 *
	 * @return プレビューの表示可否(可：View.VISIBLE, 不可：View.GONE)
	 */
	public int getVisiblePreview() {
		LogUtil.d(TAG, "[IN ] getVisiblePreview()");

		int ret = this.currentHomeSettingDto.getVisiblePreview();

		LogUtil.d(TAG, "return:" + String.valueOf(ret));
		LogUtil.d(TAG, "[OUT] getVisiblePreview()");

		return ret;
	}

	/**
	 * ラジオボタンIDで該当のホーム画面設定DTOを取得します。<br>
	 *
	 * @param radioId
	 * @return ホーム画面設定DTO
	 */
	private HomeSettingDto findByRadioId(int radioId) {
		return (HomeSettingDto)this.homeSettingDtoMap.get(radioId, this.defaultHomeSettingDto);
	}

	/**
	 * ホームモードで該当のホーム画面設定DTOを取得します。<br>
	 *
	 * @param homeMode
	 * @return ホーム画面設定DTO
	 */
	private HomeSettingDto findByHomeMode(int homeMode) {
		int radioId = 0;
		HomeSettingDto dto;
		for (int i = 0; i < this.homeSettingDtoMap.size(); i++) {
			radioId = homeSettingDtoMap.keyAt(i);
			dto = findByRadioId(radioId);
			if (dto != null) {
				if (dto.getHomeMode() == homeMode) {
					return dto;
				}
			}
		}
		return this.defaultHomeSettingDto;
	}
}
