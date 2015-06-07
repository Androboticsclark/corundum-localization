package com.corundumPro.features.whitelist;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.corundumPro.R;
import com.corundumPro.common.activity.BaseActivity;
import com.corundumPro.common.dto.WhiteListInfo;
import com.corundumPro.common.util.LogUtil;

/**
 * WhiteListAdapterクラス
 * <p>
 * 「ホワイトリストアダプター」クラス
 * </p>
 * @author androbotics.ltd
 */
public class WhiteListAdapter extends BaseAdapter {
	static final String TAG = "WhiteListAdapter";

	private BaseActivity activity;
	private List<WhiteListInfo> itemList;

	public WhiteListAdapter(BaseActivity activity, List<WhiteListInfo> itemList) {
		LogUtil.d(TAG, "[IN ] WhiteListAdapter()");

		this.activity = activity;
		this.itemList = itemList;

		LogUtil.d(TAG, "[OUT] WhiteListAdapter()");
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

		TextView titleTextView;
		TextView urlTextView;
		View view = convertView;

		if (null == view) {
			LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.white_list_row, null);
		}

		WhiteListInfo item = (WhiteListInfo)getItem(position);

		if (null != item) {
			/* タイトル設定 */
			titleTextView = (TextView)view.findViewById(R.id.white_list_row_TextView1);
			titleTextView.setText(item.getWhiteListTitle());

			/* URL設定 */
			urlTextView = (TextView)view.findViewById(R.id.white_list_row_TextView2);
			urlTextView.setText(item.getWhiteListUrl());
			view.setTag(R.id.white_list_ListView1, item);
		}

		LogUtil.d(TAG, "[OUT] getView()");
		return view;
	}
}
