// JavaScript Document
function createPerson(name,sex,age){
	// summary:  建立一个person对象
	// String name : person的名称
	// String sex : person的性别
	// String age : 年龄
	//
	// return: 返回一个person对象
	person = new Object();
	person.name = name;
	person.sex = sex;
	person.age = age;

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
	
	return person ;
}

function createTeam(name){
	// summary:  建立一个team对象
	// String name : team的名称
	//
	// return: 返回一个team对象
	team = new Object();
	team.name = name;
	team.members = new Array();
	team.leader = new Object();

	team.addMember = function(member){
		//这里的this指的是team对象
		this.members.push(member);
	}

	team.count = function(){
		//这里的this指的是team对象
		return this.members.length;
	}

    return team ;
}