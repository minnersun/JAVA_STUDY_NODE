<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE HTML>
<html>
	<head>
		<title>欢迎注册EasyMall</title>
		<meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" href="css/regist.css"/>
		<script type="text/javascript" src="js/jquery-1.4.2.js"></script>
		<script type="text/javascript">
			
			// 前台校验
			var formObj = {
				// 校验输入的数据
				"checkForm" : function(){
					var flag = true;
					flag = this.checkNull("username","用户名不能为空") && flag;
					flag = this.checkNull("password", "密码不能为空") && flag;
					flag = this.checkNull("password2", "确认密码不能为空") && flag;
					flag = this.checkNull("nickname", "昵称不能为空") && flag;
					flag = this.checkNull("email", "邮箱不能为空") && flag;
					flag = this.checkNull("valister", "验证码不能为空") && flag;
					
					flag = this.checkPassword() && flag;
					flag = this.checkEmail() && flag;
					return flag;
				},
				// 非空校验的函数
				"checkNull" : function(name,msg){
					// 获取标签中参数的值
					var value = $("input[name='"+name+"']").val();

					// 清空对应input标签后的span标签中的值
					$("input[name='"+name+"']").nextAll("span").text("");
					// 判断是否为空
					if(value==""){
						$("input[name='"+name+"']").nextAll("span").text(msg).css("color","red").css("color","red");
						return false;
					}					
					return true;
				},
			
				// 验证密码的一致性
				"checkPassword" : function(){
					// 获取password的值
					var password = $("input[name='password']").val();
					// 获取password2的值
					var password2 = $("input[name='password2']").val();
					
					if(password!=""&&password2!=""&&password!=password2){
						$("input[name='password2']").nextAll("span").text("输入的密码不一致").css("color","red");
						return false;
					}
					return true;
				},
				
				// 邮箱格式校验
				"checkEmail" : function(){
					// 邮箱的正则表达式
					var reg = /\w+@\w+(\.\w+)+/;
					// 获取用户输入的邮箱
					var email = $("input[name='email']").val();
					
					if(email!=""&&!reg.test(email)){
						$("input[name='email']").nextAll("span").text("邮箱格式不正确").css("color","red");
						return false;
					}
					return true;
				},
				
				
			};
			
			//页面加载完成后的事件
			$(function(){
				// 单击图片刷新新的验证码
				$("img[name='img']").click(function(){
					$(this).attr("src","${pageContext.request.contextPath}/VlidateServlet?time="+new Date().getTime());
				});
				
				
				$("input[name='username']").blur(function(){
					//获取输入框中的内容
					var username = $(this).val();
					//如果用户名为空则不需要进行ajax校验
					if(username == ""){
						return;
					}
					//实现ajax
					$("#username_msg").load("${pageContext.request.contextPath}/AjaxCheckUsername",{"username":username});
					
					
				});					
					
			});
			
		</script>
	</head>
	<body>
		<%@include file="_head.jsp" %>
		<form action="<%=request.getContextPath() %>/RegictServlet" method="POST" onsubmit="return formObj.checkForm()">
			<h1>欢迎注册EasyMall</h1>
			<table>
				<tr>
				<td colspan="2" style="text-align: center;"><font color="red" size="2" >	${requestScope.msg }	</font></td>
				</tr>
				<tr>
					<td class="tds">用户名：</td>
					<td>
						<input type="text" name="username" value="${param.username }"/> 
						
						<span id="username_msg"></span>
					</td>
				</tr>
				<tr>
					<td class="tds">密码：</td>
					<td>
						<input type="password" name="password"/><span></span>
					</td>
				</tr>
				<tr>
					<td class="tds">确认密码：</td>
					<td>
						<input type="password" name="password2"/><span></span>
					</td>
				</tr>
				<tr>
					<td class="tds">昵称：</td>
					<td>
						<input type="text" name="nickname" value="${param.nickname }"/><span></span>	
					</td>
				</tr>
				<tr>
					<td class="tds">邮箱：</td>
					<td>
						<input type="text" name="email" value="${param.email }"/><span></span>
					</td>
				</tr>
				<tr>
					<td class="tds">验证码：</td>
					<td>
						<input type="text" name="valistr"/>
						<img name="img" src="${pageContext.request.contextPath}/VlidateServlet" width="" height="" alt="此处是验证码" /><span></span>
					</td>
				</tr>
				<tr>
					<td class="sub_td" colspan="2" class="tds">
						<input type="submit" value="注册用户"/><span></span>
					</td>
				</tr>
			</table>
		</form>
		<%@include file="_foot.jsp" %>
	</body>
</html>
