package com.haojiahong.weixin.message.weather;

import java.util.List;

public class WeatherResp {
	private String error;
	private String status;
	private List<WeatherResult> results;//������Ҫ�ͷ��ؽ������һ��
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<WeatherResult> getResults() {
		return results;
	}
	public void setResults(List<WeatherResult> results) {
		this.results = results;
	}
}
