package com.example.appqueology;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 
 * 
 * @author Joaquim Fornaguera
 * 
 * This custom class extends view and in the board and draws a line point to point
 *
 */
public class Line extends View{
	
	private Paint paint = new Paint();
	private PointF p1 = new PointF();
	private PointF p2 = new PointF();
	private Artifact father,son;
	long startTime;
	
	public Line(Context context,PointF p1,PointF p2,Artifact father,Artifact son) {
		super(context);
		this.p1=p1;
		this.p2=p2;
		this.father = father;
		this.son = son;
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(5);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		
		if(!this.isEnabled())
			return false;
		
		float x,x1,x2,y,y1,y2;
		float Xmid,Ymid;
		double dist;
		RelativeLayout Rel= (RelativeLayout)this.getParent();
		x = event.getX();
		y = event.getY();
		x1 = p1.x;
		y1 = p1.y;
		x2 = p2.x;
		y2 = p2.y;
		
		Xmid = (x1 + x2)/2;
		Ymid = (y1 + y2)/2;
		
		dist = Math.sqrt(Math.pow(Xmid-x,2)+Math.pow(Ymid-y,2));
		
		
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				if(dist <= 50){//if the touch is less or equal than 50 of distance of the middle point of the line we catch the event 
					startTime = System.currentTimeMillis();
					paint.setColor(Color.RED);
					this.invalidate();//invalidate to refresh it on the screen so we can see the line red as feedback
					return true;
				}
			break;
			
			case MotionEvent.ACTION_UP:
				if(System.currentTimeMillis()-startTime > 1000 && dist <= 50){//long click at the middle of the line
					this.son.removeFather(this.father);
					this.father.removeSon(this.son);
					Rel.removeView(this);
				}else{
					paint.setColor(Color.BLACK);
					this.invalidate();
				}
				startTime = 0;
				return true;
			
			case MotionEvent.ACTION_MOVE:
				if(dist > 50){
					startTime = 0;
					paint.setColor(Color.BLACK);
					this.invalidate();
				}
					
			break;
			
			default:
				startTime = 0;
				paint.setColor(Color.BLACK);
				this.invalidate();
		}
		return false;

	}
	
	/**
	 * onDraw method
	 * 
	 * here is where we draw the line between the artifacts 
	 * 
	 */
	@Override
    public void onDraw(Canvas canvas) {
            canvas.drawLine(p1.x,p1.y,p2.x,p2.y,paint);//draws the line
            float Xmid,Ymid;
            Xmid = (p1.x + p2.x)/2;
    		Ymid = (p1.y + p2.y)/2;
    		
    		paint.setColor(Color.GREEN);
    		canvas.drawCircle(Xmid, Ymid, 5, paint);//set a green dot at the middle
    		paint.setColor(Color.BLACK);
            
    }

}
