http_request = null ;
//建立AJAX引擎的函数
function createAjax(){
	if (window.XMLHttpRequest) { // Mozilla, Safari,
	    http_request = new XMLHttpRequest();
	    if(http_request.overrideMimeType){
		     http_request.overrideMimeType('text/xml');
	    }
      } else if (window.ActiveXObject) { // IE
	    try {
		     http_request = new ActiveXObject("Msxml2.XMLHTTP");
	    } catch (e) {
		    try {
			     http_request = new ActiveXObject("Microsoft.XMLHTTP");
		    } catch (e) {
				alert("exception");
			}
	    }
      }
}

//封装了自动保存功能的类
function AutoSaver( formid,time){
  this.formid = formid;
  this.form=document.getElementById(formid);
  this.time = time;
  this.targetUrl = "./hid_saved2.html";  
 
  
  this.postAutoSave=function(){//post auto saved data
    createAjax();
	http_request.onreadystatechange = this.onReceived;
	http_request.open("GET", this.targetUrl, true);
	//需要自己建立发送的数据
	var title = this.__getField("title");
	var content = this.__getField("content");
	var sendData = "title="+title+"&content="+content ;
	//发送表单的数据
	http_request.send(sendData);

  }

 this.__getField= function(fieldName) {  
    for (var e = 0; e < this.form.elements.length; e++){
      if (this.form.elements[e].name == fieldName)
        return this.form.elements[e];
        if (this.form.elements[e].id == fieldName)
        return this.form.elements[e];
     }
  return null;
  }  
    
  this.onReceived=function(){
     if (http_request.readyState == 4) { 
		// 一切就绪，服务器响应
		// 并且数据已接收完成
		if (http_request.status == 200 || http_request.status == 0) { 
			// 成功获得数据！
			var requestContent = http_request.responseText ;
			alert(requestContent);
		}else if(http_request.status == 404 || http_request.status == 2){
		    alert("404(Not Found)，未找到请求的文件");
		}else { 
			alert("请求数据过程出现错误!("+http_request.status+")");
		}
	} else {
       //尚未就绪
	}

  }
  
  
	
}
var __secCounter = null;
var __auto_save_minue = 1;
var autoSaver ;
//time : minue
function startCount(start){
	if(!start)autoSaver.postAutoSave();
 __secCounter = setTimeout("startCount(false)", __auto_save_minue*60*1000);
}