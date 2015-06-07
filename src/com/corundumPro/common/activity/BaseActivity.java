package com.corundumPro.common.activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.corundumPro.R;
import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.dao.DBAdapter;
import com.corundumPro.common.dto.ShortcutInfo;
import com.corundumPro.common.exception.ArcAppMsgException;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.features.home.HomeButtonReceiver;

/**
 * BaseActivityクラス
 * <p>
 * アクティビティのベースクラス
 * </p>
 * @author androbotics.ltd
 */
public class BaseActivity extends BaseFragmentActivity {
	static final String TAG = "BaseActivity";

	/*
	 * DB
	 */
	/** DBアダプター */
	protected DBAdapter dbAdapter;

	/** 「ショートカット情報」 */
	protected ShortcutInfo shortcutInfo;

	/** プリファレンス */
	protected SharedPreferences preference;
	protected Editor editor;

	/*
	 * レシーバー
	 */
	/** ホームボタンレシーバ */
	protected HomeButtonReceiver homeButtonReceiver = null;


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
		LogUtil.d(TAG, "[IN ] onCreate()");

		super.onCreate(bundle);

		/* プリファレンスの準備 */
		preference = getApplicationContext().getSharedPreferences(SystemInfo.PREF_SYS_INFO, Context.MODE_PRIVATE);
		editor = preference.edit();
		editor.commit();

		/* DB読み出し */
		loadDataBase();

		LogUtil.d(TAG, "[OUT] onCreate()");
	}

	@Override
	public void onResume() {
		LogUtil.d(TAG, "[IN ] onResume()");

		if (null == homeButtonReceiver) {
			/* ホームボタンレシーバ登録 */
			homeButtonReceiver = new HomeButtonReceiver();
			homeButtonReceiver.setBaseActivity(this);
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
			this.registerReceiver(homeButtonReceiver, intentFilter);
		}

		super.onResume();

		LogUtil.d(TAG, "[OUT] onResume()");
	}

	/**
	 * 破棄処理
	 * <p>
	 * onDestroy()のオーバーライド<br>
	 * アクティビティ破棄時にコールされる。
	 * </p>
	 */
	@Override
	public void onDestroy() {
		LogUtil.d(TAG, "[IN ] onDestroy()");

		if (null != homeButtonReceiver) {
			deleteReceiver();
		}

		super.onDestroy();

		LogUtil.d(TAG, "[OUT] onDestroy()");
	}

	/**
	 * DB読み出し処理
	 * <p>
	 * 「システム情報」「ショートカット情報」「一時情報」のDBを読み出す。
	 * </p>
	 */
	public void loadDataBase() {
		LogUtil.d(TAG, "[IN ] loadDataBase()");

		 /* DBアダプタ初期化 */
		dbAdapter = new DBAdapter(this);

		/* DBオープン */
		dbAdapter.open();

		/* 「ショートカット情報」テーブル読み出し */
		shortcutInfo = new ShortcutInfo();
		dbAdapter.getShortcutInfo(preference.getInt(SystemInfo.KEY_CURRENT_SHORTCUT_ID, 0), shortcutInfo);

		/* DBクローズ */
		dbAdapter.close();

		LogUtil.d(TAG, "[OUT] loadDataBase()");
	}

	/**
	 * DB保存処理
	 * <p>
	 * 「システム情報」「ショートカット情報」「一時情報」のDBを保存する。
	 * </p>
	 */
	public void saveDataBase() {
		LogUtil.d(TAG, "[IN ] saveDatabase()");

		/* DBオープン */
		dbAdapter.open();

		/* 「ショートカット情報」テーブル更新 */
		dbAdapter.updateShortcutInfo(shortcutInfo);

		/* DBクローズ */
		dbAdapter.close();

		LogUtil.d(TAG, "[OUT] saveDatabase()");
	}

	/**
	 * 設定変更イベント処理
	 * <p>
	 * onConfigurationChanged()のオーバーライド<br>
	 * 画面縦横変化時にコールされる。<br>
	 * この処理とマニュフェストの設定で、ホームアクティビティの縦横切り替え時の再起動を抑止する。
	 * </p>
	 * @param newConfig コンフィグ情報
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		LogUtil.d(TAG, "[IN ] onConfigurationChanged()");
		LogUtil.d(TAG, "[OUT] onConfigurationChanged()");
	}

	/**
	 * BroadcastReceiver削除処理
	 * <p>
	 * BroadcastReceiverを削除する。
	 * </p>
	 */
	public void deleteReceiver() {
		LogUtil.d(TAG, "[IN ] deleteReceiver()");

		/* ホームボタンレシーバ破棄 */
		unregisterReceiver(homeButtonReceiver);
		homeButtonReceiver = null;

		LogUtil.d(TAG, "[IN ] deleteReceiver()");
	}

	/**
	 * 表示可/不可を取得します。(boolean -> int)
	 *
	 * @param isVisible 表示可：true, 不可：false
	 * @return 表示可：View.VISIBLE, 不可：View.GONE
	 */
	public int convertVisible(boolean isVisible) {
		int ret = View.GONE;
		if (isVisible) {
			ret = View.VISIBLE;
		}
		return ret;
	}

	/**
	 * リフレクションを行います。
	 *
	 * @param loaderOrClass
	 * @param className	クラス名
	 * @param methodName メソッド名
	 * @param args メソッドに対する引数
	 * @return メソッドの戻り値
	 * @throws ArcAppMsgException アプリケーション例外
	 */
	protected Object invoke(Object loaderOrClass
							,String className
							,String methodName
							,Object... args)
									throws ArcAppMsgException {
		ClassLoader loader = null;
		Class<?> clazz = null;
		Object ret = null;

		if (loaderOrClass instanceof ClassLoader) {
			loader = (ClassLoader) loaderOrClass;
		} else {
			clazz = (Class<?>) loaderOrClass;
		}

		try {
			if (loader != null) {
				clazz = loader.loadClass(className);
			}
			Object obj = clazz.newInstance();
			Method method = clazz.getMethod(methodName);
			ret = method.invoke(obj, args);

		} catch (ClassNotFoundException e) {
			throw new ArcAppMsgException(R.string.err_exception, e);
		} catch (InstantiationException e) {
			throw new ArcAppMsgException(R.string.err_exception, e);
		} catch (IllegalAccessException e) {
			throw new ArcAppMsgException(R.string.err_exception, e);
		} catch (NoSuchMethodException e) {
			throw new ArcAppMsgException(R.string.err_exception, e);
		} catch (IllegalArgumentException e) {
			throw new ArcAppMsgException(R.string.err_exception, e);
		} catch (InvocationTargetException e) {

		}
		return ret;
	}
}