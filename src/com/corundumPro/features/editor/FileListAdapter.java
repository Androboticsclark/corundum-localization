package com.corundumPro.features.editor;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.dto.FileListInfo;
import com.corundumPro.common.util.LogUtil;

/**
 * FileListAdapterクラス
 * <p>
 * 「ファイルリストアダプター」クラス
 * </p>
 * @author androbotics.ltd
 */
public class FileListAdapter extends BaseAdapter {
	static final String TAG = "FileListAdapter";

	private BaseActivity activity;
	private List<FileListInfo> itemList;

	public FileListAdapter(BaseActivity activity, List<FileListInfo> itemList) {
		LogUtil.d(TAG, "[IN ] FileListAdapter()");

		this.activity = activity;
		this.itemList = itemList;

		LogUtil.d(TAG, "[OUT] FileListAdapter()");
	}

	/**
	 * カウント数取得処理
	 * <p>
	 * getCount()のオーバーライド<br>
	 * ファイルリストのカウント数を取得する
	 * </p>
	 * @return カウント数
	 */
	@Override
	public int getCount() {
		LogUtil.d(TAG, "[IN ] getCount()");
		LogUtil.d(TAG, "size:" + itemList.size());
		LogUtil.d(TAG, "[OUT] getCount()");
		return itemList.size();
	}

	/**
	 * アイテム取得処理
	 * <p>
	 * getItem()のオーバーライド<br>
	 * ファイルリストのアイテムオブジェクトを取得する
	 * </p>
	 * @param position リスト位置
	 * @return アイテムオブジェクト
	 */
	@Override
	public Object getItem(int position) {
		LogUtil.d(TAG, "[IN ] getItem()");
		LogUtil.d(TAG, "position:" + position);
		LogUtil.d(TAG, "[OUT] getItem()");
		return itemList.get(position);
	}

	/**
	 * アイテムID取得処理
	 * <p>
	 * getItemId()のオーバーライド<br>
	 * ファイルリストのアイテムIDを取得する
	 * </p>
	 * @param position リスト位置
	 * @return アイテムID
	 */
	@Override
	public long getItemId(int position) {
		LogUtil.d(TAG, "[IN ] getItemId()");
		LogUtil.d(TAG, "position:" + position);
		LogUtil.d(TAG, "[OUT] getItemId()");
		return position;
	}

	/**
	 * View情報取得処理
	 * <p>
	 * getView()のオーバーライド<br>
	 * ファイルリストのView情報を取得する
	 * </p>
	 * @param position リスト位置
	 * @param convertView Viewオブジェクト
	 * @param parent Viewグループオブジェクト
	 * @return アイテムID
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LogUtil.d(TAG, "[IN ] getView()");
		LogUtil.d(TAG, "position:" + position);

		View view = convertView;

		if (null == view) {
			LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.file_list_row, null);
		}

		/* ファイル毎の情報(アイコン、ファイル名、ファイルサイズ、更新日時)を設定 */
		FileListInfo fileListInfo = (FileListInfo)getItem(position);
		if (null != fileListInfo) {
			/* アイコン設定 */
			ImageView icon = (ImageView)view.findViewById(R.id.file_list_row_ImageView1);
			icon.setImageResource(fileListInfo.getResourceId());

			/* ファイル名設定 */
			TextView fileName = (TextView)view.findViewById(R.id.file_list_row_TextView1);
			fileName.setText(fileListInfo.getFileName());

			/* ファイルサイズ設定 */
			TextView fileSize = (TextView)view.findViewById(R.id.file_list_row_TextView2);
			fileSize.setText("(" + fileListInfo.getFileSize() + ")");

			/* 更新時間設定 */
			TextView timestamp = (TextView)view.findViewById(R.id.file_list_row_TextView3);
			timestamp.setText("(" + fileListInfo.getTimestamp() + ")");
			view.setTag(R.id.file_list_ListView1, fileListInfo);
		}

		LogUtil.d(TAG, "[OUT] getView()");
		return view;
	}
}
