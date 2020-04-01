package com.tedu.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tedu.utils.VerifyCode;

public class VlidateServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 验证码不能使用缓存图片，所以需要关闭浏览器缓存
		response.setDateHeader("Expires", -1);
		
		// 生成工具类的对象
		VerifyCode vc = new VerifyCode();
		// 将生成的图片返回给页面
		vc.drawImage(response.getOutputStream());
		String code = vc.getCode();
		HttpSession session = request.getSession();
		// 将生成的验证码存入到session中
		session.setAttribute("code", code);
		System.out.println("图片生成成功，code:"+code);
		
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);

	}

}
