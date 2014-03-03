package com.example.appqueology;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnTouchArtifact implements OnTouchListener{
	
	Artifact touchedArtifact;
	
	public OnTouchArtifact(Artifact touchedArtifact){
		this.touchedArtifact = touchedArtifact;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			v.setBackgroundColor(Color.CYAN);
			touchedArtifact = (Artifact) v;
		}
		
		
		return false;
	}
	
}
