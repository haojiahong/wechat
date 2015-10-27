package com.haojiahong.weixin.message.menu;
/**
 * 复合类型的按钮，包含二级菜单的一级菜单
 * @author haojiahong
 *
 * @createtime 2015-7-16
 */
public class ComplexButton extends BaseButton{
	private BaseButton[] sub_button;//这个必须是sub_button这种形式，其他的不行，微信要求的

	public BaseButton[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(BaseButton[] sub_button) {
		this.sub_button = sub_button;
	}

	
}
