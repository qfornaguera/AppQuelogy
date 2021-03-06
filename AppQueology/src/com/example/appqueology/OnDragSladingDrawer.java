package com.example.appqueology;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.RelativeLayout;

/**
 * 
 * @author Joaquim Fornaguera
 * 
 * This custom OnDragListener is set to the slading drawer to check if one of it's tools is dragged
 * on the graph board
 *
 */

public class OnDragSladingDrawer implements OnDragListener{
	@Override
	public boolean onDrag(View v, DragEvent event) {
		Artifact touchedArtifact = (Artifact) event.getLocalState();
		View owner = (View) touchedArtifact.getParent();
		switch (event.getAction()) {
		
			case DragEvent.ACTION_DRAG_ENTERED://when a tool is dragged in the slading drawer
				if(owner == v){
					Global.exited = false;
				}
			  break;
			  
			case DragEvent.ACTION_DRAG_EXITED://when a tool is dragged out the slading drawer
				if(owner == v){
					Global.exited = true;
				}
			  break;
			
			
			default:
			break;
		}
		return true;
	}

}
