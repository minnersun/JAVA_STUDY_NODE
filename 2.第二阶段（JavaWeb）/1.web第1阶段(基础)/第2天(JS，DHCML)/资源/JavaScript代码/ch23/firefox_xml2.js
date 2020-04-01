var isIE =  !! document.all;

function parseXML(st){
   if (isIE){
     var result =  new ActiveXObject("microsoft.XMLDOM");
    result.loadXML(st);
  } else {
     var parser =  new DOMParser();
     var result = parser.parseFromString(st,"text/xml");
  }
   return result;
}

if (!isIE){
   var ex;
  XMLDocument.prototype.__proto__.__defineGetter__("xml", function (){
     try {
       return  new XMLSerializer().serializeToString( this );
    } catch (ex){
       var d = document.createElement("div");
      d.appendChild( this .cloneNode( true ));
       return d.innerHTML;
    }
  });
  Element.prototype.__proto__.__defineGetter__("xml", function (){
     try {
       return  new XMLSerializer().serializeToString( this );
    } catch (ex){
       var d = document.createElement("div");
      d.appendChild( this .cloneNode( true ));
       return d.innerHTML;
    }
  });
  XMLDocument.prototype.__proto__.__defineGetter__("text", function (){
     return  this .firstChild.textContent
  });
  Element.prototype.__proto__.__defineGetter__("text", function (){
     return  this .textContent
  });

  XMLDocument.prototype.selectSingleNode = Element.prototype.selectSingleNode = function (xpath){
     var x = this .selectNodes(xpath)
     if ( ! x || x.length < 1 ) return  null ;
     return x[ 0 ];
  }
  XMLDocument.prototype.selectNodes = Element.prototype.selectNodes = function (xpath){
     var xpe =  new XPathEvaluator();
     var nsResolver = xpe.createNSResolver( this .ownerDocument ==  null  ?
       this .documentElement : this .ownerDocument.documentElement);
     var result = xpe.evaluate(xpath, this , nsResolver, 0 , null );
     var found = [];
     var res;
     while (res = result.iterateNext())
      found.push(res);
     return found;
  }
}