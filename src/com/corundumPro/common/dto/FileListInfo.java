package com.corundumPro.common.dto;

import java.io.File;

import com.corundumPro.common.util.LogUtil;

/**
 * FileListInfoクラス
 * <p>
 * 「ファイルリスト情報」クラス
 * </p>
 * @author androbotics.ltd
 */
public class FileListInfo {
	static final String TAG = "FileListInfo";

	/*
	 * 「ファイル種別(fileType)」設定値
	 */
	/** ファイル種別：ディレクトリ */
	public static final int FILE_TYPE_DIR = 0;

	/** ファイル種別：画像 */
	public static final int FILE_TYPE_IMAGE = 1;

	/** ファイル種別：編集可能ファイル */
	public static final int FILE_TYPE_EDIT_ABLE = 2;

	/** ファイル種別：その他 */
	public static final int FILE_TYPE_OTHER = 3;

	/*
	 * ファイル情報
	 */
	/** ファイル情報：ID */
	private int id;

	/** ファイル情報：アイコンリソースID */
	private int resourceId;

	/** ファイル情報：ファイル名 */
	private String fileName;

	/** ファイル情報：ファイルサイズ */
	private String fileSize;

	/** ファイル情報：最終更新日時 */
	private String timestamp;

	/** ファイル情報：ファイル種別 */
	private int fileType;

	/** ファイル情報：Fileオブジェクト */
	private File file;

	/**
	 * コンストラクタ
	 * <p>
	 * 「ファイルリスト情報」のコンストラクタ。
	 * </p>
	 * @param id ID
	 * @param resourceId アイコンリソースID
	 * @param fileName ファイル名
	 * @param fileSize ファイルサイズ
	 * @param timestamp 最終更新日時
	 * @param fileType ファイル種別
	 * @param file Fileオブジェクト
	 */
	public FileListInfo(int id, int resourceId, String fileName, String fileSize, String timestamp, int fileType, File file) {
		LogUtil.d(TAG, "[IN ] FileListInfo()");
		LogUtil.d(TAG, "id:" + id);
		LogUtil.d(TAG, "resourceId:" + resourceId);
		LogUtil.d(TAG, "fileName:" + fileName);
		LogUtil.d(TAG, "fileSize:" + fileSize);
		LogUtil.d(TAG, "timestamp:" + timestamp);
		LogUtil.d(TAG, "fileType:" + fileType);

		/* ファイル情報保持 */
		this.id = id;
		this.resourceId = resourceId;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.timestamp = timestamp;
		this.fileType = fileType;
		this.file = file;

		LogUtil.d(TAG, "[OUT] FileListInfo()");
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
	 * リソースID設定処理
	 * <p>
	 * アイコンのリソースIDを設定する。
	 * </p>
	 * @param resourceId リソースID
	 */
	public void setResourceId(int resourceId) {
		LogUtil.d(TAG, "[IN ] setResourceId()");
		LogUtil.d(TAG, "resourceId:" + resourceId);
		LogUtil.d(TAG, "[OUT] setResourceId()");
		this.resourceId = resourceId;
	}

	/**
	 * リソースID取得処理
	 * <p>
	 * アイコンのリソースIDを取得する。
	 * </p>
	 * @return resourceId リソースID
	 */
	public int getResourceId() {
		LogUtil.d(TAG, "[IN ] getResourceId()");
		LogUtil.d(TAG, "resourceId:" + this.resourceId);
		LogUtil.d(TAG, "[OUT] getResourceId()");
		return this.resourceId;
	}

	/**
	 * ファイル名設定処理
	 * <p>
	 * ファイル名を設定する。
	 * </p>
	 * @param fileName ファイル名
	 */
	public void setFileName(String fileName) {
		LogUtil.d(TAG, "[IN ] setFileName()");
		LogUtil.d(TAG, "fileName:" + fileName);
		LogUtil.d(TAG, "[OUT] setFileName()");
		this.fileName = fileName;
	}

	/**
	 * フィル名取得処理
	 * <p>
	 * ファイル名を取得する。
	 * </p>
	 * @return fileName ファイル名
	 */
	public String getFileName() {
		LogUtil.d(TAG, "[IN ] getFileName()");
		LogUtil.d(TAG, "fileName:" + this.fileName);
		LogUtil.d(TAG, "[OUT] getFileName()");
		return this.fileName;
	}

	/**
	 * ファイルサイズ設定処理
	 * <p>
	 * ファイルサイズを設定する。
	 * </p>
	 * @param fileSize ファイルサイズ
	 */
	public void setFileSize(String fileSize) {
		LogUtil.d(TAG, "[IN ] setFileSize()");
		LogUtil.d(TAG, "fileSize:" + fileSize);
		LogUtil.d(TAG, "[OUT] setFileSize()");
		this.fileSize = fileSize;
	}

	/**
	 * ファイルサイズ取得処理
	 * <p>
	 * ファイルサイズを取得する。
	 * </p>
	 * @return fileSize ファイルサイズ
	 */
	public String getFileSize() {
		LogUtil.d(TAG, "[IN ] getFileSize()");
		LogUtil.d(TAG, "fileSize:" + this.fileSize);
		LogUtil.d(TAG, "[OUT] getFileSize()");
		return this.fileSize;
	}

	/**
	 * 最終更新日時設定処理
	 * <p>
	 * 最終更新日時を設定する。
	 * </p>
	 * @param timestamp 最終更新日時
	 */
	public void setTimestamp(String timestamp) {
		LogUtil.d(TAG, "[IN ] setTimestamp()");
		LogUtil.d(TAG, "timestamp:" + timestamp);

		this.timestamp = timestamp;

		LogUtil.d(TAG, "[OUT] setTimestamp()");
	}

	/**
	 * 最終更新日時取得処理
	 * <p>
	 * 最終更新日時を取得する。
	 * </p>
	 * @return timestamp 最終更新日時
	 */
	public String getTimestamp() {
		LogUtil.d(TAG, "[IN ] getTimestamp()");
		LogUtil.d(TAG, "timestamp:" + this.timestamp);
		LogUtil.d(TAG, "[OUT] getTimestamp()");
		return this.timestamp;
	}

	/**
	 * ファイル種別設定処理
	 * <p>
	 * ファイル種別を設定する。
	 * </p>
	 * @param fileType ファイル種別
	 */
	public void setFileType(int fileType) {
		LogUtil.d(TAG, "[IN ] setFileType()");
		LogUtil.d(TAG, "fileType:" + fileType);

		this.fileType = fileType;

		LogUtil.d(TAG, "[OUT] setFileType()");
	}

	/**
	 * ファイル種別取得処理
	 * <p>
	 * ファイル種別を取得する。
	 * </p>
	 * @return fileType ファイル種別
	 */
	public int getFileType() {
		LogUtil.d(TAG, "[IN ] getFileType()");
		LogUtil.d(TAG, "fileType:" + this.fileType);
		LogUtil.d(TAG, "[OUT] getFileType()");
		return this.fileType;
	}

	/**
	 * ファイルオブジェクト設定処理
	 * <p>
	 * ファイルオブジェクトを設定する。
	 * </p>
	 * @param file ファイルオブジェクト
	 */
	public void setFile(File file) {
		LogUtil.d(TAG, "[IN ] setFile()");

		this.file = file;

		LogUtil.d(TAG, "[OUT] setFile()");
	}

	/**
	 * ファイルオブジェクト取得処理
	 * <p>
	 * ファイルオブジェクトを取得する。
	 * </p>
	 * @return file ファイルオブジェクト
	 */
	public File getFile() {
		LogUtil.d(TAG, "[IN ] getFile()");
		LogUtil.d(TAG, "[OUT] getFile()");
		return this.file;
	}
}
