<%@page import="java.net.URLDecoder"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" session="false"%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" href="css/login.css"/>
		<title>EasyMall欢迎您登陆</title>
	
	</head>
	<body>
	 <%
		// 如果存在存放remname的cookie值 将用它存储
		Cookie renameC = null;
		// 获取浏览器中的cookie
		Cookie[] cs = request.getCookies();
		// cs不为null时获取判断有无remname的键
		if(cs != null){
			// 遍历cs
			for(Cookie c:cs){
				// 判断有无remname的键
				if("remname".equals(c.getName())){
					renameC = c;
				}			
			}
		}
		
		// 如果存在remname将Cookie中的值拿出来
		String username = "";
		if(renameC!=null){
			// 获取remname中的值
			username = URLDecoder.decode(renameC.getValue(),"utf-8");
		}
	 %> 


		<h1>欢迎登陆EasyMall</h1>
		<form action="${pageContext.request.contextPath}/LoginServlet" method="POST">
			<table>
				<tr>
					<td class="tdx">用户名：</td>
					<td><input type="text" name="username" name="username" value="<%=username%>"/></td>
				</tr>
				<tr>
					<td class="tdx">密&nbsp;&nbsp; 码：</td>
					<td><input type="password" name="password"/></td>
					<td><%=request.getAttribute("msg")==null?"":request.getAttribute("msg") %></td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="checkbox" name="remname" value="true"
						
							${empty cookie.remname.value?"":"checked='checked'" }
						
						/>记住用户名
						<input type="checkbox" name="autologin" value="true"/>30天内自动登陆
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align:center">
						<input type="submit" value="登 陆"/>
					</td>
				</tr>
			</table>
		</form>		
	</body>
</html>
