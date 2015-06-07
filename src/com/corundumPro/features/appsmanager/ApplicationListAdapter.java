package com.corundumPro.features.appsmanager;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.corundumPro.common.util.LogUtil;

/**
 * ApplicationListAdapterクラス
 * <p>
 * 「アプリケーションリストアダプター」クラス
 * </p>
 * @author androbotics.ltd
 */
public class ApplicationListAdapter extends ArrayAdapter<ApplicationListInfo> {
	static final String TAG = "ApplicationListAdapter";

	private List<ApplicationListInfo> itemList;

	private LayoutInflater layoutInflater;
    private int layoutResource;
    /** 選択されたポジション */
    private int selectedPosition = 0;

	/**
	 * コンストラクター
	 *
	 * @param context
	 * @param resource
	 * @param itemList
	 */
	public ApplicationListAdapter(Context context, int resource, List<ApplicationListInfo> itemList) {
		super(context, resource, itemList);
		LogUtil.d(TAG, "[IN ] ApplicationListAdapter()");

		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layoutResource = resource;
		this.itemList = itemList;

		LogUtil.d(TAG, "[OUT] ApplicationListAdapter()");
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
	public ApplicationListInfo getItem(int position) {
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
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LogUtil.d(TAG, "[IN ] getView()");
		LogUtil.d(TAG, "position:" + position);

		final ApplicationListLayout view;

		if (convertView == null) {
			// Viewがなかったら生成
			view = (ApplicationListLayout) layoutInflater.inflate(layoutResource, null);
		} else {
			view = (ApplicationListLayout) convertView;
		}

		view.setChecked(false);

        view.bindView(getItem(position));

        return view;
	}

	/**
	 * 選択されたポジションを取得します。
	 *
	 * @return 選択されたポジション
	 */
	public int getSelectedPosition() {
		return this.selectedPosition;
	}

	/**
	 * 選択されたポジションを設定します。
	 *
	 * @param position
	 */
	public void setSelectedPosition(int position) {
		this.selectedPosition = position;
	}

	/**
	 * 選択されたポジションを減らします。
	 *
	 */
	public void decreaseSelectedPosition() {
		int pos = this.selectedPosition - 1;

		if (pos < 0) {
			pos = 0;
		}
		this.selectedPosition = pos;
	}

	/**
	 * 選択されたポジションを増やします。
	 *
	 */
	public void increaseSelectedPosition() {
		int pos = this.selectedPosition + 1;

		if (pos > getCount() - 1) {
			pos = getCount() - 1;
		}
		this.selectedPosition = pos;
	}

	/**
	 * 選択されたポジションのアプリケーションリストオブジェクトを取得します。
	 *
	 * @return ApplicationListInfo
	 */
	public ApplicationListInfo getSelectedItem() {
		return getItem(this.selectedPosition);
	}

	/**
	 * アプリケーションリストの有無を取得します。
	 *
	 * @return 有：true, 無：false
	 */
	public boolean hasList() {
		boolean result = false;
		if (getCount() > 0) {
			result = true;
		}
		return result;
	}
}
