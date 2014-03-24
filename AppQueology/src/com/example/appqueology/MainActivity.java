package com.example.appqueology;



import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.ZoomControls;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Global.ID = 0;
        RelativeLayout Rel = (RelativeLayout)findViewById(R.id.activity_main);
        
        Rel.getLayoutParams().height = Rel.getHeight() + 10000;
        Rel.getLayoutParams().width = Rel.getWidth() + 10000;
        
        ZoomControls zoom = (ZoomControls)findViewById(R.id.zoomControls1);
        ArrayList <View> zoomButtons = zoom.getTouchables();
        zoomButtons.get(0).getLayoutParams().width = 100;
        zoomButtons.get(1).getLayoutParams().width = 100;
        zoomButtons.get(0).getLayoutParams().height = 100;
        zoomButtons.get(1).getLayoutParams().height = 100;
        
        
        findViewById(R.id.slidingDrawer).bringToFront();
        
        startSlider();
        
        Rel.setOnDragListener(new OnDragArtifact());
        
        Rel.setOnTouchListener(new OnTouchListener() {
			float lastX;
			float lastY;
        	
        	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Global.touchedArtifact = null;
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						lastX = event.getX();
						lastY = event.getY();
					break;
					
					case  MotionEvent.ACTION_MOVE:
						SlidingDrawer sd = (SlidingDrawer)v.findViewById(R.id.slidingDrawer);
						float distX = Math.abs(event.getX()-v.getX());
						float distY = Math.abs(event.getY()-v.getY());
						float relDistX = distX/(distX+distY);
						float relDistY = distY/(distX+distY);
						if(lastX > event.getX()){
							v.setX(v.getX()-50*relDistX);
							sd.setX(sd.getX()+50*relDistX);
						}else if(lastX < event.getX() && v.getX()+50*relDistX<0){
							v.setX(v.getX()+50*relDistX);
							sd.setX(sd.getX()-50*relDistX);
						}
						
						if(lastY > event.getY()){
							v.setY(v.getY()-50*relDistY);
							sd.setY(sd.getY()+50*relDistY);
						}else if(lastY < event.getY() && v.getY()+50*relDistY<0){
							v.setY(v.getY()+50*relDistY);
							sd.setY(sd.getY()-50*relDistY);
						}
						
					break;
				}
				return true;
			}
		});
        
        zoomButtons.get(0).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RelativeLayout Rel = (RelativeLayout)findViewById(R.id.activity_main);
				SlidingDrawer sd = (SlidingDrawer)findViewById(R.id.slidingDrawer);
				// TODO Auto-generated method stub
				if(Rel.getScaleX()-(float)0.1 >= 0 && Rel.getScaleY()-(float)0.1 >= 0){
					Rel.setScaleX(Rel.getScaleX()-(float)0.1);
					Rel.setScaleY(Rel.getScaleY()-(float)0.1);
					sd.setScaleX(sd.getScaleX()+(float)0.1);
					sd.setScaleY(sd.getScaleY()+(float)0.1);
				}
				Log.v("X"," "+Rel.getX());
				Log.v("Y"," "+Rel.getY());
			}
		});

		zoomButtons.get(1).setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				RelativeLayout Rel = (RelativeLayout)findViewById(R.id.activity_main);
				SlidingDrawer sd = (SlidingDrawer)findViewById(R.id.slidingDrawer);
				// TODO Auto-generated method stub
				if(sd.getScaleX()-(float)0.1 >= 0 && sd.getScaleY()-(float)0.1 >= 0){
					Rel.setScaleX(Rel.getScaleX()+(float)0.1);
					Rel.setScaleY(Rel.getScaleY()+(float)0.1);
					sd.setScaleX(sd.getScaleX()-(float)0.1);
					sd.setScaleY(sd.getScaleY()-(float)0.1);
				}
			}
		});
        
    }
    
    
    public void startSlider(){
    	Artifact square = new Artifact(getApplicationContext());
        square.setBackgroundColor(Color.BLACK);
        square.setX(50);
        square.setY(50);
        LinearLayout slideContent = (LinearLayout)findViewById(R.id.content);
        slideContent.addView(square, 50, 50);
        
    	
    }
}
