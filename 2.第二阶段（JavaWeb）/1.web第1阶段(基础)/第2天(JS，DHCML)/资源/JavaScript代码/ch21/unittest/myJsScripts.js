// JavaScript Document
function multiplyAndAddFive(value1, value2){
	// summary:  计算value1*value2+5的结果。
	//           两个参数都必须是number类型，
	//           如果参数不符合要求，返回null。
	// return:   返回结果，类型是Number
	
	//如果value1和value2都不是Number类型，直接返回null
	if(typeof value1 !="number") return null ;
	if(typeof value2 !="number") return null ;
	
	var result = value1*value2+5 ;
	return result ;
}