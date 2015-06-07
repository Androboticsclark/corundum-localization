/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.util;

import java.io.File;
import java.io.IOException;

import android.content.Context;

import com.corundumPro.R;
import com.corundumPro.common.constant.EnumAssetsDir;
import com.corundumPro.common.dto.FileListInfo;
import com.corundumPro.common.function.StorageFunction;

/**
 * ユーティリティクラス.
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class ResourceUtil {
	static final String TAG = "ResourceUtil";

	/**
	 * デフォルトコンストラクタ.
	 */
	private ResourceUtil() {
	}

	/**
	 * リソースファイルコピー処理
	 * <p>
	 * リソース配下(asset)におかれた画像ファイルを、指定パスへコピーする。
	 * </p>
	 * @param context コンテキスト
	 * @param outPath コピー先フォルダパス
	 */
	public static void copyResourceFiles(Context context, File outPath) {
		LogUtil.d(TAG, "[IN ] copyResourceFile()");
		LogUtil.d(TAG, "outPath:" + outPath.getPath().toString());

		StorageFunction storageFunction;
		String srcPath = "";
		for (EnumAssetsDir dir : EnumAssetsDir.values()){
			srcPath = dir.getDirName();
			LogUtil.d(TAG, "srcPath:" + srcPath);

			storageFunction = new StorageFunction(context, srcPath, outPath);
			try {
				storageFunction.initData();
			} catch (IOException e) {
				LogUtil.e(TAG, e.getMessage());
			}
		}

		LogUtil.d(TAG, "[OUT] copyResourceFile()");
	}

	/**
	 * ファイル種別->リソースID変換処理
	 * <p>
	 * ファイルリストで使用する「ファイル種別」からアイコン用「リソースID」への変換を行う。
	 * </p>
	 * @param fileType ファイル種別
	 * @return リソースID
	 */
	public static int convertResourceImage(int fileType) {
		LogUtil.d(TAG, "[IN ] convertResourceImage()");
		LogUtil.d(TAG, "fileType:" + fileType);

		int resourceId = 0;

		switch (fileType) {
		/* ファイル種別：フォルダ */
		case FileListInfo.FILE_TYPE_DIR:
			resourceId = R.drawable.folder;
			break;

		/* ファイル種別：画像 */
		case FileListInfo.FILE_TYPE_IMAGE:
			resourceId = R.drawable.image;
			break;

		/* ファイル種別：編集可能ファイル */
		case FileListInfo.FILE_TYPE_EDIT_ABLE:
			resourceId = R.drawable.file;
			break;

		/* ファイル種別：編集不可能ファイル */
		case FileListInfo.FILE_TYPE_OTHER:
		default:
			resourceId = R.drawable.other_file;
			break;
		}

		LogUtil.d(TAG, "resourceId:" + resourceId);
		LogUtil.d(TAG, "[OUT] convertResourceImage()");
		return resourceId;
	}
}
