package com.haojiahong.weixin.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.haojiahong.weixin.message.resp.Article;
import com.haojiahong.weixin.message.weather.WeatherData;
import com.haojiahong.weixin.message.weather.WeatherResp;

/**
 * �ٶȵ�ͼ����Ԥ��������
 * 
 * @author haojiahong
 * 
 * @createtime 2015-7-26
 */
public class WeatherUtil {
	/**
	 * ���ݳ������Ʋ�ѯ������������ְ棩
	 * 
	 * @param cityName
	 * @return
	 */
	public static String queryWeather(String cityName) {
		// �����ַ
		String requestUrl = "http://api.map.baidu.com/telematics/v3/weather?location=LOCATION&output=json&ak=AK";
		requestUrl = requestUrl.replace("LOCATION",
				CommonUtil.urlEncodeUTF8(cityName)).replace("AK",
				"iDnhehCPbLAXT4UtKlHdTCID");
		// ��������,�õ����ص�json
		String respJSON = CommonUtil.httpRequest(requestUrl, "GET", null);

		// ͨ��GSON��jsonת����java����
		Gson gson = new Gson();
		WeatherResp weatherResp = gson.fromJson(respJSON, WeatherResp.class);
		// ����������ɸ��û���������ʽ��
		String currentCity = cityName;
		List<WeatherData> weatherDataList = weatherResp.getResults().get(0)
				.getWeather_data();
		StringBuffer buffer = new StringBuffer();
		buffer.append(currentCity).append("����Ԥ����").append("\n\n");
		for (WeatherData data : weatherDataList) {
			buffer.append(data.getDate()).append(" ")
					.append(data.getTemperature()).append(", ")
					.append(data.getWeather()).append(", ")
					.append(data.getWind()).append(".").append("\n");
		}
		return buffer.toString();
	}

	/**
	 * ���ݳ������Ʋ�ѯ���������ͼ�İ棩
	 * 
	 * @param cityName
	 * @return
	 */
	public static List<Article> queryWeatherWithPicture(String cityName) {
		// �����ַ
		String requestUrl = "http://api.map.baidu.com/telematics/v3/weather?location=LOCATION&output=json&ak=AK";
		requestUrl = requestUrl.replace("LOCATION",
				CommonUtil.urlEncodeUTF8(cityName)).replace("AK",
				"iDnhehCPbLAXT4UtKlHdTCID");
		// ��������,�õ����ص�json
		String respJSON = CommonUtil.httpRequest(requestUrl, "GET", null);

		// ͨ��GSON��jsonת����java����
		Gson gson = new Gson();
		WeatherResp weatherResp = gson.fromJson(respJSON, WeatherResp.class);
		// ����������ɸ��û���������ʽ��
		String currentCity = cityName;
		List<WeatherData> weatherDataList = weatherResp.getResults().get(0)
				.getWeather_data();

		List<Article> articList = new ArrayList<Article>();
		// ȡ�õ��յ��������
		WeatherData firstData = weatherDataList.get(0);
		Article article = new Article();
		article.setTitle(currentCity + "\n" + firstData.getDate() + "\n"
				+ firstData.getWeather() + " " + firstData.getWind());
		article.setDescription("");
		article.setPicUrl(WeiXinConstant.PROJECT_ROOT + "images/top.jpg");
		article.setUrl("");
		articList.add(article);
		// ����δ������������������ȥ���գ�
		weatherDataList.remove(0);
		for (WeatherData data : weatherDataList) {
			article = new Article();
			article.setTitle(currentCity + " " + data.getDate() + " "
					+ data.getTemperature() + " " + data.getWeather() + " "
					+ data.getWind());
			article.setDescription("");
			article.setPicUrl(WeiXinConstant.PROJECT_ROOT + "images/"
					+ getWeatherPic(data.getDayPictureUrl()));
			article.setUrl("");
			articList.add(article);
		}

		return articList;
	}

	/**
	 * ��ȡ����Ԥ��ͼƬ
	 * 
	 * @return
	 */
	private static String getWeatherPic(String picUrl) {
		String result = picUrl.substring(picUrl.lastIndexOf("/") + 1);
		result = "weather_" + result;
		return result;

	}

	public static void main(String[] args) {
		System.out.println(queryWeatherWithPicture("��̨"));
	}
}
