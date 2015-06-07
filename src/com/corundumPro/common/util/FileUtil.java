/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.util;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import android.content.Context;
import android.os.Environment;

import com.corundumPro.R;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.constant.EnumExtension;
import com.corundumPro.common.dto.FileListInfo;

/**
 * ファイルユーティリティクラス.
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class FileUtil {
	static final String TAG = "FileUtil";



	/**
	 * デフォルトコンストラクタ.
	 */
	private FileUtil() {
	}

	/**
	 * ベースフォルダパス取得処理
	 * <p>
	 * SDカード上のコランダムフォルダのパスを取得する。
	 * </p>
	 * @param context コンテキスト
	 * @return SDカード上のコランダムフォルダパス
	 */
	public static String getBaseDirPath(Context context) {
		LogUtil.d(TAG, "[IN ] getBaseDirPath()");

		String path = Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.app_name);

		LogUtil.d(TAG, "path:" + path);
		LogUtil.d(TAG, "[OUT] getBaseDirPath()");
		return path;
	}

	/**
	 * ファイル種別変換処理
	 * <p>
	 * ファイルリストで使用する「ファイル種別」への変換を行う。
	 * </p>
	 * @param file ファイルオブジェクト
	 * @return ファイル種別
	 */
	public static int convertFileType(File file) {
		LogUtil.d(TAG, "[IN ] convertFileType()");
		LogUtil.d(TAG, "file:" + file.getPath());

		int fileType = FileListInfo.FILE_TYPE_OTHER;

		/* ファイル種別：フォルダ */
		if (true == file.isDirectory()) {
			fileType = FileListInfo.FILE_TYPE_DIR;

		/* ファイル種別：画像 */
		} else if ((file.getName().endsWith(".jpg"))
				|| (file.getName().endsWith(".JPG"))
				|| (file.getName().endsWith(".jpeg"))
				|| (file.getName().endsWith(".JPEG"))
				|| (file.getName().endsWith(".png"))
				|| (file.getName().endsWith(".PNG"))
				|| (file.getName().endsWith(".gif"))
				|| (file.getName().endsWith(".GIF"))) {
			fileType = FileListInfo.FILE_TYPE_IMAGE;

		/* ファイル種別：編集可能ファイル */
		} else if ((file.getName().endsWith(".js"))
				|| (file.getName().endsWith(".JS"))
				|| (file.getName().endsWith(".css"))
				|| (file.getName().endsWith(".CSS"))
				|| (file.getName().endsWith(".html"))
				|| (file.getName().endsWith(".HTML"))
				|| (file.getName().endsWith(".xml"))
				|| (file.getName().endsWith(".XML"))
				|| (file.getName().endsWith(".txt"))
				|| (file.getName().endsWith(".TXT"))
				|| (-1 == file.getName().lastIndexOf("."))) {
			fileType = FileListInfo.FILE_TYPE_EDIT_ABLE;

		/* ファイル種別：編集不可能ファイル */
		} else {
			fileType = FileListInfo.FILE_TYPE_OTHER;
		}

		LogUtil.d(TAG, "fileType:" + fileType);
		LogUtil.d(TAG, "[OUT] convertFileType()");
		return fileType;
	}

	/**
	 * ファイルサイズ変換処理
	 * <p>
	 * ファイルリストで使用する「ファイルサイズ」に変換を行う。
	 * </p>
	 * @param size ファイルサイズ
	 * @return 表示用のファイルサイズ文字列
	 */
	public static String convertSize(long size) {
		LogUtil.d(TAG, "[IN ] convertSize()");
		LogUtil.d(TAG, "size:" + size);

		float tmp = 0;
		String fileSize = "";
		long GByte = 1000 * 1000 * 1000;
		long MByte = 1000 * 1000;
		long KByte = 1000;

		if (size >= GByte) {
			/* ギガバイト表示 */
			tmp = (float)size / GByte;
			fileSize = tmp + " GByte";
		} else if (size >= MByte) {
			/* メガバイト表示 */
			tmp = (float)size / MByte;
			fileSize = tmp + " MByte";
		} else if (size >= KByte) {
			/* キロバイト表示 */
			tmp = (float)size / KByte;
			fileSize = tmp + " KByte";
		} else {
			/* バイト表示 */
			tmp = (float)size;
			fileSize = tmp + " Byte";
		}

		LogUtil.d(TAG, "fileSize:" + fileSize);
		LogUtil.d(TAG, "[OUT] convertSize()");
		return fileSize;
	}

	/**
	 * ファイル強制削除処理
	 * <p>
	 * ファイルの強制削除を行う。<br>
	 * フォルダの場合、配下のファイルも全て削除する。
	 * </p>
	 * @param file ファイル
s	 */
	public static void deleteForce(File file){
		LogUtil.d(TAG, "[IN ] deleteForce()");
		LogUtil.d(TAG, "file:" + file.getPath());

		/* ファイルが存在しない */
		if (false == file.exists()) {
			LogUtil.d(TAG, "[OUT] deleteForce()");
			return ;
		}

		/* ファイル削除 */
		if (true == file.isFile()) {
			try {
				boolean ret = file.delete();
				LogUtil.d(TAG, "ret:" + ret);
			} catch (Exception e) {
				LogUtil.d(TAG, e.getMessage());
			}
		}

		/* フォルダの場合、配下のファイルを再帰的に削除 */
		if (true == file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteForce( files[i] );
			}

			try {
				boolean ret = file.delete();
				LogUtil.d(TAG, "ret:" + ret);
			} catch (Exception e) {
				LogUtil.d(TAG, e.getMessage());
			}
		}

		LogUtil.d(TAG, "[OUT] deleteForce()");
	}

	/**
	 * 最後の名前区切り文字の位置を取得します。<br>
	 *
	 * @param filename
	 * @return 最後の名前区切り文字位置
	 */
	public static int indexOfLastSeparator(String filename) {
		if (filename == null) {
			return -1;
		}
		int lastUnixPos = filename.lastIndexOf(Const.SEPARATOR_SLASH);
		int lastWindowsPos = filename.lastIndexOf(Const.SEPARATOR_YEN);
		return Math.max(lastUnixPos, lastWindowsPos);
	}

	/**
	 * 拡張子区切り文字の位置を取得します。<br>
	 *
	 * @param filename
	 * @return 拡張子区切り文字位置
	 */
	public static int indexOfExtension(String filename) {
		if (filename == null) {
			return -1;
		}
		int extensionPos = filename.lastIndexOf(Const.SEPARATOR_EXTENSION);
		int lastSeparator = indexOfLastSeparator(filename);
		return (lastSeparator > extensionPos ? -1 : extensionPos);
	}

	/**
	 * 拡張子を取得します。<br>
	 *
	 * @param filename
	 * @return 拡張子
	 */
	public static String getExtension(String filename) {
		if (filename == null) {
			return null;
		}
		int index = indexOfExtension(filename);
		if (index == -1) {
			return "";
		} else {
			return filename.substring(index + 1);
		}
	}

	/**
	 * ファイル名(拡張子あり)を取得します。<br>
	 *
	 * @param filename
	 * @return ファイル名(拡張子あり)
	 */
	public static String getName(String filename) {
		if (filename == null) {
			return null;
		}
		int index = indexOfLastSeparator(filename);
		return filename.substring(index + 1);
	}

	/**
	 * ファイルパスから拡張子を取り除きます。<br>
	 *
	 * @param filename
	 * @return 拡張子を取り除いたファイルパス
	 */
	public static String removeExtension(String filename) {
		if (filename == null) {
			return null;
		}
		int index = indexOfExtension(filename);
		if (index == -1) {
			return filename;
		} else {
			return filename.substring(0, index);
		}
	}

	/**
	 * ファイル名(拡張子なし)を取得します。<br>
	 *
	 * @param filename
	 * @return ファイル名(拡張子なし)
	 */
	public static String getBaseName(String filename) {
		return removeExtension(getName(filename));
	}


	/**
	 * ファイルの拡張子に合致した拡張子Enumオブジェクトを取得します。<br>
	 *
	 * @param filename
	 * @return 拡張子Enumオブジェクト
	 */
	public static EnumExtension getEnumExtension(String filename) {
		String extension = getExtension(filename);

		for (EnumExtension ext : EnumExtension.values()){
			if (ext.getExtension().equalsIgnoreCase(extension)) {
				return ext;
			}
		}
		return null;
	}

	/**
	 * HTMLファイルか判定します。<br>
	 *
	 * @param filename
	 * @return HTMLファイル：true
	 */
	public static boolean isHtml(String filename) {
		EnumExtension ext = getEnumExtension(filename);

		if (ext == null) {
			return false;
		}

		return ext.isHtml();
	}

	/**
	 * 一時ファイル名を取得します。<br>
	 * 例：<br>
	 * 変換元 -> file:///aaa/bbb/ccc.html
	 * 変換後 -> file:///aaa/bbb/ccc_20150310235959999.html
	 *
	 * @param filename
	 * @return 一時ファイル名
	 */
	public static String getTemporaryFileName(String filename) {
		String path = removeExtension(filename);
		String extension = getExtension(filename);
		String dateStr = DateUtil.formatDate(new Date(), DateUtil.FORMAT_YYYYMMDDHHMMSSSSS);

		StringBuilder sb = new StringBuilder();
		sb.append(path).append("_").append(dateStr).append(Const.SEPARATOR_EXTENSION).append(extension);

		return sb.toString();
	}

	/**
	 * ファイルが存在している場合、削除します。
	 *
	 * @param file
	 */
	public static void deleteQuietly(File file) {
		if (file.exists()) {
			FileUtils.deleteQuietly(file);
		}
	}
}
