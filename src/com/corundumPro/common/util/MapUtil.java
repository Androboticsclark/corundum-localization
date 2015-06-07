/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * マップユーティリティクラス.
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class MapUtil {
	static final String TAG = "MapUtil";

	/**
	 * デフォルトコンストラクタ.
	 */
	private MapUtil() {
	}

	/**
	 * マップからバリューをもとにキーを取得します。<br>
	 * キーとバリューが1対1の場合に使用<br>
	 *
	 * @param map マップ
	 * @param value バリュー
	 * @return キー
	 */
	public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
		for (Entry<K, V> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * マップからバリューをもとにキーを取得します。<br>
	 * キーとバリューが1対多の場合に使用<br>
	 *
	 * @param map マップ
	 * @param value バリュー
	 * @return キー
	 */
	public static <K, V> Set<K> getKeysByValue(Map<K, V> map, V value) {
		Set<K> keys = new HashSet<K>();
		for (Entry<K, V> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				keys.add(entry.getKey());
			}
		}
		return keys;
	}
}
