package com.example.appqueology;

import java.io.Serializable;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.Timer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Parcelable;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;
import android.widget.RelativeLayout;

/**
 * 
 * @author Joaquim Fornaguera
 * 
 * this custom OnDragListener that implements the drag and drop of the artifact views. From the slidingDrawer to create a new node
 * and for the creation of a new artifact, from the board to move the artifact. 
 * 
 * Also detects the long click to modify an artifact properties 
 * 
 * Is set at the graph RelativeLayout of MainActivity class 
 *
 */
public class OnDragArtifact implements OnDragListener{
	float startX,startY;
	long startTime;
	public OnDragArtifact() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onDrag(View v, DragEvent event) {
		Artifact touchedArtifact = (Artifact) event.getLocalState();
		switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED://when the artifact launches its drag action
				startX = touchedArtifact.getX();
				startY = touchedArtifact.getY();
				startTime = System.currentTimeMillis();
			  break;
			case DragEvent.ACTION_DRAG_ENTERED:
			  break;
			  
			case DragEvent.ACTION_DRAG_EXITED:
			  break;
			case DragEvent.ACTION_DROP://when the artifact is droped somewhere
				View owner = (View)touchedArtifact.getParent();
				if(v != owner){//if the drag and drop started at a SlideDrawer Artifact
					if(v.getX()+event.getX() > owner.getWidth()){//Check if it has been dropped on the board
						Artifact square = new Artifact(v.getContext());//then create a new Artifact at the main frame where it was dropped
						square.setBackgroundResource(R.drawable.anfora);
				        RelativeLayout Rel = (RelativeLayout)v;
				        Rel.addView(square,100,100);
				        square.setPrevWidth(100);
				        square.setPrevHeight(100);
				        square.setX(event.getX()-50);
				        square.setY(event.getY()-50);
				        square.setTag("node");
				        square.setId(Global.ID);
				        Global.ID++;
				        square.seekFather(Rel);
				        Utility.recalculateLines(Rel);
				        touchedArtifact.setBackgroundResource(R.drawable.anfora);
				        
					}else{//else 
						touchedArtifact.setBackgroundResource(R.drawable.anfora);
					}

					View slideDrawer = (View)owner.getParent();//ensure the SlideDrawer overlaps all the views
					slideDrawer.bringToFront();
					
				}else{//else means we want to move or long click the artifact
					RelativeLayout Rel = (RelativeLayout)v;
					touchedArtifact.setBackgroundResource(R.drawable.anfora);
					if(System.currentTimeMillis()-startTime > 500 && Math.abs(startX-event.getX()) < 100 && Math.abs(startY-event.getY()) < 100){//onlongClick artifacts event
						Intent toArtifactActivity = new Intent(Rel.getContext(), ArtifactActivity.class);
						toArtifactActivity.putExtra("id",touchedArtifact.getId());
						toArtifactActivity.putExtra("text",touchedArtifact.getText());
						Activity a = (Activity)Rel.getContext();
						a.startActivityForResult(toArtifactActivity, 0);
					}else{//else we want to move the artifact
						touchedArtifact.setX(event.getX()-touchedArtifact.getWidth()/2);
						touchedArtifact.setY(event.getY()-touchedArtifact.getHeight()/2);
						touchedArtifact.seekFather(Rel);
						Utility.recalculateLines(Rel);
						touchedArtifact.bringToFront();
						View slideDrawer = ((View) Rel.getParent()).findViewById(R.id.slidingDrawer);//ensure the SlideDrawer overlaps all the views
						slideDrawer.bringToFront();
					}
				}
	
			  break;
			case DragEvent.ACTION_DRAG_ENDED:
			  default:
			  break;
			  
			  
		}
		return true;
			
	}


}
