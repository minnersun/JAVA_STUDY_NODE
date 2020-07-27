package xdclass_websocket.controller.v5;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import xdclass_websocket.utils.HttpUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 功能描述：接口服务，调用股票行情接口
 *
 * <p> 创建时间：Jan 6, 2018 </p> 
 * <p> 贡献者：小D学院, 官网：www.xdclass.net </p>
 *
 * @author <a href="mailto:xd@xdclass.net">小D老师</a>
 * @since 0.0.1
 */
public class StockService {

	public static Map<String, String > getStockInfo(){
		 String host = "https://stock.api51.cn";
		    String path = "/stock/";
		    String method = "GET";
		    String appcode = "748ffa05c4c54cf391ebe231b3b2bbab";
		    Map<String, String> headers = new HashMap<String, String>();
		    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		    headers.put("Authorization", "APPCODE " + appcode);
		    Map<String, String> querys = new HashMap<String, String>();
		    querys.put("en_prod_code", "000006.SZ");

	    try {
	    	/**
	    	* 重要提示如下:
	    	* HttpUtils请从
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* 下载
	    	*
	    	* 相应的依赖请参照
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*/
	    	HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);

	    	
	    	JSONObject obj = JSONObject.parseObject(EntityUtils.toString( response.getEntity()));
	    	
	    	if (obj.containsKey("data")) {
	    		JSONObject snapshotObj =  obj.getJSONObject("data");
	    		
	    		if(snapshotObj.containsKey("snapshot")) {
	    			JSONArray szArray = snapshotObj.getJSONObject("snapshot").getJSONArray("000006.SZ");
	    			JSONArray fields = snapshotObj.getJSONObject("snapshot").getJSONArray("fields");
	    			Map<String, String> mapInfo = new HashMap<>();
	    			
	    			for(int i=0; i<szArray.size(); i++){
	    				mapInfo.put(fields.getString(i), szArray.getString(i));
	    			}
	    			System.out.println(mapInfo);
	    			return mapInfo;
	    		}
	    		
	    	}
	    	

	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		
		
		return null;
		
	}
	
	
	
	
}
