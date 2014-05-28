package com.example.appqueology;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * 
 * @author Joaquim Fornaguera
 * 
 * This custom class extends view and in the board and draws a line point to point
 *
 */
public class Line extends View{
	
	Paint paint = new Paint();
	PointF p1 = new PointF();
	PointF p2 = new PointF();
	
	public Line(Context context,PointF p1,PointF p2) {
		super(context);
		this.p1=p1;
		this.p2=p2;
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(5);
		this.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						Log.v("touched","touched "+event.getX()+" "+event.getY());
					break;
				}
				return false;
			}
		});
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int x,x1,x2,y,y1,y2;
		int minX,maxX,minY,maxY;
		float Xmid,Ymid;
		double dist;
		x = (int) event.getX();
		y = (int) event.getY();
		x1 = (int) p1.x;
		y1 = (int) p1.y;
		x2 = (int) p2.x;
		y2 = (int) p2.y;
		
		Xmid = (p1.x + p2.x)/2;
		Ymid = (p1.y + p2.y)/2;
		
		dist = Math.sqrt(Math.pow(Xmid-event.getX(),2)+Math.pow(Ymid-event.getY(),2));
		
		minX = Math.min(x1, x2);
		maxX = Math.max(x1, x2);
		minY = Math.min(y1, y2);
		maxY = Math.max(y1, y2);
		
		try{
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				if((x-x1)/(x1-x2) == (y-y1)/(y1-y2)){
					if(x > minX && x < maxX && y > minY && y < maxY && dist <= 50){
						Log.v("True","True");
						return true;
					}else{
						return false;
					}
					
				}else{
					return false;
				}
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
    public void onDraw(Canvas canvas) {
            canvas.drawLine(p1.x,p1.y,p2.x,p2.y,paint);
    }

}
