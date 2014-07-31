package com.hygc.mian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.andorid.shu.love.R;
public class ConferenceView
{
	private View oriView = null;
	private View latView = null;
	private ImageButton myImageButton = null;
	private TextView longClickTextView = null;
	public static int iPosition = -1;
	public static boolean bFlag = false;
	
	public ConferenceView(Context context,View oriView,int position)
	{
		this.oriView = oriView;
		this.latView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.list_item_2, null);
		this.myImageButton = (ImageButton) latView.findViewById(R.id.myImageButton);
		this.longClickTextView = (TextView) latView.findViewById(R.id.longClickTextView);
		ConferenceView.iPosition = position;
		bFlag = true;
	}
}