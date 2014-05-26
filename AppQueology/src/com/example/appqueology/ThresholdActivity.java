package com.example.appqueology;

import java.text.NumberFormat;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Build;

/**
 * This activity receive the threshold and the offset parameters and send it to the TimeLapActivity
 * 
 * @author Joaquim Fornaguera
 *
 */
public class ThresholdActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_threshold);
		
		findViewById(R.id.button1).setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_UP){
					
					boolean itsOK = checkParameters();
					if(itsOK){
						Intent toTimelineActivity = new Intent(findViewById(R.id.formthreshold).getContext(), TimeLineActivity.class);
						EditText aux = (EditText)findViewById(R.id.editText1);
						toTimelineActivity.putExtra("min",Integer.parseInt(aux.getText().toString()));
						aux = (EditText)findViewById(R.id.editText2);
						toTimelineActivity.putExtra("max",Integer.parseInt(aux.getText().toString()));
						aux = (EditText)findViewById(R.id.editText3);
						toTimelineActivity.putExtra("off",Integer.parseInt(aux.getText().toString()));
						startActivityForResult(toTimelineActivity, 0);
						return true;
					}
				}
				
				return false;
				
			}
			
			
			/**
			 * checkParameters method
			 * 
			 * Checks if the parameters are correct for the next step. Reports the error when incorrect
			 * @return
			 */
			private boolean checkParameters() {
				// TODO Auto-generated method stub
				
				int min = 0,max = 0,off = 0;
				
				try{
					EditText aux = (EditText)findViewById(R.id.editText1);
					min = Integer.parseInt(aux.getText().toString());
					aux = (EditText)findViewById(R.id.editText2);
					max = Integer.parseInt(aux.getText().toString());
					aux = (EditText)findViewById(R.id.editText3);
					off = Integer.parseInt(aux.getText().toString());
				}catch(NumberFormatException e){
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(ThresholdActivity.this);
					alertDialog.setTitle("Parameter Error");
					alertDialog.setMessage("There are parameters that remain unset");
					alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						// here you can add functions
						}
					});
					alertDialog.show();
					return false;
				}
				
				
				if(min > max){
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(ThresholdActivity.this);
					alertDialog.setTitle("Parameter Error");
					alertDialog.setMessage("Maxium age cannot be lower than Minium age");
					alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						// here you can add functions
						}
					});
					alertDialog.show();
					return false;
				}
				
				if((max-min)%off != 0){
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(ThresholdActivity.this);
					alertDialog.setTitle("Parameter Error");
					alertDialog.setMessage("The Threshold("+(max-min)+"years) must be divisible by the Lap size ("+off+")");
					alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						// here you can add functions
						}
					});
					alertDialog.show();
					return false;
				}
				
				return true;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.threshold, menu);
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

	

}
