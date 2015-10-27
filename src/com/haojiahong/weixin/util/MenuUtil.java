package com.haojiahong.weixin.util;

import net.sf.json.JSONObject;

/**
 * �˵�������
 * 
 * @author haojiahong
 * 
 * @createtime 2015-7-16
 */
public class MenuUtil {
	public final static String MENU_CREATE_URL = " https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	/**
	 * �����˵�
	 * @param json �˵��ṹ
	 * @param accessToken
	 * @return
	 */
	public static boolean createMenu(String json, String accessToken) {
		boolean result = false;
		String requestUrl = MENU_CREATE_URL
				.replace("ACCESS_TOKEN", accessToken);
		String respJson = CommonUtil.httpsRequest(requestUrl, "POST", json);
		JSONObject jsonObject = JSONObject.fromObject(respJson);
		if(jsonObject!=null){
			int errCode =jsonObject.getInt("errcode");
			String errmsg = jsonObject.getString("errmsg");
			if(0==errCode){
				result = true;
			}else{
				//result = false;
				System.out.println("�˵�����ʧ��"+ respJson);
			}
		}
		
		return result;
	}
}
