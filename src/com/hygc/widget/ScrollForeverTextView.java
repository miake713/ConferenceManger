package com.hygc.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
* 单行文本跑马灯控件
* @author admin
*
*/

public class ScrollForeverTextView extends TextView {

public ScrollForeverTextView(Context context) {
super(context);
// TODO Auto-generated constructor stub
}

public ScrollForeverTextView(Context context, AttributeSet attrs) {
super(context, attrs);
}

public ScrollForeverTextView(Context context, AttributeSet attrs,int defStyle) {
super(context, attrs, defStyle);
}

@Override
public boolean isFocused() {
return true;
}

}