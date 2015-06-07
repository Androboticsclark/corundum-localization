package com.corundumPro.features.appsmanager;

import com.corundumPro.common.constant.Const;
import com.corundumPro.common.constant.EnumExtension;
import com.corundumPro.common.util.LogUtil;


/**
 * ApplicationListInfoクラス
 * <p>
 * 「アプリケーションリスト情報」クラス
 * </p>
 * @author androbotics.ltd
 */
public class ApplicationListInfo {
	static final String TAG = "ApplicationListInfo";

	/*
	 * アプリケーションリスト情報
	 */
	/** アプリケーションリスト情報：ID */
	private int id;

	/** アプリケーションリスト情報：アイコンファイルパス */
	private String iconPath;

	/** アプリケーションリスト情報：アプリケーションrootパス */
	private String rootPath;

	/** アプリケーションリスト情報：アプリケーション名(フォルダ名) */
	private String applicationName;

	/** アプリケーションリスト情報：詳細情報 */
	private String description;

	/** アプリケーションリスト情報：ルートHTMLパス */
	private String indexHtmlPath;

	/** バージョン */
	private String version;

	/** 公開レベル */
	private String publishedLevel;


	/**
	 * コンストラクタ
	 * <p>
	 * 「アプリケーションリスト情報」のコンストラクタ。
	 * </p>
	 * @param id ID
	 * @param iconPath アイコンファイルパス
	 * @param rootPath アプリケーションrootパス
	 * @param applicationName アプリケーション名
	 * @param description 詳細情報
	 * @param indexHtmlPath ルートHTMLパス
	 * @param version バージョン
	 * @param publishedLevel 公開レベル
	 */
	public ApplicationListInfo(int id, String iconPath, String rootPath, String applicationName, String description, String indexHtmlPath, String version, String publishedLevel) {
		LogUtil.d(TAG, "[IN ] ApplicationListInfo()");
		LogUtil.d(TAG, "id:" + id);
		LogUtil.d(TAG, "iconPath:" + iconPath);
		LogUtil.d(TAG, "rootPath:" + rootPath);
		LogUtil.d(TAG, "applicationName:" + applicationName);
		LogUtil.d(TAG, "description:" + description);
		LogUtil.d(TAG, "indexHtmlPath:" + indexHtmlPath);

		/* ファイル情報保持 */
		this.id = id;
		this.iconPath = iconPath;
		this.rootPath = rootPath;
		this.applicationName = applicationName;
		this.description = description;
		this.indexHtmlPath = indexHtmlPath;
		this.version = version;
		this.publishedLevel = publishedLevel;

		LogUtil.d(TAG, "[OUT] ApplicationListInfo()");
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
	 * アイコンファイルパス設定処理
	 * <p>
	 * アイコンファイルパスを設定する。
	 * </p>
	 * @param iconPath アイコンファイルパス
	 */
	public void setIconPath(String iconPath) {
		LogUtil.d(TAG, "[IN ] setIconPath()");
		LogUtil.d(TAG, "iconPath:" + iconPath);
		LogUtil.d(TAG, "[OUT] setIconPath()");
		this.iconPath = iconPath;
	}

	/**
	 * アイコンファイルパス取得処理
	 * <p>
	 * アイコンファイルパスを取得する。
	 * </p>
	 * @return iconPath アイコンファイルパス
	 */
	public String getIconPath() {
		LogUtil.d(TAG, "[IN ] getIconPath()");
		LogUtil.d(TAG, "iconPath:" + this.iconPath);
		LogUtil.d(TAG, "[OUT] getIconPath()");
		return this.iconPath;
	}

	/**
	 * アプリケーションrootパス設定処理
	 * <p>
	 * アプリケーションrootパスを設定する。
	 * </p>
	 * @param rootPath アプリケーションrootパス
	 */
	public void setRootPath(String rootPath) {
		LogUtil.d(TAG, "[IN ] setRootPath()");
		LogUtil.d(TAG, "rootPath:" + rootPath);
		LogUtil.d(TAG, "[OUT] setRootPath()");
		this.rootPath = rootPath;
	}

	/**
	 * アプリケーションrootパス取得処理
	 * <p>
	 * アプリケーションrootパスを取得する。
	 * </p>
	 * @return rootPath アプリケーションrootパス
	 */
	public String getRootPath() {
		LogUtil.d(TAG, "[IN ] getRootPath()");
		LogUtil.d(TAG, "iconPath:" + this.rootPath);
		LogUtil.d(TAG, "[OUT] getRootPath()");
		return this.rootPath;
	}

	/**
	 * アプリケーション名設定処理
	 * <p>
	 * アプリケーション名を設定する。
	 * </p>
	 * @param applicationName アプリケーション名
	 */
	public void setApplicationName(String applicationName) {
		LogUtil.d(TAG, "[IN ] setApplicationName()");
		LogUtil.d(TAG, "applicationName:" + applicationName);
		LogUtil.d(TAG, "[OUT] setApplicationName()");
		this.applicationName = applicationName;
	}

	/**
	 * アプリケーション名取得処理
	 * <p>
	 * アプリケーション名を取得する。
	 * </p>
	 * @return applicationName アプリケーション名
	 */
	public String getApplicationName() {
		LogUtil.d(TAG, "[IN ] getApplicationName()");
		LogUtil.d(TAG, "applicationName:" + this.applicationName);
		LogUtil.d(TAG, "[OUT] getApplicationName()");
		return this.applicationName;
	}

	/**
	 * 詳細情報設定処理
	 * <p>
	 * 詳細情報を設定する。
	 * </p>
	 * @param description 詳細情報
	 */
	public void setDescription(String description) {
		LogUtil.d(TAG, "[IN ] setDescription()");
		LogUtil.d(TAG, "description:" + description);
		LogUtil.d(TAG, "[OUT] setDescription()");
		this.description = description;
	}

	/**
	 * 詳細情報取得処理
	 * <p>
	 * 詳細情報を取得する。
	 * </p>
	 * @return description 詳細情報
	 */
	public String getDescription() {
		LogUtil.d(TAG, "[IN ] getDescription()");
		LogUtil.d(TAG, "description:" + this.description);
		LogUtil.d(TAG, "[OUT] getDescription()");
		return this.description;
	}

	/**
	 * ルートHTMLパス設定処理
	 * <p>
	 * ルートHTMLパスを設定する。
	 * </p>
	 * @param indexHtmlPath ルートHTMLパス
	 */
	public void setIndexHtmlPath(String indexHtmlPath) {
		LogUtil.d(TAG, "[IN ] setIndexHtmlPath()");
		LogUtil.d(TAG, "indexHtmlPath:" + indexHtmlPath);
		LogUtil.d(TAG, "[OUT] setIndexHtmlPath()");
		this.indexHtmlPath = indexHtmlPath;
	}

	/**
	 * ルートHTMLパス取得処理
	 * <p>
	 * ルートHTMLパスを取得する。
	 * </p>
	 * @return indexHtmlPath ルートHTMLパス
	 */
	public String getIndexHtmlPath() {
		LogUtil.d(TAG, "[IN ] getIndexHtmlPath()");
		LogUtil.d(TAG, "indexHtmlPath:" + this.indexHtmlPath);
		LogUtil.d(TAG, "[OUT] getIndexHtmlPath()");
		return this.indexHtmlPath;
	}

	/**
	 * バージョン取得処理
	 * <p>
	 * バージョンを取得する。
	 * </p>
	 * @return version バージョン
	 */
	public String getVersion() {
		LogUtil.d(TAG, "[IN ] getVersion()");
		LogUtil.d(TAG, "version:" + this.version);
		LogUtil.d(TAG, "[OUT] getVersion()");
		return this.version;
	}

	/**
	 * 公開レベル取得処理
	 * <p>
	 * 公開レベルを取得する。
	 * </p>
	 * @return publishedLevel 公開レベル
	 */
	public String getPublishedLevel() {
		LogUtil.d(TAG, "[IN ] getPublishedLevel()");
		LogUtil.d(TAG, "publishedLevel:" + this.publishedLevel);
		LogUtil.d(TAG, "[OUT] getPublishedLevel()");
		return this.publishedLevel;
	}

	/**
	 * zipファイルパス取得処理
	 * <p>
	 * zipファイルパスを取得する。
	 * </p>
	 * @return zipファイルパス
	 */
	public String getZipPath() {
		return getRootPath() + Const.SEPARATOR_EXTENSION + EnumExtension.EXT_ZIP.getExtension();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("id:").append(id);
		sb.append(Const.SEPARATOR_COMMA);
		sb.append("iconPath:").append(iconPath);
		sb.append(Const.SEPARATOR_COMMA);
		sb.append("rootPath:").append(rootPath);
		sb.append(Const.SEPARATOR_COMMA);
		sb.append("applicationName:").append(applicationName);
		sb.append(Const.SEPARATOR_COMMA);
		sb.append("description:").append(description);
		sb.append(Const.SEPARATOR_COMMA);
		sb.append("indexHtmlPath:").append(indexHtmlPath);
		sb.append(Const.SEPARATOR_COMMA);

		return sb.toString();
	}
}
