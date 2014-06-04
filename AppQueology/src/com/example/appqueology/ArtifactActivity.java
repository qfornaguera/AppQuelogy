package com.example.appqueology;

import java.util.ArrayList;
import java.util.zip.Inflater;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Build;

/**
 * 
 * @author Joaquim Fornaguera
 * 
 * At this activity we are able to check the properties of and artifact and modify some of them
 * 
 * We can save the properties or delete the artifact
 *
 */
public class ArtifactActivity extends ActionBarActivity {
	int id;
	String text;
	Intent resultIntent = new Intent();
	String [] position = {"Normal","Adosat","Cobreix"};
	ArrayList <String> sonsAges,fathersAges;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artifact);
		RadioButton rb;
		sonsAges = getIntent().getStringArrayListExtra("sonsAges");
		fathersAges = getIntent().getStringArrayListExtra("fathersAges");
		id = getIntent().getIntExtra("id", -1);
		text = getIntent().getStringExtra("text");
		EditText t = (EditText)findViewById(R.id.editText1);
		t.setText(text);
		TextView tv = (TextView)findViewById(R.id.textView3);
		text = "";
		ArrayList <String> above = getIntent().getStringArrayListExtra("father");
		for(int i = 0;i<above.size();i++){
			if(above.get(i).compareTo("") == 0)
				text = text + " | " + "Unknown";
			else
				text = text + " | " + above.get(i);
		}
		
		if(above.size() == 0)
			tv.setText("Nothing");
		else
			tv.setText(text);
		
		text = "";
		tv = (TextView)findViewById(R.id.textView5);
		ArrayList <String> below = getIntent().getStringArrayListExtra("sons");
		for(int i = 0;i<below.size();i++){
			if(below.get(i).compareTo("") == 0)
				text = text + " | " + "Unknown";
			else
				text = text + " | " + below.get(i);
		}
		
		if(below.size() == 0)
			tv.setText("Nothing");
		else
			tv.setText(text);
		
		tv = (TextView)findViewById(R.id.textView8);
		text = getIntent().getStringExtra("type");
		tv.setText(text);
		
		t = (EditText)findViewById(R.id.editText3);
		text = getIntent().getStringExtra("information");
		t.setText(text);
		
		long age = getIntent().getLongExtra("age",0);
		
		t = (EditText)findViewById(R.id.editText2);
		t.setText(Long.toString(Math.abs(age)));
		if(age < 0){
			rb = (RadioButton)findViewById(R.id.radio0);
			rb.setChecked(true);
			rb = (RadioButton)findViewById(R.id.radio1);
			rb.setChecked(false);
		}else{
			rb = (RadioButton)findViewById(R.id.radio0);
			rb.setChecked(false);
			rb = (RadioButton)findViewById(R.id.radio1);
			rb.setChecked(true);
		}
		findViewById(R.id.save).setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
				RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
				if(event.getAction() == MotionEvent.ACTION_UP && checkAges()){
					EditText t = (EditText)findViewById(R.id.editText1);
					text = t.getText().toString();
					resultIntent.putExtra("text", text);
					resultIntent.putExtra("id", id);
					resultIntent.putExtra("option", "save");
					t = (EditText)findViewById(R.id.editText2);
					Log.v(""+t.getText().toString().compareTo(""),"");
					if(t.getText().toString().compareTo("") != 0){
						if(rb == rg.findViewById(R.id.radio0)){
							resultIntent.putExtra("age",-1*Long.parseLong((String) t.getText().toString()));
						}else{
							resultIntent.putExtra("age", Long.parseLong((String) t.getText().toString()));
						}
					}else{
						resultIntent.putExtra("age", 0);
					}
					
					t = (EditText)findViewById(R.id.editText3);
					resultIntent.putExtra("information", (String) t.getText().toString());
					Spinner spinner = (Spinner)findViewById(R.id.spinner1);
					resultIntent.putExtra("position", (String)spinner.getSelectedItem());
					
					setResult(Activity.RESULT_OK, resultIntent);
					finish();
				}
				return true;
			}
			
			public boolean checkAges(){
				long age;
				RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
				RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
				EditText et = (EditText) findViewById(R.id.editText2);
				if(et.getText().toString().compareTo("") != 0){
					if(rb == rg.findViewById(R.id.radio0)){
						age = -1*Long.parseLong((String) et.getText().toString());
					}else{
						age = Long.parseLong((String) et.getText().toString());
					}
				}else{
					age = 0;
				}
				for(int i = 0;i<sonsAges.size();i++){
					if(age < Long.parseLong(sonsAges.get(i))){
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(et.getContext());
						alertDialog.setTitle("Invalid Age");
						alertDialog.setMessage("Nodes can't be older than his sons(Son's Age: "+sonsAges.get(i)+" Node's Age: "+age+")");
						alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
							// here you can add functions
							}
						});
						alertDialog.show();
						return false;
					}
				}
				
				for(int i = 0;i<fathersAges.size();i++){
					if(age > Long.parseLong(fathersAges.get(i))){
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(et.getContext());
						alertDialog.setTitle("Invalid Age");
						alertDialog.setMessage("Nodes can't be younger than his fathers(Father's Age: "+fathersAges.get(i)+" Node's Age: "+age+")");
						alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
							// here you can add functions
							}
						});
						alertDialog.show();
						return false;
					}
				}
				return true;
			}
		});
		
		findViewById(R.id.delete).setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_UP){
					resultIntent.putExtra("id",id);
					resultIntent.putExtra("option","delete");
					setResult(Activity.RESULT_OK, resultIntent);
					finish();
				}
				return true;
			}
		});
		
		Spinner spinner = (Spinner)findViewById(R.id.spinner1);
		
		ArrayAdapter<String> adapter_position = new ArrayAdapter<String>(this,R.layout.spinner_item, position);
		adapter_position.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter_position);
		
		spinner.setSelection(getIndex(getIntent().getStringExtra("position")));
		
	}
	

	
	
	public int getIndex(String pos){
		for(int i=0;i<position.length;i++){
			if(pos.compareTo(position[i])==0){
				return i;
			}
		}
		return 0;
	}

}
