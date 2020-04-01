team = new Object();
team.name = "demo team";
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

team.leader.name = "leader man";

team.leader.run=function(){
	//这里的this 指的是team.leader对象
	this.state="running";
}