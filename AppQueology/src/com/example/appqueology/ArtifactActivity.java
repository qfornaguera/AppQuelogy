package com.example.appqueology;

import java.util.zip.Inflater;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artifact);
		
		id = getIntent().getIntExtra("id", -1);
		text = getIntent().getStringExtra("text");
		EditText t = (EditText)findViewById(R.id.editText1);
		t.setText(text);
		
		findViewById(R.id.save).setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_UP){
					EditText t = (EditText)findViewById(R.id.editText1);
					text = t.getText().toString();
					resultIntent.putExtra("text", text);
					resultIntent.putExtra("id", id);
					resultIntent.putExtra("option", "save");
					setResult(Activity.RESULT_OK, resultIntent);
					finish();
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
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.artifact, menu);
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
