package com.hygc.mian;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andorid.shu.love.BookActivity;
import com.andorid.shu.love.BookPageFactory;
import com.andorid.shu.love.R;

import com.hygc.mian.CheckState.hygl;
import com.hygc.utils.AppConfig;
import com.hygc.utils.AppManager;
import com.hygc.utils.ProgressDialogUtils;
import com.hygc.utils.WebServiceUtils;
import com.hygc.utils.WebServiceUtils.WebServiceCallBack;
import com.sqlite.DbHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ConferenceActivity extends BaseActivity {
	private ListView myListView = null;
	private ProgressDialog myUpdatePd;
	private ArrayList<issueList> arrayListData = new ArrayList<issueList>();
	private ArrayList<downfile> downfilelist = new ArrayList<downfile>();
	private DownLoadFileTask downLoadFileTask;
	public ConferenceBaseAdapter myBaseAdapter = null;
	private int i;
	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
	private static final String ACTION_DISABLE_RECENT_BUTTON = "action_disable_recent_button";
	public static String IP_Port = "";
	// private static final String ACTION_ENABLE_RECENT_BUTTON =
	// "action_enable_recent_button";
	private ConferenceView myView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		AppManager.getAppManager().addActivity(this);
		setContentView(R.layout.activity_main);
		myListView = (ListView) findViewById(R.id.myListView);

		TextView tv = (TextView) findViewById(R.id.titeFirst);
		TextView titesecond = (TextView) findViewById(R.id.titesecond);
		String title = "湖州市第三届人大二次会议";
		/*
		 * //创建一个 SpannableString对象 SpannableString msp = new
		 * SpannableString(title);
		 * 
		 * //设置字体(default,default-bold,monospace,serif,sans-serif)
		 * msp.setSpan(new TypefaceSpan("微软雅黑"), 0, 2,
		 * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); msp.setSpan(new
		 * TypefaceSpan("serif"), 2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		 * //设置字体样式正常，粗体，斜体，粗斜体
		 * 
		 * msp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
		 * title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //粗体
		 * tv.setText(msp);
		 */

		tv.setTypeface(Typeface.createFromAsset(getAssets(),
				"fonts/FZCCHJW.TTF"));
		Shader shader = new LinearGradient(0, 0, 0, 50, 0xfffffdc9, 0xffffd91c,
				TileMode.CLAMP);
		tv.getPaint().setShader(shader);
		tv.setText("湖州市第三届人大二次会议");
		titesecond.getPaint().setShader(shader);
		titesecond.setTypeface(Typeface.createFromAsset(getAssets(),
				"fonts/FZCCHJW.TTF"));

		IP_Port = AppConfig.getAppConfig(this).get("ip") + ":"
				+ AppConfig.getAppConfig(this).get("port");
		
		
		ProgressDialogUtils.showProgressDialog(this, "数据加载中...");
		LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();

		WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL
				+ "stateLogService", "getStateLogAll", properties,
				new WebServiceCallBack() {
					// WebService接口返回的数据回调到这个方法中
					@Override
					public void callBack(Object result) {
						// 关闭进度条
						ProgressDialogUtils.dismissProgressDialog();
						if (result != null) {
							setData(formatJson(result));
							// System.out.println("_______________________>"+result);

						} else {
							Toast.makeText(ConferenceActivity.this,
									"获取WebService数据错误", Toast.LENGTH_SHORT)
									.show();
						}
					}

				});

		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (ConferenceView.bFlag) {
					ConferenceView.bFlag = false;
					((BaseAdapter) myListView.getAdapter())
							.notifyDataSetChanged();
				} else {
					if (BookActivity.mCurPageBitmap != null) {
						BookActivity.mCurPageBitmap.recycle();
					}
					if (BookActivity.mNextPageBitmap != null) {
						BookActivity.mNextPageBitmap.recycle();
					}

					if (BookPageFactory.m_book_bg != null) {
						BookPageFactory.m_book_bg.recycle();
					}

					Intent intent = new Intent();
					intent.setClass(ConferenceActivity.this, BookActivity.class);
					System.out.println("_________interId______________>"
							+ downfilelist.get(position).interId);

					intent.putExtra("bookid",
							downfilelist.get(position).interId);

					startActivity(intent);

					/*
					 *  * Intent intent = new Intent( ConferenceActivity.this,
					 * MyActivityTest.class); //
					 * intent.setAction(Intent.ACTION_VIEW);
					 * startActivity(intent);
					 */

					// Toast.makeText(ConferenceActivity.this, "菜单项"+position,
					// Toast.LENGTH_SHORT).show();
				}
			}
		});

		try {
			CheckState.callWebService(CheckState.WEB_SERVER_URL, "getStateLog",
					null, "return", new CheckState.WebServiceCallBack() {

						@Override
						public void callBack(Object result) {
							// 关闭进度条
							if (result != null) {

								hygl hygl = (hygl) result;

								// System.out.println("_______________result_"+
								// hygl.issueId);

								if (!hygl.isVote) {

									if (hygl.isOpen) {

										if (BookPageFactory.m_book_bg != null) {
											BookPageFactory.m_book_bg.recycle();
										}

										Intent intent = new Intent();
										intent.setClass(
												ConferenceActivity.this,
												BookActivity.class);
										// System.out.println("_________interId______________>"+downfilelist.get(position).interId);
										intent.putExtra("bookid", hygl.issueId);
										startActivity(intent);

										/*
										 *  * LocalActivityManager m =
										 * getLocalActivityManager(); Uri uri =
										 * Uri
										 * .parse("/storage/emulated/legacy/2.pdf"
										 * ); Intent intent = new
										 * Intent(MyActivityTest
										 * .this,MuPDFActivity.class);
										 * intent.setAction(Intent.ACTION_VIEW);
										 * intent.setData(uri); Window w =
										 * m.startActivity("tratat",intent);
										 * View v = w.getDecorView();
										 * LinearLayout container =
										 * (LinearLayout)
										 * findViewById(R.id.test1);
										 * container.addView(v) ;
										 */

										// System.out.println("_______________result_"+
										// result);

									} else {
										// System.out.println("_______________result_"+result.isopen);

									}
								} else {

									Intent intent = new Intent(
											ConferenceActivity.this,
											VoteActivity.class);
									// intent.setAction(Intent.ACTION_VIEW);
									startActivity(intent);

								}

							}

							else {

							}
						}
					});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * myListView.setOnItemLongClickListener(new OnItemLongClickListener() {
		 * 
		 * @Override public boolean onItemLongClick(AdapterView<?> parent, View
		 * view, int position, long id) { // TODO Auto-generated method stub
		 * View itemView = (View) myListView.getItemAtPosition(position); myView
		 * = new ConferenceView(ConferenceActivity.this, itemView, position);
		 * ((BaseAdapter)myListView.getAdapter()).notifyDataSetChanged(); return
		 * true; } });
		 */
	}

	private String formatJson(Object result) {

		String str = result.toString().replace(
				"getStateLogAllResponse{return=", "");
		str = str.substring(0, str.length() - 3);
		System.out.println("_______11________________>" + str);
		return str;

	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = new Intent(ACTION_DISABLE_RECENT_BUTTON);
		sendBroadcast(intent);
	}

	private void setData(String result) {

		JSONObject obj;
		String kssj = "";
		try {

			obj = new JSONObject(result);
			JSONArray issueList = obj.getJSONArray("issueList");
			for (int i = 0; i < issueList.length(); i++) {
				JSONObject jsonObject = (JSONObject) issueList.opt(i);
				issueList issueLi = new issueList();
				issueLi.compereCompany = jsonObject.getString("compereCompany");
				issueLi.compereDuty = jsonObject.getString("compereDuty");
				issueLi.compereName = jsonObject.getString("compereName");
				issueLi.endDate = jsonObject.getString("endDate").substring(11,
						16);
				issueLi.issueName = jsonObject.getString("issueName");
				issueLi.issueNo = jsonObject.getString("issueNo");
				issueLi.issueTypeName = jsonObject.getString("issueTypeName");
				issueLi.spokesmanCompany = jsonObject
						.getString("spokesmanCompany");
				issueLi.spokesmanDuty = jsonObject.getString("spokesmanDuty");
				issueLi.spokesmanName = jsonObject.getString("spokesmanName");
				issueLi.issueid = jsonObject.getString("interId");
				issueLi.conferenceId = jsonObject.getString("conferenceId");
				issueLi.startDate = jsonObject.getString("startDate")
						.substring(11, 16);
				arrayListData.add(issueLi);

				downfile d = new downfile();
				d.filename = jsonObject.getString("fileName");
				d.fileUrl = jsonObject.getString("fileUrl");
				d.interId = jsonObject.getString("interId");
				downfilelist.add(d);

			}

			myBaseAdapter = new ConferenceBaseAdapter(ConferenceActivity.this,
					arrayListData);
			myBaseAdapter.myBaseAdapter = myBaseAdapter;
			myListView.setAdapter(myBaseAdapter);

			System.out.println("------------> "
					+ obj.getJSONObject("aaConferenceForm")
							.getString("logoUrl"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {// 创建菜单
		super.onCreateOptionsMenu(menu);
		// 通过MenuInflater将XML 实例化为 Menu Object
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {// 操作菜单
		int ID = item.getItemId();
		switch (ID) {
		/*
		 * case R.id.exitto: addBookMark(); //dialog.cancel(); finish();
		 * //creatIsExit(); break;
		 */
		case R.id.download:

			i = 0;
			myUpdatePd = new ProgressDialog(this);
			myUpdatePd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			myUpdatePd.setMessage("正在下载...");
			myUpdatePd.setCancelable(false);
			myUpdatePd.show();
			down();

			break;
		case R.id.nowprogress:

			break;
		default:
			break;

		}
		return true;
	}

	DownLoadFileThreadTask task;

	public void down() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			if (i == downfilelist.size()) {
				myUpdatePd.dismiss();
			}

			if (i < downfilelist.size()) {

				Log.i("", "i=========0--" + downfilelist.get(i).filename
						+ "===" + downfilelist.get(i).interId);

				myUpdatePd.setMessage("正在下载" + downfilelist.get(i).filename);
				task = new DownLoadFileThreadTask("http://" +ConferenceActivity.IP_Port
						+ "/hygc" + downfilelist.get(i).fileUrl,
						"/storage/emulated/0/lovereader/"
								+ downfilelist.get(i).filename);
				new Thread(task).start();
				DbHelper db = new DbHelper(this);
				db.insert(downfilelist.get(i).filename, "0",
						downfilelist.get(i).interId);
				db.close();

				// System.out.println("----db--------> "+db.getFileName(downfilelist.get(i).interId));

			}
		} else {
			// Toast.makeText(getApplicationContext(), "sd卡不可用", 1).show();
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
				if (i < downfilelist.size()) {
					// Log.i(TAG, "i的值" + i);
					i++;
					handler.sendEmptyMessage(0);

				}
			} catch (Exception e) {
				e.printStackTrace();
				// Log.i(TAG, "下载失败" + list.get(i).getPic_url());
				if (i < downfilelist.size()) {
					i++;
					handler.sendEmptyMessage(0);
					Toast.makeText(getApplicationContext(), "下载失败", 1).show();
					// Log.i(TAG, "" + i);
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
			// pd.setMessage("正在下载"+filepath);
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				int fileSize = conn.getContentLength();
				pd.setMax(fileSize);
				File file = new File(filepath);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				int downloadSize = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					downloadSize += len;
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

	public class issueList {
		public String issueid;
		public String conferenceId;
		public String issueName;
		public String issueNo;
		public String issueTypeName;
		public String spokesmanName;
		public String spokesmanDuty;
		public String spokesmanCompany;
		public String compereCompany;
		public String compereName;
		public String compereDuty;
		public String startDate;
		public String endDate;
		public String state;

	}

	public class downfile {
		public String interId;
		public String filename;
		public String fileUrl;

	}

}
