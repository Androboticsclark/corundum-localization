/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

/**
 * イメージユーティリティクラス.
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class ImageUtil {
	static final String TAG = "ImageUtil";

	/**
	 * デフォルトコンストラクタ.
	 */
	private ImageUtil() {
	}

	/**
	 * Bitmap読み出し処理
	 * <p>
	 * 引数で指定されたファイルパスのBitmapを取得する。<br>
	 * (Out of memory対応)
	 * </p>
	 * @param filePath Bitmapファイルパス
	 * @return 読み出したBitmapオブジェクト
	 */
	public static Bitmap readBitmap(String filePath) {
		LogUtil.d(TAG, "[IN ] readBitmap()");
		LogUtil.d(TAG, "filePath:" + filePath);

		/* 読み込み用のオプションオブジェクト生成 */
		BitmapFactory.Options options = new BitmapFactory.Options();

		/* 画像のサイズ情報だけを取得 */
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		/* 縮尺算出 */
		int scaleW = options.outWidth / 380 + 1;
		int scaleH = options.outHeight / 420 + 1;

		/*
		 * 縮尺は整数値で、2なら画像の縦横のピクセル数を1/2にしたサイズ。
		 * 3なら1/3にしたサイズで読み込む。
		 */
		int scale = Math.max(scaleW, scaleH);

		/* 画像取得 */
		options.inJustDecodeBounds = false;
		options.inSampleSize = scale;
		Bitmap image = BitmapFactory.decodeFile(filePath, options);

		LogUtil.d(TAG, "[OUT] readBitmap()");
		return image;
	}

	/**
	 * Bitmapコピー処理
	 * <p>
	 * 指定されたBitmapファイルをコピーする。
	 * </p>
	 * @param image コピー元のBitmapファイル
	 * @param root コピー先フォルダパス
	 * @param fileName コピー先ファイル名
	 */
	public static void copyBitmap(Bitmap image, File root, String fileName) {
		LogUtil.d(TAG, "[IN ] copyBitmap()");
		LogUtil.d(TAG, "root:" + root.getPath());
		LogUtil.d(TAG, "fileName:" + fileName);

		FileOutputStream output = null;

		try {
			/* ストリームオープン */
			output = new FileOutputStream(new File(root, fileName));

			/* 画像を非圧縮でコピー */
			image.compress(CompressFormat.PNG, 100, output);

			/* ストリームクローズ */
			output.close();
		} catch (Exception e) {
			LogUtil.d(TAG, e.getMessage());
		}

		LogUtil.d(TAG, "[OUT] copyBitmap()");
	}

	/**
	 * Bitmapリサイズ処理
	 * <p>
	 * 指定されたサイズでBitmapをリサイズする。<br>
	 * アスペクト比を維持持した状態でリサイズする。
	 * </p>
	 * @param image 元のBitmapファイル
	 * @param hightSize 縦サイズ
	 * @param widthSize 横サイズ
	 * @return リサイズ後のBitmap
	 */
	public static Bitmap resizeBitmap(Bitmap image, int hightSize, int widthSize) {
		LogUtil.d(TAG, "[IN ] resizeBitmap()");

		float imgHeight = image.getHeight();
		float imgWidth  = image.getWidth();
		float widthRatio;
		float hightRatio;
		float ratio;
		float height = hightSize;
		float width  = widthSize;

		/* 縦横の縮尺比率を取得 */
		hightRatio = height / imgHeight;
		widthRatio = width / imgWidth;

		/* より小さくリサイズする必要がある側(縦横)の縮尺比率を使用する。 */
		if (widthRatio < hightRatio) {
			ratio = widthRatio;
		} else {
			ratio = hightRatio;
		}

		/* リサイズ後の縦横サイズ取得 */
		height = imgHeight * ratio;
		width = imgWidth * ratio;

		LogUtil.d(TAG, "hightSize:" + hightSize);
		LogUtil.d(TAG, "widthSize:" + widthSize);
		LogUtil.d(TAG, "imgHeight:" + imgHeight);
		LogUtil.d(TAG, "imgWidth:" + imgWidth);
		LogUtil.d(TAG, "hightRatio:" + hightRatio);
		LogUtil.d(TAG, "widthRatio:" + widthRatio);
		LogUtil.d(TAG, "ratio:" + ratio);
		LogUtil.d(TAG, "height:" + height);
		LogUtil.d(TAG, "width:" + width);

		/* リサイズ */
		Bitmap resizeImage = Bitmap.createScaledBitmap(image, (int)width, (int)height, true);

		LogUtil.d(TAG, "[OUT] resizeBitmap()");
		return resizeImage;
	}

	/**
	 * 画像ファイル名取得処理
	 * <p>
	 * 画像ファイル名の生成を行うう。
	 * </p>
	 * @return 画像ファイル名
	 */
	public static String getOrgPicFileName() {
		LogUtil.d(TAG, "[IN ] getOrgPicFileName()");

		StringBuilder result = new StringBuilder();
		result.append(DateUtil.formatDate(new Date(), DateUtil.FORMAT_YYYYMMDDHHMMSS));
		result.append("_org.jpg");

		LogUtil.d(TAG, "[OUT] getOrgPicFileName()");
		return result.toString();
	}

	/**
	 * 画像ファイル名取得処理
	 * <p>
	 * 画像ファイル名の生成を行うう。
	 * </p>
	 * @return 画像ファイル名
	 */
	public static String getResPicFileName() {
		LogUtil.d(TAG, "[IN ] getResPicFileName()");

		StringBuilder result = new StringBuilder();
		result.append(DateUtil.formatDate(new Date(), DateUtil.FORMAT_YYYYMMDDHHMMSS));
		result.append("_res.jpg");

		LogUtil.d(TAG, "[OUT] getResPicFileName()");
		return result.toString();
	}
}
