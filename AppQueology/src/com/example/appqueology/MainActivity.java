package com.example.appqueology;



import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
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
        RelativeLayout Rel = (RelativeLayout)findViewById(R.id.graph);
        RelativeLayout Main = (RelativeLayout)findViewById(R.id.activity_main);
        
        Main.getLayoutParams().height = 10000;
        Main.getLayoutParams().width = 10000;
        Rel.getLayoutParams().height = 10000;
        Rel.getLayoutParams().width = 10000;

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
						float distX = Math.abs(event.getX()-lastX);
						float distY = Math.abs(event.getY()-lastY);
						float relDistX = distX/(distX+distY);
						float relDistY = distY/(distX+distY);
						float dist = (float)Math.sqrt(Math.pow(distX, 2)+Math.pow(distY, 2));
						
						if(lastX > event.getX()){
							v.setX(v.getX()-dist*relDistX*v.getScaleX());
						}else if(lastX < event.getX() && v.getX()+dist*relDistX*v.getScaleX()<0){
							v.setX(v.getX()+dist*relDistX*v.getScaleX());
						}
						
						if(lastY > event.getY()){
							v.setY(v.getY()-dist*relDistY*v.getScaleY());
						}else if(lastY < event.getY() && v.getY()+dist*relDistY*v.getScaleY()<0){
							v.setY(v.getY()+dist*relDistY*v.getScaleY());
						}
						
					break;
				}
				return true;
			}
		});
        
        zoomButtons.get(0).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				RelativeLayout Rel = (RelativeLayout)findViewById(R.id.graph);
				
				DisplayMetrics displaymetrics = new DisplayMetrics();
		        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		        Rel.setPivotY(displaymetrics.heightPixels/2);
		        Rel.setPivotX(displaymetrics.widthPixels/2);
				// TODO Auto-generated method stub
				if(Rel.getScaleX()-(float)0.1 >= 0 && Rel.getScaleY()-(float)0.1 >= 0){
					Rel.setScaleX(Rel.getScaleX()-(float)0.1);
					Rel.setScaleY(Rel.getScaleY()-(float)0.1);
					Rel.invalidate();
				}
				
			}
		});

		zoomButtons.get(1).setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				RelativeLayout Rel = (RelativeLayout)findViewById(R.id.graph);
				// TODO Auto-generated method stub
				if(Rel.getScaleX()+(float)0.1 <= 5 && Rel.getScaleY()+(float)0.1 <= 5){
					Rel.setScaleX(Rel.getScaleX()+(float)0.1);
					Rel.setScaleY(Rel.getScaleY()+(float)0.1);
					Rel.invalidate();
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
            default:
            	return true;
            
        }
        
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	Display display = getWindowManager().getDefaultDisplay();
    	if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
    		findViewById(R.id.slidingDrawer).getLayoutParams().height = 600;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
        	findViewById(R.id.slidingDrawer).getLayoutParams().height = 1000;
        }
        
    }
    
}
