package com.example.appqueology;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class OnTouchZoomandMove implements OnTouchListener {

	float lastX,lastX2,Xstart;
	float lastY,lastY2,Ystart;
	float lastDist;
	RelativeLayout Rel;
	
	public OnTouchZoomandMove(RelativeLayout Rel){
		this.Rel = Rel;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Global.touchedArtifact = null;
		
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				Xstart = event.getX();
				Ystart = event.getY();
			break;
			
			case MotionEvent.ACTION_POINTER_DOWN:
				RelativeLayout graph = (RelativeLayout) Rel.findViewById(R.id.graph);
				lastX = event.getX(0);
				lastY = event.getY(0);
				lastX2 = event.getX(1);
				lastY2 = event.getY(1);
				Rel.setPivotY(-1*graph.getX()+((event.getX(0)+event.getX(1))/2));
		        Rel.setPivotX(-1*graph.getY()+((event.getY(0)+event.getY(1))/2));
				lastDist = (float)Math.sqrt(Math.pow(lastX-lastX2, 2)+Math.pow(lastY-lastY2, 2));
			break;
			
			case  MotionEvent.ACTION_MOVE:
				
				if(event.getPointerCount() == 1){
					float distX = Math.abs(event.getX()-Xstart);
					float distY = Math.abs(event.getY()-Ystart);
					if(Xstart > event.getX(0)){
						Rel.setX(Rel.getX()-distX);
					}else if(Xstart < event.getX(0)){
						Rel.setX(Rel.getX()+distX);
					}
					
					if(Ystart > event.getY(0)){
						Rel.setY(Rel.getY()-distY);
						
					}else if(Ystart < event.getY(0)){
						Rel.setY(Rel.getY()+distY);
					}
					Xstart = event.getX();
					Ystart = event.getY();
					
				}else if(event.getPointerCount() == 2){
					float distX = Math.abs(event.getX(0)-event.getX(1));
					float distY = Math.abs(event.getY(0)-event.getY(1));
					float dist = (float)Math.sqrt(Math.pow(distX, 2)+Math.pow(distY, 2));
					if(lastDist < dist && Rel.getScaleY()+(float)0.01 <= 5){
						Rel.setScaleX(Rel.getScaleX()+(float)0.01);
						Rel.setScaleY(Rel.getScaleY()+(float)0.01);
					}else if(lastDist > dist && Rel.getScaleY()-(float)0.01 >= 0.2){
						Rel.setScaleX(Rel.getScaleX()-(float)0.01);
						Rel.setScaleY(Rel.getScaleY()-(float)0.01);
					}
					lastDist = dist;

				}					
			break;
			
			case  MotionEvent.ACTION_UP:
			break;
		}
		return true;
	}
}


