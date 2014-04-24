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

/**
 * 
 * @author Joaquim Fornaguera
 * 
 * this class is a custom text view that represents a node of a graph
 *
 */

public class Artifact extends TextView{
	/**sons field is a list with the sons of this node */
	ArrayList <Artifact> sons = null;
	/**father is the node's father node (is null when node is root of a tree */
	Artifact father = null;
	float width,heigth;
	
	/**
	 * Class constructor sets the artifact with center text align and an empty list of sons
	 * @param context
	 **/
	public Artifact(Context context) {
		super(context);
		sons = new ArrayList<Artifact>();
		Global.touchedArtifact = this;
		this.setOnTouchListener(new OnTouchArtifact());
		this.setTextColor(Color.BLACK);
		this.setPadding(10,10,10,10);
		this.setGravity(Gravity.CENTER);
		
	}
	
	/**
	 * setPrevWidth 
	 * 
	 * sets the width of the artifact, this is needed so, the width in layoutParams hasn't been set until activity draw method is launched
	 * and we need width and height to set correctly the node position
	 * @param width
	 */
	public void setPrevWidth(float width){
		this.width = width;
		getLayoutParams().width = (int) width;
	}
	
	/**
	 * setPrevHeight
	 * 
	 * sets the height of an artifact
	 * @param heigth
	 */
	public void setPrevHeight(float heigth){
		this.heigth = heigth;
		getLayoutParams().height = (int) heigth;
	}
	
	/**
	 * getCenterX
	 * 
	 * Gets the center x position of an artifact
	 * @return
	 */
	public float getCenterX(){
		return this.getX() + this.width/2;
	}
	
	/**
	 * getCenterY
	 * 
	 * Gets the center Y position of an artifact
	 * @return
	 */
	public float getCenterY(){
		return this.getY() + this.heigth/2;
	}
	
	/**
	 * setFather
	 * 
	 * sets the father of a node
	 * 
	 * @param father
	 */
	public void setFather(Artifact father){
		this.father = father;
	}
	
	/**
	 * addSon
	 * 
	 * adds a son to sons list
	 * 
	 * @param son
	 */
	public void addSon(Artifact son){
		sons.add(son);
	}
	
	/**
	 * removeAllSons
	 * 
	 * empties the sons list of an artifact
	 * 
	 */
	public void removeAllSons(){
		sons = new ArrayList<Artifact>();
	}
	
	/**
	 * getFather
	 * 
	 * returns the artifact that is father of this artifact
	 * 
	 * @return
	 */
	public Artifact getFather(){
		return father;
	}
	
	/**
	 * getSons
	 * 
	 * returns the list of sons of an artifact
	 * @return
	 */
	public ArrayList<Artifact> getSons(){
		return sons;
	}
	
	/**
	 * removeSon
	 * 
	 * Removes son by index
	 * @param index
	 */
	public void removeSon(int index){
		sons.remove(index);
	}
	
	/**
	 * removeSon
	 * 
	 * removes son by object
	 * 
	 * @param son
	 */
	public void removeSon(Artifact son){
		sons.remove(son);
	}
	
	
	/**
	 * seekFather
	 * 
	 * seeks its father by distance checking all the other artifacts on the board (Rel)
	 * 
	 * @param Rel is the board where the artifact is seeking his father
	 */
	public boolean seekFather(RelativeLayout Rel){
		Artifact father = null;
		float minDist = Float.MAX_VALUE;
		for(int i=0;i<Rel.getChildCount();i++){
			View maybeFather = Rel.getChildAt(i);
			if(maybeFather.getTag() != null){
				if(maybeFather.getTag().toString().compareTo("node") == 0 && this != maybeFather){//if the possible father is a node and it's not itself
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
		
		if(father!=null){//if a father has been found
			if(!McFlyYouHadOneJob(father)){
				return false;
			}
			if(this.father != null)//and if the artifact has a current father
				this.father.removeSon(this);//report the father to remove this artifact from his son list
			
			if(!father.getSons().contains(this))//if the father does not contain already this artifact in his sons list
				father.addSon(this);
		}

		this.father = father;
		return true;
		
	}
	
	/**
	 * kill
	 * 
	 * this method makes the artifact to tell sons to find a new father
	 * and and tell it's father to remove this artifact from sons list
	 * then the lines are recalculated
	 * 
	 * @param Rel is the board where the artifact is
	 */
	public void kill(RelativeLayout Rel){
		ArrayList <Artifact> aux = (ArrayList<Artifact>) sons.clone();
		for(int i = 0;i<aux.size();i++){
			aux.get(i).seekFather(Rel);
		}
		
		if(this.father != null)//and if the artifact has a current father
			this.father.removeSon(this);//report the father to remove this artifact from his son list
		
		Utility.recalculateLines(Rel);
		removeAllSons();
	}
	
	/**
	 * matchWithText
	 * 
	 * Changes the with and the height of the artifact to match with the inner text
	 */
	public void matchWithText(){
		String text = (String) getText();
		String[] words = text.split(" ");
		int maxLength = 0;
		for(int i=0;i<words.length;i++){
			maxLength = Math.max(maxLength, words[i].length());
		}
		setPrevWidth(maxLength*16+getPaddingLeft()+getPaddingRight());
		setPrevHeight(words.length*35+getPaddingTop()+getPaddingBottom());
		requestLayout();
	}
	
	/**
	 * McFlyYouHadOneJob
	 * 
	 * avoids that a node could be father of a node already related with. this is made to avoid a infinite buckle at beautify
	 * recursion
	 * 
	 * @return returns True if node and maybeFather are not related or if is a node without siblings, returns False if yes 
	 */
	
	public boolean McFlyYouHadOneJob(Artifact maybeFather){
		if(this.getSons().size() == 0)
			return true;
		Artifact next = maybeFather.getFather();
		while(next != null){
			if(this == next){
				return false;
			}
			next = next.getFather();
		}
		
		return true;
	}

}
