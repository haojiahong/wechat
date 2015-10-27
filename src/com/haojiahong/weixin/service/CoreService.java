package com.haojiahong.weixin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.haojiahong.weixin.message.resp.Article;
import com.haojiahong.weixin.message.resp.NewsMessage;
import com.haojiahong.weixin.message.resp.TextMessage;
import com.haojiahong.weixin.util.FacePlusPlusUtil;
import com.haojiahong.weixin.util.MessageUtil;
import com.haojiahong.weixin.util.WeatherUtil;
import com.haojiahong.weixin.util.WeiXinConstant;

/**
 * 核心服务类
 * 
 * @author haojiahong
 */
public class CoreService {
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return xml
	 */
	public static String processRequest(HttpServletRequest request) {
		// xml格式的消息数据
		String respXml = null;
		try {
			// 调用parseXml方法解析请求消息
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// 发送方帐号
			String fromUserName = requestMap.get("FromUserName");
			// 开发者微信号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {

				String content = requestMap.get("Content");
				// 如果用户发送的内容以天气 开头或结尾，则调用天气预报返回天气情况、
				if (content.startsWith("天气") || content.endsWith("天气")) {
					String cityName = content.trim().replace("天气", "");
					String weatherInfo = WeatherUtil.queryWeather(cityName);
					textMessage.setContent(weatherInfo);
				} else {

					textMessage.setContent("sar mysql的使用测试");
				}
				// MySQLUtil.saveTextMessage(fromUserName, content);

				respXml = MessageUtil.messageToXml(textMessage);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return respXml;
	}

	public static String process(Map<String, String> requestMap) {
		// xml格式的消息数据
		String respXml = null;
		try {
			// 发送方帐号
			String fromUserName = requestMap.get("FromUserName");
			// 开发者微信号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				String content = requestMap.get("Content");
				// 如果用户发送的内容以天气 开头或结尾，则调用天气预报返回天气情况、
				if (content.startsWith("天气") || content.endsWith("天气")) {

					String cityName = content.trim().replace("天气", "");
					// 文字版天气预报
					// String weatherInfo = WeatherUtil.queryWeather(cityName);
					// textMessage.setContent(weatherInfo);
					// 图文版天气预报
					List<Article> articList = WeatherUtil.queryWeatherWithPicture(cityName);

					NewsMessage nm = new NewsMessage();
					nm.setFromUserName(toUserName);
					nm.setToUserName(fromUserName);
					nm.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
					nm.setCreateTime(new Date().getTime());
					nm.setArticleCount(articList.size());
					nm.setArticles(articList);
					respXml = MessageUtil.messageToXml(nm);

				} else if ("1".equals(content)) {
					textMessage.setContent("点击开始玩<a href=\"" + WeiXinConstant.PROJECT_ROOT + "2048/index.html"
							+ "\">2048</a>游戏");
					respXml = MessageUtil.messageToXml(textMessage);
				} else if ("2".equals(content)) {
					List<Article> articList = new ArrayList<Article>();
					Article article = new Article();
					article.setTitle("2048");
					article.setDescription("坑货，来玩啊");
					article.setPicUrl(WeiXinConstant.PROJECT_ROOT + "2048/2048.jpg");
					article.setUrl(WeiXinConstant.PROJECT_ROOT + "2048/index.html");
					articList.add(article);

					NewsMessage nm = new NewsMessage();
					nm.setFromUserName(toUserName);
					nm.setToUserName(fromUserName);
					nm.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
					nm.setCreateTime(new Date().getTime());
					nm.setArticleCount(articList.size());
					nm.setArticles(articList);
					respXml = MessageUtil.messageToXml(nm);
				} else if ("3".equals(content)) {
					textMessage.setContent("点击开始<a href=\"" + WeiXinConstant.PROJECT_ROOT + "BMI.html"
							+ "\">BMI</a>计算体重标准");
					respXml = MessageUtil.messageToXml(textMessage);
				}else {

					textMessage.setContent("sar mysql的使用测试");
					// MySQLUtil.saveTextMessage(fromUserName, content);
					respXml = MessageUtil.messageToXml(textMessage);
				}

			}
			// 图片消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				String imageUrl = requestMap.get("PicUrl");
				textMessage.setContent(FacePlusPlusUtil.detectFace(imageUrl));
				respXml = MessageUtil.messageToXml(textMessage);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return respXml;
	}
}
