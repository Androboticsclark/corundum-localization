/*
 *  Copyright(c) 2013- AndroboGroup, All rights reserved.
 */
package com.corundumPro.common.dto;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.corundumPro.common.constant.EnumResponseParams;
import com.corundumPro.common.util.StringUtil;


/**
 * JSON結果DTO.<br>
 * <br>
 *
 * @author  H.Yamazaki
 * @version $id$
 */
public class JsonResultDto implements Serializable{

    private static final long serialVersionUID = 1L;

    /** 終了コード */
    private String returnCode = "";

    /** 表示メッセージ */
    private String message = "";

    /** 原因 */
    private String cause = "";

    /**
     * JSONオブジェクトの内容をバインドします。
     *
     * @param json
     */
    public void bindJson(JSONObject json) {
    	try {
			this.returnCode = StringUtil.nvl(json.getString(EnumResponseParams.RETURN_CODE.getParamName()), "");
	    	this.message = StringUtil.nvl(json.getString(EnumResponseParams.MESSAGE.getParamName()), "");
	    	this.cause = StringUtil.nvl(json.getString(EnumResponseParams.CAUSE.getParamName()), "");
		} catch (JSONException e) {
			// 何もしない
		}
    }

    /**
     * 終了コードを取得します。
     *
     * @return 終了コード
     */
    public String getReturnCode() {
    	return this.returnCode;
    }

    /**
     * 表示メッセージを取得します。
     *
     * @return 表示メッセージ
     */
    public String getMessage() {
    	return this.message;
    }

    /**
     * 原因を取得します。
     *
     * @return 原因
     */
    public String getCause() {
    	return this.cause;
    }

    @Override
    public String toString() {
    	String separator = " ";
    	StringBuilder buf = new StringBuilder("");

    	buf.append("returnCode:").append(this.returnCode);
    	buf.append(separator);
    	buf.append("message:").append(this.message);
    	buf.append(separator);
    	buf.append("cause:").append(this.cause);

    	return buf.toString();
    }
}
