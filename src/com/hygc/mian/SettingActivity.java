package com.hygc.mian;



import com.andorid.shu.love.R;
import com.hygc.utils.AppConfig;
import com.hygc.utils.AppManager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends BaseActivity {
	TextView title;
	TextView right_text;
	EditText  editTextIp;
	EditText  editTextPort;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		setContentView(R.layout.settings);
		editTextIp=(EditText) this.findViewById(R.id.edittext_ip);
		editTextPort=(EditText) this.findViewById(R.id.edittext_port);
		System.out.println("-----name-------"+AppConfig.getAppConfig(this).get("ip"));
		
		
		initView();
		initData();
	}
	
	
	private void initView() {
		title = (TextView)findViewById(R.id.title);
		right_text = (TextView)findViewById(R.id.right_text);
		right_text.setVisibility(View.VISIBLE);
		right_text.setClickable(true);
		right_text.setOnClickListener(new View.OnClickListener() {		
			public void onClick(View v) {	
				AppConfig.getAppConfig(v.getContext()).set("ip",editTextIp.getText().toString());
				AppConfig.getAppConfig(v.getContext()).set("port",editTextPort.getText().toString());
				Toast.makeText(SettingActivity.this, "保存成功",
						Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void initData() {
		
		editTextIp.setText(AppConfig.getAppConfig(this).get("ip"));
		editTextPort.setText(AppConfig.getAppConfig(this).get("port"));	
		title.setText("设置");
		right_text.setText("保存");
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}
}
