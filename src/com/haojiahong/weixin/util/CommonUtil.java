package com.haojiahong.weixin.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;

import com.haojiahong.weixin.message.domain.Token;

public class CommonUtil {
	/**
	 * 处理https请求（get/post）
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方法
	 * @param outputStr
	 *            请求参数
	 * @return
	 */
	public static String httpsRequest(String requestUrl, String requestMethod,
			String outputStr) {
		StringBuffer buffer = null;
		try {
			// 创建SSLContext
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			TrustManager[] tm = { new MyX509TrustManager() };
			// 初始化
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 获取SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			// 设置当前实例使用的SSLSocketFactory
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();

			// 往服务器端写内容
			if (outputStr != null) {
				OutputStream os = conn.getOutputStream();
				os.write(outputStr.getBytes("UTF-8"));
				os.close();
			}

			// 读取服务器端返回的内容
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			buffer = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}

	public static Token getAccessToken(String appId, String appSecret) {
		Token token = null;
		String requestUrl = WeiXinConstant.TOKEN_URL.replace("APPID", appId)
				.replace("APPSECRET", appSecret);
		String jsonStr = CommonUtil.httpsRequest(requestUrl, "GET", null);
		try {

			JSONObject jsonObject = JSONObject.fromObject(jsonStr);

			String accessToken = jsonObject.getString("access_token");
			int expiresIn = jsonObject.getInt("expires_in");
			token = new Token();
			token.setAccessToken(accessToken);
			token.setExpiresIn(expiresIn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return token;
	}
	
	/**
	 * 处理http请求（get/post）
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方法
	 * @param outputStr
	 *            请求参数
	 * @return
	 */
	public static String httpRequest(String requestUrl, String requestMethod,
			String outputStr) {
		StringBuffer buffer = null;
		try {
			
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();

			// 往服务器端写内容
			if (outputStr != null) {
				OutputStream os = conn.getOutputStream();
				os.write(outputStr.getBytes("UTF-8"));
				os.close();
			}

			// 读取服务器端返回的内容
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			buffer = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}
	/**
	 * url编码
	 * @param source
	 * @return
	 */
	public static String urlEncodeUTF8(String source){
		String result = source;
		try {
			result = URLEncoder.encode(source,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
}
