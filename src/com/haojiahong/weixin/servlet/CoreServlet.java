package com.haojiahong.weixin.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haojiahong.weixin.service.CoreService;
import com.haojiahong.weixin.util.MessageUtil;
import com.haojiahong.weixin.util.SignUtil;

/**
 * 请求处理的核心类
 * 
 * @author haojiahong
 */
public class CoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 请求校验（确认请求来自微信服务器）
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		PrintWriter out = response.getWriter();
		// 请求校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
		out.close();
		out = null;
	}

	/**
	 * 处理微信服务器发来的消息
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");

		// 加解密类型
		String encryptType = request.getParameter("encrypt_type");

		System.out.println(signature);
		System.out.println(timestamp);
		System.out.println(nonce);
		System.out.println(encryptType);

		try {
			PrintWriter out = response.getWriter();

			if (SignUtil.checkSignature(signature, timestamp, nonce)) {

				Map<String, String> requestMap = null;

				// 安全模式
				if ("aes".equals(encryptType)) {
					requestMap = MessageUtil.parseXmlCrypt(request);
					// 调用核心业务类接收消息、处理消息
					String respXml = CoreService.process(requestMap);
					// 加密
					respXml = MessageUtil.getWxCrypt().encryptMsg(respXml,
							timestamp, nonce);
					System.out.println(respXml);
					out.print(respXml);

				} else {// 明文模式
					requestMap = MessageUtil.parseXml(request);
					String respXml = CoreService.process(requestMap);
					out.print(respXml);
				}

			}

			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
