package com.example.appqueology;

import java.io.Serializable;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
		RelativeLayout Main = (RelativeLayout)v.getParent();
		ViewGroup sladingDrawer = (ViewGroup)Main.findViewById(R.id.slidingDrawer);
		View owner = (View)touchedArtifact.getParent();
		switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED://when the artifact launches its drag action
				startX = touchedArtifact.getX();
				startY = touchedArtifact.getY();
				startTime = System.currentTimeMillis();
				if(v == owner){//if the drag event is started by an artifact on the graph board we disable the slading drawer drag listener so it don't interfere in our actions with the artifact
					sladingDrawer.setEnabled(false);
				}
			  break;
			case DragEvent.ACTION_DRAG_ENTERED:
			  break;
			  
			case DragEvent.ACTION_DRAG_EXITED:
			  break;
			case DragEvent.ACTION_DROP://when the artifact is droped somewhere
				if(v != owner){//if the drag and drop started at a SlideDrawer Artifact
					Log.v("if: "+(v.getX()+event.getX()), "board: "+v.getX()+" event: "+event.getX());
					Log.v("if: "+(v.getX()+(event.getX()/v.getScaleX())), "board: "+v.getX()+" event: "+(event.getX()/v.getScaleX()));
					if(Global.exited){//Check if it has been dropped on the board(aca exited from the slading drawer)
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
				        Global.exited = false;
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
						if(touchedArtifact.getFather() == null)
							toArtifactActivity.putExtra("father", "Nothing");
						else
							toArtifactActivity.putExtra("father", touchedArtifact.getFather().getText());
						ArrayList <String> sonsText = new ArrayList<String>();
						for(int i=0;i<touchedArtifact.getSons().size();i++){
							sonsText.add((String) touchedArtifact.getSons().get(i).getText());
						}
						toArtifactActivity.putStringArrayListExtra("sons", sonsText);
						Activity a = (Activity)Rel.getContext();
						a.startActivityForResult(toArtifactActivity, 0);
					}else{//else we want to move the artifact
						touchedArtifact.setX(event.getX()-touchedArtifact.getWidth()/2);
						touchedArtifact.setY(event.getY()-touchedArtifact.getHeight()/2);
						if(touchedArtifact.seekFather(Rel)){//if the father found is compatible 
							Utility.recalculateLines(Rel);
							touchedArtifact.bringToFront();
							View slideDrawer = ((View) Rel.getParent()).findViewById(R.id.slidingDrawer);//ensure the SlideDrawer overlaps all the views
							slideDrawer.bringToFront();
						}else{
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(touchedArtifact.getContext());
							alertDialog.setTitle("Illegal Move");
							alertDialog.setMessage("You cannot put a node  below an another node already related to it at one of his branches");
							alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
								// here you can add functions
								}
							});
							alertDialog.show();
							touchedArtifact.setX(startX);
							touchedArtifact.setY(startY);
						}
					}
				}
	
			  break;
			case DragEvent.ACTION_DRAG_ENDED:
				if(v == owner){//once we finished the drag event done in the graph board we enable again the drag listener on slading drawer viewgrouo
					sladingDrawer.setEnabled(true);
				}
			break;
				
			default:
			break;
			  
			  
		}
		return true;
			
	}


}
