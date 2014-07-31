package com.hygc.mian;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.andorid.shu.love.R;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * POST����WebService�Ĺ�����,
 *
 * @author WQ
 * 
 */
public class CheckState {
	public static final String WEB_SERVER_URL = "http://"+ConferenceActivity.IP_Port+"/hygc/ws/stateLogService";
	// ����3���̵߳��̳߳�
	private static final ExecutorService executorService = Executors
			.newFixedThreadPool(3);
	// �����ռ�
	private static final String NAMESPACE = "http://webservice.giant.shenlan.com/";
	public static String laststr="";
	public static String voteState="";
	private static String Xmlnode="";

	/**
	 * 
	 * @param url
	 *            WebService��������ַ
	 * @param methodName
	 *            WebService�ĵ��÷�����
	 * @param properties
	 *            WebService�Ĳ���
	 * @param webServiceCallBack
	 *            �ص��ӿ�
	 * @throws IOException 
	 */
	public static void callWebService(String url, final String methodName,
			final HashMap<String, String> properties, final String xmlnode,	
			final WebServiceCallBack webServiceCallBack) throws IOException {
	
		// ��������webservice
	  
		Xmlnode=xmlnode;
	    final Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				
			
				super.handleMessage(msg);
				// ������ֵ�ص���callBack�Ĳ�����
				webServiceCallBack.callBack(msg.obj);
			
			}

		};
		
		

		// �����߳�ȥ����WebService
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				String result = null;
				hygl hygl;
				try {
	
				while(true)
				 {
					 
				   StringBuilder sb= new StringBuilder();								
					URL posturl = new URL(WEB_SERVER_URL);
				    URLConnection  conn = posturl.openConnection();			
					conn.setDoOutput(true);
					conn.setRequestProperty("Pragma:", "no-cache");
					conn.setRequestProperty("Cache-Control", "no-cache");
					conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");	
					sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
					.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">")
					.append("<soap:Body>")
					.append("<"+methodName+" xmlns=\""+NAMESPACE+"\">");
		
					if (properties != null) {
						for (Iterator<Map.Entry<String, String>> it = properties.entrySet()
								.iterator(); it.hasNext();) {
							Map.Entry<String, String> entry = it.next();
						//	System.out.println("--------------------->"+entry.getKey()+"---"+entry.getValue());			
							sb.append("<"+entry.getKey()+">"+entry.getValue()+"</"+entry.getKey()+">");			
						}
					}
					 sb.append("</"+methodName+">").append("</soap:Body>")
					 .append("</soap:Envelope>");					
					   // System.out.println("-------->>>>>>"+sb.toString());
					    OutputStreamWriter ots = new OutputStreamWriter(conn.getOutputStream());
						ots.write(new String(sb.toString().getBytes("utf-8")));
						ots.flush();
						ots.close();			
						BufferedReader br = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
						StringBuilder sBuilder = new StringBuilder();
						String line = "";
						for (line = br.readLine(); line != null; line = br.readLine()) {
						sBuilder.append(line+"\r\n");					
					}
								
					if (sBuilder.toString() != null) {	
            			result=ReadXmlJson(xmlnode,sBuilder.toString());
						//result=sBuilder.toString();
				        //System.out.println("1111_______"+result.toString());
					}
			
					JSONObject obj = new JSONObject(result);
					hygl=new hygl();
					if(!laststr.equals(obj.getString("issueId"))&&obj.getString("state").equals("1"))
					{	
						laststr=obj.getString("issueId");
						hygl.issueId=obj.getString("issueId");	  		        
						hygl.state=obj.getString("state");	
						hygl.isOpen=true;
						
					}
					
					else
					{
						hygl.issueId=obj.getString("issueId");	  		        
						hygl.state=obj.getString("state");	
						hygl.isOpen=false;
						
						if(obj.getString("state").equals("2"))
						{
							laststr="";
							
						}
						
					}
					
					if(!voteState.equals(obj.getString("state"))&&obj.getString("state").equals("4"))
					{						
						
						hygl.isVote=true;			
						voteState=obj.getString("state");
						
					}				 
				   
					
					else
					{
						if(obj.getString("state").equals("5"))
						{						
							voteState="";							
						}
						
						Log.d("", "--------state--------"+obj.getString("state")+"---------: ");	
						hygl.isVote=false;
						
					}
					
					
					
					mHandler.sendMessage(mHandler.obtainMessage(0,
				    		 hygl));
					
					 Log.d("", "----------------212---------: ");
					 Thread.sleep(2000); 
					
					
			 }
			
				
				}
									
				 catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				finally {
					
					mHandler.sendMessage(mHandler.obtainMessage(0,
							result));
				}
			}
		});
	}
	/**
	 * 
	 * 
	 * @author wq
	 * 
	 */
	
	public interface WebServiceCallBack {
		public void callBack(Object obj);
	}
	
	
	private static String ReadXmlJson(String tag,String xml)
	{
		
		String Json="";
		   try {
	            //���幤�� XmlPullParserFactory
	            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

	            //��������� XmlPullParser
	            XmlPullParser parser = factory.newPullParser();

	            //��ȡxml��������
	            parser.setInput(new StringReader(xml));
                 
	            try {
	                //��ʼ�����¼�
	                int eventType = parser.getEventType();

	                //�����¼����������ĵ�������һֱ����
	                while (eventType != XmlPullParser.END_DOCUMENT) {
	                    //��Ϊ������һ�Ѿ�̬�������������������switch
	                    switch (eventType) {
	                        case XmlPullParser.START_DOCUMENT:
	                            break;
	                        case XmlPullParser.START_TAG:
	                            //����ǰ��ǩ�������
	                            String tagName = parser.getName();
	                             // Log.d("", "====XmlPullParser.START_TAG=== tagName: " + tagName);
	                            if(tagName.equals(Xmlnode)){	                           
	                            	Json =parser.nextText();	
	                             //	Log.d("", "-------------------------: " + Json);                            	
	                            }
	                            break;

	                        case XmlPullParser.END_TAG:
	                            break;
	                        case XmlPullParser.END_DOCUMENT:
	                            break;
	                    }

	                    //��������next����������һ���¼������˵Ľ���ͳ���ѭ��#_#
	                    eventType = parser.next();
	                }
	            } catch (XmlPullParserException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	            
	            
	            
	        } catch (XmlPullParserException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		
		return Json;
	}
	
	
	
	public static class  hygl {
		
		public String issueId;
		public String  state; 	//4ͶƱ;5ֹͣͶƱ
		public Boolean isVote;
		public Boolean isOpen;

	}
	

}
