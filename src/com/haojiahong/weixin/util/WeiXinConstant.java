package com.haojiahong.weixin.util;

public class WeiXinConstant {
	// 与开发模式接口配置信息中的Token保持一致
	public static String token = "weixinCourse";

	public static final String ENCODING_AES_KEY = "bOvA3BTw75xGejgylHuEesAKD08kjtKncF0oXZDTYdQ";

//	public static final String APP_ID = "wxc640ecdb05dd8347";
	
//	public static final String APP_SECRET = "a2c0fbe845a399a6b47c6c8da481a1f9";

	public static final String APP_ID = "wxb28ce8d2c811912f";
	
	public static final String APP_SECRET = "cbae072db693e00cc9edb8c0fd512c83";
	
	// 获取接口凭证的url
	public static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	//项目的根路径
	public static final String PROJECT_ROOT = "http://jokerone.imwork.net/wechat/";
}
