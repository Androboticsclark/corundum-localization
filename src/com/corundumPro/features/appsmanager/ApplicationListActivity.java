package com.corundumPro.features.appsmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.corundumPro.R;
import com.corundumPro.common.activity.AbstractAuthActivity;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.dao.DBAdapter;
import com.corundumPro.common.dialog.OkCancelDialog;
import com.corundumPro.common.dto.JsonResultDto;
import com.corundumPro.common.exception.ArcAppException;
import com.corundumPro.common.exception.ArcBaseMsgException;
import com.corundumPro.common.util.ArchiveUtil;
import com.corundumPro.common.util.EnvUtil;
import com.corundumPro.common.util.FileUtil;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.features.home.HomeActivity;
import com.corundumPro.features.menu.MenuActivity;


/**
 * ApplicationListActivityクラス
 * <p>
 * 「アプリケーションリストアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
@SuppressLint("InflateParams")
public class ApplicationListActivity extends AbstractAuthActivity implements IUploadActivity {
	static final String TAG = "ApplicationListActivity";

	/*
	 * アプリケーションリスト
	 */
	private ApplicationListAdapter listAdapter;
	private List<ApplicationListInfo> itemList = new ArrayList<ApplicationListInfo>();

	/*
	 * 画面
	 */
	/** アプリケーションリスト */
	private ListView application_list_ListView1;

	/** ダウンロードボタン */
	private Button application_list_Button1;

	/** メニューボタン */
	private Button application_list_Button2;

	/** 実行ボタン */
	private Button application_list_Button3;

	/** アップロードボタン */
	private Button application_list_Button4;

	/** 削除ボタン */
	private Button application_list_Button5;

	/** HTTP Client */
	DefaultHttpClient httpClient;


	/**
	 * 作成処理
	 * <p>
	 * onCreate()のオーバーライド<br>
	 * アクティビティ生成時にコールされる。
	 * </p>
	 * @param bundle バンドル
	 */
	@Override
	public void onCreate(final Bundle bundle) {
		super.onCreate(bundle);
		LogUtil.d(TAG, "[IN ] onCreate()");

		// Viewにレイアウト設定
		setContentView(R.layout.application_list);

		// View初期化
		initViews();

		// ファイルリストロード
		loadNote(FileUtil.getBaseDirPath(this) + "/app");
		// アダプター設定
		listAdapter = new ApplicationListAdapter(this.getApplicationContext(), R.layout.application_list_row, itemList);
		application_list_ListView1.setAdapter(listAdapter);

		// HTTPクライアント生成
		httpClient = new DefaultHttpClient();

		LogUtil.d(TAG, "[OUT] onCreate()");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtil.d(TAG, "[IN ] onResume()");

		// アプリケーションリスト再描画
		showApplicationList();

		LogUtil.d(TAG, "[OUT] onResume()");
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtil.d(TAG, "[IN ] onPause()");
		LogUtil.d(TAG, "[OUT] onPause()");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtil.d(TAG, "[IN ] onStop()");
		LogUtil.d(TAG, "[OUT] onStop()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(TAG, "[IN ] onDestroy()");

		// HTTPクライアントシャットダウン
		if (this.httpClient != null) {
			this.httpClient.getConnectionManager().shutdown();
		}

		LogUtil.d(TAG, "[OUT] onDestroy()");
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
			/* 終了 */
			finish();
		}

		result = super.onKeyDown(keyCode, event);

		LogUtil.d(TAG, "result:" + result);
		LogUtil.d(TAG, "[OUT] onKeyDown()");
		return result;
	}

	@Override
	public DefaultHttpClient getHttpClient() {
		return this.httpClient;
	}

	@Override
	public void onAuthPreparation() {
		LogUtil.d(TAG, "[IN ] onAuthPreparation()");

		// プログレスダイアログを表示
		super.showSpinningDialog();

		LogUtil.d(TAG, "[OUT] onAuthPreparation()");
	}

	@Override
	public void onAuthSuccess(JsonResultDto json) {
		LogUtil.d(TAG, "[IN ] onAuthSuccess()");
		LogUtil.d(TAG, "json:" + json.toString());

		// プリファレンスにユーザ情報を保存
		authFunction.saveUserInfo(editor, userInfoDto);

		// アップロード処理を行う
		UploadFunction uploadFunction = new UploadFunction();
		uploadFunction.upload(getHttpClient(), this, listAdapter.getSelectedItem());

		LogUtil.d(TAG, "[OUT] onAuthSuccess()");
	}

	@Override
	public void onAuthFailure(JsonResultDto json) {
		LogUtil.d(TAG, "[IN ] onAuthFailure()");

		// プログレスダイアログを閉じる
		super.closeSpinningDialog();

		// 処理に失敗した場合、エラーダイアログを表示
		showErrorDialog(json.getMessage());

		LogUtil.d(TAG, "[OUT] onAuthFailure()");
	}

	@Override
	public void onAuthException(Throwable e) {
		LogUtil.d(TAG, "[IN ] onAuthException()");
		LogUtil.e(TAG, "Throwable:" + e.getMessage());

		// プログレスダイアログを閉じる
		super.closeSpinningDialog();

		// 処理中に例外が発生した場合、エラーダイアログを表示
		if (e instanceof ArcBaseMsgException) {
			ArcBaseMsgException ex = (ArcBaseMsgException)e;
			this.showErrorDialog(ex.getResourceId());
		}

		LogUtil.d(TAG, "[OUT] onAuthException()");
	}

	@Override
	public void onUploadPreparation() {
		LogUtil.d(TAG, "[IN ] onUploadPreparation()");
		// 何もしない
		LogUtil.d(TAG, "[OUT] onUploadPreparation()");
	}

	@Override
	public void onUploadSuccess(JsonResultDto json) {
		LogUtil.d(TAG, "[IN ] onUploadSuccess()");
		LogUtil.d(TAG, "json:" + json.toString());

		// プログレスダイアログを閉じる
		super.closeSpinningDialog();

		// zipファイルを削除する
		FileUtil.deleteQuietly(new File(listAdapter.getSelectedItem().getZipPath()));

		// 処理に成功した場合、メッセージダイアログを表示
		showMsgDialog(R.string.msg_upload_success);

		LogUtil.d(TAG, "[OUT] onUploadSuccess()");
	}

	@Override
	public void onUploadFailure(JsonResultDto json) {
		LogUtil.d(TAG, "[IN ] onUploadFailure()");
		LogUtil.d(TAG, "json:" + json.toString());

		// プログレスダイアログを閉じる
		super.closeSpinningDialog();

		// zipファイルを削除する
		FileUtil.deleteQuietly(new File(listAdapter.getSelectedItem().getZipPath()));

		// 処理に失敗した場合、エラーダイアログを表示
		showErrorDialog(R.string.err_upload_failure);

		LogUtil.d(TAG, "[OUT] onUploadFailure()");
	}

	@Override
	public void onUploadException(Throwable e) {
		LogUtil.d(TAG, "[IN ] onUploadException()");
		LogUtil.e(TAG, "Throwable:" + e.getMessage());

		// プログレスダイアログを閉じる
		super.closeSpinningDialog();

		// zipファイルを削除する
		FileUtil.deleteQuietly(new File(listAdapter.getSelectedItem().getZipPath()));

		// 処理中に例外が発生した場合、エラーダイアログを表示
		if (e instanceof ArcBaseMsgException) {
			ArcBaseMsgException ex = (ArcBaseMsgException)e;
			this.showErrorDialog(ex.getResourceId());
		}

		LogUtil.d(TAG, "[OUT] onUploadException()");
	}

	/**
	 * View初期化処理
	 * <p>
	 * Viewの初期化を行う。
	 * </p>
	 */
	private void initViews() {
		LogUtil.d(TAG, "[IN ] initViews()");

		/* ファイルリスト */
		application_list_ListView1 = (ListView)findViewById(R.id.application_list_ListView1);
		application_list_ListView1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);	// 単一選択モードにする
		application_list_ListView1.setOnItemClickListener(
				new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

						// 選択されたポジションを保持
						listAdapter.setSelectedPosition(position);

						LogUtil.d(TAG, "selectedApplicationListInfo:" + listAdapter.getSelectedItem().toString());

		                // アダプタの内容を即時反映する
						listAdapter.notifyDataSetChanged();
					}
				});

		/* ダウンロードボタン */
		application_list_Button1 = (Button)findViewById(R.id.application_list_Button1);
		application_list_Button1.setOnClickListener(downloadListener);

		/* メニューボタン */
		application_list_Button2 = (Button)findViewById(R.id.application_list_Button2);
		application_list_Button2.setOnClickListener(menuListener);

		/* アプリ実行ボタン */
		application_list_Button3 = (Button)findViewById(R.id.application_list_Button3);
		application_list_Button3.setOnClickListener(startAppListener);

		/* アップロードボタン */
		application_list_Button4 = (Button)findViewById(R.id.application_list_Button4);
		application_list_Button4.setOnClickListener(uploadListener);

		/* 削除ボタン */
		application_list_Button5 = (Button)findViewById(R.id.application_list_Button5);
		application_list_Button5.setOnClickListener(deleteAppListener);

		LogUtil.d(TAG, "[OUT] initViews()");
	}

	/**
	 * アプリケーションリストを表示します。
	 *
	 */
	private void showApplicationList() {
		// リストの存在有無によって、アプリ実行、アップロード、削除ボタンの活性／非活性を設定
		application_list_Button3.setEnabled(listAdapter.hasList());
		application_list_Button4.setEnabled(listAdapter.hasList());
		application_list_Button5.setEnabled(listAdapter.hasList());

		// 選択リスト設定
		application_list_ListView1.setItemChecked(listAdapter.getSelectedPosition(), listAdapter.hasList());
	}

	/**
	 * ファイルリスト読み込み処理
	 * <p>
	 * 指定ディレクトリ内のファイルリストを作成する。
	 * </p>
	 * @param path ディレクトリパス
	 */
	private void loadNote(String path){
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
			if (false == file.isDirectory()) {
				continue;
			}

			String applicationName;
			String description;
			String version;
			String publishedLevel;
			BufferedReader buffer = null;

			int BUFFER_SIZE = 8192;
			final Charset UTF8 = Charset.forName("UTF-8");

			try {
				/* アプリケーション名読み出し(１行のみ)(UTF-8で読み出す) */
				buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file.getPath() + Const.SEPARATOR_SLASH + EnumAppInfoFile.APP_NAME.getFileName()), UTF8), BUFFER_SIZE);
				applicationName = buffer.readLine();
				buffer.close();

				/* 詳細情報読み出し(１行のみ)(UTF-8で読み出す) */
				buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file.getPath() + Const.SEPARATOR_SLASH + EnumAppInfoFile.APP_DESCRIPTION.getFileName()), UTF8), BUFFER_SIZE);
				description = buffer.readLine();
				buffer.close();

				/* バージョン読み出し(１行のみ)(UTF-8で読み出す) */
				buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file.getPath() + Const.SEPARATOR_SLASH + EnumAppInfoFile.VERSION.getFileName()), UTF8), BUFFER_SIZE);
				version = buffer.readLine();
				buffer.close();

				/* 公開レベル読み出し(１行のみ)(UTF-8で読み出す) */
				buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file.getPath() + Const.SEPARATOR_SLASH + EnumAppInfoFile.PUBLISHED_LEVEL.getFileName()), UTF8), BUFFER_SIZE);
				publishedLevel = buffer.readLine();
				buffer.close();

			} catch (Exception e) {
				LogUtil.d(TAG, e.getMessage());
				applicationName = Const.SEPARATOR_HYPHEN;
				description = Const.SEPARATOR_HYPHEN;
				version = Const.SEPARATOR_HYPHEN;
				publishedLevel = Const.SEPARATOR_HYPHEN;

				if (null != buffer) {
					try {
						buffer.close();
					} catch (Exception e1) {
						LogUtil.d(TAG, e1.getMessage());
					}
				}
			}

			ApplicationListInfo item = new ApplicationListInfo(i
																,file.getPath() + "/icon.png"
																,file.getPath()
																,applicationName
																,description
																,file.getPath() + "/index.html"
																,version
																,publishedLevel);

			itemList.add(item);
		}

		LogUtil.d(TAG, "[OUT] loadNote()");
	}


	/**
	 * メニューボタンリスナ処理
	 * <p>
	 * メニューボタンが押下された時にコールされる。<br>
	 * </p>
	 */
	private View.OnClickListener menuListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] menuListener::onClick()");

			/* パスコード使用が有効 かつ 管理者用モード以外の場合、パスコード認証を行う */
			if (true == preference.getBoolean(SystemInfo.KEY_PASS_CODE_FLAG, false) && (DBAdapter.WEB_MODE_ADMIN != preference.getInt(SystemInfo.KEY_CURRENT_WEB_MODE, DBAdapter.WEB_MODE_ADMIN))) {
				/* 「パスコードを入力」ダイアログ作成 */
				AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(ApplicationListActivity.this);
				alertdialogBuilder.setTitle(R.string.passcode_input_passcode);

				/* パスコードエディットボックスの設定 */
				final EditText pass_code = new EditText(ApplicationListActivity.this);
				pass_code.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				pass_code.setHint(R.string.passcode_hint);
				pass_code.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(Const.CONFIG_PASSCODE_MAX_LENGTH) });
				alertdialogBuilder.setView(pass_code);

				/* 実行ボタン押下時 */
				alertdialogBuilder.setPositiveButton(R.string.passcode_ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							LogUtil.d(TAG, "[IN ] setPositiveButton::onClick()");

							/* パスコード取得 */
							String save_pass_code = preference.getString(SystemInfo.KEY_PASS_CODE, "");

							/* パスコードマッチング */
							if (pass_code.getText().toString().equals(save_pass_code)) {
								/* MenuActivity起動 */
								LogUtil.d(TAG, "MenuActivity");
								Intent intent = new Intent(getApplication(), MenuActivity.class);
								startActivity(intent);
								finish();
							} else {
								/* パスコード誤り */
								Toast.makeText(ApplicationListActivity.this, R.string.err_passcode, Toast.LENGTH_LONG).show();
							}

							LogUtil.d(TAG, "[OUT] setPositiveButton::onClick()");
						}
					});

				/* キャンセルボタン押下時 */
				alertdialogBuilder.setNegativeButton(R.string.passcode_cancel,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							LogUtil.d(TAG, "[IN ] setNegativeButton::onClick()");
							Toast.makeText(getApplicationContext(), R.string.msg_cancel, Toast.LENGTH_LONG).show();
							LogUtil.d(TAG, "[OUT] setNegativeButton::onClick()");
						}
					});

				/* ダイアログ表示 */
				alertdialogBuilder.setCancelable(true);
				AlertDialog alertdialog = alertdialogBuilder.create();
				alertdialog.show();
			} else {
				/* MenuActivity起動 */
				LogUtil.d(TAG, "MenuActivity");
				Intent intent = new Intent(ApplicationListActivity.this, MenuActivity.class);
				startActivity(intent);
				finish();
			}

			LogUtil.d(TAG, "[OUT] menuListener::onClick()");
		}
	};

	/**
	 * ダウンロードボタンリスナ処理
	 * <p>
	 * ダウンロードボタンが押下された時にコールされる。<br>
	 * </p>
	 */
	private View.OnClickListener downloadListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] downloadListener::onClick()");

			Intent intent = new Intent(ApplicationListActivity.this, ApplicationShopActivity.class);
			startActivity(intent);
			finish();

			LogUtil.d(TAG, "[OUT] downloadListener::onClick()");
		}
	};

	/**
	 * アプリ実行ボタンリスナ処理
	 * <p>
	 * アプリ実行ボタンが押下された時にコールされる。<br>
	 * </p>
	 */
	private View.OnClickListener startAppListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] startAppListener::onClick()");
			LogUtil.d(TAG, "startAppListener:" + listAdapter.getSelectedItem().toString());

			// アプリ実行
			start();

			LogUtil.d(TAG, "[OUT] startAppListener::onClick()");
		}
	};

	/**
	 * アップロードボタンリスナ処理
	 * <p>
	 * アップロードボタンが押下された時にコールされる。<br>
	 * </p>
	 */
	private View.OnClickListener uploadListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] uploadListener::onClick()");
			LogUtil.d(TAG, "uploadListener:" + listAdapter.getSelectedItem().toString());

			// 選択されたアプリ情報を取得
			ApplicationListInfo applicationListInfo = listAdapter.getSelectedItem();

			try {
				// ZIPファイル作成
				File tgtFile = new File(applicationListInfo.getRootPath());
				File zipFile = new File(applicationListInfo.getZipPath());

				// 既にZIPファイルが存在している場合、削除する
				FileUtil.deleteQuietly(zipFile);
				// 圧縮する
				ArchiveUtil.compress(tgtFile, zipFile);

				// ローカルからユーザ情報を取得
				authFunction.loadUserInfo(preference, userInfoDto);

				// 登録済みユーザ情報削除(Dev環境のみ設定可)
				if (EnvUtil.isDeleteUserInfo()) {
					authFunction.deleteUserInfo(editor, userInfoDto);
				}

				if (!authFunction.hastUserInfo(userInfoDto)) {
					// ユーザ情報が存在しない場合、入力ダイアログ経由で認証処理を行う。
					login(getHttpClient());

				} else {
					// ユーザ情報が存在する場合、直接Corundum Apps Manager認証を行う (非同期開始)
					authFunction.authenticate(getHttpClient(), ApplicationListActivity.this, userInfoDto);
				}

			} catch (ArcAppException e) {
				ApplicationListActivity.this.showErrorDialog(R.string.err_upload_failure);
			}

			LogUtil.d(TAG, "[OUT] uploadListener::onClick()");
		}
	};

	/**
	 * 削除ボタンリスナ処理
	 * <p>
	 * 削除ボタンが押下された時にコールされる。<br>
	 * </p>
	 */
	private View.OnClickListener deleteAppListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] deleteAppListener::onClick()");
			LogUtil.d(TAG, "deleteAppListener:" + listAdapter.getSelectedItem().toString());

			// 削除処理
			delete();

			LogUtil.d(TAG, "[OUT] deleteAppListener::onClick()");
		}
	};

	/**
	 * アプリ実行処理を行います。
	 */
	private void start() {
		LogUtil.d(TAG, "[IN ] start()");

		/* WEB表示モード設定 */
		editor.putInt(SystemInfo.KEY_CURRENT_HOME_MODE, DBAdapter.WEB_MODE_FULL);

		/* アプリケーションのURL設定 */
		editor.putString(SystemInfo.KEY_CURRENT_URL, "file://" + listAdapter.getSelectedItem().getIndexHtmlPath());

		editor.commit();

		/* DB保存 */
		saveDataBase();

		/* HomeActivity起動 */
		LogUtil.d(TAG, "HomeActivity");
		Intent intent = new Intent(ApplicationListActivity.this, HomeActivity.class);
		startActivity(intent);

		LogUtil.d(TAG, "[OUT] delete()");
	}

	/**
	 * 削除処理を行います<br>
	 */
	private void delete() {
		LogUtil.d(TAG, "[IN ] delete()");

		// 削除確認ダイアログ生成
		OkCancelDialog dialog = OkCancelDialog.newInstance(R.string.app_name
															,R.string.application_list_delete_ok
															,R.drawable.ic_launcher
															,R.string.application_list_delete
															,R.string.application_list_cancel);
		dialog.setOnOkClickListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				LogUtil.d(TAG, "[IN ] setOnOkClickListener::onClick()");
				LogUtil.d(TAG, "which:" + which);

				/* 削除 */
				File file = new File(listAdapter.getSelectedItem().getRootPath());
				FileUtil.deleteForce(file);
				itemList.remove(listAdapter.getSelectedItem());

				/* ファイルリスト更新 */
				loadNote(FileUtil.getBaseDirPath(ApplicationListActivity.this) + "/app");
				listAdapter.notifyDataSetChanged();
				listAdapter.decreaseSelectedPosition();

				// アプリケーションリスト再描画
				showApplicationList();

				LogUtil.d(TAG, "[OUT] setOnOkClickListener::onClick()");
			}
		});
		dialog.setOnCancelClickListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				LogUtil.d(TAG, "[IN ] setOnCancelClickListener::onClick()");
				// 終了
				dialog.dismiss();
				LogUtil.d(TAG, "[OUT] setOnCancelClickListener::onClick()");
			}
		});
		dialog.show(this.getSupportFragmentManager(), TAG);

		LogUtil.d(TAG, "[OUT] delete()");
	}
}
