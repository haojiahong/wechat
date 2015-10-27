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
 * ������ĺ�����
 * 
 * @author haojiahong
 */
public class CoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * ����У�飨ȷ����������΢�ŷ�������
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// ΢�ż���ǩ��
		String signature = request.getParameter("signature");
		// ʱ���
		String timestamp = request.getParameter("timestamp");
		// �����
		String nonce = request.getParameter("nonce");
		// ����ַ���
		String echostr = request.getParameter("echostr");

		PrintWriter out = response.getWriter();
		// ����У�飬��У��ɹ���ԭ������echostr����ʾ����ɹ����������ʧ��
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
		out.close();
		out = null;
	}

	/**
	 * ����΢�ŷ�������������Ϣ
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// ��������Ӧ�ı��������ΪUTF-8����ֹ�������룩
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// ΢�ż���ǩ��
		String signature = request.getParameter("signature");
		// ʱ���
		String timestamp = request.getParameter("timestamp");
		// �����
		String nonce = request.getParameter("nonce");

		// �ӽ�������
		String encryptType = request.getParameter("encrypt_type");

		System.out.println(signature);
		System.out.println(timestamp);
		System.out.println(nonce);
		System.out.println(encryptType);

		try {
			PrintWriter out = response.getWriter();

			if (SignUtil.checkSignature(signature, timestamp, nonce)) {

				Map<String, String> requestMap = null;

				// ��ȫģʽ
				if ("aes".equals(encryptType)) {
					requestMap = MessageUtil.parseXmlCrypt(request);
					// ���ú���ҵ���������Ϣ��������Ϣ
					String respXml = CoreService.process(requestMap);
					// ����
					respXml = MessageUtil.getWxCrypt().encryptMsg(respXml,
							timestamp, nonce);
					System.out.println(respXml);
					out.print(respXml);

				} else {// ����ģʽ
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
