package com.hygc.mian;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

import com.andorid.shu.love.R;
import com.hygc.utils.AppManager;
import com.hygc.utils.ProgressDialogUtils;
import com.hygc.utils.WebServiceUtils;
import com.hygc.utils.WebServiceUtils.WebServiceCallBack;

public class VoteResultActivity extends BaseActivity implements CordovaInterface{  
    
    CordovaWebView cordovaWebView;  
      
    private final ExecutorService threadPool =Executors.newCachedThreadPool();  
    @SuppressLint("SetJavaScriptEnabled") 
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  

		AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.voteatatistics);      
        cordovaWebView=(CordovaWebView) findViewById(R.id.tutoriaView);  
        Config.init(this);
     
		
		LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
		properties.put("voteId", "0");
		
		WebServiceUtils.callWebService(
				"http://192.168.0.21:8888/hygc/ws/voteService", "getVoteResult",
				properties, new WebServiceCallBack() {
					// WebService接口返回的数据回调到这个方法中
					@Override
					public void callBack(Object result) {
						// 关闭进度条
						ProgressDialogUtils.dismissProgressDialog();
						if (result != null) {

							
							setData(formatJson(result));
							
							System.out.println("---------"+result);
							

						} else {
							Toast.makeText(VoteResultActivity.this, "请求失败,请重试",
									Toast.LENGTH_SHORT).show();

						}
					}

				});
		
		
				
			
        
				
				
        
        //cordovaWebView.loadUrl("file:///android_asset/www/index.html");  
    }  
  
    
    
    
    
    private void setData(String result)
    {
    	String str = "";
    	
    	try {
			    JSONObject obj = new JSONObject(result);
				str += " <!DOCTYPE html>";
				str += " <html>";
				str += " <head>";
				str += " <meta charset='UTF-8'>";
				str += " <title>ichartjs designer</title>";
				str += " <script type=\"text/javascript\" src=\"content://com.andorid.shu.love/img/ichart.js\"></script>";
				str += " <script type='text/javascript'>";
				str += " $(function(){ ";
				str += " var chart = iChart.create({ ";
				str += " render:\"ichart-render\", ";
				str += " width:1200, ";
				str += " height:700, ";
				str += " background_color:\"#fefefe\", ";
				str += " gradient:false, ";
				str += " color_factor:0.2, ";
				str += " border:{ ";
				str += " color:\"BCBCBC\", ";
				str += " width:0 ";
				str += " }, ";
				str += " align:\"center\", ";
				str += " offsetx:0, ";
				str += " offsety:0, ";
				str += " sub_option:{ ";
				str += "  border:{ ";
				str += " color:\"#BCBCBC\", ";
				str += " width:1 ";
				str += " }, ";
				str += " label:{ ";
				str += " fontweight:500, ";
				str += " fontsize:11, ";
				str += " color:\"#4572a7\", ";
				str += " sign:\"square\", ";
				str += " sign_size:12, ";
				str += " border:{ ";
				str += " color:\"#BCBCBC\", ";
				str += " width:1 ";
				str += " }, ";
				str += "  background_color:\"#fefefe\" ";
				str += " } ";
				str += " }, ";
				str += " shadow:true, ";
				str += " shadow_color:\"#666666\", ";
				str += " shadow_blur:2, ";
				str += " showpercent:false, ";
				str += " column_width:\"70%\", ";
				str += " bar_height:\"70%\", ";
				str += " radius:\"90%\", ";
				str += " title:{ ";
				str += " text:\"全国人大二次会议\", ";
				str += " color:\"#111111\", ";
				str += " fontsize:30, ";
				str += " font:\"微软雅黑\", ";
				str += " textAlign:\"center\", ";
				str += " height:30, ";
				str += " offsetx:0, ";
				str += " offsety:0 ";
				str += " }, ";
				str += " subtitle:{ ";
				str += " text:\"投票结果\", ";
				str += " color:\"#111111\", ";
				str += " fontsize:20, ";
				str += " font:\"微软雅黑\", ";
				str += " textAlign:\"center\", ";
				str += " height:20, ";
				str += " offsetx:0, ";
				str += " offsety:0 ";
				str += " }, ";
				str += " footnote:{ ";
				str += " text:\"\", ";
				str += " color:\"#111111\", ";
				str += " fontsize:12, ";
				str += " font:\"微软雅黑\", ";
				str += " textAlign:\"right\", ";
				str += " height:20, ";
				str += " offsetx:0, ";
				str += " offsety:0 ";
				str += " }, ";
				str += "  legend:{ ";
				str += " enable:false, ";
				str += "  background_color:\"#fefefe\", ";
				str += "  color:\"#333333\", ";
				str += " fontsize:12, ";
				str += "  border:{ ";
				str += " color:\"#BCBCBC\", ";
				str += " width:1 ";
				str += " }, ";
				str += " column:1, ";
				str += " align:\"right\", ";
				str += " valign:\"center\", ";
				str += " offsetx:0, ";
				str += " offsety:0 ";
				str += "  }, ";
				str += " coordinate:{ ";
				str += " width:\"80%\", ";
				str += " height:\"84%\", ";
				str += " background_color:\"#ffffff\", ";
				str += " axis:{ ";
				str += " color:\"#a5acb8\", ";
				str += " width:[1,\"\",1,\"\"] ";
				str += " }, ";
				str += " grid_color:\"#d9d9d9\", ";
				str += " label:{ ";
				str += " fontweight:500, ";
				str += " color:\"#666666\", ";
				str += " fontsize:11 ";
				str += " } ";
				str += " }, ";
				str += " label:{ ";
				str += " fontweight:500, ";
				str += " color:\"#666666\", ";
				str += " fontsize:11 ";
				str += " }, ";
				str += " type:\"pie2d\", ";
				str += " data:[ ";
				str += " { ";
				str += " name:\"赞成\", ";
				str += " value:"+obj.getString("approveCount")+",";
				str += " color:\"#007133\" ";
				str += " },{ ";
				str += " name:\"反对\", ";
				str += " value:"+obj.getString("opposeCount")+", ";
				str += " color:\"#6f060b\" ";
				str += " },{ ";
				str += " name:\"弃权\", ";
				str += "  value:"+obj.getString("waiverCount")+",";
				str += "         color:\"#ffff00\" ";
			    str += "    } ";
				str += "    ] ";
				str += "   }); ";
				str += " chart.draw(); ";
				str += " })";
				str += " </script> ";
				str += " </head> ";
				str += " <body style='background-color:#fefefe;'> ";
				str += " <div id='ichart-render'></div> ";
				str += " </body> ";
				str += " </html> ";
		
				
				cordovaWebView.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
               // System.out.println("-----str-"+str);
		
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	
    	
    }
    
    private String formatJson(Object result) {

		String str = result.toString().replace(
				"getVoteResultResponse{return=", "");
		str = str.substring(0, str.length() - 3);
		System.out.println("_______11________________>" + str);
		return str;

	}
  
    @Override  
    public Activity getActivity() {  
        return this;  
    }  
  
  
  
    @Override  
    public ExecutorService getThreadPool() {  
        return threadPool;  
    }  
  
    @Override  
    public Object onMessage(String string, Object object) {  
        return null;  
    }  
  
   
  
    @Override  
    public void startActivityForResult(CordovaPlugin plugin, Intent intent, int i) {  
          
    }


	@Override
	public void setActivityResultCallback(CordovaPlugin arg0) {
		// TODO Auto-generated method stub
		
	}






    
}