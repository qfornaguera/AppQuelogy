package com.example.appqueology;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
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
		
	}
	
	public void setPrevWidth(float width){
		this.width = width;
	}
	
	public void setPrevHeight(float heigth){
		this.heigth = heigth;
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
		
		//letSonsDown(Rel);
		
	}
	
	
	/*public void letSonsDown(RelativeLayout Rel){
		ArrayList <Artifact> sonsToLetDown = this.sons;
		removeAllSons();
		for(int i = 0;i<sonsToLetDown.size();i++){
			sonsToLetDown.get(i).seekFather(Rel);
		}
		
	}*/

}
