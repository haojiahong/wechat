package com.haojiahong.weixin.message.menu;
/**
 * �������͵İ�ť�����������˵���һ���˵�
 * @author haojiahong
 *
 * @createtime 2015-7-16
 */
public class ComplexButton extends BaseButton{
	private BaseButton[] sub_button;//���������sub_button������ʽ�������Ĳ��У�΢��Ҫ���

	public BaseButton[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(BaseButton[] sub_button) {
		this.sub_button = sub_button;
	}

	
}
