package com.hygc.mian;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;

import com.andorid.shu.love.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AttachActivity extends BaseActivity implements CordovaInterface {

	CordovaWebView attachWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attach);
		attachWebView = (CordovaWebView) findViewById(R.id.attachWebView);
		//ListView listView = (ListView) findViewById(R.id.listattach);
		Config.init(this);

		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 30; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ItemTitle", "This is Title" + (i + 1));
			map.put("ItemText", "This is text" + (i + 1));
			mylist.add(map);
		}
		// 生成适配器，数组===》ListItem
	
		SimpleAdapter adapter = new SimpleAdapter(this, mylist,
				R.layout.file_item, new String[] { "ItemTitle", "ItemText" },
				new int[] { R.id.file_name, R.id.file_icon });
		//listView.setAdapter(adapter);	
		//listView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int postion,
//					long arg3) {
//				
//
//				//attachWebView.loadDataWithBaseURL("http://www.baidu.com", "", "text/html", "utf-8", null);
//				
//				attachWebView.loadUrl("http://h.hiphotos.baidu.com/image/pic/item/91529822720e0cf3a8adc4f60846f21fbe09aa61.jpg");
//				
//				
//				
//				
//				
//			}	
//			
//		});
//		
		
		

	}

	@Override
	public Activity getActivity() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public ExecutorService getThreadPool() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object onMessage(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setActivityResultCallback(CordovaPlugin arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startActivityForResult(CordovaPlugin arg0, Intent arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
