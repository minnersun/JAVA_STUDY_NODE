package com.tedu.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.tedu.utils.JDBCUtils;
import com.tedu.utils.WEBUtils;

public class RegictServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 乱码处理
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		
		// 获取页面中的参数
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String nickname = request.getParameter("nickname");
		String email = request.getParameter("email");
		String valistr = request.getParameter("valistr");
		
		// 判断参数是否为空
		if(WEBUtils.isNull(username)){
			request.setAttribute("msg", "用户名不能为空");
			request.getRequestDispatcher("/regist.jsp").forward(request, response);
			return;
		}
		if(WEBUtils.isNull(password)){
			request.setAttribute("msg", "密码不能为空");
			request.getRequestDispatcher("/regist.jsp").forward(request, response);
			return;
		}
		if(WEBUtils.isNull(password2)){
			request.setAttribute("msg", "确认密码不能为空");
			request.getRequestDispatcher("/regist.jsp").forward(request, response);
			return;
		}
		if(WEBUtils.isNull(nickname)){
			request.setAttribute("msg", "昵称不能为空");
			request.getRequestDispatcher("/regist.jsp").forward(request, response);
			return;
		}
		if(WEBUtils.isNull(email)){
			request.setAttribute("msg", "邮箱不能为空");
			request.getRequestDispatcher("/regist.jsp").forward(request, response);
			return;
		}
		if(WEBUtils.isNull(valistr)){
			request.setAttribute("msg", "验证码不能为空");
			request.getRequestDispatcher("/regist.jsp").forward(request, response);
			return;
		}
		
		
		// 密码一致性校验
		if(password==""||password2==""||(!password.equals(password2))){
			request.setAttribute("msg", "密码输入不一致");
			request.getRequestDispatcher("/regist.jsp").forward(request, response);
			return;
		}
		
		// 邮箱校验
		Pattern pattern = Pattern.compile("[\\w]+@[\\w]+(.[\\w]+){1,3}");
		Matcher matcher = pattern.matcher(email);
		if(!matcher.matches()){
			request.setAttribute("msg", "邮箱格式错误");
			request.getRequestDispatcher("/regist.jsp").forward(request, response);
			return;
		}
		
		// 验证码验证
		// 获取浏览器中的session
		HttpSession session = request.getSession();
		String code = (String)session.getAttribute("code");
		// 比较用户输入的验证码和生成的验证码的值
		if(!code.equalsIgnoreCase(valistr)){
			request.setAttribute("msg", "验证码输入有误");
			request.getRequestDispatcher("/regist.jsp").forward(request, response);
			return;
		}
				
		
		// 数据入库
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// 获取连接池对象
		ComboPooledDataSource source = JDBCUtils.getPoor();
		// 连接数据库
		try {
			conn = source.getConnection();
			// 查询用户名
			ps = conn.prepareStatement("select * from user where username=?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			if(rs.next()){
				request.setAttribute("msg", "用户名已存在");
				request.getRequestDispatcher("/regist.jsp").forward(request, response);
				return;
			}else{
				ps = conn.prepareStatement("insert into user values(null,?,?,?,?)");
				ps.setString(1, username);
				ps.setString(2, password);
				ps.setString(3, nickname);
				ps.setString(4, email);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JDBCUtils.close(conn, ps, rs);
		}
		response.getWriter().write("<h1><font color='red'>注册成功,首页将在三秒后到达战场</font><h1>");
		response.setHeader("refresh","3;url=http://www.easymall.com");
		
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);

	}

}
