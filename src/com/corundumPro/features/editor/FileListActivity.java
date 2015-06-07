/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.features.editor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.dto.FileListInfo;
import com.corundumPro.common.util.CheckUtil;
import com.corundumPro.common.util.FileUtil;
import com.corundumPro.common.util.ImageUtil;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.common.util.ResourceUtil;
import com.corundumPro.common.util.StringUtil;
import com.corundumPro.features.menu.MenuActivity;

/**
 * FileListActivityクラス
 * <p>
 * 「ファイルリストアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class FileListActivity extends BaseActivity implements OnClickListener {
	static final String TAG = "FileListActivity";

	/*
	 * コンテキストメニュー
	 */
	/** コンテキストメニュー：リネーム */
	static final int MENUITEM_ID_RENAME	= 1;

	/** コンテキストメニュー：削除 */
	static final int MENUITEM_ID_DELETE	= 2;

	/*
	 * ファイルリスト
	 */
	private FileListAdapter listAdapter;
	private List<FileListInfo> itemList = new ArrayList<FileListInfo>();

	/*
	 * 画面
	 */
	/** ファイルリスト */
	private ListView file_list_ListView1;

	/** 新規登録ボタン */
	private Button file_list_Button1;

	/** ディレクトリパス */
	private String dirPath;

	/** 作成時ファイル種別 */
	private int createType;

	/** 新規作成画像ファイル名 */
	private String newImageFile;

	/** メニューボタン */
	private Button file_list_Button2;

	private BaseActivity activity;

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

		activity = this;

		/* ディレクトリパス初期設定 */
		dirPath = FileUtil.getBaseDirPath(this);

		/* Viewにレイアウト設定 */
		setContentView(R.layout.file_list);

		/* View初期化 */
		initViews();

		/* ファイルリストロード */
		listAdapter = new FileListAdapter(this, itemList);
		file_list_ListView1.setAdapter(listAdapter);
		loadNote(dirPath);

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

		/* ファイルリスト */
		file_list_ListView1 = (ListView)findViewById(R.id.file_list_ListView1);
		file_list_ListView1.setOnCreateContextMenuListener(
			new OnCreateContextMenuListener(){
				@Override
				public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo) {
					LogUtil.d(TAG, "[IN ] onCreateContextMenu()");

					/* リネーム */
					contextMenu.add(0, MENUITEM_ID_RENAME, 0, getString(R.string.file_list_rename));

					/* 削除 */
					contextMenu.add(0, MENUITEM_ID_DELETE, 0, getString(R.string.file_list_delete));

					LogUtil.d(TAG, "[OUT] onCreateContextMenu()");
				}
			});

		file_list_ListView1.setOnItemClickListener(
				new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						final FileListInfo item = itemList.get(position);

						if (FileListInfo.FILE_TYPE_EDIT_ABLE == item.getFileType()) {
							// テキストファイルの場合、FileListDetailActivity起動
							LogUtil.d(TAG, "REQUEST_file_list_DETAIL:" + Const.REQUEST_HOME_MODIFY_DETAIL);
							Intent intent = new Intent(FileListActivity.this, FileListDetailActivity.class);
							intent.putExtra("filePath", item.getFile().getPath());
							startActivityForResult(intent, Const.REQUEST_HOME_MODIFY_DETAIL);

						} else if (FileListInfo.FILE_TYPE_IMAGE == item.getFileType()) {
							// 画像ファイルの場合、ImagePreviewActivity起動
							LogUtil.d(TAG, "REQUEST_image_PREVIEW:" + Const.REQUEST_IMAGE_PREVIEW);
							Intent intent = new Intent(FileListActivity.this, ImagePreviewActivity.class);
							intent.putExtra(SystemInfo.KEY_IMAGE_PREVIEW_URL, item.getFile().getPath());
							startActivityForResult(intent, Const.REQUEST_IMAGE_PREVIEW);

						} else if (FileListInfo.FILE_TYPE_DIR == item.getFileType()) {
							/* ディレクトリパスの階層を移動 */
							dirPath += "/" + item.getFile().getName();

							/* ファイルリスト更新 */
							listAdapter.notifyDataSetChanged();
							loadNote(dirPath);
						} else {
							/* 何もしない */
						}

					}
				});

		/* 新規作成ボタン */
		file_list_Button1 = (Button)findViewById(R.id.file_list_Button1);
		file_list_Button1.setOnClickListener(createListener);

		/* メニューボタン */
		file_list_Button2 = (Button)findViewById(R.id.file_list_Button2);
		file_list_Button2.setOnClickListener(menuListener);

		LogUtil.d(TAG, "[OUT] initViews()");
	}

	/**
	 * 新規作成ボタンリスナ処理
	 * <p>
	 * 新規作成ボタンが押下された時にコールされる。<br>
	 * 新規作成ダイアログを表示する。
	 * </p>
	 */
	private View.OnClickListener createListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] createListener::onClick()");

			/* ラジオボタン */
			final String item_list[] = new String[] {
					/* ファイル */
					getResources().getString(R.string.create_file),

					/* 画像 */
					getResources().getString(R.string.create_image),

					/* ディレクトリ */
					getResources().getString(R.string.create_folder)};

			/* ラジオボタンは先頭がデフォルト */
			createType = 0;

			/* 新規作成ダイアログ */
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FileListActivity.this);
			alertDialogBuilder.setTitle(R.string.create_input);

			/* ファイル名エディットボックス */
			final EditText newFileName = new EditText(FileListActivity.this);
			newFileName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
			newFileName.setHint(R.string.create_hint);
			newFileName.setFilters(new InputFilter[]{ new FileNameFilter(activity), new InputFilter.LengthFilter(Const.CONFIG_FILE_NAME_MAX_LENGTH) });
			alertDialogBuilder.setView(newFileName);

			/* ラジオボタン変更時 */
			alertDialogBuilder.setSingleChoiceItems(item_list, 0, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface diaUtilityControllerInterface, int whichButton) {
					LogUtil.d(TAG, "[IN ] setSingleChoiceItems::onClick()");
					LogUtil.d(TAG, "whichButton:" + whichButton);

					/* ラジオボタンの選択状態更新 */
					createType = whichButton;

					LogUtil.d(TAG, "[OUT] setSingleChoiceItems::onClick()");
				}
			});

			/* 実行ボタン押下時 */
			alertDialogBuilder.setPositiveButton(R.string.create_ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface diaUtilityControllerInterface, int which) {
						LogUtil.d(TAG, "[IN ] setPositiveButton::onClick()");
						LogUtil.d(TAG, "which:" + which);

						/* ファイル名重複チェック */
						File dir = new File(dirPath);
						File[] files = dir.listFiles();

						for (int i = 0; i < files.length; i++) {
							File file = files[i];

							/* ファイル名重複 */
							if (true == file.getName().equals(newFileName.getText().toString())) {
								Toast.makeText(getApplicationContext(), R.string.err_duplication_file_name, Toast.LENGTH_LONG).show();
								LogUtil.d(TAG, "[OUT] setPositiveButton::onClick()");
								return;
							}
						}

						LogUtil.d(TAG, "createType:" + createType);
						switch (createType) {
						/* ファイル */
						case 0:
							File newFile = new File(dirPath + "/" + newFileName.getText().toString());
							try {
								try {
									/* ファイル作成 */
									boolean ret = newFile.createNewFile();
									LogUtil.d(TAG, "ret:" + ret);
								} catch (Exception e) {
									LogUtil.d(TAG, e.getMessage());
								}

								/* HomeModifyDetailActivity起動 */
								LogUtil.d(TAG, "REQUEST_file_list_DETAIL:" + Const.REQUEST_HOME_MODIFY_DETAIL);
								Intent intent = new Intent(FileListActivity.this, FileListDetailActivity.class);
								intent.putExtra("filePath", dirPath + "/" + newFileName.getText().toString());
								startActivityForResult(intent, Const.REQUEST_HOME_MODIFY_DETAIL);
							} catch (Exception e) {
								LogUtil.d(TAG, e.getMessage());
							}
							break;

						/* 画像 */
						case 1:
							/* 新規作成画像ファイル名保持 */
							newImageFile = newFileName.getText().toString();

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
							break;

						/* ディレクトリ */
						case 2:
						default:
							/* ディレクトリ作成 */
							File newDir = new File(dirPath + "/" + newFileName.getText().toString());

							try {
								boolean ret = newDir.mkdir();
								LogUtil.d(TAG, "ret:" + ret);
							} catch (Exception e) {
								LogUtil.d(TAG, e.getMessage());
							}
							break;
						}

						/* ファイルリスト更新 */
						listAdapter.notifyDataSetChanged();
						loadNote(dirPath);

						LogUtil.d(TAG, "[OUT] setPositiveButton::onClick()");
					}
				});

			/* キャンセルボタン押下時 */
			alertDialogBuilder.setNegativeButton(R.string.create_cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface diaUtilityControllerInterface, int which) {
						LogUtil.d(TAG, "[IN ] setNegativeButton::onClick()");
						LogUtil.d(TAG, "which:" + which);
						Toast.makeText(getApplicationContext(), R.string.msg_cancel, Toast.LENGTH_LONG).show();
						LogUtil.d(TAG, "[OUT] setNegativeButton::onClick()");
					}
				});

			/* ダイアログ表示 */
			alertDialogBuilder.setCancelable(true);
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

			LogUtil.d(TAG, "[OUT] createListener::onClick()");
		}
	};

	/**
	 * ファイルリスト読み込み処理
	 * <p>
	 * 指定ディレクトリ内のファイルリストを作成する。
	 * </p>
	 * @param path ディレクトリパス
	 */
	protected void loadNote(String path){
		LogUtil.d(TAG, "[IN ] loadNote()");
		LogUtil.d(TAG, "path:" + path);

		/* ファイルリストクリア */
		itemList.clear();

		/* ファイルリスト取得 */
		File dir = new File(path);
		File[] files = dir.listFiles();
		int i = 0;

		for (i = 0; i < files.length; i++) {
			File file = files[i];
			Date date = new Date(file.lastModified());
			String size = "";

			/* ファイル種別取得 */
			int fileType = FileUtil.convertFileType(file);

			/* アイコンリソースID取得 */
			int resourceId = ResourceUtil.convertResourceImage(fileType);

			/* ファイル種別が「ディレクトリ」の場合、ファイルサイズは「- byte」表示 */
			if (FileListInfo.FILE_TYPE_DIR != fileType) {
				size = FileUtil.convertSize(file.length());
			} else {
				size = "- byte";
			}

			/* 新規に作成したファイルをリスト追加 */
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
			String timestamp = sdf.format(date);
			FileListInfo item = new FileListInfo(i, resourceId, file.getName(), size, timestamp, fileType, file);
			itemList.add(item);
		}

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
		final FileListInfo item = itemList.get(menuInfo.position);
		String fileName = "";

		/* ファイル名取得 */
		if (null != item.getFile()) {
			fileName = "file://" + item.getFile().getPath();
			LogUtil.d(TAG, "fileName:" + fileName);
		}

		LogUtil.d(TAG, "ItemId:" + menuItem.getItemId());

		switch(menuItem.getItemId()){
		/* リネーム */
		case MENUITEM_ID_RENAME:
			/* ホームHTMLはリネーム禁止 */
			if((true == fileName.equals(preference.getString(SystemInfo.KEY_HOME_HTML_PATH, "")) || (true == item.getFileName().equals(getResources().getString(R.string.file_list_move_up))))) {
				Toast.makeText(getApplicationContext(), R.string.err_cannot_rename_file, Toast.LENGTH_LONG).show();
				LogUtil.d(TAG, "return true");
				LogUtil.d(TAG, "[OUT] onContextItemSelected()");
				return true;
			}

			/* リネームダイアログ */
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle(R.string.rename_input);

			/* ファイル名エディットボックス */
			final EditText newFileName = new EditText(this);
			newFileName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
			newFileName.setHint(R.string.reneme_hint);
			newFileName.setFilters(new InputFilter[]{ new FileNameFilter(this), new InputFilter.LengthFilter(Const.CONFIG_FILE_NAME_MAX_LENGTH) });
			alertDialogBuilder.setView(newFileName);

			/* 実行ボタン押下時 */
			alertDialogBuilder.setPositiveButton(R.string.rename_ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface diaUtilityControllerInterface, int which) {
						LogUtil.d(TAG, "[IN ] setPositiveButton::onClick()");
						LogUtil.d(TAG, "which:" + which);

						/* ファイル名重複チェック */
						File dir = new File(dirPath);
						File[] files = dir.listFiles();

						for (int i = 0; i < files.length; i++) {
							File file = files[i];

							/* ファイル名重複 */
							if (true == file.getName().equals(newFileName.getText().toString())) {
								Toast.makeText(getApplicationContext(), R.string.err_duplication_file_name, Toast.LENGTH_LONG).show();
								LogUtil.d(TAG, "[OUT] setPositiveButton::onClick()");
								return;
							}
						}

						/* リネーム */
						itemList.remove(item);
						File newFile = new File(dirPath + "/" + newFileName.getText());

						try {
							boolean ret = item.getFile().renameTo(newFile);
							LogUtil.d(TAG, "ret:" + ret);
						} catch (Exception e) {
							LogUtil.d(TAG, e.getMessage());
						}

						/* ファイル情報更新 */
						item.setFile(newFile);
						item.setFileName(newFile.getName());
						itemList.add(item);

						/* ファイルリスト更新 */
						listAdapter.notifyDataSetChanged();
						loadNote(dirPath);

						LogUtil.d(TAG, "[OUT] setPositiveButton::onClick()");
					}
				});

			/* キャンセルボタン押下時 */
			alertDialogBuilder.setNegativeButton(R.string.rename_cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface diaUtilityControllerInterface, int which) {
						LogUtil.d(TAG, "[IN ] setNegativeButton::onClick()");
						LogUtil.d(TAG, "which:" + which);
						Toast.makeText(getApplicationContext(), R.string.msg_cancel, Toast.LENGTH_LONG).show();
						LogUtil.d(TAG, "[OUT] setNegativeButton::onClick()");
					}
				});

			/* ダイアログ表示 */
			alertDialogBuilder.setCancelable(true);
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			break;

		/* 削除処理 */
		case MENUITEM_ID_DELETE:
			/* ホームHTMLは削除禁止 */
			if ((true == fileName.equals(preference.getString(SystemInfo.KEY_HOME_HTML_PATH, ""))) || (true == item.getFileName().equals(getResources().getString(R.string.file_list_move_up)))) {
				Toast.makeText(getApplicationContext(), R.string.err_cannot_delete_file, Toast.LENGTH_LONG).show();
				LogUtil.d(TAG, "return true");
				LogUtil.d(TAG, "[OUT] onContextItemSelected()");
				return true;
			}

			/* 削除ダイアログ */
			new AlertDialog.Builder(this)
				.setIcon(R.drawable.ic_launcher)
				.setTitle(getString(R.string.file_list_delete_ok))
				.setPositiveButton(getString(R.string.file_list_delete),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int which) {
							LogUtil.d(TAG, "[IN ] setPositiveButton::onClick()");
							LogUtil.d(TAG, "which:" + which);

							/* 削除 */
							FileUtil.deleteForce(item.getFile());
							itemList.remove(item);

							/* ファイルリスト更新 */
							listAdapter.notifyDataSetChanged();
							loadNote(dirPath);

							LogUtil.d(TAG, "[OUT] setPositiveButton::onClick()");
						}
					})
				.setNegativeButton(getString(R.string.file_list_cancel),
				null).show();

			LogUtil.d(TAG, "return true");
			LogUtil.d(TAG, "[OUT] onContextItemSelected()");
			return true;
		}

		result = super.onContextItemSelected(menuItem);

		LogUtil.d(TAG, "result:" + result);
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
	 * @param intent インテント
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		LogUtil.d(TAG, "[IN ] onActivityResult()");
		LogUtil.d(TAG, "requestCode:" + requestCode);
		LogUtil.d(TAG, "resultCode:" + resultCode);

		/* ギャラリーからの処理結果の場合 */
		if ((Const.REQUEST_GALLERY == requestCode) && (RESULT_OK == resultCode)) {
			try {
				/* Uri -> File Path変換 */
				String filePath = StringUtil.convertUriToFilePath(getApplicationContext(), intent.getData());

				/* Bitmap取得 */
				Bitmap img = ImageUtil.readBitmap(filePath);

				/* Bitmapコピー */
				File imageFile = new File(dirPath);
				ImageUtil.copyBitmap(img, imageFile, newImageFile);
			} catch (Exception e) {
				LogUtil.d(TAG, e.getMessage());
			}
		}

		/* ファイルリスト更新 */
		listAdapter.notifyDataSetChanged();
		loadNote(dirPath);

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

			if (dirPath.equals(FileUtil.getBaseDirPath(this))) {
				/* MenuActivity起動 */
				LogUtil.d(TAG, "MenuActivity");
				Intent intent = new Intent(this, MenuActivity.class);
				startActivity(intent);

				/* 終了 */
				finish();
			} else {
				/* ディレクトリパスを一階層上に変更 */
				String tmp = dirPath;
				dirPath = dirPath.substring(0, tmp.lastIndexOf('/'));

				/* ファイルリスト更新 */
				listAdapter.notifyDataSetChanged();
				loadNote(dirPath);
			}
		}

		result = false;

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

			if (dirPath.equals(FileUtil.getBaseDirPath(FileListActivity.this))) {
				/* MenuActivity起動 */
				LogUtil.d(TAG, "MenuActivity");
				Intent intent = new Intent(FileListActivity.this, MenuActivity.class);
				startActivity(intent);

				/* 終了 */
				finish();
			} else {
				/* ディレクトリパスを一階層上に変更 */
				String tmp = dirPath;
				dirPath = dirPath.substring(0, tmp.lastIndexOf('/'));

				/* ファイルリスト更新 */
				listAdapter.notifyDataSetChanged();
				loadNote(dirPath);
			}

			LogUtil.d(TAG, "[OUT] menuListener::onClick()");
		}
	};


}
