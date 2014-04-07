package com.example.appqueology;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class Artifact extends TextView{
	
	ArrayList <Artifact> sons = null;
	Artifact father = null;
	float width,heigth;
	
	public Artifact(Context context) {
		super(context);
		sons = new ArrayList<Artifact>();
		Global.touchedArtifact = this;
		// TODO Auto-generated constructor stub
		this.setOnTouchListener(new OnTouchArtifact());
		this.setTextColor(Color.WHITE);
		this.setPadding(10,10,10,10);
		this.setGravity(Gravity.CENTER);
		
	}
	
	public void setPrevWidth(float width){
		this.width = width;
		getLayoutParams().width = (int) width;
	}
	
	public void setPrevHeight(float heigth){
		this.heigth = heigth;
		getLayoutParams().height = (int) heigth;
	}
	
	public float getCenterX(){
		return this.getX() + this.width/2;
	}
	
	public float getCenterY(){
		return this.getY() + this.heigth/2;
	}
	
	public void setFather(Artifact father){
		this.father = father;
	}
	
	public void addSon(Artifact son){
		sons.add(son);
	}
	
	public void removeAllSons(){
		sons = new ArrayList<Artifact>();
	}
	
	public Artifact getFather(){
		return father;
	}
	
	public ArrayList<Artifact> getSons(){
		return sons;
	}
	
	public void removeSon(int index){
		sons.remove(index);
	}
	
	public void removeSon(Artifact son){
		sons.remove(son);
	}
	
	public void seekFather(RelativeLayout Rel){
		Artifact father = null;
		float minDist = Float.MAX_VALUE;
		for(int i=0;i<Rel.getChildCount();i++){
			View maybeFather = Rel.getChildAt(i);
			if(maybeFather.getTag() != null){
				if(maybeFather.getTag().toString().compareTo("node") == 0 && this != maybeFather){
					float dist = (float)Math.sqrt(Math.pow(maybeFather.getX()-this.getX(),2)+Math.pow(maybeFather.getY()-this.getY(),2));
					if(minDist > dist && maybeFather.getY() < this.getY()){
						if(Math.abs(maybeFather.getY()-this.getY()) >= 100){
							minDist = dist;
							father = (Artifact) maybeFather;
						}
					}
				}
			}
		}
		
		if(father!=null){
			if(this.father != null)
				this.father.removeSon(this);
			
			if(!father.getSons().contains(this))
				father.addSon(this);
		}
		
		this.father = father;
		
	}
	
	public void kill(RelativeLayout Rel){
		ArrayList <Artifact> aux = (ArrayList<Artifact>) sons.clone();
		for(int i = 0;i<aux.size();i++){
			aux.get(i).seekFather(Rel);
		}
		OnDragArtifact.recalculateLines(Rel);
		removeAllSons();
	}
	
	public void matchWithText(){
		String text = (String) getText();
		String[] words = text.split(" ");
		int maxLength = 0;
		for(int i=0;i<words.length;i++){
			maxLength = Math.max(maxLength, words[i].length());
		}
		Log.v("maxLength "+maxLength,"words "+words.length);
		setPrevWidth(maxLength*15+getPaddingLeft()+getPaddingRight());
		setPrevHeight(words.length*35+getPaddingTop()+getPaddingBottom());
		requestLayout();
	}

}
