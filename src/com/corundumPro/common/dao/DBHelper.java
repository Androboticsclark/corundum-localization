package com.corundumPro.common.dao;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.corundumPro.R;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.dto.ShortcutInfo;
import com.corundumPro.common.dto.WhiteListInfo;
import com.corundumPro.common.util.EnvUtil;
import com.corundumPro.common.util.FileUtil;
import com.corundumPro.common.util.LogUtil;

/**
 * DBHelperクラス
 * <p>
 * DBヘルパークラス
 * </p>
 * @author androbotics.ltd
 */
public class DBHelper extends SQLiteOpenHelper {
	static final String TAG = "DBHelper";

	/* コンテキスト */
	public Context context;

	/**
	 * コンストラクタ
	 * <p>
	 * DBヘルパーのコンストラクタ。
	 * </p>
	 * @param context コンテキスト
	 */
	public DBHelper(Context context) {
		super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
		LogUtil.d(TAG, "[IN ] DBHelper()");

		/* コンテキスト保持 */
		this.context = context;

		LogUtil.d(TAG, "[OUT] DBHelper()");
	}

	/**
	 * DB作成処理
	 * <p>
	 * onCreate()のオーバーライド<br>
	 * クラス生成時にコールされる。
	 * </p>
	 * @param db DBオブジェクト
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		LogUtil.d(TAG, "[IN ] onCreate()");

		/* 「ショートカット情報」テーブル作成 */
		db.execSQL(
				"CREATE TABLE IF NOT EXISTS " + DBAdapter.TABLE_SHORTCUT_INFO + " ("
				+ ShortcutInfo.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"	/* ID */
				+ ShortcutInfo.COLUMN_SHORTCUT_NAME + " TEXT NOT NULL,"				/* ショートカット名 */
				+ ShortcutInfo.COLUMN_HOME_WEB_URL + " TEXT NOT NULL"				/* ホームWEB URL */
				+ ");");

		/* 「ショートカット情報」テーブル初期化 */
		init_shortcut_info(db);

		/* 「ホワイトリスト情報」テーブル作成 */
		db.execSQL(
				"CREATE TABLE IF NOT EXISTS " + DBAdapter.TABLE_WHITE_LIST_INFO + " ("
				+ WhiteListInfo.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"	/* ID */
				+ WhiteListInfo.COLUMN_WHITE_LIST_TITLE + " TEXT NOT NULL,"	 		/* ホワイトリストタイトル */
				+ WhiteListInfo.COLUMN_WHITE_LIST_URL + " TEXT NOT NULL,"			/* ホワイトリストURL */
				+ WhiteListInfo.COLUMN_WHITE_LIST_MODE + " INTEGER NOT NULL"		/* ホワイトリストモード */
				+ ");");

		/* 「ホワイトリスト情報」テーブル初期化 */
		init_white_list_info(db);

		LogUtil.d(TAG, "[OUT] onCreate()");
	}

	/**
	 * DBアップグレード処理
	 * <p>
	 * onUpgrade()のオーバーライド<br>
	 * DBアップグレード時にコールされる。
	 * </p>
	 * @param db DBオブジェクト
	 * @param oldVersion 旧バージョン
	 * @param newVersion 新バージョン
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		LogUtil.d(TAG, "[IN ] onUpgrade()");
		LogUtil.d(TAG, "oldVersion:" + oldVersion);
		LogUtil.d(TAG, "newVersion:" + newVersion);

		/* ファイル削除 */
		File baseDir = new File(FileUtil.getBaseDirPath(this.context));
		FileUtil.deleteForce(baseDir);

		/* 「ショートカット情報」テーブル削除 */
		db.execSQL("DROP TABLE IF EXISTS " + DBAdapter.TABLE_SHORTCUT_INFO);

		/* 「ホワイトリスト情報」テーブル削除 */
		db.execSQL("DROP TABLE IF EXISTS " + DBAdapter.TABLE_WHITE_LIST_INFO);

		/* DB作成処理 */
		onCreate(db);

		LogUtil.d(TAG, "[OUT] onUpgrade()");
	}

	/**
	 * 「ショートカット情報」テーブル初期化処理
	 * <p>
	 * 「ショートカット情報」テーブルの初期化を行う。
	 * </p>
	 * @param db DBオブジェクト
	 */
	public void init_shortcut_info(SQLiteDatabase db) {
		LogUtil.d(TAG, "[IN ] init_shortcut_info()");

		/* 初期値設定 */
		ContentValues shrotcut_info_value = new ContentValues();
		shrotcut_info_value.put(ShortcutInfo.COLUMN_SHORTCUT_NAME, this.context.getResources().getString(R.string.app_name));
		shrotcut_info_value.put(ShortcutInfo.COLUMN_HOME_WEB_URL, EnvUtil.getShortcutUrl());

		/* DB登録 */
		db.insert(DBAdapter.TABLE_SHORTCUT_INFO, "", shrotcut_info_value);

		LogUtil.d(TAG, "[OUT] init_shortcut_info()");
	}

	/**
	 * 「ホワイトリスト情報」テーブル初期化処理
	 * <p>
	 * 「ホワイトリスト情報」テーブルの初期化を行う。
	 * </p>
	 * @param db DBオブジェクト
	 */
	public void init_white_list_info(SQLiteDatabase db) {
		LogUtil.d(TAG, "[IN ] init_white_list_info()");

		/* 初期値設定 */
		ContentValues white_list_info_value = new ContentValues();
		white_list_info_value.put(WhiteListInfo.COLUMN_WHITE_LIST_URL, Const.URL_VASDAQ_TV);
		white_list_info_value.put(WhiteListInfo.COLUMN_WHITE_LIST_TITLE, "VASDAQ.TV");
		white_list_info_value.put(WhiteListInfo.COLUMN_WHITE_LIST_MODE, DBAdapter.WHITE_LIST_MODE_URL);

		/* DB登録 */
		db.insert(DBAdapter.TABLE_WHITE_LIST_INFO, "", white_list_info_value);

		/* 初期値設定 */
		white_list_info_value.put(WhiteListInfo.COLUMN_WHITE_LIST_URL, Const.URL_GOOGLE);
		white_list_info_value.put(WhiteListInfo.COLUMN_WHITE_LIST_TITLE, "グーグル");
		white_list_info_value.put(WhiteListInfo.COLUMN_WHITE_LIST_MODE, DBAdapter.WHITE_LIST_MODE_URL);

		/* DB登録 */
		db.insert(DBAdapter.TABLE_WHITE_LIST_INFO, "", white_list_info_value);

		LogUtil.d(TAG, "[OUT] init_white_list_info()");
	}
}
