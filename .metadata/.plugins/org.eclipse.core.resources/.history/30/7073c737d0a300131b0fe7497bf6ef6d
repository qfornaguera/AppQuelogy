package com.example.appqueology;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;



public class Artifact extends View{
	
	Artifact touchedArtifact;
	
	public Artifact(Context context,Artifact touchedArtifact) {
		super(context);
		this.touchedArtifact = touchedArtifact;
		// TODO Auto-generated constructor stub
		this.setOnTouchListener(new OnTouchArtifact(touchedArtifact));
	}

}
