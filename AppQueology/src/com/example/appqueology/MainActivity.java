package com.example.appqueology;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;


/**
 * @author Joaquim Fornaguera
 * 
 * MainActivity
 * 
 * This class is the activity that contains the board where the graph is printed and modified. Is the key of the app
 * 
 * */
public class MainActivity extends Activity {
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
        square.setText("Artefacte");
        square.setType("Artefacte");
        square.setBackgroundResource(R.drawable.anfora);
        RelativeLayout slideContent = (RelativeLayout)findViewById(R.id.content);
        slideContent.addView(square, 150, 150);
        
        square = new Artifact(getApplicationContext());
        square.setBackgroundColor(Color.BLACK);
        square.setX(50);
        square.setY(250);
        square.setText("Estrat");
        square.setType("Estrat");
        square.setTextSize(20);
        square.setBackgroundResource(R.drawable.estrat);
        slideContent.addView(square, 150, 150);
        
        square = new Artifact(getApplicationContext());
        square.setBackgroundColor(Color.BLACK);
        square.setX(50);
        square.setY(450);
        square.setText("Mur");
        square.setType("Mur");
        square.setTextSize(20);
        square.setBackgroundResource(R.drawable.mur);
        slideContent.addView(square, 150, 150);
        
        square = new Artifact(getApplicationContext());
        square.setBackgroundColor(Color.BLACK);
        square.setX(50);
        square.setY(650);
        square.setText("Negativa");
        square.setType("Negativa");
        square.setBackgroundResource(R.drawable.negativa);
        slideContent.addView(square, 150, 150);
        
        square = new Artifact(getApplicationContext());
        square.setBackgroundColor(Color.BLACK);
        square.setX(50);
        square.setY(850);
        square.setText("Interfase");
        square.setType("Interfase");
        square.setBackgroundResource(R.drawable.interfase);
        slideContent.addView(square, 150, 150);
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
    	RelativeLayout graf = (RelativeLayout)findViewById(R.id.graph);
    	XmlReadWriter xmlRW = new XmlReadWriter(graf);
        switch (item.getItemId()) {
            case R.id.save:
                xmlRW.writeXML(getFilesDir()+"graf.xml");
                return true;
            case R.id.load:
            	graf.removeAllViews();
            	xmlRW.readXML(getFilesDir()+"graf.xml");
            	Utility.beautifygraph((RelativeLayout)findViewById(R.id.graph));
            	Utility.recalculateLines((RelativeLayout)findViewById(R.id.graph));
                return true;
                
            case R.id.beautify:
            	Utility.beautifygraph((RelativeLayout)findViewById(R.id.graph));
            	Utility.recalculateLines((RelativeLayout)findViewById(R.id.graph));
            	return true;
            case R.id.timeline:
            	Intent toTimeLineView = new Intent(this, ThresholdActivity.class);
				this.startActivityForResult(toTimeLineView, 1);
            	return true;
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
    	RelativeLayout Rel = (RelativeLayout)findViewById(R.id.graph);
        switch(requestCode) {
        	case 0:
        		if (resultCode == Activity.RESULT_OK) {
        			Artifact artifact = (Artifact)Rel.findViewById(data.getIntExtra("id", -1));
        			if(data.getStringExtra("option").compareTo("save") == 0){
        				artifact.setText(data.getStringExtra("text"));
        				artifact.setAge(data.getLongExtra("age",0));
        				artifact.setInformation(data.getStringExtra("information"));
        				artifact.setPosition(data.getStringExtra("position"));
        				artifact.setBackgroundResource(Utility.getDrawableType(artifact));
        				if("".compareTo((String) artifact.getText()) != 0){
        					artifact.matchWithText();
    					}
        			}else if(data.getStringExtra("option").compareTo("delete") == 0){
        				Rel.removeView(artifact);
        				artifact.kill(Rel);
        			}
        			artifact.setEnabled(true);
        			Utility.recalculateLines(Rel);
        		} 
        	break;
        	
        	default:
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
    	Button handler = (Button)findViewById(R.id.handler);
    	Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
    	findViewById(R.id.content).bringToFront();
    	lockUnlock.bringToFront();
    	lockUnlock.setX(size.x-150);
        lockUnlock.setY(size.y-250);
        handler.setY((size.y/2)-100);
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
         Button handler = (Button)findViewById(R.id.handler);
         RelativeLayout sladingContent = (RelativeLayout)findViewById(R.id.content);
         RelativeLayout slader = (RelativeLayout)findViewById(R.id.slader);
         
         
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
         
         handler.setX(0);
         slader.setVisibility(View.INVISIBLE);
         
         startSlider();
         
         configurationsChanged();
         
         
         Rel.setOnDragListener(new OnDragArtifact());
         
         Main.setOnTouchListener(new OnTouchZoomandMove(Rel));
         sladingContent.setOnDragListener(new OnDragSladingDrawer());
         
         handler.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				RelativeLayout slader = (RelativeLayout) findViewById(R.id.slader);
				Button handler = (Button)v;
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
					break;
					
					case MotionEvent.ACTION_UP:
						if(slader.getVisibility() == View.VISIBLE){
							slader.setVisibility(View.INVISIBLE);
							handler.setX(0);
							handler.setText(">");
						}else if(slader.getVisibility() == View.INVISIBLE){
							slader.setVisibility(View.VISIBLE);
							handler.setX(290);
							handler.setText("<");
						}
							
					break;
					
					case MotionEvent.ACTION_MOVE:
					break;
				}
				return false;
			}
		});
         
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
 					Utility.beautifygraph(Rel);
 					Utility.setPositionByAge(Rel);
 				}else{
 					b.setBackgroundResource(R.drawable.unlock);
 					b.setTag("UNLOCKED");
 					Rel.setEnabled(true);
 					Utility.beautifygraph(Rel);
 					for(int u=0;u<Rel.getChildCount();u++){//when we unlock, we enable all the artifacts disabled
 						if(Rel.getChildAt(u).getTag() != null){
 								Rel.getChildAt(u).setEnabled(true);
 						}
 					}
 					Utility.recalculateLines(Rel);
 					
 				}
 				
 			}
 		});
    }
}
