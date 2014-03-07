package com.example.appqueology;

import java.security.acl.Owner;

import android.graphics.Color;
import android.graphics.Point;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.webkit.WebView.FindListener;
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
				View touchedArtifact = (View) event.getLocalState();
				View owner = (View)touchedArtifact.getParent();
				if(v != owner){//if the drag and drop started at a SlideDrawer Artifact
					if(event.getX() > owner.getWidth()){//Check if it has been dropped to main frame
						Artifact square = new Artifact(v.getContext());//then create a new Artifact at the main frame where it was dropped
						square.setX(event.getX());
				        square.setY(event.getY());
				        square.setBackgroundColor(Color.BLACK);
				        RelativeLayout Rel = (RelativeLayout)v;
				        Rel.addView(square,100,100);
				        touchedArtifact.setBackgroundColor(Color.BLACK);
					}else{//else 
						touchedArtifact.setBackgroundColor(Color.BLACK);
					}

					View slideDrawer = (View)owner.getParent();//ensure the SlideDrawer overlaps all the views
					slideDrawer.bringToFront();
					
				}else{
					touchedArtifact.setX(event.getX()-50);
					touchedArtifact.setY(event.getY()-50);
					touchedArtifact.setBackgroundColor(Color.BLACK);
					
					
				}
	
			  break;
			case DragEvent.ACTION_DRAG_ENDED:
			  default:
			  break;
		}
		return true;
			
	}

}
