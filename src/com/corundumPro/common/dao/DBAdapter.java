package com.corundumPro.common.dao;

import com.corundumPro.common.dto.ShortcutInfo;
import com.corundumPro.common.dto.WhiteListInfo;
import com.corundumPro.common.util.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * DBAdapterクラス
 * <p>
 * DBアダプタクラス
 * </p>
 * @author androbotics.ltd
 */
public class DBAdapter {
	static final String TAG = "DBAdapter";

	/** DBファイル名 */
	public static final String DATABASE_NAME = "database.db";

	/** DBバージョン */
	public static final int DATABASE_VERSION = 10;

	/*
	 * テーブル名
	 */
	/** DBテーブル：ショートカット情報テーブル */
	public static final String TABLE_SHORTCUT_INFO = "shortcut_info";

	/** DBテーブル：ホワイトリスト情報テーブル */
	public static final String TABLE_WHITE_LIST_INFO = "white_list_info";

	/*
	 * 「WEB表示モード(web_mode)」設定値
	 */
	/** WEB表示モード：フルスクリーンモード */
	public static final int WEB_MODE_FULL = 0;

	/** WEB表示モード：オペレーションモード */
	public static final int WEB_MODE_OPERATION = 1;

	/** WEB表示モード：管理者用モード */
	public static final int WEB_MODE_ADMIN = 2;

	/*
	 * 「ホーム画面モード(home_mode)」設定値
	 */
	/** ホーム画面モード：アプリリスト */
	public static final int HOME_MODE_LIST = 0;

	/** ホーム画面モード：URLを指定 */
	public static final int HOME_MODE_URL = 1;

	/** ホーム画面モード：アプリを指定 */
	public static final int HOME_MODE_APP = 2;

	/*
	 * 「ホワイトリストモード(white_list_mode)」設定値
	 */
	/** ホワイトリストモード：URL完全一致 */
	public static final int WHITE_LIST_MODE_FULL_URL = 0;

	/** ホワイトリストモード：URL以下全て */
	public static final int WHITE_LIST_MODE_URL = 1;

	/*
	 * assetファイル名
	 */
	/** assetファイル：ショートカットアイコン(初期値) */
	public static final String SHORTCUT_ICON = "shortcut_icon.png";

	/** assetファイル：「コランダムとは？」PDF */
	public static final String INTRODUCTION_PDF = "preview.pdf";

	/** assetファイル：起動画面画像 */
	public static final String START_IMAGE = "start_image.png";

	/** assetファイル：タブレット用コランダムホームHTML */
	public static final String LOCAL_HTMLH = "indexh.html";

	/** assetファイル：スマートフォン用コランダムホームHTML */
	public static final String LOCAL_HTMLM = "indexm.html";

	/*
	 * DB
	 */
	/** ヘルパー */
	protected DBHelper dbHelper;

	/** DB */
	protected SQLiteDatabase db;

	/**
	 * コンストラクタ
	 * <p>
	 * DBアダプタのコンストラクタ。
	 * </p>
	 * @param context コンテキスト
	 */
	public DBAdapter(Context context){
		LogUtil.d(TAG, "[IN ] DBAdapter()");

		/* DBヘルパー作成 */
		dbHelper = new DBHelper(context);

		LogUtil.d(TAG, "[OUT] DBAdapter()");
	}

	/**
	 * 「ショートカット情報」テーブル追加処理
	 * <p>
	 * 「ショートカット情報」テーブルの追加を行う。
	 * </p>
	 * @param shortcutName ショートカット名
	 * @param homeUrl ホームURL
	 * @return ショートカットID
	 */
	public int insertShortcutInfo(String shortcutName, String homeUrl) {
		LogUtil.d(TAG, "[IN ] insertShortcutInfo()");
		LogUtil.d(TAG, "shortcutName:" + shortcutName);
		LogUtil.d(TAG, "homeUrl:" + homeUrl);

		/* 「ショートカット情報」データ設定 */
		ContentValues shrotcut_info_value = new ContentValues();
		shrotcut_info_value.put(ShortcutInfo.COLUMN_SHORTCUT_NAME, shortcutName);
		shrotcut_info_value.put(ShortcutInfo.COLUMN_HOME_WEB_URL, homeUrl);

		/* DB登録 */
		long shortcut_id = db.insert(TABLE_SHORTCUT_INFO, "", shrotcut_info_value);

		LogUtil.d(TAG, "shortcut_id:" + shortcut_id);
		LogUtil.d(TAG, "[OUT] insertShortcutInfo()");
		return (int)shortcut_id;
	}

	/**
	 * 「ショートカット情報」取得処理
	 * <p>
	 * 「ショートカット情報」の取得を行う。<br>
	 * 引数「shortcutId」をDB検索のキーとする。<br>
	 * 引数「shortcutInfo」にDBから取得した「ショートカット情報」を設定する。
	 * </p>
	 * @param shortcutId ショートカットID(DB検索キー)
	 * @param shortcutInfo 「ショートカット情報」
	 */
	public void getShortcutInfo(int shortcutId, ShortcutInfo shortcutInfo) {
		LogUtil.d(TAG, "[IN ] getShortcutInfo()");
		LogUtil.d(TAG, "shortcutId:" + shortcutId);

		/* カーソル取得 */
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SHORTCUT_INFO + " WHERE _id = " + String.valueOf(shortcutId), null);
		cursor.moveToFirst();

		/* 「ショートカット情報」設定 */
		shortcutInfo.setId(cursor.getInt(cursor.getColumnIndex(ShortcutInfo.COLUMN_ID)));
		shortcutInfo.setShortcutName(cursor.getString(cursor.getColumnIndex(ShortcutInfo.COLUMN_SHORTCUT_NAME)));
		shortcutInfo.setHomeWebUrl(cursor.getString(cursor.getColumnIndex(ShortcutInfo.COLUMN_HOME_WEB_URL)));

		/* カーソル解放 */
		cursor.close();

		LogUtil.d(TAG, "[OUT] getShortcutInfo()");
	}

	/**
	 * 「ショートカット情報」更新処理
	 * <p>
	 * 「ショートカット情報」の更新を行う。
	 * </p>
	 * @param shortcutInfo 「ショートカット情報」
	 */
	public void updateShortcutInfo(ShortcutInfo shortcutInfo) {
		LogUtil.d(TAG, "[IN ] updateShortcutInfo()");

		/* 「ショートカット情報」テーブル更新 */
		db.execSQL("UPDATE " + TABLE_SHORTCUT_INFO + " SET "
				+ ShortcutInfo.COLUMN_SHORTCUT_NAME + " = \"" + shortcutInfo.getShortcutName() + "\" , "
				+ ShortcutInfo.COLUMN_HOME_WEB_URL + " = \""  + shortcutInfo.getHomeWebUrl() + "\""
				+ " WHERE " + ShortcutInfo.COLUMN_ID + " = "  + shortcutInfo.getId() + ";");

		LogUtil.d(TAG, "[OUT] updateShortcutInfo()");
	}

	/**
	 * 「ホワイトリスト情報」テーブル追加処理
	 * <p>
	 * 「ホワイトリスト情報」テーブルの追加を行う。
	 * </p>
	 * @param whiteListInfo 「ホワイトリスト情報」
	 */
	public void insertWhiteListInfo(WhiteListInfo whiteListInfo) {
		LogUtil.d(TAG, "[IN ] insertWhiteListInfo()");

		/* 「ホワイトリスト情報」データ設定 */
		ContentValues white_list_info_value = new ContentValues();
		white_list_info_value.put(WhiteListInfo.COLUMN_WHITE_LIST_URL, whiteListInfo.getWhiteListUrl());
		white_list_info_value.put(WhiteListInfo.COLUMN_WHITE_LIST_TITLE, whiteListInfo.getWhiteListTitle());
		white_list_info_value.put(WhiteListInfo.COLUMN_WHITE_LIST_MODE, whiteListInfo.getWhiteListMode());

		/* DB登録 */
		db.insert(TABLE_WHITE_LIST_INFO, "", white_list_info_value);

		LogUtil.d(TAG, "[OUT] insertWhiteListInfo()");
	}

	/**
	 * 「ホワイトリスト情報」更新処理
	 * <p>
	 * 「ホワイトリスト情報」の更新を行う。
	 * </p>
	 * @param whiteListInfo 「ホワイトリスト情報」
	 */
	public void updateWhiteListInfo(WhiteListInfo whiteListInfo) {
		LogUtil.d(TAG, "[IN ] updateWhiteListInfo()");

		/* 「ホワイトリスト情報」テーブル更新 */
		db.execSQL("UPDATE " + TABLE_WHITE_LIST_INFO + " SET "
				+ WhiteListInfo.COLUMN_WHITE_LIST_URL + " = \"" + whiteListInfo.getWhiteListUrl() + "\","
				+ WhiteListInfo.COLUMN_WHITE_LIST_TITLE + " = \"" + whiteListInfo.getWhiteListTitle() + "\","
				+ WhiteListInfo.COLUMN_WHITE_LIST_MODE + " = " + whiteListInfo.getWhiteListMode()
				+ " WHERE " + WhiteListInfo.COLUMN_ID + " = " + whiteListInfo.getId() + ";");

		LogUtil.d(TAG, "[OUT] updateWhiteListInfo()");
	}

	/**
	 * 「ホワイトリスト情報」取得処理
	 * <p>
	 * 「ホワイトリスト情報」の取得を行う。<br>
	 * 引数「id」をDB取得のキーとする。<br>
	 * 引数「whiteListInfo」にDBから取得した「ホワイトリスト情報」を設定する。
	 * </p>
	 * @param id ID(DB検索キー)
	 * @param whiteListInfo 「ホワイトリスト情報」
	 */
	public void getWhiteListInfo(int id, WhiteListInfo whiteListInfo) {
		LogUtil.d(TAG, "[IN ] getWhiteListInfo()");
		LogUtil.d(TAG, "id:" + id);

		/* カーソル取得 */
		Cursor cursor = db.query(TABLE_WHITE_LIST_INFO, null, WhiteListInfo.COLUMN_ID + " = " + String.valueOf(id), null, null, null, null);
		cursor.moveToFirst();

		/* 「ホワイトリスト情報」設定 */
		whiteListInfo.setId(cursor.getInt(cursor.getColumnIndex(WhiteListInfo.COLUMN_ID)));
		whiteListInfo.setWhiteListTitle(cursor.getString(cursor.getColumnIndex(WhiteListInfo.COLUMN_WHITE_LIST_TITLE)));
		whiteListInfo.setWhiteListUrl(cursor.getString(cursor.getColumnIndex(WhiteListInfo.COLUMN_WHITE_LIST_URL)));
		whiteListInfo.setWhiteListMode(cursor.getInt(cursor.getColumnIndex(WhiteListInfo.COLUMN_WHITE_LIST_MODE)));

		/* カーソル破棄 */
		cursor.close();

		LogUtil.d(TAG, "[OUT] getWhiteListInfo()");
	}

	/**
	 * 全「ホワイトリスト情報」取得処理
	 * <p>
	 * 全ての「ホワイトリスト情報」の取得を行う。<br>
	 * 戻り値としてカーソルを返す。
	 * </p>
	 * @return 「ホワイトリスト情報」テーブルのカーソル
	 */
	public Cursor getAllWhiteList(){
		LogUtil.d(TAG, "[IN ] getAllWhiteList()");

		/* カーソル取得 */
		Cursor cursor = db.query(TABLE_WHITE_LIST_INFO, null, null, null, null, null, null);

		LogUtil.d(TAG, "[OUT] getAllWhiteList()");
		return cursor;
	}

	/**
	 * 「ホワイトリスト情報」削除処理
	 * <p>
	 * 「ホワイトリスト情報」の削除を行う。<br>
	 * 引数「id」をDBの削除キーとする。
	 * </p>
	 * @param id ID
	 * @return 処理結果
	 */
	public boolean deleteWhiteList(int id){
		LogUtil.d(TAG, "[IN ] deleteWhiteList()");
		LogUtil.d(TAG, "id:" + id);

		/* 「ホワイトリスト情報」削除 */
		boolean result = db.delete(TABLE_WHITE_LIST_INFO, WhiteListInfo.COLUMN_ID + "=" + id, null) > 0;

		LogUtil.d(TAG, "[OUT] deleteWhiteList()");
		return result;
	}

	/**
	 * DBアダプタオープン処理
	 * <p>
	 * DBアダプタのオープンを行う。
	 * </p>
	 * @return DBAdapter DBアダプタ
	 */
	public DBAdapter open() {
		LogUtil.d(TAG, "[IN ] open()");

		/* DBオープン */
		db = dbHelper.getWritableDatabase();

		LogUtil.d(TAG, "[OUT] open()");
		return this;
	}

	/**
	 * DBアダプタクローズ処理
	 * <p>
	 * DBアダプタのクローズを行う。
	 * </p>
	 */
	public void close(){
		LogUtil.d(TAG, "[IN ] close()");

		/* DBクローズ */
		dbHelper.close();

		LogUtil.d(TAG, "[OUT] close()");
	}
}
