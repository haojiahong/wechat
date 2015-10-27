package com.haojiahong.weixin.test;

import com.haojiahong.weixin.message.domain.Token;
import com.haojiahong.weixin.util.CommonUtil;

public class tokenTest {
	public static void main(String[] args) {
		Token token = CommonUtil.getAccessToken("wxc640ecdb05dd8347",
				"a2c0fbe845a399a6b47c6c8da481a1f9");
		System.out.println(token.getAccessToken());
		System.out.println(token.getExpiresIn());
	}
}
