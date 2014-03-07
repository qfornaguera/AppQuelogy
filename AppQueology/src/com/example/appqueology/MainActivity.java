package com.example.appqueology;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          
        RelativeLayout Rel = (RelativeLayout)findViewById(R.id.activity_main);
        
        findViewById(R.id.slidingDrawer).bringToFront();
        
        startSlider();
        
        Rel.setOnDragListener(new OnDragArtifact());
        
        Rel.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Global.touchedArtifact = null;
				return true;
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
