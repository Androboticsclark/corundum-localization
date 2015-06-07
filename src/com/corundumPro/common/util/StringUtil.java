/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.corundumPro.common.constant.Const;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

/**
 * 文字列ユーティリティクラス.
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class StringUtil {
	static final String TAG = "StringUtil";

	/**
	 * デフォルトコンストラクタ.
	 */
	private StringUtil() {
	}

	/**
	 * URI -> Sting変換処理
	 * <p>
	 * URI形式からString形式にパス変換を行う。
	 * </p>
	 * @param context コンテキスト
	 * @param uri URL形式のファイルパス
	 * @return String形式のファイルパス
	 */
	public static String convertUriToFilePath(Context context, Uri uri) {
		LogUtil.d(TAG, "[IN ] convertUriToFilePath()");
		LogUtil.d(TAG, "uri:" + uri.toString());

		ContentResolver contentResolver = context.getContentResolver();
		String[] columns = {MediaStore.Images.Media.DATA};
		Cursor cursor = contentResolver.query(uri, columns, null, null, null);
		cursor.moveToFirst();
		String path = cursor.getString(0);

		LogUtil.d(TAG, "path:" + path);
		LogUtil.d(TAG, "[OUT] convertUriToFilePath()");
		return path;
	}

	/**
	 * 16進数の文字列に変換します。
	 *
	 * @param bytes バイトの配列
	 * @return 16進数の文字列
	 */
	public static String toHex(byte[] bytes) {
		LogUtil.d(TAG, "[IN ] toHex()");

		if (bytes == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; ++i) {
			appendHex(sb, bytes[i]);
		}
		LogUtil.d(TAG, "[OUT] toHex()");
		return sb.toString();
	}

	/**
	 * 文字列に、数値を16進数に変換した文字列を追加します。
	 *
	 * @param buf 追加先の文字列
	 * @param i 数値
	 */
	public static void appendHex(final StringBuffer buf, final byte i) {
		LogUtil.d(TAG, "[IN ] appendHex()");

		buf.append(Character.forDigit((i & 0xf0) >> 4, 16));
		buf.append(Character.forDigit((i & 0x0f), 16));

		LogUtil.d(TAG, "[OUT] appendHex()");
	}

	/**
	 * 対象文字列がnullの場合、置換文字を返します。
	 *
	 * @param text 対象文字列
	 * @param replace 置換文字
	 * @return 文字列
	 */
	public static String nvl(final String text, final String replace) {
		LogUtil.d(TAG, "[IN ] nvl()");

		if (text == null) {
			return replace;
		}
		LogUtil.d(TAG, "[OUT] nvl()");

		return text;
	}

	/**
	 * 文字列をURLエンコードします。
	 *
	 * @param s 対象文字列
	 * @param encoding エンコーディング
	 * @return URLエンコード文字列
	 */
	public static String encodeUrl(String s, String encoding) {
		LogUtil.d(TAG, "[IN ] encodeUrl()");
		LogUtil.d(TAG, "s:" + s);

		String encoded;

		try {
			encoded = URLEncoder.encode(s, encoding);
			LogUtil.d(TAG, "encoded:" + encoded);

		} catch (UnsupportedEncodingException e) {
			LogUtil.e(TAG, e.getMessage());
			encoded = s;
		}
		LogUtil.d(TAG, "[OUT] encodeUrl()");

		return encoded;
	}

	/**
	 * 文字列をURLデコードします。
	 *
	 * @param s 対象文字列
	 * @param encoding エンコーディング
	 * @return URLデコード文字列
	 */
	public static String decodeUrl(String s, String encoding) {
		LogUtil.d(TAG, "[IN ] decodeUrl()");
		LogUtil.d(TAG, "s:" + s);

		String decoded;

		try {
			decoded = URLDecoder.decode(s, encoding);
			LogUtil.d(TAG, "decoded:" + decoded);

		} catch (UnsupportedEncodingException e) {
			LogUtil.e(TAG, e.getMessage());
			decoded = s;
		}
		LogUtil.d(TAG, "[OUT] decodeUrl()");

		return decoded;
	}

	/**
	 * バイト配列をBase64エンコードします。
	 *
	 * @param b 対象バイト配列
	 * @return Base64エンコード文字列
	 */
	public static String encodeBase64(byte[] b) {
		LogUtil.d(TAG, "[IN ] encodeBase64()");

		String encoded = new String(Base64.encode(b, Base64.DEFAULT));

		LogUtil.d(TAG, "encoded:" + encoded);
		LogUtil.d(TAG, "[OUT] encodeBase64()");

		return encoded;
	}

	/**
	 * バイト配列をBase64デコードします。
	 *
	 * @param b 対象バイト配列
	 * @return Base64デコード文字列
	 */
	public static String decodeBase64(byte[] b) {
		LogUtil.d(TAG, "[IN ] decodeBase64()");

		String decoded = new String(Base64.decode(b, Base64.DEFAULT));

		LogUtil.d(TAG, "decoded:" + decoded);
		LogUtil.d(TAG, "[OUT] decodeBase64()");

		return decoded;
	}

	/**
	 * 文字列をBase64エンコードします。
	 *
	 * @param s 対象文字列
	 * @return Base64エンコード文字列
	 */
	public static String encodeBase64(String s) {
		LogUtil.d(TAG, "[IN ] encodeBase64()");
		LogUtil.d(TAG, "s:" + s);

		String encoded = encodeBase64(s.getBytes());

		LogUtil.d(TAG, "encoded:" + encoded);
		LogUtil.d(TAG, "[OUT] encodeBase64()");

		return encoded;
	}

	/**
	 * 文字列をBase64デコードします。
	 *
	 * @param s 対象文字列
	 * @return Base64デコード文字列
	 */
	public static String decodeBase64(String s) {
		LogUtil.d(TAG, "[IN ] decodeBase64()");
		LogUtil.d(TAG, "s:" + s);

		String decoded = decodeBase64(s.getBytes());

		LogUtil.d(TAG, "decoded:" + decoded);
		LogUtil.d(TAG, "[OUT] decodeBase64()");

		return decoded;
	}

	/**
	 * コランダムAPI作成
	 *
	 * @param apiName コランダムAPI名
	 * @param param 引数文字列
	 * @return JavaScriptコールバック関数名
	 */
	public static String makeCorundumApiName(String apiName) {
		StringBuilder sb = new StringBuilder();

		sb.append(Const.PREFIX_CORUNDUM_API)
		.append(apiName);

		return sb.toString();
	}


	/**
	 * JavaScriptコールバック関数名作成
	 *
	 * @param apiName コランダムAPI名
	 * @param param 引数文字列
	 * @return JavaScriptコールバック関数名
	 */
	public static String makeJsCallbackName(String apiName, String param) {
		StringBuilder sb = new StringBuilder();

		sb.append(Const.PREFIX_JAVASCRIPT)
		.append(apiName)
		.append("(\"")
		.append(StringUtil.encodeUrl(param, Const.CHARSET_UTF8))
		.append("\");");

		return sb.toString();
	}

	/**
	 * 文字列が<code>null</code>または空文字列なら<code>true</code>を返します。
	 *
	 * @param text 文字列
	 * @return 文字列が<code>null</code>または空文字列なら<code>true</code>
	 */
	public static final boolean isEmpty(final String text) {
		return text == null || text.length() == 0;
	}

	/**
	 * 文字列が<code>null</code>または空文字列またはabout:blankなら<code>true</code>を返します。
	 *
	 * @param text 文字列
	 * @return 文字列が<code>null</code>または空文字列またはabout:blankなら<code>true</code>
	 */
	public static final boolean isEmptyWithAboutBlank(final String text) {
		if (isEmpty(text)) {
			return true;
		}
		if (isEmpty(text.replaceAll(Const.ABOUT_BLANK, "").trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 正規表現でマッチした部分文字列を取得します。
	 *
	 * @param regex 正規表現
	 * @param target 対象文字列
	 * @param groupNum グループ番号
	 * @return 部分文字列
	 */
	public static String extractMatchString(String regex, String target, int groupNum) {
		String result = "";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(target);

		if (matcher.find()) {
			result = matcher.group(groupNum);
		}

		return result;
	}
}
