// JavaScript Document
person = new Object();
person.name = "mike";
person.sex = "male";
person.age = 30;

person.walk = function() {
	person.state = "walking";
	person.speed = 1;
}


person.faster = function(){
	if(this.state == "walking"){
		this.speed = this.speed + 0.1;
	}
}

person.slower = function(){
	if(this.state == "walking"){
		this.speed = this.speed - 0.1;
	}
}

person.stopWalking = function(){
	this.state = "standing";
	this.speed=0;
}