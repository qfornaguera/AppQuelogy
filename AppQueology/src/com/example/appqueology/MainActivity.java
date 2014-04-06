package com.example.appqueology;



import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
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
	ScaleGestureDetector mScaleDetector;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Global.ID = 0;
        RelativeLayout Rel = (RelativeLayout)findViewById(R.id.graph);
        RelativeLayout Main = (RelativeLayout)findViewById(R.id.activity_main);
        
        Main.getLayoutParams().height = 10000;
        Main.getLayoutParams().width = 10000;
        Rel.getLayoutParams().height = 10000;
        Rel.getLayoutParams().width = 10000;
        
        
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Rel.setPivotY(displaymetrics.heightPixels/2);
        Rel.setPivotX(displaymetrics.widthPixels/2);
        
        
        
        findViewById(R.id.slidingDrawer).bringToFront();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        
    	findViewById(R.id.slidingDrawer).getLayoutParams().height = size.y-100;
        
        startSlider();
        
        Rel.setOnDragListener(new OnDragArtifact());
        
        Main.setOnTouchListener(new OnTouchListener() {
			float lastX,lastX2,Xstart;
			float lastY,lastY2,Ystart;
			float lastDist;
			RelativeLayout Rel = (RelativeLayout)findViewById(R.id.graph);

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
						lastX = event.getX(0);
						lastY = event.getY(0);
						lastX2 = event.getX(1);
						lastY2 = event.getY(1);
						lastDist = (float)Math.sqrt(Math.pow(lastX-lastX2, 2)+Math.pow(lastY-lastY2, 2));
					break;
					
					case  MotionEvent.ACTION_MOVE:
						
						if(event.getPointerCount() == 1){
							float distX = Math.abs(event.getX()-Xstart);
							float distY = Math.abs(event.getY()-Ystart);
							if(Xstart > event.getX(0)){
								Rel.setX(Rel.getX()-distX);
							}else if(Xstart < event.getX(0) && Rel.getX()+distX<0){
								Rel.setX(Rel.getX()+distX);
							}
							
							if(Ystart > event.getY(0)){
								Rel.setY(Rel.getY()-distY);
								
							}else if(Ystart < event.getY(0) && Rel.getY()+distY<0){
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

								Rel.invalidate();
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
    	XmlReadWriter xmlRW = new XmlReadWriter((RelativeLayout)findViewById(R.id.graph));
        switch (item.getItemId()) {
            case R.id.save:
                xmlRW.writeXML(getFilesDir()+"graf.xml");
                return true;
            case R.id.load:
            	xmlRW.readXML(getFilesDir()+"graf.xml");
                return true;
                
            case R.id.beautify:
            	OnDragArtifact.beautifygraph((RelativeLayout)findViewById(R.id.graph));
            default:
            	return true;
            
        }
        
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
    	findViewById(R.id.slidingDrawer).getLayoutParams().height = size.y-100;
    }
    
    @Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	//super.onActivityResult(requestCode, resultCode, data);
    	RelativeLayout Rel = (RelativeLayout)findViewById(R.id.graph);
        Artifact artifact = (Artifact)Rel.findViewById(data.getIntExtra("id", -1));
        switch(requestCode) {
        	case 0:
        		if (resultCode == Activity.RESULT_OK) { 
        			if(data.getStringExtra("option").compareTo("save") == 0){
        				artifact.setText(data.getStringExtra("text"));
        			}else if(data.getStringExtra("option").compareTo("delete") == 0){
        				Rel.removeView(artifact);
        				artifact.kill(Rel);
        			}
        		} 
        	break; 
        } 
         
    }
    
}
