package com.corundumPro.features.shortcut;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.dao.DBAdapter;
import com.corundumPro.common.util.CheckUtil;
import com.corundumPro.common.util.FileUtil;
import com.corundumPro.common.util.ImageUtil;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.common.util.StringUtil;
import com.corundumPro.features.menu.MenuActivity;
import com.corundumPro.features.splash.SplashActivity;

/**
 * ShortcutActivityクラス
 * <p>
 * 「ショートカットアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class ShortcutActivity extends BaseActivity {
	static final String TAG = "ShortcutActivity";

	/*
	 * 画面
	 */
	/** アプリ名エディットボックス */
	private EditText shortcut_editText1;

	/** アイコン画像選択 */
	private Button shortcut_button1;

	/** アイコン画像 */
	private ImageView shortcut_imageView1;

	/** URLエディットボックス */
	private EditText shortcut_editText2;

	/** 起動アイコン作成 */
	private Button shortcut_button2;

	/** メニューボタン */
	private Button shortcut_button3;


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
		setContentView(R.layout.shortcut);

		/* View初期化 */
		initViews();

		/* 画面のフォーカスを外す */
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		/* ショートカット名設定 */
		shortcut_editText1.setText(shortcutInfo.getShortcutName());

		/* アイコン設定 */
		File root = new File(FileUtil.getBaseDirPath(this));
		String path = "file://" + root.getPath() + "/" + DBAdapter.SHORTCUT_ICON;
		shortcut_imageView1.setImageURI(Uri.parse(path));

		/* ホームURL設定 */
		shortcut_editText2.setText(shortcutInfo.getHomeWebUrl());

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

		/* ショートカット名 */
		shortcut_editText1 = (EditText)findViewById(R.id.shortcut_editText1);

		/* アイコン画像選択ボタン */
		shortcut_button1 = (Button)findViewById(R.id.shortcut_button1);
		shortcut_button1.setOnClickListener(iconListener);

		/* アイコン画像 */
		shortcut_imageView1 = (ImageView)findViewById(R.id.shortcut_imageView1);

		/* URLエディットボックス */
		shortcut_editText2 = (EditText)findViewById(R.id.shortcut_editText2);

		/* 起動アイコン作成ボタン */
		shortcut_button2 = (Button)findViewById(R.id.shortcut_button2);
		shortcut_button2.setOnClickListener(shortcutListener);

		/* メニューボタン */
		shortcut_button3 = (Button)findViewById(R.id.shortcut_button3);
		shortcut_button3.setOnClickListener(menuListener);

		LogUtil.d(TAG, "[OUT] initViews()");
	}

	/**
	 * アイコン画像選択ボタンリスナ処理
	 * <p>
	 * アイコン画像選択ボタンが押下された時にコールされる。<br>
	 * ギャラリーを起動しショートカット用のアイコンを選択させる。
	 * </p>
	 */
	private View.OnClickListener iconListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] iconListener::onClick()");

			/* ギャラリー呼び出し */
			LogUtil.d(TAG, "REQUEST_GALLERY:" + Const.REQUEST_GALLERY);
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);

			/* インテントチェック */
			if (true == CheckUtil.checkIntent(getApplicationContext(), intent)) {
				/* ギャラリーアプリ呼び出し */
				startActivityForResult(intent, Const.REQUEST_GALLERY);
			} else {
				/* インテントなし */
				Toast.makeText(getApplicationContext(), R.string.err_no_intent_garally, Toast.LENGTH_LONG).show();
			}

			LogUtil.d(TAG, "[OUT] iconListener::onClick()");
		}
	};

	/**
	 * 起動アイコン作成ボタンリスナ処理
	 * <p>
	 * 起動アイコン作成ボタンが押下された時にコールされる。<br>
	 * ショートカットを作成する。
	 * </p>
	 */
	private View.OnClickListener shortcutListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] shortcutListener::onClick()");

			/* ショートカット名取得 */
			String shortcut_name = shortcut_editText1.getText().toString();

			/* ショートカットアイコン取得 */
			BitmapDrawable bitmapDrawable = (BitmapDrawable)shortcut_imageView1.getDrawable();
			Bitmap shortcut_icon = bitmapDrawable.getBitmap();

			/* ホームURL取得 */
			String home_url = shortcut_editText2.getText().toString();

			/* ショートカットに持たせるインテント設定 */
			Intent shortcutIntent = new Intent(Intent.ACTION_VIEW);
			shortcutIntent.setClassName(getApplication(), SplashActivity.class.getName());

			 /* 新レコード追加(戻り値はインクリメント後のショートカットID) */
			dbAdapter.open();
			shortcutIntent.setData(Uri.parse(String.valueOf(dbAdapter.insertShortcutInfo(shortcut_name, home_url))));
			dbAdapter.close();

			/* ショートカット作成 */
			Intent intent = new Intent();
			intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
			intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcut_name);
			intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, shortcut_icon);
			intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
			sendBroadcast(intent);

			Toast.makeText(getApplicationContext(), R.string.msg_create_shortcut, Toast.LENGTH_LONG).show();

			LogUtil.d(TAG, "[OUT] shortcutListener::onClick()");
		}
	};

	/**
	 * 処理結果取得処理
	 * <p>
	 * onActivityResult()のオーバーライド<br>
	 * 別インテントからの処理結果受信を行う。
	 * </p>
	 * @param requestCode リクエストコード
	 * @param resultCode 処理結果コード
	 * @param intent インテント
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		LogUtil.d(TAG, "[IN ] onActivityResult()");
		LogUtil.d(TAG, "requestCode:" + requestCode);
		LogUtil.d(TAG, "resultCode:" + resultCode);

		if ((Const.REQUEST_GALLERY == requestCode) && (RESULT_OK == resultCode)) {
			try {
				/* Uri -> File Path変換 */
				String filePath = StringUtil.convertUriToFilePath(getApplicationContext(), intent.getData());

				/* Bitmap取得 */
				Bitmap img = ImageUtil.readBitmap(filePath);

				/* appwidgetの1*1の大きさへリサイズ */
				Bitmap bmp = ImageUtil.resizeBitmap(img, 72, 72);

				/* 選択した画像を表示 */
				shortcut_imageView1.setImageBitmap(bmp);
			} catch (Exception e) {
				LogUtil.d(TAG, e.getMessage());
			}
		}

		LogUtil.d(TAG, "[OUT] onActivityResult()");
	}

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

	/**
	 * メニューボタンリスナ処理
	 * <p>
	 * メニューボタンが押下された時にコールされる。<br>
	 * MenuActivityを起動する。
	 * </p>
	 */
	private View.OnClickListener menuListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] menuListener::onClick()");

			/* DB保存 */
			saveDataBase();

			/* MenuActivity起動 */
			LogUtil.d(TAG, "MenuActivity");
			Intent intent = new Intent(ShortcutActivity.this, MenuActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] menuListener::onClick()");
		}
	};
}
