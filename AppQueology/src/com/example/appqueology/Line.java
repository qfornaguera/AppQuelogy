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
    public void onDraw(Canvas canvas) {
            canvas.drawLine(p1.x,p1.y,p2.x,p2.y,paint);
    }

}
