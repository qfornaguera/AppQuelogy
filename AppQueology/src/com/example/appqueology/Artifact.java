package com.example.appqueology;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;



public class Artifact extends View{
	
	
	public Artifact(Context context) {
		super(context);
		Global.touchedArtifact = this;
		// TODO Auto-generated constructor stub
		this.setOnTouchListener(new OnTouchArtifact());
	}

}
