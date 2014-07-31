package com.hygc.mian;

import java.util.LinkedHashMap;
import java.util.List;
import com.andorid.shu.love.R;
import com.hygc.mian.ConferenceActivity.issueList;
import com.hygc.utils.ProgressDialogUtils;
import com.hygc.utils.WebServicePostUtilsTest;
import com.hygc.utils.WebServiceUtils;
import com.hygc.utils.WebServiceUtils.WebServiceCallBack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConferenceBaseAdapter extends BaseAdapter {
	public Context context = null;
	@SuppressWarnings("rawtypes")
	public List list = null;
	public Holder holder = null;
	private LayoutInflater layoutInflater = null;
	public ConferenceBaseAdapter myBaseAdapter = null;

	@SuppressWarnings("rawtypes")
	public ConferenceBaseAdapter(Context context, List list) {
		this.context = context;
		this.list = list;
		this.layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@SuppressWarnings("rawtypes")
	private List myList = null;

	// ˽���࣬��Ӧitem����button�ĵ���¼���
	private class lvButtonOnClickListener implements OnClickListener {

		public int iPosition = -1;

		public lvButtonOnClickListener(int position) {
			this.iPosition = position;
		}

		@Override
		public void onClick(View v) {

			String msg = "";
			String statemsg = "";
			issueList listd = (issueList) list.get(iPosition);
			LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
			properties.put("issueId", listd.issueid);
			// properties.put("conferenceId", listd.conferenceId);

			switch (v.getId()) {
			case R.id.btnvoteend: //
				properties.put("state", "5"); // ͶƱ����
				WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL
						+ "voteService", "endVote", properties,
						new WebServiceCallBack() {
							// WebService�ӿڷ��ص����ݻص������������
							@Override
							public void callBack(Object result) { // �رս�����
								ProgressDialogUtils.dismissProgressDialog();
								if (result != null) {

									int res = Integer.parseInt(formatJson(
											result, "endVote"));
									if (res > 0) {
										Toast.makeText(context, "ͶƱ����",
												Toast.LENGTH_SHORT).show();
										issueList listd = (issueList) list
												.get(iPosition);
										listd.state = "ͶƱ����";
										myBaseAdapter.notifyDataSetChanged();

									} else if (res == 0) {

										Toast.makeText(context, "��ͶƱ���ܽ���",
												Toast.LENGTH_SHORT).show();

									} else {
										Toast.makeText(context, "���ݿ��쳣,����ϵ����Ա",
												Toast.LENGTH_SHORT).show();

									}

								} else {

									Toast.makeText(context, "����ʧ��,������",
											Toast.LENGTH_SHORT).show();

								}
							}

						});

				break;
			case R.id.btnvotestart:

				LinkedHashMap<String, String> properties1 = new LinkedHashMap<String, String>();
				properties1.put("issueId", listd.issueid);
				WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL
						+ "voteService", "startVote", properties1,
						new WebServiceCallBack() { // WebService�ӿڷ��ص����ݻص������������
							@Override
							public void callBack(Object result) { // �رս�����
								ProgressDialogUtils.dismissProgressDialog();
								if (result != null) {

									int res = Integer.parseInt(formatJson(
											result, "startVote"));
									if (res > 0) {
										Toast.makeText(context, "��ʼͶƱ",
												Toast.LENGTH_SHORT).show();
										issueList listd = (issueList) list
												.get(iPosition);
										listd.state = "ͶƱ������";
										myBaseAdapter.notifyDataSetChanged();

									} else if (res == 0) {

										Toast.makeText(context, "������ͶƱ���ܿ�ʼ",
												Toast.LENGTH_SHORT).show();

									} else {
										Toast.makeText(context, "���ݿ��쳣,����ϵ����Ա",
												Toast.LENGTH_SHORT).show();

									}

								} else {

									Toast.makeText(context, "����ʧ��,������",
											Toast.LENGTH_SHORT).show();

								}
							}

						});

				// ͶƱ��ʼ 
				
				break; 
				case R.id.btnytend:

				// properties.put("state", "2");
				WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL
						+ "conferenceIssueService", "endIssue", properties,
						new WebServiceCallBack() { // WebService�ӿڷ��ص����ݻص������������

							@Override
							public void callBack(Object result) { // �رս�����
								ProgressDialogUtils.dismissProgressDialog();
								if (result != null) {
									int res = Integer.parseInt(formatJson(
											result, "endIssue"));
									if (res > 0) {
										Toast.makeText(context, "�������",
												Toast.LENGTH_SHORT).show();
										issueList listd = (issueList) list
												.get(iPosition);
										listd.state = "�������";
										myBaseAdapter.notifyDataSetChanged();

									} else if (res == 0) {

										Toast.makeText(context, "�����ⲻ�ܽ���",
												Toast.LENGTH_SHORT).show();

									} else {
										Toast.makeText(context, "���ݿ��쳣,����ϵ����Ա",
												Toast.LENGTH_SHORT).show();

									}

								} else {

									Toast.makeText(context, "����ʧ��,������",
											Toast.LENGTH_SHORT).show();

								}
							}

						});

				break;
			case R.id.btnytstart:
				// properties.put("state", "1");
				WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL
						+ "conferenceIssueService", "startIssue", properties,
						new WebServiceCallBack() { // WebService�ӿڷ��ص����ݻص������������

							@Override
							public void callBack(Object result) { // �رս�����
								ProgressDialogUtils.dismissProgressDialog();
								if (result != null) {

									int res = Integer.parseInt(formatJson(
											result, "startIssue"));
									if (res > 0) {
										Toast.makeText(context, "���⿪ʼ",
												Toast.LENGTH_SHORT).show();
										issueList listd = (issueList) list
												.get(iPosition);
										listd.state = "���������";
										myBaseAdapter.notifyDataSetChanged();

									} else if (res == 0) {

										Toast.makeText(context, "�����ⲻ�ܿ�ʼ",
												Toast.LENGTH_SHORT).show();

									} else {
										Toast.makeText(context, "���ݿ��쳣,����ϵ����Ա",
												Toast.LENGTH_SHORT).show();

									}

								} else {

									Toast.makeText(context, "����ʧ��,������",
											Toast.LENGTH_SHORT).show();

								}
							}

						});
				break;
			}

		}

	}

	private class lvButtonOnClickListener1 implements OnClickListener {
		public int iPosition = -1;

		public lvButtonOnClickListener1(int position) {
			this.iPosition = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Toast.makeText(context, list.get(iPosition).toString(),
					Toast.LENGTH_SHORT).show();
			AlertDialog.Builder builder = new AlertDialog.Builder(context)
					.setTitle("�Ի���").setIcon(R.drawable.bg_icon_n)
					.setMessage("�������1111111111111" + iPosition + "��");
			builder.create().show();
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		if (ConferenceView.bFlag && ConferenceView.iPosition == position) {
			/*
			 * convertView = layoutInflater.inflate(R.layout.list_item_2,null);
			 * ImageButton myImageButton = (ImageButton)convertView
			 * .findViewById(R.id.myImageButton); TextView longClickTextView =
			 * (TextView)convertView .findViewById(R.id.longClickTextView);
			 * longClickTextView.setText("����֮�����ͼ");
			 */
			view = layoutInflater.inflate(R.layout.list_item_2, null);
			ImageButton myImageButton = (ImageButton) view
					.findViewById(R.id.myImageButton);
			TextView longClickTextView = (TextView) view
					.findViewById(R.id.longClickTextView);
			longClickTextView.setText("����֮�����ͼ");
			myImageButton.setOnClickListener(new lvButtonOnClickListener(
					position));
		} else {
			/*
			 * if(convertView != null) { holder = (Holder)convertView.getTag();
			 * } else{ convertView = layoutInflater.inflate(R.layout.list_item,
			 * null); holder = new Holder(); holder.myTextView = (TextView)
			 * convertView.findViewById(R.id.myTextView); holder.myButton =
			 * (Button) convertView.findViewById(R.id.myButton);
			 * convertView.setTag(holder); } if(holder!=null) {
			 * holder.myTextView.setText(list.get(position).toString());
			 * holder.myButton.setOnClickListener(new
			 * lvButtonOnClickListener(position)); }
			 */

			view = layoutInflater.inflate(R.layout.conference_item, null);
			issueList listd = (issueList) list.get(position);
			TextView myTextView = (TextView) view.findViewById(R.id.issueName);
			// myTextView.setTypeface(Typeface.createFromAsset(this.context.getAssets(),"fonts/FZCCHJW.TTF"));
			myTextView.setText(listd.issueName);

			TextView issueNo = (TextView) view.findViewById(R.id.issueNo);
			issueNo.setText(listd.issueNo);
			// issueNo.setTypeface(Typeface.createFromAsset(this.context.getAssets(),"fonts/FZCCHJW.TTF"));
			TextView compereCompany = (TextView) view
					.findViewById(R.id.compereCompany);
			compereCompany.setText("�����ˣ�" + listd.compereName);
			// compereCompany.setTypeface(Typeface.createFromAsset(this.context.getAssets(),"fonts/FZCCHJW.TTF"));
			TextView spokesmanName = (TextView) view
					.findViewById(R.id.spokesmanName);
			spokesmanName.setText("�����ˣ�" + listd.spokesmanName);
			// spokesmanName.setTypeface(Typeface.createFromAsset(this.context.getAssets(),"fonts/FZCCHJW.TTF"));
			TextView issueTypeName = (TextView) view
					.findViewById(R.id.issueTypeName);
			issueTypeName.setText(listd.issueTypeName);
			// issueTypeName.setTypeface(Typeface.createFromAsset(this.context.getAssets(),"fonts/FZCCHJW.TTF"));

			TextView startDate = (TextView) view.findViewById(R.id.startDate);
			startDate.setText(listd.startDate + "-" + listd.endDate);
			
			// startDate.setTypeface(Typeface.createFromAsset(this.context.getAssets(),"fonts/FZCCHJW.TTF"));

			if (position == 0) {
				view.setBackgroundResource(R.drawable.shape_top);

			}

			// view.setBackgroundResource(R.drawable.shape_bottom);

			TextView zt = (TextView) view.findViewById(R.id.zt);
			zt.setText(listd.state);

			Button btnytstart = (Button) view.findViewById(R.id.btnytstart);
			btnytstart.setOnClickListener(new lvButtonOnClickListener(position));
			
			Button btnytend = (Button) view.findViewById(R.id.btnytend);
			btnytend.setOnClickListener(new lvButtonOnClickListener(position));
			
			
			Button btnvotestart = (Button) view.findViewById(R.id.btnvotestart);
			btnvotestart.setOnClickListener(new lvButtonOnClickListener(position));
			Button btnvoteend = (Button) view.findViewById(R.id.btnvoteend);
			btnvoteend.setOnClickListener(new lvButtonOnClickListener(position));
		

		}
		return view;
		// return convertView;
	}

	private class Holder {
		public TextView myTextView = null;
		public Button myButton = null;
	}

	private String formatJson(Object result, String methodname) {

		String str = result.toString().replace(methodname + "Response{return=",
				"");
		str = str.substring(0, str.length() - 3);
		System.out.println("_______11________________>" + str);
		return str;

	}
}