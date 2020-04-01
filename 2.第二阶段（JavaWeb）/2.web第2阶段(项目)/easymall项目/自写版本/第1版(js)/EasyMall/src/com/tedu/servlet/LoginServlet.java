package com.tedu.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.tedu.utils.JDBCUtils;

// 记住用户名功能的实现
public class LoginServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 乱码处理
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		// 参数获取
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String remname = request.getParameter("remname");
		
		// 判断是否勾选了 【记住用户名】
		if("true".equals(remname)){
			// 如果勾选了则将用户名放入域中
			Cookie cookie = new Cookie("remname", URLEncoder.encode(username, "UTF-8"));
			//设置cookie的路径和时间
			cookie.setPath(request.getContextPath()+"/");
			cookie.setMaxAge(60*60*24);
			// 将cookie返回到浏览器中
			response.addCookie(cookie);
			
		}
		
		// 判断用户名密码是否输入正确
		Connection conn=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ComboPooledDataSource source = JDBCUtils.getPoor();
		try {
			conn =source.getConnection();
			ps = conn.prepareStatement("select * from user where username = ? and password = ?");
			ps.setString(1, username);
			ps.setString(2, password);
			rs = ps.executeQuery();
			if(!rs.next()){
				request.setAttribute("msg", "<font color='red'>用户名或密码不正确</font>");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
				return;
			}else{
				// 设置session 存储用户名
				HttpSession session = request.getSession();
				session.setAttribute("username", username);
				response.sendRedirect("http://www.easymall.com");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			// 关闭连接
			JDBCUtils.close(conn, ps, rs);
		}
		
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);

	}

}
