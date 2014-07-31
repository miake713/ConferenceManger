package com.hygc.mian;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andorid.shu.love.BookActivity;
import com.andorid.shu.love.R;
import com.hygc.mian.CheckState.WebServiceCallBack;
/**
 * 
 * @author WQ
 ** 2014-7-7下午4:31:46
 */


public class MyActivityTest extends ActivityGroup {
	private ProgressDialog myUpdatePd;
    private ArrayList<String> list= new ArrayList<String>();
	//private List<StudyWord> list;
	private DownLoadFileTask downLoadFileTask;
	private int i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		HashMap<String, String> properties = new HashMap<String, String>();
		setContentView(R.layout.mian2);
	
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		myUpdatePd = new ProgressDialog(this);
		myUpdatePd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		myUpdatePd.setMessage("正在下载...");
		myUpdatePd.setCancelable(false);		
		list.add(0, "http://wxq10086.com/lhrh.apk");
		list.add(1, "http://gdown.baidu.com/data/wisegame/b949dd9cad4278cf/ESFileExplorer_212.apk");
	/*	Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				System.out.println("______________>1");
				// TODO Auto-generated method stub
				i=0;
				myUpdatePd.show();
				down();
				System.out.println("______________>2");
				
			}
		});
	*/
		
		LocalActivityManager m = getLocalActivityManager();
		//Uri uri = Uri.parse("/storage/emulated/legacy/2.pdf");
		
		
		Intent intent = new Intent(MyActivityTest.this,BookActivity.class);
      	intent.setAction(Intent.ACTION_VIEW);
	    intent.putExtra("bookid", "23");
		Window w = m.startActivity("tratat",intent);
		View v = w.getDecorView();
		LinearLayout container = (LinearLayout) findViewById(R.id.test2);
		container.addView(v);
		
		
		
		System.out.println("______________>");
		properties.put("adt_begin", "2014-03-17T00:00:00");
		properties.put("adt_end", "2014-03-17T00:00:00");
		try {
			CheckState.callWebService(
					CheckState.WEB_SERVER_URL, "getStateLog",
					null, "return", new WebServiceCallBack() {
		
						@Override
						public void callBack(Object result) {
							// 关闭进度条
							if (result != null) {

								
								//System.out.println("_______________result_"+ result);
								
								
								/*if (!result.isVote) {

									if (result.isopen) {

										LocalActivityManager m = getLocalActivityManager();
										Uri uri = Uri.parse("/storage/emulated/legacy/2.pdf");
										Intent intent = new Intent(MyActivityTest.this,MuPDFActivity.class);
										intent.setAction(Intent.ACTION_VIEW);
										intent.setData(uri);
										Window w = m.startActivity("tratat",intent);
										View v = w.getDecorView();
										LinearLayout container = (LinearLayout) findViewById(R.id.test1);
										container.addView(v);
										
										System.out.println("_______________result_"+ result);

									} else {
										// System.out.println("_______________result_"+result.isopen);

									}
								} else {

									Intent intent = new Intent(
											MyActivityTest.this,
											voteActivity.class);
									// intent.setAction(Intent.ACTION_VIEW);
									startActivity(intent);

								}*/

							}

							else {

							}
						}
					});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void copyBigDataToSD(String strOutFileName) throws IOException {
		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(strOutFileName);
		myInput = this.getAssets().open("Android.pdf");
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}

		myOutput.flush();
		myInput.close();
		myOutput.close();
	}


	
	
	DownLoadFileThreadTask task;
	public void down() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (i == list.size()) {				
		     	myUpdatePd.dismiss();
         	}
		
			if (i < list.size()) {
				myUpdatePd.setMessage("正在下载"+i);
				task = new DownLoadFileThreadTask(list.get(i).toString(),"/storage/emulated/0/Android/data/com.example.down/"+i+".apk");
				//Log.i(TAG, "i=========0--" + i + list.get(i).toString());				
				new Thread(task).start();
		
			}
		}
		else 
		{
			//Toast.makeText(getApplicationContext(), "sd卡不可用", 1).show();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			down();
		};
	};
	
	private class DownLoadFileThreadTask implements Runnable {
		private String path; // 服务器路径
		private String filepath; // 本地文件路径

		public DownLoadFileThreadTask(String path, String filepath) {
			this.path = path;
			this.filepath = filepath;
		}
		@Override
		public void run() {
			try {
				File file = DownLoadFileTask.getInstance().getFile(path,
						filepath, myUpdatePd);
				if (i < list.size()) {
				//	Log.i(TAG, "i的值" + i);				
					i++;
					handler.sendEmptyMessage(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				// Log.i(TAG, "下载失败" + list.get(i).getPic_url());
				if (i < list.size()) {
					i++;
					handler.sendEmptyMessage(0);
					Toast.makeText(getApplicationContext(), "下载失败", 1).show();
					//Log.i(TAG, "" + i);
				}
			}

		}

	}

	private static class DownLoadFileTask {
		private DownLoadFileTask() {
		}

		private static DownLoadFileTask instance = null;

		public static DownLoadFileTask getInstance() {
			if (instance == null) {
				instance = new DownLoadFileTask();
			}
			return instance;
		}

	
	 /**
	 * 
	 * @param path
	 * @param filepath
	 * @param pd
	 * @return
	 * @throws Exception
	 */
		public File getFile(String path, String filepath, ProgressDialog pd)
				throws Exception {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			//pd.setMessage("正在下载"+filepath);
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				int fileSize = conn.getContentLength();
				pd.setMax(fileSize);
				File file = new File(filepath);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				int downloadSize=0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					downloadSize+=len;
					pd.setProgress(downloadSize);
				}
				fos.flush();
				fos.close();
				is.close();
				return file;
			}
			return null;
		}
	}
	
	
	

}
