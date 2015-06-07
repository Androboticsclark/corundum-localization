/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;

import com.corundumPro.common.constant.Const;
import com.corundumPro.common.exception.ArcAppException;

/**
 * アーカイブユーティリティクラス.
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class ArchiveUtil {
	static final String TAG = "ArchiveUtil";

	/**
	 * デフォルトコンストラクタ.
	 */
	private ArchiveUtil() {
	}

	/**
	 * ZIPファイルの解凍処理を行います。
	 *
	 * @param targetPath 解凍先のフォルダ
	 * @param zipFile 解凍するZIPファイル
	 *
	 * @return 解凍されたZIPファイルの一覧
	 * @throws ArcAppException アプリケーション例外
	 */
	public static List<File> unCompress(final File targetPath , final File zipFile) throws ArcAppException {
		return unCompress(targetPath,zipFile, Const.CHARSET_UTF8);
	}

	/**
	 * ZIPファイルの解凍処理を行います。
	 *
	 * @param targetPath 解凍先のフォルダ
	 * @param zipFile 解凍するZIPファイル
	 * @param encording ファイルのエンコード
	 *
	 * @return 解凍されたZIPファイルの一覧
	 * @throws ArcAppException アプリケーション例外
	 */
	public static List<File> unCompress(final File targetPath, final File zipFile, final String encording) throws ArcAppException {
		List<File> list = new ArrayList<File>();

		ZipArchiveInputStream zis = null;
		File tmpFile = null;

		try {
			zis = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile)) ,encording);

			ZipArchiveEntry entry = null;
			while (( entry = zis.getNextZipEntry()) != null) {
				tmpFile = new File(targetPath,entry.getName());
				list.add(tmpFile);

				// ディレクトリの場合は、自身を含むディレクトリを作成
				if (entry.isDirectory()) {
					if (!tmpFile.exists() && !tmpFile.mkdirs()) {
						LogUtil.e(TAG, "failed to make directory :" + tmpFile.getAbsolutePath());
						continue;
					}
				} else {
					// ファイルの場合は、自身の親となるディレクトリを作成
					if (!tmpFile.getParentFile().exists() && !tmpFile.getParentFile().mkdirs()) {
						LogUtil.e(TAG, "failed to make directory :" + tmpFile.getAbsolutePath());
						continue;
					}

					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tmpFile), Const.BUF_SIZE);
					try {
						IOUtils.copy(zis,out);

					} catch (IOException zex) {
						LogUtil.e(TAG, "failed to copy file :" + tmpFile.getAbsolutePath() + " " + zex.getMessage());
						continue;

					} finally {
						IOUtils.closeQuietly(out);
					}
				}
			}

		} catch(FileNotFoundException ex) {
			if (tmpFile != null) {
				LogUtil.e(TAG, "tmpFile:" + tmpFile.getAbsolutePath() + " " + ex.getMessage());
			}
			throw new ArcAppException(ex);
		} catch(IOException ex) {
			LogUtil.e(TAG, "tmpFile:" + tmpFile.getAbsolutePath() + " " + ex.getMessage());
			throw new ArcAppException(ex);
		} finally {
			IOUtils.closeQuietly(zis);
		}

		return list;
	}

	/**
	 * ファイルまたはフォルダを圧縮します。
	 *
	 * @param target 圧縮対象ファイルまたはフォルダ
	 * @param zipFile ZIPファイル名
	 * @return ZIPファイルのエントリーファイル名一覧
	 * @throws ArcAppException アプリケーション例外
	 */
	public static List<File> compress(final File target, final File zipFile) throws ArcAppException {
		File[] files = null;

		files = new File[] {target};

		return compress(files, zipFile, Const.CHARSET_UTF8);
	}

	/**
	 * 指定されたファイル又はフォルダ内に存在するファイルをZIPファイルに圧縮します。
	 *
	 * @param files 圧縮対象のファイル配列
	 * @param zipFile ZIPファイル名
	 * @param encording
	 *
	 * @return ZIPファイルのエントリーファイル名一覧
	 * @throws ArcAppException アプリケーション例外
	 */
	public static List<File> compress(final File[] files, final File zipFile, final String encording)
									throws ArcAppException {
		List<File> list = new ArrayList<File>();
		ZipArchiveOutputStream zos = null;

		try {
			zos = new ZipArchiveOutputStream(new BufferedOutputStream (new FileOutputStream(zipFile), Const.BUF_SIZE));
			if (encording != null) {
				zos.setEncoding(encording);
			}

			// ZIP圧縮処理を行います
			addZipEntry(list, zos, "" , files);	// 開始フォルダは、空文字列とします。

		} catch(FileNotFoundException ex) {
			LogUtil.e(TAG, "tmpFile:" + zipFile.getAbsolutePath() + " " + ex.getMessage());
			throw new ArcAppException(ex);

		} finally {
			IOUtils.closeQuietly(zos);
		}

		return list;
	}

	/**
	 * ZIP圧縮処理を行います。
	 *
	 * @param list ZIPファイルのエントリーファイル名一覧
	 * @param zos ZIP用OutputStream
	 * @param prefix 圧縮時のフォルダ
	 * @param files 圧縮対象のファイル配列
	 * @throws ArcAppException アプリケーション例外
	 */
	private static void addZipEntry(final List<File> list, final ZipArchiveOutputStream zos, final String prefix, final File[] files)
								throws ArcAppException {
		File tmpFile = null;
		try {
			for (File fi : files) {
				tmpFile = fi;				// エラー時のファイル
				list.add(fi);
				if (fi.isDirectory()) {
					String entryName = prefix + fi.getName() + "/" ;
					ZipArchiveEntry zae = new ZipArchiveEntry(entryName);
					zos.putArchiveEntry(zae);
					zos.closeArchiveEntry();

					addZipEntry(list, zos, entryName, fi.listFiles());

				} else {
					String entryName = prefix + fi.getName() ;
					ZipArchiveEntry zae = new ZipArchiveEntry(entryName);
					zos.putArchiveEntry(zae);
					InputStream is = new BufferedInputStream(new FileInputStream(fi), Const.BUF_SIZE);
					IOUtils.copy(is,zos);
					zos.closeArchiveEntry();
					IOUtils.closeQuietly(is);
				}
			}
		} catch(FileNotFoundException ex) {
			LogUtil.e(TAG, "tmpFile:" + tmpFile.getAbsolutePath() + " " + ex.getMessage());
			throw new ArcAppException(ex);

		} catch(IOException ex) {
			LogUtil.e(TAG, "tmpFile:" + tmpFile.getAbsolutePath() + " " + ex.getMessage());
			throw new ArcAppException(ex);

		}
	}
}
