/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.function;

import java.util.ArrayList;
import java.util.List;

import com.corundumPro.common.dto.SelectorDto;
import com.corundumPro.common.util.LogUtil;

/**
 * セレクターファンクション.<br>
 * ダイアログ選択肢を管理します。<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class SelectorFunction {
	static final String TAG = "SelectorFunction";

	/** セレクター情報を格納するリスト */
	private List<SelectorDto> list = new ArrayList<SelectorDto>();
	/** デフォルトセレクター情報 */
	private SelectorDto defaultSelectorDto = new SelectorDto();
	/** 選択されたインデックス */
	private int selectorIndex = 0;

	/**
	 * セレクター情報を追加します。<br>
	 *
	 * @param dto セレクター情報DTO
	 * @param デフォルトセレクター情報とする場合、true
	 */
	public void addSelectorDto(SelectorDto selectorDto, boolean isDefault) {
		LogUtil.d(TAG, "[IN ] addSelectorDto()");

		this.list.add(selectorDto);

		if (isDefault) {
			this.defaultSelectorDto = selectorDto;
			this.selectorIndex = this.list.size() - 1;
		}

		LogUtil.d(TAG, "selectorDto:" + selectorDto.toString());
		LogUtil.w(TAG, "[OUT] addSelectorDto()");
	}

	/**
	 * セレクター情報のバリューを取得します。<br>
	 *
	 * @return バリュー
	 */
	public String getSelectorValue() {
		LogUtil.d(TAG, "[IN ] getSelectorValue()");

		SelectorDto selectorDto = this.getSelectorDto(this.selectorIndex);

		LogUtil.d(TAG, "return:" + selectorDto.toString());
		LogUtil.w(TAG, "[OUT] getSelectorValue()");

		return selectorDto.getValue();
	}

	/**
	 * セレクターのバリューに該当するインデックスを取得します。<br>
	 *
	 * @param value
	 * @return インデックス
	 */
	public int getSelectorIndex(String value) {
		LogUtil.d(TAG, "[IN ] getSelectorIndex()");

		int index = 0;

		SelectorDto selectorDto;
		for (int i = 0; i < this.list.size(); i++) {
			selectorDto = this.getSelectorDto(i);
			if (value.equals(selectorDto.getValue())) {
				index = i;
				break;
			}
		}

		LogUtil.d(TAG, "return:" + String.valueOf(index));
		LogUtil.w(TAG, "[OUT] getSelectorIndex()");

		return index;
	}

	/**
	 * ラベルの文字列配列を取得します。<br>
	 *
	 * @return ラベルの文字列配列
	 */
	public String[] getLabels() {
		LogUtil.d(TAG, "[IN ] getSelectorDto()");

		List<String> labelList = new ArrayList<String>();

		SelectorDto selectorDto;
		for (int i = 0; i < this.list.size(); i++) {
			selectorDto = this.getSelectorDto(i);
			labelList.add(selectorDto.getLabel());
		}

		LogUtil.d(TAG, "return:" + labelList.toString());
		LogUtil.w(TAG, "[OUT] getSelectorDto()");

		return labelList.toArray(new String[0]);
	}

	/**
	 * インデックスを取得します。<br>
	 *
	 * @return selectorIndex
	 */
	public int getSelectorIndex() {
		return this.selectorIndex;
	}

	/**
	 * 選択されたインデックスを設定します。<br>
	 *
	 * @param selectorIndex
	 */
	public void setSelectorIndex(int selectorIndex) {
		this.selectorIndex = selectorIndex;
	}

	/**
	 * セレクター情報を取得します。<br>
	 *
	 * @param index リストのインデックス
	 * @return セレクター情報DTO
	 */
	private SelectorDto getSelectorDto(int index) {
		LogUtil.d(TAG, "[IN ] getSelectorDto()");
		SelectorDto selectorDto = this.list.get(index);

		if (selectorDto == null) {
			selectorDto = defaultSelectorDto;
		}

		LogUtil.d(TAG, "return:" + selectorDto.toString());
		LogUtil.w(TAG, "[OUT] getSelectorDto()");

		return selectorDto;
	}
}
