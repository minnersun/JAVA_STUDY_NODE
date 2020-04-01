function createDocument() {
var aVersions = [ "MSXML2.DOMDocument.5.0",
"MSXML2.DOMDocument.4.0"," MSXML2.DOMDocument.3.0",
"MSXML2.DOMDocument"," Microsoft.XmlDom"
];
for (var i = 0; i < aVersions.length; i++) {
try {
var oXmlDom = new ActiveXObject(aVersions[i]);
return oXmlDom;
} catch (oError) {
//Do nothing
}
}
throw new Error("MSXML is not installed.");
}
