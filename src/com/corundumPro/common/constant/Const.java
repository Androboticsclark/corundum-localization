/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.constant;

/**
 * 共通定数クラス.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class Const {

	/** キャラクタセット */
	public static final String CHARSET_UTF8 = "UTF-8";

	/** コランダムAPIプレフィックス */
	public static final String PREFIX_CORUNDUM_API = "corundum-api://";

	/** JavaScriptコールバックプレフィックス */
	public static final String PREFIX_JAVASCRIPT = "javascript:corundum.";

	/** HTTPプロトコル */
	public static final String PROTOCOL_HTTP = "http://";

	/** HTTPSプロトコル */
	public static final String PROTOCOL_HTTPS = "https://";

	/** FILEプロトコル */
	public static final String PROTOCOL_FILE = "file://";

	/** Google URL */
	public static final String URL_GOOGLE = "http://www.google.co.jp";

	/** Vasdaq TV URL */
	public static final String URL_VASDAQ_TV = "http://www.vasdaq.tv/";

	/** about:blank */
	public static final String ABOUT_BLANK = "about:blank";

	/** UNIX名前区切り文字 */
	public static final String SEPARATOR_SLASH = "/";

	/** Windows名前区切り文字 */
	public static final String SEPARATOR_YEN = "\\";

	/** 拡張子区切り文字 */
	public static final String SEPARATOR_EXTENSION = ".";

	/** カンマ区切り文字 */
	public static final String SEPARATOR_COMMA = ",";

	/** ハイフン区切り文字 */
	public static final String SEPARATOR_HYPHEN = "-";

	/** 改行コード */
	public static final String CARRIAGE_RETURN = "\n";

	/** バッファサイズ */
	public static final int BUF_SIZE = 1024;

	/*
	 * GUI入力系の制限設定値
	 */
	/** パスコード最大長 */
	public static final int CONFIG_PASSCODE_MAX_LENGTH = 30;

	/** ファイル名最大長 */
	public static final int CONFIG_FILE_NAME_MAX_LENGTH = 512;

	/*
	 * リクエストID
	 */
	/** リクエストID：ホワイトリスト詳細画面 */
	public static final int REQUEST_WHITE_LIST_DETAIL = 1;

	/** リクエストID：スクリーンセーバー画面 */
	public static final int REQUEST_SCREENSAVER = 2;

	/** リクエストID：ギャラリーアプリ */
	public static final int REQUEST_GALLERY = 3;

	/** リクエストID：カメラアプリ */
	public static final int REQUEST_CAMERA = 4;

	/** リクエストID：PDFビュアーアプリ */
	public static final int REQUEST_PDF_VIEWER = 5;

	/** リクエストID：WEBブラウザアプリ */
	public static final int REQUEST_WEB_BROWSER = 6;

	/** リクエストID：ホーム画面編集画面 */
	public static final int REQUEST_HOME_MODIFY_DETAIL = 7;

	/** リクエストID：アクションシート画面 */
	public static final int REQUEST_ACTION_SHEET = 8;

	/** リクエストID：ギャラリーアプリ(トリミング) */
	public static final int REQUEST_GALLERY_TRIMING = 9;

	/** リクエストID：URLスキーム実行(対象アプリ不定) */
	public static final int REQUEST_URL_SHCEME = 10;

	/** リクエストID：コランダムエディタプレビュー画面 */
	public static final int REQUEST_EDITOR_PREVIEW = 11;

	/** リクエストID：画像プレビュー画面 */
	public static final int REQUEST_IMAGE_PREVIEW = 12;

	/*
	 * アクションシート画面戻り値
	 */
	/** アクションシート戻り値：ツールバー表示/非表示 */
	public static final int ACTION_SHEET_TOOLBAR = 1;

	/** アクションシート戻り値：WEBブラウザ起動 */
	public static final int ACTION_SHEET_WEB_BORWSER = 2;

	/** アクションシート戻り値：環境設定メニューへ */
	public static final int ACTION_SHEET_MENU = 3;

	/** アクションシート戻り値：このアプリを強制終了 */
	public static final int ACTION_SHEET_EXIT = 4;

	/** アクションシート戻り値：キャンセル */
	public static final int ACTION_SHEET_CANCEL = 5;
}
