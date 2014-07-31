package com.hygc.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class Download {

	private ProgressDialog myUpdatePd;
    private ArrayList<String> list= new ArrayList<String>();
	private DownLoadFileTask downLoadFileTask= new DownLoadFileTask();
	private void init() {


		list.add(0, "http://wxq10086.com/lhrh.apk");
		list.add(1, "http://gdown.baidu.com/data/wisegame/b949dd9cad4278cf/ESFileExplorer_212.apk");
		i = 0;
		
		// myUpdatePd.setMax(list.size() * 3);
		//myUpdatePd.setMax(100);
		
	
	}

	ImageView iv;
	private int i;

	// 下载
	public void click(View v) {
		
		myUpdatePd.show();
		down();
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
		} else {
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
					//Log.i(TAG, "下载失败" + i);
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
