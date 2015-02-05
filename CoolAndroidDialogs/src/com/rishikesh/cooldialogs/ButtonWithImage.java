package com.rishikesh.cooldialogs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ButtonWithImage extends LinearLayout {

	private TextView label;
	private ImageView thumbnail;

	public ButtonWithImage(Context context) {
		super(context);

	}

	public ButtonWithImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ButtonWithImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.button_with_image, this);
		
		setBackgroundColor(getResources().getColor(R.color.titlecolor));
        //Add missing top level attributes    
      
		this.label = (TextView) findViewById(R.id.label);
		this.thumbnail = (ImageView) findViewById(R.id.thumbnail);
	}

}
