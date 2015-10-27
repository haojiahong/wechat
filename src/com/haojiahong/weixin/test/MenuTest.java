package com.haojiahong.weixin.test;

import net.sf.json.JSONObject;

import com.haojiahong.weixin.message.domain.Token;
import com.haojiahong.weixin.message.menu.BaseButton;
import com.haojiahong.weixin.message.menu.ClickButton;
import com.haojiahong.weixin.message.menu.ComplexButton;
import com.haojiahong.weixin.message.menu.Menu;
import com.haojiahong.weixin.message.menu.ViewButton;
import com.haojiahong.weixin.util.CommonUtil;
import com.haojiahong.weixin.util.MenuUtil;
import com.haojiahong.weixin.util.WeiXinConstant;

public class MenuTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClickButton btn11 = new ClickButton();
		btn11.setName("天气预报");
		btn11.setType("click");
		btn11.setKey("KEY_WEATHER");
		
		ClickButton btn21 = new ClickButton();
		btn21.setName("歌曲");
		btn21.setType("click");
		btn21.setKey("KEY_MUSIC");
		
		ViewButton btn22 = new ViewButton();
		btn22.setName("baidu");
		btn22.setType("view");
		btn22.setUrl("http://www.baidu.com");
		
		ComplexButton btn2 = new ComplexButton();
		btn2.setName("menu");
		btn2.setSub_button(new BaseButton[]{btn21,btn22});
		
		Menu menu = new Menu();
		menu.setButton(new BaseButton[]{btn11,btn2});
		//将java对象转成json字符串
		String jsonStr = JSONObject.fromObject(menu).toString();
		System.out.println(jsonStr);
		Token token = CommonUtil.getAccessToken(WeiXinConstant.APP_ID, WeiXinConstant.APP_SECRET);
		boolean result = MenuUtil.createMenu(jsonStr, token.getAccessToken());
		System.out.println(result);
	}

}
