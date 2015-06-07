/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.function;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.content.res.AssetManager;

import com.corundumPro.common.util.LogUtil;


/**
 * リソースファンクションクラス.
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class StorageFunction {
	static final String TAG = "StorageFunction";

	/** コピー先フォルダオブジェクト */
	public final File dataDir;
	/** コピー元パス */
	private final String path;
	/** AssetManager */
	private final AssetManager assetManager;
	/** バッファーサイズ */
	private static final int BUFFER_SIZE = 1024;

	/**
	 * コンストラクタ.<br>
	 *
	 * @param context コンテキスト
	 * @param path コピー元パス
	 */
	public StorageFunction(Context context, String path) {
		this(context, path, context.getDir(path, Context.MODE_PRIVATE));
	}

	/**
	 * コンストラクタ.<br>
	 *
	 * @param context コンテキスト
	 * @param path コピー元パス
	 * @param dstDirPath コピー先フォルダオブジェクト
	 */
	public StorageFunction(Context context, String path, File dstDirPath) {
		super();
		LogUtil.d(TAG, "[IN ] StorageFunction()");

		this.path = path;
		this.assetManager = context.getResources().getAssets();

		if (dstDirPath != null) {
			this.dataDir = new File(dstDirPath, path);
		} else {
			this.dataDir = context.getDir(path, Context.MODE_PRIVATE);
		}

		LogUtil.d(TAG, "path:" + this.path);
		LogUtil.d(TAG, "dataDir:" + this.dataDir.getAbsolutePath());
		LogUtil.d(TAG, "[OUT] StorageFunction()");
	}

	/**
	 * 指定したassets内のフォルダを再帰的にコピーします。<br>
	 *
	 * @throws IOException
	 */
	public void initData() throws IOException {
		LogUtil.d(TAG, "[IN ] initData()");

		copyFiles(null, path, dataDir);

		LogUtil.d(TAG, "[OUT] initData()");
	}

	/**
	 * 指定したassets内のフォルダを再帰的に削除します。<br>
	 *
	 * @throws IOException
	 */
	public void deleteData() throws IOException {
		LogUtil.d(TAG, "[IN ] deleteData()");

		deleteAll(dataDir);

		LogUtil.d(TAG, "[OUT] deleteData()");
	}

	/**
	 * フォルダまたはファイルをコピーします。<br>
	 * フォルダの場合、再帰的にコピーします。<br>
	 *
	 * @param parentPath 親のパス(ルートの場合、nullを指定)
	 * @param filename ファイルまたはフォルダ名
	 * @param toDir コピー先フォルダオブジェクト
	 * @throws IOException
	 */
	private void copyFiles(final String parentPath, final String filename, final File toDir) throws IOException {
		LogUtil.d(TAG, "[IN ] copyFiles()");

		String assetpath = (parentPath != null ? parentPath + File.separator + filename : filename);
		LogUtil.d(TAG, "assetpath:" + assetpath);

		if (isDirectory(assetpath)) {
			LogUtil.d(TAG, assetpath + " is a Directory.");

			if (!toDir.exists()) {
				if(!toDir.mkdirs()) {
					// コピー先フォルダの作成に失敗した場合、処理を中止する。
					LogUtil.w(TAG, "Making Directory failed;" + toDir.getAbsolutePath());
					return;
				}
			}

			for (String child : assetManager.list(assetpath)) {
				copyFiles(assetpath, child, new File(toDir, child));
			}

		} else {
			LogUtil.d(TAG, assetpath + " is a File.");

			if (assetpath.endsWith(".zip")) {
				unzip(assetManager.open(assetpath, AssetManager.ACCESS_STREAMING), toDir.getParentFile());
			} else {
				copyData(assetManager.open(assetpath), new FileOutputStream(new File(toDir.getParentFile(), filename)));
			}
		}

		LogUtil.d(TAG, "[OUT] copyFiles()");
	}

	/**
	 * フォルダか判定します。<br>
	 *
	 * @param path パス
	 * @return フォルダ：true, ファイル：false
	 * @throws IOException
	 */
	private boolean isDirectory(final String path) throws IOException {
		LogUtil.d(TAG, "[IN ] isDirectory()");

		InputStream is = null;
		boolean isDirectory = false;
		try {
			if (assetManager.list(path).length > 0){
				isDirectory = true;
			} else {
				is = assetManager.open(path);
			}
		} catch (FileNotFoundException fnfe) {
			isDirectory = true;
		} finally {
			IOUtils.closeQuietly(is);
		}

		LogUtil.d(TAG, "return:" + String.valueOf(isDirectory));
		LogUtil.d(TAG, "[OUT] isDirectory()");

		return isDirectory;
	}

	/**
	 * フォルダまたはファイルを削除します。<br>
	 * フォルダの場合、再帰的に削除します。<br>
	 *
	 * @param file 削除対象ファイルオブジェクト
	 * @return フォルダ：true, ファイル：false
	 */
	private void deleteAll(File file) {
		LogUtil.d(TAG, "[IN ] deleteAll()");

		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				deleteAll(f);
			}
		}
		if(!file.delete()) {
			// 削除対象ファイルの削除に失敗した場合、処理を中止する。
			LogUtil.w(TAG, "Deleting file failed;" + file.getAbsolutePath());
			return;
		}

		LogUtil.d(TAG, "[OUT] deleteAll()");
	}

	/**
	 * ファイルを解凍します。<br>
	 *
	 * @param is InputStream
	 * @param toDir 解凍先ファイルオブジェクト
	 * @throws IOException
	 */
	private void unzip(InputStream is, File toDir) throws IOException {
		LogUtil.d(TAG, "[IN ] unzip()");

		ZipInputStream zis = null;
		try {
			zis = new ZipInputStream(is);
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				String entryFilePath = entry.getName().replace('\\', File.separatorChar);
				File outFile = new File(toDir, entryFilePath);
				if (entry.isDirectory()) {
					if (!outFile.mkdirs()) {
						// 解凍先ファイルフォルダの作成に失敗した場合、処理を中止する。
						LogUtil.w(TAG, "Making Directory failed;" + outFile.getAbsolutePath());
						return;
					}
				} else {
					writeData(zis, new FileOutputStream(outFile));
					zis.closeEntry();
				}
			}
		} finally {
			IOUtils.closeQuietly(zis);
			LogUtil.d(TAG, "[OUT] unzip()");
		}
	}

	/**
	 * コピーします。<br>
	 *
	 * @param in InputStream
	 * @param out OutputStream
	 * @throws IOException
	 */
	private void copyData(final InputStream in, final OutputStream out) throws IOException {
		LogUtil.d(TAG, "[IN ] copyData()");

		BufferedInputStream bis = new BufferedInputStream(in);
		try {
			writeData(bis, out);
		} finally {
			IOUtils.closeQuietly(bis);
			LogUtil.d(TAG, "[OUT] copyData()");
		}
	}

	/**
	 * 書き込みを行います。<br>
	 *
	 * @param is InputStream
	 * @param os OutputStream
	 * @throws IOException
	 */
	private void writeData(final InputStream is, final OutputStream os) throws IOException {
		LogUtil.d(TAG, "[IN ] writeData()");

		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(os);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ( (len = is.read(buffer, 0, buffer.length)) > 0) {
				bos.write(buffer, 0, len);
			}
			bos.flush();
		} finally {
			IOUtils.closeQuietly(bos);
			LogUtil.d(TAG, "[OUT] writeData()");
		}
	}
}
