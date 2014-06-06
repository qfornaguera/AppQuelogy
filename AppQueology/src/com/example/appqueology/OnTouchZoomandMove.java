package com.example.appqueology;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

/**
 * 
 * @author Joaquim Fornaguera
 * 
 * this custom onTouchListener is set for main relativeLayout of MainActivity class.
 * Implements the move of the board and the pinch zoom in and out
 * 
 * we will set it at the main relativeLayout due to a accuracy problems when the graph board relativelayout was scaled down to 1.0
 *
 *
 */
public class OnTouchZoomandMove implements OnTouchListener {

	float lastX,lastX2,Xstart;
	float lastY,lastY2,Ystart;
	float lastDist;
	RelativeLayout Rel;//Rel is the graph board
	boolean zooming = false;
	
	public OnTouchZoomandMove(RelativeLayout Rel){
		this.Rel = Rel;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN://when one pointer is down(initialize the move of the board)
				Xstart = event.getX();
				Ystart = event.getY();
			break;
			
			case  MotionEvent.ACTION_MOVE://when the pointer(or pointers) move
				
				if(event.getPointerCount() == 1){//if just one pointer down move the board
					if(zooming){//if we were zooming we return true to finish the action, this avoids to sometimes the board to move after zoom
						return true;
					}
					float distX = Math.abs(event.getX()-Xstart);
					float distY = Math.abs(event.getY()-Ystart);
					if(Xstart > event.getX()){//we translate the board according where we moved the pointer from the start, for X and Y axis
						Rel.setX(Rel.getX()-distX);
					}else if(Xstart < event.getX() && Rel.getX()+distX < 0){
						Rel.setX(Rel.getX()+distX);
					}
					
					if(Ystart > event.getY()){
						Rel.setY(Rel.getY()-distY);
						
					}else if(Ystart < event.getY() && Rel.getY()+distY < 0){
						Rel.setY(Rel.getY()+distY);
					}
					Xstart = event.getX();
					Ystart = event.getY();
					
					
				}else if(event.getPointerCount() == 2){//if 2 pointers down zoom in or zoom out
					zooming = true;//when we zoom we set zooming to true
					Rel.setPivotX(-1*Rel.getX()+(event.getX(0)+event.getX(1))/2);//we reverse the offset applied by the coordinates of the graph board so that the coordinates watched on the window and the position of the graph board are reversed
			        Rel.setPivotY(-1*Rel.getY()+(event.getY(0)+event.getY(1))/2);
					float distX = Math.abs(event.getX(0)-event.getX(1));
					float distY = Math.abs(event.getY(0)-event.getY(1));
					float dist = (float)Math.sqrt(Math.pow(distX, 2)+Math.pow(distY, 2));
					if(lastDist < dist && Rel.getScaleY()+(float)0.01 <= 5){//if the distance between pointers decrease, zoom out
						Rel.setScaleX(Rel.getScaleX()+(float)0.01);
						Rel.setScaleY(Rel.getScaleY()+(float)0.01);
					}else if(lastDist > dist && Rel.getScaleY()-(float)0.01 >= 0.2){//if the distance bewteen increase, zoom in
						Rel.setScaleX(Rel.getScaleX()-(float)0.01);
						Rel.setScaleY(Rel.getScaleY()-(float)0.01);
					}
					lastDist = dist;
				}					
			break;
			
			case  MotionEvent.ACTION_UP:
				if(event.getPointerCount() == 1){
					zooming = false;//when all the pointers go up we set zooming to false
				}
			break;
		}
		return true;
	}
}


