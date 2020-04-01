/**
* 一个用户对象类，这个类只是一个演示案例。
* 为了简单化，所以这个用户类并没有包含太多的属性和方法。
* 在一个现实的项目中，像这样的用户类，将有不少的属性和方法。
* 尤其是类中的方法，需要经过大量的测试，
* 保证这些方法是可以正确执行的，并能够得到正确的结果的。
**/
function User(id,name){
	this.name=name;
	this.id=id;
	this.email = "";
	this.phone = "";
	this.age = 0;
	
	this.EMAIL_WRONG_FORMAT=0;
	this.EMAIL_WRONG_HOST=-1;
	this.EMAIL_CORRECT=1
}

User.prototype.setEmail=function (email){
	// summary:  设置用户的email。
	//           保存的email地址必须是经过验证的，
	
	this.email=email ;
}

User.prototype.getEmail=function (){
	// summary:  获取用户的Email地址
	//           虽然可以通过email 属性直接获得email地址
	//           按照面向对象的规范，还是用get方法来获取属性比较好
	// return String ：
	//           如果还没有设置email地址，将根据用户名，建立一个默认Email地址
	
	if(this.email==""){
		return this.name+"@"+"smartdio.com";
	}
	return this.email ;
		
}
User.prototype.checkEmail=function(email){
	// summary： 校验email地址是否正确。
	// return int:
	//           如果email符合规范，将返回EMAIL_CORRECT常量；
	//           email格式不正确，返回：
	//                            EMAIL_WRONG_HOST
	//                            EMAIL_WRONG_FORMAT
	
	var emailPat=/^(.+)@(.+)$/;
	var matchArray=email.match(emailPat);
	if (matchArray==null){
		return this.EMAIL_WRONG_FORMAT;
	}
	if (email.indexOf(" ")>-1){
		return this.EMAIL_WRONG_FORMAT;
	}
	// 增加对服务器的判断
	var host="smartdio.com";
	if (email.length<(host.length+2)){
		return this.EMAIL_WRONG_HOST;
	}
	var num1=email.length-host.length ;
	
	if(email.substr(num1,email.length)!=host){
		return this.EMAIL_WRONG_HOST;
	}
	
	return this.EMAIL_CORRECT;
}
// ...... 现实中，User 类还有更多方法和属性