function Person(name,sex,age){
	this.name = name ;
	this.sex = sex ;
	this.age = age ;
	
	//定义类的方法，把函数名赋值给方法名
	//函数体在后面书写，在类代码块之外
	this.walk = walk ;

	this.faster = faster ;

	this.slower = slower ;

	this.stopWalking = stopWalking;
}

//书写函数体
function walk() {
	this.state = "walking";
	this.speed = 1;
}


function faster(){
	if(this.state == "walking"){
		this.speed = this.speed + 0.1;
	}
}

function slower(){
	if(this.state == "walking"){
		this.speed = this.speed - 0.1;
	}
}

function stopWalking(){
	this.state = "standing";
	this.speed=0;
}