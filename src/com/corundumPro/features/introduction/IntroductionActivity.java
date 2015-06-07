package com.corundumPro.features.introduction;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.dao.DBAdapter;
import com.corundumPro.common.util.CheckUtil;
import com.corundumPro.common.util.FileUtil;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.features.menu.MenuActivity;

/**
 * IntroductionActivityクラス
 * <p>
 * 「イントロダクションアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class IntroductionActivity extends BaseActivity {
	static final String TAG = "IntroductionActivity";

	/**
	 * 作成処理
	 * <p>
	 * onCreate()のオーバーライド<br>
	 * アクティビティ生成時にコールされる。
	 * </p>
	 * @param bundle バンドル
	 */
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		LogUtil.d(TAG, "[IN ] onCreate()");

		/* PDFリーダー起動 */
		LogUtil.d(TAG, "REQUEST_PDF_VIEWER:" + Const.REQUEST_PDF_VIEWER);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		File html = new File(FileUtil.getBaseDirPath(this) + "/www");
		String pdf_path = "file://" + html.getPath() + "/" + DBAdapter.INTRODUCTION_PDF;
		intent.setDataAndType(Uri.parse(pdf_path), "application/pdf");

		/* インテントチェック */
		if (true == CheckUtil.checkIntent(getApplicationContext(), intent)) {
			/* ギャラリーアプリ呼び出し */
			startActivityForResult(intent, Const.REQUEST_PDF_VIEWER);
		} else {
			/* インテントなし */
			Toast.makeText(getApplicationContext(), R.string.err_no_intent_pdf_reader, Toast.LENGTH_LONG).show();
			finish();
		}

		LogUtil.d(TAG, "[OUT] onCreate()");
	}

	/**
	 * 処理結果取得処理
	 * <p>
	 * onActivityResult()のオーバーライド<br>
	 * 別インテントからの処理結果受信を行う。
	 * </p>
	 * @param requestCode リクエストコード
	 * @param resultCode 処理結果コード
	 * @param data インテント
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.d(TAG, "[IN ] onActivityResult()");
		LogUtil.d(TAG, "requestCode:" + requestCode);
		LogUtil.d(TAG, "resultCode:" + resultCode);

		/* MenuActivity起動 */
		LogUtil.d(TAG, "MenuActivity");
		Intent intent = new Intent(this, MenuActivity.class);
		startActivity(intent);

		/* 終了 */
		finish();

		LogUtil.d(TAG, "[OUT] onActivityResult()");
	}

	/**
	 * 物理ボタン押下イベント処理
	 * <p>
	 * onKeyDown()のオーバーライド<br>
	 * 物理ボタン押下時にコールされる。
	 * </p>
	 * @param keyCode ボタンキーコード
	 * @param event イベントオブジェクト
	 * @return 処理結果
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtil.d(TAG, "[IN ] onKeyDown()");
		LogUtil.d(TAG, "keyCode:" + keyCode);
		LogUtil.d(TAG, "event:" + event.getAction());

		boolean result;

		/* 戻るボタン処理 */
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			/* MenuActivity起動 */
			LogUtil.d(TAG, "MenuActivity");
			Intent intent = new Intent(this, MenuActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();
		}

		result = super.onKeyDown(keyCode, event);

		LogUtil.d(TAG, "result:" + result);
		LogUtil.d(TAG, "[OUT] onKeyDown()");
		return result;
	}
}
