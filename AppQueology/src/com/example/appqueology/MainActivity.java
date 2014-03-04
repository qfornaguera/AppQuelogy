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
import android.widget.RelativeLayout;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Artifact square = new Artifact(getApplicationContext());
        
        Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
        
        square.setX(size.x/2 - 100);
        square.setY(size.y/2 - 100);
        square.setBackgroundColor(Color.BLACK);
        
        RelativeLayout Rel = (RelativeLayout)findViewById(R.id.activity_main);
        Rel.addView(square, 100, 100);
        
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
