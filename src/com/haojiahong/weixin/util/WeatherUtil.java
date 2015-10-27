package com.haojiahong.weixin.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.haojiahong.weixin.message.resp.Article;
import com.haojiahong.weixin.message.weather.WeatherData;
import com.haojiahong.weixin.message.weather.WeatherResp;

/**
 * 百度地图天气预报工具类
 * 
 * @author haojiahong
 * 
 * @createtime 2015-7-26
 */
public class WeatherUtil {
	/**
	 * 根据城市名称查询天气情况（文字版）
	 * 
	 * @param cityName
	 * @return
	 */
	public static String queryWeather(String cityName) {
		// 请求地址
		String requestUrl = "http://api.map.baidu.com/telematics/v3/weather?location=LOCATION&output=json&ak=AK";
		requestUrl = requestUrl.replace("LOCATION",
				CommonUtil.urlEncodeUTF8(cityName)).replace("AK",
				"iDnhehCPbLAXT4UtKlHdTCID");
		// 发起请求,拿到返回的json
		String respJSON = CommonUtil.httpRequest(requestUrl, "GET", null);

		// 通过GSON将json转换成java对象
		Gson gson = new Gson();
		WeatherResp weatherResp = gson.fromJson(respJSON, WeatherResp.class);
		// 将结果解析成给用户看到的样式：
		String currentCity = cityName;
		List<WeatherData> weatherDataList = weatherResp.getResults().get(0)
				.getWeather_data();
		StringBuffer buffer = new StringBuffer();
		buffer.append(currentCity).append("天气预报：").append("\n\n");
		for (WeatherData data : weatherDataList) {
			buffer.append(data.getDate()).append(" ")
					.append(data.getTemperature()).append(", ")
					.append(data.getWeather()).append(", ")
					.append(data.getWind()).append(".").append("\n");
		}
		return buffer.toString();
	}

	/**
	 * 根据城市名称查询天气情况（图文版）
	 * 
	 * @param cityName
	 * @return
	 */
	public static List<Article> queryWeatherWithPicture(String cityName) {
		// 请求地址
		String requestUrl = "http://api.map.baidu.com/telematics/v3/weather?location=LOCATION&output=json&ak=AK";
		requestUrl = requestUrl.replace("LOCATION",
				CommonUtil.urlEncodeUTF8(cityName)).replace("AK",
				"iDnhehCPbLAXT4UtKlHdTCID");
		// 发起请求,拿到返回的json
		String respJSON = CommonUtil.httpRequest(requestUrl, "GET", null);

		// 通过GSON将json转换成java对象
		Gson gson = new Gson();
		WeatherResp weatherResp = gson.fromJson(respJSON, WeatherResp.class);
		// 将结果解析成给用户看到的样式：
		String currentCity = cityName;
		List<WeatherData> weatherDataList = weatherResp.getResults().get(0)
				.getWeather_data();

		List<Article> articList = new ArrayList<Article>();
		// 取得当日的天气情况
		WeatherData firstData = weatherDataList.get(0);
		Article article = new Article();
		article.setTitle(currentCity + "\n" + firstData.getDate() + "\n"
				+ firstData.getWeather() + " " + firstData.getWind());
		article.setDescription("");
		article.setPicUrl(WeiXinConstant.PROJECT_ROOT + "images/top.jpg");
		article.setUrl("");
		articList.add(article);
		// 遍历未来三天的天气情况（除去当日）
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
	 * 获取天气预报图片
	 * 
	 * @return
	 */
	private static String getWeatherPic(String picUrl) {
		String result = picUrl.substring(picUrl.lastIndexOf("/") + 1);
		result = "weather_" + result;
		return result;

	}

	public static void main(String[] args) {
		System.out.println(queryWeatherWithPicture("烟台"));
	}
}
