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

public class OnDragArtifact implements OnDragListener{
	float startX,startY;
	long startTime;
	static float nodeCounter,posX;
	public OnDragArtifact() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onDrag(View v, DragEvent event) {
		Artifact touchedArtifact = (Artifact) event.getLocalState();
		switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				startX = touchedArtifact.getX();
				startY = touchedArtifact.getY();
				startTime = System.currentTimeMillis();
			  break;
			case DragEvent.ACTION_DRAG_ENTERED:
			  break;
			  
			case DragEvent.ACTION_DRAG_EXITED:
			  break;
			case DragEvent.ACTION_DROP:
				View owner = (View)touchedArtifact.getParent();
				if(v != owner){//if the drag and drop started at a SlideDrawer Artifact
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
				        square.seekFather(Rel);
				        recalculateLines(Rel);
				        touchedArtifact.setBackgroundColor(Color.BLACK);
				        
					}else{//else 
						touchedArtifact.setBackgroundColor(Color.BLACK);
					}

					View slideDrawer = (View)owner.getParent();//ensure the SlideDrawer overlaps all the views
					slideDrawer.bringToFront();
					
				}else{
					RelativeLayout Rel = (RelativeLayout)v;
					touchedArtifact.setBackgroundColor(Color.BLACK);
					if(System.currentTimeMillis()-startTime > 500 && Math.abs(startX-event.getX()) < 100 && Math.abs(startY-event.getY()) < 100){//onlongClick artifacts event
						Intent toArtifactActivity = new Intent(Rel.getContext(), ArtifactActivity.class);
						toArtifactActivity.putExtra("id",touchedArtifact.getId());
						toArtifactActivity.putExtra("text",touchedArtifact.getText());
						Activity a = (Activity)Rel.getContext();
						a.startActivityForResult(toArtifactActivity, 0);
					}else{
						touchedArtifact.setX(event.getX()-touchedArtifact.getWidth()/2);
						touchedArtifact.setY(event.getY()-touchedArtifact.getHeight()/2);
						touchedArtifact.seekFather(Rel);
						recalculateLines(Rel);
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
	
	public static void recalculateLines(RelativeLayout Rel){
		
		while(Rel.findViewWithTag("line") != null){
			Rel.removeView(Rel.findViewWithTag("line"));
		}
		
		ArrayList<Artifact> nodeList = new ArrayList<Artifact>();
		for(int i=0;i<Rel.getChildCount();i++){
			if(Rel.getChildAt(i).getTag() != null){
				if(Rel.getChildAt(i).getTag().toString().compareTo("node") == 0){
					nodeList.add((Artifact)Rel.getChildAt(i));
					//nodeList.get(nodeList.size()-1).seekFather(Rel);
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
	
	public static void beautifygraph(RelativeLayout Rel){
		nodeCounter = 0;
		ArrayList<Artifact> nodeList = new ArrayList<Artifact>();
		for(int i=0;i<Rel.getChildCount();i++){
			if(Rel.getChildAt(i).getTag() != null){
				if(Rel.getChildAt(i).getTag().toString().compareTo("node") == 0){
					Artifact node = (Artifact)Rel.getChildAt(i);
					if(node.getFather() == null)
						nodeList.add(node);
				}
			}	
		}
		posX = 0;
		for(int j=0;j<nodeList.size();j++){
			Artifact node = nodeList.get(j);
			setNodePosition(node,j,1);
			posX = posX + 1000;
		}
		recalculateLines(Rel);
		
	}
	
	public static void setNodePosition(Artifact node,int treeNum,int level){		
		ArrayList<Artifact> sons = node.getSons();
		for(int i=0;i<sons.size();i++){
			setNodePosition(sons.get(i),treeNum,level+1);
			if((sons.size()-1)/2 == i){
				if(sons.size()%2 != 0)
					node.setX(posX);
				else{
					posX = posX + 200;
					node.setX(posX);
				}
			}
		}
		if(sons.size() == 0){
			posX = posX + 200;
			node.setX(posX);
		}
		node.setY(level*300);

	}

}
