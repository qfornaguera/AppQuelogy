package com.example.appqueology;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.os.Build;

/**
 * 
 * 
 * @author Joaquim Fornaguera
 * 
 * This activity is a view of the artifacts classified by age from a chosen threshold
 *
 */
public class TimeLineActivity extends ActionBarActivity {
	private String [] BAChrist = {"B.C","A.C"};
	private long max,min,off;
	private int idCounter = 40000;//we set the idCounter to 40000, this will be the id for the time laps, we could put any other number, but for this first stage of the app i've set it to 40000, that means that the artifacts could go from 0 to 39999 id
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line);
		min = getIntent().getIntExtra("min",0);
		max = getIntent().getIntExtra("max",0);
		off = getIntent().getIntExtra("off",0);
		RelativeLayout Rel = (RelativeLayout)findViewById(R.id.timelinebody);
		
		createTimeLaps(min,max,off);
		XmlReadWriter xml = new XmlReadWriter(Rel);
		xml.readXML(getFilesDir()+"graf.xml");
		initArtifactsToTimeLaps(Rel);
	}
	
	
	/**
	 * createLap method
	 * 
	 * Creates a time Lap in the Layout formed by a horizontal scroll view and a linear layout  with a text view set with the param text
	 * 
	 * @param timeline
	 * @param text
	 */
	public void createLap(LinearLayout timeline,String text){
		LinearLayout Lap = new LinearLayout(this);
		HorizontalScrollView hsv = new HorizontalScrollView(this);
		TextView textView = new TextView(this);
		hsv.setBackground(getResources().getDrawable(R.drawable.bordersblack));
		Lap.setPadding(20, 10, 20, 0);
		hsv.addView(Lap);
		timeline.addView(hsv);
		Lap.getLayoutParams().height = 400;
		Lap.getLayoutParams().width = LayoutParams.MATCH_PARENT;
		Lap.setId(idCounter);
		textView.setTextColor(Color.BLACK);
		textView.setText(text);
		Lap.addView(textView);
		idCounter++;
	}
	
	/**
	 * 
	 * createTimeLaps method
	 *
	 * Create Time Laps on the layout according the minimum age, the maximum age and the offset
	 * 
	 * @param minAge
	 * @param maxAge
	 * @param offset
	 */
	public void createTimeLaps(long minAge,long maxAge,long offset){
		int AfterOrBeforeC1,AfterOrBeforeC2;
		LinearLayout timeline = (LinearLayout)findViewById(R.id.timeline);

		createLap(timeline,"Unset\n Age \nArtifacts");
		
		createLap(timeline,"Older");
		
		
		for(long i=minAge;i<maxAge;i+=offset){
			if(i < 0){
				AfterOrBeforeC1 = 0;
			}else{
				AfterOrBeforeC1 = 1;
			}
			
			if(i+offset <= 0){
				AfterOrBeforeC2 = 0;
			}else{
				AfterOrBeforeC2 = 1;
			}
			
			String text;
			if(i==0){//this if sets the correct age label for every Time Lap
				i+=1;
				text = "From " + Math.abs(i) + " " + BAChrist[AfterOrBeforeC1] + "\n to \n"+ Math.abs(i-1+offset) + " " + BAChrist[AfterOrBeforeC2];
				i-=1;
			}else if(i+offset == 0){
				i-=1;
				text = "From " + Math.abs(i+1) + " " + BAChrist[AfterOrBeforeC1] + "\n to \n"+ Math.abs(i+offset) + " " + BAChrist[AfterOrBeforeC2];
				i+=1;
			}else{
				text = "From " + Math.abs(i) + " " + BAChrist[AfterOrBeforeC1] + "\n to \n"+ Math.abs(i+offset-1) + " " + BAChrist[AfterOrBeforeC2];
			}
			
			createLap(timeline,text);
			
		}
		
		createLap(timeline,"Younger");
		
	}
	
	
	/**
	 * initArtifactsToTimeLaps method
	 * 
	 * Deploy the artifacts of the graph to the time laps acording to it's age.
	 * 
	 * @param Rel
	 */
	public void initArtifactsToTimeLaps(RelativeLayout Rel){
		ArrayList<Artifact> nodeList = new ArrayList<Artifact>();
		LinearLayout l;
		for(int i=0;i<Rel.getChildCount();i++){
			if(Rel.getChildAt(i).getTag() != null){
				if(Rel.getChildAt(i).getTag().toString().compareTo("node") == 0){//then we get all the artifact views of the board
					nodeList.add((Artifact)Rel.getChildAt(i));
				}
			}
			
		}
		
		for(int j=0;j<nodeList.size();j++){//then we iterate and create the lines between artifacts
			Artifact node = nodeList.get(j);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			params.setMargins(20,100,0,0);
			node.setLayoutParams(params);
			node.setEnabled(false);
			if("".compareTo((String)node.getText())!=0){
				node.matchWithText();
			}else{
				node.getLayoutParams().width = 100;
				node.getLayoutParams().height = 100;
			}
			
			Rel.removeView(node);
			if(node.getAge() == 0){
				l = (LinearLayout)findViewById(40000);
				l.addView(node);
			}else if(node.getAge()<min){
				l = (LinearLayout)findViewById(40001);
				l.addView(node);
			}else if(node.getAge()>max){
				l = (LinearLayout)findViewById(idCounter-1);
				l.addView(node);
			}else{
				int assignedTimeLap = findThreshold(node.getAge());
				l = (LinearLayout)findViewById(assignedTimeLap+40002);//40002 is the id of the 'older' level so we add this offset to the assignedTimeLap that goes from 0 to (max - min)/off
				l.addView(node);
			}
		}
	}
	
	
	/**
	 * findThreshold method
	 * 
	 * Calculates the id of the time lap where an age belong
	 * 
	 * @param age
	 * @return
	 */
	public int findThreshold(float age){
		int index = 0;
		index = (int) (((age - min)/off));
		return index;
	}
}
