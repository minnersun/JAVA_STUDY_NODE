function Person(name,sex,age){
	this.name = name ;
	this.sex = sex ;
	this.age = age ;
	
	this.walk = function() {
		this.state = "walking";
		this.speed = 1;
	}

	this.faster = function(){
		if(this.state == "walking"){
			this.speed = this.speed + 0.1;
		}
	}

	this.slower = function(){
		if(this.state == "walking"){
			this.speed = this.speed - 0.1;
		}
	}

	this.stopWalking = function(){
		this.state = "standing";
		this.speed=0;
	}
}