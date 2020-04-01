function AutoSaver( formid,time){
  this.formid = formid;
  this.form=document.getElementById(formid);
  this.time = time;
  this.autosave_url;
  this.autosave_id="";
  this.autosave_formId = formid;
  this.target="_autoSave";
  this.targetUrl = "./hid_saved.html";
  
  this.__tmpTarget ;
  this.__tmpAction ;
  
  this.create=function(){
     
     document.write(this.__iframe());//write iframe;     
    
  }  
  
  this.postAutoSave=function(){//post auto saved data
      this.__tmpTarget=this.form.target;
      this.form.target=this.target;
      this.__tmpAction=this.form.action;
      this.form.action=this.targetUrl;
      this.form.submit();
      this.form.target=this.__tmpTarget;
      this.form.action=this.__tmpAction;
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
  this.__iframe=function (){
     var fileUrl = this.targetUrl;
  	
	var divHtml = "<div style=\"position:absolute;left:0px;top:0px;width:200px;height:64px;z-index:1;visibility:hidden\">";
	divHtml=divHtml+"<iframe  id=\"_autoSave\" name=\"_autoSave\" src=\""+fileUrl+"\" width=\"200\""+" height=\"64\" scrolling=\"yes\"></iframe>\n";
	divHtml=divHtml+"</div>";
  	return  divHtml;
  }
  
  
  
  this.showSaveComplete=function(){
     alert("Auto Saved Complete");
  }
	
}
var __secCounter = null;
var __auto_save_minue = 1;
var autoSaver ;
//time : minue
function startCount(){
	 autoSaver.postAutoSave();
	 __secCounter = setTimeout("startCount()", __auto_save_minue*60*1000);
}

function startCountOnly(){
 __secCounter = setTimeout("startCount()", __auto_save_minue*60*1000);
}