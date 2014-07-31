package com.hygc.mian;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;

import com.andorid.shu.love.R;
import com.android.FileBrowser.FileUtil;
import com.hygc.mian.ScrollWebView.OnScrollChangedCallback;
import com.hygc.utils.AppManager;
import com.hygc.widget.BadgeView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ContentActivity extends BaseActivity implements CordovaInterface {
	private Handler handler = null;
	ScrollWebView attachWebView;
	private int pageCount = 0;
	private int fontSize = 15;
	private int currentPage = 1;
	private TextView textPage;
	private int lastPage = 0;
	private Handler mHandler;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attach);
		AppManager.getAppManager().addActivity(this);
		attachWebView = (ScrollWebView) findViewById(R.id.attachWebView);
		Button btndown = (Button) findViewById(R.id.btndown);
		Button btnup = (Button) findViewById(R.id.btnup);
		Button btnGoto = (Button) findViewById(R.id.btnGoto);
		Button btnFontDown = (Button) findViewById(R.id.btnFontDown);
		Button btnFontUp = (Button) findViewById(R.id.btnFontUp);
		Button btnattach = (Button) findViewById(R.id.btnattach);
		BadgeView badge5 = new BadgeView(this, btnattach);
		badge5.setText("5");
		// badge5.setTextSize(10);
		badge5.toggle(true);
		attachWebView.getSettings().setJavaScriptEnabled(true);
		textPage = (TextView) this.findViewById(R.id.textPage);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					textPage.setText(currentPage+"/"+pageCount);
				}
				super.handleMessage(msg);
			}
		};
		new MyThread().start();
		handler = new Handler();
		attachWebView.getSettings().setLoadWithOverviewMode(true);
		attachWebView.addJavascriptInterface(new Object() {
			public void setPageCount(String count) {
				pageCount = Integer.parseInt(count);
				System.out.println("-------count---------" + count);
			}

		}, "getPageCount");

		attachWebView.addJavascriptInterface(new Object() {
			public void setCurrentPage(String count) {
				currentPage =Integer.parseInt(count);
			//System.out.println("-------getCurrentPage---------"+ currentPage);

			}

		}, "getCurrentPage");

		
		
		textPage.setText("1/"+pageCount);
		btndown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pagedown();

			}
		});

		btnFontDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (fontSize > 5) {
					fontSize = fontSize - 2;
					attachWebView.loadUrl("javascript:setfontSize(" + fontSize
							+ ")");
				}

			}
		});

		btnFontUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (fontSize <= 25) {
					fontSize = fontSize + 2;
					attachWebView.loadUrl("javascript:setfontSize(" + fontSize
							+ ")");
				}

			}
		});

		btnup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pageup();

			}
		});

		btnGoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				gotoPage();

			}
		});

		// ListView listView = (ListView) findViewById(R.id.listattach);
		Config.init(this);
	//	String str = com.android.FileBrowser.FileUtil
			//	.readSDFile("安全诚信集成ERP办公系统_用户功能及操作手册（21页）[1].html");
	//	System.out.println("----------------" + str);
	//	attachWebView
			//	.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
	
		attachWebView.loadUrl("file://mnt/sdcard/安全诚信集成ERP办公系统_用户功能及操作手册（21页）[1].html");

	}

	public void pagedown() {
		// 通过handler来确保init方法的执行在主线程中
		handler.post(new Runnable() {
			public void run() {
				// 调用客户端setContactInfo方法
				attachWebView.loadUrl("javascript:pagedown(800)");
			}
		});
	}

	public void pageup() {
		// 通过handler来确保init方法的执行在主线程中
		handler.post(new Runnable() {
			public void run() {
				// 调用客户端setContactInfo方法
				attachWebView.loadUrl("javascript:pagedown(20)");
			}
		});
	}

	private void settext() {

		System.out.println("-------text---------" + currentPage);
		textPage.setText(currentPage);
	}

	private void gotoPage() {

		LayoutInflater inflater = getLayoutInflater();
		final View layout = inflater.inflate(R.layout.bar,
				(ViewGroup) findViewById(R.id.seekbar));
		SeekBar seek = (SeekBar) layout.findViewById(R.id.seek);
		seek.setMax(pageCount - 1);

		final TextView textView = (TextView) layout
				.findViewById(R.id.textprogress);
		// txtProgress = pagefactory.getCurProgress();
		 seek.setProgress(currentPage);
		textView.setText("当前页数：第"+currentPage+"页");
		seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			int progressBar = 1;

			@SuppressLint("WrongCall")
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int progressBar = seekBar.getProgress();
				attachWebView.loadUrl("javascript:gotoPage(" + progressBar
						+ ")");

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Toast.makeText(mContext, "StartTouch",
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					textView.setText("当前页数：第" + (progress + 1) + "页");
				}
			}
		});
		new AlertDialog.Builder(this).setTitle("跳转").setView(layout)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Toast.makeText(mContext, "您选中的是",
						// Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				}).show();

	}

	public class MyThread extends Thread {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {

				Message msg = new Message();
     			msg.what = 1;
				
				  try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				mHandler.sendMessage(msg);
			
			}
		}
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
