package com.example.appqueology;

import java.security.acl.Owner;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.webkit.WebView;
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
				Artifact touchedArtifact = (Artifact) event.getLocalState();
				View owner = (View)touchedArtifact.getParent();
				if(v != owner){//if the drag and drop started at a SlideDrawer Artifact
					Log.v(""+owner.getWidth(),""+event.getX());
					if(v.getX()+event.getX() > owner.getWidth()){//Check if it has been dropped to main frame 
						Artifact square = new Artifact(v.getContext());//then create a new Artifact at the main frame where it was dropped
						square.setBackgroundColor(Color.BLACK);
				        RelativeLayout Rel = (RelativeLayout)v;
				        Rel.addView(square,100,100);
				        square.setPrevWidth(100);
				        square.setPrevHeight(100);
				        square.setX(event.getX()-50);
				        square.setY(event.getY()-50);
				        square.setTag("node");
				        square.setId(Global.ID);
				        Global.ID++;
				        recalculateLines(Rel);
				        touchedArtifact.setBackgroundColor(Color.BLACK);
				        
					}else{//else 
						touchedArtifact.setBackgroundColor(Color.BLACK);
					}

					View slideDrawer = (View)owner.getParent();//ensure the SlideDrawer overlaps all the views
					slideDrawer.bringToFront();
					
				}else{
					RelativeLayout Rel = (RelativeLayout)v;
					touchedArtifact.setX(event.getX()-touchedArtifact.getWidth()/2);
					touchedArtifact.setY(event.getY()-touchedArtifact.getHeight()/2);
					touchedArtifact.setBackgroundColor(Color.BLACK);
					recalculateLines(Rel);
					touchedArtifact.bringToFront();
					View slideDrawer = ((View) Rel.getParent()).findViewById(R.id.slidingDrawer);//ensure the SlideDrawer overlaps all the views
					slideDrawer.bringToFront();
					
				}
	
			  break;
			case DragEvent.ACTION_DRAG_ENDED:
			  default:
			  break;
		}
		return true;
			
	}
	
	public static void recalculateLines(RelativeLayout Rel){
		while(Rel.findViewWithTag("line") != null){
			Rel.removeView(Rel.findViewWithTag("line"));
		}
		ArrayList<Artifact> nodeList = new ArrayList<Artifact>();
		for(int i=0;i<Rel.getChildCount();i++){
			if(Rel.getChildAt(i).getTag() != null){
				if(Rel.getChildAt(i).getTag().toString().compareTo("node") == 0){
					nodeList.add((Artifact)Rel.getChildAt(i));
					nodeList.get(nodeList.size()-1).seekFather(Rel);
				}
			}
			
		}
		
		for(int j=0;j<nodeList.size();j++){
				Artifact node = nodeList.get(j);
				if(node.getFather() != null){
					Line line = new Line(Rel.getContext(),new PointF(node.getCenterX(),node.getCenterY()),new PointF(node.getFather().getCenterX(),node.getFather().getCenterY()));
					line.setTag("line");
					Rel.addView(line);
					node.bringToFront();
					node.getFather().bringToFront();
					View slideDrawer = ((View) Rel.getParent()).findViewById(R.id.slidingDrawer);//ensure the SlideDrawer overlaps all the views
					slideDrawer.bringToFront();
				}
			}
		
	}

}
