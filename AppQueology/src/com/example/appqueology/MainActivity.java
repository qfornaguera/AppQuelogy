package com.example.appqueology;



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
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          
        RelativeLayout Rel = (RelativeLayout)findViewById(R.id.activity_main);
        Rel.getLayoutParams().height = Rel.getHeight() + 2000;
        Rel.getLayoutParams().width = Rel.getWidth() + 2000;
        /*SlidingDrawer sd = (SlidingDrawer)findViewById(R.id.slidingDrawer);
        sd.getLayoutParams().height = getWindow().getAttributes().height;*/
        
        
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
    }
    
    
    public void startSlider(){
    	Artifact square = new Artifact(getApplicationContext(),-1);
        square.setBackgroundColor(Color.BLACK);
        square.setX(50);
        square.setY(50);
        LinearLayout slideContent = (LinearLayout)findViewById(R.id.content);
        slideContent.addView(square, 50, 50);
        
    	
    }
}
