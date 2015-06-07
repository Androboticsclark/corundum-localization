/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 暗号化ユーティリティクラス.
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class CryptUtil {
	static final String TAG = "CryptUtil";

	/** 暗号化アルゴリズム(MD5) */
	private static final String ALGORITHM_MD5 = "md5";

	/**
	 * デフォルトコンストラクタ.
	 */
	private CryptUtil() {
	}

	/**
	 * 文字列をMD5変換します。<br>
	 * <ol>
	 * 	<li>引数の文字列をMD5のアルゴリズムで固定長のハッシュ値を取得する。</li>
	 * </ol>
	 * @param str 暗号化対象文字列
	 * @return ハッシュ値
	 */
	public static byte[] encryptMD5(String str) {
		LogUtil.d(TAG, "[IN ] encryptMD5()");
		LogUtil.d(TAG, "str:" + str);

		byte[] b;

		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
			b = md.digest(str.getBytes());

		} catch (NoSuchAlgorithmException e) {
			b = new byte[0];
		}

		LogUtil.d(TAG, "[OUT] encryptMD5()");

		return b;
	}

	/**
	 * 文字列をMD5変換します。<br>
	 * <ol>
	 * 	<li>引数の文字列をMD5のアルゴリズムで固定長のハッシュ値を取得する。</li>
	 * 	<li>ハッシュ値は16進数文字列化し、返却する。</li>
	 * </ol>
	 * @param str 暗号化対象文字列
	 * @return ハッシュ値(16進数文字列)
	 */
	public static String encryptMD5ToHex(String str) {
		return StringUtil.toHex(encryptMD5(str));
	}

	/**
	 * 文字列をMD5変換します。<br>
	 * <ol>
	 * 	<li>引数の文字列をMD5のアルゴリズムで固定長のハッシュ値を取得する。</li>
	 * 	<li>ハッシュ値は16進数文字列化し、返却する。</li>
	 * </ol>
	 * @param str 暗号化対象文字列
	 * @return ハッシュ値(16進数文字列)
	 */
	public static String encryptMD5ToBase64(String str) {
		return StringUtil.encodeBase64(encryptMD5(str));
	}
}
