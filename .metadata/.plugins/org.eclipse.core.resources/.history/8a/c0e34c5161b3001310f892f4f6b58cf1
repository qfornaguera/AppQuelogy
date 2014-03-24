package com.example.appqueology;

import android.content.ClipData;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;

public class OnTouchArtifact implements OnTouchListener{
	
	
	public OnTouchArtifact(){

	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			v.setBackgroundColor(Color.CYAN);
			ClipData data = ClipData.newPlainText("", "");
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
			v.startDrag(data, shadowBuilder, v, 0);
			
		}
		
		
		
		return true;
	}
	
}
