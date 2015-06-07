package com.corundumPro.features.whitelist;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.dto.WhiteListInfo;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.features.menu.MenuActivity;

/**
 * WhiteListActivityクラス
 * <p>
 * 「ホワイトリストアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class WhiteListActivity extends BaseActivity implements OnClickListener {
	static final String TAG = "WhiteListActivity";

	/*
	 * コンテキストメニュー
	 */
	/** コンテキストメニュー：削除 */
	static final int MENUITEM_ID_DELETE = 1;

	/*
	 * DB
	 */
	private WhiteListAdapter listAdapter;
	private List<WhiteListInfo> itemList = new ArrayList<WhiteListInfo>();

	/*
	 * 画面
	 */
	/** ホワイトリスト */
	private ListView white_list_ListView1;

	/** 新規作成ボタン */
	private Button white_list_Button1;

	/** メニューボタン */
	private Button white_list_Button2;

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
		setContentView(R.layout.white_list);

		/* View初期化 */
		initViews();

		/* ホワイトリストロード */
		listAdapter = new WhiteListAdapter(this, itemList);
		white_list_ListView1.setAdapter(listAdapter);
		loadNote();

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

		/* ホワイトリスト */
		white_list_ListView1 = (ListView)findViewById(R.id.white_list_ListView1);
		white_list_ListView1.setOnCreateContextMenuListener(
				new OnCreateContextMenuListener(){
					@Override
					public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
						LogUtil.d(TAG, "[IN ] onCreateContextMenu()");

						/* 削除 */
						menu.add(0, MENUITEM_ID_DELETE, 0, getString(R.string.white_list_delete));

						LogUtil.d(TAG, "[OUT] onCreateContextMenu()");
					}
				});

		white_list_ListView1.setOnItemClickListener(
				new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						final WhiteListInfo item = itemList.get(position);

						/* WhiteListDetailActivity起動 */
						LogUtil.d(TAG, "REQUEST_WHITE_LIST_DETAIL:" + Const.REQUEST_WHITE_LIST_DETAIL);
						Intent intent = new Intent(WhiteListActivity.this, WhiteListDetailActivity.class);
						intent.putExtra("itemId", String.valueOf(item.getId()));
						startActivityForResult(intent, Const.REQUEST_WHITE_LIST_DETAIL);
					}
				});

		/* 新規作成ボタン */
		white_list_Button1 = (Button)findViewById(R.id.white_list_Button1);
		white_list_Button1.setOnClickListener(this);

		/* メニューボタン */
		white_list_Button2 = (Button)findViewById(R.id.white_list_Button2);
		white_list_Button2.setOnClickListener(menuListener);

		LogUtil.d(TAG, "[OUT] initViews()");
	}

	/**
	 * ホワイトリスト読み込み処理
	 * <p>
	 * ホワイトリストを作成する。<br>
	 * ※非推奨メソッド「startManagingCursor()」「stopManagingCursor()」を使用。<br>
	 *   Android 2.X.Xをサポートしている為、回避手段がない。
	 * </p>
	 */
	@SuppressWarnings("deprecation")
	protected void loadNote(){
		LogUtil.d(TAG, "[IN ] loadNote()");

		itemList.clear();

		/* ホワイトリスト読み出し */
		dbAdapter.open();
		Cursor cursor = dbAdapter.getAllWhiteList();

		startManagingCursor(cursor);

		if (true == cursor.moveToFirst()) {
			do {
				WhiteListInfo item = new WhiteListInfo(
						cursor.getInt(cursor.getColumnIndex(WhiteListInfo.COLUMN_ID)),
						cursor.getString(cursor.getColumnIndex(WhiteListInfo.COLUMN_WHITE_LIST_TITLE)),
						cursor.getString(cursor.getColumnIndex(WhiteListInfo.COLUMN_WHITE_LIST_URL)),
						cursor.getInt(cursor.getColumnIndex(WhiteListInfo.COLUMN_WHITE_LIST_MODE))
				);

				itemList.add(item);
			} while(cursor.moveToNext());
		}

		stopManagingCursor(cursor);
		listAdapter.notifyDataSetChanged();
		dbAdapter.close();

		LogUtil.d(TAG, "[OUT] loadNote()");
	}

	/**
	 * コンテキストアイテム選択イベント処理
	 * <p>
	 * onContextItemSelected()のオーバーライド<br>
	 * コンテキストアイテムが選択されて場合にコールされる。
	 * </p>
	 * @param menuItem メニューアイテム
	 * @return 処理結果
	 */
	@Override
	public boolean onContextItemSelected(MenuItem menuItem) {
		LogUtil.d(TAG, "[IN ] onContextItemSelected()");

		boolean result;

		/* 選択されたアイテム情報を取得 */
		AdapterView.AdapterContextMenuInfo menuInfo  = (AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();
		WhiteListInfo item = itemList.get(menuInfo.position);
		final int item_id = item.getId();

		LogUtil.d(TAG, "item_id:" + item_id);
		switch(menuItem.getItemId()){
		/* 削除処理 */
		case MENUITEM_ID_DELETE:
			new AlertDialog.Builder(this)
				.setIcon(R.drawable.ic_launcher)
				.setTitle(getString(R.string.white_list_delete_ok))
				/* 実行ボタン押下時 */
				.setPositiveButton(getString(R.string.white_list_delete),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							LogUtil.d(TAG, "[IN ] setPositiveButton::onClick()");

							/* ホワイトリスト更新 */
							dbAdapter.open();

							if (true == dbAdapter.deleteWhiteList(item_id)) {
								Toast.makeText(getBaseContext(), getString(R.string.msg_deleted), Toast.LENGTH_SHORT).show();
								loadNote();
							}

							dbAdapter.close();

							LogUtil.d(TAG, "[OUT] setPositiveButton::onClick()");
						}
					})
				/* キャンセルボタン押下時 */
				.setNegativeButton(getString(R.string.white_list_cancel),
				null).show();

			LogUtil.d(TAG, "return true");
			LogUtil.d(TAG, "[OUT] onContextItemSelected()");
			return true;
		}

		result = super.onContextItemSelected(menuItem);

		LogUtil.d(TAG, "return true");
		LogUtil.d(TAG, "[OUT] onContextItemSelected()");
		return result;
	}

	/**
	 * クリックイベント処理
	 * <p>
	 * onClick()のオーバーライド<br>
	 * クリック時にコールされる。
	 * </p>
	 * @param view ビュー
	 */
	@Override
	public void onClick(View view) {
		LogUtil.d(TAG, "[IN ] onClick()");

		switch(view.getId()){
		case R.id.white_list_Button1:
			/* WhiteListDetailActivity起動 */
			LogUtil.d(TAG, "REQUEST_WHITE_LIST_DETAIL:" + Const.REQUEST_WHITE_LIST_DETAIL);
			Intent intent = new Intent(WhiteListActivity.this, WhiteListDetailActivity.class);
			intent.putExtra("itemId", String.valueOf(0));
			startActivityForResult(intent, Const.REQUEST_WHITE_LIST_DETAIL);
			break;
		default:
			break;
		}

		LogUtil.d(TAG, "[OUT] onClick()");
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.d(TAG, "[IN ] onActivityResult()");
		LogUtil.d(TAG, "requestCode:" + requestCode);
		LogUtil.d(TAG, "resultCode:" + resultCode);

		/* ホワイトリスト更新 */
		loadNote();

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
			Intent intent = new Intent(WhiteListActivity.this, MenuActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] menuListener::onClick()");
		}
	};
}
