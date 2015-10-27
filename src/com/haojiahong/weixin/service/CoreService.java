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
 * ���ķ�����
 * 
 * @author haojiahong
 */
public class CoreService {
	/**
	 * ����΢�ŷ���������
	 * 
	 * @param request
	 * @return xml
	 */
	public static String processRequest(HttpServletRequest request) {
		// xml��ʽ����Ϣ����
		String respXml = null;
		try {
			// ����parseXml��������������Ϣ
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// ���ͷ��ʺ�
			String fromUserName = requestMap.get("FromUserName");
			// ������΢�ź�
			String toUserName = requestMap.get("ToUserName");
			// ��Ϣ����
			String msgType = requestMap.get("MsgType");

			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {

				String content = requestMap.get("Content");
				// ����û����͵����������� ��ͷ���β�����������Ԥ���������������
				if (content.startsWith("����") || content.endsWith("����")) {
					String cityName = content.trim().replace("����", "");
					String weatherInfo = WeatherUtil.queryWeather(cityName);
					textMessage.setContent(weatherInfo);
				} else {

					textMessage.setContent("sar mysql��ʹ�ò���");
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
		// xml��ʽ����Ϣ����
		String respXml = null;
		try {
			// ���ͷ��ʺ�
			String fromUserName = requestMap.get("FromUserName");
			// ������΢�ź�
			String toUserName = requestMap.get("ToUserName");
			// ��Ϣ����
			String msgType = requestMap.get("MsgType");

			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				String content = requestMap.get("Content");
				// ����û����͵����������� ��ͷ���β�����������Ԥ���������������
				if (content.startsWith("����") || content.endsWith("����")) {

					String cityName = content.trim().replace("����", "");
					// ���ְ�����Ԥ��
					// String weatherInfo = WeatherUtil.queryWeather(cityName);
					// textMessage.setContent(weatherInfo);
					// ͼ�İ�����Ԥ��
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
					textMessage.setContent("�����ʼ��<a href=\"" + WeiXinConstant.PROJECT_ROOT + "2048/index.html"
							+ "\">2048</a>��Ϸ");
					respXml = MessageUtil.messageToXml(textMessage);
				} else if ("2".equals(content)) {
					List<Article> articList = new ArrayList<Article>();
					Article article = new Article();
					article.setTitle("2048");
					article.setDescription("�ӻ������氡");
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
					textMessage.setContent("�����ʼ<a href=\"" + WeiXinConstant.PROJECT_ROOT + "BMI.html"
							+ "\">BMI</a>�������ر�׼");
					respXml = MessageUtil.messageToXml(textMessage);
				}else {

					textMessage.setContent("sar mysql��ʹ�ò���");
					// MySQLUtil.saveTextMessage(fromUserName, content);
					respXml = MessageUtil.messageToXml(textMessage);
				}

			}
			// ͼƬ��Ϣ
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
