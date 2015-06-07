package com.corundumPro.common.dto;

import com.corundumPro.common.util.LogUtil;


/**
 * ShortcutInfoクラス
 * <p>
 * 「ショートカット情報」クラス
 * </p>
 * @author androbotics.ltd
 */
public class ShortcutInfo {
	static final String TAG = "ShortcutInfo";

	/*
	 * カラム(ショートカット情報テーブル)
	 */
	/** DBカラム：ID */
	public static final String COLUMN_ID = "_id";

	/** DBカラム：ショートカット名 */
	public static final String COLUMN_SHORTCUT_NAME = "shortcut_name";

	/** DBカラム：ショートカット名 */
	public static final String COLUMN_HOME_WEB_URL = "home_web_url";

	/*
	 * ショートカット情報
	 */
	/** ショートカット情報：ID */
	protected int id;

	/** ショートカット情報：ショートカット名 */
	protected String shortcut_name;

	/** ショートカット情報：ホームWEB URL */
	protected String home_web_url;

	/**
	 * コンストラクタ
	 * <p>
	 * 「ショートカット情報」のコンストラクタ。
	 * </p>
	 */
	public ShortcutInfo() {
		LogUtil.d(TAG, "[IN ] ShortcutInfo()");

		this.id = 0;
		this.shortcut_name = "";
		this.home_web_url = "";

		LogUtil.d(TAG, "[OUT] ShortcutInfo()");
	}

	/**
	 * ID設定処理
	 * <p>
	 * IDを設定する。
	 * </p>
	 * @param id ID
	 */
	public void setId(int id) {
		LogUtil.d(TAG, "[IN ] setId()");
		LogUtil.d(TAG, "id:" + id);

		this.id = id;

		LogUtil.d(TAG, "[OUT] setId()");
	}

	/**
	 * ID取得処理
	 * <p>
	 * IDを取得する。
	 * </p>
	 * @return id ID
	 */
	public int getId() {
		LogUtil.d(TAG, "[IN ] getId()");
		LogUtil.d(TAG, "id:" + this.id);
		LogUtil.d(TAG, "[OUT] getId()");
		return this.id;
	}

	/**
	 * ショートカット名設定処理
	 * <p>
	 * ショートカット名を設定する。
	 * </p>
	 * @param shortcut_name ショートカット名
	 */
	public void setShortcutName(String shortcut_name) {
		LogUtil.d(TAG, "[IN ] setShortcutName()");
		LogUtil.d(TAG, "shortcut_name:" + shortcut_name);

		this.shortcut_name = shortcut_name;

		LogUtil.d(TAG, "[OUT] setShortcutName()");
	}

	/**
	 * ショートカット名取得処理
	 * <p>
	 * ショートカット名を取得する。
	 * </p>
	 * @return shortcut_id ショートカット名
	 */
	public String getShortcutName() {
		LogUtil.d(TAG, "[IN ] getShortcutName()");
		LogUtil.d(TAG, "shortcut_name:" + this.shortcut_name);
		LogUtil.d(TAG, "[OUT] getShortcutName()");
		return this.shortcut_name;
	}

	/**
	 * ホームURL設定処理
	 * <p>
	 * ホームURLを設定する。
	 * </p>
	 * @param home_web_url ホームURL
	 */
	public void setHomeWebUrl(String home_web_url) {
		LogUtil.d(TAG, "[IN ] setHomeWebUrl()");
		LogUtil.d(TAG, "home_web_url:" + home_web_url);

		this.home_web_url = home_web_url;

		LogUtil.d(TAG, "[OUT] setHomeWebUrl()");
	}

	/**
	 * ホームURL取得処理
	 * <p>
	 * ホームURLを取得する。
	 * </p>
	 * @return home_web_url ホームURL
	 */
	public String getHomeWebUrl() {
		LogUtil.d(TAG, "[IN ] getHomeWebUrl()");
		LogUtil.d(TAG, "home_web_url:" + this.home_web_url);
		LogUtil.d(TAG, "[OUT] getHomeWebUrl()");
		return this.home_web_url;
	}
}

