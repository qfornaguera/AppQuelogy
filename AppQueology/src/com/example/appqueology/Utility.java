package com.example.appqueology;

import java.util.ArrayList;
import android.graphics.PointF;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 
 * @author Joaquim Fornaguera
 * 
 * This class contains all useful methods to run the application
 *
 */
public class Utility {
	/**
	 * posX is the offset X coordinate where an artifact must stand
	 */
	static float nodeCounter,posX;
	
	/**
	 * beautifygraph is the method that sort on positions the artifacts of the board(Rel) 
	 * @param Rel
	 */
	public static void beautifygraph(RelativeLayout Rel){
		nodeCounter = 0;
		ArrayList<Artifact> nodeList = new ArrayList<Artifact>();
		for(int i=0;i<Rel.getChildCount();i++){//at first we pick up the artifacts that are root of a graph
			if(Rel.getChildAt(i).getTag() != null){
				if(Rel.getChildAt(i).getTag().toString().compareTo("node") == 0){
					Artifact node = (Artifact)Rel.getChildAt(i);
					if(node.getFather() == null)
						nodeList.add(node);
				}
			}	
		}
		posX = 0;
		for(int j=0;j<nodeList.size();j++){//then for every root artifact we set the node position
			Artifact node = nodeList.get(j);
			setNodePosition(node,j,1);
			posX = posX + 1000;
		}
		recalculateLines(Rel);
		
	}
	
	/**
	 * setNodePosition
	 * 
	 * this method calculates coordinates of an artifact depending on the offset the number of sons and the depth of the artifact
	 * on the graph
	 * 
	 * @param node is the current artifact
	 * @param treeNum 
	 * @param level is the depth of the current artifact
	 */
	public static void setNodePosition(Artifact node,int treeNum,int level){		
		ArrayList<Artifact> sons = node.getSons();
		for(int i=0;i<sons.size();i++){//we iterate the sons
			setNodePosition(sons.get(i),treeNum,level+1);//and call to set the positions of the sons
			if((sons.size()-1)/2 == i){//if it was the middle son
				if(sons.size()%2 != 0){//if the number of sons is odd put the artifact at the same offset of his middle son
					if(sons.size() == 1){
						node.setX(sons.get(i).getX()-(node.width-sons.get(i).width)/2);//align in case of just one son (this puts father and son at the same center X)
					}else{
						node.setX(sons.get(i).getX());
					}
				}else{//else increment the offset and set the x position of the artifact
					posX = posX + 200;
					node.setX(posX);
				}
			}
		}
		if(sons.size() == 0){//if the artifact has no sons increment the offset and set x position of the artifact
			posX = posX + 200;
			node.setX(posX);
		}
		node.setY(level*400);//set the Y position depending on the depth of the artifact on the graph

	}
	
	/**
	 * recalculateLines
	 * 
	 * this method remove and recalculate the lines between an artifact and it's father
	 * 
	 * @param Rel
	 */
	public static void recalculateLines(RelativeLayout Rel){
		
		while(Rel.findViewWithTag("line") != null){//first we remove all the lines
			Rel.removeView(Rel.findViewWithTag("line"));
		}
		
		ArrayList<Artifact> nodeList = new ArrayList<Artifact>();
		for(int i=0;i<Rel.getChildCount();i++){
			if(Rel.getChildAt(i).getTag() != null){
				if(Rel.getChildAt(i).getTag().toString().compareTo("node") == 0){//then we get all the artifact views of the board
					nodeList.add((Artifact)Rel.getChildAt(i));
					//nodeList.get(nodeList.size()-1).seekFather(Rel);
				}
			}
			
		}
		
		for(int j=0;j<nodeList.size();j++){//then we iterate and create the lines between artifacts
				Artifact node = nodeList.get(j);
				if(node.getFather() != null){
					Line line = new Line(Rel.getContext(),new PointF(node.getCenterX(),node.getCenterY()),new PointF(node.getFather().getCenterX(),node.getFather().getCenterY()));
					line.setTag("line");
					Rel.addView(line);
					node.bringToFront();
					node.getFather().bringToFront();
					View slideDrawer = ((View) Rel.getParent()).findViewById(R.id.slidingDrawer);//ensure the SlideDrawer overlaps all the views
					slideDrawer.bringToFront();
				}
		}
		
	}

}
