package com.corundumPro.features.editor;

import java.io.BufferedReader;
import java.io.FileReader;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.dao.DBAdapter;
import com.corundumPro.common.dialog.SingleChoiceDialog;
import com.corundumPro.common.dto.SelectorDto;
import com.corundumPro.common.exception.ArcAppException;
import com.corundumPro.common.exception.ArcRuntimeException;
import com.corundumPro.common.function.FileFunction;
import com.corundumPro.common.function.SelectorFunction;
import com.corundumPro.common.util.FileUtil;
import com.corundumPro.common.util.LogUtil;

/**
 * FileListDetailActivityクラス
 * <p>
 * 「ファイルリストディレールアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class FileListDetailActivity extends BaseActivity{
	static final String TAG = "FileListDetailActivity";

	/*
	 * 画面
	 */
	/** エディットボックス */
	private EditText file_list_detail_editText1;

	/** 保存ボタン */
	private Button file_list_detail_Button1;

	/** キャンセルボタン */
	private Button file_list_detail_Button2;

	/** プレビューボタン */
	private Button file_list_detail_Button3;

	/** ファイルパス */
	private String filePath;

	/** ダイアログ選択肢管理オブジェクト */
	private SelectorFunction selectorFunction = new SelectorFunction();


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

		/* ファイルパス受け取り */
		Intent intent = getIntent();
		filePath = intent.getStringExtra("filePath");
		LogUtil.d(TAG, "filePath:" + filePath);

		/* Viewにレイアウト設定 */
		setContentView(R.layout.file_list_detail);

		/* View初期化 */
		initViews();

		/* 画面のフォーカスを外す */
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		/* ファイル読み出し */
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filePath));
			while (bufferedReader.ready()) {
				/* 改行がない状態なので追加する */
				String line = bufferedReader.readLine() + "\n";

				/* エディットボックスに読み出した内容を追加する */
				file_list_detail_editText1.append(line);
			}

			bufferedReader.close();
		} catch (Exception e) {
			LogUtil.d(TAG, e.getMessage());

			if (null != bufferedReader) {
				try {
					bufferedReader.close();
				} catch (Exception e1) {
					LogUtil.d(TAG, e1.getMessage());
				}
			}
		}

		LogUtil.d(TAG, "[OUT] onCreate()");
	}

	/**
	 * レジューム処理
	 * <p>
	 * onResume()のオーバーライド<br>
	 * アクティビティ再開時にコールされる。
	 * </p>
	 */
	@Override
	public void onResume() {
		LogUtil.d(TAG, "[IN ] onResume()");
		super.onResume();
		LogUtil.d(TAG, "[OUT] onResume()");
	}

	/**
	 * フラグメントレジューム処理
	 * <p>
	 * onResumeFragments()のオーバーライド<br>
	 * アクティビティ表示時にコールされる。
	 * </p>
	 */
	@Override
	public void onResumeFragments() {
		LogUtil.d(TAG, "[IN ] onResumeFragments()");
		super.onResumeFragments();
		LogUtil.d(TAG, "[OUT] onResumeFragments()");
	}

	/**
	 * 一時停止処理
	 * <p>
	 * onPause()のオーバーライド<br>
	 * アクティビティ一時停止時にコールされる。
	 * </p>
	 */
	@Override
	public void onPause() {
		LogUtil.d(TAG, "[IN ] onPause()");
		super.onPause();
		LogUtil.d(TAG, "[OUT] onPause()");
	}

	/**
	 * 停止処理
	 * <p>
	 * onStop()のオーバーライド<br>
	 * アクティビティ停止時にコールされる。
	 * </p>
	 */
	@Override
	public void onStop() {
		LogUtil.d(TAG, "[IN ] onStop()");
		super.onStop();
		LogUtil.d(TAG, "[OUT] onStop()");
	}

	/**
	 * 終了処理
	 * <p>
	 * onDestroy()のオーバーライド<br>
	 * アクティビティ終了時にコールされる。
	 * </p>
	 */
	@Override
	public void onDestroy() {
		LogUtil.d(TAG, "[IN ] onDestroy()");
		super.onDestroy();
		LogUtil.d(TAG, "[OUT] onDestroy()");
	}

	/**
	 * View初期化処理
	 * <p>
	 * Viewの初期化を行う。
	 * </p>
	 */
	public void initViews() {
		LogUtil.d(TAG, "[IN ] initViews()");

		// ファイルエディットボックス
		file_list_detail_editText1 = (EditText)findViewById(R.id.file_list_detail_editText1);

		// 保存ボタン
		file_list_detail_Button1 = (Button)findViewById(R.id.file_list_detail_Button1);
		file_list_detail_Button1.setOnClickListener(saveListener);

		// キャンセルボタン
		file_list_detail_Button2 = (Button)findViewById(R.id.file_list_detail_Button2);
		file_list_detail_Button2.setOnClickListener(cancelListener);

		// プレビューボタン
		file_list_detail_Button3 = (Button)findViewById(R.id.file_list_detail_Button3);
		file_list_detail_Button3.setOnClickListener(previewListener);
		// HTMLファイルのみプレビューボタンを表示する。
		file_list_detail_Button3.setVisibility(convertVisible(FileUtil.isHtml(filePath)));

		// ダイアログ選択肢管理オブジェクトの準備
		this.prepareSelector();

		LogUtil.d(TAG, "[OUT] initViews()");
	}

	/**
	 * 保存ボタンリスナ処理
	 * <p>
	 * 保存ボタンが押下された時にコールされる。<br>
	 * エディットボックスの内容をファイルに保存する。
	 * </p>
	 */
	private View.OnClickListener saveListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] saveListener::onClick()");

			FileFunction fileFunction = new FileFunction();
			try {
				// ファイルを保存する
				fileFunction.writeFile(filePath, file_list_detail_editText1.getText().toString());

			} catch (ArcAppException e) {
				LogUtil.e(TAG, getString(e.getResourceId()) + e.getMessage());
				throw new ArcRuntimeException(e);
			}

			// 終了
			setResult(RESULT_OK);
			finish();

			LogUtil.d(TAG, "[OUT] saveListener::onClick()");
		}
	};

	/**
	 * キャンセルボタンリスナ処理
	 * <p>
	 * キャンセルボタンが押下された時にコールされる。<br>
	 * エディットボックスの内容を破棄する。
	 * </p>
	 */
	private View.OnClickListener cancelListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] cancelListener::onClick()");

			/* 終了 */
			setResult(RESULT_OK);
			finish();

			LogUtil.d(TAG, "[OUT] cancelListener::onClick()");
		}
	};

	/**
	 * プレビューボタンリスナ処理
	 * <p>
	 * プレビューボタンが押下された時にコールされる。<br>
	 * プレビューを表示する。
	 * </p>
	 */
	private View.OnClickListener previewListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] previewListener::onClick()");

			// Webモード選択ダイアログ表示
			showWebModeDialog(selectorFunction.getSelectorIndex());

			LogUtil.d(TAG, "[OUT] previewListener::onClick()");
		}
	};

	/**
	 * 物理ボタン押下イベント処理
	 * <p>
	 * onKeyDown()のオーバーライド<br>
	 * 物理ボタン押下時にコールされる。
	 * </p>
	 * @param keyCode ボタンのキーコード
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
			/* 終了 */
			setResult(RESULT_OK);
			finish();
		}

		result = super.onKeyDown(keyCode, event);

		LogUtil.d(TAG, "result:" + result);
		LogUtil.d(TAG, "[OUT] onKeyDown()");
		return result;
	}

	/**
	 * Webモード選択ダイアログを表示します。<br>
	 *
	 * @param checkedItem 選択されたアイテム
	 */
	protected void showWebModeDialog(int checkedItem) {
		LogUtil.d(TAG, "[IN ] showWebModeDialog()");
		LogUtil.d(TAG, "checkedItem:" + String.valueOf(checkedItem));

		// 表示文字列生成
		String[] items = selectorFunction.getLabels();

		SingleChoiceDialog dialogFragment = SingleChoiceDialog
				.newInstance(R.string.editor_mode_title, items, checkedItem);

		// 選択時の処理
		dialogFragment.setOnSingleChoiceClickListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				LogUtil.d(TAG, "[IN ] setOnSingleChoiceClickListener::onClick()");
				LogUtil.d(TAG, "item:" + String.valueOf(item));
				// 選択されたインデックスを登録
				selectorFunction.setSelectorIndex(item);
				LogUtil.d(TAG, "[OUT] setOnSingleChoiceClickListener::onClick()");
			}
		});

		// OKボタン押下時の処理
		dialogFragment.setOnOkClickListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				LogUtil.d(TAG, "[IN ] setOnOkClickListener::onClick()");
				LogUtil.d(TAG, "item:" + String.valueOf(item));

				// 選択されたインデックスよりWebモードを取得
				int selectedWebMode;
				try {
					selectedWebMode = Integer.parseInt(selectorFunction.getSelectorValue());
				} catch (NumberFormatException e) {
					selectedWebMode = DBAdapter.WEB_MODE_OPERATION;
				}
				LogUtil.d(TAG, "selectedWebMode:" + String.valueOf(selectedWebMode));

				// EditorPreviewActivity起動
				Intent intent = new Intent(FileListDetailActivity.this, EditorPreviewActivity.class);
				// プレビューするファイルパスを渡す
				intent.putExtra(SystemInfo.KEY_EDITOR_PREVIEW_URL, filePath);
				// プレビューするコンテンツを渡す
				intent.putExtra(SystemInfo.KEY_EDITOR_PREVIEW_CONTENTS, file_list_detail_editText1.getText().toString());
				// 選択されたWebモードを渡す
				intent.putExtra(SystemInfo.KEY_SELECTED_WEB_MODE, selectedWebMode);
				startActivityForResult(intent, Const.REQUEST_EDITOR_PREVIEW);

				LogUtil.d(TAG, "[OUT] setOnOkClickListener::onClick()");
			}
		});

		// キャンセルボタン押下時の処理
		dialogFragment.setOnCancelClickListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				LogUtil.d(TAG, "[IN ] setOnCancelClickListener::onClick()");
				dialog.dismiss();
				LogUtil.d(TAG, "[OUT] setOnCancelClickListener::onClick()");
			}
		});

		dialogFragment.show(this.getSupportFragmentManager(), TAG);

		LogUtil.d(TAG, "[OUT] showWebModeDialog()");
	}

	/**
	 * ダイアログ選択肢管理オブジェクトの準備をします。<br>
	 *
	 */
	private void prepareSelector() {

		SelectorDto selectorDto;

		// 管理者モード
		selectorDto = new SelectorDto();
		selectorDto.setLabel(getString(R.string.editor_admin));
		selectorDto.setValue(String.valueOf(DBAdapter.WEB_MODE_ADMIN));
		selectorFunction.addSelectorDto(selectorDto, false);

		// オペレーションモード(デフォルト)
		selectorDto = new SelectorDto();
		selectorDto.setLabel(getString(R.string.editor_operation));
		selectorDto.setValue(String.valueOf(DBAdapter.WEB_MODE_OPERATION));
		selectorFunction.addSelectorDto(selectorDto, true);

		// フルスクリーンモード
		selectorDto = new SelectorDto();
		selectorDto.setLabel(getString(R.string.editor_full));
		selectorDto.setValue(String.valueOf(DBAdapter.WEB_MODE_FULL));
		selectorFunction.addSelectorDto(selectorDto, false);
	}
}
