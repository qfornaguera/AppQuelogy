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
import android.view.Gravity;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.ZoomControls;

/**
 * @author Joaquim Fornaguera
 * 
 * MainActivity
 * 
 * This class is the activity that contains the board where the graph is printed and modified. Is the key of the app
 * 
 * */
public class MainActivity extends Activity {
	ScaleGestureDetector mScaleDetector;

	/**
	 * OnCreate method
	 * 
	 * Initiates the activity 
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMainActivity();
    }
    
    
	/**
	 * StartSlider method
	 * 
	 * Starts the SlidingDrawer view and its tools
	 */
    public void startSlider(){
    	Artifact square = new Artifact(getApplicationContext());
        square.setBackgroundColor(Color.BLACK);
        square.setX(50);
        square.setY(50);
        square.setBackgroundResource(R.drawable.anfora);
        LinearLayout slideContent = (LinearLayout)findViewById(R.id.content);
        slideContent.addView(square, 50, 50);

    }
    
    
    /**
     * onCreateOptionsMenu method
     * 
     * Initiates the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
    
    /**
     * onOptionsItemSelected method
     * 
     * Execute the actions depending on the option chosen by the user at the menu
     */
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
            	Utility.beautifygraph((RelativeLayout)findViewById(R.id.graph));
            default:
            	return true;
            
        }
        
    }
    
    
    /**
     * onConfigurationChanged method
     * 
     * Executed when configuration changes, actually when the window size or orientation changes
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	configurationsChanged();
    }
    
    
    /**
     * onActivityResult method
     * 
     * Executed when an other activity launched from this activity returns a result. The method acts at consequence of the result
     *
     */
    @Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	//super.onActivityResult(requestCode, resultCode, data);
    	RelativeLayout Rel = (RelativeLayout)findViewById(R.id.graph);
        switch(requestCode) {
        	case 0:
        		if (resultCode == Activity.RESULT_OK) {
        			Artifact artifact = (Artifact)Rel.findViewById(data.getIntExtra("id", -1));
        			if(data.getStringExtra("option").compareTo("save") == 0){
        				artifact.setText(data.getStringExtra("text"));
        				artifact.setAge(data.getLongExtra("age",0));
        				if("".compareTo((String) artifact.getText()) != 0){
        					artifact.matchWithText();
    					}
        			}else if(data.getStringExtra("option").compareTo("delete") == 0){
        				Rel.removeView(artifact);
        				artifact.kill(Rel);
        			}
        			Utility.recalculateLines(Rel);
        		} 
        	break; 
        } 
         
    }
    
    /**
     * configurationsChanged method
     * 
     * It's called when the orientation at configurations changes. Resize the SlidingDrawer, reassign Lock button position etc
     */
    public void configurationsChanged(){
    	Configuration config = getResources().getConfiguration();
    	Button lockUnlock = (Button)findViewById(R.id.button1);
    	Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
    	findViewById(R.id.slidingDrawer).getLayoutParams().height = size.y-100;
    	findViewById(R.id.slidingDrawer).bringToFront();
    	lockUnlock.bringToFront();
    	lockUnlock.setX(size.x-150);
        lockUnlock.setY(size.y-250);
    }
    
    /**
     * initMainactivity method
     * 
     * Initialize the activity assets that it need to go on with the app.
     * assigns the width and height of the board, the listeners and the set of the its tools
     * 
     */
    public void initMainActivity(){
    	 Global.ID = 0;
         RelativeLayout Rel = (RelativeLayout)findViewById(R.id.graph);
         RelativeLayout Main = (RelativeLayout)findViewById(R.id.activity_main);
         Button lockUnlock = (Button)findViewById(R.id.button1);
         ViewGroup sladingDrawer = (ViewGroup)findViewById(R.id.slidingDrawer);
         
         Main.getLayoutParams().height = 10000;
         Main.getLayoutParams().width = 10000;
         Rel.getLayoutParams().height = 10000;
         Rel.getLayoutParams().width = 10000;
         
         Rel.setPivotY(0);
         Rel.setPivotX(0);
         
         lockUnlock.getLayoutParams().height = 100;
         lockUnlock.getLayoutParams().width = 100;
         
         lockUnlock.setTag("UNLOCKED");
         lockUnlock.setBackgroundResource(R.drawable.unlock);
         
         configurationsChanged();
         
         startSlider();
         
         Rel.setOnDragListener(new OnDragArtifact());
         
         Main.setOnTouchListener(new OnTouchZoomandMove(Rel));
         
         sladingDrawer.setOnDragListener(new OnDragSladingDrawer());
         
         lockUnlock.setOnClickListener(new OnClickListener() {
        	RelativeLayout Rel = (RelativeLayout)findViewById(R.id.graph);
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Button b = (Button)v;
				String tag = (String)b.getTag();
				if(tag.compareTo("UNLOCKED") == 0){
					b.setBackgroundResource(R.drawable.lock);
					b.setTag("LOCKED");
					Rel.setEnabled(false);
				}else{
					b.setBackgroundResource(R.drawable.unlock);
					b.setTag("UNLOCKED");
					Rel.setEnabled(true);
				}
				
			}
		});
    }
}
