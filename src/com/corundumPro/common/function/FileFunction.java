/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.function;

import java.io.File;
import java.io.FileWriter;

import com.corundumPro.R;
import com.corundumPro.common.exception.ArcAppException;
import com.corundumPro.common.util.EnvUtil;
import com.corundumPro.common.util.FileUtil;
import com.corundumPro.common.util.LogUtil;

/**
 * ファイルファンクション.<br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class FileFunction {
	static final String TAG = "FileFunction";

	/**
	 * コンストラクタ<br>
	 *
	 */
	public FileFunction() {
		LogUtil.d(TAG, "[IN ] FileFunction()");
		LogUtil.d(TAG, "[OUT] FileFunction()");
	}

	/**
	 * ファイルに書き込みます。<br>
	 *
	 * @param filePath ファイルパス
	 * @param contents コンテンツ
	 * @throw ArcAppException アプリケーション例外
	 */
	public void writeFile(String filePath, String contents) throws ArcAppException {
		LogUtil.d(TAG, "[IN ] writeFile()");
		LogUtil.d(TAG, "filePath:" + filePath);

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(filePath);

			/* ファイル書き込み */
			fileWriter.write(contents);
			fileWriter.close();

		} catch (Exception e) {
			if (null != fileWriter) {
				try {
					fileWriter.close();
				} catch (Exception e1) {
					throw new ArcAppException(R.string.log_err_close_file, e1);
				}
			}
			throw new ArcAppException(R.string.log_err_write_file, e);
		} finally {
			LogUtil.d(TAG, "[OUT] writeFile()");
		}
	}

	/**
	 * 一時ファイルを作成します。
	 *
	 * @param filePath ファイルパス
	 * @param contents コンテンツ
	 * @throws ArcAppException アプリケーション例外
	 */
	public String makeTemporaryFile(String filePath, String contents) throws ArcAppException {
		LogUtil.d(TAG, "[IN ] makeTemporaryFile()");

		// プレビュー用に一時ファイルを保存する
		String tempFilePath = "";
		for (int i = 0; i < EnvUtil.getRetry(); i++) {
			tempFilePath = FileUtil.getTemporaryFileName(filePath);
			if (new File(tempFilePath).exists()) {
				continue;
			} else {
				break;
			}
		}
		LogUtil.d(TAG, "tempFilePath:" + String.valueOf(tempFilePath));

		try {
			// ファイルを保存する
			this.writeFile(tempFilePath, contents);

		} catch (ArcAppException e) {
			throw e;

		} finally {
			LogUtil.d(TAG, "[OUT] makeTemporaryFile()");
		}
		return tempFilePath;
	}
}
