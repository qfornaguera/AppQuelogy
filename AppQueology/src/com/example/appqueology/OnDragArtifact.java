package com.example.appqueology;

import android.graphics.Color;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.widget.RelativeLayout;

public class OnDragArtifact implements OnDragListener{
	
	Artifact touchedArtifact;
	
	public OnDragArtifact(Artifact touchedArtifact) {
		this.touchedArtifact = touchedArtifact;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onDrag(View v, DragEvent event) {
		
		switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				touchedArtifact.setBackgroundColor(Color.RED);
			  break;
			case DragEvent.ACTION_DRAG_ENTERED:
			  break;
			case DragEvent.ACTION_DRAG_EXITED:        
			  break;
			case DragEvent.ACTION_DROP:
			  touchedArtifact.setX(event.getX());
			  touchedArtifact.setY(event.getY());
			  break;
			case DragEvent.ACTION_DRAG_ENDED:
			  default:
			  break;
		}
		return true;
			
	}

}
