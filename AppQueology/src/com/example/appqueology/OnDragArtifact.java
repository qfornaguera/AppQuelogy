package com.example.appqueology;

import android.graphics.Color;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.widget.RelativeLayout;

public class OnDragArtifact implements OnDragListener{
	
	public OnDragArtifact() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onDrag(View v, DragEvent event) {
		
		switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
			  break;
			case DragEvent.ACTION_DRAG_ENTERED:
			  break;
			case DragEvent.ACTION_DRAG_EXITED:        
			  break;
			case DragEvent.ACTION_DROP:
				Global.touchedArtifact.setBackgroundColor(Color.BLACK);
				Global.touchedArtifact.setX(event.getX()-50);
				Global.touchedArtifact.setY(event.getY()-50);
			  break;
			case DragEvent.ACTION_DRAG_ENDED:
			  default:
			  break;
		}
		return true;
			
	}

}
