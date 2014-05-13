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
	String [] BAChrist = {"B.C","A.C"};
	long max,min,off;
	int idCounter = 40000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line);
		min = -5000;
		max = 2000;
		off = 1000;
		RelativeLayout Rel = (RelativeLayout)findViewById(R.id.timelinebody);
		
		createTimeLaps(min,max,off);
		XmlReadWriter xml = new XmlReadWriter(Rel);
		xml.readXML(getFilesDir()+"graf.xml");
		initArtifactsToTimeLaps(Rel);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_line, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
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

	public void createTimeLaps(long minAge,long maxAge,long offset){
		int AfterOrBeforeC1,AfterOrBeforeC2;
		LinearLayout timeline = (LinearLayout)findViewById(R.id.timeline);

		createLap(timeline,"Unseted\n Age \nArtifacts");
		
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
			if(i==0){
				i+=1;
				text = "From " + Math.abs(i) + " " + BAChrist[AfterOrBeforeC1] + "\n to \n"+ Math.abs(i-1+offset) + " " + BAChrist[AfterOrBeforeC2];
				i-=1;
			}else if(i+offset == 0){
				i-=1;
				text = "From " + Math.abs(i+1) + " " + BAChrist[AfterOrBeforeC1] + "\n to \n"+ Math.abs(i+offset) + " " + BAChrist[AfterOrBeforeC2];
				i+=1;
			}else{
				text = "From " + Math.abs(i) + " " + BAChrist[AfterOrBeforeC1] + "\n to \n"+ Math.abs(i+offset) + " " + BAChrist[AfterOrBeforeC2];
			}
			
			createLap(timeline,text);
			
		}
		
		createLap(timeline,"Younger");
		
	}
	
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
			if("".compareTo((String)node.getText())!=0)
				node.matchWithText();
			
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
				int assignedTimeLap = (int) (((node.getAge()+Math.abs(min))/off) + 40001);
				l = (LinearLayout)findViewById(assignedTimeLap);
				l.addView(node);
			}
		}
	}
}
