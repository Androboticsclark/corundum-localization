package com.corundumPro.features.actionsheet;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.util.LogUtil;

/**
 * ActionSheetActivityクラス
 * <p>
 * 「アクションシートアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class ActionSheetActivity extends BaseActivity {
	static final String TAG = "ActionSheetActivity";

	/*
	 * 画面
	 */
	/** ツールバー表示/非表示 */
	private Button action_sheet_button1;

	/** WEBブラウザ起動 */
	private Button action_sheet_button2;

	/** このアプリを強制終了 */
	private Button action_sheet_button3;

	/** 環境設定メニューへ */
	private Button action_sheet_button4;

	/** キャンセル */
	private Button action_sheet_button5;

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

		/* タイトルバーを非表示にする */
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* Viewにレイアウト設定 */
		setContentView(R.layout.action_sheet);

		/* View初期化 */
		initViews();

		LogUtil.d(TAG, "[OUT] onCreate()");
	}

	/**
	 * View初期化処理
	 * <p>
	 * Viewの初期化を行う。
	 * </p>
	 */
	public void initViews() {
		LogUtil.d(TAG, "[IN ] initViews()");

		/* ツールバー表示/非表示ボタンリスナ */
		action_sheet_button1 = (Button)findViewById(R.id.action_sheet_button1);
		action_sheet_button1.setOnClickListener(toolbarListener);

		/* WEBブラウザ起動ボタンリスナ */
		action_sheet_button2 = (Button)findViewById(R.id.action_sheet_button2);
		action_sheet_button2.setOnClickListener(execWebBrowserListener);

		/* このアプリを強制終了ボタンリスナ */
		action_sheet_button3 = (Button)findViewById(R.id.action_sheet_button3);
		action_sheet_button3.setOnClickListener(exitListener);

		/* 環境設定メニューへボタンリスナ */
		action_sheet_button4 = (Button)findViewById(R.id.action_sheet_button4);
		action_sheet_button4.setOnClickListener(menuListener);

		/* キャンセルボタンリスナ */
		action_sheet_button5 = (Button)findViewById(R.id.action_sheet_button5);
		action_sheet_button5.setOnClickListener(cancelListener);

		LogUtil.d(TAG, "[OUT] initViews()");
	}

	/**
	 * ツールバー表示/非表示ボタンリスナ処理
	 * <p>
	 * ツールバー表示/非表示ボタンが押下された時にコールされる。<br>
	 * 画面下部のツールバーの表示/非表示を切り替える。
	 * </p>
	 */
	private View.OnClickListener toolbarListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] toolbarListener::onClick()");

			setResult(Const.ACTION_SHEET_TOOLBAR);
			finish();

			LogUtil.d(TAG, "[OUT] toolbarListener::onClick()");
		}
	};

	/**
	 * WEBブラウザ起動ボタンリスナ処理
	 * <p>
	 * WEBブラウザ起動ボタンが押下された時にコールされる。<br>
	 * WEBブラウザを起動する。
	 * </p>
	 */
	private View.OnClickListener execWebBrowserListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] execWebBrowserListener::onClick()");

			setResult(Const.ACTION_SHEET_WEB_BORWSER);
			finish();

			LogUtil.d(TAG, "[OUT] execWebBrowserListener::onClick()");
		}
	};

	/**
	 * このアプリを強制終了ボタンリスナ処理
	 * <p>
	 * このアプリを強制終了ボタンが押下された時にコールされる。<br>
	 * 強制終了する。
	 * </p>
	 */
	private View.OnClickListener exitListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] exitListener::onClick()");

			setResult(Const.ACTION_SHEET_EXIT);
			finish();

			LogUtil.d(TAG, "[OUT] exitListener::onClick()");
		}
	};

	/**
	 * 環境設定メニューへボタンリスナ処理
	 * <p>
	 * 環境設定メニューへボタンが押下された時にコールされる。<br>
	 * 環境設定メニューを起動する。
	 * </p>
	 */
	private View.OnClickListener menuListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] menuListener::onClick()");

			setResult(Const.ACTION_SHEET_MENU);
			finish();

			LogUtil.d(TAG, "[OUT] menuListener::onClick()");
		}
	};

	/**
	 * キャンセルボタンリスナ処理
	 * <p>
	 * キャンセルボタンが押下された時にコールされる。<br>
	 * キャンセルする。
	 * </p>
	 */
	private View.OnClickListener cancelListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] cancelListener::onClick()");

			setResult(Const.ACTION_SHEET_CANCEL);
			finish();

			LogUtil.d(TAG, "[OUT] cancelListener::onClick()");
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
			 setResult(Const.ACTION_SHEET_CANCEL);
			 finish();
		}

		result = super.onKeyDown(keyCode, event);

		LogUtil.d(TAG, "result:" + result);
		LogUtil.d(TAG, "[OUT] onKeyDown()");
		return result;
	}
}
