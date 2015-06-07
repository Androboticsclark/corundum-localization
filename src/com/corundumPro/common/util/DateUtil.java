/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日付ユーティリティクラス.
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class DateUtil {
	static final String TAG = "DateUtil";

	/** 日付フォーマットパターン(yyyyMMdd) */
	public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
	/** 日付フォーマットパターン(yyyyMMddHHmmss) */
	public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	/** 日付フォーマットパターン(yyyyMMddHHmmssSSS) */
	public static final String FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

	/**
	 * デフォルトコンストラクタ.
	 */
	private DateUtil() {
	}

	/**
	 * 日付を文字列で取得します。<br>
	 *
	 * @param date 日付
	 * @param format 日付フォーマット
	 * @return 文字列
	 */
	public static String formatDate(Date date, String format) {
		if (date == null) {
			return "";
		}
		if (StringUtil.isEmpty(format)) {
			format = FORMAT_YYYYMMDD;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

		return sdf.format(date);
	}

	/**
	 * 文字列を日付で取得します。<br>
	 *
	 * @param value 文字列
	 * @param format 日付フォーマット
	 * @return 日付
	 */
	public static Date toDate(String value, String format) {
		if (StringUtil.isEmpty(value)) {
			return null;
		}
		if (StringUtil.isEmpty(format)) {
			format = FORMAT_YYYYMMDD;
		}

		// 日付フォーマットを作成
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
		// 日付の厳密チェックを指定
		dateFormat.setLenient(false);
		try {
			// 日付値を返す
			return dateFormat.parse(value);
		} catch (ParseException e) {
			// 日付値なしを返す
			return null;
		} finally {
			dateFormat = null;
		}
	}
}
