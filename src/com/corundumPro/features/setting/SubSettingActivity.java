package com.corundumPro.features.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.constant.Const;
import com.corundumPro.common.constant.SystemInfo;
import com.corundumPro.common.util.LogUtil;
import com.corundumPro.features.menu.MenuActivity;
import com.corundumPro.features.screensaver.ScreenSaverActivity;

/**
 * SubSettingActivityクラス
 * <p>
 * 「サブセッティングアクティビティ」クラス
 * </p>
 * @author androbotics.ltd
 */
public class SubSettingActivity extends BaseActivity {
	static final String TAG ="SubSettingActivity";

	/*
	 * 画面
	 */
	/** スクリーンセーバーを使用 */
	private CheckBox sub_setting_checkBox1;

	/** 秒数エディットボックス */
	private EditText sub_setting_editText1;

	/** URLエディットボックス */
	private EditText sub_setting_editText2;

	/** プレビューボタン */
	private Button sub_setting_button1;

	/** パスコードを使用する */
	private CheckBox sub_setting_checkBox2;

	/** コード入力 */
	private Button sub_setting_button2;

	/** 終了時に通知する */
	private CheckBox sub_setting_checkBox3;

	/** パスコード */
	private String pass_code;

	/** メニューボタン */
	private Button sub_setting_button3;

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
		setContentView(R.layout.sub_setting);

		/* View初期化 */
		initViews();

		/* 画面のフォーカスを外す */
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		/* スクリーンセーバー使用フラグ */
		if (true == preference.getBoolean(SystemInfo.KEY_SCREEN_SAVER_FLAG, false)) {
			sub_setting_checkBox1.setChecked(true);
		} else {
			sub_setting_checkBox1.setChecked(false);
		}

		/* スクリーンセーバー起動時間(sec) */
		sub_setting_editText1.setText(String.valueOf(preference.getInt(SystemInfo.KEY_SCREEN_SAVER_TIME, 0)));

		/* スクリーンセーバーURL */
		sub_setting_editText2.setText(preference.getString(SystemInfo.KEY_SCREEN_SAVER_URL, ""));

		/* パスコード使用フラグ */
		if (true == preference.getBoolean(SystemInfo.KEY_PASS_CODE_FLAG, false)) {
			sub_setting_checkBox2.setChecked(true);
		} else {
			sub_setting_checkBox2.setChecked(false);
		}

		/* パスコード */
		pass_code = preference.getString(SystemInfo.KEY_PASS_CODE, "");

		/* ホームボタン終了抑止フラグ */
		if (true == preference.getBoolean(SystemInfo.KEY_EXIT_FLAG, false)) {
			sub_setting_checkBox3.setChecked(true);
		} else {
			sub_setting_checkBox3.setChecked(false);
		}

		LogUtil.d(TAG, "[OUT] onCreate()");
	}

	/**
	 * DB保存処理
	 * <p>
	 * 「システム情報」「ショートカット情報」「一時情報」のDBを保存する。
	 * </p>
	 */
	@Override
	public void saveDataBase() {
		LogUtil.d(TAG, "[IN ] saveDataBase()");

		/* スクリーンセーバー使用フラグ */
		if (true == sub_setting_checkBox1.isChecked()) {
			editor.putBoolean(SystemInfo.KEY_SCREEN_SAVER_FLAG, true);
		} else {
			editor.putBoolean(SystemInfo.KEY_SCREEN_SAVER_FLAG, false);
		}

		/* スクリーンセーバー起動時間(sec) */
		editor.putInt(SystemInfo.KEY_SCREEN_SAVER_TIME, Integer.parseInt(sub_setting_editText1.getText().toString()));

		/* スクリーンセーバーURL */
		editor.putString(SystemInfo.KEY_SCREEN_SAVER_URL, sub_setting_editText2.getText().toString());

		/* パスコード使用フラグ */
		if (true == sub_setting_checkBox2.isChecked()) {
			editor.putBoolean(SystemInfo.KEY_PASS_CODE_FLAG, true);
		} else {
			editor.putBoolean(SystemInfo.KEY_PASS_CODE_FLAG, false);
		}

		/* 新しいパスコード */
		editor.putString(SystemInfo.KEY_PASS_CODE, pass_code);

		/* ホームボタン終了抑止フラグ */
		if (true == sub_setting_checkBox3.isChecked()) {
			editor.putBoolean(SystemInfo.KEY_EXIT_FLAG, true);
		} else {
			editor.putBoolean(SystemInfo.KEY_EXIT_FLAG, false);
		}

		editor.commit();

		/* DB保存 */
		super.saveDataBase();

		LogUtil.d(TAG, "[OUT] saveDataBase()");
	}

	/**
	 * View初期化処理
	 * <p>
	 * Viewの初期化を行う。
	 * </p>
	 */
	public void initViews() {
		LogUtil.d(TAG, "[IN ] initViews()");

		/* スクリーンセーバーを使用 */
		sub_setting_checkBox1 = (CheckBox)findViewById(R.id.sub_setting_checkBox1);

		/* 秒数エディットボックス */
		sub_setting_editText1 = (EditText)findViewById(R.id.sub_setting_editText1);
		sub_setting_editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				LogUtil.d(TAG, "[IN ] onFocusChange()");
				LogUtil.d(TAG, "hasFocus:" + hasFocus);

				if (true == hasFocus) {
					/* フォーカスを受け取った時 */
				} else {
					/* フォーカスが離れた時 */
					saveDataBase();
				}

				LogUtil.d(TAG, "[OUT] onFocusChange()");
			}
		});

		/* URLエディットボックス */
		sub_setting_editText2 = (EditText) findViewById(R.id.sub_setting_editText2);
		sub_setting_editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				LogUtil.d(TAG, "[IN ] onFocusChange()");
				LogUtil.d(TAG, "hasFocus:" + hasFocus);

				if (true == hasFocus) {
					/* フォーカスを受け取った時 */
				} else {
					/* フォーカスが離れた時 */
					saveDataBase();
				}

				LogUtil.d(TAG, "[OUT] onFocusChange()");
			}
		});

		/* プレビューボタン */
		sub_setting_button1 = (Button) findViewById(R.id.sub_setting_button1);
		sub_setting_button1.setOnClickListener(previewListener);

		/* パスコードを使用する */
		sub_setting_checkBox2 = (CheckBox) findViewById(R.id.sub_setting_checkBox2);

		/* コード入力 */
		sub_setting_button2 = (Button) findViewById(R.id.sub_setting_button2);
		sub_setting_button2.setOnClickListener(codeListener);

		/* 終了時に通知する */
		sub_setting_checkBox3 = (CheckBox) findViewById(R.id.sub_setting_checkBox3);

		/* メニューボタン */
		sub_setting_button3 = (Button)findViewById(R.id.sub_setting_button3);
		sub_setting_button3.setOnClickListener(menuListener);

		LogUtil.d(TAG, "[OUT] initViews()");
	}

	/**
	 * プレビューボタンリスナ処理
	 * <p>
	 * プレビューボタンが押下された時にコールされる。<br>
	 * プレビュー表示する。
	 * </p>
	 */
	private View.OnClickListener previewListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] previewListener::onClick()");

			/* ScreenSaverActivity起動 */
			LogUtil.d(TAG, "REQUEST_SCREENSAVER:" + Const.REQUEST_SCREENSAVER);
			Intent intent = new Intent(SubSettingActivity.this, ScreenSaverActivity.class);
			startActivityForResult(intent, Const.REQUEST_SCREENSAVER);

			LogUtil.d(TAG, "[OUT] previewListener::onClick()");
		}
	};

	/**
	 * コード入力ボタンリスナ処理
	 * <p>
	 * コード入力ボタンが押下された時にコールされる。<br>
	 * コード入力ダイアログを表示する。
	 * </p>
	 */
	private View.OnClickListener codeListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			LogUtil.d(TAG, "[IN ] codeListener::onClick()");

			/* 「現在のパスコードを入力」ダイアログ作成 */
			AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(SubSettingActivity.this);
			alertDialogBuilder1.setTitle(R.string.passcode_input_now_passcode);

			/* パスコードエディットボックスの設定 */
			final EditText now_pass_code = new EditText(SubSettingActivity.this);
			now_pass_code.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			now_pass_code.setHint(R.string.passcode_hint);
			now_pass_code.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(Const.CONFIG_PASSCODE_MAX_LENGTH) });
			alertDialogBuilder1.setView(now_pass_code);

			/* 実行ボタン押下時 */
			alertDialogBuilder1.setPositiveButton(R.string.passcode_ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int which) {
						LogUtil.d(TAG, "[IN ] setPositiveButton::onClick()");
						LogUtil.d(TAG, "which:" + which);
						LogUtil.d(TAG, "now_pass_code:" + now_pass_code.getText().toString());

						/* 設定済パスコードチェック */
						if (true == pass_code.equals(now_pass_code.getText().toString())) {

							/* 「新しいパスコードを入力」ダイアログ作成 */
							AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(SubSettingActivity.this);
							alertDialogBuilder2.setTitle(R.string.passcode_input_new_passcode);

							/* パスコードエディットボックスの設定 */
							final EditText new_pass_code1 = new EditText(SubSettingActivity.this);
							new_pass_code1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
							new_pass_code1.setHint(R.string.passcode_hint);
							new_pass_code1.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(Const.CONFIG_PASSCODE_MAX_LENGTH) });
							alertDialogBuilder2.setView(new_pass_code1);

							/* 実行ボタン押下時 */
							alertDialogBuilder2.setPositiveButton(R.string.passcode_ok,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialogInterface, int which) {
										LogUtil.d(TAG, "[IN ] setPositiveButton::onClick()");
										LogUtil.d(TAG, "which:" + which);
										LogUtil.d(TAG, "new_pass_code1:" + new_pass_code1.getText().toString());

										/* 「もう一度パスコードを入力」ダイアログ作成 */
										AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(SubSettingActivity.this);
										alertDialogBuilder3.setTitle(R.string.passcode_input_new_passcode_again);

										/* パスコードエディットボックスの設定 */
										final EditText new_pass_code2 = new EditText(SubSettingActivity.this);
										new_pass_code2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
										new_pass_code2.setHint(R.string.passcode_hint);
										new_pass_code2.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(Const.CONFIG_PASSCODE_MAX_LENGTH) });
										alertDialogBuilder3.setView(new_pass_code2);

										/* 実行ボタン押下時 */
										alertDialogBuilder3.setPositiveButton(R.string.passcode_ok,
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialogInterface, int which) {
													LogUtil.d(TAG, "[IN ] setPositiveButton::onClick()");
													LogUtil.d(TAG, "which:" + which);
													LogUtil.d("SubSettingActivity", "new_pass_code2:" + new_pass_code2.getText().toString());

													/* 新しいパスコードチェック */
													if (true == new_pass_code2.getText().toString().equals(new_pass_code1.getText().toString())) {
														pass_code = new_pass_code1.getText().toString();
														Toast.makeText(getApplicationContext(), R.string.msg_save_pass_code, Toast.LENGTH_LONG).show();
													} else {
														/* 新しいパスコード誤り */
														Toast.makeText(SubSettingActivity.this, R.string.err_passcode, Toast.LENGTH_LONG).show();
													}

													LogUtil.d(TAG, "[OUT] setPositiveButton::onClick()");
												}
											});

										/* キャンセルボタン押下時 */
										alertDialogBuilder3.setNegativeButton(R.string.passcode_cancel,
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialogInterface, int which) {
													LogUtil.d(TAG, "[IN ] setNegativeButton::onClick()");
													LogUtil.d(TAG, "which:" + which);
													LogUtil.d(TAG, "new_pass_code2:" + new_pass_code2.getText().toString());
													LogUtil.d(TAG, "[OUT] setNegativeButton::onClick()");
												}
											});

										/* ダイアログ表示 */
										alertDialogBuilder3.setCancelable(true);
										AlertDialog alertDialog3 = alertDialogBuilder3.create();
										alertDialog3.show();

										LogUtil.d(TAG, "[OUT] setPositiveButton::onClick()");
									}
							});

							/* キャンセルボタン押下時 */
							alertDialogBuilder2.setNegativeButton(R.string.passcode_cancel,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialogInterface, int which) {
										LogUtil.d(TAG, "[IN ] setNegativeButton::onClick()");
										LogUtil.d(TAG, "which:" + which);
										LogUtil.d(TAG, "new_pass_code1:" + new_pass_code1.getText().toString());
										LogUtil.d(TAG, "[OUT] setNegativeButton::onClick()");
									}
								});

							/* ダイアログ表示 */
							alertDialogBuilder2.setCancelable(true);
							AlertDialog alertDialog2 = alertDialogBuilder2.create();
							alertDialog2.show();

						} else {
							/* パスコード間違い */
							Toast.makeText(SubSettingActivity.this, R.string.err_passcode, Toast.LENGTH_LONG).show();
						}

						LogUtil.d(TAG, "[OUT] setPositiveButton::onClick()");
					}
				});

			/* キャンセルボタン押下時 */
			alertDialogBuilder1.setNegativeButton(R.string.passcode_cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int which) {
						LogUtil.d(TAG, "[IN ] setNegativeButton::onClick()");
						LogUtil.d(TAG, "which:" + which);
						LogUtil.d(TAG, "now_pass_code:" + now_pass_code.getText().toString());
						LogUtil.d(TAG, "[OUT] setNegativeButton::onClick()");
					}
				});

			/* ダイアログ表示 */
			alertDialogBuilder1.setCancelable(true);
			AlertDialog alertDialog1 = alertDialogBuilder1.create();
			alertDialog1.show();

			LogUtil.d(TAG, "[OUT] codeListener::onClick()");
		}
	};

	/**
	 * 表示前処理
	 * <p>
	 * onPause()のオーバーライド<br>
	 * アクティビティ作成前にコールされる。
	 * </p>
	 */
	@Override
	public void onPause() {
		super.onPause();
		LogUtil.d(TAG, "[IN ] onPause()");

		/* DB保存 */
		saveDataBase();

		LogUtil.d(TAG, "[OUT] onPause()");
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

			/* DB保存 */
			saveDataBase();

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
			Intent intent = new Intent(SubSettingActivity.this, MenuActivity.class);
			startActivity(intent);

			/* 終了 */
			finish();

			LogUtil.d(TAG, "[OUT] menuListener::onClick()");
		}
	};
}
