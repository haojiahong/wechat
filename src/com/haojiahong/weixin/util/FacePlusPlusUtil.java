package com.haojiahong.weixin.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.haojiahong.weixin.message.domain.Face;

/**
 * Face++�ӿڹ�����
 * 
 * @author haojiahong
 * 
 * @createtime 2015-7-25
 */
public class FacePlusPlusUtil {
	/**
	 * ����ʶ�� ���ⲿ����
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static String detectFace(String imageUrl) {
		// ��װ�����ַ
		String requestUrl = "http://apicn.faceplusplus.com/v2/detection/detect?api_key=YOUR_API_KEY&api_secret=YOUR_API_SECRET&url=URL";
		requestUrl = requestUrl
				.replace("YOUR_API_KEY", "e9f27ec9f22b2a06c99db5d63b1a3f04")
				.replace("YOUR_API_SECRET", "zloR98g-dOEcNwsxDxAgP58_a_I9QPvX")
				.replace("URL", CommonUtil.urlEncodeUTF8(imageUrl));
		// ��������
		String respJSON = CommonUtil.httpRequest(requestUrl, "GET", null);
		List<Face> faceList = new ArrayList<Face>();
		// ����JSON
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
		//����
		Collections.sort(faceList);
		
		StringBuffer buffer = new StringBuffer();
		// ��⵽һ������
		if (faceList.size() == 1) {
			buffer.append("����⵽һ������:").append("\n\n");
			for (Face face : faceList) {
				buffer.append(face.getRaceValue()).append("��")
						.append(face.getGenderValue()).append("��")
						.append(face.getAgeValue()).append("������");
			}
		}
		// ��⵽��������
		if (faceList.size() > 1) {
			buffer.append("����⵽").append(faceList.size())
					.append("������������������Ϊ��").append("\n\n");
			for (Face face : faceList) {
				buffer.append(face.getRaceValue()).append("��")
						.append(face.getGenderValue()).append("��")
						.append(face.getAgeValue()).append("������")
						.append("\n\n");
			}
		}
		return buffer.toString();
	}

	private static String raceConvert(String race) {
		String result = "������";
		if (race.equals("Asian")) {
			result = "������";
		}
		if (race.equals("White")) {
			result = "������";
		}
		if (race.equals("Black")) {
			result = "������";
		}

		return result;
	}

	private static String genderConvert(String gender) {
		String result = "��";
		if (gender.equals("Female")) {
			result = "Ů";
		}
		return result;
	}

	public static void main(String[] args) {
		String result = detectFace("http://imgsrc.baidu.com/forum/pic/item/8b82b9014a90f60384c4a7c23912b31bb051ed3b.jpg");
		System.out.println(result);
	}
}
