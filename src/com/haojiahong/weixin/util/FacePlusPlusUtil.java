package com.haojiahong.weixin.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.haojiahong.weixin.message.domain.Face;

/**
 * Face++接口工具类
 * 
 * @author haojiahong
 * 
 * @createtime 2015-7-25
 */
public class FacePlusPlusUtil {
	/**
	 * 人脸识别 给外部调用
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static String detectFace(String imageUrl) {
		// 封装请求地址
		String requestUrl = "http://apicn.faceplusplus.com/v2/detection/detect?api_key=YOUR_API_KEY&api_secret=YOUR_API_SECRET&url=URL";
		requestUrl = requestUrl
				.replace("YOUR_API_KEY", "e9f27ec9f22b2a06c99db5d63b1a3f04")
				.replace("YOUR_API_SECRET", "zloR98g-dOEcNwsxDxAgP58_a_I9QPvX")
				.replace("URL", CommonUtil.urlEncodeUTF8(imageUrl));
		// 发起请求
		String respJSON = CommonUtil.httpRequest(requestUrl, "GET", null);
		List<Face> faceList = new ArrayList<Face>();
		// 解析JSON
		JSONArray faceArray = (JSONArray) JSONObject.fromObject(respJSON).get(
				"face");
		for (int i = 0; i < faceArray.size(); i++) {
			JSONObject faceObject = (JSONObject) faceArray.get(i);
			JSONObject attrObject = faceObject.getJSONObject("attribute");
			JSONObject posObject = faceObject.getJSONObject("position");

			Face face = new Face();
			face.setFaceId(faceObject.getString("face_id"));
			face.setAgeValue(attrObject.getJSONObject("age").getInt("value"));
			face.setAgeRange(attrObject.getJSONObject("age").getInt("range"));
			face.setGenderValue(genderConvert(attrObject
					.getJSONObject("gender").getString("value")));
			face.setGenderConfidence(attrObject.getJSONObject("gender")
					.getDouble("confidence"));
			face.setRaceValue(raceConvert(attrObject.getJSONObject("race")
					.getString("value")));
			face.setRaceConfidence(attrObject.getJSONObject("race").getDouble(
					"confidence"));
			face.setSmilingValue(attrObject.getJSONObject("smiling").getDouble(
					"value"));
			face.setCenterX(posObject.getJSONObject("center").getDouble("x"));
			face.setCenterY(posObject.getJSONObject("center").getDouble("y"));
			faceList.add(face);
		}
		//排序
		Collections.sort(faceList);
		
		StringBuffer buffer = new StringBuffer();
		// 检测到一张人脸
		if (faceList.size() == 1) {
			buffer.append("共检测到一张人脸:").append("\n\n");
			for (Face face : faceList) {
				buffer.append(face.getRaceValue()).append("；")
						.append(face.getGenderValue()).append("；")
						.append(face.getAgeValue()).append("岁左右");
			}
		}
		// 检测到多张人脸
		if (faceList.size() > 1) {
			buffer.append("共检测到").append(faceList.size())
					.append("张人脸，从左到右依次为：").append("\n\n");
			for (Face face : faceList) {
				buffer.append(face.getRaceValue()).append("；")
						.append(face.getGenderValue()).append("；")
						.append(face.getAgeValue()).append("岁左右")
						.append("\n\n");
			}
		}
		return buffer.toString();
	}

	private static String raceConvert(String race) {
		String result = "黄种人";
		if (race.equals("Asian")) {
			result = "黄种人";
		}
		if (race.equals("White")) {
			result = "白种人";
		}
		if (race.equals("Black")) {
			result = "黑种人";
		}

		return result;
	}

	private static String genderConvert(String gender) {
		String result = "男";
		if (gender.equals("Female")) {
			result = "女";
		}
		return result;
	}

	public static void main(String[] args) {
		String result = detectFace("http://imgsrc.baidu.com/forum/pic/item/8b82b9014a90f60384c4a7c23912b31bb051ed3b.jpg");
		System.out.println(result);
	}
}
