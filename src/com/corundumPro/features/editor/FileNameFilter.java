package com.corundumPro.features.editor;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.util.LogUtil;

/**
 * FileNameFilterクラス
 * <p>
 * 「ファイルネームフィルター」クラス
 * </p>
 * @author androbotics.ltd
 */
public class FileNameFilter implements InputFilter {
	static final String TAG = "FileNameFilter";

	private BaseActivity activity;

	public FileNameFilter(BaseActivity activity) {
		LogUtil.d(TAG, "[IN ] FileNameFilter()");

		this.activity = activity;

		LogUtil.d(TAG, "[OUT] FileNameFilter()");
	}

	/**
	 * フィルター処理
	 * <p>
	 * 文字入力時にコールされる。<br>
	 * 文字数制限、禁止文字チェックを行う。<br>
	 * エラー時は最後の入力文字を削除する。
	 * </p>
	 * @param charSequence 入力文字列
	 * @param start 先頭位置インデックス
	 * @param end 末尾位置インデックス
	 * @param dest スパン
	 * @param dstart 追加先頭位置インデックス
	 * @param dend 追加末尾位置インデックス
	 * @return charSequence エディットボックス文字列
	 */
	@Override
	public CharSequence filter(CharSequence charSequence, int start, int end, Spanned dest, int dstart, int dend) {
		LogUtil.d(TAG, "[IN ] filter()");
		LogUtil.d(TAG, "source:" + charSequence);
		LogUtil.d(TAG, "start:" + start);
		LogUtil.d(TAG, "end:" + end);
		LogUtil.d(TAG, "dstart:" + dstart);
		LogUtil.d(TAG, "dend:" + dend);

		String tmp = "";

		/* 入力桁数チェック */
		if ((Const.CONFIG_FILE_NAME_MAX_LENGTH < dstart) || (Const.CONFIG_FILE_NAME_MAX_LENGTH < end)) {
			/* 最後の文字を削除 */
			tmp = charSequence.toString().substring(0, charSequence.toString().length() - 1);

			LogUtil.d(TAG, "[OUT] filter()");
			return tmp;
		}

		/* 禁止文字チェック */
		char validChar[] = {'<', '>', ':', '*', '?', '"', '/', '\\', '|'};

		for(int i = 0; i < validChar.length; i++) {
			/* 禁止文字が存在する場合 */
			if (-1 != charSequence.toString().indexOf(validChar[i])) {
				String errorMessage = "[ " + validChar[i] + " ] " + activity.getResources().getString(R.string.err_illegal_character);
				Toast.makeText(activity.getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();

				/* 最後の文字を削除 */
				tmp = charSequence.toString().substring(0, charSequence.toString().length() - 1);

				LogUtil.d(TAG, "[OUT] filter()");
				return tmp;
			}
		}

		LogUtil.d(TAG, "[OUT] filter()");
		return charSequence;
    }
}
