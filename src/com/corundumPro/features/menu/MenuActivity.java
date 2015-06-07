package com.corundumPro.features.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.dao.DBAdapter;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.features.appsmanager.ApplicationListActivity;
import com.corundumPro.features.editor.FileListActivity;
import com.corundumPro.features.home.HomeActivity;
import com.corundumPro.features.introduction.IntroductionActivity;
import com.corundumPro.features.setting.MainSettingActivity;
import com.corundumPro.features.setting.SubSettingActivity;
import com.corundumPro.features.shortcut.ShortcutActivity;
import com.corundumPro.features.whitelist.WhiteListActivity;

/**
 * MenuActivityクラス
 * <p>
 * 「メニューアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class MenuActivity extends BaseActivity {
	static final String TAG = "MenuActivity";

	/*
	 * 画面
	 */
	/** コランダムとは？(PDF) */
	private Button menu_button1;

	/** 管理者用モード */
	private Button menu_button2;

	/** オペレーションモード */
	private Button menu_button3;

	/** フルスクリーンモード */
	private Button menu_button4;

	/** 導入処理 */
	private Button menu_button5;

	/** その他導入処理 */
	private Button menu_button6;

	/** ホワイトリスト設定 */
	private Button menu_button7;

	/** 起動アイコン設定 */
	private Button menu_button8;

	/** コランダムエディタ */
	private Button menu_button9;

	/** Top */
	private Button menu_button10;

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

		/* Viewにレイアウト設定 */
		setContentView(R.layout.menu);

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

		/* コランダムとは？(PDF)ボタンリスナ */
		menu_button1 = (Button)findViewById(R.id.menu_button1);
		menu_button1.setOnClickListener(introductionListener);

		/* 管理者用モードボタンリスナ */
		menu_button2 = (Button)findViewById(R.id.menu_button2);
		menu_button2.setOnClickListener(adminHomeListener);

		/* オペレーションモードボタンリスナ */
		menu_button3 = (Button)findViewById(R.id.menu_button3);
		menu_button3.setOnClickListener(operationHomeListener);

		/* フルスクリーンモードボタンリスナ */
		menu_button4 = (Button)findViewById(R.id.menu_button4);
		menu_button4.setOnClickListener(fullScreenHomeListener);

		/* 導入処理ボタンリスナ */
		menu_button5 = (Button)findViewById(R.id.menu_button5);
		menu_button5.setOnClickListener(mainSettingListener);

		/* その他導入処理ボタンリスナ */
		menu_button6 = (Button)findViewById(R.id.menu_button6);
		menu_button6.setOnClickListener(subSettingListener);

		/* ホワイトリスト設定ボタンリスナ */
		menu_button7 = (Button)findViewById(R.id.menu_button7);
		menu_button7.setOnClickListener(whiteListListener);

		/* 起動アイコン設定ボタンリスナ */
		menu_button8 = (Button)findViewById(R.id.menu_button8);
		menu_button8.setOnClickListener(shortcutListener);

		/* コランダムエディタボタンリスナ */
		menu_button9 = (Button)findViewById(R.id.menu_button9);
		menu_button9.setOnClickListener(corundumEditorListener);

		/* Topボタンリスナ */
		menu_button10 = (Button)findViewById(R.id.menu_button10);
		menu_button10.setOnClickListener(topListener);

		LogUtil.d(TAG, "[OUT] initViews()");
	}

	/**
	 * コランダムとは？(PDF)ボタンリスナ処理
	 * <p>
	 * コランダムとは？(PDF)ボタンが押下された時にコールされる。<br>
	 * IntroductionActivityを起動する。
	 * </p>
	 */
	private View.OnClickListener introductionListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] introductionListener::onClick()");

			/* IntroductionActivity起動 */
			LogUtil.d(TAG, "IntroductionActivity");
			Intent intent = new Intent(MenuActivity.this, IntroductionActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] introductionListener::onClick()");
		}
	};

	/**
	 * 導入処理ボタンリスナ処理
	 * <p>
	 * 導入処理ボタンが押下された時にコールされる。<br>
	 * MainSettingActivityを起動する。
	 * </p>
	 */
	private View.OnClickListener mainSettingListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] mainSettingListener::onClick()");

			/* MainSettingActivity起動 */
			LogUtil.d(TAG, "MainSettingActivity");
			Intent intent = new Intent(MenuActivity.this, MainSettingActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] mainSettingListener::onClick()");
		}
	};

	/**
	 * その他導入処理ボタンリスナ処理
	 * <p>
	 * その他導入処理ボタンが押下された時にコールされる。<br>
	 * SubSettingActivityを起動する。
	 * </p>
	 */
	private View.OnClickListener subSettingListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] subSettingListener::onClick()");

			/* SubSettingActivity起動 */
			LogUtil.d(TAG, "SubSettingActivity");
			Intent intent = new Intent(MenuActivity.this, SubSettingActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] subSettingListener::onClick()");
		}
	};

	/**
	 * ホワイトリスト設定ボタンリスナ処理
	 * <p>
	 * ホワイトリスト設定ボタンが押下された時にコールされる。<br>
	 * WhiteListActivityを起動する。
	 * </p>
	 */
	private View.OnClickListener whiteListListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] whiteListListener::onClick()");

			/* WhiteListActivity起動 */
			LogUtil.d(TAG, "WhiteListActivity");
			Intent intent = new Intent(MenuActivity.this, WhiteListActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] whiteListListener::onClick()");
		}
	};

	/**
	 * 起動アイコン設定ボタンリスナ処理
	 * <p>
	 * 起動アイコン設定ボタンが押下された時にコールされる。<br>
	 * ShortcutActivityを起動する。
	 * </p>
	 */
	private View.OnClickListener shortcutListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] shortcutListener::onClick()");

			/* ShortcutActivity起動 */
			LogUtil.d(TAG, "ShortcutActivity");
			Intent intent = new Intent(MenuActivity.this, ShortcutActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] shortcutListener::onClick()");
		}
	};

	/**
	 * コランダムエディタボタンリスナ処理
	 * <p>
	 * コランダムエディタボタンが押下された時にコールされる。<br>
	 * FileListActivityを起動する。
	 * </p>
	 */
	private View.OnClickListener corundumEditorListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] corundumEditorListener::onClick()");

			/* FileListActivity起動 */
			LogUtil.d(TAG, "FileListActivity");
			Intent intent = new Intent(MenuActivity.this, FileListActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] corundumEditorListener::onClick()");
		}
	};

	/**
	 * 管理者用モードボタンリスナ処理
	 * <p>
	 * 管理者用モードボタンが押下された時にコールされる。<br>
	 * 管理者用モードでHomeActivityを起動する。
	 * </p>
	 */
	private View.OnClickListener adminHomeListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] adminHomeListener::onClick()");

			/* WEB表示モード設定 */
			editor.putInt(SystemInfo.KEY_CURRENT_WEB_MODE, DBAdapter.WEB_MODE_ADMIN);
			editor.putBoolean(SystemInfo.KEY_BOTTOM_BAR_FLAG, true);
			editor.commit();

			/* DB保存 */
			saveDataBase();

			/* HomeActivity起動 */
			LogUtil.d(TAG, "HomeActivity");
			Intent intent = new Intent(MenuActivity.this, HomeActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] adminHomeListener::onClick()");
		}
	};

	/**
	 * オペレーションモードボタンリスナ処理
	 * <p>
	 * オペレーションモードボタンが押下された時にコールされる。<br>
	 * オペレーションモードでHomeActivityを起動する。
	 * </p>
	 */
	private View.OnClickListener operationHomeListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] operationHomeListener::onClick()");

			/* WEB表示モード設定 */
			editor.putInt(SystemInfo.KEY_CURRENT_WEB_MODE, DBAdapter.WEB_MODE_OPERATION);
			editor.putBoolean(SystemInfo.KEY_BOTTOM_BAR_FLAG, true);
			editor.commit();

			/* DB保存 */
			saveDataBase();

			/* HomeActivity起動 */
			LogUtil.d(TAG, "HomeActivity");
			Intent intent = new Intent(MenuActivity.this, HomeActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] operationHomeListener::onClick()");
		}
	};

	/**
	 * フルスクリーンモードボタンリスナ処理
	 * <p>
	 * フルスクリーンモードボタンが押下された時にコールされる。<br>
	 * フルスクリーンモードでHomeActivityを起動する。
	 * </p>
	 */
	private View.OnClickListener fullScreenHomeListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] fullScreenHomeListener::onClick()");

			/* WEB表示モード設定 */
			editor.putInt(SystemInfo.KEY_CURRENT_WEB_MODE, DBAdapter.WEB_MODE_FULL);
			editor.putBoolean(SystemInfo.KEY_BOTTOM_BAR_FLAG, false);
			editor.commit();

			/* DB保存 */
			saveDataBase();

			/* HomeActivity起動 */
			LogUtil.d(TAG, "HomeActivity");
			Intent intent = new Intent(MenuActivity.this, HomeActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] fullScreenHomeListener::onClick()");
		}
	};

	/**
	 * Topボタンリスナ処理
	 * <p>
	 * Topボタンが押下された時にコールされる。<br>
	 * ApplicationListActivityを起動する。
	 * </p>
	 */
	private View.OnClickListener topListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] topListener::onClick()");

			/* ApplicationListActivity起動 */
			LogUtil.d(TAG, "ApplicationListActivity");
			Intent applicationIntent = new Intent(getApplication(), ApplicationListActivity.class);
			startActivity(applicationIntent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] topListener::onClick()");
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
			/* ApplicationListActivity起動 */
			LogUtil.d(TAG, "ApplicationListActivity");
			Intent intent = new Intent(this, ApplicationListActivity.class);
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