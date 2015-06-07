package com.corundumPro.common.dto;

import com.corundumPro.common.util.LogUtil;


/**
 * WhiteListInfoクラス
 * <p>
 * 「ホワイトリスト情報情報」クラス
 * </p>
 * @author androbotics.ltd
 */
public class WhiteListInfo {
	static final String TAG = "WhiteListInfo";

	/*
	 * カラム(ホワイトリスト情報テーブル)
	 */
	/** DBカラム：ID */
	public static final String COLUMN_ID = "_id";

	/** DBカラム：ショートカットID */
	public static final String COLUMN_SHORTCUT_ID = "shortcut_id";

	/** DBカラム：ホワイトリストタイトル */
	public static final String COLUMN_WHITE_LIST_TITLE = "white_list_title";

	/** DBカラム：ホワイトリストURL */
	public static final String COLUMN_WHITE_LIST_URL = "white_list_url";

	/** DBカラム：ホワイトリストモード */
	public static final String COLUMN_WHITE_LIST_MODE = "white_list_mode";

	/*
	 * ホワイトリスト情報
	 */
	/** ホワイトリスト情報：ID */
	private int id;

	/** ホワイトリスト情報：ショートカットID */
	private int shortcutId;

	/** ホワイトリスト情報：ホワイトリストタイトル */
	private String white_list_title;

	/** ホワイトリスト情報：ホワイトリストURL */
	private String white_list_url;

	/** ホワイトリスト情報：ホワイトリストモード */
	private int white_list_mode;

	/**
	 * コンストラクタ
	 * <p>
	 * 「システム情報」のコンストラクタ。
	 * </p>
	 */
	public WhiteListInfo() {
		LogUtil.d(TAG, "[IN ] WhiteListInfo()");
		LogUtil.d(TAG, "[OUT] WhiteListInfo()");
	}

	/**
	 * コンストラクタ
	 * <p>
	 * 「システム情報」のコンストラクタ。
	 * </p>
	 * @param id ID
	 * @param title ホワイトリストタイトル
	 * @param url ホワイトリストURL
	 * @param mode ホワイトリストモード
	 */
	public WhiteListInfo(int id, String title, String url, int mode) {
		LogUtil.d(TAG, "[IN ] WhiteListInfo()");
		LogUtil.d(TAG, "id:" + id);
		LogUtil.d(TAG, "title:" + title);
		LogUtil.d(TAG, "url:" + url);
		LogUtil.d(TAG, "mode:" + mode);

		this.id = id;
		white_list_title = title;
		white_list_url = url;
		white_list_mode = mode;

		LogUtil.d(TAG, "[OUT] WhiteListInfo()");
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
	 * ショートカットID設定処理
	 * <p>
	 * ショートカットIDを設定する。
	 * </p>
	 * @param shortcutId ショートカットID
	 */
	public void setShortcutId(int shortcutId) {
		LogUtil.d(TAG, "[IN ] setShortcutId()");
		LogUtil.d(TAG, "shortcutId:" + shortcutId);

		this.shortcutId = shortcutId;

		LogUtil.d(TAG, "[OUT] setShortcutId()");
	}

	/**
	 * ショートカットID取得処理
	 * <p>
	 * ショートカットIDを取得する。
	 * </p>
	 * @return shortcutId ショートカットID
	 */
	public int getShortcutId() {
		LogUtil.d(TAG, "[IN ] getShortcutId()");
		LogUtil.d(TAG, "shortcutId:" + this.shortcutId);
		LogUtil.d(TAG, "[OUT] getShortcutId()");
		return this.shortcutId;
	}

	/**
	 * ホワイトリストタイトル設定処理
	 * <p>
	 * ホワイトリストタイトルを設定する。
	 * </p>
	 * @param white_list_title ホワイトリストタイトル
	 */
	public void setWhiteListTitle(String white_list_title) {
		LogUtil.d(TAG, "[IN ] setWhiteListTitle()");
		LogUtil.d(TAG, "white_list_title:" + white_list_title);

		this.white_list_title = white_list_title;

		LogUtil.d(TAG, "[OUT] setWhiteListTitle()");
	}

	/**
	 * ホワイトリストタイトル取得処理
	 * <p>
	 * ホワイトリストタイトルを取得する。
	 * </p>
	 * @return white_list_title ホワイトリストタイトル
	 */
	public String getWhiteListTitle() {
		LogUtil.d(TAG, "[IN ] getWhiteListTitle()");
		LogUtil.d(TAG, "white_list_title:" + white_list_title);
		LogUtil.d(TAG, "[OUT] getWhiteListTitle()");
		return this.white_list_title;
	}

	/**
	 * ホワイトリストURL設定処理
	 * <p>
	 * ホワイトリストURLを設定する。
	 * </p>
	 * @param white_list_url ホワイトリストURL
	 */
	public void setWhiteListUrl(String white_list_url) {
		LogUtil.d(TAG, "[IN ] setWhiteListUrl()");
		LogUtil.d(TAG, "white_list_url:" + white_list_url);

		this.white_list_url = white_list_url;

		LogUtil.d(TAG, "[OUT] setWhiteListUrl()");
	}

	/**
	 * ホワイトリストURL取得処理
	 * <p>
	 * ホワイトリストURLを取得する。
	 * </p>
	 * @return white_list_url ホワイトリストURL
	 */
	public String getWhiteListUrl() {
		LogUtil.d(TAG, "[IN ] getWhiteListUrl()");
		LogUtil.d(TAG, "white_list_url:" + this.white_list_url);
		LogUtil.d(TAG, "[OUT] getWhiteListUrl()");
		return this.white_list_url;
	}

	/**
	 * ホワイトリストモード設定処理
	 * <p>
	 * ホワイトリストモードを設定する。
	 * </p>
	 * @param white_list_mode ホワイトリストモード
	 */
	public void setWhiteListMode(int white_list_mode) {
		LogUtil.d(TAG, "[IN ] setWhiteListMode()");
		LogUtil.d(TAG, "white_list_mode:" + white_list_mode);

		this.white_list_mode = white_list_mode;

		LogUtil.d(TAG, "[OUT] setWhiteListMode()");
	}

	/**
	 * ホワイトリストモード取得処理
	 * <p>
	 * ホワイトリストモードを取得する。
	 * </p>
	 * @return white_list_mode ホワイトリストモード
	 */
	public int getWhiteListMode() {
		LogUtil.d(TAG, "[IN ] getWhiteListMode()");
		LogUtil.d(TAG, "white_list_mode:" + this.white_list_mode);
		LogUtil.d(TAG, "[OUT] getWhiteListMode()");
		return this.white_list_mode;
	}
}
