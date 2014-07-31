package com.hygc.mian;

import java.util.LinkedHashMap;

import com.andorid.shu.love.R;
import com.hygc.utils.AppManager;
import com.hygc.utils.ProgressDialogUtils;
import com.hygc.utils.WebServiceUtils;
import com.hygc.utils.WebServiceUtils.WebServiceCallBack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 
 * @author WQ 2014-6-30����2:16:11
 */
public class VoteActivity extends BaseActivity implements OnClickListener {
	private TextView tv;
	private String voteId;
	private String android_id; 
	private String result="";
	private  Button btnagree;
	private  Button btnagainst;
	private  Button btnabstain;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
      
		AppManager.getAppManager().addActivity(this);
		setContentView(R.layout.vote);
	     btnagree=(Button)this.findViewById(R.id.btnagree);
	     btnagainst=(Button)this.findViewById(R.id.btnagainst);
	     btnabstain=(Button)this.findViewById(R.id.btnabstain);
		
		
		this.findViewById(R.id.btnagree).setOnClickListener(this);
		this.findViewById(R.id.btnagainst).setOnClickListener(this);
		this.findViewById(R.id.btnabstain).setOnClickListener(this);
		this.findViewById(R.id.btnsend).setOnClickListener(this);
		tv = (TextView) this.findViewById(R.id.votemsg);
		Intent intent = getIntent();
		voteId = intent.getStringExtra("voteId");
		android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);

	}

	@Override
	public void onClick(View v) {
		LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
		properties.put("voteId", "0");
		
		switch (v.getId()) {
		case R.id.btnagree:
			tv.setText("���Ѿ�ѡ���޳�,��ȷ�Ϻ��ύ");
			btnagree.setBackgroundResource(R.drawable.btn_vote_agree_press);
			btnagainst.setBackgroundResource(R.drawable.btn_vote_against);
			btnabstain.setBackgroundResource(R.drawable.btn_vote_abstain_item);
			result="1";		
			break;
		case R.id.btnagainst:
			tv.setText("���Ѿ�ѡ�񷴶�,��ȷ�Ϻ��ύ");
			btnagree.setBackgroundResource(R.drawable.btn_vote_agree);
			btnagainst.setBackgroundResource(R.drawable.btn_vote_against_press);
			btnabstain.setBackgroundResource(R.drawable.btn_vote_abstain_item);
			
			result="2";
			break;
		case R.id.btnabstain:     		
			tv.setText("���Ѿ�ѡ����Ȩ,��ȷ�Ϻ��ύ");
			btnagree.setBackgroundResource(R.drawable.btn_vote_agree);
			btnagainst.setBackgroundResource(R.drawable.btn_vote_against);
			btnabstain.setBackgroundResource(R.drawable.btn_vote_abstain_press);
			result="3";
			break;
		case R.id.btnsend:

			properties.put("result", result);
    		if(!result.equals(""))
			{
				properties.put("mac",android_id);
				
				WebServiceUtils.callWebService(
						
						WebServiceUtils.WEB_SERVER_URL+"voteService", "insertVoteResult",
						properties, new WebServiceCallBack() {
							// WebService�ӿڷ��ص����ݻص������������
							@Override
							public void callBack(Object result) {
								// �رս�����
								ProgressDialogUtils.dismissProgressDialog();
								if (result != null) {

									Toast.makeText(VoteActivity.this, "ͶƱ�ɹ�",
											Toast.LENGTH_SHORT).show();
									//finish();

								} else {
									Toast.makeText(VoteActivity.this, "����ʧ��,������",
											Toast.LENGTH_SHORT).show();

								}
							}

						});
				
				
			}
			else
			{
		      	Toast.makeText(VoteActivity.this, "��ѡ������ύ",
						Toast.LENGTH_SHORT).show();
		
			}
			
			break;

		}
		  
		

	}

}
