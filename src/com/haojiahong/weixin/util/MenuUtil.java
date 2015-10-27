package com.haojiahong.weixin.util;

import net.sf.json.JSONObject;

/**
 * 菜单工具类
 * 
 * @author haojiahong
 * 
 * @createtime 2015-7-16
 */
public class MenuUtil {
	public final static String MENU_CREATE_URL = " https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	/**
	 * 创建菜单
	 * @param json 菜单结构
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
				System.out.println("菜单创建失败"+ respJson);
			}
		}
		
		return result;
	}
}
